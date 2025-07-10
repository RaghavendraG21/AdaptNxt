package com.ecommerce.api.service;

import com.ecommerce.api.dto.CartItemDTO;
import com.ecommerce.api.model.CartItem;
import com.ecommerce.api.model.Product;
import com.ecommerce.api.model.User;
import com.ecommerce.api.repository.CartItemRepository;
import com.ecommerce.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired private CartItemRepository cartRepo;
    @Autowired private ProductRepository productRepo;

    public void addItemToCart(User user, CartItemDTO dto) {
        Product product = productRepo.findById(dto.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        CartItem item = cartRepo.findByUserAndProduct(user, product).orElse(new CartItem());

        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(item.getQuantity() + dto.getQuantity());

        cartRepo.save(item);
    }

    public List<CartItem> getCartItems(User user) {
        return cartRepo.findByUser(user);
    }

    public void updateItem(User user, Long productId, int quantity) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        CartItem item = cartRepo.findByUserAndProduct(user, product).orElseThrow(() -> new RuntimeException("Cart item not found"));

        item.setQuantity(quantity);
        cartRepo.save(item);
    }

    public void removeItem(User user, Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        cartRepo.deleteByUserAndProduct(user, product);
    }

    public void clearCart(User user) {
        cartRepo.deleteByUser(user);
    }
}
