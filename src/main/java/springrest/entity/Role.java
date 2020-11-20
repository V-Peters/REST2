package springrest.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ERole name;

  public Role(ERole name) {
    this.name = name;
  }

  public Role(List<String> roles) {
    System.out.println("ROLES: " + roles);
  }
  public static Set<Role> convertStringSetToRoleSetWithStreams(List<String> roles) {
    Set<String> rolesSet = new HashSet<>(roles);

    return rolesSet.stream().map(roleInString -> {
      final Role role = new Role();
      role.setName(ERole.valueOf(roleInString));
      return role;
    }).collect(Collectors.toSet());
  }

}
