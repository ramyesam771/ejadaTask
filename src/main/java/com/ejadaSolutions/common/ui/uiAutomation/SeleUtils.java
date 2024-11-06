package com.ejadaSolutions.common.ui.uiAutomation;

import com.ejadaSolutions.common.ui.base.BaseWebDriver;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * this class used for interacting with webElements,
 * webDriver object is initiated in BaseWebDriver class
 *
 * @author Mahmoud Osama & Mahmoud Osama
 */
public class SeleUtils extends BaseWebDriver {
    JSUtils jsUtils = new JSUtils();
    private static final int defaultTimeout = 10;


    /**
     * get parent element with the given tagName
     *
     * @param elem          Element object
     * @param parentElemTag desired tagName
     * @return parent WebElement object
     */
    public WebElement getParentElem(WebElement elem, String parentElemTag) {
        log.info("getting parent element with tag: {}", parentElemTag);
        String script = "return arguments[0].parentElement";
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        WebElement parent = null;
        do {
            if (parent == null) {
                parent = (WebElement) js.executeScript(script, elem);
            } else {
                parent = (WebElement) js.executeScript(script, parent);
            }
        } while (!parent.getTagName().equalsIgnoreCase(parentElemTag) && !parentElemTag.isEmpty());
        return parent;
    }

    /**
     * get displayed parent element
     *
     * @param elem Element object
     * @return parent WebElement object
     */
    public WebElement getDisplayedParentElem(WebElement elem) {
        WebElement parent = null;
        do {
            parent = parent == null ? getParentElem(elem, "") : getParentElem(parent, "");
        } while (!new JSUtils().isElementDisplayed(parent));
        return parent;
    }

    /**
     * get displayed element which contains given text
     *
     * @param optParentElem
     * @param text          displayed text content
     * @return object WebElement which contains text
     */
    public WebElement getElemWithText(WebElement optParentElem, String text) {
        List<WebElement> t = new JSUtils().getElementsBySelectorFilter(optParentElem, "*", " if(i.innerText && i.innerText.toLowerCase().indexOf('" + text.replaceAll("'", "\\\\'") + "'.toLowerCase())>-1 && i.offsetParent){ return true;}");
        log.info("{} count of elements with text>>{}", t.size(), text);
        if (t.isEmpty()) return null;
        else return t.get(t.size() - 1);
    }


    /**
     * get button element with displayed text
     *
     * @param btnName button's displayed text
     * @return button WebElement object
     */
    public List<WebElement> getButtonsWithText(String btnName) {
        return new JSUtils().getButtonWithText(btnName);
    }
    

