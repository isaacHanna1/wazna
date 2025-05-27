package com.watad.dao;

import com.watad.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class RoleDaoImp implements  RoleDao{


    private final EntityManager entityManger;

    public RoleDaoImp(EntityManager entityManger) {
        this.entityManger = entityManger;
    }

    @Override
    public Role findByName(String name) {

        List<Role> result = entityManger.createQuery("FROM Role WHERE roleName = :data", Role.class)
                .setParameter("data", name)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);

    }

    @Override
    @Transactional
    public void save(Role role) {
        entityManger.persist(role);
    }
    public Role findById(int id ){
        return entityManger.find(Role.class,id);
    }
}
