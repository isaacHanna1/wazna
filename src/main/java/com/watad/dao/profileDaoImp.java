package com.watad.dao;

import com.watad.entity.Profile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class profileDaoImp implements ProfileDao{

    private final EntityManager entityManager;

    public profileDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Profile> findAll() {
        TypedQuery<Profile> query = entityManager.createQuery(
                "SELECT p FROM Profile p JOIN FETCH p.user", Profile.class
        );
        return query.getResultList();
    }

    @Override
    @Transactional
    public void saveProfile(Profile profile) {

        if (profile !=null){
            entityManager.persist(profile);
        }
    }

    @Override
    public Profile getProfileById(int id) {
        try {
            return entityManager.find(Profile.class,id);
        } catch (NoResultException e) {
            throw new NoResultException("There Is no Profile Founded with Id "+id);
        }
    }
}
