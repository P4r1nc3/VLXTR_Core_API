package com.allegroservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "product_id", nullable = false, unique = true)
    private String productId; // Primary key, unique string identifier for the product

    @Column(name = "product_name", nullable = false)
    private String productName; // Name of the product

    @Column(name = "product_colour", nullable = false)
    private String productColour; // Colour of the product

    @Column(name = "product_price", nullable = false)
    private Double productPrice; // Price of the product

    @Column(name = "allegro_offer_id", nullable = false, unique = true)
    private String allegroOfferId; // Allegro offer ID

    @Column(name = "allegro_offer_link", nullable = false, unique = true)
    private String allegroOfferLink; // Link to the Allegro offer

    @Column(name = "google_drive_id", nullable = false)
    private String googleDriveId; // Google Drive file ID

    @Column(name = "google_drive_link", nullable = false)
    private String googleDriveLink; // Link to the Google Drive file
}
