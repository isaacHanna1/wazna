package com.watad.dto.quiz;

import com.watad.enumValues.QuizStatus;
import com.watad.enumValues.QuizType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class QuizRequest {

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private QuizType quizType;
    private QuizStatus quizStatus;
}
