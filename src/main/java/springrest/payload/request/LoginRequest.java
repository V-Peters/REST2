package springrest.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginRequest {

  @NotBlank
  private String usernameOrEmail;

  @NotBlank
  @Size(min = 5, max = 60)
  private String password;
}
