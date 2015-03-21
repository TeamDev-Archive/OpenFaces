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
package org.openfaces.component.borderlayoutpanel;

import org.junit.Ignore;
import org.junit.Test;
import org.openfaces.test.BaseSeleniumTest;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.openfaces.BorderLayoutPanelInspector;
import org.seleniuminspector.openfaces.SidePanelInspector;

/**
 * @author Alexey Tarasyuk
 *         // todo: consider optimizing performance by combinbing several tests that involve various splitter draggings into one test
 */
public class BorderLayoutPanelTest extends BaseSeleniumTest {
     @Test
    @Ignore
    public void testRendering() {
        testAppFunctionalPage("/components/borderlayoutpanel/borderLayoutPanelft.jsf");

//test positions & sizes
        BorderLayoutPanelInspector borderLayoutPanel = borderLayoutPanel("formID:borderLayoutPanel00");
        borderLayoutPanel.assertElementExists();
        borderLayoutPanel.assertExpressionEquals("offsetWidth", 600);
        borderLayoutPanel.assertExpressionEquals("offsetHeight", 400);

        ElementInspector sidePanel01 = element("formID:sidePanel01");
        sidePanel01.assertElementExists();
        sidePanel01.assertStyle("left: 0px; top: 100px");
        sidePanel01.assertExpressionEquals("offsetWidth", 50);
        sidePanel01.assertExpressionEquals("offsetHeight", 200);

        ElementInspector sidePanel02 = element("formID:sidePanel02");
        sidePanel02.assertElementExists();
        sidePanel02.assertStyle("left: 50px; top: 100px");
        sidePanel02.assertExpressionEquals("offsetWidth", 50);
        sidePanel02.assertExpressionEquals("offsetHeight", 200);

        ElementInspector sidePanel03 = element("formID:sidePanel03");
        sidePanel03.assertElementExists();
        sidePanel03.assertStyle("right: 0px; top: 100px");
        sidePanel03.assertExpressionEquals("offsetWidth", 50);
        sidePanel03.assertExpressionEquals("offsetHeight", 200);

        ElementInspector sidePanel04 = element("formID:sidePanel04");
        sidePanel04.assertElementExists();
        sidePanel04.assertStyle("right: 50px; top: 100px");
        sidePanel04.assertExpressionEquals("offsetWidth", 50);
        sidePanel04.assertExpressionEquals("offsetHeight", 200);

        ElementInspector sidePanel05 = element("formID:sidePanel05");
        sidePanel05.assertElementExists();
        sidePanel05.assertStyle("left: 0px; top: 0px");
        sidePanel05.assertExpressionEquals("offsetWidth", 600);
        sidePanel05.assertExpressionEquals("offsetHeight", 50);

        ElementInspector sidePanel06 = element("formID:sidePanel06");
        sidePanel06.assertElementExists();
        sidePanel06.assertStyle("left: 0px; top: 50px");
        sidePanel06.assertExpressionEquals("offsetWidth", 600);
        sidePanel06.assertExpressionEquals("offsetHeight", 50);

        ElementInspector sidePanel07 = element("formID:sidePanel07");
        sidePanel07.assertElementExists();
        sidePanel07.assertStyle("left: 0px; bottom: 0px");
        sidePanel07.assertExpressionEquals("offsetWidth", 600);
        sidePanel07.assertExpressionEquals("offsetHeight", 50);

        ElementInspector sidePanel08 = element("formID:sidePanel08");
        sidePanel08.assertElementExists();
        sidePanel08.assertStyle("left: 0px; bottom: 50px");
        sidePanel08.assertExpressionEquals("offsetWidth", 600);
        sidePanel08.assertExpressionEquals("offsetHeight", 50);

        borderLayoutPanel.content().assertElementExists();
        borderLayoutPanel.content().assertStyle("left: 100px; top: 100px");
        borderLayoutPanel.content().assertExpressionEquals("offsetWidth", 400);
        borderLayoutPanel.content().assertExpressionEquals("offsetHeight", 200);

        ElementInspector contentDiv = element("borderLayoutPanel00Content");
        contentDiv.assertElementExists();
    }

