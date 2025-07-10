//package com.ecommerce.api.service;
//
//import com.ecommerce.api.model.CartItem;
//import com.ecommerce.api.model.Order;
//import com.ecommerce.api.model.Product;
//import com.ecommerce.api.model.User;
//import com.ecommerce.api.repository.CartItemRepository;
//import com.ecommerce.api.repository.OrderRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//@Service
//public class OrderService {
//
//    @Autowired private OrderRepository orderRepo;
//    @Autowired private CartItemRepository cartRepo;
//    @Autowired private CartService cartService;
//
//    public void placeOrder(User user) {
//        List<CartItem> cartItems = cartRepo.findByUser(user);
//        if (cartItems.isEmpty()) throw new RuntimeException("Cart is empty");
//
//        List<Product> products = new ArrayList<>();
//        for (CartItem item : cartItems) {
//            products.add(item.getProduct());
//        }
//
//        Order order = new Order();
//        order.setUser(user);
//        order.setProducts(products);
//        order.setOrderDate(new Date());
//
//        orderRepo.save(order);
//        cartService.clearCart(user);
//    }
//
//    public List<Order> getUserOrders(User user) {
//        return orderRepo.findByUser(user);
//    }
//}






package com.ecommerce.api.service;

import com.ecommerce.api.dto.OrderItemDTO;
import com.ecommerce.api.dto.OrderRequest;
import com.ecommerce.api.model.*;
import com.ecommerce.api.model.OrderItem;
import com.ecommerce.api.repository.OrderRepository;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private UserRepository userRepo;

    public Order placeOrder(String username, OrderRequest request) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PLACED);
        order.setShippingAddress(request.getShippingAddress());
        order.setPaymentMethod(request.getPaymentMethod());

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for (OrderItemDTO itemDto : request.getItems()) {
            Product product = productRepo.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(product.getPrice());
            orderItems.add(item);

            total += product.getPrice() * itemDto.getQuantity();
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        return orderRepo.save(order);
    }

    public List<Order> getOrdersForUser(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepo.findByUser(user);
    }
}

