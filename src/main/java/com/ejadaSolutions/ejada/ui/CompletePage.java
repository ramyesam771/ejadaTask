package com.ejadaSolutions.ejada.ui;

import com.ejadaSolutions.common.ui.base.BaseWebDriver;
import com.ejadaSolutions.common.ui.uiAutomation.SeleUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;

public class CompletePage extends BaseWebDriver {
    private static final By messageContainer = By.id("checkout_complete_container");
    private final SeleUtils seleUtils = new SeleUtils();

    public void assertMessage(String message) {
        String shownMessage = seleUtils.getText(messageContainer);
        boolean isMessagePresent = shownMessage.contains(message);
        Assertions.assertTrue(isMessagePresent, "The message '" + message + "' was not found in the shown message.");
    }
}
