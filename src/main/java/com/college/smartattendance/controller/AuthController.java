package com.college.smartattendance.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.college.smartattendance.repository.*;
import com.college.smartattendance.model.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @PostMapping("/student-login")
    public String studentLogin(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session){

        Student student = studentRepository.findAll()
                .stream()
                .filter(s -> s.getEmail().equals(email)
                        && s.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if(student == null){
            return "redirect:/login";
        }

        session.setAttribute("student", student);

        return "redirect:/student-dashboard";
    }

    @PostMapping("/teacher-login")
    public String teacherLogin(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session){

        Teacher teacher = teacherRepository.findAll()
                .stream()
                .filter(t -> t.getEmail().equals(email)
                        && t.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if(teacher == null){
            return "redirect:/login";
        }

        session.setAttribute("teacher", teacher);

        return "redirect:/teacher-dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }
}