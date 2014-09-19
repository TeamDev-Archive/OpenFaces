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
package org.openfaces.component.dropdownfield;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.openfaces.DropDownFieldInspector;

import java.awt.event.KeyEvent;

/**
 * @author Darya Shumilina
 */
public class DropDownClientSideEvents extends OpenFacesTestCase {
     @Test
    public void testDropDownEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/dropdown/dropDown.jsf");

        DropDownFieldInspector keypressField = dropDownField("formID:keypressID");
        // onkeypress
        keypressField.field().keyPress(KeyEvent.VK_Z);

        assertTrue(selenium.isTextPresent("onkeypress works"));
        assertTrue(selenium.isTextPresent("keypress"));

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

        // onmouseup
        element("formID:mouseupID").mouseUp();
        assertTrue(selenium.isTextPresent("onmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        // onmouseout
        element("formID:mouseoutID").mouseOut();
        assertTrue(selenium.isTextPresent("onmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        // onmousemove
        element("formID:mousemoveID").mouseMove();
        assertTrue(selenium.isTextPresent("onmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));

        //todo: uncomment if JSFC-1431 is fixed

        // onchange
        DropDownFieldInspector changeField = dropDownField("formID:changeID");
        changeField.field().type("Red");
        sleep(100);
        assertTrue(selenium.isTextPresent("onchange works"));
        assertTrue(selenium.isTextPresent("change"));

        // onfocus
        sleep(100);
        assertTrue(selenium.isTextPresent("onfocus works"));
        assertTrue(selenium.isTextPresent("focus"));

        // onblur
        DropDownFieldInspector keyDownField = dropDownField("formID:keydownID");
        sleep(100);
        assertTrue(selenium.isTextPresent("onblur works"));
        assertTrue(selenium.isTextPresent("blur"));

        // onkeydown
        keyDownField.field().keyDown(KeyEvent.VK_Z);
        assertTrue(selenium.isTextPresent("onkeydown works"));
        assertTrue(selenium.isTextPresent("keydown"));

        // onkeyup
        DropDownFieldInspector keyupField = dropDownField("formID:keyupID");
        keyupField.field().keyUp(KeyEvent.VK_Z);
        assertTrue(selenium.isTextPresent("onkeyup works"));
        assertTrue(selenium.isTextPresent("keyup"));

        //todo: uncomment it if JSFC-1436 is fixed
/*    // ondropdown
    selenium.click("formID:dropdownID" + BUTTON_SUFFIX);
    assertTrue(selenium.isVisible("formID:dropdownID--popup"));
    assertTrue(selenium.isTextPresent("ondropdown works"));
    assertTrue(selenium.isTextPresent("dropdown"));

    // oncloseup
    selenium.click("formID:closeupID" + BUTTON_SUFFIX);
    assertTrue(selenium.isVisible("formID:closeupID--popup"));
    selenium.click("formID:closeupID--popup::popupItem0");
    assertTrue(selenium.isTextPresent("oncloseup works"));
    assertTrue(selenium.isTextPresent("closeup"));*/
    }

}