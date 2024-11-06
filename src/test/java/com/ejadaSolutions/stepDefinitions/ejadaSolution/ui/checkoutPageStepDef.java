package com.ejadaSolutions.stepDefinitions.ejadaSolution.ui;

import com.ejadaSolutions.common.ui.base.BaseWebDriver;
import com.ejadaSolutions.ejada.ui.CheckoutPage;
import io.cucumber.java.en.Then;

public class checkoutPageStepDef extends BaseWebDriver {
    private final CheckoutPage checkoutPage  = new CheckoutPage();

    @Then("user fills required data for checkout")
    public void fillCheckoutData() {
        checkoutPage.setFirstName();
        checkoutPage.setlastName();
        checkoutPage.setPostalCode();
    }


    @Then("user clicks on {string} button in checkout page")
    public void checkoutPageButtons(String button) {
        switch (button.toLowerCase()) {
            case "continue":
                checkoutPage.clickContinue();
                break;
            case "cancel":
                checkoutPage.clickCancel();
                break;
            default:
                log.warn("Unknown button: {}", button);
                break;
        }
    }
}