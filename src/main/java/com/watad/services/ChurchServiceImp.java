package com.watad.services;

import com.watad.dao.ChurchDao;
import com.watad.dto.ChurchDto;
import com.watad.entity.Church;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChurchServiceImp implements ChurchService{
    private ChurchDao churchDao;

    public ChurchServiceImp(ChurchDao churchDao) {
        this.churchDao = churchDao;
    }
    @Override
    public List<Church> findAll() {
        return  churchDao.findAll();
    }

    @Override
    public List<ChurchDto> findByDioceseId(int diocesesId) {
        return churchDao.findByDiocesesId(diocesesId);
    }
}
