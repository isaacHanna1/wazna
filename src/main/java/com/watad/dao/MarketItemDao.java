package com.watad.dao;

import com.watad.entity.MarketItem;

import java.util.List;

public interface MarketItemDao {

    public long countByCategory(int categoryId) ;
    public List<MarketItem> findByCategoryWithPagination(int categoryId, int page, int size);
}
