package com.example.model.response;

import com.example.model.dto.Officer;
import java.io.Serializable;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Validated
public record TruProxyOfficerResponse(
    List<Officer> items) implements Serializable {
}
