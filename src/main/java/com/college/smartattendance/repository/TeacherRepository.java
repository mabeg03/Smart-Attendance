package com.college.smartattendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.college.smartattendance.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}