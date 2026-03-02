package com.watad.services;

import com.watad.dto.MarketItemDto;
import com.watad.entity.MarketItem;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MarketItemService {

    public long countByCategory(int categoryId);
        public List<MarketItem> findByCategoryWithPagination (int categoryId,int page, int size) ;
        public  void saveItem (MarketItem item , MultipartFile file) throws IOException;
        public List<MarketItemDto> getMarketItem(int pageNum , int pageSize);
        public List<MarketItemDto> searchByItemNameOrDesc(String keyword);
        public  MarketItemDto getItemById(int id );
        public  MarketItem getEntityItemById(int id );
        public String updateStatus(List<MarketItemDto> list);
        public void upadteItem(MarketItem marketItem);
        public  int getMarketItemCount();
        public void updateStock(int itemId, int quantity);
}