     @Test
    @Ignore
    public void testSidePanelSplitterDragging() {
        testAppFunctionalPage("/components/borderlayoutpanel/borderLayoutPanelft.jsf");

        SidePanelInspector sidePanel01 = sidePanel("formID:sidePanel01");
        sidePanel01.assertExpressionEquals("offsetWidth", 50);
        sidePanel01.splitter().dragAndDrop(+300, 0);
        sidePanel01.assertExpressionEquals("offsetWidth", 350);
        sidePanel01.splitter().dragAndDrop(+125, 0);
        sidePanel01.assertExpressionEquals("offsetWidth", 450);
        sidePanel01.splitter().dragAndDrop(+125, 0);
        sidePanel01.assertExpressionEquals("offsetWidth", 450);
        sidePanel01.splitter().dragAndDrop(-5, 0);
        sidePanel01.assertExpressionEquals("offsetWidth", 445);
        sidePanel01.splitter().dragAndDrop(-1000, 0);
        sidePanel01.assertExpressionEquals("offsetWidth", 7);
        sidePanel01.splitter().dragAndDrop(+43, 0);
        sidePanel01.assertExpressionEquals("offsetWidth", 50);
        sidePanel01.splitter().mouseDown();
        sidePanel01.splitter().mouseUp();
        sidePanel01.assertExpressionEquals("offsetWidth", 7);
        sidePanel01.splitter().mouseDown();
        sidePanel01.splitter().mouseUp();
        sidePanel01.assertExpressionEquals("offsetWidth", 50);

        SidePanelInspector sidePanel04 = sidePanel("formID:sidePanel04");
        sidePanel04.assertExpressionEquals("offsetWidth", 50);
        sidePanel04.splitter().dragAndDrop(-300, 0);
        sidePanel04.assertExpressionEquals("offsetWidth", 350);
        sidePanel04.splitter().dragAndDrop(-125, 0);
        sidePanel04.assertExpressionEquals("offsetWidth", 450);
        sidePanel04.splitter().dragAndDrop(-125, 0);
        sidePanel04.assertExpressionEquals("offsetWidth", 450);
        sidePanel04.splitter().dragAndDrop(+5, 0);
        sidePanel04.assertExpressionEquals("offsetWidth", 445);
        sidePanel04.splitter().dragAndDrop(+1000, 0);
        sidePanel04.assertExpressionEquals("offsetWidth", 7);
        sidePanel04.splitter().dragAndDrop(-43, 0);
        sidePanel04.assertExpressionEquals("offsetWidth", 50);
        sidePanel04.splitter().mouseDown();
        sidePanel04.splitter().mouseUp();
        sidePanel04.assertExpressionEquals("offsetWidth", 7);
        sidePanel04.splitter().mouseDown();
        sidePanel04.splitter().mouseUp();
        sidePanel04.assertExpressionEquals("offsetWidth", 50);

        SidePanelInspector sidePanel05 = sidePanel("formID:sidePanel05");
        sidePanel05.assertExpressionEquals("offsetHeight", 50);
        sidePanel05.splitter().dragAndDrop(0, +100);
        sidePanel05.assertExpressionEquals("offsetHeight", 150);
        sidePanel05.splitter().dragAndDrop(0, +125);
        sidePanel05.assertExpressionEquals("offsetHeight", 250);
        sidePanel05.splitter().dragAndDrop(0, +125);
        sidePanel05.assertExpressionEquals("offsetHeight", 250);
        sidePanel05.splitter().dragAndDrop(0, -5);
        sidePanel05.assertExpressionEquals("offsetHeight", 245);
        sidePanel05.splitter().dragAndDrop(0, -1000);
        sidePanel05.assertExpressionEquals("offsetHeight", 7);
        sidePanel05.splitter().dragAndDrop(0, +43);
        sidePanel05.assertExpressionEquals("offsetHeight", 50);
        sidePanel05.splitter().mouseDown();
        sidePanel05.splitter().mouseUp();
        sidePanel05.assertExpressionEquals("offsetHeight", 7);
        sidePanel05.splitter().mouseDown();
        sidePanel05.splitter().mouseUp();
        sidePanel05.assertExpressionEquals("offsetHeight", 50);

        SidePanelInspector sidePanel08 = sidePanel("formID:sidePanel08");
        sidePanel08.assertExpressionEquals("offsetHeight", 50);
        sidePanel08.splitter().dragAndDrop(0, -100);
        sidePanel08.assertExpressionEquals("offsetHeight", 150);
        sidePanel08.splitter().dragAndDrop(0, -125);
        sidePanel08.assertExpressionEquals("offsetHeight", 250);
        sidePanel08.splitter().dragAndDrop(0, -125);
        sidePanel08.assertExpressionEquals("offsetHeight", 250);
        sidePanel08.splitter().dragAndDrop(0, +5);
        sidePanel08.assertExpressionEquals("offsetHeight", 245);
        sidePanel08.splitter().dragAndDrop(0, +1000);
        sidePanel08.assertExpressionEquals("offsetHeight", 7);
        sidePanel08.splitter().dragAndDrop(0, -43);
        sidePanel08.assertExpressionEquals("offsetHeight", 50);
        sidePanel08.splitter().mouseDown();
        sidePanel08.splitter().mouseUp();
        sidePanel08.assertExpressionEquals("offsetHeight", 7);
        sidePanel08.splitter().mouseDown();
        sidePanel08.splitter().mouseUp();
        sidePanel08.assertExpressionEquals("offsetHeight", 50);
    }

