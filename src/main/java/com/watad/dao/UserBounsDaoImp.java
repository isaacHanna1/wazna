package com.watad.dao;


import com.watad.entity.UserBonus;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserBounsDaoImp implements UserBounsDao{


    private final EntityManager entityManager;

    public UserBounsDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(UserBonus userBonus) {
            entityManager.persist(userBonus);
    }
}
