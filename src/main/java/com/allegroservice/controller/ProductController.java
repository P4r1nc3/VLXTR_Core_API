package com.allegroservice.controller;

import com.allegroservice.model.Product;
import com.allegroservice.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/populate")
    public ResponseEntity<List<Product>> populateProducts(@RequestHeader("Authorization") String bearerToken) {
        List<Product> products = productService.populateProductsFromAllegro(bearerToken);
        return ResponseEntity.ok(products);
    }
}
