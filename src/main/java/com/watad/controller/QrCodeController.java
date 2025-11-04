package com.watad.controller;

import com.watad.Common.TimeUtil;
import com.watad.dto.QRCodeDto;
import com.watad.entity.QrCode;
import com.watad.services.BonusAddingService;
import com.watad.services.BonusTypeService;
import com.watad.services.QrCodeService;
import com.watad.wrapper.QrCodeWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/qr")
public class QrCodeController {

    private final QrCodeService qrCodeService;
    private final BonusTypeService bonusTypeService;

    @Autowired
    private TimeUtil timeUtil;

    public QrCodeController(QrCodeService qrCodeService, BonusTypeService bonusTypeService) {
        this.qrCodeService = qrCodeService;
        this.bonusTypeService = bonusTypeService;
    }

    @PostMapping("/code")
    public String  createQrCode(@ModelAttribute QrCode qrCode , RedirectAttributes redirectAttributes){
        String msg  = "";
        String type = "";
        try {
            QrCode created = qrCodeService.create(qrCode);
           msg  = "we Create the QR Code : "+created.getCode();
           type = "success";
        }catch (DataIntegrityViolationException ex){
            msg  = "We have QrCode with this name ";
            type = "error";
        } catch (RuntimeException e) {
            msg  = "We have internal error ";
            type = "error";
        }

        redirectAttributes.addFlashAttribute("msg",msg);
        redirectAttributes.addFlashAttribute("type",type);
        return  "redirect:/qr/viewEditQrCode";
    }


    @GetMapping("/code")
    public String qrCodeView(Model model){
        model.addAttribute("qrCode",new QrCode());
        model.addAttribute("bonusType",bonusTypeService.findAll("PO"));
        return  "QRCode";
    }

    @GetMapping("/viewEditQrCode")
    public String editQrCodeView(
            Model model,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

         fromDate = (fromDate == null)? timeUtil.now_localDate() :fromDate;
         toDate = (toDate == null) ? fromDate.plusMonths(1) : toDate;
        List<QrCode> list = qrCodeService.getPaginatedQrCodes(fromDate, toDate ,pageNum,pageSize);
        QrCodeWrapper wrapper = new QrCodeWrapper();
        List<QRCodeDto> dtoList = new ArrayList<>();
        for (QrCode code : list){
            QRCodeDto dto = new QRCodeDto();
            dto.setId(code.getId());
            dto.setActive(code.isActive());
            dtoList.add(dto);
        }
        wrapper.setList(dtoList);
        prepareModel(model,list,fromDate,toDate,pageSize,pageNum);
        model.addAttribute("qrCodeFormWrapper",wrapper);
        return  "QrCodeView";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("qrCodeFormWrapper") QrCodeWrapper qrCodeFormWrapper , @RequestParam LocalDate fromDate , @RequestParam LocalDate toDate , Model model
                        ,@RequestParam(defaultValue = "1") int pageNum , @RequestParam(defaultValue = "10") int pageSize) {
        List<QrCode> list = qrCodeService.getPaginatedQrCodes(fromDate,toDate,pageNum,pageSize);
        for(QRCodeDto dto : qrCodeFormWrapper.getList()){
           qrCodeService.update(dto);
        }

        prepareModel(model,list,fromDate,toDate,pageSize,pageNum);
        return  "QrCodeView";
    }
    private int numOfPages(int pageSize ,LocalDate from , LocalDate to) {
        int numOfPages = 1;
        int numOfRow = qrCodeService.findAll(from , to).size();

        if (numOfRow > 0 && pageSize > 0) {
            numOfPages = (numOfRow + pageSize - 1) / pageSize;
        }

        return numOfPages;
    }

    private void prepareModel(Model model ,List<QrCode> list , LocalDate from , LocalDate to , int pageSize , int pageNum ){
        model.addAttribute("qrCodeList",list);
        model.addAttribute("start",from);
        model.addAttribute("end",to );
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("pageNum",pageNum);
        model.addAttribute("numOfPages",numOfPages(pageSize,from,to));
        model.addAttribute("currentPage", pageNum);

    }


}
