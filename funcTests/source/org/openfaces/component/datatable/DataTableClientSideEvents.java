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
package org.openfaces.component.datatable;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.html.TableCellInspector;
import org.seleniuminspector.html.TableInspector;

import java.awt.event.KeyEvent;

/**
 * @author Darya Shumilina
 */
public class DataTableClientSideEvents extends OpenFacesTestCase {

    /*test for checking client-side events for the entire DataTable*/
     @Test
    public void testCommonDataTableEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/dataTable_events.jsf");

        ElementInspector first = element("fn:first");
        //onclick
        first.click();
        assertTrue(selenium.isTextPresent("onclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //onmousedown
        first.mouseDown();
        assertTrue(selenium.isTextPresent("onmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //onmouseup
        first.mouseUp();
        assertTrue(selenium.isTextPresent("onmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        ElementInspector secondSecondColumn = element("fn:second:1:secondColumn_body");
        //onkeydown
        secondSecondColumn.keyDown(KeyEvent.VK_RIGHT);
        assertTrue(selenium.isTextPresent("onkeydown works"));
        assertTrue(selenium.isTextPresent("keydown"));

        //onkeyup
        secondSecondColumn.keyUp(KeyEvent.VK_RIGHT);
        assertTrue(selenium.isTextPresent("onkeyup works"));
        assertTrue(selenium.isTextPresent("keyup"));

        //onkeypress
        secondSecondColumn.keyPress(KeyEvent.VK_RIGHT);
        assertTrue(selenium.isTextPresent("onkeypress works"));
        assertTrue(selenium.isTextPresent("keypress"));

        //ondblclick
        element("fn:second").doubleClick();
        assertTrue(selenium.isTextPresent("ondblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        //onmouseover
        element("fn:third").mouseOver();
        assertTrue(selenium.isTextPresent("onmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //onmousemove
        element("fn:fourth").mouseMove();
        assertTrue(selenium.isTextPresent("onmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));

        //onmouseout
        element("fn:fifth").mouseOut();
        assertTrue(selenium.isTextPresent("onmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));
    }

    /*check onfocus and onblur events for the entire DataTable*/
     @Test
    public void testOnblurOnfocusEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/OnfocusOnblurEvents.jsf");
        waitForPageToLoad();
        selenium.click("fn:sixth:8:firstColumn_body");
        selenium.getEval("var el = O$('click'); el.focus();");
        assertTrue(selenium.isTextPresent("focus"));
        assertTrue(selenium.isTextPresent("blur"));
        assertTrue(selenium.isTextPresent("onfocus works"));
        assertTrue(selenium.isTextPresent("onblur works"));
    }

    /*test that checks client-side events for the entire DataTable column*/
     @Test
    public void testDataTableEntireColumnEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/column_entire_column.jsf");

        // onclick
        dataTable("fn:first").body().row(0).cell(0).click();
        assertTrue(selenium.isTextPresent("onclick works"));
        assertTrue(selenium.isTextPresent("click"));

        // ondblclick
        dataTable("fn:second").body().row(0).cell(0).doubleClick();
        assertTrue(selenium.isTextPresent("ondblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        TableCellInspector thirdTableCell = dataTable("fn:third").body().row(0).cell(0);
        // onmousedown
        thirdTableCell.mouseDown();
        assertTrue(selenium.isTextPresent("onmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        // onmouseup
        thirdTableCell.mouseUp();
        assertTrue(selenium.isTextPresent("onmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        // onmouseover
        dataTable("fn:fourth").body().row(0).cell(0).mouseOver();
        assertTrue(selenium.isTextPresent("onmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        // onmouseout
        dataTable("fn:fifth").body().row(0).cell(0).mouseOut();
        assertTrue(selenium.isTextPresent("onmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        // onmousemove
        dataTable("fn:sixth").body().row(0).cell(0).mouseMove();
        assertTrue(selenium.isTextPresent("onmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));
    }

    /*test for DataTable column header client-side events*/
     @Test
    public void testDataTableColumnHeaderEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/column_columnHeader.jsf");

        //headeronclick
        element("fn:clickDT:header").click();
        assertTrue(selenium.isTextPresent("headeronclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //headerondblclick
        element("fn:dblclickDT:header").doubleClick();
        assertTrue(selenium.isTextPresent("headerondblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        ElementInspector mousedownDT = element("fn:mousedownDT:header");
        //headeronmousedown
        mousedownDT.mouseDown();
        assertTrue(selenium.isTextPresent("headeronmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //headeronmouseup
        mousedownDT.mouseUp();
        assertTrue(selenium.isTextPresent("headeronmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        //headeronmouseover
        element("fn:mouseoverDT:header").mouseOver();
        assertTrue(selenium.isTextPresent("headeronmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //headeronmouseout
        element("fn:mouseoutDT:header").mouseOut();
        assertTrue(selenium.isTextPresent("headeronmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //headeronmousemove
        element("fn:mousemoveDT:header").mouseMove();
        assertTrue(selenium.isTextPresent("headeronmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));
    }

    /*test for DataTable column body client-side events*/
     @Test
    public void testDataTableColumnBodyEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/column_columnBoby.jsf");

        //bodyOnclick
        element("fn:clickDT:4:body").click();
        assertTrue(selenium.isTextPresent("bodyOnclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //bodyOndblclick
        element("fn:dblclickDT:3:body").doubleClick();
        assertTrue(selenium.isTextPresent("bodyOndblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        ElementInspector mousedownDtBody = element("fn:mousedownDT:4:body");
        //bodyOnmousedown
        mousedownDtBody.mouseDown();
        assertTrue(selenium.isTextPresent("bodyOnmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //bodyOnmouseup
        mousedownDtBody.mouseUp();
        assertTrue(selenium.isTextPresent("bodyOnmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        //bodyOnmouseover
        element("fn:mouseoverDT:3:body").mouseOver();
        assertTrue(selenium.isTextPresent("bodyOnmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //bodyOnmouseout
        element("fn:mouseoutDT:5:body").mouseOut();
        assertTrue(selenium.isTextPresent("bodyOnmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //bodyOnmousemove
        element("fn:mousemoveDT:3:body").mouseMove();
        assertTrue(selenium.isTextPresent("bodyOnmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));
    }

    /*test for DataTable column footer client-side events*/
     @Test
    public void testDataTableColumnFooterEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/column_columnFooter.jsf");

        // footerOnclick
        element("fn:clickDT:footer").click();
        assertTrue(selenium.isTextPresent("footerOnclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //footerOndblclick
        element("fn:dblclickDT:footer").doubleClick();
        assertTrue(selenium.isTextPresent("footerOndblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        ElementInspector mousedownDtFooter = element("fn:mousedownDT:footer");
        //footerOnmousedown
        mousedownDtFooter.mouseDown();
        assertTrue(selenium.isTextPresent("footerOnmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //footerOnmouseup
        mousedownDtFooter.mouseUp();
        assertTrue(selenium.isTextPresent("footerOnmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        // footerOnmouseover
        element("fn:mouseoverDT:footer").mouseOver();
        assertTrue(selenium.isTextPresent("footerOnmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //footerOnmouseout
        element("fn:mouseoutDT:footer").mouseOut();
        assertTrue(selenium.isTextPresent("footerOnmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //footerOnmousemove
        element("fn:mousemoveDT:footer").mouseMove();
        assertTrue(selenium.isTextPresent("footerOnmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));
    }

    /*test for DataTable checkbox column client-side events*/
     @Test
    public void testDataTableCheckboxColumnEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/checkbox_entire_column.jsf");

        //onclick

        dataTable("fn:clickDT").column(0).bodyCell(5).click();
        //todo: uncomment it if JSFC-1419 fixed
/*    assertTrue(selenium.isTextPresent("onclick works"));
    assertTrue(selenium.isTextPresent("click"));*/

        //ondblclick
        dataTable("fn:dblclickDT").column(0).bodyCell(4).doubleClick();
        //todo: uncomment it if JSFC-1419 fixed
        /*assertTrue(selenium.isTextPresent("ondblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));*/

        ElementInspector mousedownDtChecked = dataTable("fn:mousedownDT").column(0).bodyCell(4);
        //onmousedown
        mousedownDtChecked.mouseDown();
        assertTrue(selenium.isTextPresent("onmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //onmouseup
        mousedownDtChecked.mouseUp();
        assertTrue(selenium.isTextPresent("onmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        //onmouseover
        dataTable("fn:mouseoverID").column(0).bodyCell(0).mouseOver();
        assertTrue(selenium.isTextPresent("onmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //onmouseout
        dataTable("fn:mouseoutDT").column(0).bodyCell(3).mouseOut();
        assertTrue(selenium.isTextPresent("onmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //onmousemove
        dataTable("fn:mousemoveDT").column(0).bodyCell(4).mouseMove();
        assertTrue(selenium.isTextPresent("onmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));
    }

    /*test for DataTable checkbox column header client-side events*/
     @Test
    public void testDataTableCheckboxColumnHeaderEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/checkbox_columnHeader.jsf");

        // headeronclick
        dataTable("fn:clickDT").column(0).headerCell(0).click();
        assertTrue(selenium.isTextPresent("headerOnclick works"));
        assertTrue(selenium.isTextPresent("click"));

        // headerondblclick
        dataTable("fn:dblclick").column(0).headerCell(0).doubleClick();
        assertTrue(selenium.isTextPresent("headerOndblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        TableInspector mousedownDt = dataTable("fn:mousedownDT");
        // headeronmousedown
        mousedownDt.column(0).headerCell(0).mouseDown();
        assertTrue(selenium.isTextPresent("headerOnmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        // headeronmouseup
        mousedownDt.column(0).headerCell(0).mouseUp();
        assertTrue(selenium.isTextPresent("headerOnmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        // headeronmouseover
        dataTable("fn:mouseoverDT").column(0).headerCell(0).mouseOver();
        assertTrue(selenium.isTextPresent("headerOnmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        // headeronmouseout
        dataTable("fn:mouseoutDT").column(0).headerCell(0).mouseOut();
        assertTrue(selenium.isTextPresent("headerOnmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        // headeronmousemove
        dataTable("fn:mousemoveDT").column(0).headerCell(0).mouseMove();
        assertTrue(selenium.isTextPresent("headerOnmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));
    }

    /* test for DataTable checkbox column footer client-side events */
     @Test
    public void testDataTableCheckboxColumnFooterEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/checkbox_columnFooter.jsf");

        // footeronclick
        dataTable("fn:clickDT").column(0).footerCell(0).click();
        assertTrue(selenium.isTextPresent("footerOnclick works"));
        assertTrue(selenium.isTextPresent("click"));

        // footerondblclick
        dataTable("fn:dblclickDT").column(0).footerCell(0).doubleClick();
        assertTrue(selenium.isTextPresent("footerOndblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        TableInspector mousedownDt = dataTable("fn:mousedownDT");
        // footeronmousedown
        mousedownDt.column(0).footerCell(0).mouseDown();
        assertTrue(selenium.isTextPresent("footerOnmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        // footeronmouseup
        mousedownDt.column(0).footerCell(0).mouseUp();
        assertTrue(selenium.isTextPresent("footerOnmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        // footeronmouseover
        dataTable("fn:mouseoverDT").column(0).footerCell(0).mouseOver();
        assertTrue(selenium.isTextPresent("footerOnmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        // footeronmouseout
        dataTable("fn:mouseoutDT").column(0).footerCell(0).mouseOut();
        assertTrue(selenium.isTextPresent("footerOnmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        // footeronmousemove
        dataTable("fn:mousemoveDT").column(0).footerCell(0).mouseMove();
        assertTrue(selenium.isTextPresent("footerOnmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));
    }

    /* test for DataTable checkbox column body client-side events */
     @Test
    public void testDataTableCheckboxColumnBodyEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/checkbox_columnBoby.jsf");

        // bodyonclick
        dataTable("fn:clickDT").column(0).bodyCell(5).click();
        //todo: uncomment it if JSFC-1419 fixed
/*    assertTrue(selenium.isTextPresent("bodyOnclick works"));
    assertTrue(selenium.isTextPresent("click"));*/

        // bodyondblclick
        dataTable("fn:dblclickDT").column(0).bodyCell(4).doubleClick();
        //todo: uncomment it if JSFC-1419 fixed
        /*assertTrue(selenium.isTextPresent("bodyOndblclick  works"));
        assertTrue(selenium.isTextPresent("dblclick"));*/

        ElementInspector mousedownDtChecked = dataTable("fn:mousedownDT").column(0).bodyCell(4);
        // bodyonmousedown
        mousedownDtChecked.mouseDown();
        assertTrue(selenium.isTextPresent("bodyOnmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        // bodyonmouseup
        mousedownDtChecked.mouseUp();
        assertTrue(selenium.isTextPresent("bodyOnmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        // bodyonmouseover
        dataTable("fn:mouseoverDT").column(0).bodyCell(0).mouseOver();
        assertTrue(selenium.isTextPresent("bodyOnmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        // bodyonmouseout
        dataTable("fn:mouseoutDT").column(0).bodyCell(3).mouseOut();
        assertTrue(selenium.isTextPresent("bodyOnmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        // bodyonmousemove
        dataTable("fn:mousemoveDT").column(0).bodyCell(4).mouseMove();
        assertTrue(selenium.isTextPresent("bodyOnmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));
    }

    /*test for DataTable selection column client-side events*/
     @Test
    public void testDataTableSelectionColumnEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/selection_entire_column.jsf");

        //onclick
        dataTable("fn:clickDT").column(0).bodyCell(4).click();
        assertTrue(selenium.isTextPresent("onclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //ondblclick
        dataTable("fn:dblclickDT").column(0).bodyCell(4).doubleClick();
        assertTrue(selenium.isTextPresent("ondblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        ElementInspector mouseDownDtColOne = dataTable("fn:mousedownDT").column(0).bodyCell(6);
        //onmousedown
        mouseDownDtColOne.mouseDown();
        assertTrue(selenium.isTextPresent("onmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //onmouseup
        mouseDownDtColOne.mouseUp();
        assertTrue(selenium.isTextPresent("onmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        //onmouseover
        dataTable("fn:mouseoverDT").column(0).bodyCell(7).mouseOver();
        assertTrue(selenium.isTextPresent("onmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //onmouseout
        dataTable("fn:mouseoutDT").column(0).bodyCell(4).mouseOut();
        assertTrue(selenium.isTextPresent("onmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //onmousemove
        dataTable("fn:mousemoveDT").column(0).bodyCell(5).mouseMove();
        assertTrue(selenium.isTextPresent("onmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));
    }

    /*test for DataTable selection column header client-side events*/
     @Test
    public void testDataTableSelectionColumnHeaderEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/selection_columnHeader.jsf");

        //headeronclick
        element("fn:clickDT:header").click();
        assertTrue(selenium.isTextPresent("headerOnclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //headerondblclick
        element("fn:dblclickDT:header").doubleClick();
        assertTrue(selenium.isTextPresent("headerOndblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        ElementInspector mousedownDtHeader = element("fn:mousedownDT:header");
        //headeronmousedown
        mousedownDtHeader.mouseDown();
        assertTrue(selenium.isTextPresent("headerOnmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //headeronmouseup
        mousedownDtHeader.mouseUp();
        assertTrue(selenium.isTextPresent("headerOnmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        //headeronmouseover
        element("fn:mouseoverDT:header").mouseOver();
        assertTrue(selenium.isTextPresent("headerOnmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //headeronmouseout
        element("fn:mouseoutDT:header").mouseOut();
        assertTrue(selenium.isTextPresent("headerOnmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //headeronmousemove
        element("fn:mousemoveDT:header").mouseMove();
        assertTrue(selenium.isTextPresent("headerOnmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));

    }

    /*test for DataTable selection column footer client-side events*/
     @Test
    public void testDataTableSelectionColumnFooterEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/selection_columnFooter.jsf");

        //footeronclick
        element("fn:clickDT:footer").click();
        assertTrue(selenium.isTextPresent("footerOnclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //footerondblclick
        element("fn:dblclickDT:footer").doubleClick();
        assertTrue(selenium.isTextPresent("footerOndblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        ElementInspector mouseDownDtFooter = element("fn:mousedownDT:footer");
        //footeronmousedown
        mouseDownDtFooter.mouseDown();
        assertTrue(selenium.isTextPresent("footerOnmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //footeronmouseup
        mouseDownDtFooter.mouseUp();
        assertTrue(selenium.isTextPresent("footerOnmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        //footeronmouseover
        element("fn:mouseoverDT:footer").mouseOver();
        assertTrue(selenium.isTextPresent("footerOnmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //footeronmouseout
        element("fn:mouseoutDT:footer").mouseOut();
        assertTrue(selenium.isTextPresent("footerOnmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //footeronmousemove
        element("fn:mousemoveDT:footer").mouseMove();
        assertTrue(selenium.isTextPresent("footerOnmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));

    }

    /*test for DataTable selection column body client-side events*/
     @Test
    public void testDataTableSelectionColumnBodyEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/selection_columnBoby.jsf");

        //bodyonclick
        dataTable("fn:clickDT").column(0).bodyCell(2).click();
        assertTrue(selenium.isTextPresent("bodyOnclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //bodyondblclick
        dataTable("fn:dblclickDT").column(0).bodyCell(4).doubleClick();
        assertTrue(selenium.isTextPresent("bodyOndblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        ElementInspector mousedownDtColOne = dataTable("fn:mousedownDT").column(0).bodyCell(5);
        //bodyonmousedown
        mousedownDtColOne.mouseDown();
        assertTrue(selenium.isTextPresent("bodyOnmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //bodyonmouseup
        mousedownDtColOne.mouseUp();
        assertTrue(selenium.isTextPresent("bodyOnmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        //bodyonmouseover
        dataTable("fn:mouseoverDT").column(0).bodyCell(3).mouseOver();
        assertTrue(selenium.isTextPresent("bodyOnmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //bodyonmouseout
        dataTable("fn:mouseoutDT").column(0).bodyCell(5).mouseOut();
        assertTrue(selenium.isTextPresent("bodyOnmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //bodyonmousemove
        dataTable("fn:mousemoveDT").column(0).bodyCell(5).mouseMove();
        assertTrue(selenium.isTextPresent("bodyOnmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));
    }

    /*test 'onchange' events for the DataTable selection*/
     @Test
    public void testOnchangeForDataTableSelection() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/selectionOnChange.jsf");

        ElementInspector multiple = dataTable("fn:multiple").column(0).bodyCell(0);
        multiple.click();
        multiple.keyDown(KeyEvent.VK_DOWN);
        assertTrue(selenium.isTextPresent("multiple selection onchange works"));
        assertTrue(selenium.isTextPresent("multiple change"));

        ElementInspector single = dataTable("fn:single").column(0).bodyCell(0);
        single.click();
        single.keyDown(KeyEvent.VK_DOWN);
        assertTrue(selenium.isTextPresent("single selection onchange works"));

        assertTrue(selenium.isTextPresent("single change"));
    }

    /*test row events*/
     @Test
    public void testRowEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/rowEvents.jsf");

        //rowonclick

        dataTable("fn:clickDT").column(0).bodyCell(4).click();
        assertTrue(selenium.isTextPresent("rowOnclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //rowondblclick
        dataTable("fn:dblclickDT").column(0).bodyCell(3).doubleClick();
        assertTrue(selenium.isTextPresent("rowOndblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        ElementInspector mouseDownDtBody = dataTable("fn:mousedownDT").column(0).bodyCell(4);
        //rowOnmousedown
        mouseDownDtBody.mouseDown();
        assertTrue(selenium.isTextPresent("rowOnmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //rowOnmouseup
        mouseDownDtBody.mouseUp();
        assertTrue(selenium.isTextPresent("rowOnmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        //rowOnmouseover
        dataTable("fn:mouseoverDT").column(0).bodyCell(3).mouseOver();
        assertTrue(selenium.isTextPresent("rowOnmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //rowOnmouseout
        dataTable("fn:mouseoutDT").column(0).bodyCell(2).mouseOut();
        assertTrue(selenium.isTextPresent("rowOnmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //rowOnmousemove
        dataTable("fn:mousemoveDT").column(0).bodyCell(6).mouseMove();
        assertTrue(selenium.isTextPresent("rowOnmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));
    }

}