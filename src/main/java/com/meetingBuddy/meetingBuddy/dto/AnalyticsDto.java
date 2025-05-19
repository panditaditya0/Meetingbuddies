package com.meetingBuddy.meetingBuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDto {
    Long joined;
    Long failed;
    Long pending;
}
