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
package org.openfaces.component.tabbedpane;

import com.thoughtworks.selenium.Selenium;
import org.junit.Ignore;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.openfaces.OpenFacesAjaxLoadingMode;

/**
 * @author Darya Shumilina
 */
public class TabbedPaneClientSideEvents extends OpenFacesTestCase {

    //todo: uncomment when the JSFC-3629 is fixed

     @Test @Ignore
    public void _testTabbedPaneClientSideEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/tabbedpane/tabbedPane.jsf");

        //onclick
        selenium.click("test_form:onclickTabbedPaneID");
        assertTrue(selenium.isTextPresent("onclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //ondblclick
        selenium.doubleClick("test_form:ondblclickTabbedPaneID");
        assertTrue(selenium.isTextPresent("ondblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        //onmousedown
        selenium.mouseDown("test_form:onmousedownTabbedPaneID");
        assertTrue(selenium.isTextPresent("onmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //onmouseup
        selenium.mouseUp("test_form:onmouseupTabbedPaneID");
        assertTrue(selenium.isTextPresent("onmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        //todo: uncomment if JSFC-1449 fixed
/*    //onmouseover
    selenium.mouseOver("test_form:onmouseoverTabbedPaneID");
    assertTrue(selenium.isTextPresent("onmouseover works"));
    assertTrue(selenium.isTextPresent("mouseover"));

    //onmouseout
    selenium.mouseOut("test_form:onmouseoutTabbedPaneID");
    assertTrue(selenium.isTextPresent("onmouseout works"));
    assertTrue(selenium.isTextPresent("mouseout"));*/

        //onmousemove
        selenium.mouseMove("test_form:onmousemoveTabbedPaneID");
        assertTrue(selenium.isTextPresent("onmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));

        //onkeydown
        selenium.keyDown("test_form:firstTabKeyDownInputID", "77");
        assertTrue(selenium.isTextPresent("onkeydown works"));
        assertTrue(selenium.isTextPresent("keydown"));

        //onkeyup
        selenium.keyUp("test_form:firstTabKeyUpInputID", "77");
        assertTrue(selenium.isTextPresent("onkeyup works"));
        assertTrue(selenium.isTextPresent("keyup"));

        //onkeypress
        selenium.keyPress("test_form:firstTabKeyPressInputID", "77");
        assertTrue(selenium.isTextPresent("onkeypress works"));
        assertTrue(selenium.isTextPresent("keypress"));

        //onselectionchange
        tabbedPane("test_form:onselectionchangeTabbedPaneID").tabSet().tabs().get(1).click();
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        assertTrue(selenium.isTextPresent("onselectionchange works"));
        //todo: uncomment if JSFC-1447 and JSFC-1448 fixed
        /*assertTrue(selenium.isTextPresent("selectionchange"));*/
    }

}