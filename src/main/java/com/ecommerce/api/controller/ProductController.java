package com.ecommerce.api.controller;

import com.ecommerce.api.dto.ProductDTO;
import com.ecommerce.api.model.Product;
import com.ecommerce.api.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired private ProductService productService;

    @GetMapping
    public List<Product> getProducts(
        @RequestParam Optional<String> name,
        @RequestParam Optional<String> category,
        @RequestParam Optional<Integer> page,
        @RequestParam Optional<Integer> size
    ) {
        return productService.getAllProducts(name, category, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Product addProduct(@RequestBody ProductDTO dto) {
        return productService.addProduct(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Product updateProduct(@PathVariable Long id, @RequestBody ProductDTO dto) {
        return productService.updateProduct(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted");
    }
}
