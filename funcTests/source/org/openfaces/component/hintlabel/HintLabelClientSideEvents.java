/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.hintlabel;

import com.thoughtworks.selenium.Selenium;
import org.junit.Ignore;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.openfaces.HintLabelInspector;

/**
 * @author Darya Shumilina
 */
public class HintLabelClientSideEvents extends OpenFacesTestCase {
    @Test
    public void testHintLabelClientSideEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/hintlabel/hintLabel.jsf");

        //onclick
        selenium.click("formID:clickID");
        assertTrue(selenium.isTextPresent("onclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //ondblclick
        selenium.doubleClick("formID:doubleclickID");
        assertTrue(selenium.isTextPresent("ondblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        //onmousedown
        selenium.mouseDown("formID:mousedownID");
        assertTrue(selenium.isTextPresent("onmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //onmouseover
        selenium.mouseOver("formID:mouseoverID");
        assertTrue(selenium.isTextPresent("onmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //onmouseup
        selenium.mouseUp("formID:mouseupID");
        assertTrue(selenium.isTextPresent("onmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        //onmouseout
        selenium.mouseOut("formID:mouseoutID");
        assertTrue(selenium.isTextPresent("onmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //onmousemove
        selenium.mouseMove("formID:mousemoveID");
        assertTrue(selenium.isTextPresent("onmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));

    }

    //todo: uncomment this test if JSFC-1440 fixed
    @Ignore
    @Test
    public void _testHintLabelTooltipPartClientSideEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/hintlabel/hintLabel.jsf");

        //onclick
        HintLabelInspector clickHintLabel = hintLabel("formID:clickID");
        clickHintLabel.showHintLabel();
        clickHintLabel.hint().click();
        assertTrue(selenium.isTextPresent("onclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //ondblclick
        HintLabelInspector doubleClickHintLabel = hintLabel("formID:doubleclickID");
        doubleClickHintLabel.showHintLabel();
        doubleClickHintLabel.doubleClick();
        assertTrue(selenium.isTextPresent("ondblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        //onmousedown
        HintLabelInspector mouseDownHintLabel = hintLabel("formID:mousedownID");
        mouseDownHintLabel.showHintLabel();
        mouseDownHintLabel.hint().mouseDown();
        assertTrue(selenium.isTextPresent("onmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //onmouseover
        HintLabelInspector mouseOverHintLabel = hintLabel("formID:mouseoverID");
        mouseOverHintLabel.showHintLabel();
        mouseOverHintLabel.hint().mouseOver();
        assertTrue(selenium.isTextPresent("onmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //onmouseup
        HintLabelInspector mouseUpHintLabel = hintLabel("formID:mouseupID");
        mouseUpHintLabel.showHintLabel();
        mouseUpHintLabel.hint().mouseUp();
        assertTrue(selenium.isTextPresent("onmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        //onmouseout
        HintLabelInspector mouseOutHintLabel = hintLabel("formID:mouseoutID");
        mouseOutHintLabel.showHintLabel();
        mouseOutHintLabel.hint().mouseOut();
        assertTrue(selenium.isTextPresent("onmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //onmousemove
        HintLabelInspector mouseMoveHintLabel = hintLabel("formID:mousemoveID");
        mouseMoveHintLabel.showHintLabel();
        mouseMoveHintLabel.hint().mouseMove();
        assertTrue(selenium.isTextPresent("onmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));
    }


}