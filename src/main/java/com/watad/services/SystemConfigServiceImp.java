package com.watad.services;

import com.watad.dao.SystemConfigDao;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigServiceImp implements  SystemConfigService{

    private final SystemConfigDao systemConfigDao;

    public SystemConfigServiceImp(SystemConfigDao systemConfigDao) {
        this.systemConfigDao = systemConfigDao;
    }

    @Override
    public String getSystemCongigValueByKey(String key) {
        return systemConfigDao.getSystemConfigValueByKey(key);
    }

}
