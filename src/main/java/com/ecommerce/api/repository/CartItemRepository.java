package com.ecommerce.api.repository;

import com.ecommerce.api.model.CartItem;
import com.ecommerce.api.model.User;
import com.ecommerce.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndProduct(User user, Product product);
    void deleteByUser(User user);
    void deleteByUserAndProduct(User user, Product product);
}
