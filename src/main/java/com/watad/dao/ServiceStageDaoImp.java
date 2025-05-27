package com.watad.dao;

import com.watad.entity.ServiceStage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ServiceStageDaoImp implements  ServiceStageDao{

    private final EntityManager entityManager;

    public ServiceStageDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ServiceStage> findAll() {

        List <ServiceStage> theServicesStages = new ArrayList<>();
        TypedQuery<ServiceStage> theQurery = entityManager.createQuery("From ServiceStage ",ServiceStage.class);
        theServicesStages =  theQurery.getResultList();
        return theServicesStages;
    }

    @Override
    public ServiceStage findById(int id) {
        return  entityManager.find(ServiceStage.class,id);
    }
}
