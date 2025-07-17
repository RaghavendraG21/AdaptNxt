package com.ecommerce.api.controller;

import com.ecommerce.api.dto.CartItemDTO;
import com.ecommerce.api.model.CartItem;
import com.ecommerce.api.model.User;
import com.ecommerce.api.service.CartService;
import com.ecommerce.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired private CartService cartService;
    @Autowired private UserService userService;

    private User getAppUser(String username) {
        return userService.findByUsername(username).orElseThrow();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartItemDTO dto, Authentication authentication) {
        String username = authentication.getName();
        User user = getAppUser(username);
        cartService.addItemToCart(user, dto);
        return ResponseEntity.ok("Item added to cart");
    }

    @GetMapping
    public List<CartItem> viewCart(Authentication authentication) {
        String username = authentication.getName();
        User user = getAppUser(username);
        return cartService.getCartItems(user);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<String> updateCart(
            @PathVariable Long productId,
            @RequestParam int quantity,
            Authentication authentication) {
        String username = authentication.getName();
        User user = getAppUser(username);
        cartService.updateItem(user, productId, quantity);
        return ResponseEntity.ok("Cart updated");
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeItem(@PathVariable Long productId, Authentication authentication) {
        String username = authentication.getName();
        User user = getAppUser(username);
        cartService.removeItem(user, productId);
        return ResponseEntity.ok("Item removed");
    }
}
