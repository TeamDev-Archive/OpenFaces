/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.requests;

import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

/**
 * @author Pavel Kaplin
 */
public class JSFC_2282Test extends OpenFacesTestCase {
    //achtung
    // modal layer in ie6 behaves incorrectly if there's no background color or image specified
    @Test
    public void testDefaultStyleForModalLayerIncludesClearGif() {
        testAppFunctionalPage("/requests/JSFC_2282.jsf");
        WebElement element = getDriver().findElement(By.id("fm:bt"));
        element.click();
//        element("fm:bt").click();
        try {
            element = getDriver().findElement(By.id("fm:cn::blockingLayer"));
        } catch (WebDriverException e) {
            assert false;
        }
        getDriver().findElement(By.id("fm:cn::no_button")).click();
//        element("fm:cn::blockingLayer").assertElementExists(true);

//        element("test").click();
        element = getDriver().findElement(By.id("test"));
        element.click();
        assertEquals("OK", window().document().getAlert());
    }
}
