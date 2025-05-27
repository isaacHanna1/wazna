package com.watad.dao;

import com.watad.entity.Meetings;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class MeetingDaoImp implements  MeetingDao{

    private final EntityManager entityManager;

    public MeetingDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }



    @Override
    public List<Meetings> findAll() {
        return entityManager.createQuery("FROM Meetings", Meetings.class).getResultList();
    }
}
