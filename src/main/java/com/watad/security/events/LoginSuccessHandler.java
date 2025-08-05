package com.watad.security.events;

import com.watad.dao.UserDao;
import com.watad.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;


@Component


public class LoginSuccessHandler  extends SimpleUrlAuthenticationSuccessHandler {


    private final UserDao userDao;

    public LoginSuccessHandler(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String userName             = authentication.getName();
        userDao.findByUserNameForLogin(userName).ifPresent(user->{
            user.setLastLogin(LocalDateTime.now());
            userDao.saveUser(user);
        });
        super.onAuthenticationSuccess(request,response,authentication);
    }


}
