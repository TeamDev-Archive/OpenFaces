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

package org.openfaces.component.dynamicimage;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.ElementInspector;

public class DynamicImageClientSideEvents extends OpenFacesTestCase {
     @Test
    public void testDynamicImageClientSideEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/dynamicimage/dynamicImage_clientEvents.jsf");

        ElementInspector dynamicImage = element("form1:dynamicImage1");

        //onclick
        dynamicImage.click();
        assertTrue(selenium.isTextPresent("click"));

        //ondblclick
        dynamicImage.doubleClick();
        assertTrue(selenium.isTextPresent("dblclick"));

        //onmousedown
        dynamicImage.mouseDown();
        assertTrue(selenium.isTextPresent("mousedown"));

        //onmouseup
        dynamicImage.mouseUp();
        assertTrue(selenium.isTextPresent("mouseup"));

        //onmouseover
        dynamicImage.mouseOver();
        assertTrue(selenium.isTextPresent("mouseover"));

        //onmouseout
        dynamicImage.mouseOut();
        assertTrue(selenium.isTextPresent("mouseout"));

        //onmousemove
        dynamicImage.mouseMove();
        assertTrue(selenium.isTextPresent("mousemove"));

    }

}
