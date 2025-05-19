package com.meetingBuddy.meetingBuddy.controller;

import com.meetingBuddy.meetingBuddy.dto.AnalyticsDto;
import com.meetingBuddy.meetingBuddy.dto.ResponseDto;
import com.meetingBuddy.meetingBuddy.entity.Meetings;
import com.meetingBuddy.meetingBuddy.service.MeetingService;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class MeetingController {
    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @CrossOrigin
    @PostMapping("/join-meeting")
    public ResponseEntity<ResponseDto> joinBotsToMeeting(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Meetings meetingInfo) {
        try {
            Object joinedMeetingInfo = this.meetingService
                    .joinAMeeting(meetingInfo);
            return new ResponseEntity<>(new ResponseDto(true, joinedMeetingInfo, ""), HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ResponseDto(false, "", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @GetMapping("/refill-meeting/{id}")
    public ResponseEntity<ResponseDto> refillBotsToMeeting(@PathVariable Long id) {
        try {
            Object joinedMeetingInfo = this.meetingService
                    .refillMeeting(id);
            return new ResponseEntity<>(new ResponseDto(true, joinedMeetingInfo, "Refill Request Accepted"), HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ResponseDto(false, "", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @GetMapping("/meetings/today")
    public ResponseEntity<ResponseDto> todaysMeeting() {
        try {
            List<Meetings> meetingsList = meetingService.todaysMeeting();
            return new ResponseEntity<>(new ResponseDto(true, meetingsList, ""), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ResponseDto(false, "", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @GetMapping("/meetings/meeting/analytics/{id}")
    public ResponseEntity<ResponseDto> analyticsMeeting(@PathVariable Long id) {
        try {
            AnalyticsDto analyticsDto = meetingService.fetchMeetingAnalytics(id);
            return new ResponseEntity<>(new ResponseDto(true, analyticsDto, ""), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ResponseDto(false, "", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/export/today-meetings")
    public ResponseEntity<byte[]> exportTodayMeetingsAsCsv() {
        List<Meetings> meetings = meetingService.todaysMeeting();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        writer.println("ID,Meeting ID,Passcode,Provider,Bot Users Count,Created At,Timeout,Locale");

        for (Meetings meeting : meetings) {
            writer.printf(
                    "%d,%s,%s,%s,%d,%s,%d,%s%n",
                    meeting.getId(),
                    meeting.getMeetingId(),
                    meeting.getPasscode(),
                    meeting.getMeetingProvider(),
                    meeting.getBotUsersCount(),
                    meeting.getCreatedAt(),
                    meeting.getTimeout(),
                    meeting.getLocale()
            );
        }

        writer.flush();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=today_meetings.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(out.toByteArray());
    }
}