     @Test
    @Ignore
    public void testEvents() {
        testAppFunctionalPage("/components/borderlayoutpanel/borderLayoutPanelft.jsf");
        SidePanelInspector sidePanel = sidePanel("formID:sidePanel01");
        ElementInspector contentText = element("borderLayoutPanel00Content");

        /* test oncontentresize */
        contentText.assertExpressionEquals("innerHTML", "offsetWidth = 400 offsetHeight = 200");
        sidePanel.splitter().dragAndDrop(+100, 0);
        contentText.assertExpressionEquals("innerHTML", "offsetWidth = 300 offsetHeight = 200");
    }

     @Test
    @Ignore
    public void testContentResizeOnSplitterDrag() {
        testAppFunctionalPage("/components/borderlayoutpanel/borderLayoutPanelft.jsf");
        BorderLayoutPanelInspector borderLayoutPanel00 = borderLayoutPanel("formID:borderLayoutPanel00");

        SidePanelInspector sidePanel01 = sidePanel("formID:sidePanel01");
        borderLayoutPanel00.content().assertExpressionEquals("offsetWidth", 400);
        sidePanel01.splitter().dragAndDrop(+300, 0);
        borderLayoutPanel00.content().assertExpressionEquals("offsetWidth", 100);
        sidePanel01.splitter().dragAndDrop(+125, 0);
        borderLayoutPanel00.content().assertExpressionEquals("offsetWidth", 0);
        sidePanel01.splitter().dragAndDrop(+125, 0);
        borderLayoutPanel00.content().assertExpressionEquals("offsetWidth", 0);
        sidePanel01.splitter().dragAndDrop(-5, 0);
        borderLayoutPanel00.content().assertExpressionEquals("offsetWidth", 5);
        sidePanel01.splitter().dragAndDrop(-1000, 0);
        borderLayoutPanel00.content().assertExpressionEquals("offsetWidth", 443);
        sidePanel01.splitter().dragAndDrop(+43, 0);
        borderLayoutPanel00.content().assertExpressionEquals("offsetWidth", 400);
        sidePanel01.splitter().mouseDown();
        sidePanel01.splitter().mouseUp();
        borderLayoutPanel00.content().assertExpressionEquals("offsetWidth", 443);
        sidePanel01.splitter().mouseDown();
        sidePanel01.splitter().mouseUp();
        borderLayoutPanel00.content().assertExpressionEquals("offsetWidth", 400);

        SidePanelInspector sidePanel04 = sidePanel("formID:sidePanel04");
        sidePanel04.splitter().dragAndDrop(-300, 0);
        borderLayoutPanel00.content().assertExpressionEquals("offsetWidth", 100);
        sidePanel04.splitter().dragAndDrop(-125, 0);
        borderLayoutPanel00.content().assertExpressionEquals("offsetWidth", 0);
        sidePanel04.splitter().dragAndDrop(-125, 0);
        borderLayoutPanel00.content().assertExpressionEquals("offsetWidth", 0);
        sidePanel04.splitter().dragAndDrop(+5, 0);
        borderLayoutPanel00.content().assertExpressionEquals("offsetWidth", 5);
        sidePanel04.splitter().dragAndDrop(+1000, 0);
        borderLayoutPanel00.content().assertExpressionEquals("offsetWidth", 443);
        sidePanel04.splitter().dragAndDrop(-43, 0);
        borderLayoutPanel00.content().assertExpressionEquals("offsetWidth", 400);
        sidePanel04.splitter().mouseDown();
        sidePanel04.splitter().mouseUp();
        borderLayoutPanel00.content().assertExpressionEquals("offsetWidth", 443);
        sidePanel04.splitter().mouseDown();
        sidePanel04.splitter().mouseUp();
        borderLayoutPanel00.content().assertExpressionEquals("offsetWidth", 400);

        SidePanelInspector sidePanel05 = sidePanel("formID:sidePanel05");
        borderLayoutPanel00.content().assertExpressionEquals("offsetHeight", 200);
        sidePanel05.splitter().dragAndDrop(0, +100);
        borderLayoutPanel00.content().assertExpressionEquals("offsetHeight", 100);
        sidePanel05.splitter().dragAndDrop(0, +125);
        borderLayoutPanel00.content().assertExpressionEquals("offsetHeight", 0);
        sidePanel05.splitter().dragAndDrop(0, +125);
        borderLayoutPanel00.content().assertExpressionEquals("offsetHeight", 0);
        sidePanel05.splitter().dragAndDrop(0, -5);
        borderLayoutPanel00.content().assertExpressionEquals("offsetHeight", 5);
        sidePanel05.splitter().dragAndDrop(0, -1000);
        borderLayoutPanel00.content().assertExpressionEquals("offsetHeight", 243);
        sidePanel05.splitter().dragAndDrop(0, +43);
        borderLayoutPanel00.content().assertExpressionEquals("offsetHeight", 200);
        sidePanel05.splitter().mouseDown();
        sidePanel05.splitter().mouseUp();
        borderLayoutPanel00.content().assertExpressionEquals("offsetHeight", 243);
        sidePanel05.splitter().mouseDown();
        sidePanel05.splitter().mouseUp();
        borderLayoutPanel00.content().assertExpressionEquals("offsetHeight", 200);

        SidePanelInspector sidePanel08 = sidePanel("formID:sidePanel08");
        sidePanel08.splitter().dragAndDrop(0, -100);
        borderLayoutPanel00.content().assertExpressionEquals("offsetHeight", 100);
        sidePanel08.splitter().dragAndDrop(0, -125);
        borderLayoutPanel00.content().assertExpressionEquals("offsetHeight", 0);
        sidePanel08.splitter().dragAndDrop(0, -125);
        borderLayoutPanel00.content().assertExpressionEquals("offsetHeight", 0);
        sidePanel08.splitter().dragAndDrop(0, +5);
        borderLayoutPanel00.content().assertExpressionEquals("offsetHeight", 5);
        sidePanel08.splitter().dragAndDrop(0, +1000);
        borderLayoutPanel00.content().assertExpressionEquals("offsetHeight", 243);
        sidePanel08.splitter().dragAndDrop(0, -43);
        borderLayoutPanel00.content().assertExpressionEquals("offsetHeight", 200);
        sidePanel08.splitter().mouseDown();
        sidePanel08.splitter().mouseUp();
        borderLayoutPanel00.content().assertExpressionEquals("offsetHeight", 243);
        sidePanel08.splitter().mouseDown();
        sidePanel08.splitter().mouseUp();
        borderLayoutPanel00.content().assertExpressionEquals("offsetHeight", 200);
    }

