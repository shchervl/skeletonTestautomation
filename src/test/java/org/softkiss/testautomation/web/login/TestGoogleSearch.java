package org.softkiss.testautomation.web.login;

import org.softkiss.testautomation.BaseTest;
import org.softkiss.testautomation.pageobject.GooglePage;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;

@Description("This is an example test suite")
public class TestGoogleSearch extends BaseTest {

    private GooglePage googlePage;

    @Test
    public void testTextSearch() {
        googlePage = new GooglePage();
        googlePage
                .openPage()
                .searchText("hotel")
                .verifyNumberOfResultItemsPerPage();
    }
}



