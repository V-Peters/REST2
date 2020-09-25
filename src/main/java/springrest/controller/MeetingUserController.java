package springrest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springrest.entity.User;
import springrest.service.MeetingService;
import springrest.service.MeetingUserService;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/meetingUser")
public class MeetingUserController {
	
	@Autowired
	private MeetingUserService meetingUserService;
	
	@Autowired
	private MeetingService meetingService;

	@GetMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> listParticipants(@RequestParam int id) {
		return meetingUserService.getParticipants(id);
	}
/*
	@GetMapping("/signUp")
	public String signUp(@RequestParam(Constants.MEETING_ID) int meetingId, HttpServletRequest request) {

		User userId = (User)request.getSession().getAttribute("user");

		meetingUserService.signOut(userId, meetingId);
		meetingUserService.signUp(userId, meetingId);

	}

	@GetMapping("/signOut")
	public String signOut(@RequestParam(Constants.MEETING_ID) int meetingId, HttpServletRequest request) {

		meetingUserService.signOut((User)request.getSession().getAttribute("user"), meetingId);

	}*/
	
}
