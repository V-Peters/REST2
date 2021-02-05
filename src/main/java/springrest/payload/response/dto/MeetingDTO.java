package springrest.payload.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MeetingDTO {
  private int id;
  private String name;
  private LocalDateTime datetime;
  private boolean display;
  private List<UserDTO> users;
}
