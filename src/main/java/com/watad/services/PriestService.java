package com.watad.services;

import com.watad.dto.PriestDto;

import java.util.List;

public interface PriestService {

    public List<PriestDto> findByDioceses(int diocesId) ;
    }
