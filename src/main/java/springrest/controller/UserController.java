package springrest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springrest.entity.User;
import springrest.payload.request.EditUser;
import springrest.service.EmailService;
import springrest.service.ResetPasswordService;
import springrest.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin(origins = {"${CROSS_ORIGIN}"})
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final EmailService emailService;
  private final ResetPasswordService resetPasswordService;
  private final UserService userService;

  @PostMapping("/checkIfUsernameExists")
  public boolean checkIfUsernameExists(@RequestBody String username) {
    return userService.existsByUsername(username);
  }

  @PostMapping("/checkIfEmailExists")
  public boolean checkIfEmailExists(@RequestBody String email) {
    return userService.existsByEmail(email);
  }

  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@Valid @RequestBody User loginUser) {
    return userService.login(loginUser);
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
    if (userService.matchesUsernameAndEmail(forgotPassword) && !resetPasswordService.existsById(userService.convertUsernameToId(forgotPassword.getUsername()))) {
      return emailService.sendEmail(userService.convertUsernameToId(forgotPassword.getUsername()), forgotPassword.getUsername(), forgotPassword.getEmail());
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
