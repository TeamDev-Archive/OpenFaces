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

package org.openfaces.tests.components.dataTable;

import org.apache.commons.lang3.StringUtils;
import org.inspector.api.Table;
import org.inspector.components.table.TableCell;
import org.inspector.css.BaseActionLabels;
import org.inspector.navigator.FuncTestsPages;
import org.openfaces.tests.common.BaseSeleniumTest;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.inspector.css.BaseActionLabels.*;

/**
 * @author Max Yurin
 */
public class DataTableTestClientSideEvents extends BaseSeleniumTest {
    private static final FuncTestsPages PAGE = FuncTestsPages.DATATABLE_EVENTS;

    private static final String FIRST_TABLE_ID = "fn:first";
    private static final String SECOND_TABLE_ID = "fn:second";
    private static final String THIRD_TABLE_ID = "fn:third";
    private static final String FOURTH_TABLE_ID = "fn:fourth";
    private static final String FIFTH_TABLE_ID = "fn:fifth";
    private static final String SIXTH_TABLE_ID = "fn:sixth";
    private static final String SINGLE_TABLE_ID = "fn:single";
    private static final String MULTIPLY_TABLE_ID = "fn:multiple";

    private static final String REPORT_BLOCK_ID = "empty";

    @Test(groups = {"component", "events"})
    public void testMouseUp() {
        navigateTo(PAGE);
        final Table table = getControlFactory().getDataTable(FIRST_TABLE_ID);
        table.mouse().mouseUp();

        assertEventFired(ON_MOUSE_UP);
    }

    @Test(groups = {"component", "events"})
    public void testMouseDown() {
        navigateTo(PAGE);
        final Table table = getControlFactory().getDataTable(FIRST_TABLE_ID);
        table.mouse().mouseDown();

        assertEventFired(ON_MOUSE_DOWN);
    }

    @Test(groups = {"component", "events"})
    public void testMouseOver() {
        navigateTo(PAGE);
        final Table thirdTable = getControlFactory().getDataTable(THIRD_TABLE_ID);
        thirdTable.mouse().mouseOver();

        assertEventFired(ON_MOUSE_OVER);
    }

    @Test(groups = {"component", "events"})
    public void testClick() {
        navigateTo(PAGE);
        final Table table = getControlFactory().getDataTable(FIRST_TABLE_ID);
        table.click();

        assertEventFired(ON_CLICK);
    }

    @Test(groups = {"component", "events"})
    public void testDblClick() {
        navigateTo(PAGE);
        final Table secondTable = getControlFactory().getDataTable(SECOND_TABLE_ID);
        secondTable.mouse().doubleClick();

        assertEventFired(ON_DOUBLE_CLICK);
    }

    //TODO: (Yurin) Enable when will be able feature with DataTable focusable
    @Test(groups = {"component", "events"})
    public void testFocus() {
        navigateTo(PAGE);
        final Table sixthTable = getControlFactory().getDataTable(SIXTH_TABLE_ID);
        sixthTable.body().row(2).cell(3).element().click();

        assertEventFired(ON_FOCUS);
    }

    //TODO: (Yurin) Enable when will be able feature with DataTable focusable
    @Test(groups = {"component", "events"}, enabled = false)
    public void testBlur() {
        navigateTo(PAGE);
        final Table sixthTable = getControlFactory().getDataTable(SIXTH_TABLE_ID);
        sixthTable.mouse().focus();
        assertEventFired(ON_FOCUS);

        sixthTable.mouse().blur();
        assertEventFired(ON_BLUR);
    }

    @Test(groups = {"component", "events"}, enabled = false)
    public void testMouseOut() {
        navigateTo(PAGE);
        final Table sixthTable = getControlFactory().getDataTable(SIXTH_TABLE_ID);
        final Table thirdTable = getControlFactory().getDataTable(FIFTH_TABLE_ID);

        sixthTable.mouse().mouseMove(thirdTable.element());

        assertEventFired(ON_MOUSE_OUT);
    }

    @Test(groups = {"component", "events"})
    public void testKeyPress_SingleSelectionTable() {
        navigateTo(PAGE);
        final Table singleTable = getControlFactory().getDataTable(SINGLE_TABLE_ID);
        final TableCell cell = singleTable.body().nextRow().nextCell();
        cell.click();
        cell.keyboard().keyPress(Keys.DOWN);

        assertEventFired(ON_CHANGE);
    }

    @Test(groups = {"component", "events"})
    public void testKeyPress_MultiplySelectionTable() {
        navigateTo(PAGE);
        final Table singleTable = getControlFactory().getDataTable(MULTIPLY_TABLE_ID);
        final TableCell cell = singleTable.body().nextRow().nextCell();
        cell.click();
        singleTable.keyboard().keyUp(Keys.CONTROL, Keys.DOWN);

        assertEventFired(ON_CHANGE);
    }

    @Test(groups = {"component", "events"})
    public void testKeyUp_MultiplySelectionTable() {
        navigateTo(PAGE);
        final Table multiplyTable = getControlFactory().getDataTable(MULTIPLY_TABLE_ID);
        multiplyTable.click();
        multiplyTable.keyboard().keyUpWithShiftPressed(Keys.DOWN);

        assertEventFired(ON_CHANGE);
    }

    @Test(groups = {"component", "events"})
    public void testKeyDown_SingleSelectionTable() {
        navigateTo(PAGE);
        final Table singleTable = getControlFactory().getDataTable(SINGLE_TABLE_ID);
        singleTable.body().nextRow().nextCell().keyboard().keyDown(Keys.DOWN);

        assertEventFired(ON_CHANGE);
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
