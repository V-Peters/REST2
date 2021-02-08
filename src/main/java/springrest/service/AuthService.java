package springrest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springrest.payload.request.ForgotPasswordRequest;
import springrest.payload.request.LoginRequest;
import springrest.payload.request.RegisterRequest;
import springrest.payload.request.SetNewPasswordRequest;
import springrest.payload.response.dto.LoginDTO;
import springrest.entity.ERole;
import springrest.entity.Role;
import springrest.entity.User;
import springrest.repository.RoleRepository;
import springrest.repository.UserRepository;
import springrest.security.jwt.JWTUtils;
import springrest.security.services.UserDetailsImpl;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final ResetPasswordService resetPasswordService;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authenticationManager;
  private final JWTUtils jwtUtils;
  private final UserService userService;

  @Value("${TYPE}")
  private String type;

  public ResponseEntity<LoginDTO> login(LoginRequest loginRequest) {
    String username = "";
    if (userService.existsByUsername(loginRequest.getUsernameOrEmail())){
      username = loginRequest.getUsernameOrEmail();
    } else if (userService.existsByEmail(loginRequest.getUsernameOrEmail())) {
      username = userService.findUsernameByEmail(loginRequest.getUsernameOrEmail());
    }
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining());

    return ResponseEntity.ok(new LoginDTO(type, jwt, role, userDetails.getId(), userDetails.getUsername(), userDetails.getFirstname(), userDetails.getLastname(), userDetails.getEmail(), userDetails.getCompany()));
  }

  public void save(RegisterRequest registerRequest) {
    User user = new User();
    user.setId(0);
    user.setUsername(registerRequest.getUsername());
    user.setPassword(encoder.encode(registerRequest.getPassword()));
    user.setFirstname(registerRequest.getFirstname());
    user.setLastname(registerRequest.getLastname());
    user.setEmail(registerRequest.getEmail());
    user.setCompany(registerRequest.getCompany());

    Set<Role> roles = new HashSet<>();
    Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));

    roles.add(userRole);
    user.setRoles(roles);

    user.setLastUpdated(LocalDateTime.now());

    userRepository.save(user);
  }

  public boolean matchesUsernameAndEmail(ForgotPasswordRequest forgotPasswordRequest) {
    if (userService.existsByUsername(forgotPasswordRequest.getUsername())){
      return forgotPasswordRequest.getEmail().equals(userRepository.findByUsername(forgotPasswordRequest.getUsername()).orElseThrow(RuntimeException::new).getEmail());
    }
    return false;
  }

  public int convertUsernameToId(String username) {
    return userRepository.findByUsername(username).orElseThrow(RuntimeException::new).getId();
  }

  public boolean setNewPassword(HttpServletRequest request, SetNewPasswordRequest setNewPasswordRequest) {
    String rps = request.getHeader("rps");
    if (rps != null && resetPasswordService.existsByResetPasswordSecret(rps)) {
      int userId = resetPasswordService.getUserId(rps);
      User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
      user.setPassword(encoder.encode(setNewPasswordRequest.getPassword()));
      userRepository.save(user);
      resetPasswordService.deleteResetPasswordSecret(rps);
      return true;
    }
    return false;
  }
}
