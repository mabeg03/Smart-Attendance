package com.college.smartattendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.college.smartattendance.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}