/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2015, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.tests.common;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;
import org.apache.commons.lang.StringUtils;
import org.inspector.SeleniumHolder;
import org.inspector.navigator.URLPageNavigator;
import org.inspector.webriver.PropertyTestConfiguration;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.util.concurrent.TimeUnit;

/**
 * @author Max Yurin
 */
public class BaseSeleniumTest extends Assert {
    public static final String DEMO_CONTEXT_PATH = "demo.context.path";
    public static final String TEST_APP_CONTEXT_PATH = "test.app.context.path";
    public static final String TEST_APP_IS_FACELETS = "test.app.is.facelets";

    protected static String testAppUrlPrefix;
    protected static String demoUrlPrefix;
    protected static Boolean isFacelets;

    protected URLPageNavigator urlPageNavigator;

    private static PropertyTestConfiguration properties;

    static {
        properties = new PropertyTestConfiguration();
    }

    public static PropertyTestConfiguration getProperties() {
        return properties;
    }

    private static String getTestAppUrlPrefix() {
        return properties.getTestAppFaceletsURLPrefix();
    }

    private static String getDemoUrlPrefix() {
        return properties.getLiveDemoFaceletsURLPrefix();
    }

    public static boolean isTestAppFacelets() {
        if (isFacelets == null) {
            final String property = System.getProperty(TEST_APP_IS_FACELETS);
            return !StringUtils.isNotBlank(property) || Boolean.parseBoolean(property);
        }
        return isFacelets;
    }

    public static Boolean isFacelets() {
        return isFacelets != null && isFacelets;
    }

    @BeforeClass
    @Parameters({"browser", "version", "platform"})
    public void setUp(String browser, String version, String platform) throws Exception {
        SeleniumHolder.createNewDriverProvider(properties, browser, version, platform);

        isFacelets = isTestAppFacelets();
        testAppUrlPrefix = getTestAppUrlPrefix();
        demoUrlPrefix = getDemoUrlPrefix();

        urlPageNavigator = new URLPageNavigator(getDriver(), properties.getDefaultUrl() + testAppUrlPrefix);
    }

    @AfterMethod
    public void tearDown() {
        resetSelenium();
    }

    public void resetSelenium() {
        SeleniumHolder.getInstance().resetSelenium();
    }

    public void sleep(int milliseconds) {
        getDriver().manage().timeouts().implicitlyWait(milliseconds, TimeUnit.MILLISECONDS);
    }

    public WebDriver getDriver() {
        return SeleniumHolder.getInstance().getDriver();
    }

    @SuppressWarnings("unchecked")
    public Selenium getSelenium() {
        return SeleniumHolder.getInstance().getSelenium();
    }

    protected void assertTestPageAvailable(String pageUrl, String expectedPageTitle) {
        openPage(testAppUrlPrefix + pageUrl);
    }

    protected void assertDemoPageAvailable(String pageUrl, String expectedPageTitle) {
        openPage(demoUrlPrefix + pageUrl);
    }

    private boolean assertPageContentValid(String applicationUrl, String pageUrl) {
        String fullPageUrl = applicationUrl + pageUrl;

        Selenium selenium = getSelenium();
        String htmlSource;

        try {
            htmlSource = selenium.getHtmlSource();
        } catch (SeleniumException e) {
            String pageTitle;
            try {
                pageTitle = selenium.getTitle();
            } catch (Exception ex) {
                pageTitle = "<exception on selenium.getTitle(): " + ex.getMessage() + ">";
            }

            throw new RuntimeException("Couldn`t open the page: " + pageUrl
                    + ", Current page title: " + pageTitle
                    + ", Current page source: " + getDriver().getPageSource(), e);
        }

        boolean htmlSourceValid = htmlSource.contains(getUtilJsUrlSubstring());
        if (!htmlSourceValid) {
            fail("Unexpected page content. Page url: " + fullPageUrl + " ; Expected (but missing) HTML " +
                    "source substring: " + getUtilJsUrlSubstring() + "; Current page title: " +
                    getDriver().getTitle() + " Current page source: " + getDriver().getPageSource());
        }

        return true;
    }

