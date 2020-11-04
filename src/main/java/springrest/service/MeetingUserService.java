package springrest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springrest.entity.MeetingUser;
import springrest.entity.User;
import springrest.repository.MeetingUserRepository;
import springrest.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MeetingUserService {
	
	private MeetingUserRepository meetingUserRepository;
	private UserRepository userRepository;
	
	@Autowired
	public MeetingUserService(MeetingUserRepository meetingUserRepository, UserRepository userRepository) {
		this.meetingUserRepository = meetingUserRepository;
		this.userRepository = userRepository;
	}

	public List<User> getParticipants(int id) {
		List<MeetingUser> meetingUserList = meetingUserRepository.findByIdMeeting(id);
		List<User> users = new ArrayList<>();

		for (MeetingUser tempMeetingUser : meetingUserList) {
			users.add(userRepository.findById((tempMeetingUser.getIdUser())));
		}
		return users;
	}

	public List<MeetingUser> getMeetingsForUser(int id) {
		return meetingUserRepository.findByIdUser(id);
	}

	public void updateSignup(Map<String, Boolean> signups, int userId) {
		for (Object meetingId : signups.keySet().toArray()) {
			if (meetingUserRepository.findByIdUserAndIdMeeting(userId, Integer.parseInt(meetingId.toString())).isPresent() && signups.get(meetingId) == false) {
				meetingUserRepository.deleteByIdUserAndIdMeeting(userId, Integer.parseInt(meetingId.toString()));
			} else if (!meetingUserRepository.findByIdUserAndIdMeeting(userId, Integer.parseInt(meetingId.toString())).isPresent() && signups.get(meetingId) == true){
				meetingUserRepository.save(new MeetingUser(Integer.parseInt(meetingId.toString()), userId));
			}
		}
	}
}
