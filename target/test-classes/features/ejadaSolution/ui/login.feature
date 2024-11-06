@Ui @logInPage
Feature: Login page validation

  Background:
    Given user open "getProp.Browser" browser
    Then user navigate to URL "getProp.ejadaSolution"

  Scenario: Positive Scenario - Login to ejadaSolution website with correct credentials
    When validate user is on "login" page
    And user set the username: "standard_user"
    And user set the Password: "secret_sauce"
    Then user click on "Login" button
    And validate user is on "Products" page


  Scenario Outline: Negative Scenarios - Login with invalid credentials
    When validate user is on "login" page
    And user set the username: "<userName>"
    And user set the Password: "<passWord>"
    Then user click on "Login" button
    Then validate error message "<errorMessage>" is displayed

    Examples:
      | userName      | passWord      | errorMessage                                                              |
      | standard_user | wrongPassword | Epic sadface: Username and password do not match any user in this service |
      | wrongUser     | secret_sauce  | Epic sadface: Username and password do not match any user in this service |
      | wrongUser     | wrongPassword | Epic sadface: Username and password do not match any user in this service |
      |               | secret_sauce  | Epic sadface: Username is required                                        |
      | standard_user |               | Epic sadface: Password is required                                        |
      |               |               | Epic sadface: Username is required                                        |