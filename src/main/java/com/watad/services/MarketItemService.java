package com.watad.services;

import com.watad.entity.MarketItem;

import java.util.List;

public interface MarketItemService {

    public long countByCategory(int categoryId);
        public List<MarketItem> findByCategoryWithPagination (int categoryId, int page, int size) ;
}
