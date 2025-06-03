package com.watad.dao;


import com.watad.entity.MarketCategory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class MarketCategoryDaoImp implements MarketCategoryDao{

    private  final EntityManager entityManager;

    public MarketCategoryDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<MarketCategory> allActiveCategory(int meeting_id , int church_id) {
        String nativeQuery = """
                SELECT id, description  FROM wazna.market_category 
                where meeting_id = :meeting_id and church_id = :church_id and active = 1 ;
                """;
        List<Object[]> results = entityManager.createNativeQuery(nativeQuery)
                .setParameter("meeting_id", meeting_id)
                .setParameter("church_id", church_id)
                .getResultList();
        List<MarketCategory> allActiveCategory = new ArrayList<>();
        MarketCategory marketCategory = null;
        for (Object[] row : results) {
            int id = ((Number) row[0]).intValue();
            String desc = (String) row[1];
            marketCategory = new MarketCategory(id, desc);
            allActiveCategory.add(marketCategory);
        }
        return allActiveCategory;
    }
}
