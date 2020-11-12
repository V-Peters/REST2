package springrest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springrest.entity.Meeting;
import springrest.entity.MeetingUser;
import springrest.entity.User;
import springrest.service.MeetingService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/meetings")
public class MeetingController {

  @Autowired
  private MeetingService meetingService;

  @GetMapping()
  @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
  public List<Meeting> getMeetings(HttpServletRequest request) {
    return meetingService.getMeetings(request);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public Meeting getMeeting(@PathVariable int id) {
    return meetingService.getMeeting(id);
  }

  @PostMapping()
  @PreAuthorize("hasRole('ADMIN')")
  public Meeting saveMeeting(@RequestBody Meeting meeting) {
    return meetingService.saveMeeting(meeting);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public Meeting deleteMeeting(@PathVariable int id) {
    return meetingService.deleteMeeting(id);
  }

  @PostMapping("/updateDisplay")
  @PreAuthorize("hasRole('ADMIN')")
  public boolean updateDisplay(@RequestBody Map<String, Boolean> displays) {
    for (Object display : displays.keySet().toArray()) {
      meetingService.changeDisplay(Integer.parseInt(display.toString()), displays.get(display));
    }
    return true;
  }

  @GetMapping("/listParticipants/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public List<User> listParticipants(@PathVariable int id) {
    return meetingService.getParticipants(id);
  }

  @GetMapping("/getMeetingsSignedUpToForUser/{id}")
  @PreAuthorize("hasRole('USER')")
  public List<MeetingUser> getMeetingsSignedUpToForUser(@PathVariable int id) {
    return meetingService.getMeetingsSignedUpToForUser(id);
  }

  @PostMapping("/updateSignup/{userId}")
  @PreAuthorize("hasRole('USER')")
  public boolean updateSignup(@RequestBody Map<String, Boolean> signUps, @PathVariable int userId) {
    meetingService.updateSignup(signUps, userId);
    return true;
  }

}