package com.watad.services;

import com.watad.dao.MarketCategoryDao;
import com.watad.entity.MarketCategory;
import com.watad.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketCategoryServiceImp implements MarketCategoryService{


    private MarketCategoryDao marketCategoryDao;
    private final UserServices userServices;

    public MarketCategoryServiceImp(UserServices userServices, MarketCategoryDao marketCategoryDao) {
        this.userServices = userServices;
        this.marketCategoryDao = marketCategoryDao;
    }

    @Override
    public List<MarketCategory> allActiveCategory() {
        User user        = userServices.logedInUser();
        int theMeeting   = userServices.getLogInUserMeeting().getId();
        int theChurch    = userServices.getLogInUserChurch().getId();
        return  marketCategoryDao.allActiveCategory(theMeeting,theChurch);
    }
}
