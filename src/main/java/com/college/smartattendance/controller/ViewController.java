package com.college.smartattendance.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/teacher-dashboard")
    public String teacherDashboard(HttpSession session){

        if(session.getAttribute("teacher") == null){
            return "redirect:/login";
        }

        return "teacher-dashboard";
    }

    @GetMapping("/student-dashboard")
    public String studentDashboard(HttpSession session){

        if(session.getAttribute("student") == null){
            return "redirect:/login";
        }

        return "student-dashboard";
    }
}