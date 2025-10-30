package com.watad.services;

import com.watad.entity.Dioceses;

import java.util.List;

public interface DiocesesService {

    List<Dioceses> findAll();
    Dioceses findById(int diocesesId);
}
