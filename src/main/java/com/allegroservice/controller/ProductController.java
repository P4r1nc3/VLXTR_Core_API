package com.allegroservice.controller;

import com.allegroservice.model.Product;
import com.allegroservice.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = productService.getProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductByAllegroOfferId(@PathVariable String productId) {
        System.out.println("test");
        Product product = productService.getProductByAllegroOfferId(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/populate")
    public ResponseEntity<List<Product>> populateProducts(@RequestHeader("Authorization") String bearerToken) {
        List<Product> products = productService.populateProducts(bearerToken);
        return ResponseEntity.ok(products);
    }
}
