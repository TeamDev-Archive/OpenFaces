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
import org.apache.commons.lang.StringUtils;
import org.inspector.SeleniumHolder;
import org.inspector.api.ControlFactory;
import org.inspector.navigator.FuncTestsPages;
import org.inspector.navigator.URLPageNavigator;
import org.inspector.webriver.PropertyTestConfiguration;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Max Yurin
 */
public class BaseSeleniumTest {
    public static final String DEMO_CONTEXT_PATH = "demo.context.path";
    public static final String TEST_APP_CONTEXT_PATH = "test.app.context.path";
    public static final String TEST_APP_IS_FACELETS = "test.app.is.facelets";

    protected static String testAppUrlPrefix;
    protected static String demoUrlPrefix;
    protected static Boolean isFacelets;
    private static PropertyTestConfiguration properties;
    private URLPageNavigator urlPageNavigator;
    private ControlFactory controlFactory;

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

    public ControlFactory getControlFactory() {
        return controlFactory;
    }

    protected void navigateTo(FuncTestsPages page) {
        urlPageNavigator.navigateTo(page);
    }

    protected List<String> checkAllPages(){
        return urlPageNavigator.checkAllPages();
    }

    @BeforeMethod
    @Parameters({"browser", "version", "platform"})
    public void setUp(String browser, String version, String platform) throws Exception {
        SeleniumHolder.createNewDriverProvider(properties, browser, version, platform);

        isFacelets = isTestAppFacelets();
        testAppUrlPrefix = getTestAppUrlPrefix();
        demoUrlPrefix = getDemoUrlPrefix();

        urlPageNavigator = new URLPageNavigator(getDriver(), properties.getDefaultUrl() + testAppUrlPrefix);
        controlFactory = new ControlFactory(getDriver());
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
}