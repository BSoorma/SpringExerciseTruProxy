package com.example.api;

import static com.example.api.ApiConstants.X_API_KEY;
import static com.example.constants.TestConstants.API_COMPANIES_ENDPOINT;
import static com.example.constants.TestConstants.BBC_COMPANY_NAME;
import static com.example.constants.TestConstants.COMPANY_NUMBER;
import static com.example.constants.TestConstants.SOME_A_PI_KEY;
import static com.example.constants.TestConstants.TEST_COMPANY_RESPONSE;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.hc.core5.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.example.model.request.SearchRequestBody;
import com.example.service.SearchService;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchApiControllerTest {

    @MockitoBean
    SearchService mockSearchService;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    void findCompanyPostSuccess() {
        SearchRequestBody searchRequestBody = new SearchRequestBody(BBC_COMPANY_NAME, COMPANY_NUMBER);

        when(mockSearchService.findCompanyAndOfficer(SOME_A_PI_KEY, true, searchRequestBody))
            .thenReturn(TEST_COMPANY_RESPONSE);

        String requestBody = "{\"companyName\": \"" + BBC_COMPANY_NAME + "\",\"companyNumber\": \"" + COMPANY_NUMBER + "\"}";

        ExtractableResponse<Response> response = given()
            .contentType(JSON)
            .header(new Header(X_API_KEY, SOME_A_PI_KEY))
            .queryParam("active", true)
            .body(requestBody)
            .when()
            .post(API_COMPANIES_ENDPOINT)
            .then()
            .statusCode(SC_OK)
            .contentType(JSON)
            .extract();

        String expectedResponse = "{\"total_results\":1," +
            "\"items\":[{\"company_number\":\"12345678\",\"company_type\":\"companyType\",\"title\":\"title\"," +
            "\"company_status\":\"companyStatus\",\"date_of_creation\":\"dateOfCreation\"," +
            "\"address\":{\"locality\":\"Teddington\",\"postal_code\":\"TW11 9NH\",\"premises\":\"2\"," +
            "\"address_line_1\":\"2 Quay West Court\",\"country\":\"UK\"}," +
            "\"officers\":[{\"name\":\"DITTMAR, Frank\",\"officer_role\":\"director\",\"appointed_on\":\"1999-07-01\"," +
            "\"address\":{\"locality\":\"Teddington\",\"postal_code\":\"TW11 9NH\",\"premises\":\"2\"," +
            "\"address_line_1\":\"2 Quay West Court\",\"country\":\"UK\"}}]}]}";

        assertEquals(expectedResponse, response.body().asString());
    }

}