package com.example.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.exceptions.CustomClientException;
import com.example.exceptions.CustomServerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

@ExtendWith(MockitoExtension.class)
class TruProxyRestfulClientTest {

    private static final String X_API_KEY_HEADER = "x-api-key";
    private static final String BASE_URL = "https://api.example.com";
    private static final String DUMMY_API_KEY = "dummy-api-key";
    private static final String PATH = "/test-path";

    @Mock
    private RestClient mockRestClient;

    @Mock
    private RestClient.RequestHeadersUriSpec mockRequestHeadersUriSpec;

    @Mock
    private RestClient.ResponseSpec mockResponseSpec;

    @InjectMocks
    private TruProxyRestfulClient truProxyRestfulClient;

    @BeforeEach
    void setUp() {
        when(mockRestClient.get()).thenReturn(mockRequestHeadersUriSpec);
        when(mockRequestHeadersUriSpec.uri(anyString())).thenReturn(mockRequestHeadersUriSpec);
        when(mockRequestHeadersUriSpec.header(X_API_KEY_HEADER, DUMMY_API_KEY)).thenReturn(mockRequestHeadersUriSpec);
        when(mockRequestHeadersUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.toEntity(String.class)).thenReturn(ResponseEntity.ok("Success"));

        truProxyRestfulClient = new TruProxyRestfulClient(BASE_URL, mockRestClient);
    }

    /**
     * Test commented out with refactor of RestClient from using retrieve().toEntity()
     * to .exchange() and being able to mock call
     */

    //@Test
    void testGenericExchangeSuccess() {
        ResponseEntity<String> mockResponse = ResponseEntity.ok("Success");
        when(mockRestClient.get()
            .uri(BASE_URL + "/rest/Companies/v1" + PATH)
            .header(X_API_KEY_HEADER, DUMMY_API_KEY)
            .retrieve()
            .toEntity(String.class))
            .thenReturn(mockResponse);

        String response = truProxyRestfulClient.genericExchange(DUMMY_API_KEY, PATH, String.class);

        assertNotNull(response);
        assertEquals("Success", response);
    }

    //@Test
    void testGenericExchangeClientError() {
        HttpClientErrorException exception = mock(HttpClientErrorException.class);

        when(mockRestClient.get()
            .uri(BASE_URL + "/rest/Companies/v1" + PATH)
            .header(X_API_KEY_HEADER, DUMMY_API_KEY)
            .retrieve()
            .toEntity(String.class))
            .thenThrow(exception);

        CustomClientException customClientException = assertThrows(CustomClientException.class,
            () -> truProxyRestfulClient.genericExchange(DUMMY_API_KEY, PATH, String.class));
        assertNotNull(customClientException);
        assertEquals("Client Error: null", customClientException.getMessage());
    }

    //@Test
    void testGenericExchangeServerError() {
        HttpServerErrorException exception = mock(HttpServerErrorException.class);

        when(mockRestClient.get()
            .uri(BASE_URL + "/rest/Companies/v1" + PATH)
            .header(X_API_KEY_HEADER, DUMMY_API_KEY)
            .retrieve()
            .toEntity(String.class))
            .thenThrow(exception);

        CustomServerException customServerException = assertThrows(CustomServerException.class,
            () -> truProxyRestfulClient.genericExchange(DUMMY_API_KEY, PATH, String.class));
        assertNotNull(customServerException);
        assertEquals("Server Error: null", customServerException.getMessage());
    }

    //@Test
    void testGenericExchangeUnexpectedException() {
        RuntimeException exception = new RuntimeException("Unexpected error");

        when(mockRestClient.get()
            .uri(BASE_URL + "/rest/Companies/v1" + PATH)
            .header(X_API_KEY_HEADER, DUMMY_API_KEY)
            .retrieve()
            .toEntity(String.class))
            .thenThrow(exception);

        Exception exceptionThrown = assertThrows(Exception.class,
            () -> truProxyRestfulClient.genericExchange(DUMMY_API_KEY, PATH, String.class));
        assertNotNull(exceptionThrown);
        assertEquals("Unexpected error occurred", exceptionThrown.getMessage());
    }

}