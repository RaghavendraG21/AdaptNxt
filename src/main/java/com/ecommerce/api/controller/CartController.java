package com.ecommerce.api.controller;

import com.ecommerce.api.dto.CartItemDTO;
import com.ecommerce.api.model.CartItem;
import com.ecommerce.api.model.User;
import com.ecommerce.api.service.CartService;
import com.ecommerce.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart") // Removed /api prefix (already handled in route mapping)
public class CartController {

    @Autowired private CartService cartService;
    @Autowired private UserService userService;

    // ✅ Use Spring's UserDetails, then map to your User entity
    private User getAppUser(UserDetails userDetails) {
        return userService.findByUsername(userDetails.getUsername()).orElseThrow();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartItemDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAppUser(userDetails);
        cartService.addItemToCart(user, dto);
        return ResponseEntity.ok("Item added to cart");
    }

    @GetMapping
    public List<CartItem> viewCart(@AuthenticationPrincipal UserDetails userDetails) {
        User user = getAppUser(userDetails);
        return cartService.getCartItems(user);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<String> updateCart(
            @PathVariable Long productId,
            @RequestParam int quantity,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAppUser(userDetails);
        cartService.updateItem(user, productId, quantity);
        return ResponseEntity.ok("Cart updated");
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeItem(@PathVariable Long productId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAppUser(userDetails);
        cartService.removeItem(user, productId);
        return ResponseEntity.ok("Item removed");
    }
}
