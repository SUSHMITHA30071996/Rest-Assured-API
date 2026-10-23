// ABOUTME: Tests for GoRest Post and Comment APIs.
// ABOUTME: Covers listing posts, creating posts for users, and adding comments.

package com.capstone.tests;

import com.capstone.base.BaseTest;
import com.capstone.endpoints.Routes;
import com.capstone.pojo.User;
import com.capstone.utils.ApiHelper;
import com.capstone.utils.TestDataGenerator;
import com.capstone.config.ConfigReader;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PostApiTest extends BaseTest {

    private int userId;
    private int postId;

    @BeforeClass
    public void createTestUser() {
        User user = TestDataGenerator.createRandomUser();
        Response response = given()
            .spec(ApiHelper.gorestRequestSpec())
            .body(user)
        .when()
            .post(Routes.USERS)
        .then()
            .statusCode(201)
            .extract().response();

        userId = response.jsonPath().getInt("id");
    }

    @Test(priority = 1, description = "Get all posts and verify response structure")
    public void testGetAllPosts() {
        given()
            .spec(ApiHelper.gorestRequestSpec())
        .when()
            .get(Routes.POSTS)
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("[0].id", notNullValue())
            .body("[0].title", notNullValue())
            .body("[0].body", notNullValue())
            .time(lessThan(ConfigReader.getResponseTimeout()));

        test.get().info("Successfully retrieved posts list");
    }

    @Test(priority = 2, description = "Create a post for a specific user")
    public void testCreatePostForUser() {
        String postBody = "{\"title\": \"Capstone Test Post\", \"body\": \"This post was created by the REST Assured automation framework.\"}";

        Response response = given()
            .spec(ApiHelper.gorestRequestSpec())
            .pathParam("userId", userId)
            .body(postBody)
        .when()
            .post(Routes.USER_POSTS)
        .then()
            .statusCode(201)
            .body("title", equalTo("Capstone Test Post"))
            .body("user_id", equalTo(userId))
            .body("id", notNullValue())
            .time(lessThan(ConfigReader.getResponseTimeout()))
            .extract().response();

        postId = response.jsonPath().getInt("id");
        test.get().info("Created post ID: " + postId + " for user ID: " + userId);
    }

    @Test(priority = 3, dependsOnMethods = "testCreatePostForUser", description = "Add a comment to a post")
    public void testCommentOnPost() {
        String commentBody = "{\"name\": \"Capstone Tester\", \"email\": \"tester@example.com\", \"body\": \"Automated comment from REST Assured framework.\"}";

        given()
            .spec(ApiHelper.gorestRequestSpec())
            .pathParam("postId", postId)
            .body(commentBody)
        .when()
            .post(Routes.POST_COMMENTS)
        .then()
            .statusCode(201)
            .body("name", equalTo("Capstone Tester"))
            .body("email", equalTo("tester@example.com"))
            .body("post_id", equalTo(postId))
            .time(lessThan(ConfigReader.getResponseTimeout()));

        test.get().info("Added comment to post ID: " + postId);
    }

    @AfterClass
    public void cleanupTestUser() {
        given()
            .spec(ApiHelper.gorestRequestSpec())
            .pathParam("userId", userId)
        .when()
            .delete(Routes.USER_BY_ID);
    }
}
