package springrest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springrest.payload.request.EditPasswordRequest;
import springrest.payload.request.EditUserRequest;
import springrest.payload.response.dto.AuthorDTO;
import springrest.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin(origins = {"${CROSS_ORIGIN}"})
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/checkIfUsernameExists")
  public boolean checkIfUsernameExists(@RequestBody String username) {
    return userService.existsByUsername(username);
  }

  @PostMapping("/checkIfEmailExists")
  public boolean checkIfEmailExists(@RequestBody String email) {
    return userService.existsByEmail(email);
  }

  @PreAuthorize("hasRole('USER')")
  @PostMapping("/editUser")
  public boolean editUser(HttpServletRequest request, @Valid @RequestBody EditUserRequest editUserRequest) {
    return userService.editUser(request, editUserRequest);
  }

  @PreAuthorize("hasRole('USER')")
  @PostMapping("/editPassword")
  public boolean editPassword(HttpServletRequest request, @Valid @RequestBody EditPasswordRequest editPasswordRequest) {
    return userService.editPassword(request, editPasswordRequest);
  }

  @PreAuthorize("hasRole('USER')")
  @DeleteMapping("/")
  public boolean deleteUser(HttpServletRequest request) {
    return userService.deleteUser(request);
  }

  @GetMapping("/author/{id}")
  public AuthorDTO getAuthor(@PathVariable int id) {
    return userService.getAuthor(id);
  }
}
