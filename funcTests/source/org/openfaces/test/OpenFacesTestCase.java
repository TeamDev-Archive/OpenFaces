/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.test;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seleniuminspector.SeleniumFactory;
import org.seleniuminspector.SeleniumHolder;
import org.seleniuminspector.SeleniumTestCase;
import org.seleniuminspector.openfaces.*;
import org.seleniuminspector.webriver.MySeleniumFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class OpenFacesTestCase extends SeleniumTestCase {

    private static final int CUSTOM_SELENIUM_PORT = 14444;
    private static BrowserType browserType = BrowserType.FIREFOX;

    /* Configuration */
    protected static final boolean IS_FACELETS = getBooleanSystemProperty("test.app.is.facelets", true);
    // SUN12 - Sun Reference Implementation 1.2
    // SUN11 - Sun Reference Implementation 1.1
    protected static final String IMPLEMENTATION = getSystemProperty("test.app.jsf.implementation", "SUN12");

    protected static final String TEST_APP_URL_PREFIX = getSystemProperty("test.app.context.path", IS_FACELETS ? "/TestAppFacelets" : "/TestAppJsp");
//    protected static final String LIVE_DEMO_URL_PREFIX = getSystemProperty("demo.context.path", IS_FACELETS ? "/LiveDemoFacelets" : "/LiveDemoJsp");
    protected static final String LIVE_DEMO_URL_PREFIX = "";

    static {
        Properties properties = new Properties();
        InputStream resourceAsStream = OpenFacesTestCase.class.getResourceAsStream("/funcTests.properties");
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException("Can't obtain Selenium properties", e);
        }
        String startUrl = properties.getProperty("org.openfaces.funcTests.startUrl");

        boolean addNamespacesToXpath = OpenFacesTestCase.IMPLEMENTATION.equals("SUN12") && OpenFacesTestCase.IS_FACELETS;
        String browserPath = browserType.getBrowserPath(properties);
        SeleniumFactory seleniumFactory = new MySeleniumFactory("localhost", CUSTOM_SELENIUM_PORT, browserPath, startUrl);
        SeleniumHolder.getInstance().setSeleniumFactory(seleniumFactory);
    }

    private static String getSystemProperty(String propertyName, String defaultValue) {
        String systemPropertyValue = System.getProperty(propertyName);
        return (systemPropertyValue != null) ? systemPropertyValue : defaultValue;
    }

    private static boolean getBooleanSystemProperty(String propertyName, boolean defaultValue) {
        String systemPropertyValue = System.getProperty(propertyName);
        if (systemPropertyValue != null) {
            return Boolean.parseBoolean(systemPropertyValue);
        }
        return defaultValue;
    }


    public static BrowserType getBrowserType() {
        return browserType;
    }

    public static enum BrowserType {
        FIREFOX,
        EXPLORER,
        CHROME,
        SAFARI,
        OPERA;

        public String getBrowserPath(Properties properties) {
            return properties.getProperty("org.openfaces.funcTests." + toString().toLowerCase() + ".path");
        }
    }

    private void testAppPage(String testAppPageUrl, String htmlSubstringOfAValidPage) {
        open(TEST_APP_URL_PREFIX, testAppPageUrl, htmlSubstringOfAValidPage, 5);
    }

    @Override
    protected void assertPageAvailable(String pageUrl, String expectedPageTitle) {
        Selenium selenium = getSelenium();
        open("", pageUrl, getUtilJsUrlSubstring(), 5);
        assertEquals("Couldn't open page (unexpected page title): " + pageUrl, expectedPageTitle, selenium.getTitle());
    }

    private void open(String applicationUrl, String pageUrl, String htmlSubstringOfAValidPage, int attemptCount) {
        for (int i = 1; i <= attemptCount; i++) {
            boolean lastAttempt = (i == attemptCount);
            try {
                openAndWait(applicationUrl, pageUrl);
                JavascriptExecutor js = (JavascriptExecutor) getDriver();
                js.executeScript("var script = document.createElement('script'); script.setAttribute('src', '../asd.js');script.setAttribute('type', 'text/javascript'); document.body.insertBefore(script, document.body.childNodes[0]);");

//                WebDriverWait wait = new WebDriverWait(getDriver(), 5);
//                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("seleniumLoaderFlag")));
            } catch (Exception e) {
                if (!lastAttempt) {
                    sleep(10 * 1000);
                    continue;
                }

                throw new RuntimeException(e);
            }


            if (assertPageContentValid(applicationUrl, pageUrl, htmlSubstringOfAValidPage, lastAttempt))
                break;
            sleep(10 * 1000);
        }
    }

    private boolean assertPageContentValid(
            String applicationUrl,
            String pageUrl,
            String htmlSubstringOfAValidPage,
            boolean failIfNotLoaded) {
        String fullPageUrl = applicationUrl + pageUrl;
        if (htmlSubstringOfAValidPage == null)
            return true;

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
            try {
                String alert = selenium.getAlert();
                if (failIfNotLoaded)
                    throw new RuntimeException("Couldn't open the page (failed getting HTML source of a page). " +
                            "Alert dialog has popped up: " + alert + "; page URL: " + fullPageUrl +
                            "; Page title: " + pageTitle, e);
                else
                    return false;
            } catch (Exception ex) {
                // an absence of alert is a normal case
            }

            if (failIfNotLoaded)
                throw new RuntimeException("Couldn't open the page (failed getting HTML source of a page): " + fullPageUrl + "; Page title: " + pageTitle, e);
            else
                return false;
        }

        boolean htmlSourceValid = htmlSource.contains(htmlSubstringOfAValidPage);
        if (!htmlSourceValid) {
            if (failIfNotLoaded)
                fail("Unexpected page content. Page url: " + fullPageUrl + " ; Expected (but missing) HTML " +
                        "source substring: " + htmlSubstringOfAValidPage + "; Current page title: " +
                        selenium.getTitle());
            else
                return false;
        }

        return true;
    }

    protected void testAppFunctionalPage(String testAppPageUrl) {
        testAppPage(testAppPageUrl, getUtilJsUrlSubstring());
    }

    private String getUtilJsUrlSubstring() {
        return "META-INF/resources/openfaces/util/util-"; // OpenFaces 2.x resource sub-string for util.js
    }

    protected void testAppFunctionalPage(String testAppPageUrl, String htmlSubstringOfAValidPage) {
        testAppPage(testAppPageUrl, htmlSubstringOfAValidPage);
    }

    protected void liveDemoPage(String testAppPageUrl) {
        liveDemoPage(testAppPageUrl, getUtilJsUrlSubstring());
    }

    protected void liveDemoPage(String testAppPageUrl, String htmlSubstringOfValidPage) {
        open(LIVE_DEMO_URL_PREFIX, testAppPageUrl, htmlSubstringOfValidPage, 5);
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

    protected void assertAppearanceNotChanged(String screenshotName) {
        // todo: add actual screenshot checking code
    }

    protected void assertAppearanceNotChanged(String screenshotName, String elementId) {
        // todo: add actual screenshot checking code
    }

    protected void assertPageContainsErrorIcon(boolean shouldContainIcon) {
        boolean iconExists = getSelenium().isElementPresent("//img[contains(@src,'openFacesResources/META-INF/resources/openfaces/validation/error_icon')]");
        assertEquals(shouldContainIcon, iconExists);
    }

    protected WebDriver getDriver() {
        return ((WrapsDriver) getSelenium()).getWrappedDriver();
    }
}