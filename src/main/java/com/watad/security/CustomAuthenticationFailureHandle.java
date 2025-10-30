package com.watad.security;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class CustomAuthenticationFailureHandle extends SimpleUrlAuthenticationFailureHandler {


    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationFailureHandle(
            @Qualifier("customUserDetailsService") UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String username = request.getParameter("username");
        String rawPassword = request.getParameter("password");

        System.out.println("=== AUTHENTICATION FAILURE DEBUG ===");
        System.out.println("Username: " + username);
        System.out.println("Exception Type: " + exception.getClass().getName());
        System.out.println("Message: " + exception.getMessage());

        if (exception instanceof DisabledException) {
            try {
                // Load user manually
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Manually check if password matches
                boolean correctPassword = passwordEncoder.matches(rawPassword, userDetails.getPassword());
                System.out.println("Password match for disabled user: " + correctPassword);

                if (correctPassword) {
                    // password is correct but account is disabled
                    request.getSession().setAttribute("userName", username);
                    response.sendRedirect("/awaitingApprove");
                    return;
                } else {
                    // wrong password, even if user disabled
                    response.sendRedirect("/sign-in?error=invalid");
                    return;
                }
            } catch (Exception ex) {
                System.out.println("Error verifying disabled user password: " + ex.getMessage());
                response.sendRedirect("/sign-in?error=invalid");
                return;
            }
        }
        else if (exception instanceof BadCredentialsException) {
            response.sendRedirect("/sign-in?error=invalid");
        }
        else if (exception instanceof LockedException) {
            response.sendRedirect("/sign-in?error=locked");
        }
        else {
            response.sendRedirect("/sign-in?error=unknown");
        }
    }


}
