package com.revature.controllers;

import com.revature.models.QuestionChoice;
import com.revature.models.QuizQuestion;
import com.revature.services.QuestionChoiceService;
import com.revature.services.QuizQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuizQuestionController {

    QuizQuestionService qs;
    QuestionChoiceService qcs;

    @Autowired
    public QuizQuestionController(QuizQuestionService qs, QuestionChoiceService qcs) {
        this.qs = qs;
        this.qcs = qcs;
    }

    @GetMapping("/questions")
    public List<QuizQuestion> getAllQuestions() {
        return qs.getAllQuestions();
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<QuizQuestion> getQuestion(@PathVariable int id) {
        QuizQuestion q = qs.getQuestion(id);
        if(q.getId() == id) {
            return new ResponseEntity<>(q, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/questions")
    public ResponseEntity<QuizQuestion> addQuestion(@RequestBody QuizQuestion q) {
        q = qs.addQuestion(q);
        if (q != null) {
            return new ResponseEntity<>(q, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<QuizQuestion> updateQuestion(@PathVariable int id, @RequestBody QuizQuestion q) {
        q.setId(id);
        QuizQuestion q2 = qs.getQuestion(id);
        if(q2.getId() == id) {
            try {
                q = qs.updateQuestion(q);
                return new ResponseEntity<>(q, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/quizzes/{id}/questions")
    public List<QuizQuestion> getQuestions(@PathVariable int id) {
        return qs.getQuizQuestions(id);
    }

    @PostMapping("/choices")
    public ResponseEntity<QuestionChoice> addChoice(@RequestBody QuestionChoice c) {
        c = qcs.addChoice(c);
        if (c != null) {
            return new ResponseEntity<>(c, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/choices/{id}")
    public ResponseEntity<QuestionChoice> updateChoice(@PathVariable int id, @RequestBody QuestionChoice c) {
        c.setId(id);
        QuestionChoice c2 = qcs.getChoice(id);
        if(c2.getId() == id) {
            try {
                c = qcs.updateChoice(c);
                return new ResponseEntity<>(c, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("questions/{id}/choices")
    public List<QuestionChoice> getChoices(@PathVariable int id) {
        return qcs.getQuestionChoices(id);
    }

    @GetMapping("questions/{id}/correctanswer")
    public ResponseEntity<QuestionChoice> getAnswer(@PathVariable int id) {
        QuestionChoice c = qcs.getCorrectAnswer(id);
        if (c.getQuestionId() == id && c.isCorrect()) {
            return new ResponseEntity<>(c, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}
