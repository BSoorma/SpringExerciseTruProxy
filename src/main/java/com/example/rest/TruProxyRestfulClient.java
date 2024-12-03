package com.example.rest;

import com.example.exceptions.CustomClientException;
import com.example.exceptions.CustomServerException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

@Component
public class TruProxyRestfulClient {

    private static final String X_API_KEY_HEADER = "x-api-key";
    private static final String BASE_PATH = "/rest/Companies/v1";
    private final String baseUrl;
    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TruProxyRestfulClient(@Value("${truproxy.api.url}") String baseUrl,
                                 RestClient restClient) {
        this.baseUrl = baseUrl;
        this.restClient = restClient;
    }

    private String getEndpointUrl(String path) {
        return String.format("%s%s%s", baseUrl, BASE_PATH, path);
    }

    public <T> T genericExchange(String apiKey, String path, Class<T> responseType) {
        return restClient.get()
            .uri(getEndpointUrl(path))
            .header(X_API_KEY_HEADER, apiKey)
            .exchange((request, response) -> handleResponse(responseType, response));
    }

    private <T> T handleResponse(Class<T> responseType, RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse response) throws IOException {

        try {
            if (response.getStatusCode().isError()) {
                handleError(response);
            }
        } catch (IOException ioException) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(response.getBody(), responseType);
    }

    private void handleError(RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is4xxClientError()) {
            throw new CustomClientException(response.getStatusCode().value());
        } else if (response.getStatusCode().is5xxServerError()) {
            throw new CustomServerException(response.getStatusCode().value());
        }
    }

}
