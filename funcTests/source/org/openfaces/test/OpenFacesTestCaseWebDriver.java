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

import org.junit.After;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Vladislav Lubenskiy
 */
public class OpenFacesTestCaseWebDriver extends org.junit.Assert {
    private WebDriver webDriver;

    /* Configuration */
    protected static final boolean IS_FACELETS = getBooleanSystemProperty("test.app.is.facelets", true);
    // SUN12 - Sun Reference Implementation 1.2
    // SUN11 - Sun Reference Implementation 1.1

    protected static final String TEST_APP_URL_PREFIX = getSystemProperty("test.app.context.path", IS_FACELETS ? "/TestAppFacelets" : "/TestAppJsp");
    protected static final String LIVE_DEMO_URL_PREFIX = getSystemProperty("demo.context.path", IS_FACELETS ? "/LiveDemoFacelets" : "/LiveDemoJsp");

    static {
        Properties properties = new Properties();
        InputStream resourceAsStream = OpenFacesTestCase.class.getResourceAsStream("/funcTests.properties");
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException("Can't obtain Selenium properties", e);
        }
    }

    private static boolean getBooleanSystemProperty(String propertyName, boolean defaultValue) {
        String systemPropertyValue = System.getProperty(propertyName);
        if (systemPropertyValue != null) {
            return Boolean.parseBoolean(systemPropertyValue);
        }
        return defaultValue;
    }

    private static String getSystemProperty(String propertyName, String defaultValue) {
        String systemPropertyValue = System.getProperty(propertyName);
        return (systemPropertyValue != null) ? systemPropertyValue : defaultValue;
    }

    public WebDriver getDriver() {
        if (webDriver == null) {
            webDriver = new FirefoxDriver();
        }
        return webDriver;
    }

    public void open(String prefix, String pageUrl, String htmlSubstringOfAValidPage, int attemptCount) {
        for (int i = 1; i <= attemptCount; i++) {
            boolean lastAttempt = (i == attemptCount);
            try {
                getDriver().get("http://localhost:8080" + prefix + pageUrl);
            } catch (Exception e) {
                if (!lastAttempt) {
                    continue;
                }
                throw new RuntimeException(e);
            }
            if (assertPageContentValid("http://localhost:8080", pageUrl, htmlSubstringOfAValidPage, lastAttempt))
                break;
        }
    }


    protected void testAppFunctionalPage(String testAppPageUrl) {
        open(TEST_APP_URL_PREFIX, testAppPageUrl, getUtilJsUrlSubstring(), 5);
    }

    protected void liveDemoPage(String liveDemoPageUrl) {
        open(LIVE_DEMO_URL_PREFIX, liveDemoPageUrl, getUtilJsUrlSubstring(), 5);
    }

    private String getUtilJsUrlSubstring() {
        return "META-INF/resources/openfaces/util/util-"; // OpenFaces 2.x resource sub-string for util.js
    }

    private boolean assertPageContentValid(
            String applicationUrl,
            String pageUrl,
            String htmlSubstringOfAValidPage,
            boolean failIfNotLoaded) {

        String fullPageUrl = applicationUrl + pageUrl;
        if (htmlSubstringOfAValidPage == null)
            return true;

        String htmlSource;
        try {
            htmlSource = getDriver().getPageSource();
        } catch (Exception e) {
            String pageTitle;
            try {
                pageTitle = getDriver().getTitle();
            } catch (Exception ex) {
                pageTitle = "<exception on selenium.getTitle(): " + ex.getMessage() + ">";
            }
            try {
                if (failIfNotLoaded)
                    throw new RuntimeException("Couldn't open the page (failed getting HTML source of a page). ; page URL: "
                            + fullPageUrl + "; Page title: " + pageTitle, e);
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
                        getDriver().getTitle());
            else
                return false;
        }

        return true;
    }

    @After
    public void closeDriver() {
        getDriver().close();
    }
}
