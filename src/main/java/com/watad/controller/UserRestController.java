package com.watad.controller;


import com.watad.dto.RoleDto;
import com.watad.entity.Role;
import com.watad.services.UserServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @GetMapping("/roles/{userName}")
    public List<RoleDto> getUserRoles(@PathVariable String userName){
        return  userServices.getUserRoles(userName);
    }

    @PutMapping("/{userName}/{roleId}")
    public ResponseEntity<String> updateUserRole(@PathVariable String userName , @PathVariable int roleId){
        try {
            userServices.updateUserRole(userName, roleId);
            return  ResponseEntity.ok("Role Updated");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Internal Error Update Role");
        }
    }


}
