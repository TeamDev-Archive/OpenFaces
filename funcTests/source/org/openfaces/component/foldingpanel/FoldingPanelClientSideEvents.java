/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.foldingpanel;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.ElementInspector;

import java.awt.event.KeyEvent;

/**
 * @author Darya Shumilina
 */
public class FoldingPanelClientSideEvents extends OpenFacesTestCase {
    @Test
    public void testFoldingPanelClientSideEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/foldingpanel/foldingPanel.jsf");

        // onclick
        element("formID:clickID").click();
        assertTrue(selenium.isTextPresent("onclick works"));
        assertTrue(selenium.isTextPresent("click"));

        // ondblclick
        element("formID:doubleclickID").doubleClick();
        assertTrue(selenium.isTextPresent("ondblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        // onmousedown
        element("formID:mousedownID").mouseDown();

        assertTrue(selenium.isTextPresent("onmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        // onmouseover
        element("formID:mouseoverID").mouseOver();
        assertTrue(selenium.isTextPresent("onmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        // onmousemove
        element("formID:mousemoveID").mouseMove();
        assertTrue(selenium.isTextPresent("onmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));

        // onmouseout
        element("formID:mouseoutID").mouseOut();
        assertTrue(selenium.isTextPresent("onmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        // onmouseup
        element("formID:mouseupID").mouseUp();
        assertTrue(selenium.isTextPresent("onmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        //todo: implement if JSFC-2689 fixed
/*
    //onfocus

    //onblur
*/

        // onkeydown
        ElementInspector contentKeyDown = element("formID:content_keydownID");
        contentKeyDown.setCursorPosition(2);
        contentKeyDown.keyDown(KeyEvent.VK_M);
        assertTrue(selenium.isTextPresent("onkeydown works"));
        assertTrue(selenium.isTextPresent("keydown"));

        // onkeyup
        ElementInspector contentKeyUp = element("formID:content_keyupID");
        contentKeyUp.setCursorPosition(2);
        contentKeyUp.keyUp(KeyEvent.VK_M);
        assertTrue(selenium.isTextPresent("onkeyup works"));
        assertTrue(selenium.isTextPresent("keyup"));

        // onkeypress
        ElementInspector contentKeyPress = element("formID:content_keypressID");
        contentKeyPress.setCursorPosition(2);
        contentKeyPress.keyPress(KeyEvent.VK_M);
        assertTrue(selenium.isTextPresent("onkeypress works"));
        assertTrue(selenium.isTextPresent("keypress"));

        // onstatechange
        foldingPanel("formID:statechangeID").toggle().click();
        assertTrue(selenium.isTextPresent("onstatechange works"));
        //todo: uncomment if JSFC-2691 and JSFC-1439 fixed
        /*assertTrue(selenium.isTextPresent("statechange"));*/
    }

}