package com.watad.dao;

import com.watad.entity.SprintData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;


@Repository
public class SprintDataDaoImp implements SprintDataDao{

    private final EntityManager entityManager;

    public SprintDataDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public SprintData getSprintDataByIsActive(int churchId , int meetingId) {
        TypedQuery<SprintData> theQuery = entityManager.createQuery(
                "FROM SprintData WHERE isActive = :activeStatus and meetings.id =:meetingId and church.id =:churchId ",
                SprintData.class
        ).setParameter("activeStatus", true)
                .setParameter("meetingId",meetingId)
                .setParameter("churchId",churchId);
        return theQuery.getSingleResult();
    }
}
