package springrest.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "meeting_user")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class MeetingUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "id_meeting")
  private int meetingId;

  @Column(name = "id_user")
  private int userId;

  public MeetingUser(int meetingId, int userId) {
    this.meetingId = meetingId;
    this.userId = userId;
  }

}
