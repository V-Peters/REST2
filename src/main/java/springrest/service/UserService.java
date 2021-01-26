package springrest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springrest.entity.ERole;
import springrest.entity.Role;
import springrest.entity.User;
import springrest.payload.request.EditUser;
import springrest.repository.RoleRepository;
import springrest.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  public Boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  public Boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public void save(User registerUser) {
    registerUser.setPassword(encoder.encode(registerUser.getPassword()));

    Set<Role> roles = new HashSet<>();
    Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));

    roles.add(userRole);
    registerUser.setRoles(roles);

    userRepository.save(registerUser);
  }

  public boolean changeUser(HttpServletRequest request, EditUser editUser) {
    System.out.println(editUser);
    if ((editUser.getCurrentPassword() == null && editUser.getNewPassword() == null) || checkPassword(request)) {
      User user = userRepository.findByUsername(request.getRemoteUser()).orElse(null);
      user = mapUser(user, editUser);
      userRepository.save(user);
      return true;
    }
    return false;
  }

  public boolean checkPassword(HttpServletRequest request) {
    User user = userRepository.findByUsername(request.getRemoteUser()).orElse(null);
    if (user != null) {
      System.out.println(request.getHeader("password"));
      System.out.println(user.getPassword());
      System.out.println(encoder.matches(request.getHeader("password"), user.getPassword()));
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
      userRepository.deleteById(userRepository.findByUsername(request.getRemoteUser()).orElse(null).getId());
      return checkIfDeleted(request.getRemoteUser());
    }
    return false;
  }

  private boolean checkIfDeleted(String username) {
    return userRepository.findByUsername(username).orElse(null) == null;
  }

  public boolean matchesUsernameAndEmail(User forgotPassword) {
    if (existsByUsername(forgotPassword.getUsername())){
      return forgotPassword.getEmail().equals(userRepository.findByUsername(forgotPassword.getUsername()).orElse(null).getEmail());
    }
    return false;
  }

  public boolean setNewPassword(User setNewPassword) {
    User user = userRepository.findByUsername(setNewPassword.getUsername()).orElse(null);
    user.setPassword(encoder.encode(setNewPassword.getPassword()));
    userRepository.save(user);
    return user.getPassword().equals(userRepository.findByUsername(setNewPassword.getUsername()).orElse(null).getPassword());
  }
}
