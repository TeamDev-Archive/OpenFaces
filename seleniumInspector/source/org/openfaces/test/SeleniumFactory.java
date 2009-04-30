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

package org.openfaces.test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;


public class SeleniumFactory {

    private String serverHost;
    private int serverPort;
    private String browserPath;
    private String browserUrl;
    private boolean addNamespacesToXpath;

    public SeleniumFactory(String serverHost, int serverPort, String browserPath, String browserUrl, boolean addNamespacesToXpath) {
        this(serverHost, serverPort, browserPath, browserUrl);
        this.addNamespacesToXpath = addNamespacesToXpath;
    }

    public SeleniumFactory(String serverHost, int serverPort, String browserPath, String browserUrl) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.browserPath = browserPath;
        this.browserUrl = browserUrl;
    }

    public String getBrowserUrl() {
        return browserUrl;
    }

    public void setBrowserUrl(String browserUrl) {
        this.browserUrl = browserUrl;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getBrowserPath() {
        return browserPath;
    }

    public void setBrowserPath(String browserPath) {
        this.browserPath = browserPath;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public boolean isAddNamespacesToXpath() {
        return addNamespacesToXpath;
    }

    public void setAddNamespacesToXpath(boolean addNamespacesToXpath) {
        this.addNamespacesToXpath = addNamespacesToXpath;
    }

    public Selenium getSelenium() {
        DefaultSelenium selenium = new QSelenium(serverHost, serverPort, browserPath,
                browserUrl, addNamespacesToXpath);
        return selenium;
    }
}
