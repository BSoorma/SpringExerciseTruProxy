package com.example.demo.api.controller;

import com.example.demo.api.SearchCompanyApi;
import com.example.demo.model.CompanySearchRequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchCompanyApiController implements SearchCompanyApi {

    public ResponseEntity<String> companyPost(String apiKey, CompanySearchRequestBody body) {
        return null;
    }

}
