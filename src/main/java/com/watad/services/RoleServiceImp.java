package com.watad.services;

import com.watad.dao.RoleDao;
import com.watad.entity.Role;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImp implements RoleService{

    private final RoleDao roleDao;

    public RoleServiceImp(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role findByName(String name){
        try {
            return roleDao.findByName(name);
        }catch (NoResultException e ){
            return null; // i user the when
        }
    }

    @Override
    public void save(Role role) {
        roleDao.save(role);
    }

    @Override
    public Role findById(int id) {
        return roleDao.findById(id);
    }

    @Override
    public void findOrCreate(String roleName) {
       Role role = roleDao.findByName(roleName);
       if(role == null){
           role = new Role();
           role.setRoleName(roleName);
           roleDao.save(role);
       }
    }
}
