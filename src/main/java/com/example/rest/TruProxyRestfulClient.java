package com.example.rest;

import com.example.exceptions.CustomClientException;
import com.example.exceptions.CustomServerException;
import com.example.exceptions.RestClientWrapperException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class TruProxyRestfulClient {

    private static final String X_API_KEY_HEADER = "x-api-key";
    private static final String BASE_PATH = "/rest/Companies/v1";
    private final String baseUrl;
    private final RestClient restClient;

    public TruProxyRestfulClient(@Value("${truproxy.api.url}") String baseUrl,
                                 RestClient restClient) {
        this.baseUrl = baseUrl;
        this.restClient = restClient;
    }

    private String getEndpointUrl(String path) {
        return String.format("%s%s%s", baseUrl, BASE_PATH, path);
    }

    public <T> ResponseEntity<T> genericExchange(String apiKey, String path, Class<T> responseType) {
        try {
            return restClient.get()
                .uri(getEndpointUrl(path))
                .header(X_API_KEY_HEADER, apiKey)
                .retrieve()
                .toEntity(responseType);
        } catch (HttpClientErrorException e) {
            // Handle 4xx errors
            throw new CustomClientException("Client Error: " + e.getStatusText(), e);
        } catch (HttpServerErrorException e) {
            // Handle 5xx errors
            throw new CustomServerException("Server Error: " + e.getStatusText(), e);
        } catch (RestClientException e) {
            // Handle other generic errors
            throw new RestClientWrapperException("General error during REST call", e);
        } catch (Exception e) {
            // Handle unexpected errors
            throw new RestClientWrapperException("Unexpected error occurred", e);
        }
    }

}
