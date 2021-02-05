package springrest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springrest.entity.User;
import springrest.payload.request.EditPasswordRequest;
import springrest.payload.request.EditUserRequest;
import springrest.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder encoder;

  public Boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  public Boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public boolean checkPassword(HttpServletRequest request) {
    User user = findByUsernameFromRequest(request);
    if (user != null) {
      return encoder.matches(request.getHeader("password"), user.getPassword());
    }
    return false;
  }

  public User findByUsernameFromRequest(HttpServletRequest request) {
    return userRepository.findByUsername(request.getRemoteUser()).orElseThrow(RuntimeException::new);
  }

  public boolean editUser(HttpServletRequest request, EditUserRequest editUserRequest) {
    User user = findByUsernameFromRequest(request);
    user.setFirstname(editUserRequest.getFirstname());
    user.setLastname(editUserRequest.getLastname());
    user.setEmail(editUserRequest.getEmail());
    user.setCompany(editUserRequest.getCompany());
    user.setLastUpdated(LocalDateTime.now());
    userRepository.save(user);
    return true;
  }

  public boolean editPassword(HttpServletRequest request, EditPasswordRequest editPasswordRequest) {
    User user = findByUsernameFromRequest(request);
    if (encoder.matches(editPasswordRequest.getCurrentPassword(), user.getPassword()) && editPasswordRequest.getNewPassword().equals(editPasswordRequest.getNewPasswordCheck())){
      user.setPassword(encoder.encode(editPasswordRequest.getNewPassword()));
      user.setLastUpdated(LocalDateTime.now());
      userRepository.save(user);
      return true;
    }
    return false;
  }

  public boolean deleteUser(HttpServletRequest request) {
    if (checkPassword(request)) {
      userRepository.deleteById(userRepository.findByUsername(request.getRemoteUser()).orElseThrow(RuntimeException::new).getId());
      return true;
    }
    return false;
  }
}
