package com.watad.dao;

import com.watad.entity.Church;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChurchDaoImp implements  ChurchDao{

    private EntityManager entityManager ;

    public ChurchDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Church> findAll() {
        return entityManager.createQuery("From Church",Church.class).getResultList();
    }
}
