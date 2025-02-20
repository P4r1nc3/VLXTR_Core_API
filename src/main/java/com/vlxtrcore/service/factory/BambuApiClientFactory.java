package com.vlxtrcore.service.factory;

import com.p4r1nc3.vlxtr.bambu.ApiClient;

import com.p4r1nc3.vlxtr.bambu.api.PrintersApi;
import com.vlxtrcore.utils.TokenUtils;
import org.springframework.stereotype.Component;

@Component
public class BambuApiClientFactory {
    private final TokenUtils tokenUtils;

    public BambuApiClientFactory(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    public PrintersApi createPrintersApi() {
        String bearerToken = tokenUtils.getBearerToken();
        ApiClient apiClient = new ApiClient();
        apiClient.setBearerToken(bearerToken);
        return new PrintersApi(apiClient);
    }
}
