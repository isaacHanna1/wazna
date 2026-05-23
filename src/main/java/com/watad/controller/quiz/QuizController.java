package com.watad.controller.quiz;


import com.watad.dto.quiz.QuizResponse;
import com.watad.services.Quiz.QuizServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/dashboard/quiz")
@RequiredArgsConstructor
public class QuizController {


    private final QuizServices quizServices;

    @GetMapping("/view")
    public String quizView(
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            Model model
    ) {

        List <QuizResponse> quizPage = quizServices.getQuiz(pageNum, pageSize);

        model.addAttribute("quizzes", quizPage);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);

        model.addAttribute("totalQuizzes", 0);
        model.addAttribute("activeQuizzes", 0);
        model.addAttribute("draftQuizzes", 0);
        model.addAttribute("totalAnswers", 0);

        return "quiz-list";
    }

}
