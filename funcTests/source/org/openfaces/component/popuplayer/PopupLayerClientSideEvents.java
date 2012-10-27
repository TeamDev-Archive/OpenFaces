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
package org.openfaces.component.popuplayer;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.openfaces.InputTextInspector;

import java.awt.event.KeyEvent;

/**
 * @author Darya Shumilina
 */
public class PopupLayerClientSideEvents extends OpenFacesTestCase {
    @Test
    public void testPopupLayerClientSideEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/popuplayer/popupLayer.jsf");

        //onclick
        element("formID:onclickShowID").click();
        element("formID:hideOnclickPopupID").click();
        assertTrue(selenium.isTextPresent("onclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //onkeydown
        element("formID:onkeydownShowID").click();
        InputTextInspector keyDownInput = inputText("formID:keydown_input");
        keyDownInput.keyDown(KeyEvent.VK_M);
        assertTrue(selenium.isTextPresent("onkeydown works"));
        assertTrue(selenium.isTextPresent("keydown"));

        //ondblclick
        element("formID:ondblclickShowID").click();
        element("formID:ondblclickPopup").doubleClick();
        assertTrue(selenium.isTextPresent("ondblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        //onmouseup
        element("formID:onmouseupShowID").click();
        element("formID:onmouseupPopup").mouseUp();
        assertTrue(selenium.isTextPresent("onmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        //onmouseover
        element("formID:onmouseoverShowID").click();
        element("formID:onmouseoverPopup").mouseOver();
        assertTrue(selenium.isTextPresent("onmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //onmouseout
        element("formID:onmouseoutShowID").click();
        element("formID:onmouseoutPopup").mouseOut();
        assertTrue(selenium.isTextPresent("onmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //onmousemove
        element("formID:onmousemoveShowID").click();
        element("formID:onmousemovePopup").mouseMove();
        assertTrue(selenium.isTextPresent("onmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));

        //onmousedown
        element("formID:onmousedownShowID").click();
        element("formID:onmousedownPopup").mouseDown();
        assertTrue(selenium.isTextPresent("onmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //onkeyup
        selenium.click("formID:onkeyupShowID");
        InputTextInspector keyUpInput = inputText("formID:keyup_input");
        keyUpInput.keyUp(KeyEvent.VK_M);
        assertTrue(selenium.isTextPresent("onkeyup works"));
        assertTrue(selenium.isTextPresent("keyup"));

        //onkeypress
        element("formID:onkeypressShowID").click();
        InputTextInspector keyPressInput = inputText("formID:keypress_input");
        keyPressInput.keyPress(KeyEvent.VK_M);
        assertTrue(selenium.isTextPresent("onkeypress works"));
        assertTrue(selenium.isTextPresent("keypress"));

        //todo: implement checking of this two client-side events
/*
    //ondragend

    //ondragstart
*/

        //onhide
        element("formID:onhideShowID").click();
        element("formID:hidePopupID").clickAndWait();
        assertTrue(selenium.isTextPresent("onhide works"));
        //todo: uncomment if JSFC-1444 and JSFC-1445 fixed
/*    assertTrue(selenium.isTextPresent("hide"));*/
        //onshow                           `
        element("formID:onshowShowID").clickAndWait();
        element("formID:hideShowPopupID").click();

        assertTrue(selenium.isTextPresent("onshow works"));
        //todo: uncomment if JSFC-1444 and JSFC-1445 fixed
/*    assertTrue(selenium.isTextPresent("show"));*/
    }

}