package com.ejadaSolutions.ejada.ui;

import com.ejadaSolutions.common.ui.base.BaseWebDriver;
import com.ejadaSolutions.common.ui.uiAutomation.SeleUtils;
import org.openqa.selenium.By;

public class ProductsPage extends BaseWebDriver {
    private static final By firstProduct = By.id("add-to-cart-sauce-labs-fleece-jacket");
    private static final By secondProduct = By.id("add-to-cart-sauce-labs-backpack");
    private static final By cartIcon = By.xpath("//a[@class='shopping_cart_link']");
    private final SeleUtils seleUtils = new SeleUtils();

    public void clickFirstProduct() {
        seleUtils.clickOnElement(firstProduct);
    }

    public void clickSecondProduct() {
        seleUtils.clickOnElement(secondProduct);
    }

    public void clickCartIcon() {
        seleUtils.clickOnElement(cartIcon);
    }

}
