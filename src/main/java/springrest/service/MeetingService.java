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
import java.util.*;

@Service
public class MeetingService {

  @Autowired
  private MeetingRepository meetingRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MeetingUserRepository meetingUserRepository;

  public List<Meeting> getMeetings(HttpServletRequest request) {
    List<Meeting> meetings;
    if (request.isUserInRole("ADMIN")) {
      meetings = meetingRepository.findAll();
    } else {
      meetings = meetingRepository.findByDisplayTrue();
      for (Meeting meeting: meetings) {
        meeting.setUsers(new HashSet<>());
      }
    }
      return meetings;
  }

  public Meeting getMeeting(int id) {
    Meeting meeting = meetingRepository.findById(id).orElse(null);
    if (meeting != null) {
      for (User user: meeting.getUsers()){
        user.setPassword(null);
      }
    }
    return meeting;
  }

  public Meeting saveMeeting(Meeting meeting) {
    meeting.setLastUpdated(LocalDateTime.now());
    return meetingRepository.save(meeting);
  }

  public Meeting deleteMeeting(int meetingId) {
    meetingRepository.deleteById(meetingId);
    return this.getMeeting(meetingId);
  }

  public boolean updateDisplay(Map<String, Boolean> displays) {
    for (Object display : displays.keySet().toArray()) {
      Meeting meeting = meetingRepository.findById(Integer.parseInt(display.toString())).orElse(null);
      if (meeting == null) {
        return false;
      }
      meeting.setDisplay(displays.get(display));
      this.saveMeeting(meeting);
    }
    return true;
  }

  public boolean updateSignup(Map<String, Boolean> signUps, int userId) {
    for (Object meetingId : signUps.keySet().toArray()) {
      if (meetingUserRepository.findByIdUserAndIdMeeting(userId, Integer.parseInt(meetingId.toString())).isPresent() && !signUps.get(meetingId)) {
        meetingUserRepository.deleteByIdUserAndIdMeeting(userId, Integer.parseInt(meetingId.toString()));
      } else if (!meetingUserRepository.findByIdUserAndIdMeeting(userId, Integer.parseInt(meetingId.toString())).isPresent() && signUps.get(meetingId)) {
        meetingUserRepository.save(new MeetingUser(Integer.parseInt(meetingId.toString()), userId));
      }
    }
    return true;
  }

  public User getUser(int id) {
    User tempUser = userRepository.findById(id).orElse(null);
    if (tempUser != null) {
      tempUser.setPassword(null);
    }
    return tempUser;
  }
}
