package com.revature.controllers;

import com.revature.models.Quiz;
import com.revature.services.QuizServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuizController {

    QuizServiceImpl qs;

    @Autowired
    public QuizController(QuizServiceImpl quizService){
        this.qs = quizService;
    }

    @GetMapping("/quizzes")
    public List<Quiz> getAllQuizzes(){
        return qs.getAllQuizzes();
    }

    @GetMapping("/quizzes/{id}")
    public Quiz getQuizById(@PathVariable int id){
        return qs.getQuizById(id);
    }

    @GetMapping("/course/{courseId}/quizzes")
    public List<Quiz> getQuizByCourseId(@PathVariable int courseId){
        return qs.getAllQuizzesByCourse(courseId);
    }

    @PostMapping("/quizzes")
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz q){
        Quiz createdQuiz = qs.addQuiz(q);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuiz);
    }

    @PutMapping("/quizzes/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable int id, @RequestBody Quiz updateQuiz){
        updateQuiz.setQuizId(id);
        Quiz q = qs.getQuizById(id);
        if(q.getQuizId() == id) {
            updateQuiz = qs.updateQuiz(updateQuiz);
            return new ResponseEntity<>(updateQuiz,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<Boolean> deleteQuiz(@PathVariable int id) {
        boolean wasDeleted = qs.deleteQuizById(id);
        return new ResponseEntity<>(wasDeleted? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

}
