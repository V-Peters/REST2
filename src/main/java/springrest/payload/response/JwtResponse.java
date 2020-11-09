package springrest.payload.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class JwtResponse {
  private String accessToken;
  private String type = "Bearer";
  private int id;
  private String username;
  private String firstname;
  private String lastname;
  private String email;
  private String company;
  private List<String> roles;

  public JwtResponse(String accessToken, int id, String username, String firstname, String lastname, String email, String company, List<String> roles) {
    this.accessToken = accessToken;
    this.id = id;
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.company = company;
    this.roles = roles;

  }
}
