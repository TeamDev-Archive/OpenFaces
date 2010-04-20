/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.test;

import org.seleniuminspector.SeleniumTestCase;
import org.seleniuminspector.SeleniumFactory;
import org.seleniuminspector.SeleniumWithServerAutostartFactory;
import org.seleniuminspector.SeleniumHolder;
import org.seleniuminspector.openfaces.*;

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
    protected static final String LIVE_DEMO_URL_PREFIX = getSystemProperty("demo.context.path", IS_FACELETS ? "/LiveDemoFacelets" : "/LiveDemoJsp");

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
        SeleniumFactory seleniumFactory = new SeleniumWithServerAutostartFactory(CUSTOM_SELENIUM_PORT, browserPath, startUrl, addNamespacesToXpath);
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

    private void testAppPage(String testAppPageUrl) {
        openAndWait(TEST_APP_URL_PREFIX, testAppPageUrl);
    }

    protected void testAppFunctionalPage(String testAppPageUrl) {
        testAppPage(testAppPageUrl);
    }

    protected void liveDemoPage(String testAppPageUrl) {
        openAndWait(LIVE_DEMO_URL_PREFIX, testAppPageUrl);
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

}