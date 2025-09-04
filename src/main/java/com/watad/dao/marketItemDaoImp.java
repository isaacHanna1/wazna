package com.watad.dao;

import com.watad.entity.MarketItem;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

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
    public List<MarketItem> findByCategoryWithPagination(int categoryId, int church_id , int meeting_id ,int page, int size) {
        String jpql = """
                SELECT m FROM MarketItem m WHERE m.category.id = :categoryId 
                AND m.church.id   = :church_id
                AND m.meeting.id  = :meeting_id
                order by id
                """ ;

        return entityManager.createQuery(jpql, MarketItem.class)
                .setParameter("categoryId", categoryId)
                .setParameter("church_id",church_id)
                .setParameter("meeting_id",meeting_id)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public void saveItem(MarketItem marketItem) {
        entityManager.persist(marketItem);
    }


}
