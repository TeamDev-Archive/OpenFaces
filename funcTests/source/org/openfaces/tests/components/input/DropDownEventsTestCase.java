/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2015, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to 
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.tests.components.input;

import org.apache.commons.lang3.StringUtils;
import org.inspector.api.DropDown;
import org.inspector.css.BaseActionLabels;
import org.inspector.navigator.FuncTestsPages;
import org.openfaces.tests.common.BaseSeleniumTest;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Max Yurin
 */
public class DropDownEventsTestCase extends BaseSeleniumTest {
    private static final FuncTestsPages PAGE = FuncTestsPages.DROPDOWN_FIELD;
    private static final String DROP_DOWN_ID = "formID:dropDownEvents";
    private static final String REPORT_BLOCK_ID = "empty";

    @Test(groups = {"component"})
    public void testKeyPress() {
        navigateTo(PAGE);

        final DropDown dropDown = getControlFactory().getDropDown(DROP_DOWN_ID);
        dropDown.inputField().setValue("r");

        assertEventFired(BaseActionLabels.ON_KEY_PRESS);
    }

    @Test(groups = {"component"})
    public void testClick() {
        navigateTo(PAGE);

        final DropDown dropDown = getControlFactory().getDropDown(DROP_DOWN_ID);
        dropDown.click();
        assertEventFired(BaseActionLabels.ON_CLICK);

        refreshPage();
        dropDown.inputField().click();
        assertEventFired(BaseActionLabels.ON_CLICK);

        refreshPage();
        dropDown.button().click();
        assertEventFired(BaseActionLabels.ON_CLICK);
    }

    @Test(groups = {"component"})
    public void testMouseDown() {
        navigateTo(PAGE);

        final DropDown dropDown = getControlFactory().getDropDown(DROP_DOWN_ID);
        dropDown.mouse().mouseDown();
        assertEventFired(BaseActionLabels.ON_MOUSE_DOWN);

        dropDown.inputField().mouse().mouseDown();
        assertEventFired(BaseActionLabels.ON_MOUSE_DOWN);
    }

    @Test(groups = {"component"})
    public void testMouseUp() {
        navigateTo(PAGE);

        final DropDown dropDown = getControlFactory().getDropDown(DROP_DOWN_ID);
        dropDown.mouse().mouseUp();
        assertEventFired(BaseActionLabels.ON_MOUSE_UP);

        dropDown.inputField().mouse().mouseUp();
        assertEventFired(BaseActionLabels.ON_MOUSE_UP);
    }

    @Test(groups = {"component"})
    public void testFocus() {
        navigateTo(PAGE);

        final DropDown dropDown = getControlFactory().getDropDown(DROP_DOWN_ID);
        dropDown.mouse().focus();
        assertEventFired(BaseActionLabels.ON_CLICK);

        dropDown.inputField().mouse().focus();
        assertEventFired(BaseActionLabels.ON_CLICK);

        dropDown.button().click();
        assertEventFired(BaseActionLabels.ON_CLICK);
    }

    @Test(groups = {"component"})
    public void testDblClick() {
        navigateTo(PAGE);

        final DropDown dropDown = getControlFactory().getDropDown(DROP_DOWN_ID);
        dropDown.mouse().doubleClick();
        assertEventFired(BaseActionLabels.ON_MOUSE_UP);

        dropDown.inputField().mouse().doubleClick();
        assertEventFired(BaseActionLabels.ON_MOUSE_UP);
    }

    @Test(groups = {"component"})
    public void testMouseOut() {
        navigateTo(PAGE);

        final DropDown dropDown = getControlFactory().getDropDown(DROP_DOWN_ID);
        dropDown.mouse().mouseOver();
        assertEventFired(BaseActionLabels.ON_MOUSE_OVER);

        dropDown.inputField().mouse().mouseMove(findBy(REPORT_BLOCK_ID));
        assertEventFired(BaseActionLabels.ON_MOUSE_OUT);
    }

    @Test(groups = {"component"})
    public void testMouseMove() {
        navigateTo(PAGE);
        findBy(REPORT_BLOCK_ID).getText();

        final DropDown dropDown = getControlFactory().getDropDown(DROP_DOWN_ID);
        dropDown.mouse().mouseOver();
        assertEventFired(BaseActionLabels.ON_MOUSE_MOVE);
    }

    @Test(groups = {"component"})
    public void testChange() {
        navigateTo(PAGE);
    }

    @Test(groups = {"component"})
    public void testKeyUp() {
        navigateTo(PAGE);

        final DropDown dropDown = getControlFactory().getDropDown(DROP_DOWN_ID);
        dropDown.inputField().keyboard().keyUp(Keys.CONTROL, Keys.DOWN);

        assertEventFired(BaseActionLabels.ON_KEY_UP);
    }

    @Test(groups = {"component"})
    public void testKeyDown() {
        navigateTo(PAGE);

        final DropDown dropDown = getControlFactory().getDropDown(DROP_DOWN_ID);
        dropDown.inputField().keyboard().keyDown(Keys.DOWN);

        assertEventFired(BaseActionLabels.ON_KEY_DOWN);
    }

    private void assertEventFired(BaseActionLabels event) {
        final WebElement reportBlock = findBy(REPORT_BLOCK_ID);
        final String text = reportBlock.getText();
        final String eventName = event.getValue();

        assertThat("Checking report block, should contains: "
                        + eventName + ". But it is: " + (StringUtils.isEmpty(text) ? "empty" : text),
                text.contains(eventName));
    }
}
