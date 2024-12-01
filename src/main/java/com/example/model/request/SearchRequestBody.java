package com.example.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.json.JSONObject;
import org.springframework.validation.annotation.Validated;

@Validated
public record SearchRequestBody(
    @Schema(format = "string", example = "bbc", description = "Name of the company to search for")
    @JsonProperty
    String companyName,

    @Schema(format = "string", example = "12345", description = "Number of the company to search for")
    @JsonProperty
    String companyNumber
) {
    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("companyName", companyName);
        object.put("companyNumber", companyNumber);
        return object.toString();
    }

}
