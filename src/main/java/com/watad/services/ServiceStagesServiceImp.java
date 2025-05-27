package com.watad.services;

import com.watad.dao.ServiceStageDao;
import com.watad.entity.ServiceStage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceStagesServiceImp implements ServiceStagesService{

    private final ServiceStageDao serviceStageDao;

    public ServiceStagesServiceImp(ServiceStageDao serviceStageDao) {
        this.serviceStageDao = serviceStageDao;
    }

    @Override
    public List<ServiceStage> findAll() {
        return serviceStageDao.findAll();
    }

    @Override
    public ServiceStage findById(int id) {
        return serviceStageDao.findById(id);
    }
}
