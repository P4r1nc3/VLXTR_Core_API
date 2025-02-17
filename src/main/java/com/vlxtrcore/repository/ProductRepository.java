package com.vlxtrcore.repository;

import com.vlxtrcore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByAllegroOfferId(String allegroOfferId);
}
