/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.seleniuminspector.openfaces;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seleniuminspector.LoadingMode;
import org.seleniuminspector.SeleniumHolder;

/**
 * @author Eugene Goncharov
 */
public class OpenFacesAjaxLoadingMode extends org.seleniuminspector.LoadingMode {
    private static LoadingMode loadingMode = new OpenFacesAjaxLoadingMode();

    private OpenFacesAjaxLoadingMode(){}


    public static LoadingMode getInstance() {
        return loadingMode;
    }

    public void waitForLoad() {
        // todo: write a correct processing code for OpenFaces Ajax completion without relying on fixed time delays
//        WebDriver driver = ((WrapsDriver) SeleniumHolder.getInstance().getSelenium()).getWrappedDriver();
////        boolean isListPopulated = (new WebDriverWait(driver, 30000))
////                .until(new ExpectedCondition<Boolean>() {
////                    public Boolean apply(WebDriver d) {
////                        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) d;
////                        return (Boolean)javascriptExecutor.executeScript("return (document._ajaxInProgressMessage.style.display=='none' ? true : false);");
////                    }
////                });
//        sleep(2000);
//        sleep(500); // wait a little while Ajax request starts asynchronously
//        Selenium selenium = SeleniumHolder.getInstance().getSelenium();
//        selenium.waitForCondition("var value = window.document._ajaxInProgressMessage ? window.document._ajaxInProgressMessage.style.display : 'none'; value == 'none';", "30000");

        sleep(500);

        final WebDriver driver = ((WrapsDriver) SeleniumHolder.getInstance().getSelenium()).getWrappedDriver();

        final WebDriverWait wait = new WebDriverWait(driver, 2);
        final DocumentReadyCondition condition = new DocumentReadyCondition();

        condition.apply(driver);
        wait.until(condition);
    }

    public class DocumentReadyCondition implements ExpectedCondition<Boolean> {

        public Boolean apply(WebDriver webDriver) {
            Object result = ((JavascriptExecutor) webDriver).executeScript(
                    "return document['readyState'] ? 'complete' == document.readyState : true");
            return result != null && result instanceof Boolean && (Boolean) result;
        }
    }
}
