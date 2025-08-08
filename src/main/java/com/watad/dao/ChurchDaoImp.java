package com.watad.dao;

import com.watad.dto.ChurchDto;
import com.watad.entity.Church;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChurchDaoImp implements  ChurchDao{

    private EntityManager entityManager ;

    public ChurchDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Church> findAll() {
        return entityManager.createQuery("From Church",Church.class).getResultList();
    }

    @Override
    public List<ChurchDto> findByDiocesesId(int diocesesId) {
        String jpql = "SELECT new com.watad.dto.ChurchDto(c.id , c.churchName) FROM Church c WHERE c.diocese.id = :id";

        TypedQuery<ChurchDto> query = entityManager.createQuery(jpql, ChurchDto.class)
                .setParameter("id", diocesesId);
        return query.getResultList();
    }

}
