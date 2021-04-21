package springrest.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class ForgotPasswordRequest {

  @NotBlank
  @Size(min = 4, max = 20)
  private String username;

  @NotBlank
  @Email
  @Size(max = 100)
  private String email;
}
