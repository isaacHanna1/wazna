package com.watad.dao;

import com.watad.Common.TimeUtil;
import com.watad.entity.SprintData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public class SprintDataDaoImp implements SprintDataDao{

    private final EntityManager entityManager;
    @Autowired
    private  TimeUtil timeUtil;

    public SprintDataDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public SprintData getSprintDataByIsActive(int churchId , int meetingId) {
        LocalDate today                 = timeUtil.now_localDate();
        TypedQuery<SprintData> theQuery = entityManager.createQuery(
                        "SELECT s FROM SprintData s WHERE s.isActive = :activeStatus " +
                                "AND s.meetings.id = :meetingId AND s.church.id = :churchId " +
                                "AND s.fromDate <= :now AND :now <= s.toDate",
                        SprintData.class
                ).setParameter("activeStatus", true)
                .setParameter("meetingId",meetingId)
                .setParameter("churchId",churchId)
                .setParameter("now",today);
        return theQuery.getSingleResult();
    }

    @Override
    public List<SprintData> findAll(int churchId, int meetingId) {
        List<SprintData> resultList = entityManager
                .createQuery("From SprintData where meetings.id =: meetingId AND church.id =: churchId", SprintData.class)
                .setParameter("meetingId", meetingId)
                .setParameter("churchId", churchId)
                .getResultList();

        // Print all data
        System.out.println("=== ALL SPRINT DATA ===");
        for (SprintData sprint : resultList) {
            System.out.println(sprint.toString());
        }
        System.out.println("Total records: " + resultList.size());

        return resultList;
    }
}
