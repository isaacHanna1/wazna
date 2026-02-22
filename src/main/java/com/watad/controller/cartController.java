package com.watad.controller;



import com.watad.dto.cart.CartRequest;
import com.watad.dto.cart.CartRespond;
import com.watad.services.CartServices;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
@Data
public class cartController {

    private final CartServices cartServices;

    @PostMapping
    public ResponseEntity<CartRespond> create(@RequestBody CartRequest cartRequest){
        CartRespond cartRespond = cartServices.saveItemInCart(cartRequest);
        return ResponseEntity.ok(cartRespond);
    }
}
