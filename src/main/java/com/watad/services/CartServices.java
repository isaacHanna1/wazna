package com.watad.services;


import com.watad.Common.TimeUtil;
import com.watad.dto.MarketItemDto;
import com.watad.dto.cart.CartRequest;
import com.watad.dto.cart.CartRespond;
import com.watad.entity.*;
import com.watad.enumValues.CartStatus;
import com.watad.exceptions.NotEnoughPointsException;
import com.watad.repo.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CartServices {

    private final CartRepository cartRepository;
    private final UserServices userServices;
    private final TimeUtil timeUtil;
    private final MarketItemService marketItemService;
    private final UserPointTransactionService  userPointTransactionService;


    @Transactional
    public CartRespond saveItemInCart(CartRequest cartReq){
        int countOfItem     = cartReq.getItemCount();
        MarketItemDto item = marketItemService.getItemById(cartReq.getItemId());
        double points       = item.getPoints() *countOfItem* -1; //  saved in mins for buy from market
        String itemName     = item.getItemName();
        int profileId       = userServices.logedInUser().getProfile().getId();
        int sprintId        = userServices.getActiveSprint().getId();
        double reqPoints    = item.getPoints()*cartReq.getItemCount();
        boolean valid       = checkBalance(profileId,sprintId,reqPoints);
        if(valid) {
            Cart cart = convertRequestToModelCart(cartReq);
            Cart savedCart = cartRepository.save(cart);
            saveTransaction(userServices.logedInUser(), points, " شراء " + itemName, userServices.getActiveSprint());
            return convertModelToCartResponse(savedCart);
        }else {
            throw new NotEnoughPointsException(" ليس هناك وزنات كافية لاتمام عملية الشراء ");
        }
    }

        private Cart convertRequestToModelCart(CartRequest cartReq) {

            Cart cart = new Cart();
            cart.setMarketItem(new MarketItem(cartReq.getItemId()));
            cart.setItemCount(cartReq.getItemCount());
            cart.setWaznaPoints(marketItemService.getItemById(cartReq.getItemId()).getPoints());
            cart.setStatus(CartStatus.OPEN);
            cart.setSprint(userServices.getActiveSprint());
            cart.setChurch(userServices.getLogInUserChurch());
            cart.setMeeting(userServices.getLogInUserMeeting());
            cart.setUser(userServices.logedInUser());
            cart.setPurchaseDate(timeUtil.now());
            return cart;
        }
        public CartRespond convertModelToCartResponse(Cart cart){
            CartRespond cartRespond = new CartRespond() ;
            cartRespond.setId(cart.getId());
            cartRespond.setItemCount(cart.getItemCount());
            cartRespond.setPoints(cart.getWaznaPoints());
            return  cartRespond;
        }
    public void saveTransaction(User user , double addPoint,String  usedFor,SprintData sprint){
        UserPointTransaction pointTransaction = new UserPointTransaction();
        pointTransaction.setProfile(user.getProfile());
        pointTransaction.setTransferTo(null);
        Profile profile = user.getProfile();
        int churchId    = profile.getChurch().getId();
        int meetingID   = profile.getMeetings().getId();
        pointTransaction.setSprintData(sprint);
        pointTransaction.setPoints(addPoint);
        pointTransaction.setActive(true);
        pointTransaction.setTransactionDate(timeUtil.now());
        pointTransaction.setUsedFor(usedFor);
        pointTransaction.setTransactionType("Buy");
        pointTransaction.setChurch(profile.getChurch());
        pointTransaction.setPointSource("MANUAL");
        pointTransaction.setAddedByProfileId(null);
        pointTransaction.setMeetings(profile.getMeetings());
        pointTransaction.setUserBonus(null);
        userPointTransactionService.save(pointTransaction);
    }
    public boolean checkBalance(int profileId , int sprintId , double requiredPoints){
        double currentPoints = userPointTransactionService.getTotalPointsByProfileIdAndSprintId(profileId,sprintId);
        return  currentPoints >= requiredPoints ;
    }
}
