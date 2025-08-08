package com.watad.dao;

import com.watad.dto.ChurchDto;
import com.watad.entity.Church;

import java.util.List;
import java.util.Optional;

public interface ChurchDao {

   List<Church> findAll();
   List<ChurchDto> findByDiocesesId(int diocesesId );
}
