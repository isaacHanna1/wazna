package com.watad.controller;



import com.watad.dto.cart.CartRequest;
import com.watad.dto.cart.CartRespond;
import com.watad.enumValues.CartStatus;
import com.watad.services.CartServices;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@Data
public class cartRestController {

    private final CartServices cartServices;

    @PostMapping
    public ResponseEntity<CartRespond> create(@RequestBody CartRequest cartRequest){
        CartRespond cartRespond = cartServices.saveItemInCart(cartRequest);
        return ResponseEntity.ok(cartRespond);
    }
    @GetMapping("/count")
    public ResponseEntity<Long> countOpenItemsBySprintAndUser(@RequestParam CartStatus cartStatus){
        Long count = cartServices.countItemsBySprintAndUserAndStatus(cartStatus);
        return  ResponseEntity.ok(count);
    }

    @PostMapping("/cancel/{cartId}")
    public ResponseEntity<?> cancelItem(@PathVariable int cartId) {
        try {
            cartServices.cancelCartItem(cartId);
            return ResponseEntity.ok("Order cancelled successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
