package com.watad.services;

import com.watad.entity.Role;

import java.util.Optional;

public interface RoleService {

    public Role findByName(String name);
    void save(Role role);
    public  Role findById(int id );
    public void findOrCreate(String roleName);
}
