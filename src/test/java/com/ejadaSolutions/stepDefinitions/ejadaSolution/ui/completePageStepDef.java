package com.ejadaSolutions.stepDefinitions.ejadaSolution.ui;

import com.ejadaSolutions.common.ui.base.BaseWebDriver;
import com.ejadaSolutions.ejada.ui.CompletePage;
import io.cucumber.java.en.Then;

public class completePageStepDef extends BaseWebDriver {
    private final CompletePage completePage  = new CompletePage();

    @Then("user validate {string} and {string} messages are shown")
    public void validateUrl(String firstMessage, String secondMessage) {
        completePage.assertMessage(firstMessage);
        completePage.assertMessage(secondMessage);
    }
}