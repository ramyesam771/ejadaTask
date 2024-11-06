# EjadaWebAutomationTask


## Automation Testing for "Swag Labs" Web Application
The framework tests the login feature with both positive and negative scenarios. It also focuses on the basic test cases for the purchase process.
For the API, it tests the Book system API to cover most of the test cases mentioned in the task

## Prerequisites
   * Java 17 or higher
   * Maven
   * An IDE (e.g., IntelliJ IDEA, Eclipse)

## Framework
The framework is designed in java using selenium and cucumber (gherkin), the framework supports three browsers (Chrome, Edge and Firefox) according to the config file of the project.

## Design Pattern
The project is using the page object model.

## Concepts Included

* Parallel test runs
* Headless Mode
* Page Object pattern
* Common web page interaction methods


## Tools

* Maven
* Cucumber-JVM
* JUnit
* Selenium Webdriver
* Jackson

## Test Run
In order to run the project follow below:
1. Test Runners
    * There are test runner file `TestRunner.java` that can be executed as a `Application test` where you can set parallel execution with true or false.

## Reporting
Reporting in the project is exported automatically to provide the tests results at the `/target` directory using mainly One reporting plugins:

    1- `Cucumber reports` that generates the reports in the `/target/cucumber` directory.
