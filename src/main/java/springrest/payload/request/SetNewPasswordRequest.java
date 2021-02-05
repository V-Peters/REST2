package springrest.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SetNewPasswordRequest {

  @NotBlank
  @Size(min = 5, max = 60)
  private String password;

  @NotBlank
  @Size(min = 5, max = 60)
  private String passwordCheck;
}
