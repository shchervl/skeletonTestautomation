package org.softkiss.testautomation.pageobject;

/**
 * Created by v.shcherbanyuk on 1/21/2016.
 */

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.softkiss.testautomation.client.ClientFactory;
import org.softkiss.testautomation.environment.EnvironmentConfigurator;

/**
 * Created by v.shcherbanyuk on 9/4/2015.
 */

public class GooglePage extends BasePage {

    @FindBy(css = "input#lst-ib")
    private WebElement inputSearchField;

    @FindBy(css = "button.lsb span.sbico")
    private WebElement buttonSearch;


    public GooglePage() {
        super();
    }

    public GooglePage openPage() {
        ClientFactory.getInstance().getDriver().get(EnvironmentConfigurator.getInstance().getAppUrl());
        return this;
    }

    public GoogleSearchResultPage searchText(String text) {
        sendKeys(inputSearchField, text);
        click(buttonSearch);
        return new GoogleSearchResultPage();
    }

}
