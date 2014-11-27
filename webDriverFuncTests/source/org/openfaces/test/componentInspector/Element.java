package org.openfaces.test.componentInspector;/*
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

import org.openfaces.test.WebDriverSeleniumTestCase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static com.codeborne.selenide.Selenide.$;

/**
 * Author: SergeyPensov@teamdev.com
 */
public class Element extends WebDriverSeleniumTestCase {
    private String locator;

    public Element(String id) {
        this.locator = id;
    }

    public String getId() {
        return locator;
    }

    public boolean isElementInPage(){
        try{
            driver.findElements(By.id(locator));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String toString() {
        return "elementByLocator[" + locator + "]";
    }

    public void click(){
        $(this.locator).click();
    }
}
