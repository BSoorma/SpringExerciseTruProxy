package com.example.api;

import com.example.model.request.SearchRequestBody;
import com.example.model.response.CompanyResponse;
import com.example.service.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchApiController implements SearchApi {

    private final SearchService searchService;

    public SearchApiController(SearchService searchService) {
        this.searchService = searchService;
    }

    public ResponseEntity<CompanyResponse> findCompanyPost(String apiKey,
                                                           Boolean isActive,
                                                           SearchRequestBody body) {
        CompanyResponse response = searchService.findCompanyAndOfficer(apiKey, isActive, body);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}