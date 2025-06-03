package com.watad.services;


import com.watad.entity.MarketCategory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MarketCategoryService {
    public List<MarketCategory> allActiveCategory();
}
