package springrest.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "reset_password", uniqueConstraints = {@UniqueConstraint(columnNames = "secret")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResetPassword {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @NotBlank
  @Column(name = "user_id")
  private int userId;

  @NotBlank
  @Size(min = 255, max = 255)
  @Column(name = "secret")
  private String secret;

  @NotBlank
  @Column(name = "valid_until")
  private LocalDateTime validUntil;
}
