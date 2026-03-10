package com.watad.services.marketDashboard;


import com.watad.dto.marketDashboard.ByItemDto;
import com.watad.dto.marketDashboard.BySupplierDto;
import com.watad.dto.marketDashboard.ByUserDto;
import com.watad.enumValues.CartStatus;
import com.watad.repo.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketDashboardService {

    private final CartRepository cartRepository;

    public List<ByItemDto> getSprintSalesByItem(int sprintId) {
        return cartRepository.findByItem(sprintId);
    }

    public List<BySupplierDto> getSprintSalesBySupplier(int sprintId) {
        return cartRepository.findBySupplier(sprintId);
    }

    public List<ByUserDto> getSprintOrdersByUser(int sprintId) {
        return cartRepository.findByUser(sprintId);
    }


}
