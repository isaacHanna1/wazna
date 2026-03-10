package com.watad.controller.marketDashboard;

import com.watad.dto.marketDashboard.ByItemDto;
import com.watad.dto.marketDashboard.BySupplierDto;
import com.watad.dto.marketDashboard.ByUserDto;
import com.watad.services.marketDashboard.MarketDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dashboard/sprint")
@RequiredArgsConstructor
public class marketDashBoardRestController {

    private final MarketDashboardService dashboardService;

    @GetMapping("{sprintId}/by-item")
    public List<ByItemDto> getSprintSalesByItem(@PathVariable  int sprintId){
        return dashboardService.getSprintSalesByItem(sprintId);
    }

    @GetMapping("{sprintId}/by-supplier")
    public List<BySupplierDto> getSprintSalesBySupplier(@PathVariable int sprintId) {
        return  dashboardService.getSprintSalesBySupplier(sprintId);
    }

    @GetMapping("{sprintId}/by-user")
    public List<ByUserDto> getSprintOrdersByUser(@PathVariable int sprintId) {
        return  dashboardService.getSprintOrdersByUser(sprintId);
    }





}
