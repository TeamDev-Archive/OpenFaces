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

import org.inspector.OpenfacesInspectorContext;
import org.inspector.components.AjaxSupport;
import org.inspector.webriver.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Max Yurin
 */
public class WebDriverInvokedListener implements IInvokedMethodListener {
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult iTestResult) {
        OpenfacesInspectorContext.createInstance();

        if (method.isTestMethod()) {
            final Map<String, String> parameters = method.getTestMethod().getXmlTest().getLocalParameters();
            final String browser = parameters.get("browser");
            final String version = parameters.get("version");
            final String platform = parameters.get("platform");

            WebDriverManager.createInstance(browser, version, platform);

            WebDriverManager.getWebDriver().get("http://google.com");
            WebDriverManager.getWebDriver().manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);

            AjaxSupport.init(WebDriverManager.getWebDriver());
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        final int status = iTestResult.getStatus();

        if (ITestResult.SUCCESS == status || ITestResult.FAILURE == status) {
            final WebDriver webDriver = WebDriverManager.getWebDriver();
            if(webDriver != null){
                System.out.println("Web driver close.");
                WebDriverManager.close();
            }
        }
    }
}
