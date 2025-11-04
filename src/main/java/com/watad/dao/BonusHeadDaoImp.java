package com.watad.dao;

import com.watad.entity.BonusHead;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public class BonusHeadDaoImp implements BonusHeadDao{

    private final EntityManager entityManager;


    public BonusHeadDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<BonusHead> findAllBonusHead() {
        return  entityManager.createQuery("From BonusHead bh where bh.active = true ",BonusHead.class).getResultList();
    }

    @Override
    public List<BonusHead> findBYEvaluationType(String type) {
        return  entityManager.createQuery("From BonusHead bh where bh.active = true AND evaluationType =:type ",BonusHead.class).setParameter("type",type).getResultList();

    }

}
