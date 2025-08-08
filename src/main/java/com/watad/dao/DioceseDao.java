package com.watad.dao;

import com.watad.entity.Dioceses;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface DioceseDao {

    List<Dioceses> findAll();
}
