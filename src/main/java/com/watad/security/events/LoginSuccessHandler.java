package com.watad.security.events;

import com.watad.dao.UserDao;
import com.watad.entity.User;
import com.watad.services.UserServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;


@Component


public class LoginSuccessHandler  extends SavedRequestAwareAuthenticationSuccessHandler {


    private final UserServices userServices;

    public LoginSuccessHandler(UserServices userServices) {
        this.userServices = userServices;
        setDefaultTargetUrl("/home");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String userName             = authentication.getName();
        userServices.findByUserNameForLogin(userName).ifPresent(user->{
            user.setLastLogin(LocalDateTime.now());
            userServices.saveUser(user);
        });
        super.onAuthenticationSuccess(request,response,authentication);
    }


}
