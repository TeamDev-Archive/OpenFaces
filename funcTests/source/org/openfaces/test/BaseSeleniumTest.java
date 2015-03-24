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

package org.openfaces.test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;
import org.apache.commons.lang.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.openfaces.util.PropertyTestConfiguration;
import org.openfaces.util.ScreenShooter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.SeleniumFactory;
import org.seleniuminspector.SeleniumHolder;
import org.seleniuminspector.SeleniumTestCase;
import org.seleniuminspector.openfaces.*;
import org.seleniuminspector.webriver.WebDriverBasedSelenium;
import org.seleniuminspector.webriver.WebDriverBasedSeleniumFactory;

import java.sql.Driver;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.openfaces.test.BaseSeleniumTest.BrowserType.CHROME;
import static org.openfaces.test.BaseSeleniumTest.BrowserType.IEXPLORE;
import static org.openfaces.test.BaseSeleniumTest.BrowserType.OPERA;
import static org.openfaces.test.BaseSeleniumTest.TestResultLevel.COMPLETE;
import static org.openfaces.test.BaseSeleniumTest.TestResultLevel.FAIL;

/**
 * @author Max Yurin
 */
public class BaseSeleniumTest extends SeleniumTestCase {
    public static final String FUNC_TESTS_PROPERTIES_FILE = "/funcTests.properties";
    public static final String DEMO_CONTEXT_PATH = "demo.context.path";
    public static final String TEST_APP_CONTEXT_PATH = "test.app.context.path";
    public static final String TEST_APP_IS_FACELETS = "test.app.is.facelets";

    private static PropertyTestConfiguration properties;
    private static TestResultLevel testResultLevel = FAIL;
    private static ScreenShooter screenShooter;
    private static WebDriver webDriver;
    private static String testAppUrlPrefix;
    private static String demoUrlPrefix;
    private static Boolean isFacelets;

    public WebDriver getWebDriver() {
        return webDriver;
    }

    protected void assertTestPageAvailable(String pageUrl, String expectedPageTitle) {
        super.assertPageAvailable(testAppUrlPrefix + pageUrl, expectedPageTitle);
    }

    protected void assertDemoPageAvailable(String pageUrl, String expectedPageTitle) {
        super.assertPageAvailable(demoUrlPrefix + pageUrl, expectedPageTitle);
    }

    private static class ExecutionListener extends RunListener {
        @Override
        public void testFinished(Description description) throws Exception {
            super.testFinished(description);
            makeScreenShoot(description.getMethodName());
        }

        @Override
        public void testFailure(Failure failure) throws Exception {
            super.testFailure(failure);
            makeScreenShoot(failure.getDescription().getMethodName());
        }

        @Override
        public void testIgnored(Description description) throws Exception {
            super.testIgnored(description);
        }
    }

    public enum TestResultLevel {
        COMPLETE("complete"),
        FAIL("fail"),
        IGNORE("ignore");

        TestResultLevel(String value) {
            this.value = value;
        }

        private final String value;

        public String getValue() {
            return value;
        }

        public static TestResultLevel getTestResult(String value) {
            if (StringUtils.isNotBlank(value)) {
                for (TestResultLevel testResultLevel : TestResultLevel.values()) {
                    if (testResultLevel.getValue().equals(value)) {
                        return testResultLevel;
                    }
                }
            }
            return IGNORE;
        }
    }

    public static enum BrowserType {
        FIREFOX,
        IEXPLORE,
        CHROME,
        SAFARI,
        OPERA;

        public static BrowserType findType(String value) {
            for (BrowserType browserType : BrowserType.values()) {
                final String type = browserType.name().toLowerCase();
                if (type.equals(value)) {
                    return browserType;
                }
            }
            throw new IllegalArgumentException("Incompatible Browser type: " + value);
        }
    }

    @BeforeClass
    public static void init() {
        properties = new PropertyTestConfiguration(FUNC_TESTS_PROPERTIES_FILE);

        createDefaultSeleniumInstance();

        screenShooter = new ScreenShooter(webDriver);
        isFacelets = isTestAppFacelets();
        testAppUrlPrefix = getTestAppUrlPrefix(TEST_APP_CONTEXT_PATH);
        demoUrlPrefix = getDemoUrlPrefix(DEMO_CONTEXT_PATH);

        final JUnitCore runner = new JUnitCore();
        runner.addListener(new ExecutionListener());
    }

    private static void createDefaultSeleniumInstance() {
//        selenium = new WebDriverBasedSelenium(webDriver, properties.getDefaultUrl());

        SeleniumFactory seleniumFactory = new WebDriverBasedSeleniumFactory(
                properties.getDefaultHost(),
                properties.getCustomSeleniumPort(),
                getBrowserPath(),
                properties.getDefaultUrl());

        SeleniumHolder.getInstance().setSeleniumFactory(seleniumFactory);
        webDriver = ((WrapsDriver) getSelenium()).getWrappedDriver();
    }

    private static String getBrowserPath() {
        final String defaultBrowser = properties.getDefaultBrowser();

        if (StringUtils.isNotBlank(defaultBrowser)) {
            if (defaultBrowser.equals(CHROME.name().toLowerCase())) {
                return properties.getChromePath();
            } else if (defaultBrowser.equals(IEXPLORE.name().toLowerCase())) {
                return properties.getIePath();
            } else if (defaultBrowser.equals(OPERA.name().toLowerCase())) {
                return properties.getOperaPath();
            }
        }

        return properties.getFirefoxPath();
    }

