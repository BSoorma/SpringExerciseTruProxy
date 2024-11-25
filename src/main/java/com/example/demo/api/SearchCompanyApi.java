package com.example.demo.api;

import static com.example.demo.api.ApiConstants.X_API_KEY;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.DEFAULT;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import com.example.demo.model.CompanySearchRequestBody;
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

@Validated
public interface SearchCompanyApi {

    @Operation(summary = "Search for company details ", description = "")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class)))
    @PostMapping(value = "/company", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<String> companyPost(@Parameter(in = HEADER, description = "The API key is passed in via header", required = true) @RequestHeader(value = X_API_KEY) String apiKey,
                                       @Parameter(in = DEFAULT, required = true) @Valid @RequestBody CompanySearchRequestBody body);

}
