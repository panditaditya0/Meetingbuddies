package com.meetingBuddy.meetingBuddy.service;

import com.meetingBuddy.meetingBuddy.dto.AnalyticsDto;
import com.meetingBuddy.meetingBuddy.entity.Meetings;
import com.meetingBuddy.meetingBuddy.enums.MeetingProvider;
import com.meetingBuddy.meetingBuddy.repository.MeetingsRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeetingService {

    private final MeetingProviderFactory meetingProviderFactory;
    private final NamingService namingService;
    private final MeetingsRepo meetingsRepo;

    public MeetingService(MeetingProviderFactory meetingProviderFactory, NamingService namingService, MeetingsRepo meetingsRepo) {
        this.meetingProviderFactory = meetingProviderFactory;
        this.namingService = namingService;
        this.meetingsRepo = meetingsRepo;
    }

    public Object joinAMeeting(Meetings meetingInfo) {
        ArrayList<String> names = namingService.getNameByLocale(meetingInfo.getLocale(), meetingInfo.getBotUsersCount());
        MeetingJoiner meetingType = meetingProviderFactory.getHandler(meetingInfo.getMeetingProvider());
        meetingInfo.setCreatedAt(LocalDateTime.now());
        meetingInfo.setJoinedBots(0L);
        meetingInfo.setFailedBots(0L);
        meetingsRepo.save(meetingInfo);
        for (String name : names) {
//            meetingType.joinMeeting(meetingInfo, name);
        }
        return true;
    }

    private Meetings meetingObject(Meetings meetingInfo){
        Meetings meetings = new Meetings();
        meetings.setMeetingId(meetingInfo.getMeetingId());
        meetings.setPasscode(meetingInfo.getPasscode());
        meetings.setMeetingProvider(meetingInfo.getMeetingProvider());
        meetings.setBotUsersCount(meetingInfo.getBotUsersCount());
        meetings.setCreatedAt(LocalDateTime.now());
        meetings.setTimeout(meetingInfo.getTimeout());
        meetings.setLocale(meetingInfo.getLocale());
        return meetings;
    }

    public Object refillMeeting(Long id) {
        Optional<Meetings> meetingsOpt = meetingsRepo.findById(id);
        if (meetingsOpt.isEmpty()){
            return false;
        }
        Meetings meetings = meetingsOpt.get();
        joinAMeeting(meetingObject(meetings));
        return true;
    }

    public List<Meetings> todaysMeeting(String username) {
        List<Meetings> allMeetings = meetingsRepo.findAllByCreatedBy(username);
        LocalDate today = LocalDate.now();
        return allMeetings.stream()
                .filter(meeting -> meeting.getCreatedAt().toLocalDate().isEqual(today))
                .collect(Collectors.toList());
    }

    public AnalyticsDto fetchMeetingAnalytics(Long id) {
        Optional<Meetings> meetingOptional = meetingsRepo.findById(id);
        if(meetingOptional.isEmpty()){
            throw new RuntimeException("Cannot find entry in db");
        }
        Meetings meeting = meetingOptional.get();
        return new AnalyticsDto(
                meeting.getJoinedBots(),
                meeting.getFailedBots(),
                meeting.getBotUsersCount() - meeting.getJoinedBots()-meeting.getFailedBots()
        );
    }
}
