package com.watad.dao;

import com.watad.entity.EventType;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class EventTypeImp implements EventTypeDao {

    private final EntityManager entityManager;

    public EventTypeImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<EventType> findAll(int churchId , int meetingId){
        String sql = """
                From EventType e where e.church.id =: churchId AND 
                     e.meeting.id =:meetingId
                """;
        return  entityManager.createQuery(sql, EventType.class).setParameter("churchId",churchId)
                .setParameter("meetingId",meetingId).getResultList();

    }
}
