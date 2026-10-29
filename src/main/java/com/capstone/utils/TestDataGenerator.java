// ABOUTME: Generates randomized test data for API requests.
// ABOUTME: Ensures unique emails and names to avoid conflicts between test runs.

package com.capstone.utils;

import java.util.UUID;

public class TestDataGenerator {

    public static String randomEmail() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    }

    public static String randomName() {
        return "User_" + UUID.randomUUID().toString().substring(0, 8);
    }
}
