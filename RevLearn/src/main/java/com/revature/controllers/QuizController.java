package com.revature.controllers;

import com.revature.models.Quiz;
import com.revature.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    QuizService qs;

    @Autowired
    public QuizController(QuizService quizService){
        this.qs = quizService;
    }

    @GetMapping
    public List<Quiz> getAllQuizzes(){
        return qs.getAllQuizzes();
    }

    @GetMapping("/{id}")
    public Quiz getQuizById(@PathVariable int id){
        return qs.getQuizById(id);
    }

    @GetMapping("/{courseId}")
    public List<Quiz> getQuizByCourseId(@PathVariable int id){
        return qs.getAllQuizzesByCourse(id);
    }

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz q){
        Quiz createdQuiz = qs.addQuiz(q);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuiz);
    }

    @PutMapping("/{id}")
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteQuiz(@PathVariable int id) {
        boolean wasDeleted = qs.deleteQuiz(id);
        return new ResponseEntity<>(wasDeleted? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

}
