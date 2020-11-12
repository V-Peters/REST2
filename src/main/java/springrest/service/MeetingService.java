package springrest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springrest.entity.Meeting;
import springrest.entity.MeetingUser;
import springrest.entity.User;
import springrest.repository.MeetingRepository;
import springrest.repository.MeetingUserRepository;
import springrest.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MeetingService {

  @Autowired
  private MeetingRepository meetingRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MeetingUserRepository meetingUserRepository;

  public List<Meeting> getMeetings(HttpServletRequest request) {
    if (request.isUserInRole("ADMIN")) {
      return meetingRepository.findAll();
    } else {
      return meetingRepository.findByDisplayTrue();
    }
  }

  public Meeting getMeeting(int id) {
    return meetingRepository.findById(id).orElse(null);
  }

  public Meeting saveMeeting(Meeting meeting) {
    meeting.setLastUpdated(LocalDateTime.now());
    return meetingRepository.save(meeting);
  }

  public Meeting deleteMeeting(int meetingId) {
    meetingRepository.deleteById(meetingId);
    return this.getMeeting(meetingId);
  }

  public void changeDisplay(int id, boolean display) {
    Meeting meeting = this.getMeeting(id);
    meeting.setDisplay(display);
    this.saveMeeting(meeting);
  }

  public List<User> getParticipants(int id) {
    List<MeetingUser> meetingUserList = meetingUserRepository.findByIdMeeting(id);
    List<User> users = new ArrayList<>();

    for (MeetingUser tempMeetingUser : meetingUserList) {
      users.add(userRepository.findById((tempMeetingUser.getIdUser())));
    }
    return users;
  }

  public List<MeetingUser> getMeetingsSignedUpToForUser(int id) {
    return meetingUserRepository.findByIdUser(id);
  }

  public void updateSignup(Map<String, Boolean> signUps, int userId) {
    for (Object meetingId : signUps.keySet().toArray()) {
      if (meetingUserRepository.findByIdUserAndIdMeeting(userId, Integer.parseInt(meetingId.toString())).isPresent() && !signUps.get(meetingId)) {
        meetingUserRepository.deleteByIdUserAndIdMeeting(userId, Integer.parseInt(meetingId.toString()));
      } else if (!meetingUserRepository.findByIdUserAndIdMeeting(userId, Integer.parseInt(meetingId.toString())).isPresent() && signUps.get(meetingId)) {
        meetingUserRepository.save(new MeetingUser(Integer.parseInt(meetingId.toString()), userId));
      }
    }
  }
}