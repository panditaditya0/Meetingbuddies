package com.meetingBuddy.meetingBuddy.service.VcImpl;

import com.meetingBuddy.meetingBuddy.entity.Meetings;
import com.meetingBuddy.meetingBuddy.enums.MeetingProvider;
import com.meetingBuddy.meetingBuddy.service.MeetingJoiner;

public class GoogleMeeting implements MeetingJoiner {
    @Override
    public MeetingProvider getProviderType() {
        return MeetingProvider.GoogleMeet;
    }

    @Override
    public void joinMeeting(Meetings meetingInfo, String names) {
    }
}
