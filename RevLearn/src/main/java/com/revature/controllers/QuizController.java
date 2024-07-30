package com.revature.controllers;

import com.revature.models.QuestionChoice;
import com.revature.models.Quiz;
import com.revature.models.QuizQuestion;
import com.revature.models.dtos.QuestionChoiceDTO;
import com.revature.models.dtos.QuizDTO;
import com.revature.models.dtos.QuizQuestionDTO;
import com.revature.services.QuestionChoiceServiceImpl;
import com.revature.services.QuizQuestionServiceImpl;
import com.revature.services.QuizServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuizController {

    QuizServiceImpl qs;

    QuizQuestionServiceImpl qqs;

    QuestionChoiceServiceImpl qcs;

    @Autowired
    public QuizController(QuizServiceImpl quizService, QuizQuestionServiceImpl qqs, QuestionChoiceServiceImpl qcs){
        this.qs = quizService;
        this.qcs = qcs;
        this.qqs = qqs;
    }

    @GetMapping("/quizzes")
    public List<Quiz> getAllQuizzes(){
        return qs.getAllQuizzes();
    }

    @GetMapping("/quizzes/{id}")
    public Quiz getQuizById(@PathVariable int id){
        return qs.getQuizById(id);
    }

    @GetMapping("/courses/{courseId}/quizzes")
    public List<Quiz> getQuizByCourseId(@PathVariable int courseId){
        return qs.getAllQuizzesByCourse(courseId);
    }

//    @PostMapping("/quizzes")
//    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz q){
//        Quiz createdQuiz = qs.addQuiz(q);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuiz);
//    }

    @PostMapping("/test")
    public ResponseEntity<QuizDTO> createQuiz(@RequestBody QuizDTO q){
        QuizDTO myQDTO= qs.testCourseId(q);
        return ResponseEntity.status(HttpStatus.CREATED).body(myQDTO);
    }

//    alternate post with combined DTO
    @PostMapping("/quizzes")
    public ResponseEntity<QuizDTO> createQuizAndQuestionsAndChoices(@RequestBody QuizDTO quizDTO){
        QuizDTO createdQuizDTO = qs.addQuiz(quizDTO);
        for( QuizQuestionDTO qqDTO : quizDTO.getQuestions()) {
            qqDTO = qqs.addQuestionDTO(qqDTO, 1);
            for(QuestionChoiceDTO qcDTO : qqDTO.getQuestion_choices()){
                qcs.addChoiceDTO(qcDTO, 1);
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(quizDTO);
    }

    @PatchMapping("/quizzes/{id}")
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
