package com.watad.dao;

import com.watad.dto.BonusTypeDto;
import com.watad.entity.BonusType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BonusTypeDaoImp implements BonusTypeDao {

    private final EntityManager entityManager;

    public BonusTypeDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public BonusType getBonusTypeByDescription(String description ,  int churchId  , int meetingId ) {
        try {
            TypedQuery<BonusType> query = entityManager.createQuery(
                    "FROM BonusType WHERE description = :description " +
                            "AND activeFrom <= :now " +
                            "AND (activeTo IS NULL OR :now <= activeTo) AND" +
                            " meetings.id = :meetindId AND  church.id = :churchId  ", BonusType.class);

            query.setParameter("description", description);
            query.setParameter("now", LocalDateTime.now());
            query.setParameter("meetindId",meetingId);
            query.setParameter("churchId",churchId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // or throw a custom exception if preferred
        }
    }

    @Override
    public List<BonusTypeDto> findAll() {
        LocalDateTime now               = LocalDateTime.now();
        TypedQuery<BonusTypeDto> query = entityManager.createQuery(
                "SELECT new com.watad.dto.BonusTypeDto(b.id, b.description ,b.point)  FROM BonusType b " +
                        "WHERE b.activeFrom <= :now " +
                        "AND (b.activeTo IS NULL OR :now <= b.activeTo)",
                BonusTypeDto.class
        );
        query.setParameter("now",now);
        return query.getResultList();
    }
    @Override
    public BonusType findById(int id) {
        return  entityManager.find(BonusType.class , id);
    }

}
