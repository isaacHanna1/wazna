package com.watad.services;

import com.watad.entity.MarketItem;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MarketItemService {

    public long countByCategory(int categoryId);
        public List<MarketItem> findByCategoryWithPagination (int categoryId,int page, int size) ;
        public  void saveItem (MarketItem item , MultipartFile file);
        
}