     @Test
    @Ignore
    public void testContentResizeOnParentResize() {
        testAppFunctionalPage("/components/borderlayoutpanel/borderLayoutPanelft.jsf");
        ElementInspector content1 = borderLayoutPanel("formID:borderLayoutPanel21").content();
        ElementInspector content2 = borderLayoutPanel("formID:borderLayoutPanel22").content();
        ElementInspector splitter1 = sidePanel("formID:sidePanel21").splitter();
        ElementInspector splitter2 = sidePanel("formID:sidePanel22").splitter();
        ElementInspector splitter3 = sidePanel("formID:sidePanel23").splitter();

        content1.assertExpressionEquals("offsetWidth", 293);
        content1.assertExpressionEquals("offsetHeight", 200);
        content2.assertExpressionEquals("offsetWidth", 300);
        content2.assertExpressionEquals("offsetHeight", 200);
        splitter1.dragAndDrop(100, 0);
        content1.assertExpressionEquals("offsetWidth", 393);
        content1.assertExpressionEquals("offsetHeight", 200);
        content2.assertExpressionEquals("offsetWidth", 200);
        content2.assertExpressionEquals("offsetHeight", 200);
        splitter1.dragAndDrop(-200, 0);
        content1.assertExpressionEquals("offsetWidth", 193);
        content1.assertExpressionEquals("offsetHeight", 200);
        content2.assertExpressionEquals("offsetWidth", 400);
        content2.assertExpressionEquals("offsetHeight", 200);

        splitter2.dragAndDrop(0, 100);
        content1.assertExpressionEquals("offsetWidth", 193);
        content1.assertExpressionEquals("offsetHeight", 100);
        content2.assertExpressionEquals("offsetWidth", 400);
        content2.assertExpressionEquals("offsetHeight", 200);
        splitter2.dragAndDrop(0, -200);
        content1.assertExpressionEquals("offsetWidth", 193);
        content1.assertExpressionEquals("offsetHeight", 300);
        content2.assertExpressionEquals("offsetWidth", 400);
        content2.assertExpressionEquals("offsetHeight", 200);

        splitter3.dragAndDrop(0, 100);
        content1.assertExpressionEquals("offsetWidth", 193);
        content1.assertExpressionEquals("offsetHeight", 300);
        content2.assertExpressionEquals("offsetWidth", 400);
        content2.assertExpressionEquals("offsetHeight", 100);
        splitter3.dragAndDrop(0, -200);
        content1.assertExpressionEquals("offsetWidth", 193);
        content1.assertExpressionEquals("offsetHeight", 300);
        content2.assertExpressionEquals("offsetWidth", 400);
        content2.assertExpressionEquals("offsetHeight", 300);
    }

