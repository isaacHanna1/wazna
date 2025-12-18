package com.watad.controller;


import com.watad.Common.TimeUtil;
import com.watad.Common.Util;
import com.watad.services.QrCodeService;
import com.watad.services.SystemConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Controller
public class ManaulAttandanceController {

    private final QrCodeService qrCodeService;
    private final SystemConfigService systemConfig;
    private final TimeUtil timeUtil;

    public ManaulAttandanceController(QrCodeService qrCodeService, SystemConfigService systemConfig, TimeUtil timeUtil) {
        this.qrCodeService = qrCodeService;
        this.systemConfig = systemConfig;
        this.timeUtil = timeUtil;
    }

    @GetMapping("/manualAttandance")
    public  String loadPageManalAttance(Model model){
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH);
        String currentDate = timeUtil.now().format(formatter);
        model.addAttribute("currentDate",currentDate);
        List<String> data = qrCodeService.getActiveByDate(timeUtil.now_localDate());
        model.addAttribute("codes",data);
        boolean isChildRegisterionExists = systemConfig.getSystemCongigValueByKey("child_req");
        model.addAttribute("isChildRegisterionExists", isChildRegisterionExists);
        return "manualAttandance";
    }

}
