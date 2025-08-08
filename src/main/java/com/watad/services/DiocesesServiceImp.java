package com.watad.services;


import com.watad.dao.DioceseDao;
import com.watad.entity.Dioceses;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiocesesServiceImp implements DiocesesService{

    private final DioceseDao dioceseDao;

    public DiocesesServiceImp(DioceseDao dioceseDao) {
        this.dioceseDao = dioceseDao;
    }

    @Override
    public List<Dioceses> findAll() {
        return dioceseDao.findAll();
    }
}
