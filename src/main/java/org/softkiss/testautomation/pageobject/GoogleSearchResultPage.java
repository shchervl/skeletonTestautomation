package org.softkiss.testautomation.pageobject;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by v.shcherbanyuk on 1/21/2016.
 */
public class GoogleSearchResultPage extends GooglePage {

    @FindAll(@FindBy(css = "div.rc"))
    private List<WebElement> searchresultBlocks;

    @FindBy(css = "#footcnt")
    private WebElement pageFooter;

    public GoogleSearchResultPage() {
        super();
        waitForClickable(pageFooter);
    }

    public GoogleSearchResultPage verifyNumberOfResultItemsPerPage() {
        assertEquals(9, searchresultBlocks.size());
        return this;
    }
}
