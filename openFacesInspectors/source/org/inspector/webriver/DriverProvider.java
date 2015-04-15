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

package org.inspector.webriver;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.inspector.webriver.DriverProvider.BrowserType.CHROME;
import static org.inspector.webriver.DriverProvider.BrowserType.IEXPLORER;
import static org.inspector.webriver.DriverProvider.BrowserType.findType;


/**
 * @author Max Yurin
 */
public class DriverProvider {
    public static final String IEDRIVER_SERVER_EXE = "lib\\selenium\\IEDriverServer.exe";
    public static final String CHROMEDRIVER_EXE = "lib\\selenium\\chromedriver.exe";

    private WebDriver webDriver;
    private Selenium selenium;
    private DriverService driverService;
    private PropertyTestConfiguration properties;

    public DriverProvider(PropertyTestConfiguration properties, String browser, String version, String platform) {
        this(properties);

        startDriverServerForChromeAndIE(browser);
        initWebDriver(browser, version, platform);
    }

    public DriverProvider(PropertyTestConfiguration propertyTestConfiguration) {
        this.properties = propertyTestConfiguration;
    }

    private void initWebDriver(String browser, String version, String platform) {
        try {
            if (properties.isUseGrid()) {
                webDriver = createWebDriver(platform, browser, version);
            } else {
                webDriver = new FirefoxDriver();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        selenium = new WebDriverBackedSelenium(webDriver, properties.getDefaultUrl());
    }

    public void resetSelenium() {
        if (getDriver() != null) {
            getDriver().close();
//            getDriver().quit();
        }
        if (driverService != null) {
            driverService.stop();
        }
    }

    private RemoteWebDriver createWebDriver(String platform, String browser, String version) throws Exception {
        DesiredCapabilities dc = getBrowserBy(browser);
        dc.setPlatform(getPlatformFor(platform));

        if (StringUtils.isNotBlank(version)) {
            dc.setVersion(version);
        }

        URL url = driverService != null ? driverService.getUrl() : new URL("http://localhost:9090");

        return dc.getBrowserName().equals("firefox") ? new FirefoxDriver(dc) : new RemoteWebDriver(url, dc);
    }

    private Platform getPlatformFor(String platform) {
        return platform.equals("windows") ? Platform.WINDOWS : Platform.LINUX;
    }

    private void startDriverServerForChromeAndIE(String browserName) {
        if (!CHROME.getBrowserName().equals(browserName) &&
                !IEXPLORER.getBrowserName().equals(browserName)) {
            return;
        }

        final BrowserType type = findType(browserName);

        if (type == CHROME) {
            driverService = new ChromeDriverService.Builder()
                    .usingDriverExecutable(new File(CHROMEDRIVER_EXE))
                    .usingAnyFreePort()
                    .build();

        } else if (type == IEXPLORER) {
            driverService = new InternetExplorerDriverService.Builder()
                    .usingDriverExecutable(new File(IEDRIVER_SERVER_EXE))
                    .usingAnyFreePort()
                    .build();
        } else {
            throw new IllegalArgumentException("Incompatible DriverService type " + browserName);
        }

        try {
            driverService.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sleep(int milliseconds) {
        getDriver().manage().timeouts().implicitlyWait(milliseconds, TimeUnit.MILLISECONDS);
    }

    private DesiredCapabilities getBrowserBy(String browserName) {
        final BrowserType type = findType(browserName);

        switch (type) {
            case FIREFOX:
                return DesiredCapabilities.firefox();
            case CHROME:
                return DesiredCapabilities.chrome();
            case OPERA:
                return DesiredCapabilities.opera();
            case SAFARI:
                return DesiredCapabilities.safari();
            case IEXPLORER:
                return DesiredCapabilities.internetExplorer();
            default:
                return DesiredCapabilities.firefox();
        }
    }

    public WebDriver getDriver() {
        return webDriver;
    }

    @SuppressWarnings("unchecked")
    public Selenium getSelenium() {
        return selenium;
    }

    public static enum BrowserType {
        FIREFOX("firefox"),
        IEXPLORER("iexplorer"),
        CHROME("chrome"),
        SAFARI("safari"),
        OPERA("opera");

        private String browserName;

        BrowserType(String browserName) {
            this.browserName = browserName;
        }

        public static BrowserType findType(String value) {
            for (BrowserType browserType : values()) {
                final String type = browserType.browserName.toLowerCase();
                if (type.equals(value)) {
                    return browserType;
                }
            }
            throw new IllegalArgumentException("Incompatible Browser type: " + value);
        }

        public String getBrowserName() {
            return browserName;
        }
    }
}
