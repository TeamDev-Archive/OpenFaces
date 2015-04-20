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

package org.inspector.components;

import com.google.common.base.Function;
import org.inspector.SeleniumHolder;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

/**
 * @author Max Yurin
 */
public class AjaxSupport {
    private WebDriver webDriver;

    public AjaxSupport(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void waitAjaxProcess() {
        SeleniumHolder.getDriverProvider().sleep(500);
        waitForCondition();
    }

    public void waitForCondition() {
        final Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver)
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(5, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

        wait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver webDriver) {
                return (WebElement) ((JavascriptExecutor) webDriver).executeScript(
                        "var value = window.document._ajaxInProgressMessage " +
                                "? window.document._ajaxInProgressMessage.style.display " +
                                ": 'none'; value == 'none';");
            }
        });
//
//        SeleniumHolder.getSelenium()
//                .waitForCondition("var value = window.document._ajaxInProgressMessage " +
//                        "? window.document._ajaxInProgressMessage.style.display " +
//                        ": 'none'; value == 'none';", "30000");
    }

    public void catchAlert() {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("window.alert = function(msg) { document.lastAlert=msg; }");
        String lastAlert = (String) js.executeScript("return document.lastAlert");
        if (lastAlert != null) {
            throw new RuntimeException("Alert! " + lastAlert);
        }
    }
}
