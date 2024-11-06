@api
Feature: API Status, List books and Create an order

  Background:
    Given the base URL is "https://simple-books-api.glitch.me"

  Scenario: Check API status
    When user get response of the endpoint "/status"
    Then the status should be "OK" with a status code 200

  Scenario: Get list of books
    When user get response of the endpoint "/books"
    Then the "Get" response status code should be 200

  Scenario: Get a single book
    When user get response of the endpoint "/books/1"
    Then the "Get" response status code should be 200
    And the book title should be "The Russian"

  Scenario: Submit a new order
    When user submit a new order with book ID 1 and customer name "John"
    Then the "Post" response status code should be 201

  Scenario: Get Orders List
    When user get orders list
    Then the "Get" response status code should be 200

  Scenario: Get the created order
    When user get created order
    Then the "Get" response status code should be 200

  Scenario: Update an existing order
    When user update the order with customer name "John"
    Then the "Patch" response status code should be 204

  Scenario: Delete an existing order
    When user delete the order
    Then the "Delete" response status code should be 204
