package com.watad.dao;

import com.watad.dto.MarketItemDto;
import com.watad.entity.MarketItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
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
    public long countByCategory(int categoryId , int churchId , int meetingId) {
        String jpql = "SELECT COUNT(m) FROM MarketItem m WHERE m.category.id = :categoryId and m.status = true " +
                      " AND m.church.id =:churchId   AND m.meeting.id =:meetingId";

        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("categoryId", categoryId)
                .setParameter("churchId",churchId)
                .setParameter("meetingId",meetingId)
                .getSingleResult();
        return (count != null) ? count : 0L;
    }

    @Override
    public List<MarketItem> findByCategoryWithPagination(int categoryId, int church_id , int meeting_id ,int page, int size) {
        String jpql = """
                SELECT m FROM MarketItem m WHERE m.category.id = :categoryId 
                AND m.church.id   = :church_id
                AND m.meeting.id  = :meeting_id
                AND m.status      = true
                order by id
                """ ;
        int offset = page*size;

        return entityManager.createQuery(jpql, MarketItem.class)
                .setParameter("categoryId", categoryId)
                .setParameter("church_id",church_id)
                .setParameter("meeting_id",meeting_id)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public void saveItem(MarketItem marketItem) {
        entityManager.merge(marketItem);
    }

    @Override
    public List<MarketItemDto> getMarketItem(int churchId , int meetingId , int pageNum , int pageSize) {
        String sql = """
                SELECT new com.watad.dto.MarketItemDto(m.id,m.itemName,m.itemDesc , m.points , m.status) FROM MarketItem m WHERE 
                       m.church.id       = :church_id
                   AND m.meeting.id      = :meeting_id
                order by m.id desc
                """;
        int offset = (pageNum-1)*pageSize;
        List<MarketItemDto> items = entityManager.createQuery(sql,MarketItemDto.class).setParameter("church_id",churchId)
                .setParameter("meeting_id",meetingId).setFirstResult(offset).setMaxResults(pageSize).getResultList();
        return items;
    }

    @Override
    public List<MarketItemDto> searchByItemNameOrDesc(String keyword, int churchId , int meetingId ) {
        String sql = """
                  SELECT new com.watad.dto.MarketItemDto(m.id,m.itemName,m.itemDesc , m.points , m.status) FROM MarketItem m WHERE 
                   m.church.id       = :church_id
                   AND m.meeting.id  = :meeting_id
                   AND (m.itemDesc   LIKE :keyword OR m.itemName LIKE :keyword)
                """;
        List<MarketItemDto> items = entityManager.createQuery(sql,MarketItemDto.class).setParameter("church_id",churchId)
                .setParameter("meeting_id",meetingId)
                .setParameter("keyword","%" + keyword + "%").setMaxResults(5).getResultList();
        return  items;
    }

    @Override
    public MarketItemDto getElementById(int itemId) {
        String sql = """
                    SELECT new com.watad.dto.MarketItemDto(m.id,m.itemName,m.itemDesc , m.points , m.status, m.stockQuantity) FROM MarketItem m WHERE 
                    m.id = :id 
                """;
        return  entityManager.createQuery(sql,MarketItemDto.class).setParameter("id",itemId).getSingleResult();
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public MarketItemDto getElementByIdWithLock(int itemId) {
        String sql = """
                    SELECT new com.watad.dto.MarketItemDto(m.id,m.itemName,m.itemDesc , m.points , m.status, m.stockQuantity) FROM MarketItem m WHERE 
                    m.id = :id 
                """;
        return  entityManager.createQuery(sql,MarketItemDto.class).setParameter("id",itemId).getSingleResult();
    }

    @Override
    public MarketItem getitemById(int itemId) {
        return entityManager.find(MarketItem.class , itemId);
    }

    @Override
    public void updateItem(MarketItem item) {
        entityManager.merge(item);
    }

    @Override
    public int getMarketItemSize(int churchId , int meetingId) {
        String sql = """
                        Select count(*) from MarketItem  m where
                        m.church.id       = :church_id
                        AND m.meeting.id  = :meeting_id
                """;
               Long count =  (Long)entityManager.createQuery(sql).setParameter("church_id",churchId)
                       .setParameter("meeting_id",meetingId).getSingleResult();
                return count.intValue();

    }

    public MarketItem findAndLock(int id){
        return entityManager.find(
                MarketItem.class,
                id,
                LockModeType.OPTIMISTIC
        );
    }


}
