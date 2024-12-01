package com.example.model.response;

import com.example.model.dto.TruProxyCompany;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Validated
public record TruProxyCompanyResponse(
    @JsonProperty("total_results")
    int totalResults,
    List<TruProxyCompany> items) implements Serializable {
}
