package com.vlxtrcore.service.factory;

import com.p4r1nc3.vlxtr.allegro.ApiClient;
import com.p4r1nc3.vlxtr.allegro.api.OffersApi;
import com.vlxtrcore.utils.TokenUtils;
import org.springframework.stereotype.Component;

@Component
public class AllegroApiClientFactory {
    private final TokenUtils tokenUtils;

    public AllegroApiClientFactory(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    public OffersApi createOffersApi() {
        String bearerToken = tokenUtils.getBearerToken();
        ApiClient apiClient = new ApiClient();
        apiClient.setBearerToken(bearerToken);
        return new OffersApi(apiClient);
    }
}
