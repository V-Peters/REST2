package springrest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springrest.entity.Meeting;
import springrest.entity.User;
import springrest.service.MeetingService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"${CROSS_ORIGIN}"})
@RestController
@RequestMapping("/meetings")
@RequiredArgsConstructor
public class MeetingController {

  private final MeetingService meetingService;

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
  public Meeting saveMeeting(HttpServletRequest request, @RequestBody Meeting meeting) {
    return meetingService.saveMeeting(request, meeting);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public Meeting deleteMeeting(@PathVariable int id) {
    return meetingService.deleteMeeting(id);
  }

  @PostMapping("/updateDisplay")
  @PreAuthorize("hasRole('ADMIN')")
  public boolean updateDisplay(@RequestBody Map<String, Boolean> displays) {
    return meetingService.updateDisplay(displays);
  }

  @PostMapping("/updateSignup")
  @PreAuthorize("hasRole('USER')")
  public boolean updateSignup(HttpServletRequest request, @RequestBody Map<String, Boolean> signUps) {
    return meetingService.updateSignup(request, signUps);
  }

  @GetMapping("/getUser/{id}")
  @PreAuthorize("hasRole('USER')")
  public User getUser(@PathVariable int id) {
    return meetingService.getUser(id);
  }
}
