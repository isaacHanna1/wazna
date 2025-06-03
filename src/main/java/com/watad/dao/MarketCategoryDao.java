package com.watad.dao;

import com.watad.entity.MarketCategory;

import java.util.List;

public interface MarketCategoryDao {

    List<MarketCategory> allActiveCategory(int meeting_id , int church_id);
}
