package com.college.smartattendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.college.smartattendance.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {}