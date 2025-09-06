package com.watad.services;

import com.watad.dao.MarketItemDao;
import com.watad.dto.MarketItemDto;
import com.watad.entity.Church;
import com.watad.entity.MarketItem;
import com.watad.entity.Meetings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class MarketItemServiceImp implements MarketItemService {

    private final  MarketItemDao marketItemDao;
    private final  UserServices userServices;
    private  final UploadFileServices uploadFileServices;

    @Value("${file.uploadMarket-dir}")
    private String uploadDir;

    public MarketItemServiceImp(MarketItemDao marketItemDao, UserServices userServices, UploadFileServices uploadFileServices) {
        this.marketItemDao = marketItemDao;
        this.userServices = userServices;
        this.uploadFileServices = uploadFileServices;
    }

    @Override
    public long countByCategory(int categoryId) {
        return  marketItemDao.countByCategory(categoryId);
    }

    @Override
    public List<MarketItem> findByCategoryWithPagination(int categoryId ,int page, int size) {
        int church_id  = userServices.getLogInUserChurch().getId();
        int meeting_id = userServices.getLogInUserMeeting().getId();
        return marketItemDao.findByCategoryWithPagination(categoryId,church_id , meeting_id ,page,size);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveItem(MarketItem item, MultipartFile file) {
        String fileName = null;

        try {
            fileName = uploadFileServices.uploadFile(file,uploadDir);
        if(!fileName.equals("")){
            Church church = userServices.getLogInUserChurch();
            Meetings meetings = userServices.getLogInUserMeeting();
            item.setChurch(church);
            item.setMeeting(meetings);
            item.setImageName(fileName);
            item.setStatus(true);
            marketItemDao.saveItem(item);
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MarketItemDto> getMarketItem(int pageNum , int pageSize) {
        int church_id  = userServices.getLogInUserChurch().getId();
        int meeting_id = userServices.getLogInUserMeeting().getId();
        return marketItemDao.getMarketItem(church_id,meeting_id,pageNum,pageSize);
    }

    @Override
    public List<MarketItemDto> searchByItemNameOrDesc(String keyword) {
        int church_id  = userServices.getLogInUserChurch().getId();
        int meeting_id = userServices.getLogInUserMeeting().getId();
        return  marketItemDao.searchByItemNameOrDesc(keyword,church_id,meeting_id);
    }

    @Override
    public MarketItemDto getItemById(int id) {
        return  marketItemDao.getElementById(id);
    }

    @Override
    public MarketItem getEntityItemById(int id) {
        return marketItemDao.getitemById(id);
    }

    @Override
    @Transactional
    public String updateStatus(List<MarketItemDto> list) {
        MarketItem marketItem = null;
        for(MarketItemDto dto : list){
            marketItem           = getEntityItemById(dto.getId());
            if(marketItem == null) continue;marketItem.setStatus(dto.isStatus());
            System.out.println("the status is "+marketItem.getStatus() +" the dto status that come "+dto.isStatus());
        }
        upadteItem(marketItem);
        return "Updated The Status of Items";
    }

    @Override
    @Transactional
    public void upadteItem(MarketItem marketItem) {
        marketItemDao.updateItem(marketItem);
    }


}
