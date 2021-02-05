package springrest.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RegisterRequest {

  @NotBlank
  @Size(min = 4, max = 20)
  private String username;

  @NotBlank
  @Size(min = 5, max = 60)
  private String password;

  @NotBlank
  @Size(min = 5, max = 60)
  private String passwordCheck;

  @NotBlank
  @Size(max = 50)
  private String firstname;

  @NotBlank
  @Size(max = 50)
  private String lastname;

  @NotBlank
  @Email
  @Size(max = 100)
  private String email;

  @NotBlank
  @Size(max = 100)
  private String company;
}
