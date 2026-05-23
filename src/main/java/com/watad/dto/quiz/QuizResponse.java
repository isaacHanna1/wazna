package com.watad.dto.quiz;

import com.watad.enumValues.QuizStatus;
import com.watad.enumValues.QuizType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class QuizResponse {

    private Long quizId ;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    private QuizType quizType;
    private QuizStatus quizStatus;
    private String createBy;



}
