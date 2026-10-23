// ABOUTME: Tests for AutomationExercise Product APIs.
// ABOUTME: Covers listing products, verifying product details, and searching products.

package com.capstone.tests;

import com.capstone.base.BaseTest;
import com.capstone.endpoints.Routes;
import com.capstone.utils.ApiHelper;
import com.capstone.config.ConfigReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ProductApiTest extends BaseTest {

    private JsonPath extractJson(Response response) {
        String body = response.asString()
            .replaceAll("<[^>]*>", "")
            .trim();
        return new JsonPath(body);
    }

    @Test(priority = 1, description = "Get all products and verify list is returned")
    public void testGetProductList() {
        Response response = given()
            .spec(ApiHelper.productRequestSpec())
        .when()
            .get(Routes.PRODUCTS_LIST)
        .then()
            .statusCode(200)
            .extract().response();

        JsonPath json = extractJson(response);
        int responseCode = json.getInt("responseCode");
        Assert.assertEquals(responseCode, 200, "API responseCode should be 200");

        int productCount = json.getList("products").size();
        Assert.assertTrue(productCount > 0, "Product list should not be empty");

        test.get().info("Retrieved " + productCount + " products");
    }

    @Test(priority = 2, description = "Get product details and validate fields")
    public void testGetProductDetails() {
        Response response = given()
            .spec(ApiHelper.productRequestSpec())
        .when()
            .get(Routes.PRODUCTS_LIST)
        .then()
            .statusCode(200)
            .extract().response();

        JsonPath json = extractJson(response);
        int firstProductId = json.getInt("products[0].id");
        String firstProductName = json.getString("products[0].name");
        String firstProductPrice = json.getString("products[0].price");
        String firstProductBrand = json.getString("products[0].brand");

        Assert.assertTrue(firstProductId > 0, "Product ID should be positive");
        Assert.assertNotNull(firstProductName, "Product name should not be null");
        Assert.assertFalse(firstProductName.isEmpty(), "Product name should not be empty");
        Assert.assertNotNull(firstProductPrice, "Product price should not be null");
        Assert.assertNotNull(firstProductBrand, "Product brand should not be null");

        test.get().info("Verified product: ID=" + firstProductId + ", Name=" + firstProductName);
    }

    @Test(priority = 3, description = "Search for a product by name")
    public void testSearchProduct() {
        Response response = given()
            .spec(ApiHelper.productRequestSpec())
            .contentType("application/x-www-form-urlencoded")
            .formParam("search_product", "top")
        .when()
            .post(Routes.SEARCH_PRODUCT)
        .then()
            .statusCode(200)
            .extract().response();

        JsonPath json = extractJson(response);
        int responseCode = json.getInt("responseCode");
        Assert.assertEquals(responseCode, 200, "Search API responseCode should be 200");

        int resultCount = json.getList("products").size();
        Assert.assertTrue(resultCount > 0, "Search should return at least one product");

        test.get().info("Search for 'top' returned " + resultCount + " products");
    }
}
