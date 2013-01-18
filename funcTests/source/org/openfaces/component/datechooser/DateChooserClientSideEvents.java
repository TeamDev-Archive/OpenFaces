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
package org.openfaces.component.datechooser;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.openfaces.DateChooserInspector;

import java.awt.event.KeyEvent;

/**
 * @author Darya Shumilina
 */
public class DateChooserClientSideEvents extends OpenFacesTestCase {
    @Test
    public void testDateChooserEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datechooser/dateChooser.jsf");

        //onkeypress
        DateChooserInspector keyPressDateChooser = dateChooser("formID:keypressID");
        keyPressDateChooser.field().keyPress(KeyEvent.VK_Z);
        assertTrue(selenium.isTextPresent("onkeypress works"));
        assertTrue(selenium.isTextPresent("keypress"));

        //ondblclick
        element("formID:doubleclickID").doubleClick();
        assertTrue(selenium.isTextPresent("ondblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        //onmousedown
        element("formID:mousedownID").mouseDown();
        assertTrue(selenium.isTextPresent("onmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //onmouseover
        element("formID:mouseoverID").mouseOver();
        assertTrue(selenium.isTextPresent("onmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //onmousemove
        element("formID:mousemoveID").mouseMove();
        assertTrue(selenium.isTextPresent("onmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));

        //onmouseout
        element("formID:mouseoutID").mouseOut();
        assertTrue(selenium.isTextPresent("onmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //onmouseup
        element("formID:mouseupID").mouseUp();
        assertTrue(selenium.isTextPresent("onmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        //todo: uncomment if JSFC-1431 is fixed
/*    //onfocus
    assertTrue(selenium.isTextPresent("onfocus works"));
    assertTrue(selenium.isTextPresent("focus"));

    //onblur
    assertTrue(selenium.isTextPresent("onblur works"));
    assertTrue(selenium.isTextPresent("blur"));*/

        //onkeydown
        DateChooserInspector keyDownDateChooser = dateChooser("formID:keydownID");
        keyDownDateChooser.field().keyDown(KeyEvent.VK_Z);
        assertTrue(selenium.isTextPresent("onkeydown works"));
        assertTrue(selenium.isTextPresent("keydown"));

        //onkeyup
        DateChooserInspector keyUpDateChooser = dateChooser("formID:keyupID");
        keyUpDateChooser.field().keyUp(KeyEvent.VK_Z);
        assertTrue(selenium.isTextPresent("onkeyup works"));
        assertTrue(selenium.isTextPresent("keyup"));

        //onchange
        DateChooserInspector changeInputText = dateChooser("formID:changeID");
        changeInputText.field().type("Aug 01, 2007");
        assertTrue(selenium.isTextPresent("onchange works"));
        assertTrue(selenium.isTextPresent("change"));
    }

}