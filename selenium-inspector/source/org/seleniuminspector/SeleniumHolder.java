/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.seleniuminspector;

import com.thoughtworks.selenium.Selenium;
import org.openqa.selenium.WebDriver;
import org.seleniuminspector.webriver.DriverProvider;
import org.seleniuminspector.webriver.PropertyTestConfiguration;

public class SeleniumHolder {
    private static SeleniumHolder seleniumHolder = new SeleniumHolder();
    private static DriverProvider driverProvider;

    private SeleniumHolder() {
    }

    public static SeleniumHolder getInstance() {
        return seleniumHolder;
    }

    public static void createNewDriverProvider(PropertyTestConfiguration properties,
                                               String browser, String version, String platform) {
        driverProvider = new DriverProvider(properties, browser, version, platform);
    }

    public static DriverProvider getDriverProvider() {
        if (driverProvider == null) {
            throw new RuntimeException("Can't obtain selenium. DriverProvider isn't specified.");
        }

        return driverProvider;
    }

    public static WebDriver getDriver(){
        return driverProvider.getDriver();
    }

    public static Selenium getSelenium() {
        return driverProvider.getSelenium();
    }

    public static void resetSelenium() {
        driverProvider.resetSelenium();
    }
}