package springrest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private int id;

  @NotBlank
  @Size(min = 4, max = 20)
  private String username;

  @NotBlank
  @Size(min = 5, max = 60)
  private String password;

  @NotBlank
  @Size(max = 50)
  private String firstname;

  @NotBlank
  @Size(max = 50)
  private String lastname;

  @NotBlank
  @Size(max = 100)
  @Email
  private String email;

  @NotBlank
  @Size(max = 100)
  private String company;

  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_role",
          joinColumns = @JoinColumn(name = "id_user"),
          inverseJoinColumns = @JoinColumn(name = "id_role"))
  private Set<Role> roles = new HashSet<>();

  @JsonIgnoreProperties(value = {"name", "datetime", "display", "users"})
  @ManyToMany(mappedBy = "users")
  private Set<Meeting> meetings = new HashSet<>();

  private LocalDateTime lastUpdated;

}
