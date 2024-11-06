package com.ejadaSolutions.common.utils.cucumber;

import com.ejadaSolutions.common.ui.base.BaseWebDriver;
import com.ejadaSolutions.common.utils.screnShots.ScreenShot;
import org.monte.screenrecorder.ScreenRecorder;
import com.ejadaSolutions.common.utils.timer.MyTimer;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import lombok.extern.java.Log;
import com.ejadaSolutions.common.utils.logs.MyLogger;
import org.apache.logging.log4j.core.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@Log
public class CucumberHooks {
    private static final BaseWebDriver baseWebDriver = new BaseWebDriver();
    public static Logger log = new MyLogger().getLogger();
    private static String dataAttached = "";
    private static int count = 0;
    private static ScreenRecorder screenRecorder;

    public static void setLastRestAssuredDetails(RequestSpecification request, Response response) {
        logRequestAndResponseDetails(request, response);
    }

    public static void setTextToAttach(String content) {
        dataAttached = content;
    }

    private static void logRequestAndResponseDetails(RequestSpecification lastRequest, Response lastResponse) {
        if (lastRequest != null && lastResponse != null) {
            FilterableRequestSpecification request = (FilterableRequestSpecification) lastRequest;
            String requestContent = "Request: \n" + request.getMethod() + " " + request.getURI() + "\nHeaders: " + request.getHeaders() + "\nBody: \n" + request.getBody();
            String responseContent = "\n\nResponse: \nHTTP Code: " + lastResponse.getStatusCode() + "\nHeaders: " + lastResponse.headers().toString() + "\nBody: " + lastResponse.body().prettyPrint();
            dataAttached = dataAttached + "\n" + requestContent + "\n" + responseContent + "\n";
        } else if (lastRequest != null) {
            FilterableRequestSpecification request = (FilterableRequestSpecification) lastRequest;
            String requestContent = request.log().method().toString() + request.log().uri().toString() + "\nHeaders: " + request.log().headers().toString() + "\nBody: \n" + request.log().body().toString();
            dataAttached = dataAttached + "\n" + requestContent;
        } else if (lastResponse != null) {
            String responseContent = "HTTP Code: " + lastResponse.getStatusCode() + "\nHeaders: " + lastResponse.headers().toString() + "\nBody: " + lastResponse.body().prettyPrint();
            dataAttached = dataAttached + "\n" + responseContent;
        }
    }

    private static void addTextBox(Scenario scenario) {
        if (!dataAttached.isEmpty()) {
            scenario.attach(dataAttached, "text/plain", "HTTP data " + count++);
        }
    }


    private File attachStepScreenshot(Scenario scenario, String fileName) {
        File screenShotFile = null;
        ScreenShot screenShot = new ScreenShot();
        boolean reduceScreenshotSize = Boolean.parseBoolean(System.getProperty("ReduceScreenshotSize", "false"));
        if (baseWebDriver.isDriverActive()) {
            try {
                if (reduceScreenshotSize) {
                    // Attach the resized image to the scenario
                    scenario.attach(screenShot.TakeReducedDimensionScreenShots(baseWebDriver.getDriver(), fileName), "image/png", "screenshot " + fileName);
                } else {
                    screenShotFile = new ScreenShot().takeWebScreenShot(baseWebDriver.getDriver(), fileName);
                    scenario.attach(Files.readAllBytes(screenShotFile.toPath()), "image/png", "screenshot " + screenShotFile.getName());
                }
            } catch (IOException e) {
                log.error("Error attaching screenshot.\n {}", e.getMessage());
            }
        }
        return screenShotFile;
    }

    /*********************
     * Web Ui Cucumber Hooks
     * ******************/

    @AfterStep("@ui or @UI or @Ui")
    public void afterStepUI(Scenario scenario) {
        // Generate a safe file name based on the scenario name
        String fileStepName = scenario.getName()
                .replaceAll("[ <>&:']", "_")
                .replaceAll("\"", "")
                .replaceAll("/", "");
        // Ensure the file name is within length limits
        String fileName = new MyTimer().getCurrentTimeInString() + fileStepName;
        if (fileName.length() > 170) {
            fileName = fileName.substring(0, 170);
        }
        log.info("Creating screenshot with file name: {}", fileName);
        try {
            if (attachStepScreenshot(scenario, fileName) != null) {
                log.info("After step screenshot attached.");
            } else {
                log.warn("Failed to attach screenshot for scenario: {}", scenario.getName());
            }
        } catch (Exception e) {
            log.warn("Error attaching screenshot for scenario: {}\n{}", scenario.getName(), e.getMessage());
        }
    }

    @After("@ui or @UI or @Ui")
    public void closeBrowser() {
        if (baseWebDriver.isDriverActive()) {
            baseWebDriver.quitAndRemoveDriver();
            log.info("WebDriver quit successfully.");
        } else {
            log.info("No active WebDriver instance to quit.");
        }
    }

    /*******************************
     * Logs organizer Cucumber Hooks
     * *****************************/

    @Before
    public void executionStarted() {
        log.info("""
                ###########################################################
                ###########################################################
                #################### Execution Started ####################
                ###########################################################
                ###########################################################
                """);
    }

    @After
    public void executionFinished() {
        log.info("""
                ############################################################
                ############################################################
                #################### Execution Finished. ####################
                ############################################################
                ############################################################
                """);
    }
}
