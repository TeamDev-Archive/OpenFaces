/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.server.SeleniumServer;

/**
 * Author: Andrew.Palval@teamdev.com
 */
public class KickOffTest extends WebDriverSeleniumTestCase {

    private DefaultSelenium localhost;
    private SeleniumServer seleniumServer;

    @Before
    public void setUp() throws Exception {
//        localhost = new DefaultSelenium("localhost", 4444, "*chrome", "http://openfaces.org.test");
        seleniumServer = new SeleniumServer();
        seleniumServer.start();
//        localhost.start();
    }

    @After
    public void tearDown() throws Exception {
        seleniumServer.stop();
    }

    @Test
    public void testAvailability() {
        WebDriver driver = new FirefoxDriver();
        driver.get("http://localhost:8080/overview/homepage.jsf");
    }
}
