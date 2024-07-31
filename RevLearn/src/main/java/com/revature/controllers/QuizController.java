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

//    get without DTO
//    @GetMapping("/quizzes/{id}")
//    public Quiz getQuizById(@PathVariable int id){
//        return qs.getQuizById(id);
//    }

//    get with DTO
    @GetMapping("/quizzes/{id}")
    public QuizDTO getQuizById(@PathVariable int id){
        Quiz myQuiz = qs.getQuizById(id);
        QuizDTO newQuizDTO = new QuizDTO();
        newQuizDTO.setCourse_id(myQuiz.getCourseId());
        newQuizDTO.setTitle(myQuiz.getTitle());
        newQuizDTO.setTimer(myQuiz.getTimer());
        newQuizDTO.setAttempts_allowed(myQuiz.getAttemptsAllowed());
        newQuizDTO.setOpen(myQuiz.isOpen());

        List<QuizQuestion> myQuestions = qqs.getQuizQuestions(myQuiz.getQuizId());
        QuizQuestionDTO[] quizQuestionDTOS = new QuizQuestionDTO[myQuestions.size()];
        int counter =0;
        for(QuizQuestion quizQuestion : myQuestions) {
            QuizQuestionDTO quizQuestionDTO = new QuizQuestionDTO();
            quizQuestionDTO.setQuestion_text(quizQuestion.getQuestionText());

            List<QuestionChoice> questionChoices = qcs.getQuestionChoices(quizQuestion.getId());
            QuestionChoiceDTO[] questionChoiceDTOS = new QuestionChoiceDTO[questionChoices.size()];
            int counter2=0;
            for(QuestionChoice questionChoice : questionChoices){
                QuestionChoiceDTO questionChoiceDTO = new QuestionChoiceDTO();
                questionChoiceDTO.setText(questionChoice.getChoiceText());
                questionChoiceDTO.setCorrect(questionChoice.isCorrect());
                questionChoiceDTOS[counter2] = questionChoiceDTO;
                counter2++;
            }
            quizQuestionDTO.setQuestion_choices(questionChoiceDTOS);
            quizQuestionDTOS[counter] = quizQuestionDTO;
            counter++;
        }
        newQuizDTO.setQuestions(quizQuestionDTOS);

        return newQuizDTO;
    }

    @GetMapping("/courses/{courseId}/quizzes")
    public List<Quiz> getQuizByCourseId(@PathVariable int courseId){
        return qs.getAllQuizzesByCourse(courseId);
    }

//    Post without DTO
//    @PostMapping("/quizzes")
//    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz q){
//        Quiz createdQuiz = qs.addQuiz(q);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuiz);
//    }


//    alternate post with combined DTO QuizDTO, QuizQuestionDTO, QuestionChoiceDTO
    @PostMapping("/quizzes")
    public ResponseEntity<QuizDTO> createQuizAndQuestionsAndChoices(@RequestBody QuizDTO quizDTO){
        Quiz createdQuiz = qs.addQuiz(quizDTO);
        for( QuizQuestionDTO qqDTO : quizDTO.getQuestions()) {
            QuizQuestion createdQuizQuestion = qqs.addQuestionDTO(qqDTO, createdQuiz.getQuizId());
            for(QuestionChoiceDTO qcDTO : qqDTO.getQuestion_choices()){
                qcs.addChoiceDTO(qcDTO, createdQuizQuestion.getId());
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
