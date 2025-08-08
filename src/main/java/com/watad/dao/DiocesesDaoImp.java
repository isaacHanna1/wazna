package com.watad.dao;

import com.watad.entity.Dioceses;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DiocesesDaoImp implements DioceseDao {

    private EntityManager entityManager ;

    public DiocesesDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Dioceses> findAll() {
        return  entityManager.createQuery("From Dioceses",Dioceses.class).getResultList();
    }
}
