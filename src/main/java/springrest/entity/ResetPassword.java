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
  private int id;

//  @NotBlank
  private int idUser;

  @Size(min = 255, max = 255)
  @NotBlank
  private String secret;

//  @NotBlank
  private LocalDateTime validUntil;
}
