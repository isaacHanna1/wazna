package com.watad.services;

import com.watad.dao.MarketItemDao;
import com.watad.entity.MarketItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketItemServiceImp implements MarketItemService {

    private final MarketItemDao marketItemDao;

    public MarketItemServiceImp(MarketItemDao marketItemDao) {
        this.marketItemDao = marketItemDao;
    }

    @Override
    public long countByCategory(int categoryId) {
        return  marketItemDao.countByCategory(categoryId);
    }

    @Override
    public List<MarketItem> findByCategoryWithPagination(int categoryId, int page, int size) {
                return marketItemDao.findByCategoryWithPagination(categoryId,page,size);
    }
}
