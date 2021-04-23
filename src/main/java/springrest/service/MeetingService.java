package springrest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springrest.entity.Meeting;
import springrest.entity.MeetingUser;
import springrest.entity.User;
import springrest.repository.MeetingRepository;
import springrest.repository.MeetingUserRepository;
import springrest.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MeetingService {

  private final MeetingRepository meetingRepository;
  private final UserRepository userRepository;
  private final MeetingUserRepository meetingUserRepository;

  public boolean checkIfNameExists(String name) {
    return meetingRepository.existsByName(name);
  }

  public List<Meeting> getMeetings(HttpServletRequest request) {
    List<Meeting> meetings;
    if (request.isUserInRole("ADMIN")) {
      meetings = meetingRepository.findAll();
    } else {
      meetings = meetingRepository.findByDisplayTrue();
      for (Meeting meeting : meetings) {
        meeting.setUsers(new HashSet<>());
      }
    }
    return meetings;
  }

  public Meeting getMeeting(int id) {
    Meeting meeting = meetingRepository.findById(id).orElseThrow(RuntimeException::new);
    if (meeting != null) {
      for (User user : meeting.getUsers()) {
        user.setPassword(null);
      }
    }
    return meeting;
  }

  public Meeting saveMeeting(HttpServletRequest request, Meeting meeting) {
    if (meeting.getAuthorId() == 0){
      meeting.setAuthorId(userRepository.findByUsername(request.getRemoteUser()).orElseThrow(RuntimeException::new).getId());
    }
    meeting.setLastUpdated(LocalDateTime.now());
    if (meetingRepository.findById(meeting.getId()).isPresent()) {
      meeting.setUsers(meetingRepository.findById(meeting.getId()).get().getUsers());
    }
    return meetingRepository.save(meeting);
  }

  public Meeting deleteMeeting(int meetingId) {
    meetingRepository.deleteById(meetingId);
    return this.getMeeting(meetingId);
  }

  public boolean updateDisplay(Map<String, Boolean> displays) {
    for (Object display : displays.keySet().toArray()) {
      Meeting meeting = meetingRepository.findById(Integer.parseInt(display.toString())).orElseThrow(RuntimeException::new);
      if (meeting == null) {
        return false;
      }
      meeting.setDisplay(displays.get(display));
      this.saveMeeting(null, meeting);
    }
    return true;
  }

  public boolean updateSignup(HttpServletRequest request, Map<String, Boolean> signUps) {
    User user = userRepository.findByUsername(request.getRemoteUser()).orElseThrow(RuntimeException::new);
    if (user == null) {
      return false;
    }
    int userId = user.getId();
    for (Object meetingId : signUps.keySet().toArray()) {
      if (meetingUserRepository.findByUserIdAndMeetingId(userId, Integer.parseInt(meetingId.toString())).isPresent() && !signUps.get(meetingId)) {
        meetingUserRepository.deleteByUserIdAndMeetingId(userId, Integer.parseInt(meetingId.toString()));
      } else if (!meetingUserRepository.findByUserIdAndMeetingId(userId, Integer.parseInt(meetingId.toString())).isPresent() && signUps.get(meetingId)) {
        meetingUserRepository.save(new MeetingUser(Integer.parseInt(meetingId.toString()), userId));
      }
    }
    return true;
  }

  public User getUser(int id) {
    return userRepository.findById(id).map(user -> {
      user.setPassword(null);
      return user;
    }).orElseThrow(RuntimeException::new);
  }
}
