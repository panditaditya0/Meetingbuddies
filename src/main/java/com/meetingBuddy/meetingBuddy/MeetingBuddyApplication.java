package com.meetingBuddy.meetingBuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MeetingBuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetingBuddyApplication.class, args);
	}

}
