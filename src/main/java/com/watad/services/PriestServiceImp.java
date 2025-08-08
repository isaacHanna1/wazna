package com.watad.services;

import com.watad.dao.PriestDao;
import com.watad.dto.PriestDto;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PriestServiceImp implements PriestService{

    private final PriestDao priestDao  ;

    public PriestServiceImp(PriestDao priestDao) {
        this.priestDao = priestDao;
    }

    @Override
    public List<PriestDto> findByDioceses(int diocesId) {
        return priestDao.findByDioceses(diocesId);
    }
}
