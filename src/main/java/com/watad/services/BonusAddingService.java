package com.watad.services;

import com.watad.entity.BonusType;
import com.watad.entity.Profile;
import com.watad.entity.User;

public interface BonusAddingService {

    public void addNewBonus(int profileId, int userId, int bonusTypeId) ;
}
