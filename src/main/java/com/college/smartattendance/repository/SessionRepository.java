package com.college.smartattendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.college.smartattendance.model.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {}