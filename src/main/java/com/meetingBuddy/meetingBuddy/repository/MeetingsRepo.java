package com.meetingBuddy.meetingBuddy.repository;

import com.meetingBuddy.meetingBuddy.entity.Meetings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeetingsRepo extends JpaRepository<Meetings, Long> {

    @Query("SELECT m FROM Meetings m WHERE m.createdAt >= :start AND m.createdAt < :end")
    List<Meetings> findMeetingsCreatedToday(@Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end);
}
