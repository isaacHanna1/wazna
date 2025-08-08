package com.watad.services;

import com.watad.dto.ChurchDto;
import com.watad.entity.Church;

import java.util.List;
import java.util.Optional;

public interface ChurchService {

    List<Church> findAll();
    List<ChurchDto> findByDioceseId(int diocesesId);
}
