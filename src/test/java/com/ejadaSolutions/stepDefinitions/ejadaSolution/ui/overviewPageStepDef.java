package com.ejadaSolutions.stepDefinitions.ejadaSolution.ui;

import com.ejadaSolutions.common.ui.base.BaseWebDriver;
import com.ejadaSolutions.ejada.ui.OverviewPage;
import io.cucumber.java.en.Then;

public class overviewPageStepDef extends BaseWebDriver {
    private final OverviewPage overviewPage  = new OverviewPage();

    @Then("user verify that URL matched with {string}")
    public void validateUrl(String url) {
        overviewPage.assertUrl(url);
    }

    @Then("user validate total price are equal item price")
    public void validatePrice() {
        overviewPage.validateTotalPrice();
    }

    @Then("user click on Finish")
    public void clickOnFinish() {
        overviewPage.clickFinish();
    }



}