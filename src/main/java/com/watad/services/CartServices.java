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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServices {

    private final CartRepository cartRepository;
    private final UserServices userServices;
    private final TimeUtil timeUtil;
    private final MarketItemService marketItemService;
    private final UserPointTransactionService  userPointTransactionService;


    @Transactional
    public CartRespond saveItemInCart(CartRequest cartReq) {
        MarketItemDto item  = marketItemService.getItemById(cartReq.getItemId());
        int profileId       = userServices.logedInUser().getProfile().getId();
        int sprintId        = userServices.getActiveSprint().getId();
        double reqPoints    = item.getPoints() * cartReq.getItemCount();

        if (!checkBalance(profileId, sprintId, reqPoints)) {
            throw new NotEnoughPointsException("ليس هناك وزنات كافية لاتمام عملية الشراء");
        }

        Cart cart = convertRequestToModelCart(cartReq);
        cart.setStatus(CartStatus.PENDING);
        Cart savedCart = cartRepository.save(cart);

        saveTransaction(
                userServices.logedInUser(),
                reqPoints * -1,
                "شراء " + item.getItemName(),
                userServices.getActiveSprint(),
                "BUY"
        );

        return convertModelToCartResponse(savedCart);
    }

    @Transactional
    public void cancelCartItem(int cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Only PENDING orders can be cancelled
        if (cart.getStatus() != CartStatus.PENDING) {
            throw new RuntimeException("Cannot cancel this order");
        }

        double refundPoints = cart.getWaznaPoints() * cart.getItemCount();

        cart.setStatus(CartStatus.CANCELLED);
        cartRepository.save(cart);

        saveTransaction(
                userServices.logedInUser(),
                refundPoints,
                "الغاء شراء " + cart.getMarketItem().getItemName(),
                userServices.getActiveSprint(),
                "CANCEL"
        );
    }

    public Long countItemsBySprintAndUserAndStatus(CartStatus cartStatus){
        int userId   = userServices.logedInUser().getId();
        int sprintId = userServices.getActiveSprint().getId();
        return  cartRepository.countItemsBySprintAndUserAndStatus(sprintId,userId,cartStatus);
    }
    public Long countItemsBySprintAndUser(){
        int userId   = userServices.logedInUser().getId();
        int sprintId = userServices.getActiveSprint().getId();
        return  cartRepository.countItemsBySprintAndUser(sprintId,userId);
    }
    public List<CartRespond> getAllCartItem(){
        int userId   = userServices.logedInUser().getId();
        int sprintId = userServices.getActiveSprint().getId();
        return cartRepository.findByUserIdAndSprintId(userId,sprintId)
                .stream()
                .map(this::convertModelToCartResponse)
                .collect(Collectors.toList());
    }
    public double getTotalWaznaSpent(){
        return  getAllCartItem().stream()
                .filter(c -> !c.getStatus().equals("CANCELLED"))
                .mapToDouble(cartRespons ->cartRespons.getItemCount()*cartRespons.getPoints())
                .sum();
    }
        public Cart convertRequestToModelCart(CartRequest cartReq) {

            Cart cart = new Cart();
            cart.setMarketItem(new MarketItem(cartReq.getItemId()));
            cart.setItemCount(cartReq.getItemCount());
            cart.setWaznaPoints(marketItemService.getItemById(cartReq.getItemId()).getPoints());
            cart.setStatus(CartStatus.DELIVERED);
            cart.setSprint(userServices.getActiveSprint());
            cart.setChurch(userServices.getLogInUserChurch());
            cart.setMeeting(userServices.getLogInUserMeeting());
            cart.setUser(userServices.logedInUser());
            cart.setPurchaseDate(timeUtil.now());
            return cart;
        }
    public CartRespond convertModelToCartResponse(Cart cart) {
        CartRespond cartRespond = new CartRespond();
        cartRespond.setId(cart.getId());
        cartRespond.setItemCount(cart.getItemCount());
        cartRespond.setPoints(cart.getWaznaPoints());

        cartRespond.setStatus(cart.getStatus().name());
        cartRespond.setPurchaseDate(cart.getPurchaseDate());

        CartRespond.ItemResponse itemResponse = new CartRespond.ItemResponse();
        itemResponse.setName(cart.getMarketItem().getItemName());
        itemResponse.setImageUrl(cart.getMarketItem().getImageName());
        cartRespond.setItem(itemResponse);

        return cartRespond;
    }
    public void saveTransaction(User user , double addPoint,String  usedFor,SprintData sprint , String transactionType){
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
        pointTransaction.setTransactionType(transactionType);
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
