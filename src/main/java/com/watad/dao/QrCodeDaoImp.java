package com.watad.dao;

import com.watad.entity.QrCode;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class QrCodeDaoImp  implements QrCodeDao{


    private final EntityManager entityManager;

    public QrCodeDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public Optional<QrCode> findByCode(String code) {
        try{
        TypedQuery<QrCode> theQuery = entityManager.createQuery("From QrCode where code =:data",QrCode.class);
        theQuery.setParameter("data",code);
        QrCode qrCode               = theQuery.getSingleResult();
        return Optional.ofNullable(qrCode);
        } catch (NoResultException e){
            return Optional.empty();
        }

    }

    @Override
    public List<QrCode> getActiveByDate(LocalDate localDate, int churchId, int mettingId) {
        String jpql = """
            SELECT q FROM QrCode q
            WHERE q.validDate = :p_date
              AND q.meetings.id = :meetingId
              AND q.church.id = :churchId
        """;
        return entityManager.createQuery(jpql, QrCode.class)
                .setParameter("p_date", LocalDate.now())
                .setParameter("meetingId", mettingId)
                .setParameter("churchId", churchId)
                .getResultList();
    }
}
