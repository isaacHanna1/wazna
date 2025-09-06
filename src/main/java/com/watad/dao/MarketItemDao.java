package com.watad.dao;

import com.watad.dto.MarketItemDto;
import com.watad.entity.MarketItem;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MarketItemDao {

    public long countByCategory(int categoryId) ;
    public List<MarketItem> findByCategoryWithPagination(int categoryId, int church_id , int meeting_id , int page, int size);
    public void saveItem(MarketItem marketItem );
    public List<MarketItemDto> getMarketItem(int churchId , int meetingId , int pageNum , int pageSize);
    public List<MarketItemDto> searchByItemNameOrDesc(String keyword, int churchId , int meetingId );
    public MarketItemDto getElementById(int itemId );
    public MarketItem getitemById(int itemId );
    public void updateItem(MarketItem item);
}
