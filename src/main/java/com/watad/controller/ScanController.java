package com.watad.controller;


import com.watad.Common.TimeUtil;
import com.watad.dto.PointsSummaryDTO;
import com.watad.entity.User;
import com.watad.exceptions.QrCodeException;
import com.watad.services.AttendanceProcessingService;
import com.watad.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ScanController {

    private final  UserServices userServices;
    private final AttendanceProcessingService processingService ;
    private final TimeUtil timeUtil;

    @GetMapping("/scan")
    public String scanPage(Model model){
        model.addAttribute("userId",userServices.logedInUser().getId());
        return "scan";
    }

    @GetMapping("/notifyWithPoints")
    public String pointsNotification(@RequestParam("points") Double points,
                                     @RequestParam("balance") Double balance, Model model) {
        model.addAttribute("points", points);
        model.addAttribute("balance", balance);
        return "pointsNotification";
    }

    @GetMapping("/scan/{code}")
    public String attendance(@PathVariable String code,
                               Model model) {
        try {
            User user;
                user = userServices.logedInUser();

                // when user scan with mobile so the date must be Now not entered manual
            PointsSummaryDTO pointsSummaryDTO = processingService.attendanceProcessing(user, code,timeUtil.now_localDate(),timeUtil.now_localTime());
            model.addAttribute("points", pointsSummaryDTO.getPoints());
            model.addAttribute("balance", pointsSummaryDTO.getBalance());
            return "pointsNotification";
        }catch (QrCodeException ex) {
            model.addAttribute("errorMessage", "QR Error: " + ex.getMessage());
            return "error";
        } catch (DataIntegrityViolationException ex) {
            model.addAttribute("errorMessage", "You have already scanned this QR .");
            return "error";
        } catch (Exception ex) {
            System.out.println("the exception is isaac -> "+ex.getMessage());
            model.addAttribute("errorMessage", "Unexpected error: " + "The Development Team will Check");
            return "error";
        }
    }
}
