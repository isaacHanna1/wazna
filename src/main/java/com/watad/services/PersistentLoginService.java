package com.watad.services;


import com.watad.entity.PersistentLogin;

import java.util.Optional;

public interface PersistentLoginService {
    void create(PersistentLogin login);
    Optional<PersistentLogin> findBySeries(String series);
    void update(PersistentLogin login);
    void deleteByUsername(String userName);
}
