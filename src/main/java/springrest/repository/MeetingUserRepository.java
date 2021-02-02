package springrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springrest.entity.MeetingUser;

import javax.transaction.Transactional;
import java.util.Optional;

public interface MeetingUserRepository extends JpaRepository<MeetingUser, Integer> {

  Optional<MeetingUser> findByIdUserAndIdMeeting(int userId, int meetingId);

  @Transactional
  void deleteByIdUserAndIdMeeting(int userId, int meetingId);

}
