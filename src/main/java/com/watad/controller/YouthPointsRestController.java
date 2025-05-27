package com.watad.controller;

import com.watad.dto.ProfileDtlDto;
import com.watad.dto.response.YouthRankDto;
import com.watad.services.BonusAddingService;
import com.watad.services.UserPointTransactionService;
import com.watad.services.YouthRankService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class YouthPointsRestController {

    private  final YouthRankService youthRankService;
    private final UserPointTransactionService userPointTransactionService;
    private final BonusAddingService bonusAddingService;
    public YouthPointsRestController(YouthRankService youthRankService , UserPointTransactionService userPointTransactionService ,BonusAddingService bonusAddingService) {
        this.youthRankService = youthRankService;
        this.userPointTransactionService = userPointTransactionService;
        this.bonusAddingService = bonusAddingService;
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
}
