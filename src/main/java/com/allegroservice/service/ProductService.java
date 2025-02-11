package com.allegroservice.service;

import com.google.api.services.drive.model.File;
import com.allegroservice.dto.allegro.OffersResponse;
import com.allegroservice.model.Product;
import com.allegroservice.repository.ProductRepository;
import com.allegroservice.validators.ProductValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final AllegroService allegroService;
    private final GoogleDriveService googleDriveService;
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    public ProductService(AllegroService allegroService, GoogleDriveService googleDriveService,
                          ProductRepository productRepository, ProductValidator productValidator) {
        this.allegroService = allegroService;
        this.googleDriveService = googleDriveService;
        this.productRepository = productRepository;
        this.productValidator = productValidator;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProductByAllegroOfferId(String allegroOfferId) {
        Optional<Product> product = productRepository.findByAllegroOfferId(allegroOfferId);
        return product.orElseThrow(() -> new EntityNotFoundException("Product with allegroOfferId: " + allegroOfferId + " not found"));
    }

    public List<Product> populateProducts(String bearerToken) {
        // Fetch offers from Allegro
        OffersResponse offersResponse = allegroService.fetchOffers(bearerToken);

        // Fetch files from Google Drive
        List<File> googleFiles = googleDriveService.fetchGoogleDriveFiles();

        // Process offers and map them to products
        return offersResponse.getOffers().stream()
                .map(offer -> processOffer(bearerToken, offer, googleFiles))
                .map(productRepository::save)
                .toList();
    }

    private Product processOffer(String bearerToken, OffersResponse.Offer offer, List<File> googleFiles) {
        // Fetch full offer details
        OffersResponse.Offer fullOffer = allegroService.fetchOffer(bearerToken, offer.getId());

        // Extract product model and color
        String productModel = extractProductModel(fullOffer);
        String productColour = extractProductColour(productModel);

        // Get Google Drive file info
        String googleDriveId = findGoogleDriveId(googleFiles, productModel);
        String googleDriveLink = "https://drive.google.com/file/d/" + googleDriveId + "/view";

        // Map fields to Product entity
        return buildProductEntity(fullOffer, productModel, productColour, googleDriveId, googleDriveLink);
    }

    private String extractProductModel(OffersResponse.Offer fullOffer) {
        if (fullOffer.getProductSet() != null && !fullOffer.getProductSet().isEmpty()) {
            OffersResponse.Offer.ProductSet productSet = fullOffer.getProductSet().get(0);
            if (productSet.getProduct() != null && productSet.getProduct().getParameters() != null) {
                return productSet.getProduct().getParameters().stream()
                        .filter(param -> "Model".equalsIgnoreCase(param.getName()))
                        .findFirst()
                        .map(param -> param.getValues().get(0))
                        .orElse("UNKNOWN_MODEL");
            }
        }
        return "UNKNOWN_MODEL";
    }

    private String extractProductColour(String productModel) {
        if (productModel != null && productModel.contains("-")) {
            char lastChar = productModel.charAt(productModel.length() - 1);
            return switch (lastChar) {
                case 'W' -> "White";
                case 'B' -> "Black";
                case 'G' -> "Grey";
                default -> "UNKNOWN_COLOUR";
            };
        }
        return "UNKNOWN_COLOUR";
    }

    private String findGoogleDriveId(List<File> googleFiles, String productModel) {
        return googleFiles.stream()
                .filter(file -> file.getName().startsWith(productModel + ".") || file.getName().equals(productModel))
                .findFirst()
                .map(File::getId)
                .orElse("UNKNOWN_FILE_ID");
    }

    private Product buildProductEntity(OffersResponse.Offer fullOffer, String productModel, String productColour,
                                       String googleDriveId, String googleDriveLink) {
        Product product = new Product();
        product.setProductId(productModel);
        product.setProductName(fullOffer.getName());
        product.setProductColour(productColour);
        product.setProductPrice(Double.valueOf(fullOffer.getSellingMode().getPrice().getAmount()));
        product.setAllegroOfferId(fullOffer.getId());
        product.setAllegroOfferLink("https://allegro.pl/oferta/" + fullOffer.getId());
        product.setGoogleDriveId(googleDriveId);
        product.setGoogleDriveLink(googleDriveLink);
        product.setIsValid(productValidator.isValid(product));
        return product;
    }
}
