package com.college.smartattendance.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime expiryTime;

    // ✅ ADD THESE TWO FIELDS
    private double latitude;
    private double longitude;
}