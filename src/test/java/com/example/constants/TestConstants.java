package com.example.constants;

import com.example.model.dto.Address;
import com.example.model.dto.Company;
import com.example.model.dto.Officer;
import com.example.model.dto.TruProxyCompany;
import com.example.model.response.CompanyResponse;
import java.util.List;

public class TestConstants {

    //Test Constants
    public static final String SOME_API_KEY = "someAPiKey";
    public static final String COMPANY_NUMBER = "888";
    public static final String BBC_COMPANY_NAME = "BBC LIMITED";

    //Endpoints
    public static final String API_COMPANIES_ENDPOINT = "/api/companies";

    //Expected Responses
    public static final Address TEST_COMPANY_ADDRESS = new Address(
        "Teddington",
        "TW11 9NH",
        "2",
        "2 Quay West Court",
        "UK"
    );

    public static final Officer TEST_OFFICER_RESIGNED = new Officer(
        "DITTMAR, Frank",
        "director",
        "1999-07-01",
        TEST_COMPANY_ADDRESS,
        "1999-07-01"
    );

    public static final Officer TEST_OFFICER_NOT_RESIGNED = new Officer(
        "name",
        "officerRole",
        "appointedOn",
        TEST_COMPANY_ADDRESS,
        null
    );

    public static final CompanyResponse TEST_COMPANY_RESPONSE = new CompanyResponse(1,
        List.of(new Company("12345678",
                "companyType",
                "title",
                "companyStatus",
                "dateOfCreation",
                TEST_COMPANY_ADDRESS,
                List.of(TEST_OFFICER_RESIGNED)
            )
        )
    );

    public static final List<TruProxyCompany> TEST_TRUPROXY_ACTIVE_COMPANY_RESPONSE_LIST =
        List.of(
            new TruProxyCompany(
                "888",
                "ltd",
                "12345 LIMITED",
                "active",
                "1999-07-01",
                TEST_COMPANY_ADDRESS
            )
        );

    public static final TruProxyCompany TEST_TRUPROXY_ACTIVE_COMPANY_RESPONSE =
        new TruProxyCompany(
            "888",
            "ltd",
            "12345 LIMITED",
            "active",
            "1999-07-01",
            TEST_COMPANY_ADDRESS
        );

    public static final TruProxyCompany TEST_TRUPROXY_DISSOLVED_COMPANY_RESPONSE =
        new TruProxyCompany(
            "888",
            "ltd",
            "333 LIMITED",
            "dissolved",
            "1995-05-05",
            TEST_COMPANY_ADDRESS
        );

    public static final TruProxyCompany TEST_TRUPROXY_INACTIVE_COMPANY_RESPONSE =
        new TruProxyCompany(
            "888",
            "ltd",
            "555 LIMITED",
            "inactive",
            "1998-08-08",
            TEST_COMPANY_ADDRESS
        );

    public static final List<TruProxyCompany> TEST_TRUPROXY_BBC_COMPANY_RESPONSE =
        List.of(
            new TruProxyCompany(
                "123",
                "ltd",
                "BBC LIMITED",
                "active",
                "1999-07-01",
                TEST_COMPANY_ADDRESS
            )
        );

    public static final List<TruProxyCompany> TEST_TRUPROXY_MIXED_COMPANY_RESPONSE = List.of(
        TEST_TRUPROXY_DISSOLVED_COMPANY_RESPONSE,
        TEST_TRUPROXY_INACTIVE_COMPANY_RESPONSE,
        TEST_TRUPROXY_ACTIVE_COMPANY_RESPONSE
    );

}
