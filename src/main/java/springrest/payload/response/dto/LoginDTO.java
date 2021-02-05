package springrest.payload.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginDTO {

  private String type;
  private String token;
  private String role;
  private int id;
  private String username;
  private String firstname;
  private String lastname;
  private String email;
  private String company;
}
