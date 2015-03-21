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
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;


public class SeleniumWithServerAutostartFactory extends SeleniumFactory {

    private final static String SERVER_HOST = "localhost";

    private boolean slowResources;

    public SeleniumWithServerAutostartFactory(int serverPort, String browserPath, String browserUrl, boolean addNamespacesToXpath) {
        super(SERVER_HOST, serverPort, browserPath, browserUrl, addNamespacesToXpath);
    }

    public SeleniumWithServerAutostartFactory(int serverPort, String browserPath, String browserUrl) {
        super(SERVER_HOST, serverPort, browserPath, browserUrl);
    }

    public boolean isSlowResources() {
        return slowResources;
    }

    public void setSlowResources(boolean slowResources) {
        this.slowResources = slowResources;
    }

    public Selenium getSelenium() {
        try {
            RemoteControlConfiguration conf = new RemoteControlConfiguration();
            int serverPort = getServerPort();
            conf.setPort(serverPort);
            new SeleniumServer(slowResources, conf).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return super.getSelenium();
    }
}
