package com.college.smartattendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.college.smartattendance.model.QuizAttempt;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

    boolean existsByStudentId(Long studentId);
}