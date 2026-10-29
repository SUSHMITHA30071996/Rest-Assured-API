// ABOUTME: Builds reusable REST Assured request specifications for authenticated and public APIs.
// ABOUTME: Centralizes base URI, headers, and auth token configuration.

package com.capstone.utils;

import com.capstone.config.ConfigReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiHelper {

    public static RequestSpecification gorestRequestSpec() {
        return new RequestSpecBuilder()
            .setBaseUri(ConfigReader.getGorestBaseUrl())
            .setContentType(ContentType.JSON)
            .addHeader("Authorization", "Bearer " + ConfigReader.getGorestToken())
            .build();
    }

}
