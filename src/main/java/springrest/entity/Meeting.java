package springrest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "meeting")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Meeting {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "name")
  @Size(max = 100)
  private String name;

  @Column(name = "date_time")
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime datetime;

  @Column(name = "display")
  private boolean display;

  @Column(name = "author_id")
  private int authorId;

  @JsonIgnore
  @Column(name = "last_updated")
  private LocalDateTime lastUpdated;

  @JsonIgnoreProperties(value = {"username", "password", "meetings"})
  @ManyToMany
  @JoinTable(name = "meeting_user",
          joinColumns = @JoinColumn(name = "meeting_id"),
          inverseJoinColumns = @JoinColumn(name = "user_id"))
  private Set<User> users = new HashSet<>();

}