    @AfterClass
    public static void detach() {
        resetSelenium();
    }

    private static void resetSelenium() {
        ((WrapsDriver) getSelenium()).getWrappedDriver().quit();
        SeleniumHolder.getInstance().resetSelenium();
    }

    public static Boolean isFacelets() {
        return isFacelets != null && isFacelets;
    }

    public static void makeScreenShoot(String methodName) {
        if (testResultLevel != null && StringUtils.isNotBlank(methodName) &&
                (FAIL.equals(testResultLevel) || COMPLETE.equals(testResultLevel))) {

            final SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmss");
            final String prefix = testResultLevel == FAIL ? properties.getFailTestsFolder() : properties.getCompleteTestsFolder();
            final String fileName = prefix + methodName + "_screenShoot_" + dateFormat.format(new Date()) + ".png";

            screenShooter.makeScreenShot(fileName);
        }
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
                    + ", Current page source: " + getWebDriver().getPageSource(), e);
        }

        boolean htmlSourceValid = htmlSource.contains(getUtilJsUrlSubstring());
        if (!htmlSourceValid) {
            fail("Unexpected page content. Page url: " + fullPageUrl + " ; Expected (but missing) HTML " +
                    "source substring: " + getUtilJsUrlSubstring() + "; Current page title: " +
                    getWebDriver().getTitle() + " Current page source: " + getWebDriver().getPageSource());
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
                ElementInspector.provideUtils(getWebDriver());
            } catch (Exception e) {
                if (i < maxAttemptCount - 1) {
                    sleep(10000);
                    continue;
                } else {
                    throw new RuntimeException("Couldn`t open the page: " + pageUrl
                            + ", Current page title: " + getWebDriver().getTitle()
                            + ", Current page source: " + getWebDriver().getPageSource(), e);
                }
            }

            if (assertPageContentValid(properties.getDefaultUrl(), pageUrl)) {
                break;
            }
        }
    }

    public void openAndWait(String url) {
        try {
            getWebDriver().get(properties.getDefaultUrl() + url);
            ElementInspector.provideUtils(getWebDriver());
        } catch (Exception e) {
            resetSelenium();
            getWebDriver().get(properties.getDefaultUrl() + url);
            ElementInspector.provideUtils(getWebDriver());
        }
    }

    private static String getTestAppUrlPrefix(String systemProperty) {
        String systemPropertyValue = System.getProperty(systemProperty);
        final String value = isTestAppFacelets() ? properties.getTestAppFaceletsURLPrefix() : properties.getTestAppJspURLPrefix();

        return StringUtils.isNotBlank(systemPropertyValue) ? systemPropertyValue : value;
    }

    private static String getDemoUrlPrefix(String systemProperty) {
        String systemPropertyValue = System.getProperty(systemProperty);
        final String value = isTestAppFacelets() ? properties.getLiveDemoFaceletsURLPrefix() : properties.getLiveDemoJspURLPrefix();

        return StringUtils.isNotBlank(systemPropertyValue) ? systemPropertyValue : value;
    }

    private static boolean isTestAppFacelets() {
        if (isFacelets == null) {
            final String property = System.getProperty(TEST_APP_IS_FACELETS);
            return !StringUtils.isNotBlank(property) || Boolean.parseBoolean(property);
        }
        return isFacelets;
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


    protected DataTableInspector dataTable(String locator) {
        return new DataTableInspector(locator);
    }

    protected TabSetInspector tabSet(String locator) {
        return new TabSetInspector(locator);
    }

    protected TabbedPaneInspector tabbedPane(String locator) {
        return new TabbedPaneInspector(locator);
    }

    protected FoldingPanelInspector foldingPanel(String locator) {
        return new FoldingPanelInspector(locator);
    }

    protected TreeTableInspector treeTable(String locator) {
        return new TreeTableInspector(locator);
    }

    protected DropDownFieldInspector dropDownField(String locator) {
        return new DropDownFieldInspector(locator);
    }

    protected SuggestionFieldInspector suggestionField(String locator) {
        return new SuggestionFieldInspector(locator);
    }

    protected InputTextInspector inputText(String locator) {
        return new InputTextInspector(locator);
    }

    protected PopupLayerInspector popupLayer(String locator) {
        return new PopupLayerInspector(locator);
    }

    protected ConfirmationInspector confirmation(String locator) {
        return new ConfirmationInspector(locator);
    }

    protected DateChooserInspector dateChooser(String locator) {
        return new DateChooserInspector(locator);
    }

    protected CalendarInspector calendar(String locator) {
        return new CalendarInspector(locator);
    }

    protected TwoListSelectionInspector twoListSelection(String locator) {
        return new TwoListSelectionInspector(locator);
    }

    protected HintLabelInspector hintLabel(String locator) {
        return new HintLabelInspector(locator);
    }

    protected BorderLayoutPanelInspector borderLayoutPanel(String locator) {
        return new BorderLayoutPanelInspector(locator);
    }

    protected SidePanelInspector sidePanel(String locator) {
        return new SidePanelInspector(locator);
    }

    protected DataTablePaginatorInspector dataTablePaginator(String locator) {
        return new DataTablePaginatorInspector(locator);
    }

    protected ForEachInspector forEach(String locator) {
        return new ForEachInspector(locator);
    }
}