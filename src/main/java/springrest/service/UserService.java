package springrest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springrest.entity.ERole;
import springrest.entity.Role;
import springrest.entity.User;
import springrest.repository.RoleRepository;
import springrest.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

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
}
