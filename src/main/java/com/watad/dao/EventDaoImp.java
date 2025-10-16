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
    public void createEvent(EventDetail eventDetail) {
        entityManager.persist(eventDetail);
    }

    @Override
    public List<EventDetail> findAllActiveEvent(int churchId, int meetingId, int sprintId , int status) {
        LocalDate currentDate = LocalDate.now();

        Church church = new Church();
        church.setId(churchId);

        Meetings meeting = new Meetings();
        meeting.setId(meetingId);

        SprintData sprint = new SprintData();
        sprint.setId(sprintId);

        // JPQL query
        String jpql = """
            SELECT e FROM EventDetail e 
            WHERE 
               e.curch = :church
              AND e.meetings = :meeting
              AND e.sprintData = :sprint

    """;
    StringBuilder sql = new StringBuilder(jpql);
        if(status == 1){
            sql.append(" AND e.eventActive = true");
        } else if ( status == 2) {
            sql.append(" AND e.eventActive = false");
        }

        sql.append(" ORDER BY e.from_date ");
        List<EventDetail> events = entityManager.createQuery(sql.toString(), EventDetail.class)
                .setParameter("church", church)
                .setParameter("meeting", meeting)
                .setParameter("sprint", sprint)
                .getResultList();

        return events;
    }

    @Override
    public List<EventDetail> findAllCurrentSprintEvent(int curch_id, int meeting_id, int sprint_id, int status) {
            Church church = new Church();
            church.setId(curch_id);

            Meetings meeting = new Meetings();
            meeting.setId(curch_id);

            SprintData sprint = new SprintData();
            sprint.setId(sprint_id);

            // JPQL query
            String jpql = """
            SELECT e FROM EventDetail e 
            WHERE 
               e.curch = :church
              AND e.meetings = :meeting
              AND e.sprintData = :sprint

    """;
            StringBuilder sql = new StringBuilder(jpql);
            if(status == 1){
                sql.append(" AND e.eventActive = true");
            } else if ( status == 2) {
                sql.append(" AND e.eventActive = false");
            }

            sql.append(" ORDER BY e.from_date ");
            List<EventDetail> events = entityManager.createQuery(sql.toString(), EventDetail.class)
                    .setParameter("church", church)
                    .setParameter("meeting", meeting)
                    .setParameter("sprint", sprint)
                    .getResultList();

            return events;
        }


    @Override
    public EventDetail findById(int id) {
        return  entityManager.find(EventDetail.class,id);
    }

    @Override
    public void edit(EventDetail eventDetail) {
        entityManager.merge(eventDetail);
    }
}
