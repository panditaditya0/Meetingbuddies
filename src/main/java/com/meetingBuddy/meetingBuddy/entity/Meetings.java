package com.meetingBuddy.meetingBuddy.entity;

import com.meetingBuddy.meetingBuddy.enums.MeetingProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meetings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String meetingId;
    private String passcode;
    private MeetingProvider meetingProvider;
    private Long botUsersCount;
    private LocalDateTime createdAt;
    private Long timeout;
    private String locale;
    private Long joinedBots;
    private Long failedBots;
    private String createdBy;
}
