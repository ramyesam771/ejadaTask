package com.ejadaSolutions.stepDefinitions.ejadaSolution.api;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestAssuredApisStepDef {
    public static Response getResponse;
    private static Response postResponse;
    private static Response putResponse;
    private static Response patchResponse;
    private static Response deleteResponse;
    private final String token = "c7e1e3083777e73c348ae9feb56ebfaba7fd51fb0711a1f8f948124e0636248d";
    private static String orderId = "";

    @Given("the base URL is {string}")
    public void the_api_is_running(String baseUrl) {
        System.out.println("The base url is set to " + baseUrl);
        RestAssured.baseURI = baseUrl;
    }

    @When("user get response of the endpoint {string}")
    public void user_gets_response_of_the_endpoint(String endpoint) {
        getResponse = RestAssured.get(endpoint);
        System.out.println("Response Status Code: " + getResponse.getStatusCode());
        System.out.println("Response Headers: " + getResponse.getHeaders());
        System.out.println("Response Body: " + getResponse.getBody().asString());
    }

    @Then("the status should be {string} with a status code {int}")
    public void the_status_should_be(String status, int expectedStatusCode) {
        System.out.println("Expected Status: " + status);
        System.out.println("Actual Status: " + getResponse.jsonPath().getString("status"));
        assertEquals(expectedStatusCode, getResponse.getStatusCode());
        assertEquals(status, getResponse.jsonPath().getString("status"));
    }

    @Then("the book title should be {string}")
    public void the_book_title_should_be(String name) {
        assertEquals(name, getResponse.jsonPath().getString("name"));
    }


    @Then("the {string} response status code should be {int}")
    public void verifyPostResponseStatusCode(String requestType, int expectedStatusCode) {
        Response response = null;
        switch (requestType.toLowerCase()) {
            case "get":
                response = getResponse;
                break;
            case "put":
                response = putResponse;
                break;
            case "post":
                response = postResponse;
                break;
            case "delete":
                response = deleteResponse;
                break;
            case "patch":
                response = patchResponse;
        }
        assert response != null;
        System.out.println(response.getBody().asString());
        assertStatusCode(response, expectedStatusCode);
    }

    private void assertStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        System.out.println("The Actual status code is : " + actualStatusCode);
        Assert.assertEquals(expectedStatusCode, actualStatusCode);
    }


    @When("user submit a new order with book ID {int} and customer name {string}")
    public void i_submit_a_new_order_with_book_id_and_customer_name(int bookId, String customerName) {
        String bodyText = String.format("{\"bookId\": %d, \"customerName\": \"%s\"}", bookId, customerName);
        postResponse = given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(bodyText)
                .when()
                .post("/orders");

        orderId = postResponse.jsonPath().getString("orderId");
        System.out.println("Order ID: " + orderId);
    }


    @When("user get orders list")
    public void getOrdersList() {
        getResponse = given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .when()
                .get("/orders");
    }

    @When("user get created order")
    public void getSingleOrder() {
        System.out.println("Value of order ID: " + orderId);
        getResponse = given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .when()
                .get("/orders/" + orderId);
    }


    @When("user update the order with customer name {string}")
    public void i_update_the_order_with_customer_name(String customerName) {
        String requestBody = "{ \"customerName\": \"" + customerName + "\" }";
        patchResponse = given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .patch("/orders/" + orderId);
    }

    @When("user delete the order")
    public void i_delete_the_order() {
        deleteResponse = given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .when()
                .delete("/orders/" + orderId);
    }

}