package com.example.service;

import com.example.exceptions.CustomClientException;
import com.example.exceptions.CustomServerException;
import com.example.model.dto.Company;
import com.example.model.dto.Officer;
import com.example.model.dto.TruProxyCompany;
import com.example.model.request.SearchRequestBody;
import com.example.model.response.CompanyResponse;
import com.example.model.response.TruProxyCompanyResponse;
import com.example.model.response.TruProxyOfficerResponse;
import com.example.rest.TruProxyRestfulClient;
import io.micrometer.common.util.StringUtils;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchService.class);

    private static final String ACTIVE_STATUS = "active";
    private static final String COMPANY_SEARCH_QUERY = "/Search?Query=%s";
    private static final String OFFICERS_COMPANY_NUMBER_SEARCH_QUERY = "/Officers?CompanyNumber=%s";
    private final TruProxyRestfulClient truProxyRestfulClient;

    public SearchService(TruProxyRestfulClient truProxyRestfulClient) {
        this.truProxyRestfulClient = truProxyRestfulClient;
    }

    private String getEndpoint(String path, String searchTerm) {
        return String.format(path, searchTerm);
    }

    public CompanyResponse findCompanyAndOfficer(String apiKey,
                                                 Boolean isActive,
                                                 SearchRequestBody body) {

        String searchTerm = StringUtils.isNotBlank(body.companyNumber()) ? body.companyNumber() : body.companyName();

        if (StringUtils.isEmpty(searchTerm)) {
            log.info("No search term provided");
            return new CompanyResponse(0, Collections.emptyList());
        }

        try {
            ResponseEntity<TruProxyCompanyResponse> companyResponse =
                truProxyRestfulClient.genericExchange(apiKey, getEndpoint(COMPANY_SEARCH_QUERY, searchTerm), TruProxyCompanyResponse.class);

            List<TruProxyCompany> companies = filterCompanies(isActive, companyResponse);

            ResponseEntity<TruProxyOfficerResponse> officerResponse =
                truProxyRestfulClient.genericExchange(apiKey, getEndpoint(OFFICERS_COMPANY_NUMBER_SEARCH_QUERY, searchTerm), TruProxyOfficerResponse.class);

            List<Officer> officerResponseBodies = filterOutResignedOfficers(officerResponse);

            return new CompanyResponse(companies.size(), createCompanyAndOfficerResponse(companies, officerResponseBodies));
        } catch (CustomClientException e) {
            log.error("Client error occurred: {}", e.getMessage());
            return new CompanyResponse(0, Collections.emptyList());
        } catch (CustomServerException e) {
            log.error("Server error occurred: {}", e.getMessage());
            return new CompanyResponse(0, Collections.emptyList());
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            return new CompanyResponse(0, Collections.emptyList());
        }
    }

    private static List<Company> createCompanyAndOfficerResponse(List<TruProxyCompany> companies, List<Officer> officerResponseBodies) {
        return companies.stream()
            .map(company -> new Company(
                company.companyNumber(),
                company.companyType(),
                company.title(),
                company.companyStatus(),
                company.dateOfCreation(),
                company.address(),
                officerResponseBodies
            ))
            .collect(Collectors.toList());
    }

    private List<Officer> filterOutResignedOfficers(ResponseEntity<TruProxyOfficerResponse> officerResponse) {
        List<Officer> officers = Objects.requireNonNull(officerResponse.getBody()).items();

        if (officers == null || officers.isEmpty()) {
            return Collections.emptyList();
        }

        return officers.stream()
            .filter(officer -> officer.resignedOn() == null)
            .toList();
    }

    private static List<TruProxyCompany> filterCompanies(boolean isActive, ResponseEntity<TruProxyCompanyResponse> companyResponse) {
        List<TruProxyCompany> companies = Objects.requireNonNull(companyResponse.getBody()).items();

        if (companies == null || companies.isEmpty()) {
            return Collections.emptyList();
        }

        return companies.stream()
            .filter(company -> isActive == ACTIVE_STATUS.equalsIgnoreCase(company.companyStatus()))
            .toList();
    }

}
