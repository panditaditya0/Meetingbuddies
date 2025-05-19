package com.meetingBuddy.meetingBuddy.service;

import com.meetingBuddy.meetingBuddy.enums.MeetingProvider;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MeetingProviderFactory {
    private final Map<MeetingProvider, MeetingJoiner> meetingProviderHandler = new HashMap<>();

    public MeetingProviderFactory(List<MeetingJoiner> meetingJoiner){
        for(MeetingJoiner aJoinerType : meetingJoiner){
            meetingProviderHandler.put(aJoinerType.getProviderType(), aJoinerType);
        }
    }

    public MeetingJoiner getHandler(MeetingProvider meetingProvider) {
        return meetingProviderHandler.get(meetingProvider);
    }
}
