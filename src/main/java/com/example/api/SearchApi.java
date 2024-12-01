package com.example.api;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.DEFAULT;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import com.example.model.request.SearchRequestBody;
import com.example.model.response.CompanyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
public interface SearchApi {

    @Operation(summary = "Search for company details ", description = "")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = CompanyResponse.class)))
    @PostMapping(value = "/companies", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<CompanyResponse> findCompanyPost(@Parameter(in = HEADER, description = "The API key is passed in via header", required = true) @RequestHeader(value = ApiConstants.X_API_KEY) String apiKey,
                                                    @Parameter(in = QUERY, description = "Active company search") @Valid @RequestParam(value = "active", required = false) Boolean active,
                                                    @Parameter(in = DEFAULT, required = true) @Valid @RequestBody SearchRequestBody body);

}
