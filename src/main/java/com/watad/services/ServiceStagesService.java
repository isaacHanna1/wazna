package com.watad.services;

import com.watad.entity.ServiceStage;

import java.util.List;

public interface ServiceStagesService {

    List<ServiceStage> findAll();
    ServiceStage findById(int id);
}
