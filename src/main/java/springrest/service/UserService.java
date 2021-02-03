package springrest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springrest.entity.ERole;
import springrest.entity.Role;
import springrest.entity.User;
import springrest.payload.request.EditUser;
import springrest.payload.response.UserResponse;
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
public class UserService {

  private final ResetPasswordService resetPasswordService;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authenticationManager;
  private final JWTUtils jwtUtils;

  public Boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  public Boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public ResponseEntity<?> login(User loginUser) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining());

    return ResponseEntity.ok(new UserResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getFirstname(), userDetails.getLastname(), userDetails.getEmail(), userDetails.getCompany(), role));
  }

  public void save(User registerUser) {
    registerUser.setPassword(encoder.encode(registerUser.getPassword()));

    Set<Role> roles = new HashSet<>();
    Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));

    roles.add(userRole);
    registerUser.setRoles(roles);

    registerUser.setLastUpdated(LocalDateTime.now());

    userRepository.save(registerUser);
  }

  public boolean changeUser(HttpServletRequest request, EditUser editUser) {
    if ((editUser.getCurrentPassword() == null && editUser.getNewPassword() == null) || checkPassword(request)) {
      User user = userRepository.findByUsername(request.getRemoteUser()).orElseThrow(RuntimeException::new);
      user = mapUser(user, editUser);
      userRepository.save(user);
      return true;
    }
    return false;
  }

  public boolean checkPassword(HttpServletRequest request) {
    User user = userRepository.findByUsername(request.getRemoteUser()).orElseThrow(RuntimeException::new);
    if (user != null) {
      return encoder.matches(request.getHeader("password"), user.getPassword());
    }
    return false;
  }

  public User mapUser(User currentUser, EditUser newUser) {
    if (newUser.getNewPassword() != null) {
      currentUser.setPassword(encoder.encode(newUser.getNewPassword()));
    }
    currentUser.setFirstname(newUser.getFirstname());
    currentUser.setLastname(newUser.getLastname());
    currentUser.setEmail(newUser.getEmail());
    currentUser.setCompany(newUser.getCompany());
    currentUser.setLastUpdated(LocalDateTime.now());
    return currentUser;
  }

  public boolean deleteUser(HttpServletRequest request) {
    if (checkPassword(request)) {
      userRepository.deleteById(userRepository.findByUsername(request.getRemoteUser()).orElseThrow(RuntimeException::new).getId());
      return checkIfDeleted(request.getRemoteUser());
    }
    return false;
  }

  private boolean checkIfDeleted(String username) {
    return userRepository.findByUsername(username).orElseThrow(RuntimeException::new) == null;
  }

  public boolean matchesUsernameAndEmail(User forgotPassword) {
    if (existsByUsername(forgotPassword.getUsername())){
      return forgotPassword.getEmail().equals(userRepository.findByUsername(forgotPassword.getUsername()).orElseThrow(RuntimeException::new).getEmail());
    }
    return false;
  }

  public int convertUsernameToId(String username) {
    return userRepository.findByUsername(username).orElseThrow(RuntimeException::new).getId();
  }

  public boolean setNewPassword(HttpServletRequest request, User setNewPassword) {
    String rps = request.getHeader("rps");
    if (rps != null && resetPasswordService.existsByResetPasswordSecret(rps)) {
      int userId = resetPasswordService.getUserId(rps);
      User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
      user.setPassword(encoder.encode(setNewPassword.getPassword()));
      userRepository.save(user);
      resetPasswordService.deleteResetPasswordSecret(rps);
      return true;
    }
    return false;
  }
}
