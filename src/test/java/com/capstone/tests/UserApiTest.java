// ABOUTME: Tests for GoRest User API covering CRUD operations.
// ABOUTME: Validates status codes, response body fields, and response time.

package com.capstone.tests;

import com.capstone.base.BaseTest;
import com.capstone.endpoints.Routes;
import com.capstone.pojo.User;
import com.capstone.utils.ApiHelper;
import com.capstone.utils.TestDataGenerator;
import com.capstone.config.ConfigReader;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserApiTest extends BaseTest {

    private static int createdUserId;

    @Test(priority = 1, description = "Create a new user via POST")
    public void testCreateUser() {
        User user = TestDataGenerator.createRandomUser();

        Response response = given()
            .spec(ApiHelper.gorestRequestSpec())
            .body(user)
        .when()
            .post(Routes.USERS)
        .then()
            .statusCode(201)
            .body("name", equalTo(user.getName()))
            .body("email", equalTo(user.getEmail()))
            .body("gender", equalTo(user.getGender()))
            .body("status", equalTo(user.getStatus()))
            .body("id", notNullValue())
            .time(lessThan(ConfigReader.getResponseTimeout()))
            .extract().response();

        createdUserId = response.jsonPath().getInt("id");
        test.get().info("Created user ID: " + createdUserId);
    }

    @Test(priority = 2, dependsOnMethods = "testCreateUser", description = "Get a single user by ID")
    public void testGetUserById() {
        given()
            .spec(ApiHelper.gorestRequestSpec())
            .pathParam("userId", createdUserId)
        .when()
            .get(Routes.USER_BY_ID)
        .then()
            .statusCode(200)
            .body("id", equalTo(createdUserId))
            .body("name", notNullValue())
            .body("email", notNullValue())
            .body("gender", anyOf(equalTo("male"), equalTo("female")))
            .body("status", anyOf(equalTo("active"), equalTo("inactive")))
            .time(lessThan(ConfigReader.getResponseTimeout()));

        test.get().info("Retrieved user ID: " + createdUserId);
    }

    @Test(priority = 3, description = "Get all users and verify response structure")
    public void testGetAllUsers() {
        given()
            .spec(ApiHelper.gorestRequestSpec())
        .when()
            .get(Routes.USERS)
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("[0].id", notNullValue())
            .body("[0].name", notNullValue())
            .body("[0].email", notNullValue())
            .time(lessThan(ConfigReader.getResponseTimeout()));

        test.get().info("Successfully retrieved users list");
    }

    @Test(priority = 4, dependsOnMethods = "testCreateUser", description = "Update an existing user via PUT")
    public void testUpdateUser() {
        String updatedName = TestDataGenerator.randomName();

        given()
            .spec(ApiHelper.gorestRequestSpec())
            .pathParam("userId", createdUserId)
            .body("{\"name\": \"" + updatedName + "\", \"status\": \"inactive\"}")
        .when()
            .put(Routes.USER_BY_ID)
        .then()
            .statusCode(200)
            .body("name", equalTo(updatedName))
            .body("status", equalTo("inactive"))
            .time(lessThan(ConfigReader.getResponseTimeout()));

        test.get().info("Updated user " + createdUserId + " name to: " + updatedName);
    }

    @Test(priority = 5, dependsOnMethods = "testCreateUser", description = "Delete user via DELETE")
    public void testDeleteUser() {
        given()
            .spec(ApiHelper.gorestRequestSpec())
            .pathParam("userId", createdUserId)
        .when()
            .delete(Routes.USER_BY_ID)
        .then()
            .statusCode(204)
            .time(lessThan(ConfigReader.getResponseTimeout()));

        test.get().info("Deleted user ID: " + createdUserId);
    }
}
