package com.meetingBuddy.meetingBuddy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "app_user") // or users, or anything else
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String role;
}