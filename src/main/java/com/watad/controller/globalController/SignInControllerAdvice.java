package com.watad.controller.globalController;


import com.watad.entity.User;
import com.watad.exceptions.ProfileException;
import com.watad.security.CustomUserDetails;
import com.watad.services.CustomUserDetailsService;
import com.watad.services.YouthRankService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SignInControllerAdvice {

    private final CustomUserDetailsService customUserDetailsService;
    private final YouthRankService youthRankService;
    public SignInControllerAdvice(CustomUserDetailsService customUserDetailsService , YouthRankService youthRankService) {
        this.customUserDetailsService = customUserDetailsService;
        this.youthRankService         = youthRankService;
    }


    @ModelAttribute
    public void getUserDetails(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.getPrincipal() instanceof  CustomUserDetails){
            CustomUserDetails user         = (CustomUserDetails) auth.getPrincipal();
            model.addAttribute("firstName", user.getFirstName());
            model.addAttribute("userId", user.getId());
            model.addAttribute("points" , youthRankService.getYouthPoint());
        }
    }



}
