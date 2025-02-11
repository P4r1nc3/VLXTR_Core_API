package com.vlxtrcore.validators;

import com.vlxtrcore.model.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductValidator {

    public boolean isValid(Product product) {
        return validateProductId(product) &&
                validateProductName(product) &&
                validateProductColour(product) &&
                validateProductPrice(product) &&
                validateAllegroOfferId(product) &&
                validateAllegroOfferLink(product) &&
                validateGoogleDriveId(product) &&
                validateGoogleDriveLink(product);
    }

    private boolean validateProductId(Product product) {
        return product.getProductId() != null && !product.getProductId().isEmpty() && !product.getProductId().equals("UNKNOWN_MODEL");
    }

    private boolean validateProductName(Product product) {
        return product.getProductName() != null && !product.getProductName().isEmpty();
    }

    private boolean validateProductColour(Product product) {
        return product.getProductColour() != null && !product.getProductColour().isEmpty() && !product.getProductId().equals("UNKNOWN_COLOUR");
    }

    private boolean validateProductPrice(Product product) {
        return product.getProductPrice() != null && product.getProductPrice() > 0;
    }

    private boolean validateAllegroOfferId(Product product) {
        return product.getAllegroOfferId() != null && !product.getAllegroOfferId().isEmpty();
    }

    private boolean validateAllegroOfferLink(Product product) {
        return product.getAllegroOfferLink() != null && product.getAllegroOfferLink().startsWith("https://allegro.pl/oferta/");
    }

    private boolean validateGoogleDriveId(Product product) {
        return product.getGoogleDriveId() != null && !product.getGoogleDriveId().isEmpty() && !product.getGoogleDriveId().equals("UNKNOWN_FILE_ID");
    }

    private boolean validateGoogleDriveLink(Product product) {
        return product.getGoogleDriveLink() != null && product.getGoogleDriveLink().startsWith("https://drive.google.com/file/d/");
    }
}
