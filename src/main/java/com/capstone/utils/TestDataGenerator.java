// ABOUTME: Generates randomized test data for API requests.
// ABOUTME: Ensures unique emails and names to avoid conflicts between test runs.

package com.capstone.utils;

import com.capstone.pojo.User;
import java.util.UUID;

public class TestDataGenerator {

    public static User createRandomUser() {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        return new User(
            "TestUser_" + unique,
            "testuser_" + unique + "@example.com",
            "male",
            "active"
        );
    }

    public static String randomEmail() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    }

    public static String randomName() {
        return "User_" + UUID.randomUUID().toString().substring(0, 8);
    }
}
