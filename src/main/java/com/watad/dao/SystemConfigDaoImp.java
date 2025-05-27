package com.watad.dao;

import com.watad.entity.SystemConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

@Repository
public class SystemConfigDaoImp implements SystemConfigDao{

    private final EntityManager entityManager;

    public SystemConfigDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public String getSystemConfigValueByKey(String key) {
        try {
            SystemConfig config = entityManager.find(SystemConfig.class, key);
            return config.getConfigValue();
        } catch (NoResultException ex){
            throw  new NoResultException("This Configuration Is Missed from DataBase, Please Check");
        }
    }
}
