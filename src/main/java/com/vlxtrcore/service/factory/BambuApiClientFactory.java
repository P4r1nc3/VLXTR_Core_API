package com.vlxtrcore.service.factory;

import com.p4r1nc3.vlxtr.bambu.ApiClient;

import com.p4r1nc3.vlxtr.bambu.api.PrintersApi;
import org.springframework.stereotype.Component;

@Component
public class BambuApiClientFactory {
    private final AuthService authService;

    public BambuApiClientFactory(AuthService authService) {
        this.authService = authService;
    }

    public PrintersApi createPrintersApi() {
        String bearerToken = authService.getBearerToken();
        ApiClient apiClient = new ApiClient();
        apiClient.setBearerToken(bearerToken);
        return new PrintersApi(apiClient);
    }
}
