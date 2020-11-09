package springrest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springrest.entity.Meeting;
import springrest.repository.MeetingRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeetingService {

  @Autowired
  private MeetingRepository meetingRepository;

  public List<Meeting> getMeetingsAdmin() {
    return meetingRepository.findAll();
  }

  public List<Meeting> getMeetingsUser() {
    return meetingRepository.findByDisplayTrue();
  }

  public Meeting getMeeting(int id) {
    return meetingRepository.findById(id).orElse(null);
  }

  public void saveMeeting(Meeting meeting) {
    meeting.setLastUpdated(LocalDateTime.now());
    meetingRepository.save(meeting);
  }

  public void deleteMeeting(int meetingId) {
    this.meetingRepository.deleteById(meetingId);
  }

  public void changeDisplay(int id, boolean display) {
    Meeting meeting = this.getMeeting(id);
    meeting.setDisplay(display);
    this.saveMeeting(meeting);
  }
}