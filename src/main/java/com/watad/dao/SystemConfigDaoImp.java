package com.watad.dao;

import com.watad.entity.SystemConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

@Repository
public class SystemConfigDaoImp implements SystemConfigDao {

    private final EntityManager entityManager;

    public SystemConfigDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean getSystemConfigValueByKey(String key, int meetingId) {
        boolean exists = false;
        try {
             exists = entityManager.createQuery(
                            """
                                    SELECT COUNT(c)
                                    FROM SystemConfig c
                                    WHERE c.configKey = :configKey
                                      AND c.meetingId = :meetingId
                                    """,
                            Long.class
                    )
                    .setParameter("configKey", key)
                    .setParameter("meetingId", meetingId)
                    .getSingleResult() > 0;
        } catch (NoResultException ex) {
            throw new NoResultException("This Configuration Is Missed from DataBase, Please Check");
        }
    return exists;
}
}