package com.watad.dao;

import com.watad.entity.ServiceStage;

import java.util.List;

public interface ServiceStageDao {

    List<ServiceStage> findAll();
    ServiceStage findById(int id);
}
