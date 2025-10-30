package com.watad.dao;

import com.watad.entity.QrCode;
import com.watad.services.UserServices;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class QrCodeDaoImp  implements QrCodeDao{


    private final EntityManager entityManager;

    public QrCodeDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public Optional<QrCode> findByCode(String code , int churchId , int meetingId) {
        try{
        TypedQuery<QrCode> theQuery = entityManager.createQuery("From QrCode where code =:data " +
                " AND meetings.id = :meetingId  AND church.id = :churchId ",QrCode.class);
        theQuery.setParameter("data",code);
        theQuery.setParameter("meetingId", meetingId);
        theQuery.setParameter("churchId",churchId);
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
              AND q.active   = true 
        """;
        return entityManager.createQuery(jpql, QrCode.class)
                .setParameter("p_date", LocalDate.now())
                .setParameter("meetingId", mettingId)
                .setParameter("churchId", churchId)
                .getResultList();
    }

    @Override
    public QrCode create(QrCode qrCode) {
        entityManager.persist(qrCode);
        entityManager.flush();
        return  qrCode;
    }

    @Override
    public List<QrCode> getPaginatedQrCodes(LocalDate start, LocalDate end, int pageNumber, int pageSize  , int churchId, int mettingId) {
        TypedQuery<QrCode> query = entityManager.createQuery(
                "FROM QrCode WHERE validDate >= :start AND validDate <= :end  AND church.id =: chucrhId AND meetings.id =: meetingId  ORDER BY active DESC , createAt DESC",
                QrCode.class
        );
        query.setParameter("start", start);
        query.setParameter("end", end);
        query.setParameter("chucrhId",churchId);
        query.setParameter("meetingId",mettingId);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        List<QrCode> result = query.getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    @Override
    public void update(QrCode code) {
        entityManager.merge(code);
    }

    @Override
    public QrCode findById(int id , int churchId,int meetingId)
    {
        QrCode qrcode ;
         TypedQuery<QrCode> query  = entityManager.createQuery("FROM QrCode WHERE church.id =: chucrhId AND meetings.id =: meetingId and id = :id ",QrCode.class);
         query.setParameter("chucrhId",churchId);
         query.setParameter("meetingId",meetingId);
         query.setParameter("id",id);
         qrcode                    = query.getSingleResult();
            if(qrcode == null){
                throw  new RuntimeException("We Don`t have Qr Code with this ID ");
            }
            return  qrcode;
    }

    @Override
    public List<QrCode> findAll(LocalDate from , LocalDate to ,int churchId , int meetingId) {
        List <QrCode> qrCodeList ;
        TypedQuery<QrCode> query = entityManager.createQuery("From QrCode q Where validDate >= :from AND  validDate <= :to " +
                "AND q.church.id =:churchId And q.meetings.id =:meetingId ", QrCode.class);
        query.setParameter("from",from);
        query.setParameter("to",to);
        query.setParameter("churchId",churchId);
        query.setParameter("meetingId",meetingId);
        qrCodeList              =   query.getResultList();
        if(qrCodeList == null){
            qrCodeList = new ArrayList<>();
        }
        return  qrCodeList;
    }

}
