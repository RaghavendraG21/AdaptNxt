package com.ecommerce.api.service;

import com.ecommerce.api.dto.ProductDTO;
import com.ecommerce.api.model.Product;
import com.ecommerce.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired private ProductRepository productRepository;

    public Product addProduct(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductDTO dto) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getAllProducts(Optional<String> name, Optional<String> category, Optional<Integer> page, Optional<Integer> size) {
        if (page.isPresent() && size.isPresent()) {
            Pageable pageable = PageRequest.of(page.get(), size.get());
            Page<Product> paged = productRepository.findAll(pageable);
            return paged.getContent();
        }

        if (name.isPresent()) {
            return productRepository.findByNameContainingIgnoreCase(name.get());
        }

        if (category.isPresent()) {
            return productRepository.findByCategoryIgnoreCase(category.get());
        }

        return productRepository.findAll();
    }
}