    public void robotWriter(Robot robot, String text) {
        log.info("Using Robot to write: {}", text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(text);
        clipboard.setContents(stringSelection, null);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public void clickButtonWithName(String name) {
        List<WebElement> btns = new ArrayList<>();
        try {
            btns = new SeleUtils().getButtonsWithText(name);
        } finally {
            if (btns.isEmpty()) throw new Error("Couldn't get button which contain text : " + name);
        }
        try {
            jsUtils.scrollToElement((By) btns.get(0));
            new WebDriverWait(new BaseWebDriver().getDriver(), Duration.ofSeconds(1)).until(ExpectedConditions.elementToBeClickable(btns.get(0)));
            btns.get(0).click();
        } catch (TimeoutException | ElementClickInterceptedException e) {
            jsUtils.click(btns.get(0));
        }
    }
    


    public void typeTextByLetter(By element, String text, int waitByMillisecond) {
        // find the text field element
        WebElement textField = getDriver().findElement(element);
        // enter text letter by letter
        for (char letter : text.toCharArray()) {
            textField.sendKeys(String.valueOf(letter));
            setImplicitWaitMs(waitByMillisecond);
        }
    }


    /**
     * This method is used to select specific item from drop down list implemented as UL
     */
    public String selectSpecficItemInDDL_ul(By ddl, String text, String toBeSelectedItem) {
        clickOnElement(ddl);
        By toBeSelectedItem_li = By.xpath(toBeSelectedItem + "[text()='" + text + "']");
        String newSelectedValue = getDriver().findElement(toBeSelectedItem_li).getText();
        clickOnElement(toBeSelectedItem_li);
        return newSelectedValue;
    }

    /**
     * This method is used to select specific item from drop down list by write its text
     */
    public void setTextForDDL(By ddl, By toBeSelectedItem_txt, String text) {
        clickOnElement(ddl);
        isElementDisplayed(toBeSelectedItem_txt, 15);
        setTextAndPressEnterKey(toBeSelectedItem_txt, text);
    }

    public void selectFromDropDown(By element, String item) {
        Select select = new Select(getDriver().findElement(element));
        select.selectByVisibleText(item);
    }

    /*
     ****************************************************************************
     ****************************************************************************
     ****************************************************************************
     *                             Wait Methods
     ****************************************************************************
     ****************************************************************************
     ****************************************************************************
     */


    /**
     * Waits until the page's document is fully loaded and ready.
     * This includes waiting for all asynchronous requests to complete and the page to reach a stable state.
     */
    public void waitTillPageReady() {
        log.info("Waiting for the page to be fully loaded and ready.");
        jsUtils.waitForAjax();
    }

    /**
     * Sets the implicit wait timeout for the WebDriver in milliseconds.
     *
     * @param timeoutMillis The timeout duration in milliseconds.
     */
    public void setImplicitWaitMs(int timeoutMillis) {
        log.info("Setting implicit wait timeout to {} milliseconds.", timeoutMillis);
        getDriver().manage().timeouts().implicitlyWait(Duration.ofMillis(timeoutMillis));
    }

    /**
     * Sets the implicit wait timeout for the WebDriver in seconds.
     *
     * @param timeoutSec The timeout duration in seconds.
     */
    public void setImplicitWaitSec(int timeoutSec) {
        log.info("Setting implicit wait timeout to {} seconds.", timeoutSec);
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(timeoutSec));
    }

    /**
     * Sets the implicit wait timeout for the WebDriver in minutes.
     *
     * @param timeoutMin The timeout duration in minutes.
     */
    public void setImplicitWaitMin(int timeoutMin) {
        log.info("Setting implicit wait timeout to {} minutes.", timeoutMin);
        getDriver().manage().timeouts().implicitlyWait(Duration.ofMinutes(timeoutMin));
    }

    /**
     * Sets the implicit wait timeout for the WebDriver in seconds.
     *
     * @param timeoutSec The timeout duration in seconds.
     */
    public WebDriverWait setExplicitWaitSec(int... timeoutSec) {
        WebDriver driver = getDriver();

        if (driver == null) {
            log.error("WebDriver instance is null. Cannot proceed with setting explicit wait.");
            throw new IllegalStateException("WebDriver instance is null.");
        }

        int waitTime = timeoutSec.length > 0 ? timeoutSec[0] : defaultTimeout;

        if (waitTime <= 0) {
            log.info("Explicit wait timeout is set to zero.");
            return new WebDriverWait(driver, Duration.ofSeconds(defaultTimeout));
        } else {
            log.info("Setting explicit wait timeout to {} seconds.", waitTime);
            return new WebDriverWait(driver, Duration.ofSeconds(waitTime));
        }
    }




    /*
     ****************************************************************************
     ****************************************************************************
     ****************************************************************************
     *               Element State Utility Methods
     ****************************************************************************
     ****************************************************************************
     ****************************************************************************
     */

    /**
     * Validates the presence of a web element based on a specified condition.
     *
     * @param condition The ExpectedCondition used to validate the element (e.g., visibility of element).
     */
    public void validateStateOfElement(ExpectedCondition<?> condition, int... timeInSec) {
        int timeout = (timeInSec.length > 0) ? timeInSec[0] : defaultTimeout;
        try {
            log.info("Validating the element under the specified condition with timeout of {} seconds: {}", timeout, condition);
            setExplicitWaitSec(timeout).until(condition);
        } catch (TimeoutException e) {
            log.error("Element condition timed out: {}", condition, e);
            throw new AssertionError("Element condition timed out: " + condition, e);
        } catch (Exception e) {
            log.error("Failed to validate element under the specified condition: {}", condition, e);
            throw new AssertionError("Error occurred while validating element condition: " + condition, e);
        }
    }


    /**
     * Checks whether a web element is clickable or not.
     *
     * @param element   The locator of the element to be checked.
     * @param timeInSec Optional timeout in seconds to wait for the element to become visible. If not provided, uses a default timeout.
     */
    public boolean isElementClickable(Object element, int... timeInSec) {
        if (element == null) {
            throw new NullPointerException("Element must not be null");
        }

        WebDriver driver = getDriver();
        if (driver == null) {
            log.error("WebDriver instance is null. Cannot proceed with checking element click-ability.");
            throw new IllegalStateException("WebDriver instance is null.");
        }

        log.info("Checking if the element: {} is clickable", element);

        ExpectedCondition<WebElement> condition;
        if (element instanceof By locator) {
            condition = ExpectedConditions.elementToBeClickable(locator);
        } else if (element instanceof WebElement webElement) {
            condition = ExpectedConditions.elementToBeClickable(webElement);
        } else {
            throw new AssertionError("Invalid element type provided. Must be By or WebElement.");
        }

        try {
            validateStateOfElement(condition, timeInSec);
            log.info("Element is clickable");
            return true;
        } catch (AssertionError e) {
            log.info("Element is not clickable: {}", e.getMessage());
            return false;
        }
    }


    /**
     * Checks whether a web element is displayed on the page.
     *
     * @param element   The locator of the element to be checked.
     * @param timeInSec Optional timeout in seconds to wait for the element to become visible. If not provided, uses a default timeout.
     */
    public boolean isElementDisplayed(Object element, int... timeInSec) {
        ExpectedCondition<WebElement> condition;

        if (element instanceof By locator) {
            condition = ExpectedConditions.visibilityOfElementLocated(locator);
        } else if (element instanceof WebElement webElement) {
            condition = ExpectedConditions.visibilityOf(webElement);
        } else {
            throw new AssertionError("Invalid element type provided. Must be By or WebElement.");
        }

        try {
            validateStateOfElement(condition, timeInSec);
            log.info("Element is displayed");
            return true;
        } catch (AssertionError e) {
            log.info("Element is not displayed: {}", e.getMessage());
            return false;
        }
    }


    /**
     * This method is used to wait for an element to become invisible.
     *
     * @param element   The locator of the element to be checked.
     * @param timeInSec Optional timeout in seconds to wait for the element to become invisible. If not provided, a default timeout will be used.
     */
    public boolean isElementInvisible(Object element, int... timeInSec) {
        ExpectedCondition<Boolean> condition;

        if (element instanceof By locator) {
            condition = ExpectedConditions.invisibilityOfElementLocated(locator);
        } else if (element instanceof WebElement webElement) {
            condition = ExpectedConditions.invisibilityOf(webElement);
        } else {
            throw new AssertionError("Invalid element type provided. Must be By or WebElement.");
        }

        try {
            validateStateOfElement(condition, timeInSec);
            log.info("Element is invisible");
            return true;
        } catch (AssertionError e) {
            log.info("Element is not invisible: {}", e.getMessage());
            return false;
        }
    }


    /**
     * Checks whether an element is present in the DOM.
     *
     * @param element   The locator of the element to be checked.
     * @param timeInSec Optional timeout in seconds to wait for the element to be present. If not provided, uses a default timeout.
     */
    public boolean isElementPresent(By element, int... timeInSec) {
        ExpectedCondition<WebElement> condition = ExpectedConditions.presenceOfElementLocated(element);
        try {
            validateStateOfElement(condition, timeInSec);
            log.info("Element is presence");
            return true;
        } catch (AssertionError e) {
            log.info("Element is not presence: {}", e.getMessage());
            return false;
        }
    }


    public boolean isAttrContains(Object element, String attribute, String value, int... timeInSec) {
        ExpectedCondition<Boolean> condition;

        if (element instanceof By locator) {
            condition = ExpectedConditions.attributeContains(locator, attribute, value);
        } else if (element instanceof WebElement webElement) {
            condition = ExpectedConditions.attributeContains(webElement, attribute, value);
        } else {
            throw new AssertionError("Invalid element type provided. Must be By or WebElement.");
        }

        try {
            validateStateOfElement(condition, timeInSec);
            log.info("Attribute contains that value");
            return true;
        } catch (AssertionError e) {
            log.info("Attribute does not contain that value: {}", e.getMessage());
            return false;
        }
    }


    /**
     * Checks whether an element containing the specified inner text is displayed.
     *
     * @param text      The inner text content to look for within elements.
     * @param timeInSec Optional timeout value in seconds for waiting for the element.
     */
    public boolean isTextDisplayed(Object element, String text, int... timeInSec) {
        ExpectedCondition<Boolean> condition;

        if (element instanceof By locator) {
            condition = ExpectedConditions.textToBePresentInElementLocated(locator, text);
        } else if (element instanceof WebElement webElement) {
            condition = ExpectedConditions.textToBePresentInElement(webElement, text);
        } else {
            throw new AssertionError("Argument must be either By or WebElement");
        }

        try {
            validateStateOfElement(condition, timeInSec);
            log.info("Text '{}' is displayed in the element.", text);
            return true;
        } catch (AssertionError e) {
            log.info("Text does not displayed: {}", e.getMessage());
            return false;
        }
    }


    /**
     * Waits for the text of an element to change to the specified value.
     *
     * @param element      The locator of the element whose text is being checked.
     * @param expectedText The expected text value.
     * @param timeInSec    Optional timeout in seconds to wait for the text change. If not provided, uses a default timeout.
     * @return True if the text changes to the expected value within the timeout, false otherwise.
     */
    public boolean isTextChange(By element, String expectedText, int... timeInSec) {
        try {
            WebDriverWait wait = setExplicitWaitSec(timeInSec);
            wait.until(driver -> {
                WebElement webElement = driver.findElement(element);
                return webElement.getText().equalsIgnoreCase(expectedText);
            });
            log.info("Text of the element with locator '{}' has changed to '{}'.", element, expectedText);
            return true;
        } catch (TimeoutException e) {
            log.info("Text of the element with locator '{}' did not change to '{}' .", element, expectedText);
            throw new AssertionError("Text of the element did not change");
        } catch (Exception e) {
            log.error("An error occurred while waiting for the text of element with locator '{}' to change: {}", element, e.getMessage());
            throw new AssertionError("An error occurred while waiting for the text of element to change");
        }
    }

    /**
     * Checks whether the page title contains the specified text.
     *
     * @param text      The text content to look for in the page title.
     * @param timeInSec Optional timeout value in seconds for waiting for the page title to contain the text.
     * @return true if the page title contains the specified text, false otherwise.
     */
    public boolean isTitleContains(String text, int... timeInSec) {
        try {
            WebDriverWait wait = setExplicitWaitSec(timeInSec);
            wait.until(driver -> {
                String pageTitle = getDriver().getTitle();
                return pageTitle != null && pageTitle.toLowerCase().contains(text.toLowerCase());
            });
            log.info("Page title contains '{}'.", text);
            return true;
        } catch (TimeoutException e) {
            log.info("Page title does not contain '{}'", text);
            throw new AssertionError("Page title does not contain that text");
        } catch (Exception e) {
            log.error("An error occurred while checking if the page title contains '{}': {}", text, e.getMessage());
            throw new AssertionError("An error occurred while checking if the page title contains that text");
        }
    }



    /*
     ****************************************************************************
     ****************************************************************************
     ****************************************************************************
     *                  Element Interaction Methods
     ****************************************************************************
     ****************************************************************************
     ****************************************************************************
     */


    /**
     * Attempts to click on a web element identified by either a By locator or a WebElement.
     * Validates that the element is clickable before performing the click action.
     * If the element is not directly clickable, it falls back to a JavaScript click.
     *
     * @param element The element to be clicked, either a By locator or a WebElement.
     */
    public void clickOnElement(Object element, int... timeInSec) {
        if (element == null) {
            throw new IllegalArgumentException("Element locator or WebElement cannot be null.");
        }
        try {
            WebElement webElement;
            if (element instanceof By) {
                By locator = (By) element;
                validateStateOfElement(ExpectedConditions.elementToBeClickable(locator), timeInSec);
                webElement = getDriver().findElement(locator);
                log.info("Attempting to click on element with locator: {}", locator);
            } else if (element instanceof WebElement) {
                webElement = (WebElement) element;
                validateStateOfElement(ExpectedConditions.elementToBeClickable(webElement), timeInSec);
                log.info("Attempting to click on WebElement directly.");
            } else {
                throw new IllegalArgumentException("Unsupported element type. Must be By or WebElement.");
            }
            webElement.click();
            log.info("Element has been clicked successfully.");
        } catch (Exception e) {
            log.warn("Standard click failed on element: {}", element, e);
        }
    }


    /**
     * Attempts to click on a web element identified by the given locator.
     * Validates that the element is clickable before performing the click action.
     * If the element is not directly clickable, it falls back to a JavaScript click.
     *
     * @param element The locator (By) of the element to be clicked.
     */
    public void clickOnElementWithoutCheck(By element, int... timeInSec) {
        if (element == null) {
            throw new IllegalArgumentException("Element locator cannot be null.");
        }
        try {
            log.info("Attempting to click on element with locator : {}", element);
            getDriver().findElement(element).click();
            log.info("Element with locator: {} has been clicked successfully. ", element);
        } catch (Exception e) {
            log.warn("Standard click failed on element : {}", element, e);
        }
    }


    /**
     * Sends the specified text to the web element identified by the given locator.
     * Validates that the element is visible before performing the action.
     *
     * @param element The locator (By) of the element where the text will be sent.
     * @param text    The text to be sent to the element.
     */
    public void setText(By element, String text, int... timeInSec) {
        if (element == null) {
            throw new IllegalArgumentException("Element locator cannot be null.");
        }

        try {
            validateStateOfElement(ExpectedConditions.visibilityOfElementLocated(element), timeInSec);
            log.info("Setting text: '{}' to the element with locator: {}", text, element);
            getDriver().findElement(element).sendKeys(text);
            log.info("Text has been successfully sent to the element with locator: {}", element);
        } catch (Exception e) {
            log.warn("Failed to send text to element: {}", element, e);
        }
    }

    public void setTextAndPressEnterKey(By element, String text, int... timeInSec) {
        if (element == null) {
            throw new IllegalArgumentException("Element locator cannot be null.");
        }

        try {
            validateStateOfElement(ExpectedConditions.visibilityOfElementLocated(element), timeInSec);
            log.info("Setting text: '{}' to the element with locator: {} then press enter ", text, element);
            getDriver().findElement(element).sendKeys(text, Keys.ENTER);
            log.info("Text has been successfully sent to the element with locator: {} then enter key pressed", element);
        } catch (Exception e) {
            log.warn("Failed to send text to element then press enter: {}", element, e);
        }
    }


    /**
     * Clears the text from the web element identified by the given locator.
     * Validates that the element is visible before performing the action.
     *
     * @param element The locator (By) of the element from which the text will be cleared.
     */
    public void clearText(By element, int... timeInSec) {
        if (element == null) {
            throw new IllegalArgumentException("Element locator cannot be null.");
        }
        try {
            validateStateOfElement(ExpectedConditions.visibilityOfElementLocated(element), timeInSec);
            log.info("Clearing text from the element with locator: {}", element);
            getDriver().findElement(element).clear();
            log.info("Text has been successfully cleared from the element with locator: {}", element);
        } catch (Exception e) {
            log.warn("Failed to clear text of element: {}", element, e);
        }
    }

    public String getText(By element, int... timeInSec) {
        if (element == null) {
            throw new IllegalArgumentException("Element locator cannot be null.");
        }
        String text = "";
        try {
            validateStateOfElement(ExpectedConditions.visibilityOfElementLocated(element), timeInSec);
            log.info("Getting text of the element with locator: {}", element);
            text = getDriver().findElement(element).getText();
            log.info("Text has been successfully retrieved from the element with locator: {}", element);
        } catch (Exception e) {
            log.warn("Failed to get text from the element: {}", element, e);
        }
        return text;
    }


    /**
     * Gets the value of the specified attribute from the element after waiting for it to be present.
     *
     * @param element       The locator of the element to get the attribute from.
     * @param attributeName The name of the attribute whose value is to be retrieved.
     * @param timeInSec     Optional timeout value in seconds for waiting for the element to be present. If not provided, uses a default timeout.
     * @return The value of the attribute.
     */
    public String getElementAttribute(By element, String attributeName, int... timeInSec) {
        try {
            setExplicitWaitSec(timeInSec).until(ExpectedConditions.presenceOfElementLocated(element));
            return getDriver().findElement(element).getAttribute(attributeName);
        } catch (Exception e) {
            log.error("An error occurred while getting the attribute '{}' from the element with locator '{}': {}", attributeName, element, e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves a list of WebElements based on the provided locator.
     * Ensures the elements are present before returning the list.
     *
     * @param locator The By locator used to find the elements.
     * @param timeInSec Optional parameter specifying the maximum wait time in seconds for elements to be visible.
     * @return A list of WebElements matching the locator; empty if no elements found within the timeout.
     */
    public List<WebElement> getElements(By locator, int... timeInSec) {
        if (locator == null) {
            throw new IllegalArgumentException("Locator cannot be null.");
        }

        List<WebElement> elements = new ArrayList<>();
        try {
            validateStateOfElement(ExpectedConditions.visibilityOfElementLocated(locator), timeInSec);
            elements = getDriver().findElements(locator);
            log.info("Found {} elements for locator: {}", elements.size(), locator);
        } catch (Exception e) {
            log.warn("Failed to retrieve elements for locator: {}", locator, e);
        }

        return elements;
    }


    /*
     ****************************************************************************
     ****************************************************************************
     ****************************************************************************
     *                    iFrame UTILITY METHODS
     ****************************************************************************
     ****************************************************************************
     ****************************************************************************
     */

    /**
     * Switches to an iframe based on the provided identifier, which can be an index (Integer),
     * name/ID (String), or WebElement. It validates the state of the iframe using the
     * validateStateOfElement method with optional timeout.
     *
     * @param iframeIdentifier The identifier of the iframe, can be a WebElement, String (name/ID), or Integer (index).
     * @param timeInSec        Optional timeout parameter, allows overriding default wait time.
     * @return true if successfully switched to the iframe, false otherwise.
     */
    public boolean switchToIframe(Object iframeIdentifier, int... timeInSec) {
        try {
            if (iframeIdentifier instanceof WebElement) {
                validateStateOfElement(ExpectedConditions.frameToBeAvailableAndSwitchToIt((WebElement) iframeIdentifier), timeInSec);
                log.info("Switched to iframe by WebElement");
            } else if (iframeIdentifier instanceof String) {
                validateStateOfElement(ExpectedConditions.frameToBeAvailableAndSwitchToIt((String) iframeIdentifier), timeInSec);
                log.info("Switched to iframe by name or ID: {}", iframeIdentifier);
            } else if (iframeIdentifier instanceof Integer) {
                validateStateOfElement(ExpectedConditions.frameToBeAvailableAndSwitchToIt((Integer) iframeIdentifier), timeInSec);
                log.info("Switched to iframe by index: {}", iframeIdentifier);
            }
            return true;
        } catch (Exception e) {
            log.info("Failed to switch to iframe: {}", e.getMessage());
            return false;
        }
    }


    /**
     * Switches the driver back to the default frame (main content), validates the switch,
     * and logs the action. Asserts that the switch is successful by checking a known element
     * in the default content.
     */
    public void switchToDefaultContent() {
        try {
            getDriver().switchTo().defaultContent();
            log.info("Switched back to default content and validated the switch");
        } catch (Exception e) {
            log.info("Failed to switch back to default content: {}", e.getMessage());
            Assert.fail("Exception occurred while switching back to default content: " + e.getMessage());
        }
    }

    /*
     ****************************************************************************
     ****************************************************************************
     ****************************************************************************
     *                    ALERT HANDLING UTILITY METHODS
     ****************************************************************************
     ****************************************************************************
     ****************************************************************************
     */


    /**
     * Checks if an alert is currently present and fails the test if not.
     *
     * @return true if an alert is present, false otherwise.
     */
    public boolean isAlertPresent() {
        try {
            getDriver().switchTo().alert();
            log.info("Alert is present.");
            return true;
        } catch (NoAlertPresentException e) {
            log.info("No alert present: {}", e.getMessage());
            Assert.fail("No alert was present when one was expected.");
            return false;
        }
    }


    /**
     * Accepts the current alert if it is present.
     * Fails the test if the alert cannot be accepted.
     */
    public void acceptAlert() {
        if (isAlertPresent()) {
            try {
                Alert alert = getDriver().switchTo().alert();
                alert.accept();
                log.info("Alert accepted");
            } catch (Exception e) {
                log.error("Failed to accept alert", e);
                Assert.fail("Failed to accept the alert: " + e.getMessage());
            }
        }
    }


    /**
     * Dismisses the current alert if it is present.
     * Fails the test if the alert cannot be dismissed.
     */
    public void dismissAlert() {
        if (isAlertPresent()) {
            try {
                Alert alert = getDriver().switchTo().alert();
                alert.dismiss();
                log.info("Alert dismissed");
            } catch (Exception e) {
                log.error("Failed to dismiss alert", e);
                Assert.fail("Failed to dismiss the alert: " + e.getMessage());
            }
        }
    }


    /**
     * Retrieves the text of the current alert, asserts that the alert was present,
     * and verifies that the text matches the expected value.
     *
     * @param expectedText The expected text of the alert.
     * @return The text of the alert if present; otherwise, a message indicating the alert is not present.
     */
    public String getAlertText(String expectedText) {
        if (isAlertPresent()) {
            try {
                Alert alert = getDriver().switchTo().alert();
                String alertText = alert.getText();
                log.info("Alert text is: {}", alertText);
                Assert.assertEquals("Alert text does not match the expected value.", expectedText, alertText);
                return alertText;
            } catch (Exception e) {
                log.error("Failed to retrieve or verify the alert text", e);
                Assert.fail("Exception occurred while retrieving the alert text.");
            }
        }
        log.warn("No alert present to retrieve text.");
        return "No alert present";
    }



    /**
     * Sends the specified text to the alert prompt after triggering it through the specified element,
     * and asserts that the alert was present before sending the text.
     *
     * @param element The locator of the element that triggers the alert.
     * @param text    The text to send to the alert.
     */
    public void sendKeysToAlert(By element, String text) {
        if (isAlertPresent()) {
            try {
                getDriver().findElement(element).click();
                Alert alert = getDriver().switchTo().alert();
                alert.sendKeys(text);
                log.info("Sent text to alert: {}", text);
                Assert.assertTrue("Failed to send text to alert, alert was not present", isAlertPresent());
            } catch (NoAlertPresentException e) {
                log.error("No alert present to send text", e);
                Assert.fail("No alert present to send text");
            }
        }
    }




    public void selectRandomValueByIndex(By locator) {
        WebElement dropdownElement = getDriver().findElement(locator);
        Select objSelect = new Select(dropdownElement);
        List<WebElement> options = objSelect.getOptions();
        if (options.size() <= 1) {
            log.info("Not enough options to select.");
            return;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(options.size() - 1) + 1;
        objSelect.selectByIndex(randomIndex);
    }
}