     @Test
    @Ignore
    public void testTruncatedMode() {
        testAppFunctionalPage("/components/borderlayoutpanel/borderLayoutPanelft.jsf");
        ElementInspector sidePanel30Splitter = sidePanel("formID:sidePanel30").splitter();
        ElementInspector sidePanel1 = element("formID:sidePanel32");
        ElementInspector sidePanel2 = element("formID:sidePanel33");
        ElementInspector sidePanel3 = element("formID:sidePanel34");
        ElementInspector sidePanel4 = element("formID:sidePanel35");

        sidePanel30Splitter.dragAndDrop(100, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 50);
        sidePanel2.assertExpressionEquals("offsetWidth", 50);
        sidePanel3.assertExpressionEquals("offsetWidth", 50);
        sidePanel4.assertExpressionEquals("offsetWidth", 50);
        sidePanel30Splitter.dragAndDrop(20, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 50);
        sidePanel2.assertExpressionEquals("offsetWidth", 30);
        sidePanel3.assertExpressionEquals("offsetWidth", 50);
        sidePanel4.assertExpressionEquals("offsetWidth", 50);
        sidePanel30Splitter.dragAndDrop(43, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 50);
        sidePanel2.assertExpressionEquals("offsetWidth", 30);
        sidePanel3.assertExpressionEquals("offsetWidth", 7);
        sidePanel4.assertExpressionEquals("offsetWidth", 50);
        sidePanel30Splitter.dragAndDrop(43, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 50);
        sidePanel2.assertExpressionEquals("offsetWidth", 30);
        sidePanel3.assertExpressionEquals("offsetWidth", 7);
        sidePanel4.assertExpressionEquals("offsetWidth", 7);
        sidePanel30Splitter.dragAndDrop(43, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 7);
        sidePanel2.assertExpressionEquals("offsetWidth", 30);
        sidePanel3.assertExpressionEquals("offsetWidth", 7);
        sidePanel4.assertExpressionEquals("offsetWidth", 7);
        sidePanel30Splitter.dragAndDrop(23, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 7);
        sidePanel2.assertExpressionEquals("offsetWidth", 7);
        sidePanel3.assertExpressionEquals("offsetWidth", 7);
        sidePanel4.assertExpressionEquals("offsetWidth", 7);
        sidePanel30Splitter.dragAndDrop(7, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 0);
        sidePanel2.assertExpressionEquals("offsetWidth", 7);
        sidePanel3.assertExpressionEquals("offsetWidth", 7);
        sidePanel4.assertExpressionEquals("offsetWidth", 7);
        sidePanel30Splitter.dragAndDrop(7, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 0);
        sidePanel2.assertExpressionEquals("offsetWidth", 0);
        sidePanel3.assertExpressionEquals("offsetWidth", 7);
        sidePanel4.assertExpressionEquals("offsetWidth", 7);
        sidePanel30Splitter.dragAndDrop(7, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 0);
        sidePanel2.assertExpressionEquals("offsetWidth", 0);
        sidePanel3.assertExpressionEquals("offsetWidth", 0);
        sidePanel4.assertExpressionEquals("offsetWidth", 7);
        sidePanel30Splitter.dragAndDrop(7, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 0);
        sidePanel2.assertExpressionEquals("offsetWidth", 0);
        sidePanel3.assertExpressionEquals("offsetWidth", 0);
        sidePanel4.assertExpressionEquals("offsetWidth", 0);
        sidePanel30Splitter.dragAndDrop(-7, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 0);
        sidePanel2.assertExpressionEquals("offsetWidth", 0);
        sidePanel3.assertExpressionEquals("offsetWidth", 0);
        sidePanel4.assertExpressionEquals("offsetWidth", 7);
        sidePanel30Splitter.dragAndDrop(-7, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 0);
        sidePanel2.assertExpressionEquals("offsetWidth", 0);
        sidePanel3.assertExpressionEquals("offsetWidth", 7);
        sidePanel4.assertExpressionEquals("offsetWidth", 7);
        sidePanel30Splitter.dragAndDrop(-7, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 0);
        sidePanel2.assertExpressionEquals("offsetWidth", 7);
        sidePanel3.assertExpressionEquals("offsetWidth", 7);
        sidePanel4.assertExpressionEquals("offsetWidth", 7);
        sidePanel30Splitter.dragAndDrop(-7, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 7);
        sidePanel2.assertExpressionEquals("offsetWidth", 7);
        sidePanel3.assertExpressionEquals("offsetWidth", 7);
        sidePanel4.assertExpressionEquals("offsetWidth", 7);
        sidePanel30Splitter.dragAndDrop(-23, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 7);
        sidePanel2.assertExpressionEquals("offsetWidth", 30);
        sidePanel3.assertExpressionEquals("offsetWidth", 7);
        sidePanel4.assertExpressionEquals("offsetWidth", 7);
        sidePanel30Splitter.dragAndDrop(-43, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 50);
        sidePanel2.assertExpressionEquals("offsetWidth", 30);
        sidePanel3.assertExpressionEquals("offsetWidth", 7);
        sidePanel4.assertExpressionEquals("offsetWidth", 7);
        sidePanel30Splitter.dragAndDrop(-43, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 50);
        sidePanel2.assertExpressionEquals("offsetWidth", 30);
        sidePanel3.assertExpressionEquals("offsetWidth", 7);
        sidePanel4.assertExpressionEquals("offsetWidth", 50);
        sidePanel30Splitter.dragAndDrop(-43, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 50);
        sidePanel2.assertExpressionEquals("offsetWidth", 30);
        sidePanel3.assertExpressionEquals("offsetWidth", 50);
        sidePanel4.assertExpressionEquals("offsetWidth", 50);
        sidePanel30Splitter.dragAndDrop(-43, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 50);
        sidePanel2.assertExpressionEquals("offsetWidth", 50);
        sidePanel3.assertExpressionEquals("offsetWidth", 50);
        sidePanel4.assertExpressionEquals("offsetWidth", 50);
        sidePanel30Splitter.dragAndDrop(-43, 0);
        sidePanel1.assertExpressionEquals("offsetWidth", 50);
        sidePanel2.assertExpressionEquals("offsetWidth", 50);
        sidePanel3.assertExpressionEquals("offsetWidth", 50);
        sidePanel4.assertExpressionEquals("offsetWidth", 50);

        ElementInspector sidePanel31Splitter = sidePanel("formID:sidePanel31").splitter();
        sidePanel1 = element("formID:sidePanel36");
        sidePanel2 = element("formID:sidePanel37");
        sidePanel3 = element("formID:sidePanel38");
        sidePanel4 = element("formID:sidePanel39");

        sidePanel31Splitter.dragAndDrop(0, 100);
        sidePanel1.assertExpressionEquals("offsetHeight", 50);
        sidePanel2.assertExpressionEquals("offsetHeight", 50);
        sidePanel3.assertExpressionEquals("offsetHeight", 50);
        sidePanel4.assertExpressionEquals("offsetHeight", 50);
        sidePanel31Splitter.dragAndDrop(0, 20);
        sidePanel1.assertExpressionEquals("offsetHeight", 50);
        sidePanel2.assertExpressionEquals("offsetHeight", 30);
        sidePanel3.assertExpressionEquals("offsetHeight", 50);
        sidePanel4.assertExpressionEquals("offsetHeight", 50);
        sidePanel31Splitter.dragAndDrop(0, 43);
        sidePanel1.assertExpressionEquals("offsetHeight", 50);
        sidePanel2.assertExpressionEquals("offsetHeight", 30);
        sidePanel3.assertExpressionEquals("offsetHeight", 7);
        sidePanel4.assertExpressionEquals("offsetHeight", 50);
        sidePanel31Splitter.dragAndDrop(0, 43);
        sidePanel1.assertExpressionEquals("offsetHeight", 50);
        sidePanel2.assertExpressionEquals("offsetHeight", 30);
        sidePanel3.assertExpressionEquals("offsetHeight", 7);
        sidePanel4.assertExpressionEquals("offsetHeight", 7);
        sidePanel31Splitter.dragAndDrop(0, 43);
        sidePanel1.assertExpressionEquals("offsetHeight", 7);
        sidePanel2.assertExpressionEquals("offsetHeight", 30);
        sidePanel3.assertExpressionEquals("offsetHeight", 7);
        sidePanel4.assertExpressionEquals("offsetHeight", 7);
        sidePanel31Splitter.dragAndDrop(0, 23);
        sidePanel1.assertExpressionEquals("offsetHeight", 7);
        sidePanel2.assertExpressionEquals("offsetHeight", 7);
        sidePanel3.assertExpressionEquals("offsetHeight", 7);
        sidePanel4.assertExpressionEquals("offsetHeight", 7);
        sidePanel31Splitter.dragAndDrop(0, 7);
        sidePanel1.assertExpressionEquals("offsetHeight", 0);
        sidePanel2.assertExpressionEquals("offsetHeight", 7);
        sidePanel3.assertExpressionEquals("offsetHeight", 7);
        sidePanel4.assertExpressionEquals("offsetHeight", 7);
        sidePanel31Splitter.dragAndDrop(0, 7);
        sidePanel1.assertExpressionEquals("offsetHeight", 0);
        sidePanel2.assertExpressionEquals("offsetHeight", 0);
        sidePanel3.assertExpressionEquals("offsetHeight", 7);
        sidePanel4.assertExpressionEquals("offsetHeight", 7);
        sidePanel31Splitter.dragAndDrop(0, 7);
        sidePanel1.assertExpressionEquals("offsetHeight", 0);
        sidePanel2.assertExpressionEquals("offsetHeight", 0);
        sidePanel3.assertExpressionEquals("offsetHeight", 0);
        sidePanel4.assertExpressionEquals("offsetHeight", 7);
        sidePanel31Splitter.dragAndDrop(0, 7);
        sidePanel1.assertExpressionEquals("offsetHeight", 0);
        sidePanel2.assertExpressionEquals("offsetHeight", 0);
        sidePanel3.assertExpressionEquals("offsetHeight", 0);
        sidePanel4.assertExpressionEquals("offsetHeight", 0);
        sidePanel31Splitter.dragAndDrop(0, -7);
        sidePanel1.assertExpressionEquals("offsetHeight", 0);
        sidePanel2.assertExpressionEquals("offsetHeight", 0);
        sidePanel3.assertExpressionEquals("offsetHeight", 0);
        sidePanel4.assertExpressionEquals("offsetHeight", 7);
        sidePanel31Splitter.dragAndDrop(0, -7);
        sidePanel1.assertExpressionEquals("offsetHeight", 0);
        sidePanel2.assertExpressionEquals("offsetHeight", 0);
        sidePanel3.assertExpressionEquals("offsetHeight", 7);
        sidePanel4.assertExpressionEquals("offsetHeight", 7);
        sidePanel31Splitter.dragAndDrop(0, -7);
        sidePanel1.assertExpressionEquals("offsetHeight", 0);
        sidePanel2.assertExpressionEquals("offsetHeight", 7);
        sidePanel3.assertExpressionEquals("offsetHeight", 7);
        sidePanel4.assertExpressionEquals("offsetHeight", 7);
        sidePanel31Splitter.dragAndDrop(0, -7);
        sidePanel1.assertExpressionEquals("offsetHeight", 7);
        sidePanel2.assertExpressionEquals("offsetHeight", 7);
        sidePanel3.assertExpressionEquals("offsetHeight", 7);
        sidePanel4.assertExpressionEquals("offsetHeight", 7);
        sidePanel31Splitter.dragAndDrop(0, -23);
        sidePanel1.assertExpressionEquals("offsetHeight", 7);
        sidePanel2.assertExpressionEquals("offsetHeight", 30);
        sidePanel3.assertExpressionEquals("offsetHeight", 7);
        sidePanel4.assertExpressionEquals("offsetHeight", 7);
        sidePanel31Splitter.dragAndDrop(0, -43);
        sidePanel1.assertExpressionEquals("offsetHeight", 50);
        sidePanel2.assertExpressionEquals("offsetHeight", 30);
        sidePanel3.assertExpressionEquals("offsetHeight", 7);
        sidePanel4.assertExpressionEquals("offsetHeight", 7);
        sidePanel31Splitter.dragAndDrop(0, -43);
        sidePanel1.assertExpressionEquals("offsetHeight", 50);
        sidePanel2.assertExpressionEquals("offsetHeight", 30);
        sidePanel3.assertExpressionEquals("offsetHeight", 7);
        sidePanel4.assertExpressionEquals("offsetHeight", 50);
        sidePanel31Splitter.dragAndDrop(0, -43);
        sidePanel1.assertExpressionEquals("offsetHeight", 50);
        sidePanel2.assertExpressionEquals("offsetHeight", 30);
        sidePanel3.assertExpressionEquals("offsetHeight", 50);
        sidePanel4.assertExpressionEquals("offsetHeight", 50);
        sidePanel31Splitter.dragAndDrop(0, -43);
        sidePanel1.assertExpressionEquals("offsetHeight", 50);
        sidePanel2.assertExpressionEquals("offsetHeight", 50);
        sidePanel3.assertExpressionEquals("offsetHeight", 50);
        sidePanel4.assertExpressionEquals("offsetHeight", 50);
        sidePanel31Splitter.dragAndDrop(0, -43);
        sidePanel1.assertExpressionEquals("offsetHeight", 50);
        sidePanel2.assertExpressionEquals("offsetHeight", 50);
        sidePanel3.assertExpressionEquals("offsetHeight", 50);
        sidePanel4.assertExpressionEquals("offsetHeight", 50);
    }

