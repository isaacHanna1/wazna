package com.watad.dao;

import com.watad.entity.EventDetail;
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
    public List<EventDetail> findAllActiveEvent() {
        // Get the current system date
        LocalDate currentDate = LocalDate.now();
        // Create a JPQL query to find all active events where the current date is less than from_date
        String jpql = "SELECT e FROM EventDetail e WHERE e.from_date > :currentDate order by e.from_date";
        TypedQuery<EventDetail> query = entityManager.createQuery(jpql, EventDetail.class);
        query.setParameter("currentDate", currentDate);

        // Execute the query
        List<EventDetail> events = query.getResultList();

        // Check if the result is null or empty
        if (events == null || events.isEmpty()) {
            // Return a default event or a list containing a default event
            EventDetail defaultEvent = new EventDetail(); // Create a default event
            defaultEvent.setTitle("NO Event Till Now");
            defaultEvent.setDescription("Wait Us For Comming Event");
            defaultEvent.setImageUrl("commingSoon.jpg");
            return List.of(defaultEvent);
        }
        return events;
    }
}
