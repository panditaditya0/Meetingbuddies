package com.meetingBuddy.meetingBuddy.service;

import com.meetingBuddy.meetingBuddy.entity.Meetings;
import com.meetingBuddy.meetingBuddy.enums.MeetingProvider;

public interface MeetingJoiner {
    public MeetingProvider getProviderType();

    public void joinMeeting(Meetings meetingInfo, String names);
}
