package com.college.smartattendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.college.smartattendance.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    boolean existsByStudentIdAndSessionId(Long studentId, Long sessionId);
    boolean existsByStudentId(Long studentId);
}