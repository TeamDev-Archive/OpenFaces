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
package org.openfaces.component.treetable;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.openfaces.TreeTableInspector;
import org.seleniuminspector.ElementInspector;

import java.awt.event.KeyEvent;

/**
 * @author Darya Shumilina
 */
public class TreeTableClientSideEvents extends OpenFacesTestCase {

    /*test entire TreeTable*/
    //TODO Dont't work warn
     //@Test
    public void testCommonTreeTableEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/treetable/treeTable_events.jsf");

        //onfocus
        assertTrue(selenium.isTextPresent("onfocus works"));
        //todo: uncomment if JSFC-1453 fixed
        //assertTrue(selenium.isTextPresent("focus"));

        ElementInspector treeTable = element("fn:eventsTreeTable");
        //onclick
        treeTable.click();
        assertTrue(selenium.isTextPresent("onclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //onblur
        //todo: selenium can set focus to the several page elements in own FF instance, so event not fired. Problem unresolved yet.
/*    assertTrue(selenium.isTextPresent("onblur works"));
    assertTrue(selenium.isTextPresent("blur"));*/

        //ondblclick
        treeTable.doubleClick();
        assertTrue(selenium.isTextPresent("ondblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        //onkeydown
        treeTable.keyDown(KeyEvent.VK_DOWN);
        assertTrue(selenium.isTextPresent("onkeydown works"));
        assertTrue(selenium.isTextPresent("keydown"));

        //onkeypress
        treeTable.keyPress(KeyEvent.VK_DOWN);
        assertTrue(selenium.isTextPresent("onkeypress works"));
        assertTrue(selenium.isTextPresent("keypress"));

        //onkeyup
        treeTable.keyUp(KeyEvent.VK_DOWN);
        assertTrue(selenium.isTextPresent("onkeyup works"));
        assertTrue(selenium.isTextPresent("keyup"));

        //onmousedown
        treeTable.mouseDown();
        assertTrue(selenium.isTextPresent("onmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //onmouseout
        treeTable.mouseOut();
        assertTrue(selenium.isTextPresent("onmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //onmouseover
        treeTable.mouseOver();
        assertTrue(selenium.isTextPresent("onmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //onmouseup
        treeTable.mouseOver();
        assertTrue(selenium.isTextPresent("onmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //onmousemove
        element("fn:mousemoveTreeTable").mouseMove();
        assertTrue(selenium.isTextPresent("onmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));

    }

    /*test entire TreeTable column events*/
     //@Test
    public void testTreeTableColumnEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/treetable/treeTable_entireColumn.jsf");

        ElementInspector treeTable = element("fn:treeTable");
        //onclick
        treeTable.click();
        assertTrue(selenium.isTextPresent("onclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //ondblclick
        treeTable.doubleClick();
        assertTrue(selenium.isTextPresent("ondblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        //onmousedown
        treeTable.mouseDown();
        assertTrue(selenium.isTextPresent("onmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //onmousemove
        treeTable.mouseMove();
        assertTrue(selenium.isTextPresent("onmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));

        //onmouseout
        treeTable.mouseOut();
        assertTrue(selenium.isTextPresent("onmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //onmouseover
        treeTable.mouseOver();
        assertTrue(selenium.isTextPresent("onmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //onmouseup
        treeTable.mouseUp();
        assertTrue(selenium.isTextPresent("onmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));
    }

    /*test TreeTable column header*/
     //@Test
    public void testTreeTableColumnHeaderEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/treetable/treeColumn_header.jsf");

        ElementInspector headerOutput = element("fn:treeTable:header");
        //onclick
        headerOutput.click();
        assertTrue(selenium.isTextPresent("headerOnclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //ondblclick
        headerOutput.doubleClick();
        assertTrue(selenium.isTextPresent("headerOndblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        //onmousedown
        headerOutput.mouseDown();
        assertTrue(selenium.isTextPresent("headerOnmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //onmousemove
        headerOutput.mouseMove();
        assertTrue(selenium.isTextPresent("headerOnmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));

        //onmouseout
        headerOutput.mouseOut();
        assertTrue(selenium.isTextPresent("headerOnmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //onmouseover
        headerOutput.mouseOver();
        assertTrue(selenium.isTextPresent("headerOnmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //onmouseup
        headerOutput.mouseUp();
        assertTrue(selenium.isTextPresent("headerOnmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));
    }

    /*test TreeTable column body*/
     //@Test
    public void testTreeTableColumnBodyEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/treetable/treeColumn_body.jsf");
        System.out.println();

        ElementInspector bodyOutput = element("fn:treeTable:3:body");      //:0: ?
        //onclick
        bodyOutput.click();
        assertTrue(selenium.isTextPresent("bodyOnclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //ondblclick
        bodyOutput.doubleClick();
        assertTrue(selenium.isTextPresent("bodyOndblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        //onmousedown
        bodyOutput.mouseDown();
        assertTrue(selenium.isTextPresent("bodyOnmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //onmousemove
        bodyOutput.mouseMove();
        assertTrue(selenium.isTextPresent("bodyOnmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));

        //onmouseout
        bodyOutput.mouseOut();
        assertTrue(selenium.isTextPresent("bodyOnmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //onmouseover
        bodyOutput.mouseOver();
        assertTrue(selenium.isTextPresent("bodyOnmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //onmouseup
        bodyOutput.mouseUp();
        assertTrue(selenium.isTextPresent("bodyOnmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));
    }

    /*test TreeTable column footer*/
     //@Test
    public void testTreeTableColumnFooterEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/treetable/treeColumn_footer.jsf");

        ElementInspector footerOutput = element("fn:treeTable:footer");
        //onclick
        footerOutput.click();
        assertTrue(selenium.isTextPresent("footerOnclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //ondblclick
        footerOutput.doubleClick();
        assertTrue(selenium.isTextPresent("footerOndblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        //onmousedown
        footerOutput.mouseDown();
        assertTrue(selenium.isTextPresent("footerOnmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //onmousemove
        footerOutput.mouseMove();
        assertTrue(selenium.isTextPresent("footerOnmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));

        //onmouseout
        footerOutput.mouseOut();
        assertTrue(selenium.isTextPresent("footerOnmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //onmouseover
        footerOutput.mouseOver();
        assertTrue(selenium.isTextPresent("footerOnmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //onmouseup
        footerOutput.mouseUp();
        assertTrue(selenium.isTextPresent("footerOnmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));
    }

    /*test 'onchange' events for the TreeTable selection*/
     //@Test
    public void testOnchangeForTreeTableSelection() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/treetable/TTOnchange_row_events.jsf");

        //single selection
        element("fn:singleSelectionTreeTableID:3:categoryID").click();
        assertTrue(selenium.isTextPresent("single onchange works"));
        assertTrue(selenium.isTextPresent("change"));

        //multiple selection
        element("fn:multipleSelectionTreeTableID:3:categoryID").click();
        assertTrue(selenium.isTextPresent("multiple onchange works"));
        assertTrue(selenium.isTextPresent("change"));
    }

    /*test row events*/
     //@Test
    public void testRowEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/treetable/TTOnchange_row_events.jsf");

        ElementInspector categoryOutput = element("fn:rowEventsTreeTable:2:categoryID");
        //rowOnclick
        categoryOutput.click();
        assertTrue(selenium.isTextPresent("rowOnclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //rowOndblclick
        categoryOutput.doubleClick();
        assertTrue(selenium.isTextPresent("rowOndblclick works"));
        assertTrue(selenium.isTextPresent("click"));

        //rowOnmousedown
        categoryOutput.mouseDown();
        assertTrue(selenium.isTextPresent("rowOnmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        //rowOnmousemove
        categoryOutput.mouseMove();
        assertTrue(selenium.isTextPresent("rowOnmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));

        //rowOnmouseout
        categoryOutput.mouseOut();
        assertTrue(selenium.isTextPresent("rowOnmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        //rowOnmouseover
        categoryOutput.mouseOver();
        assertTrue(selenium.isTextPresent("rowOnmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        //rowOnmouseup
        categoryOutput.mouseUp();
        assertTrue(selenium.isTextPresent("rowOnmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));
    }

    /*test cell events*/
     //@Test
    public void testCellAndRowEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/treetable/treeTableCellRowsEvents.jsf");

        String cellPath = "tbody[0]/tr[0]/td[0]";

        ElementInspector output = element("output");
        TreeTableInspector onclickTreeTable = treeTable("formID:onclickTT");
        //<o:cell> onclick
        createEvent(onclickTreeTable, cellPath, EventType.MOUSE, "click", 0, false);
        output.assertContainsText("onclick works");
        output.assertContainsText("click");

        //<o:cell> ondblclick
        createEvent(onclickTreeTable, cellPath, EventType.MOUSE, "dblclick", 0, false);
        output.assertContainsText("ondblclick works");
        output.assertContainsText("dblclick");

        ElementInspector output1 = element("output1");
        TreeTableInspector onMouseDownTreeTable = treeTable("formID:onmouseDownUpTT");
        //<o:cell> onmousedown
        createEvent(onMouseDownTreeTable, cellPath, EventType.MOUSE, "mousedown", 0, false);
        output1.assertContainsText("onmousedown works");
        output1.assertContainsText("mousedown");

        //<o:cell> onmouseup
        createEvent(onMouseDownTreeTable, cellPath, EventType.MOUSE, "mouseup", 0, false);
        output1.assertContainsText("onmouseup works");
        output1.assertContainsText("mouseup");

        ElementInspector cell = element("formID:onkeyTT").subElement(cellPath);
        ElementInspector output2 = element("output2");
        TreeTableInspector onkeyTreeTable = treeTable("formID:onkeyTT");
        //<o:cell> onkeydown
        cell.evalExpression("focus()");
        createEvent(onkeyTreeTable, cellPath, EventType.KEY, "keydown", 49, false);
        output2.assertContainsText("onkeydown works");
        output2.assertContainsText("keydown");

        //<o:cell> onkeypress
        cell.evalExpression("focus()");
        createEvent(onkeyTreeTable, cellPath, EventType.KEY, "keypress", 49, false);
        output2.assertContainsText("onkeypress works");
        output2.assertContainsText("keypress");

        //<o:cell> onkeyup
        cell.evalExpression("focus()");
        createEvent(onkeyTreeTable, cellPath, EventType.KEY, "keyup", 49, false);
        output2.assertContainsText("onkeyup works");
        output2.assertContainsText("keyup");

        ElementInspector output3 = element("output3");
        TreeTableInspector onMouseTreeTable = treeTable("formID:onMouseOverOutMoveTT");
        //<o:cell> onmouseover
        createEvent(onMouseTreeTable, cellPath, EventType.MOUSE, "mouseover", 0, false);
        output3.assertContainsText("onmouseover works");
        output3.assertContainsText("mouseover");

        //<o:cell> onmouseout
        createEvent(onMouseTreeTable, cellPath, EventType.MOUSE, "mouseout", 0, false);
        output3.assertContainsText("onmouseout works");
        output3.assertContainsText("mouseout");

        //<o:cell> onmousemove
        createEvent(onMouseTreeTable, cellPath, EventType.MOUSE, "mousemove", 0, false);
        output3.assertContainsText("onmousemove works");
        output3.assertContainsText("mousemove");

        String rowPath = "tbody[0]/tr[0]/td[0]";

        ElementInspector output01 = element("output01");
        TreeTableInspector rowOnclickTreeTable = treeTable("formID:rowOnclickTT");
        //<o:row> onclick
        createEvent(rowOnclickTreeTable, rowPath, EventType.MOUSE, "click", 0, false);
        output01.assertContainsText("row onclick works");
        output01.assertContainsText("row event type: click");

        //<o:row> ondblclick
        createEvent(rowOnclickTreeTable, rowPath, EventType.MOUSE, "dblclick", 0, false);
        output01.assertContainsText("row ondblclick works");
        output01.assertContainsText("row event type: dblclick");

        ElementInspector output11 = element("output11");
        TreeTableInspector rowOnmouseDownUp = treeTable("formID:rowOnmouseDownUpTT");
        //<o:row> onmousedown
        createEvent(rowOnmouseDownUp, rowPath, EventType.MOUSE, "mousedown", 0, false);
        output11.assertContainsText("row onmousedown works");
        output11.assertContainsText("row event type: mousedown");

        //<o:row> onmouseup
        createEvent(rowOnmouseDownUp, rowPath, EventType.MOUSE, "mouseup", 0, false);
        output11.assertContainsText("row onmouseup works");
        output11.assertContainsText("row event type: mouseup");

        ElementInspector output21 = element("output21");
        TreeTableInspector rowOnkeyTreeTable = treeTable("formID:rowOnkeyTT");
        //<o:row> onkeydown
        ElementInspector row = rowOnclickTreeTable.subElement(rowPath);
        row.evalExpression("focus()");
        createEvent(rowOnkeyTreeTable, rowPath, EventType.KEY, "keydown", 49, false);
        output21.assertContainsText("row onkeydown works");
        output21.assertContainsText("row event type: keydown");

        //<o:row> onkeypress
        row.evalExpression("focus()");
        createEvent(rowOnkeyTreeTable, rowPath, EventType.KEY, "keypress", 49, false);
        output21.assertContainsText("row onkeypress works");
        output21.assertContainsText("row event type: keypress");

        //<o:row> onkeyup
        row.evalExpression("focus()");
        createEvent(rowOnkeyTreeTable, rowPath, EventType.KEY, "keyup", 49, false);
        output21.assertContainsText("row onkeyup works");
        output21.assertContainsText("row event type: keyup");

        ElementInspector output31 = element("output31");
        TreeTableInspector rowOnMouseOverOutMove = treeTable("formID:rowOnMouseOverOutMoveTT");
        //<o:row> onmouseover
        createEvent(rowOnMouseOverOutMove, rowPath, EventType.MOUSE, "mouseover", 0, false);
        output31.assertContainsText("row onmouseover works");
        output31.assertContainsText("row event type: mouseover");

        //<o:row> onmouseout
        createEvent(rowOnMouseOverOutMove, rowPath, EventType.MOUSE, "mouseout", 0, false);
        output31.assertContainsText("row onmouseout works");
        output31.assertContainsText("row event type: mouseout");

        //<o:row> onmousemove
        createEvent(rowOnMouseOverOutMove, rowPath, EventType.MOUSE, "mousemove", 0, false);
        output31.assertContainsText("row onmousemove works");
        output31.assertContainsText("row event type: mousemove");
    }
}