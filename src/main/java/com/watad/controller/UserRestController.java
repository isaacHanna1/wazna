package com.watad.controller;


import com.watad.services.UserServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users/")
public class UserRestController {


    private final UserServices userServices;

    public UserRestController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PutMapping("/{userName}/status")
    public ResponseEntity<String>  updateUserStatus(
            @PathVariable String userName,
            @RequestParam boolean enabled) {

        try {
            int result = userServices.activeOrDisactiveUser(enabled, userName);
            if(result > 0){
                return ResponseEntity.ok("Status  updated successfully.");
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Internal Error ");
        }
    }
}
