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

import com.google.common.base.Predicate;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * @author Max Yurin
 */
public class AjaxSupport {
    public final static int DEFAULT_WAIT_4_PAGE = 2; //seconds

    private static WebDriver driver;

    public AjaxSupport() {}

    public static void init(WebDriver webDriver){
        driver = webDriver;
    }

    public static void waitAjaxProcess(By locator) {
        By by = locator != null ? locator : By.xpath("//html");

        waitForCondition();
        waitForElementPresent(by);
    }

    public static void waitForCondition() {
        new WebDriverWait(driver, DEFAULT_WAIT_4_PAGE) {
        }.until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver webDriver) {
                return ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete");
            }
        });
    }

    private static void waitForElementPresent(final By locator) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

        final WebElement webElement = new WebDriverWait(driver, DEFAULT_WAIT_4_PAGE) {
        }
                .until(new ExpectedCondition<WebElement>() {
                    @Override
                    public WebElement apply(WebDriver webDriver) {
                        return webDriver.findElement(locator);
                    }
                });

        driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS);
    }

    public static void catchAlert() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.alert = function(msg) { document.lastAlert=msg; }");
        String lastAlert = (String) js.executeScript("return document.lastAlert");
        if (lastAlert != null) {
            throw new RuntimeException("Alert! " + lastAlert);
        }
    }
}
