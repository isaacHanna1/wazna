package com.watad.controller;

import com.watad.dto.RoleDto;
import com.watad.entity.Role;
import com.watad.services.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoleRestController {


    private final RoleService roleService;

    public RoleRestController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/role")
    public List<RoleDto> getRoles(){
        return  roleService.findRoles();
    }
}
