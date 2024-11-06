package com.ejadaSolutions.ejada.ui;

import com.ejadaSolutions.common.ui.base.BaseWebDriver;
import com.ejadaSolutions.common.ui.uiAutomation.SeleUtils;
import org.openqa.selenium.By;
import com.ejadaSolutions.common.utils.generator.Generator;

public class CheckoutPage extends BaseWebDriver {
    private static final By firstNameBox = By.id("first-name");
    private static final By lastNameBox = By.id("last-name");
    private static final By postalCodeBox = By.id("postal-code");
    private static final By continueBtn = By.id("continue");
    private static final By cancelBtn = By.id("cancel");
    private final SeleUtils seleUtils = new SeleUtils();


    public void setFirstName() {
        seleUtils.setText(firstNameBox, Generator.generateRandomName());
    }

    public void setlastName() {
        seleUtils.setText(lastNameBox, Generator.generateRandomName());
    }

    public void setPostalCode() {
        seleUtils.setText(postalCodeBox, Generator.generateRandomNumbers());
    }

    public void clickContinue() {
        seleUtils.clickOnElement(continueBtn);
    }

    public void clickCancel() {
        seleUtils.clickOnElement(cancelBtn);
    }
}
