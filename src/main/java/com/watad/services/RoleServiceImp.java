package com.watad.services;

import com.watad.dao.RoleDao;
import com.watad.dto.RoleDto;
import com.watad.entity.Role;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public List<RoleDto> findRoles() {
        return  roleDao.findRoles();
    }


    @Override
    public List<RoleDto> findRolesWithArabDesc() {
        List<RoleDto> list = roleDao.findRoles();

        for (RoleDto role : list) {
            String arabicDesc = findRolesWithArabDesc(role.getName());
            role.setName(arabicDesc);
        }

        return list;
    }

    private String findRolesWithArabDesc(String roleName) {
        switch (roleName) {
            case "ROLE_YOUTH": return "مخدوم";
            case "ROLE_SERVER": return "خادم";
            case "ROLE_CLASS_LEADER": return "أمين فصل";
            case "ROLE_TREASURER": return "أمين صندوق";
            case "ROLE_SERVICE_LEADER": return "أمين خدمة";
            case "ROLE_SUPER": return "مدير النظام";
            case "ROLE_HELPER": return "خادم مساعد";
            default: return roleName;
        }
    }
}
