package org.softkiss.testautomation;

import org.openqa.selenium.OutputType;
import org.softkiss.testautomation.client.ClientFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.IHookCallBack;
import org.testng.ITestResult;
import org.testng.annotations.*;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.io.IOException;
import java.net.MalformedURLException;

@ContextConfiguration(classes = {AppConfig.class})
public class BaseTest extends AbstractTestNGSpringContextTests {

    @BeforeSuite(groups = {"init"}, alwaysRun = true)
    public void beforeSuiteInit() {
    }

    @BeforeClass(groups = {"init"}, alwaysRun = true)
    public void beforeClassInit() throws MalformedURLException {
    }

    @BeforeMethod(groups = {"init"}, alwaysRun = true)
    public void beforeMethodInit() {
    }

    @AfterMethod(groups = {"init"}, alwaysRun = true)
    public void afterMethodStop(ITestResult testResult) throws IOException {
    }

    @AfterClass(groups = {"init"}, alwaysRun = true)
    public void afterClassStop() {
        ClientFactory.getInstance().removeDriver();
    }

    @AfterSuite(groups = {"init"}, alwaysRun = true)
    public void afterSuiteStop() {
    }

    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {
        super.run(callBack, testResult);
        if (testResult.getThrowable() != null) {
            try {
                takeScreenShot(testResult.getMethod().getMethodName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Attachment(value = "Failure in method {0}", type = "image/png")
    private byte[] takeScreenShot(String failureReason) throws IOException {
        logger.info(String.format("Taking screenshot due to fail in method %s", failureReason));
        return ClientFactory.getInstance().getDriver().getScreenshotAs(OutputType.BYTES);
    }

}