package springrest.payload.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
  private int id;
  private String username;
  private String firstname;
  private String lastname;
  private String email;
  private String company;
  private String role;
  private List<MeetingDTO> meetings;
}
