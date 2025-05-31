package com.watad.dao;

import com.watad.entity.Church;
import com.watad.entity.EventDetail;
import com.watad.entity.Meetings;
import com.watad.entity.SprintData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
@Repository
public class EventDaoImp implements EventDao{
    private final EntityManager entityManager ;

    public EventDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<EventDetail> findAllActiveEvent(int churchId, int meetingId, int sprintId) {
        LocalDate currentDate = LocalDate.now();

        // Create lightweight objects with only ID set
        Church church = new Church();
        church.setId(churchId);

        Meetings meeting = new Meetings();
        meeting.setId(meetingId);

        SprintData sprint = new SprintData();
        sprint.setId(sprintId);

        // JPQL query
        String jpql = """
            SELECT e FROM EventDetail e 
            WHERE e.from_date > :currentDate
              AND e.curch = :church
              AND e.meetings = :meeting
              AND e.sprintData = :sprint
            ORDER BY e.from_date
    """;

        List<EventDetail> events = entityManager.createQuery(jpql, EventDetail.class)
                .setParameter("currentDate", currentDate)
                .setParameter("church", church)
                .setParameter("meeting", meeting)
                .setParameter("sprint", sprint)
                .getResultList();



        return events;
    }
}
