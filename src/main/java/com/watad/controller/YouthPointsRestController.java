package com.watad.controller;

import com.watad.dao.ProfileDao;
import com.watad.dto.PointTransactionSummaryDto;
import com.watad.dto.ProfileDtlDto;
import com.watad.dto.response.YouthRankDto;
import com.watad.entity.Profile;
import com.watad.services.BonusAddingService;
import com.watad.services.ProfileService;
import com.watad.services.UserPointTransactionService;
import com.watad.services.YouthRankService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class YouthPointsRestController {

    private  final YouthRankService youthRankService;
    private final UserPointTransactionService userPointTransactionService;
    private final BonusAddingService bonusAddingService;
    private final ProfileDao profileDao;

    public YouthPointsRestController(YouthRankService youthRankService, UserPointTransactionService userPointTransactionService, BonusAddingService bonusAddingService, ProfileDao profileDao) {
        this.youthRankService = youthRankService;
        this.userPointTransactionService = userPointTransactionService;
        this.bonusAddingService = bonusAddingService;
        this.profileDao = profileDao;
    }

    @GetMapping("/youth/rank/top")
    public List<YouthRankDto> getYouthWithHighestPoints(@RequestParam int limit,
                                                        @RequestParam int offset) {
        return  youthRankService.getRankedYouth(limit,offset);
    }

    @GetMapping("/youth/point/{userName}")
    public List<ProfileDtlDto> getProfilesPoint(@PathVariable String userName){
            return userPointTransactionService.findProfileByUserName(userName);
    }


    @PostMapping("/youth/point/{profileId}/{userId}/{bonusTypeId}")
    public void addPoints(@PathVariable int profileId , @PathVariable int userId , @PathVariable int bonusTypeId){
        bonusAddingService.addNewBonus(profileId,userId,bonusTypeId);
    }


    // start Api for getting youth rank with images, name, class, rank
    @GetMapping("/youth/rank/all")
    public List<YouthRankDto> gettingRankWithImage(@RequestParam(defaultValue = "0") int offset , @RequestParam(defaultValue = "20") int limt){
        return youthRankService.getRankedYouthWithImage(limt,offset);
    }
    // start Api for getting youth rank with images, name, class, rank
    @GetMapping("/youth/rank/find")
    public List<YouthRankDto> gettingRankWithImageByUserName(@RequestParam(defaultValue = "10") int limt , @RequestParam String userName){
        return youthRankService.getRankedYouthWithImageByUserName(limt,userName);
    }
    @GetMapping("/youth/transaction/details/{profileId}")
    public  List<PointTransactionSummaryDto> transactionSummary(@PathVariable int profileId){
        List<PointTransactionSummaryDto> summaryOfPoints = userPointTransactionService.getSummaryOfPoints(profileId);
        return  summaryOfPoints;
    }
}
