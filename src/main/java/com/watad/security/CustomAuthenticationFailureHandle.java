package com.watad.security;

import com.watad.services.CustomUserDetailsService;
import com.watad.services.UserServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandle extends SimpleUrlAuthenticationFailureHandler {


    private final UserDetailsService userDetailsService;

    @Autowired
    public CustomAuthenticationFailureHandle(@Qualifier("customUserDetailsService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        try {
            String userName                 = request.getParameter("username");
            System.out.println("in faluire what happend "+userName);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            System.out.println("in faluire what happend 2"+userDetails.isEnabled());

            if (userDetails != null && !userDetails.isEnabled()) {
                response.sendRedirect("/sign-in?error=disabled");
                return;
            } else {
                response.sendRedirect("/sign-in?error=invalid");
            }
        } catch (UsernameNotFoundException e) {
            response.sendRedirect("/sign-in?error=invalid");
        }
    }

}
