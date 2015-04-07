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

package org.seleniuminspector.webriver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Max Yurin
 */
public class PropertyTestConfiguration {
    public static final String FUNC_TESTS_PROPERTIES_FILE = "/funcTests.properties";

    private static final String USE_GRID = "test.use.selenium.grid";
    private static final String GRID_URL = "test.selenium.grid.url";

    private static final String DEFAULT_URL = "org.openfaces.funcTests.selenium.default.url";
    private static final String DEFAULT_HOST = "org.openfaces.funcTests.host";
    private static final String DEFAULT_PORT = "org.openfaces.funcTests.port";

    private static final String BASE_REPORT_FOLDER = "org.openfaces.funcTests.report.base.folder";
    private static final String FAIL_TESTS_FOLDER = "org.openfaces.funcTests.report.fail.tests.folder";
    private static final String COMPLETE_TESTS_FOLDER = "org.openfaces.funcTests.report.complete.tests.folder";
    private static final String DEFAULT_REPORT_LEVEL = "org.openfaces.funcTests.report.level";

    private static final String CUSTOM_SELENIUM_PORT = "org.openfaces.funcTests.selenium.custom.port";
    private static final String DEFAULT_BROWSER = "org.openfaces.funcTests.default.browser";
    private static final String FIREFOX_PATH = "org.openfaces.funcTests.firefox.path";
    private static final String CHROME_PATH = "org.openfaces.funcTests.chrome.path";
    private static final String IE_PATH = "org.openfaces.funcTests.explorer.path";
    private static final String OPERA_PATH = "org.openfaces.funcTests.opera.path";
    private static final String SAFARI_PATH = "org.openfaces.funcTests.safari.path";

    private static final String IMPLEMENTATION = "test.app.jsf.implementation.value";

    private static final String TEST_APP_FACELETS_URL_PREFIX = "org.openfaces.funcTests.testApp.facelets.url.prefix";
    private static final String LIVE_DEMO_FACELETS_URL_PREFIX = "org.openfaces.funcTests.liveDemo.facelets.url.prefix";
    private static final String TEST_APP_JSP_URL_PREFIX = "org.openfaces.funcTests.testApp.jsp.url.prefix";
    private static final String LIVE_DEMO_JSP_URL_PREFIX = "org.openfaces.funcTests.liveDemo.jsp.url.prefix";
    private static final String DEFAULT_TEST_APP = "org.openfaces.funcTest.default.test.app";
    private static final String DEFAULT_DEMO_APP = "org.openfaces.funcTest.default.demo.app";


    private final boolean useGrid;
    private final String defaultUrl;
    private final String defaultHost;
    private final String defaultPort;

    private final String iePath;
    private final String defaultBrowser;
    private final String implementation;

    private final String baseReportFolder;
    private final String failTestsFolder;
    private final String completeTestsFolder;

    private final int customSeleniumPort;
    private final String firefoxPath;
    private final String chromePath;
    private final String operaPath;
    private final String safariPath;

    private final String testAppFaceletsURLPrefix;
    private final String liveDemoFaceletsURLPrefix;
    private final String testAppJspURLPrefix;
    private final String liveDemoJspURLPrefix;
    private final boolean faceletsDefaultTestApp;
    private final boolean faceletsDefaultDemoApp;
    private String gridUrl;

    public PropertyTestConfiguration() {
        InputStream resourceAsStream = this.getClass().getResourceAsStream(FUNC_TESTS_PROPERTIES_FILE);
        Properties properties = new Properties();

        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        useGrid = Boolean.parseBoolean(properties.getProperty(USE_GRID));
        gridUrl = properties.getProperty(GRID_URL);
        iePath = properties.getProperty(IE_PATH);
        defaultBrowser = properties.getProperty(DEFAULT_BROWSER);
        firefoxPath = properties.getProperty(FIREFOX_PATH);
        chromePath = properties.getProperty(CHROME_PATH);
        operaPath = properties.getProperty(OPERA_PATH);
        safariPath = properties.getProperty(SAFARI_PATH);
        implementation = properties.getProperty(IMPLEMENTATION);
        defaultUrl = properties.getProperty(DEFAULT_URL);
        defaultHost = properties.getProperty(DEFAULT_HOST);
        defaultPort = properties.getProperty(DEFAULT_PORT);
        customSeleniumPort = Integer.parseInt(properties.getProperty(CUSTOM_SELENIUM_PORT));
        baseReportFolder = properties.getProperty(BASE_REPORT_FOLDER);
        failTestsFolder = properties.getProperty(FAIL_TESTS_FOLDER);
        completeTestsFolder = properties.getProperty(COMPLETE_TESTS_FOLDER);
        testAppFaceletsURLPrefix = properties.getProperty(TEST_APP_FACELETS_URL_PREFIX);
        liveDemoFaceletsURLPrefix = properties.getProperty(LIVE_DEMO_FACELETS_URL_PREFIX);
        testAppJspURLPrefix = properties.getProperty(TEST_APP_JSP_URL_PREFIX);
        liveDemoJspURLPrefix = properties.getProperty(LIVE_DEMO_JSP_URL_PREFIX);
        faceletsDefaultTestApp = testAppFaceletsURLPrefix != null && testAppFaceletsURLPrefix.equals(properties.getProperty(DEFAULT_TEST_APP));
        faceletsDefaultDemoApp = liveDemoFaceletsURLPrefix != null && liveDemoFaceletsURLPrefix.equals(properties.getProperty(DEFAULT_DEMO_APP));
    }

    public boolean isUseGrid() {
        return useGrid;
    }

    public String getDefaultUrl() {
        return defaultUrl;
    }

    public String getIePath() {
        return iePath;
    }

    public String getDefaultBrowser() {
        return defaultBrowser;
    }

    public String getFirefoxPath() {
        return firefoxPath;
    }

    public String getChromePath() {
        return chromePath;
    }

    public String getOperaPath() {
        return operaPath;
    }

    public String getSafariPath() {
        return safariPath;
    }

    public String getImplementation() {
        return implementation;
    }

    public String getDefaultHost() {
        return defaultHost;
    }

    public String getDefaultPort() {
        return defaultPort;
    }

    public int getCustomSeleniumPort() {
        return customSeleniumPort;
    }

    public String getBaseReportFolder() {
        return baseReportFolder;
    }

    public String getFailTestsFolder() {
        return failTestsFolder;
    }

    public String getCompleteTestsFolder() {
        return completeTestsFolder;
    }

    public String getTestAppFaceletsURLPrefix() {
        return testAppFaceletsURLPrefix;
    }

    public String getLiveDemoFaceletsURLPrefix() {
        return liveDemoFaceletsURLPrefix;
    }

    public String getTestAppJspURLPrefix() {
        return testAppJspURLPrefix;
    }

    public String getLiveDemoJspURLPrefix() {
        return liveDemoJspURLPrefix;
    }

    public boolean isFaceletsDefaultTestApp() {
        return faceletsDefaultTestApp;
    }

    public boolean isFaceletsDefaultDemoApp() {
        return faceletsDefaultDemoApp;
    }

    public String getGridUrl() {
        return gridUrl;
    }
}
