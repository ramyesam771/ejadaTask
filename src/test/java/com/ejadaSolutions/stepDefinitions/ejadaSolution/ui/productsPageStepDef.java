package com.ejadaSolutions.stepDefinitions.ejadaSolution.ui;

import com.ejadaSolutions.common.ui.base.BaseWebDriver;
import com.ejadaSolutions.ejada.ui.ProductsPage;
import io.cucumber.java.en.Then;

public class productsPageStepDef extends BaseWebDriver {
    private final ProductsPage productsPage  = new ProductsPage();


    @Then("user clicks on {string} icon")
    @Then("user adds {string} to the cart")
    public void addProductsCart(String product) {
        switch (product.toLowerCase()) {
            case "fleece jacket":
                productsPage.clickFirstProduct();
                break;
            case "backpack":
                productsPage.clickSecondProduct();
                break;
            case "cart":
                productsPage.clickCartIcon();
                break;
            default:
                log.warn("Unknown product: {}", product);
                break;
        }
    }



}