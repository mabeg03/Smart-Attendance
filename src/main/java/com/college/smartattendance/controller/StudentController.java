package com.college.smartattendance.controller;

import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import com.college.smartattendance.model.*;
import com.college.smartattendance.repository.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final SessionRepository sessionRepository;
    private final QuestionRepository questionRepository;
    private final AttendanceRepository attendanceRepository;
    private final QuizAttemptRepository quizAttemptRepository;

    public StudentController(SessionRepository sessionRepository,
                             QuestionRepository questionRepository,
                             AttendanceRepository attendanceRepository,
                             QuizAttemptRepository quizAttemptRepository) {

        this.sessionRepository = sessionRepository;
        this.questionRepository = questionRepository;
        this.attendanceRepository = attendanceRepository;
        this.quizAttemptRepository = quizAttemptRepository;
    }

    // ===================== MARK ATTENDANCE =====================
    @GetMapping("/mark-attendance")
    public String markAttendance(@RequestParam String token,
                                 @RequestParam double lat,
                                 @RequestParam double lng,
                                 HttpSession httpSession){

        Student student = (Student) httpSession.getAttribute("student");

        if(student == null) return "Login Required!";

        Session sessionObj = sessionRepository.findAll()
                .stream()
                .filter(s -> s.getToken().equals(token))
                .findFirst()
                .orElse(null);

        if(sessionObj == null) return "Invalid QR";
        if(sessionObj.getExpiryTime().isBefore(LocalDateTime.now()))
            return "QR Expired";

        double distance = calculateDistance(
                sessionObj.getLatitude(),
                sessionObj.getLongitude(),
                lat,
                lng
        );

        if(distance > 50) return "Outside Classroom Range!";

        if(attendanceRepository.existsByStudentIdAndSessionId(
                student.getId(), sessionObj.getId()))
            return "Attendance Already Marked!";

        Attendance attendance = new Attendance();
        attendance.setStudentId(student.getId());
        attendance.setSessionId(sessionObj.getId());
        attendance.setMarkedAt(LocalDateTime.now());

        attendanceRepository.save(attendance);

        return "Attendance Marked Successfully!";
    }

    // ===================== GET QUIZ =====================
    @GetMapping("/get-quiz")
    public List<Question> getRandomQuestions(HttpSession session){

        Student student = (Student) session.getAttribute("student");

        if(student == null) return Collections.emptyList();

        boolean attended = attendanceRepository.existsByStudentId(student.getId());
        if(!attended) return Collections.emptyList();

        List<Question> questions = questionRepository.findAll();
        if(questions.isEmpty()) return Collections.emptyList();

        Collections.shuffle(questions);
        return questions.stream().limit(5).toList();
    }

    // ===================== SUBMIT QUIZ =====================
    @PostMapping("/submit-quiz")
    public String submitQuiz(@RequestBody Map<Long, String> answers,
                             HttpSession session){

        Student student = (Student) session.getAttribute("student");

        if(student == null) return "Login Required!";

        if(quizAttemptRepository.existsByStudentId(student.getId()))
            return "You have already attempted the quiz!";

        List<Question> questions = questionRepository.findAll();

        int score = 0;

        for(Question q : questions){
            if(answers.containsKey(q.getId())){
                if(q.getCorrectAnswer()
                        .equalsIgnoreCase(answers.get(q.getId()))){
                    score++;
                }
            }
        }

        QuizAttempt attempt = new QuizAttempt();
        attempt.setStudentId(student.getId());
        attempt.setScore(score);
        attempt.setSubmittedAt(LocalDateTime.now());

        quizAttemptRepository.save(attempt);

        return "Your Score: " + score + " / 5";
    }

    // ===================== DISTANCE CALC =====================
    private double calculateDistance(double lat1, double lon1,
                                     double lat2, double lon2) {

        final int R = 6371000;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}