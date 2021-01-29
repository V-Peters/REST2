package springrest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springrest.entity.User;
import springrest.payload.request.EditUser;
import springrest.payload.response.UserResponse;
import springrest.security.jwt.JwtUtils;
import springrest.security.services.UserDetailsImpl;
import springrest.service.EmailService;
import springrest.service.ResetPasswordService;
import springrest.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"${CROSS_ORIGIN}"})
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final EmailService emailService;
  private final ResetPasswordService resetPasswordService;
  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final JwtUtils jwtUtils;

  @PostMapping("/checkIfUsernameExists")
  public boolean checkIfUsernameExists(@RequestBody String username) {
    return userService.existsByUsername(username);
  }

  @PostMapping("/checkIfEmailExists")
  public boolean checkIfEmailExists(@RequestBody String email) {
    return userService.existsByEmail(email);
  }

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody User loginUser) {

    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String role = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.joining());

    return ResponseEntity.ok(new UserResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getFirstname(), userDetails.getLastname(), userDetails.getEmail(), userDetails.getCompany(), role));
  }

  @PostMapping("/register")
  public boolean registerUser(@Valid @RequestBody User registerUser) {
    if (userService.existsByUsername(registerUser.getUsername()) || userService.existsByEmail(registerUser.getEmail())) {
      return false;
    }
    userService.save(registerUser);
    return true;
  }

  @PostMapping("/forgotPassword")
  public boolean forgotPassword(@Valid @RequestBody User forgotPassword) {
    if (userService.matchesUsernameAndEmail(forgotPassword) && resetPasswordService.existsById(userService.convertUsernameToId(forgotPassword.getUsername()))) {
      emailService.sendEmail(forgotPassword.getUsername(), forgotPassword.getEmail());
      return true;
    }
    return false;
  }

  @PostMapping("/setNewPassword")
  public boolean setNewPassword(HttpServletRequest request, @Valid @RequestBody User setNewPassword) {
    return userService.setNewPassword(request, setNewPassword);
  }

  @PreAuthorize("hasRole('USER')")
  @PostMapping("/changeUser")
  public boolean changeUser(HttpServletRequest request, @Valid @RequestBody EditUser editUser) {
    return userService.changeUser(request, editUser);
  }

  @PreAuthorize("hasRole('USER')")
  @DeleteMapping("/")
  public boolean deleteUser(HttpServletRequest request) {
    return userService.deleteUser(request);
  }
}
