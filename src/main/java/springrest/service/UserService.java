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

  public boolean changeUser(EditUser editUser) {
    System.out.println(editUser);
    if ((editUser.getCurrentPassword() == null && editUser.getNewPassword() == null) || checkPassword(editUser.getId(), editUser.getCurrentPassword())) {
      User user = userRepository.findById(editUser.getId()).orElse(null);
      user = mapUser(user, editUser);
      userRepository.save(user);
      return true;
    }
    return false;
  }

  public boolean checkPassword(int id, String password) {
    User user = userRepository.findById(id).orElse(null);
    if (user != null) {
      return encoder.matches(password, user.getPassword());
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
}
