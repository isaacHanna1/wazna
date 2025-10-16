package com.watad.dao;

import com.watad.entity.PersistentLogin;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PersistentLoginDaoImp implements PersistentLoginDao{

    private final EntityManager entityManager;

    public PersistentLoginDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(PersistentLogin login) {
        entityManager.persist(login);
    }

    @Override
    public Optional<PersistentLogin> findBySeries(String series) {
        PersistentLogin login = entityManager.find(PersistentLogin.class, series);
        return Optional.ofNullable(login);
    }

    @Override
    public void update(PersistentLogin login) {
        entityManager.merge(login);
    }

    @Override
    public void deleteByUserName(String userName) {
        entityManager.createQuery("DELETE FROM PersistentLogin p WHERE p.username = :username")
                .setParameter("username", userName)
                .executeUpdate();
    }
}
