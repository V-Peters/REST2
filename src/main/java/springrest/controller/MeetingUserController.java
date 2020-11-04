package springrest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springrest.entity.MeetingUser;
import springrest.entity.User;
import springrest.service.MeetingUserService;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/meetingUser")
public class MeetingUserController {
	
	@Autowired
	private MeetingUserService meetingUserService;
	
	@GetMapping("/listParticipants")
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> listParticipants(@RequestParam int id) {
		return meetingUserService.getParticipants(id);
	}

	@GetMapping("/getMeetingsForUser")
	@PreAuthorize("hasRole('USER')")
	public List<MeetingUser> getMeetingsForUser(@RequestParam int id) {
		return meetingUserService.getMeetingsForUser(id);
	}

	@PostMapping("/updateSignup")
	@PreAuthorize("hasRole('USER')")
	public void updateSignup(@RequestBody Map<String, Boolean> signups, @RequestParam int userId) {
		meetingUserService.updateSignup(signups, userId);
	}
}
