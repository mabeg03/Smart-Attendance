package com.college.smartattendance.controller;

import org.springframework.web.bind.annotation.*;

import com.college.smartattendance.model.Session;
import com.college.smartattendance.model.Question;
import com.college.smartattendance.model.Attendance;

import com.college.smartattendance.repository.SessionRepository;
import com.college.smartattendance.repository.QuestionRepository;
import com.college.smartattendance.repository.AttendanceRepository;
import com.college.smartattendance.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private final SessionRepository sessionRepository;
    private final QuestionRepository questionRepository;
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    // ✅ Constructor (No Lombok)
    public TeacherController(SessionRepository sessionRepository,
                             QuestionRepository questionRepository,
                             AttendanceRepository attendanceRepository,
                             StudentRepository studentRepository) {

        this.sessionRepository = sessionRepository;
        this.questionRepository = questionRepository;
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    // ✅ Generate QR with GPS + Expiry
    @GetMapping("/generate-qr")
    public String generateQR(@RequestParam double lat,
                            @RequestParam double lng) {

        String token = UUID.randomUUID().toString();

        Session session = new Session();
        session.setToken(token);
        session.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        // ✅ Save teacher's real GPS location
        session.setLatitude(lat);
        session.setLongitude(lng);

        sessionRepository.save(session);

        return "http://localhost:8080/student/mark-attendance?token=" + token;
    }

    // ✅ Add Question (Max 15 Limit)
    @PostMapping("/add-question")
    public String addQuestion(@ModelAttribute Question question){

        if(questionRepository.count() >= 15){
            return "Maximum 15 questions allowed!";
        }

        questionRepository.save(question);

        return "Question Added Successfully!";
    }

    // ✅ Get All Questions
    @GetMapping("/questions")
    public List<Question> getAllQuestions(){
        return questionRepository.findAll();
    }

    // ✅ View Attendance List
    @GetMapping("/attendance-list")
    public List<Attendance> getAttendanceList(){
        return attendanceRepository.findAll();
    }

    // ✅ Analytics Data
    @GetMapping("/analytics")
    public Map<String, Object> analytics(){

        long totalStudents = studentRepository.count();
        long totalAttendance = attendanceRepository.count();
        long totalQuestions = questionRepository.count();

        Map<String, Object> data = new HashMap<>();
        data.put("Total Students", totalStudents);
        data.put("Total Attendance Records", totalAttendance);
        data.put("Total Questions", totalQuestions);

        return data;
    }
}