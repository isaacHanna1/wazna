package com.watad.controller;


import com.watad.Common.Util;
import com.watad.services.QrCodeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ManaulAttandanceController {

    private final QrCodeService qrCodeService;

    public ManaulAttandanceController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }
    @GetMapping("/manualAttandance")
    public  String loadPageManalAttance(Model model){
        String currentDate = Util.getCurrentDate("EEEE, MMMM d, yyyy");
        model.addAttribute("currentDate",currentDate);
        List<String> data = qrCodeService.getActiveByDate(LocalDate.now());
        System.out.println("the data size is "+data.size()+" , "+ "the date is "+LocalDate.now());
        for(int i = 0 ;i <data.size() ; i++){
            System.out.println(data.get(i));
        }
        model.addAttribute("codes",qrCodeService.getActiveByDate(LocalDate.now()));
        return "manualAttandance";
    }

}
