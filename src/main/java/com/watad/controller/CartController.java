package com.watad.controller;

import com.watad.dto.cart.CartRespond;
import com.watad.entity.SprintData;
import com.watad.enumValues.CartStatus;
import com.watad.services.CartServices;
import com.watad.services.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {

    private final UserServices userServices;
    private final CartServices cartServices;

    @GetMapping("/home")
    public String getCartView(Model model){
        addAttrbuteToModel(model);
        return "cart";
    }

    private void addAttrbuteToModel(Model model) {
        SprintData sprintData       = userServices.getActiveSprint();
        Long itemCount              = cartServices.countItemsBySprintAndUser();
        double waznaSpent           = cartServices.getTotalWaznaSpent();
        long   openItemCount        = cartServices.countItemsBySprintAndUserAndStatus(CartStatus.PENDING);
        long   deliveredCount      = cartServices.countItemsBySprintAndUserAndStatus(CartStatus.DELIVERED);
        List<CartRespond> cartItems = cartServices.getAllCartItem();

        model.addAttribute("currentSprintName",sprintData.getSprintDesc());
        model.addAttribute("totalItems",itemCount);
        model.addAttribute("totalSpentPoints",waznaSpent);
        model.addAttribute("deliveredCount",deliveredCount);
        model.addAttribute("openCount",openItemCount);
        model.addAttribute("cartItems",     cartItems);

    }
}
