package com.watad.services;

import com.watad.dao.UserBounsDao;
import com.watad.entity.UserBonus;
import org.springframework.stereotype.Service;

@Service
public class UserBounsServiceImp implements UserBounsService {

    private final UserBounsDao userBounsDao;

    public UserBounsServiceImp(UserBounsDao userBounsDao) {
        this.userBounsDao = userBounsDao;
    }

    @Override
    public void save(UserBonus userBonus) {
            userBounsDao.save(userBonus);
    }
}