     @Test
    @Ignore
    // todo: investigate why this test fails on build server (but works locally)
    public void testBoxLayoutModel() {
        testAppFunctionalPage("/components/borderlayoutpanel/borderLayoutPanelft.jsf");

        ElementInspector sidePanel = element("formID:sidePanel11");
        sidePanel.assertStyle("left: 3px; top: 99px");
        sidePanel.assertExpressionEquals("offsetWidth", 146);
        sidePanel.assertExpressionEquals("offsetHeight", 190);

        window().evalExpression("resizeBy(10, 10)");
        sleep(2000);
        sidePanel = element("formID:sidePanel12");
        sidePanel.assertStyle("right: 3px; top: 99px");
        sidePanel.assertExpressionEquals("offsetWidth", 146);
        sidePanel.assertExpressionEquals("offsetHeight", 190);

        sidePanel = element("formID:sidePanel13");
        sidePanel.assertStyle("left: 3px; top: 3px");
        sidePanel.assertExpressionEquals("offsetWidth", 582);
        sidePanel.assertExpressionEquals("offsetHeight", 96);

        sidePanel = element("formID:sidePanel14");
        sidePanel.assertStyle("right: 3px; bottom: 3px");
        sidePanel.assertExpressionEquals("offsetWidth", 582);
        sidePanel.assertExpressionEquals("offsetHeight", 96);

        ElementInspector content = element("formID:borderLayoutPanel10::content");
        content.assertStyle("left: 149px; top: 99px");
        content.assertExpressionEquals("offsetWidth", 284);
        content.assertExpressionEquals("offsetHeight", 184);
    }

}