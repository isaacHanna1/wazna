package com.watad.services.Quiz;


import com.watad.dao.UserDao;
import com.watad.dto.quiz.QuizResponse;
import com.watad.entity.Profile;
import com.watad.repo.quiz.QuizRepository;
import com.watad.dto.quiz.QuizRequest;
import com.watad.entity.Church;
import com.watad.entity.Meetings;
import com.watad.entity.User;
import com.watad.entity.quiz.Quiz;
import com.watad.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizServices {

    private final QuizRepository quizRepository;
    private final UserServices userServices;
    private final UserDao userDao;

    public Quiz createQuiz(QuizRequest quizRequest){

        Quiz quiz = mapQuizRequestToQuiz(quizRequest);
        quizRepository.save(quiz);
        return quiz;
    }

    public List<QuizResponse> getQuiz(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Quiz> result = quizRepository.findAll(pageable);
        return  result.stream().map(this::mapQuizToResponse).toList();

    }

    private Quiz mapQuizRequestToQuiz(QuizRequest quizRequest){

        User user               = userServices.logedInUser();
        Church church           = userServices.getLogInUserChurch();
        Meetings meetings       = userServices.getLogInUserMeeting();

            return Quiz.builder()
                .title(quizRequest.getTitle())
                .createdBy(user)
                .startDate(quizRequest.getStartDate())
                .endDate(quizRequest.getEndDate())
                .quizStatus(quizRequest.getQuizStatus())
                .quizType(quizRequest.getQuizType())
                .church(church)
                .meetings(meetings)
                .active(true)
                .build();
    }


    private QuizResponse mapQuizToResponse(Quiz quiz) {

        User user = quiz.getCreatedBy();
        Profile p = user.getProfile();

        return QuizResponse.builder()
                .quizId(quiz.getId())
                .title(quiz.getTitle())
                .startDate(quiz.getStartDate())
                .endDate(quiz.getEndDate())
                .quizStatus(quiz.getQuizStatus())
                .quizType(quiz.getQuizType())
                .createBy(p.getFirstName()+" "+p.getLastName())
                .active(quiz.getActive())
                .build();
    }
    }
