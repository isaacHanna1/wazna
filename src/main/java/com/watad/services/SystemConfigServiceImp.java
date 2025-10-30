package com.watad.services;

import com.watad.dao.SystemConfigDao;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigServiceImp implements  SystemConfigService{

    private final SystemConfigDao systemConfigDao;
    private final UserServices userServices;

    public SystemConfigServiceImp(SystemConfigDao systemConfigDao, UserServices userServices) {
        this.systemConfigDao = systemConfigDao;
        this.userServices = userServices;
    }

    @Override
    public boolean getSystemCongigValueByKey(String key) {
        int meetingId  = userServices.getLogInUserMeeting().getId();
        System.out.println("the meeting id is "+meetingId +" the key is "+key);
        return systemConfigDao.getSystemConfigValueByKey(key,meetingId);
    }

}
