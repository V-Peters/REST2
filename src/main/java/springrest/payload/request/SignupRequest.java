package springrest.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
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

  private Set<String> role;
}
