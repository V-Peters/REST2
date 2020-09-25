package springrest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springrest.entity.Meeting;
import springrest.service.MeetingService;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/meetings")
public class MeetingController {
	
	@Autowired
	private MeetingService meetingService;

	@GetMapping("/listAdmin")
	@PreAuthorize("hasRole('ADMIN')")
	public List<Meeting> listMeetingsAdmin() {
		return meetingService.getMeetingsAdmin();
	}

	@GetMapping("/listUser")
	@PreAuthorize("hasRole('USER')")
	public List<Meeting> listMeetingsUser() {
		return meetingService.getMeetingsUser();
	}
	
	@GetMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public Meeting getMeeting(@RequestParam int id) {
		return meetingService.getMeeting(id);
	}
	
	@PostMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public void addMeeting(@RequestBody Meeting meeting) {
		System.out.println("Meeting saved: " + meeting.toString());
		meetingService.saveMeeting(meeting);
	}
	
	@PutMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public void updateMeeting(@RequestBody Meeting meeting) {
		System.out.println("Meeting updated: " + meeting.toString());
		meetingService.saveMeeting(meeting);
	}
	
	@DeleteMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteMeeting(@RequestParam int id) {
		System.out.println("delete in controller - id: " + id);
		meetingService.deleteMeeting(id);
	}
	
	@PostMapping("/updateDisplay")
	@PreAuthorize("hasRole('ADMIN')")
	public void updateDisplay(@RequestBody Map<String, Boolean> displays) {
		for (Object disp : displays.keySet().toArray()) {
			meetingService.changeDisplay(Integer.parseInt(disp.toString()), displays.get(disp));
		}
	}
	
}