package springrest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springrest.payload.request.ForgotPasswordRequest;
import springrest.payload.request.LoginRequest;
import springrest.payload.request.RegisterRequest;
import springrest.payload.request.SetNewPasswordRequest;
import springrest.payload.response.dto.LoginDTO;
import springrest.service.AuthService;
import springrest.service.EmailService;
import springrest.service.ResetPasswordService;
import springrest.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin(origins = {"${CROSS_ORIGIN}"})
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final EmailService emailService;
  private final ResetPasswordService resetPasswordService;
  private final AuthService authService;
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<LoginDTO> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
    return authService.login(loginRequest);
  }

  @PostMapping("/register")
  public boolean registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
    if (!(userService.existsByUsername(registerRequest.getUsername()) || userService.existsByEmail(registerRequest.getEmail())) && registerRequest.getPassword().equals(registerRequest.getPasswordCheck())) {
      authService.save(registerRequest);
      return userService.existsByUsername(registerRequest.getUsername());
    }
    return false;
  }

  @PostMapping("/forgotPassword")
  public boolean forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
    if (authService.matchesUsernameAndEmail(forgotPasswordRequest) && !resetPasswordService.existsById(authService.convertUsernameToId(forgotPasswordRequest.getUsername()))) {
      return emailService.sendEmail(authService.convertUsernameToId(forgotPasswordRequest.getUsername()), forgotPasswordRequest.getUsername(), forgotPasswordRequest.getEmail());
    }
    return false;
  }

  @PostMapping("/setNewPassword")
  public boolean setNewPassword(HttpServletRequest request, @Valid @RequestBody SetNewPasswordRequest setNewPasswordRequest) {
    return authService.setNewPassword(request, setNewPasswordRequest);
  }
}
