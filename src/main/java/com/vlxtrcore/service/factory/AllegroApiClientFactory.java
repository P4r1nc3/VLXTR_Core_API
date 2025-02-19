package com.vlxtrcore.service.factory;

import com.p4r1nc3.vlxtr.allegro.ApiClient;
import com.p4r1nc3.vlxtr.allegro.api.OffersApi;
import com.vlxtrcore.service.AuthService;
import org.springframework.stereotype.Component;

@Component
public class AllegroApiClientFactory {
    private final AuthService authService;

    public AllegroApiClientFactory(AuthService authService) {
        this.authService = authService;
    }

    public OffersApi createOffersApi() {
        String bearerToken = authService.getBearerToken();
        ApiClient apiClient = new ApiClient();
        apiClient.setBearerToken(bearerToken);
        return new OffersApi(apiClient);
    }
}
