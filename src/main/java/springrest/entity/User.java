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
  @Column(name = "id")
  private int id;

  @NotBlank
  @Size(min = 4, max = 20)
  @Column(name = "username")
  private String username;

  @NotBlank
  @Size(min = 5, max = 60)
  @Column(name = "password")
  private String password;

  @NotBlank
  @Size(max = 50)
  @Column(name = "firstname")
  private String firstname;

  @NotBlank
  @Size(max = 50)
  @Column(name = "lastname")
  private String lastname;

  @NotBlank
  @Size(max = 100)
  @Email
  @Column(name = "email")
  private String email;

  @NotBlank
  @Size(max = 100)
  @Column(name = "company")
  private String company;

  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_role",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  @JsonIgnoreProperties(value = {"name", "datetime", "display", "users"})
  @ManyToMany(mappedBy = "users")
  private Set<Meeting> meetings = new HashSet<>();

  @Column(name = "last_updated")
  private LocalDateTime lastUpdated;

}
