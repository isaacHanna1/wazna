package com.watad.controller;


import com.watad.entity.User;
import com.watad.services.UserServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping("/user")
    public void saveUser(){
        userServices.saveUser(new User());
        System.out.println("Done");
    }
}
