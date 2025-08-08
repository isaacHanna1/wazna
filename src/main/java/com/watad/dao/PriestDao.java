package com.watad.dao;

import com.watad.dto.PriestDto;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PriestDao {

    List<PriestDto> findByDioceses(int diocesID);
}
