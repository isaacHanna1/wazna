package com.watad.dao;

import com.watad.entity.MarketItem;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MarketItemDao {

    public long countByCategory(int categoryId) ;
    public List<MarketItem> findByCategoryWithPagination(int categoryId, int church_id , int meeting_id , int page, int size);
    public void saveItem(MarketItem marketItem );
}
