package springrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springrest.entity.MeetingUser;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface MeetingUserRepository extends JpaRepository<MeetingUser, Integer> {

  List<MeetingUser> findByIdUser(int id);

  Optional<MeetingUser> findByIdUserAndIdMeeting(int userId, int meetingId);

  List<MeetingUser> findByIdMeeting(int id);

  @Transactional
  void deleteByIdUserAndIdMeeting(int userId, int meetingId);

}
