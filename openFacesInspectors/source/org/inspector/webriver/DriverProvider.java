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

import org.apache.commons.lang3.StringUtils;
import org.inspector.OpenfacesInspectorContext;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.inspector.webriver.DriverProvider.BrowserType.CHROME;
import static org.inspector.webriver.DriverProvider.BrowserType.FIREFOX;
import static org.inspector.webriver.DriverProvider.BrowserType.IEXPLORER;
import static org.inspector.webriver.DriverProvider.BrowserType.findType;


/**
 * @author Max Yurin
 */
public class DriverProvider {
    public static final String IEDRIVER_SERVER_EXE = "lib\\selenium\\IEDriverServer.exe";
    public static final String CHROMEDRIVER_EXE = "lib\\selenium\\chromedriver.exe";

    private WebDriver webDriver;
    private DriverService driverService;
    private PropertyTestConfiguration properties;

    public DriverProvider(String browser, String version, String platform) {
        this.properties = OpenfacesInspectorContext.getProperties();

        startDriverServerForChromeAndIE(browser);
        initWebDriver(browser, version, platform);
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
    }

    private RemoteWebDriver createWebDriver(String platform, String browser, String version) throws Exception {
        URL url = driverService != null ? driverService.getUrl() : new URL("http://localhost:9090");

        DesiredCapabilities dc = getCapabilitiesFor(browser);
        dc.setPlatform(getPlatformFor(platform));
        dc.setJavascriptEnabled(true);
        RemoteWebDriver driver;

        if (findType(browser) == FIREFOX) {
            FirefoxProfile profile = new FirefoxProfile();
            profile.setPreference("toolkit.startup.max_resumed_crashes", "-1");
            profile.setAcceptUntrustedCertificates(true);
            driver = new FirefoxDriver(profile);
        } else {
            driver = new RemoteWebDriver(url, dc);
        }

        if (StringUtils.isNotBlank(version)) {
            dc.setVersion(version);
        }

        return driver;
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
                    .withSilent(true)
                    .withVerbose(true)
                    .usingAnyFreePort()
                    .build();

        } else if (type == IEXPLORER) {
            driverService = new InternetExplorerDriverService.Builder()
                    .usingDriverExecutable(new File(IEDRIVER_SERVER_EXE))
                    .withSilent(true)
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

    private DesiredCapabilities getCapabilitiesFor(String browserName) {
        final BrowserType type = findType(browserName);

        switch (type) {
            case FIREFOX:
                return DesiredCapabilities.firefox();
            case CHROME:
                final DesiredCapabilities chrome = DesiredCapabilities.chrome();
                return chrome;
            case OPERA:
                return DesiredCapabilities.opera();
            case SAFARI:
                return DesiredCapabilities.safari();
            case IEXPLORER:
                final DesiredCapabilities ie = DesiredCapabilities.internetExplorer();
                ie.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                return ie;
            default:
                return DesiredCapabilities.firefox();
        }
    }

    public WebDriver getDriver() {
        return webDriver;
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
