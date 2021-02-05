package springrest.payload.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EditPasswordRequest {

  @NotBlank
  @Size(min = 5, max = 60)
  private String currentPassword;

  @NotBlank
  @Size(min = 5, max = 60)
  private String newPassword;

  @NotBlank
  @Size(min = 5, max = 60)
  private String newPasswordCheck;
}
