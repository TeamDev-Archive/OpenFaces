/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.suggestionfield;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.ElementInspector;

import java.awt.event.KeyEvent;

/**
 * @author Darya Shumilina
 */
public class SuggestionFieldClientSideEvents extends OpenFacesTestCase {
    @Test
    public void testSuggestionFieldClientSideEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/suggestionfield/SuggestionFieldEvents.jsf");

        //onfocus
        //todo: uncomment if JSFC-2724 fixed
/*
    selenium.click("formID:focusBlur");
    assertTrue(selenium.isTextPresent("onfocus works"));
    assertTrue(selenium.isTextPresent("focus"));
*/

        //onclick
        element("formID:clickDblclick").click();
        assertTrue(selenium.isTextPresent("onclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //onblur
        //todo: uncomment if JSFC-2724 fixed
/*
    assertTrue(selenium.isTextPresent("onblur works"));
    assertTrue(selenium.isTextPresent("blur"));
*/

        //onmouseover
        ElementInspector mouseoverMouseoutField = element("formID:mouseoverMouseout");
        mouseoverMouseoutField.mouseOver();
        assertTrue(selenium.isTextPresent("onmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //onmouseout
        mouseoverMouseoutField.mouseOut();
        assertTrue(selenium.isTextPresent("onmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //onmouseup
        ElementInspector mouseupMousedownField = element("formID:mouseupMousedown");
        mouseupMousedownField.mouseUp();
        assertTrue(selenium.isTextPresent("onmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        //onmousedown
        mouseupMousedownField.mouseDown();
        assertTrue(selenium.isTextPresent("onmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //onkeypress
        ElementInspector keyChangeField = element("formID:keyChange");
        keyChangeField.keyPress(KeyEvent.VK_Q);
        assertTrue(selenium.isTextPresent("onkeypress works"));
        assertTrue(selenium.isTextPresent("keypress"));

        //onkeydown
        //todo: uncomment if JSFC-2724 fixed
/*
    selenium.keyDown("formID:keyChange", "85");
    assertTrue(selenium.isTextPresent("onkeydown works"));
    assertTrue(selenium.isTextPresent("keydown"));
*/

        //onkeyup
        keyChangeField.keyUp(KeyEvent.VK_I);
        assertTrue(selenium.isTextPresent("onkeyup works"));
        assertTrue(selenium.isTextPresent("keyup"));

        //onchange
        keyChangeField.keyPress(13);
        assertTrue(selenium.isTextPresent("onchange works"));
        //todo: uncomment if JSFC-2724 fixed
        /*assertTrue(selenium.isTextPresent("change"));*/

        //ondblclick
        element("formID:clickDblclick").doubleClick();
        assertTrue(selenium.isTextPresent("ondblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        //todo: uncomment if JSFC-2724 fixed
/*
    //ondropdown
    selenium.keyPress("formID:clickDblclick", "40");
    assertTrue(selenium.isTextPresent("ondropdown works"));
    assertTrue(selenium.isTextPresent("dropdown"));

    //oncloseup
    selenium.keyPress("formID:clickDblclick", "40");
    selenium.keyPress("formID:clickDblclick", "13");
    assertTrue(selenium.isTextPresent("oncloseup works"));
    assertTrue(selenium.isTextPresent("closeup"));
*/

        //onmousemove
        element("formID:mousemove").mouseMove();
        assertTrue(selenium.isTextPresent("onmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));
    }

}