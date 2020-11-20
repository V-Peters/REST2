package springrest.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
  private String accessToken;
  private String type = "Bearer";
  private int id;
  private String username;
  private String firstname;
  private String lastname;
  private String email;
  private String company;
  private String role;

  public UserResponse(String accessToken, int id, String username, String firstname, String lastname, String email, String company, String role) {
    this.accessToken = accessToken;
    this.id = id;
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.company = company;
    this.role = role;
  }
}
