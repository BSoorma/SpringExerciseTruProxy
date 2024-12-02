package com.example.service;

import static com.example.constants.TestConstants.BBC_COMPANY_NAME;
import static com.example.constants.TestConstants.COMPANY_NUMBER;
import static com.example.constants.TestConstants.SOME_API_KEY;
import static com.example.constants.TestConstants.TEST_COMPANY_ADDRESS;
import static com.example.constants.TestConstants.TEST_OFFICER_NOT_RESIGNED;
import static com.example.constants.TestConstants.TEST_OFFICER_RESIGNED;
import static com.example.constants.TestConstants.TEST_TRUPROXY_ACTIVE_COMPANY_RESPONSE_LIST;
import static com.example.constants.TestConstants.TEST_TRUPROXY_BBC_COMPANY_RESPONSE;
import static com.example.constants.TestConstants.TEST_TRUPROXY_MIXED_COMPANY_RESPONSE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.example.exceptions.CustomClientException;
import com.example.exceptions.CustomServerException;
import com.example.model.dto.Company;
import com.example.model.request.SearchRequestBody;
import com.example.model.response.CompanyResponse;
import com.example.model.response.TruProxyCompanyResponse;
import com.example.model.response.TruProxyOfficerResponse;
import com.example.rest.TruProxyRestfulClient;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    TruProxyRestfulClient mockTruProxyRestfulClient;

    @InjectMocks
    SearchService searchService;

    @Test
    void findCompanyAndOfficerReturnsSuccess() {
        when(mockTruProxyRestfulClient.genericExchange(SOME_API_KEY, "/Search?Query=" + COMPANY_NUMBER, TruProxyCompanyResponse.class))
            .thenReturn(new ResponseEntity<>(new TruProxyCompanyResponse(1, TEST_TRUPROXY_ACTIVE_COMPANY_RESPONSE_LIST), HttpStatus.OK));

        when(mockTruProxyRestfulClient.genericExchange(SOME_API_KEY, "/Officers?CompanyNumber=" + COMPANY_NUMBER, TruProxyOfficerResponse.class))
            .thenReturn(new ResponseEntity<>(new TruProxyOfficerResponse(List.of(TEST_OFFICER_NOT_RESIGNED)), HttpStatus.OK));

        SearchRequestBody searchRequestBody = new SearchRequestBody(BBC_COMPANY_NAME, COMPANY_NUMBER);

        CompanyResponse response = searchService.findCompanyAndOfficer(SOME_API_KEY, true, searchRequestBody);

        assertEquals(1, response.totalResults());
        assertNotNull(response.items());
        assertEquals(1, response.items().size());
        response.items().forEach(company -> {
            assertEquals(COMPANY_NUMBER, company.companyNumber());
            assertEquals("ltd", company.companyType());
            assertEquals("12345 LIMITED", company.title());
            assertEquals("active", company.companyStatus());
            assertEquals("1999-07-01", company.dateOfCreation());
            assertEquals(TEST_COMPANY_ADDRESS, company.address());
            assertTrue(company.officers().contains(TEST_OFFICER_NOT_RESIGNED));
        });
    }

    @Test
    void whenNoSearchTermIsProvidedReturnEmptyResponse() {
        SearchRequestBody searchRequestBody = new SearchRequestBody(null, null);

        CompanyResponse response = searchService.findCompanyAndOfficer(SOME_API_KEY, true, searchRequestBody);

        assertEquals(0, response.totalResults());
        assertEquals(0, response.items().size());
    }

    @Test
    void whenNoCompanyNumberProvidedSearchByCompanyName() {
        when(mockTruProxyRestfulClient.genericExchange(SOME_API_KEY, "/Search?Query=" + BBC_COMPANY_NAME, TruProxyCompanyResponse.class))
            .thenReturn(new ResponseEntity<>(new TruProxyCompanyResponse(1, TEST_TRUPROXY_BBC_COMPANY_RESPONSE), HttpStatus.OK));

        when(mockTruProxyRestfulClient.genericExchange(SOME_API_KEY, "/Officers?CompanyNumber=" + BBC_COMPANY_NAME, TruProxyOfficerResponse.class))
            .thenReturn(new ResponseEntity<>(new TruProxyOfficerResponse(Collections.emptyList()), HttpStatus.OK));

        SearchRequestBody searchRequestBody = new SearchRequestBody(BBC_COMPANY_NAME, null);

        CompanyResponse response = searchService.findCompanyAndOfficer(SOME_API_KEY, true, searchRequestBody);

        assertEquals(1, response.totalResults());
        assertEquals(1, response.items().size());
    }

    @Test
    void whenTruProxyCompanyResponseIsEmptyReturnEmptyResponse() {
        when(mockTruProxyRestfulClient.genericExchange(SOME_API_KEY, "/Search?Query=" + COMPANY_NUMBER, TruProxyCompanyResponse.class))
            .thenReturn(new ResponseEntity<>(new TruProxyCompanyResponse(0, List.of()), HttpStatus.OK));

        when(mockTruProxyRestfulClient.genericExchange(SOME_API_KEY, "/Officers?CompanyNumber=" + COMPANY_NUMBER, TruProxyOfficerResponse.class))
            .thenReturn(new ResponseEntity<>(new TruProxyOfficerResponse(List.of(TEST_OFFICER_NOT_RESIGNED)), HttpStatus.OK));

        SearchRequestBody searchRequestBody = new SearchRequestBody(BBC_COMPANY_NAME, COMPANY_NUMBER);

        CompanyResponse response = searchService.findCompanyAndOfficer(SOME_API_KEY, true, searchRequestBody);

        assertEquals(0, response.totalResults());
        assertEquals(0, response.items().size());
    }

    @Test
    void whenOfficerResponseIsEmptyThenReturnCompanyResponseWithEmptyOfficerList() {
        when(mockTruProxyRestfulClient.genericExchange(SOME_API_KEY, "/Search?Query=" + COMPANY_NUMBER, TruProxyCompanyResponse.class))
            .thenReturn(new ResponseEntity<>(new TruProxyCompanyResponse(1, TEST_TRUPROXY_ACTIVE_COMPANY_RESPONSE_LIST), HttpStatus.OK));

        when(mockTruProxyRestfulClient.genericExchange(SOME_API_KEY, "/Officers?CompanyNumber=" + COMPANY_NUMBER, TruProxyOfficerResponse.class))
            .thenReturn(new ResponseEntity<>(new TruProxyOfficerResponse(Collections.emptyList()), HttpStatus.OK));

        SearchRequestBody searchRequestBody = new SearchRequestBody(BBC_COMPANY_NAME, COMPANY_NUMBER);

        CompanyResponse response = searchService.findCompanyAndOfficer(SOME_API_KEY, true, searchRequestBody);

        assertEquals(1, response.totalResults());
        assertNotNull(response.items());
        assertEquals(1, response.items().size());
        response.items().forEach(company -> {
            assertEquals(COMPANY_NUMBER, company.companyNumber());
            assertEquals("ltd", company.companyType());
            assertEquals("12345 LIMITED", company.title());
            assertEquals("active", company.companyStatus());
            assertEquals("1999-07-01", company.dateOfCreation());
            assertEquals(TEST_COMPANY_ADDRESS, company.address());
            assertTrue(company.officers().isEmpty());
        });
    }

    @Test
    void whenTruProxyOfficerResponseContainsResignedOfficersDoNotIncludeThemInCompanyResponse() {
        when(mockTruProxyRestfulClient.genericExchange(SOME_API_KEY, "/Search?Query=" + COMPANY_NUMBER, TruProxyCompanyResponse.class))
            .thenReturn(new ResponseEntity<>(new TruProxyCompanyResponse(1, TEST_TRUPROXY_ACTIVE_COMPANY_RESPONSE_LIST), HttpStatus.OK));

        when(mockTruProxyRestfulClient.genericExchange(SOME_API_KEY, "/Officers?CompanyNumber=" + COMPANY_NUMBER, TruProxyOfficerResponse.class))
            .thenReturn(new ResponseEntity<>(new TruProxyOfficerResponse(List.of(TEST_OFFICER_RESIGNED)), HttpStatus.OK));

        SearchRequestBody searchRequestBody = new SearchRequestBody(BBC_COMPANY_NAME, COMPANY_NUMBER);

        CompanyResponse response = searchService.findCompanyAndOfficer(SOME_API_KEY, true, searchRequestBody);

        assertEquals(1, response.totalResults());
        assertNotNull(response.items());
        assertEquals(1, response.items().size());
        response.items().forEach(company -> {
            assertEquals(COMPANY_NUMBER, company.companyNumber());
            assertEquals("ltd", company.companyType());
            assertEquals("12345 LIMITED", company.title());
            assertEquals("active", company.companyStatus());
            assertEquals("1999-07-01", company.dateOfCreation());
            assertEquals(TEST_COMPANY_ADDRESS, company.address());
            assertTrue(company.officers().isEmpty());
        });
    }

    @Test
    void returnNonActiveCompaniesWhenIsActiveFlagIsFalse() {
        when(mockTruProxyRestfulClient.genericExchange(SOME_API_KEY, "/Search?Query=" + COMPANY_NUMBER, TruProxyCompanyResponse.class))
            .thenReturn(new ResponseEntity<>(new TruProxyCompanyResponse(3, TEST_TRUPROXY_MIXED_COMPANY_RESPONSE), HttpStatus.OK));

        when(mockTruProxyRestfulClient.genericExchange(SOME_API_KEY, "/Officers?CompanyNumber=" + COMPANY_NUMBER, TruProxyOfficerResponse.class))
            .thenReturn(new ResponseEntity<>(new TruProxyOfficerResponse(Collections.emptyList()), HttpStatus.OK));

        CompanyResponse response = searchService.findCompanyAndOfficer(SOME_API_KEY, false, new SearchRequestBody(BBC_COMPANY_NAME, COMPANY_NUMBER));

        assertEquals(2, response.totalResults());
        assertNotNull(response.items());

        Company companyOne = response.items().get(0);
        assertEquals(COMPANY_NUMBER, companyOne.companyNumber());
        assertEquals("ltd", companyOne.companyType());
        assertEquals("333 LIMITED", companyOne.title());
        assertEquals("dissolved", companyOne.companyStatus());
        assertEquals("1995-05-05", companyOne.dateOfCreation());
        assertEquals(TEST_COMPANY_ADDRESS, companyOne.address());

        Company companyTwo = response.items().get(1);
        assertEquals(COMPANY_NUMBER, companyTwo.companyNumber());
        assertEquals("ltd", companyTwo.companyType());
        assertEquals("555 LIMITED", companyTwo.title());
        assertEquals("inactive", companyTwo.companyStatus());
        assertEquals("1998-08-08", companyTwo.dateOfCreation());
        assertEquals(TEST_COMPANY_ADDRESS, companyTwo.address());
    }

    @Test
    void whenTruProxyRestfulClientThrowsExceptionReturnEmptyResponse() {
        when(mockTruProxyRestfulClient.genericExchange(SOME_API_KEY, "/Search?Query=" + COMPANY_NUMBER, TruProxyCompanyResponse.class))
            .thenThrow(new RuntimeException("Unexpected error occurred"));

        SearchRequestBody searchRequestBody = new SearchRequestBody(BBC_COMPANY_NAME, COMPANY_NUMBER);

        CompanyResponse response = searchService.findCompanyAndOfficer(SOME_API_KEY, true, searchRequestBody);

        assertEquals(0, response.totalResults());
        assertEquals(0, response.items().size());
    }

    @Test
    void whenTruProxyRestfulClientThrowsCustomClientExceptionReturnEmptyResponse() {
        when(mockTruProxyRestfulClient.genericExchange(SOME_API_KEY, "/Search?Query=" + COMPANY_NUMBER, TruProxyCompanyResponse.class))
            .thenThrow(new CustomClientException("Client Forbidden", new HttpClientErrorException(HttpStatus.FORBIDDEN)));

        SearchRequestBody searchRequestBody = new SearchRequestBody(BBC_COMPANY_NAME, COMPANY_NUMBER);

        CompanyResponse response = searchService.findCompanyAndOfficer(SOME_API_KEY, true, searchRequestBody);

        assertEquals(0, response.totalResults());
        assertEquals(0, response.items().size());
    }

    @Test
    void whenTruProxyRestfulClientThrowsCustomServerExceptionReturnEmptyResponse() {
        when(mockTruProxyRestfulClient.genericExchange(SOME_API_KEY, "/Search?Query=" + COMPANY_NUMBER, TruProxyCompanyResponse.class))
            .thenThrow(new CustomServerException("Server error", new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)));

        SearchRequestBody searchRequestBody = new SearchRequestBody(BBC_COMPANY_NAME, COMPANY_NUMBER);

        CompanyResponse response = searchService.findCompanyAndOfficer(SOME_API_KEY, true, searchRequestBody);

        assertEquals(0, response.totalResults());
        assertEquals(0, response.items().size());
    }

    @Test
    void whenTruProxyRestfulClientResponseIsNullReturnEmptyResponse() {
        when(mockTruProxyRestfulClient.genericExchange(SOME_API_KEY, "/Search?Query=" + COMPANY_NUMBER, TruProxyCompanyResponse.class))
            .thenReturn(null);

        SearchRequestBody searchRequestBody = new SearchRequestBody(BBC_COMPANY_NAME, COMPANY_NUMBER);

        CompanyResponse response = searchService.findCompanyAndOfficer(SOME_API_KEY, true, searchRequestBody);

        assertEquals(0, response.totalResults());
        assertEquals(0, response.items().size());
    }

    @Test
    void whenTruProxyRestfulClientResponseIsEmptyReturnEmptyResponse() {
        when(mockTruProxyRestfulClient.genericExchange(SOME_API_KEY, "/Search?Query=" + COMPANY_NUMBER, TruProxyCompanyResponse.class))
            .thenReturn(new ResponseEntity<>(new TruProxyCompanyResponse(0, Collections.emptyList()), HttpStatus.OK));

        SearchRequestBody searchRequestBody = new SearchRequestBody(BBC_COMPANY_NAME, COMPANY_NUMBER);

        CompanyResponse response = searchService.findCompanyAndOfficer(SOME_API_KEY, true, searchRequestBody);

        assertEquals(0, response.totalResults());
        assertEquals(0, response.items().size());
    }

}