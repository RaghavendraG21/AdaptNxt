package com.ecommerce.api.controller;

import com.ecommerce.api.dto.OrderRequest;
import com.ecommerce.api.model.Order;
import com.ecommerce.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired private OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest request, Authentication authentication) {
        String username = authentication.getName();
        Order order = orderService.placeOrder(username, request);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getUserOrders(Authentication authentication) {
        String username = authentication.getName();
        List<Order> orders = orderService.getOrdersForUser(username);
        return ResponseEntity.ok(orders);
    }
}
