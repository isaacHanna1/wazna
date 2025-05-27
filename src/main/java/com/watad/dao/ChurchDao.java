package com.watad.dao;

import com.watad.entity.Church;

import java.util.List;
import java.util.Optional;

public interface ChurchDao {

   List<Church> findAll();
}
