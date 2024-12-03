package com.example.wiremock.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.model.dto.Address;
import com.example.model.dto.Officer;
import com.example.model.dto.TruProxyCompany;
import com.example.model.request.SearchRequestBody;
import com.example.model.response.CompanyResponse;
import com.example.model.response.TruProxyCompanyResponse;
import com.example.model.response.TruProxyOfficerResponse;
import com.example.service.SearchService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"truproxy.api.url=http://localhost:8181"})
@WireMockTest(httpPort = 8181)
class SearchServiceIntegrationTest {

    @Autowired
    private SearchService searchService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getFindCompanyAndOfficerSuccessResponse(WireMockRuntimeInfo wireMockRuntimeInfo) {

        Address address = new Address(
            "Retford",
            "DN22 0AD",
            "Boswell Cottage Main Street",
            "North Leverton",
            "England"
        );
        TruProxyCompanyResponse truProxyCompanyResponse =
            new TruProxyCompanyResponse(1,
                List.of(
                    new TruProxyCompany(
                        "12345",
                        "ABC Company",
                        "ltd",
                        "active",
                        "2008-02-11",
                        address
                    )
                )
            );

        List<Officer> listOfOfficers =
            Arrays.asList(
                new Officer(
                    "John",
                    "Director",
                    "2008-02-11",
                    address,
                    null),
                new Officer(
                    "Neo",
                    "Director",
                    "2008-02-11",
                    address,
                    null)
            );

        TruProxyOfficerResponse truProxyOfficerResponse = new TruProxyOfficerResponse(listOfOfficers);

        JsonNode jsonCompany = objectMapper.valueToTree(truProxyCompanyResponse);
        JsonNode jsonOfficer = objectMapper.valueToTree(truProxyOfficerResponse);

        stubFor(get(urlPathMatching("/rest/Companies/v1/Search\\\\?.*"))
            .willReturn(aResponse()
                .withHeader("x-api-key", "someApiKey")
                .withJsonBody(jsonCompany)
            )
        );

        stubFor(get(urlPathMatching("/rest/Companies/v1/Officers\\\\?.*"))
            .willReturn(aResponse()
                .withHeader("x-api-key", "someApiKey")
                .withJsonBody(jsonOfficer)
            )
        );

        SearchRequestBody searchRequestBody = new SearchRequestBody("ABC Company", "12345");
        CompanyResponse response = searchService.findCompanyAndOfficer("someApiKey", true, searchRequestBody);

        assertNotNull(response);
        assertNotNull(response.items());
        assertEquals(1, response.items().size());
        assertEquals("12345", response.items().get(0).companyNumber());
        assertEquals("ABC Company", response.items().get(0).companyType());
        assertEquals("active", response.items().get(0).companyStatus());
        assertEquals("ltd", response.items().get(0).title());
        assertEquals("2008-02-11", response.items().get(0).dateOfCreation());
        assertNotNull(response.items().get(0).address());

        assertEquals(2, response.items().get(0).officers().size());
        assertEquals("John", response.items().get(0).officers().get(0).name());
        assertEquals("Neo", response.items().get(0).officers().get(1).name());
    }

}
