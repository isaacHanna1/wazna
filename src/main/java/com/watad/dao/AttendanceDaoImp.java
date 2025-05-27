package com.watad.dao;

import com.watad.entity.Attendance;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class AttendanceDaoImp implements AttendanceDao{

    private final EntityManager entityManager;

    public AttendanceDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(Attendance attendance) {
        entityManager.persist(attendance);
    }
}
