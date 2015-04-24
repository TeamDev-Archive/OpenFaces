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

import org.apache.commons.lang.StringUtils;
import org.inspector.InspectorContext;
import org.inspector.components.ControlFactory;
import org.inspector.components.Pagination;
import org.inspector.navigator.DocumentReadyCondition;
import org.inspector.navigator.FuncTestsPages;
import org.inspector.navigator.URLPageNavigator;
import org.inspector.webriver.PropertyTestConfiguration;
import org.inspector.webriver.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;

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
    private URLPageNavigator urlPageNavigator;
    private ControlFactory controlFactory;

    public static PropertyTestConfiguration getProperties() {
        return InspectorContext.getProperties();
    }

    private static String getTestAppUrlPrefix() {
        return getProperties().getTestAppFaceletsURLPrefix();
    }

    private static String getDemoUrlPrefix() {
        return getProperties().getLiveDemoFaceletsURLPrefix();
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

    public URLPageNavigator getUrlPageNavigator() {
        if (urlPageNavigator == null) {
            urlPageNavigator = new URLPageNavigator(getProperties().getDefaultUrl() + testAppUrlPrefix);
        }

        return urlPageNavigator;
    }

    public ControlFactory getControlFactory() {
        if (controlFactory == null) {
            controlFactory = new ControlFactory();
        }

        return controlFactory;
    }

    protected void navigateTo(FuncTestsPages page) {
        getUrlPageNavigator().navigateTo(page);
    }

    protected Pagination getTabNavigator(String tabSetId) {
        return new Pagination(getDriver(), By.id(tabSetId));
    }

    protected List<String> checkAllPages() {
        return getUrlPageNavigator().checkAllPages();
    }

    public WebElement findBy(String id) {
        return getDriver().findElement(By.id(id));
    }

    @BeforeMethod
    public void setUp() throws Exception {
        isFacelets = isTestAppFacelets();
        testAppUrlPrefix = getTestAppUrlPrefix();
        demoUrlPrefix = getDemoUrlPrefix();
    }

    public void sleep(int milliseconds) {
        getDriver().manage().timeouts().implicitlyWait(milliseconds, TimeUnit.MILLISECONDS);
    }

    public void waitForDocumentReady() {
        final WebDriverWait wait = new WebDriverWait(getDriver(), 200);

        final DocumentReadyCondition condition = new DocumentReadyCondition();
        condition.apply(getDriver());

        wait.until(condition);
    }

    public WebDriver getDriver() {
        return WebDriverManager.getWebDriver();
    }
}