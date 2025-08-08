package com.watad.dao;

import com.watad.dto.MeetingDto;
import com.watad.entity.Meetings;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    @Override
    public Meetings createMeating(Meetings meetings) {
         entityManager.persist(meetings);
         entityManager.flush();
         return  meetings;
    }

    @Override
    public List<MeetingDto> findByChurchId(int churchId) {
        TypedQuery<MeetingDto> query = entityManager.createQuery("Select new com.watad.dto.MeetingDto(m.id , m.description) From Meetings  m  JOIN m.church c Where c.id = :id ",MeetingDto.class);
        query.setParameter("id",churchId);
        return query.getResultList();
    }

}
