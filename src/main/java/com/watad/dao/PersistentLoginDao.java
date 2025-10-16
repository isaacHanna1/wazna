package com.watad.dao;

import com.watad.entity.PersistentLogin;

import java.util.Optional;

public interface PersistentLoginDao {

    void save(PersistentLogin login);
    Optional<PersistentLogin> findBySeries(String series);
    void update(PersistentLogin login);
    void deleteByUserName(String userName);
}
