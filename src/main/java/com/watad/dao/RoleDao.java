package com.watad.dao;

import com.watad.dto.RoleDto;
import com.watad.entity.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleDao {
    public Role findByName(String name);
    public void save(Role role);
    public Role findById(int id );
    public List<RoleDto> findRoles();
}
