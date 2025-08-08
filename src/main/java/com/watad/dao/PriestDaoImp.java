package com.watad.dao;

import com.watad.dto.PriestDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PriestDaoImp implements PriestDao{

    private EntityManager entityManager;

    public PriestDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }



    @Override
    public List<PriestDto> findByDioceses(int diocesId) {
        TypedQuery<PriestDto> query = entityManager.createQuery("Select new com.watad.dto.PriestDto(p.id,p.name) From Priest p JOIN p.dioceses d where d.id = :id", PriestDto.class);
        query.setParameter("id",diocesId);
        return query.getResultList();
    }
}
