package springrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springrest.entity.MeetingUser;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface MeetingUserRepository extends JpaRepository<MeetingUser, Integer> {

	public List<MeetingUser> findByIdUser(int id);

	public Optional<MeetingUser> findByIdUserAndIdMeeting(int userId, int meetingId);

	public List<MeetingUser> findByIdMeeting(int id);

	@Transactional
	public void deleteByIdUserAndIdMeeting(int userId, int meetingId);

}
