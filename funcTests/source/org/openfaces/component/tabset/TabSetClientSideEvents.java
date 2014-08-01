/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.tabset;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;

/**
 * @author Darya Shumilina
 */
public class TabSetClientSideEvents extends OpenFacesTestCase {
     //@Test
    public void testTabSetClientSideEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/tabset/tabSet.jsf");

        //onclick
        element("test_form:onclickTabSetID").click();
        assertTrue(selenium.isTextPresent("onclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //ondblclick
        element("test_form:ondblclickTabSetID").doubleClick();
        assertTrue(selenium.isTextPresent("ondblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        //onmousedown
        element("test_form:onmousedownTabSetID").mouseDown();
        assertTrue(selenium.isTextPresent("onmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //onmouseover
        element("test_form:onmouseoverTabSetID").mouseOver();
        assertTrue(selenium.isTextPresent("onmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //onmouseup
        element("test_form:onmouseupTabSetID").mouseUp();
        assertTrue(selenium.isTextPresent("onmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        //onmouseout
        element("test_form:onmouseoutTabSetID").mouseOut();
        assertTrue(selenium.isTextPresent("onmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //onmousemove
        element("test_form:onmousemoveTabSetID").mouseMove();
        assertTrue(selenium.isTextPresent("onmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));

        //onchange
        tabSet("test_form:onchangeTabSetID").tabs().get(1).click();
        assertTrue(selenium.isTextPresent("onchange works"));
        //todo: uncomment if JSFC-2696 and JSFC-2697 fixed
/*    assertTrue(selenium.isTextPresent("change"));*/
    }

}