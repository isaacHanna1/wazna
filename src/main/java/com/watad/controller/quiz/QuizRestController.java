package com.watad.controller.quiz;

import com.watad.dto.quiz.QuizRequest;
import com.watad.dto.quiz.QuizResponse;
import com.watad.entity.quiz.Quiz;
import com.watad.services.Quiz.QuizServices;
import com.watad.services.backup.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/quiz")
@RequiredArgsConstructor
public class QuizRestController {

    private final QuizServices quizServices;

    private final BackupService backupService;


    @PostMapping
    public ResponseEntity<QuizResponse> createQuiz(@RequestBody QuizRequest quizRequest){
            Quiz quiz = quizServices.createQuiz(quizRequest);
            QuizResponse quizResponse =   QuizResponse.builder()
                    .quizId(quiz.getId())
                    .title(quiz.getTitle())
                    .build();

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(quizResponse);

    }
    @RequestMapping("/go")
    public String test(){
        backupService.callJob();
        return "Done";
    }
}
