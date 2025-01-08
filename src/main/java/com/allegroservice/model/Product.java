package com.allegroservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String link;

    @Column(nullable = false)
    private String title;

    @Column(name = "file_path", nullable = true)
    private String filePath;
}
