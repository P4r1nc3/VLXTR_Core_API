package com.allegroservice.repository;

import com.allegroservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByAllegroOfferId(String allegroOfferId);
}
