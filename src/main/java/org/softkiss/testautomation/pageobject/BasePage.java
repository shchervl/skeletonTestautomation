package org.softkiss.testautomation.pageobject;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import org.softkiss.testautomation.client.ClientFactory;
import org.softkiss.testautomation.client.ClientType;
import org.softkiss.testautomation.environment.EnvironmentConfigurator;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.testng.AssertJUnit.assertEquals;

public class BasePage extends HtmlElement {

    protected static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(BasePage.class);
    private RemoteWebDriver webDriver = ClientFactory.getInstance().getDriver();
    private WebDriverWait webDriverWait = new WebDriverWait(this.webDriver, ClientFactory.TIME_WAIT_SECONDS);

    @FindBy(css = "input[type='file']")
    protected WebElement inputFile;
    private Wait<WebDriver> visibilityWait;
    private Wait<WebDriver> invisibilityWait;

    public BasePage() {
        PageFactory.initElements(new HtmlElementDecorator(new HtmlElementLocatorFactory(webDriver)), this);
    }

    public RemoteWebDriver getWebDriver() {
        return this.webDriver;
    }

    public void sendKeys(final WebElement webElement, String text) {
        waitForClickable(webElement);
        if (EnvironmentConfigurator.getInstance().getTestClient().equals(ClientType.IE.toString())) {
            webElement.sendKeys("a");
        }
        webElement.clear();
        webElement.sendKeys(text);
    }

    public void pressEnter(final WebElement webElement) {
        waitForClickable(webElement);
        webElement.sendKeys(Keys.ENTER);
    }

    public WebElement waitForVisibility(WebElement webElement) {
        try {
            webDriverWait.until(ExpectedConditions.visibilityOf(webElement));
        } catch (NoSuchElementException nse) {
            LOGGER.error("", nse);
            return null;
        }
        return webElement;
    }

    protected WebElement waitForVisibility(By locator) {
        try {
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (NoSuchElementException nse) {
            LOGGER.error("", nse);
            return null;
        }
        return getWebDriver().findElement(locator);
    }

    public void waitForInvisibility(final By locator) {
        try {
            webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (NoSuchElementException e) {
            LOGGER.error("", e);
        }
    }

    public WebElement waitForClickable(WebElement webElement) {
        waitForVisibility(webElement);
        try {
            webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
        } catch (NoSuchElementException nse) {
            LOGGER.error("Try to wait little more (wait for clickable)", nse);
        }
        return webElement;
    }

    public boolean click(WebElement webElement) {
        boolean result = false;
        int attempts = 0;
        while (attempts < 3) {
            try {
                scrollToElement(waitForVisibility(webElement));
                waitForClickable(webElement);
                for (int i = 0; i < 10; i++) {
                    try {
                        webElement.click();
                        result = true;
                        break;
                    } catch (Exception e) {
                        pause();
                    }
                }
                break;
            } catch (StaleElementReferenceException ignored) {
                LOGGER.error("", ignored);
            }
            attempts++;
        }
        return result;
    }

    private void pause() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected String getImageNameFromAbsolutePath(File fileToParse) {
        int indexOfSlash;
        if (fileToParse.getAbsolutePath().contains("/"))
            indexOfSlash = fileToParse.getAbsolutePath().lastIndexOf('/');
        else indexOfSlash = fileToParse.getAbsolutePath().lastIndexOf('\\');
        int indexOfDot = fileToParse.getAbsolutePath().lastIndexOf('.');
        return fileToParse.getAbsolutePath().substring(indexOfSlash + 1, indexOfDot);
    }

    protected String getImageNameFromStringPath(String path) {
        int indexOfSlash;
        if (path.contains("/"))
            indexOfSlash = path.lastIndexOf('/');
        else indexOfSlash = path.lastIndexOf('\\');
        int indexOfDot = path.lastIndexOf('.');
        return path.substring(indexOfSlash + 1, indexOfDot);
    }

    public boolean isElementPresent(final WebElement we) {
        webDriver.getPageSource();
        try {
            return we.isDisplayed();
        } catch (Exception e) {
//            LOGGER.error("", e);
            return false;
        }
    }

    protected void selectCustomDropDown(WebElement buttonSelect, List<WebElement> optionsLinks, String textToSelect) {
        click(buttonSelect);
        for (WebElement optionLink : optionsLinks) {
            if (waitForClickable(optionLink).getText().equalsIgnoreCase(textToSelect)) {
                optionLink.click();
                break;
            }
        }
    }

    public RemoteWebDriver switchToNewlyOpenedTab() {
        RemoteWebDriver webDriverNewTab;
        List<String> currentTabs = new ArrayList<>(getWebDriver().getWindowHandles());
        webDriverNewTab = (RemoteWebDriver) getWebDriver().switchTo().window(currentTabs.get(getWebDriver().getWindowHandles().size() - 1));
        webDriverNewTab.manage().window().maximize();
        return webDriverNewTab;
    }

    protected void moveMouseCursorToWebElement(WebElement webElement) {
        waitForClickable(webElement);
        scrollToElement(webElement);
        Actions action = new Actions(getWebDriver());
        action.moveToElement(webElement).perform();
    }

    protected Actions clickAndHoldWebElement(WebElement webElement) {
        waitForClickable(webElement);
        scrollToElement(webElement);
        Actions action = new Actions(getWebDriver());
        action.clickAndHold(webElement).perform();
        return action;
    }

    protected void doubleClickElement(WebElement webElement) {
        scrollToElement(webElement);
        waitForClickable(webElement);
        Actions action = new Actions(getWebDriver());
        action.doubleClick(webElement).perform();
    }

    protected Object executeJS(final String script, final Object... params) {
        return getWebDriver().executeScript(script, params);
    }

    protected void dragAndDropWebElementHTML5(WebElement draggedWebElement, WebElement targetWebElement) {
        new Actions(getWebDriver()).clickAndHold(draggedWebElement).release(targetWebElement).build().perform();
    }

    protected WebElement scrollToElement(WebElement we) {
        executeJS("arguments[0].scrollIntoView(true);", we);
        return we;
    }

    protected String getValueFromElement(WebElement webElement) {
        return executeJS("return arguments[0].value", webElement).toString();
    }

    @Step
    protected void verifyFieldErrorNotifier(WebElement errorNotifierContainer, WebElement errorNotifierFlyOut, String reason) {
        waitForClickable(errorNotifierContainer);
        scrollToElement(errorNotifierContainer);
        moveMouseCursorToWebElement(errorNotifierContainer);
        waitForVisibility(errorNotifierFlyOut);
        assertEquals("Hint Text isn't expected", reason, errorNotifierFlyOut.getText());
    }

    @Step
    public void refreshPage() {
        getWebDriver().navigate().refresh();
    }


    @Step
    public void uploadFile(WebElement webElement, File file) {
        if (file != null) {
            if (EnvironmentConfigurator.getInstance().getTestClient().equals(ClientType.IE.toString())) {
                JavascriptExecutor jsExecutor = getWebDriver();
                jsExecutor.executeScript("arguments[0].setAttribute('style', 'margin: 0px; padding: 0px; width: 1px; height: 1px; position: absolute; opacity: 1;')", webElement);
            }
            webElement.sendKeys(file.getAbsolutePath());
        }
    }


    public boolean isReadOnly(WebElement webElement) {
        waitForVisibility(webElement);
        try {
            String ngReadonly = webElement.getAttribute("ng-readonly");
            return (ngReadonly.equalsIgnoreCase("readonly") || ngReadonly.equalsIgnoreCase("true"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Step
    public void uploadFile(File file) {
        uploadFile(inputFile, file);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return null;
    }
}