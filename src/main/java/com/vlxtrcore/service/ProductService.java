package com.vlxtrcore.service;

import com.google.api.services.drive.model.File;
import com.p4r1nc3.vlxtr.allegro.api.OffersApi;
import com.p4r1nc3.vlxtr.allegro.model.OfferDetailsResponse;
import com.p4r1nc3.vlxtr.allegro.model.OfferListItem;
import com.p4r1nc3.vlxtr.allegro.model.OfferProductSetItem;
import com.p4r1nc3.vlxtr.allegro.model.OffersListResponse;
import com.vlxtrcore.exception.ApiException;
import com.vlxtrcore.model.Product;
import com.vlxtrcore.repository.ProductRepository;
import com.vlxtrcore.service.factory.AllegroApiClientFactory;
import com.vlxtrcore.validators.ProductValidator;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final GoogleDriveService googleDriveService;
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;
    private final AllegroApiClientFactory allegroApiClientFactory;

    public ProductService(GoogleDriveService googleDriveService,
                          ProductRepository productRepository,
                          ProductValidator productValidator,
                          AllegroApiClientFactory allegroApiClientFactory) {
        this.googleDriveService = googleDriveService;
        this.productRepository = productRepository;
        this.productValidator = productValidator;
        this.allegroApiClientFactory = allegroApiClientFactory;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProductByAllegroOfferId(String allegroOfferId) {
        Optional<Product> product = productRepository.findByAllegroOfferId(allegroOfferId);
        return product.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                "Product not found",
                "Product with allegroOfferId: " + allegroOfferId + " not found",
                "Verify the Allegro Offer ID and try again."));
    }

    public List<Product> populateProducts() {
        try {
            OffersApi offersApi = allegroApiClientFactory.createOffersApi();
            OffersListResponse offersListResponse = offersApi.getOffers();
            List<File> googleFiles = googleDriveService.fetchGoogleDriveFiles();

            return offersListResponse.getOffers()
                    .stream()
                    .map(offer -> processOffer(offer, googleFiles))
                    .map(productRepository::save)
                    .toList();
        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to populate products",
                    e.getMessage(),
                    "Check API connectivity and try again.");
        }
    }

    private Product processOffer(OfferListItem offer, List<File> googleFiles) {
        try {
            OffersApi offersApi = allegroApiClientFactory.createOffersApi();
            OfferDetailsResponse fullOffer = offersApi.getOfferById(offer.getId());

            String productModel = extractProductModel(fullOffer);
            String productColour = extractProductColour(productModel);
            String googleDriveId = findGoogleDriveId(googleFiles, productModel);
            String googleDriveLink = "https://drive.google.com/file/d/" + googleDriveId + "/view";

            return buildProductEntity(fullOffer, productModel, productColour, googleDriveId, googleDriveLink);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to process offer",
                    e.getMessage(),
                    "Check offer details and try again.");
        }
    }

    private String extractProductModel(OfferDetailsResponse fullOffer) {
        if (fullOffer.getProductSet() != null && !fullOffer.getProductSet().isEmpty()) {
            OfferProductSetItem productSet = fullOffer.getProductSet().get(0);
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
            String[] parts = productModel.split("-");
            return parts[parts.length - 1];
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

    private Product buildProductEntity(OfferDetailsResponse fullOffer, String productModel, String productColour,
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