    protected void assertPageContainsErrorIcon(boolean shouldContainIcon) {
        boolean iconExists = getSelenium().isElementPresent("//img[contains(@src,'javax.faces.resource/validation/error_icon')]");
        assertEquals(shouldContainIcon, iconExists);
    }

    protected void assertAppearanceNotChanged(String value) {
        //TODO:(Here may be you assert Not Changed handler)
    }

    protected void assertAppearanceNotChanged(String value1, String value2) {
        //TODO:(Here may be you assert Not Changed handler)
    }

    public void openPage(String pageUrl) {
        final int maxAttemptCount = 5;

        for (int i = 0; i < maxAttemptCount; i++) {
            try {
                openAndWait(pageUrl);
                sleep(1000);
            } catch (Exception e) {
                if (i < maxAttemptCount - 1) {
                    sleep(10000);
                    continue;
                } else {
                    throw new RuntimeException("Couldn`t open the page: " + pageUrl
                            + ", Current page title: " + getDriver().getTitle()
                            + ", Current page source: " + getDriver().getPageSource(), e);
                }
            }

            if (assertPageContentValid(properties.getDefaultUrl(), pageUrl)) {
                break;
            }
        }
    }

    public void openAndWait(String url) {
        try {
            getDriver().get(properties.getDefaultUrl() + url);
//            ElementInspector.provideUtils(getWebDriver());
        } catch (Exception e) {
            resetSelenium();
            getDriver().get(properties.getDefaultUrl() + url);
//            ElementInspector.provideUtils(getWebDriver());
        }
    }

    private String getUtilJsUrlSubstring() {
        return "javax.faces.resource/util/util.js.jsf?ln=openfaces"; // OpenFaces 3.x resource sub-string for util.js
    }

    protected void testAppFunctionalPage(String pageUrl) {
        openPage(testAppUrlPrefix + pageUrl);
    }

    private void testAppPage(String testAppPageUrl, String htmlSubstringOfAValidPage) {
        openPage(testAppUrlPrefix + testAppPageUrl);
    }

    protected void liveDemoPage(String testAppPageUrl) {
        liveDemoPage(testAppPageUrl, getUtilJsUrlSubstring());
    }

    protected void liveDemoPage(String testAppPageUrl, String htmlSubstringOfValidPage) {
        openPage(demoUrlPrefix + testAppPageUrl);
    }

    public void waitForPageToLoad() {
        sleep(500); // wait a little while Ajax request starts asynchronously
        Selenium selenium = getSelenium();
        selenium.waitForCondition("var value = window.document._ajaxInProgressMessage ? window.document._ajaxInProgressMessage.style.display : 'none'; value == 'none';", "30000");
        sleep(2000);
    }

    public boolean isMessageTextPresent(String text) {
        return getSelenium().isElementPresent("//span[contains(text(),'" + text + "')]") ||
                getSelenium().isElementPresent("//li[contains(text(),'" + text + "')]");
    }

    public void assertConversionErrorOccured(boolean errorExpected) {
        assertEquals(errorExpected, isMessageTextPresent("Conversion error") || isMessageTextPresent("Conversion Error") || isMessageTextPresent("could not be understood as a date"));
    }

    public void closeBrowser() {
        getDriver().close();
    }

    public boolean isAlertPresent() {
        try {
            getDriver().switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public String getAlert() {
        return getDriver().switchTo().alert().getText();
    }

    public void assertEquals() {

    }

    public void acceptAlert() {
        getDriver().switchTo().alert().accept();
    }

    public void dismissAlert() {
        getDriver().switchTo().alert().dismiss();
    }

    protected void assertNoAlert(String messagePrefix) {
        Selenium selenium = getSelenium();
        if (selenium.isAlertPresent()) {
            fail(messagePrefix + " " + selenium.getAlert());
        }
    }
    protected enum EventType {
        KEY("KeyEvents"), MOUSE("MouseEvents");

        private String value;

        private EventType(String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }
    }
}