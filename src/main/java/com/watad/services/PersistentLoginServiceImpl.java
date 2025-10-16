package com.watad.services;


import com.watad.dao.PersistentLoginDao;
import com.watad.entity.PersistentLogin;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PersistentLoginServiceImpl implements PersistentLoginService{

    private final PersistentLoginDao dao;

    public PersistentLoginServiceImpl(PersistentLoginDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional
    public void create(PersistentLogin login) {
        dao.save(login);
    }

    @Override
    public Optional<PersistentLogin> findBySeries(String series) {
        return dao.findBySeries(series);
    }

    @Override
    @Transactional
    public void update(PersistentLogin login) {
        dao.update(login);
    }

    @Override
    @Transactional
    public void deleteByUsername(String userName) {
        dao.deleteByUserName(userName);
    }
}
