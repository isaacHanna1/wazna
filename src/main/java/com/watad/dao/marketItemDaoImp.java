package com.watad.dao;

import com.watad.entity.MarketItem;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class marketItemDaoImp implements MarketItemDao {

    private EntityManager entityManager;

    public marketItemDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public long countByCategory(int categoryId) {
        String jpql = "SELECT COUNT(m) FROM MarketItem m WHERE m.category.id = :categoryId";

        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("categoryId", categoryId)
                .getSingleResult();

        return (count != null) ? count : 0L;
    }

    @Override
    public List<MarketItem> findByCategoryWithPagination(int categoryId, int page, int size) {
        String jpql = " SELECT m FROM MarketItem m WHERE m.category.id = :categoryId order by id ";

        return entityManager.createQuery(jpql, MarketItem.class)
                .setParameter("categoryId", categoryId)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }


}
