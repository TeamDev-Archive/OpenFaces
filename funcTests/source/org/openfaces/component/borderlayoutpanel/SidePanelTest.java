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
package org.openfaces.component.borderlayoutpanel;

import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.openfaces.SidePanelInspector;
import org.seleniuminspector.ServerLoadingMode;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.LoadingMode;

/**
 * @author Alexey Tarasyuk
 */
public class SidePanelTest extends OpenFacesTestCase {
     //@Test
    public void testRendering() {
        testAppFunctionalPage("/components/borderlayoutpanel/sidePanelft.jsf");
        // test defaultSidePanel rendering

        SidePanelInspector defaultSidePanel = sidePanel("formID:defaultSidePanelId");
        defaultSidePanel.assertElementExists();
        defaultSidePanel.splitter().assertElementExists();
        defaultSidePanel.panel().assertElementExists();
        defaultSidePanel.caption().assertElementExists(false);
        defaultSidePanel.content().assertElementExists();

        // test leftSidePanel rendering
        SidePanelInspector leftSidePanel = sidePanel("formID:leftSidePanelId");
        leftSidePanel.assertElementExists();
        leftSidePanel.splitter().assertElementExists();
        leftSidePanel.panel().assertElementExists();
        leftSidePanel.caption().assertElementExists();
        leftSidePanel.content().assertElementExists();

        element("leftSidePanelCaptionTextId").assertElementExists();
        element("leftSidePanelContentTextId").assertElementExists();
    }

     //@Test
    public void testLeftSidePanel() {
        testAppFunctionalPage("/components/borderlayoutpanel/sidePanelft.jsf");

        SidePanelInspector sidePanel = sidePanel("formID:leftSidePanelId");

        /*test offsetHeight/offsetWidth
        * -----------------------------
        * by default size = "50%" i.e. sidePanel.offsetWidth = 100
        * sidePanel.style.height = "100%" i.e. sidePanel.offsetHeight = 200
        */
        sidePanel.assertExpressionEquals("offsetWidth", 100);
        sidePanel.assertExpressionEquals("offsetHeight", 200);

        /*test collapse/restore
        * ---------------------
        * default splitter width/height = "4px", and splitter borders have a "3px" width at sum, so splitter size = "7px"
        * after collapse() sidePanel size should be equal to splitter size
        * after restore() sidePanel size should return to a normal size
        */
        sidePanel.splitter().mouseDown();
        sidePanel.splitter().mouseUp();
        sidePanel.assertExpressionEquals("_size", "7px");
        sidePanel.splitter().mouseDown();
        sidePanel.splitter().mouseUp();
        sidePanel.assertExpressionEquals("_size", "50%");

        /*test splitter dragging
         ----------------------
         by default minSize and maxSize not specified, so it should be equal "7px" and "100%", i.e. 7 and 200
        */
        sidePanel.splitter().dragAndDrop(-60, 0);
        sidePanel.assertExpressionEquals("_size", "20%");
        sidePanel.splitter().dragAndDrop(0, 60);
        sidePanel.assertExpressionEquals("_size", "20%");
        sidePanel.splitter().dragAndDrop(-50, 0);
        sidePanel.assertExpressionEquals("_size", "3.5%");
        sidePanel.splitter().dragAndDrop(400, 0);
        sidePanel.assertExpressionEquals("_size", "100%");
    }

     //@Test
    public void testTopSidePanel() {
        testAppFunctionalPage("/components/borderlayoutpanel/sidePanelft.jsf");

        SidePanelInspector sidePanel = sidePanel("formID:topSidePanelId");

        /*test offsetHeight/offsetWidth
        * -----------------------------
        * by default size = "50%" i.e. sidePanel.offsetHeight = "100"
        * sidePanel.style.width = "100%" i.e. sidePanel.offsetWidth = "200"
        */
        sidePanel.assertExpressionEquals("offsetWidth", 200);
        sidePanel.assertExpressionEquals("offsetHeight", 100);

        /*test collapse/restore
        * ---------------------
        * default splitter width/height = "4px", and splitter borders have a "3px" width at sum, so splitter size = "7px"
        * after collapse() sidePanel size should be equal to splitter size
        * after restore() sidePanel size should return to a normal size
        */
        sidePanel.splitter().mouseDown();
        sidePanel.splitter().mouseUp();
        sidePanel.assertExpressionEquals("_size", "7px");
        sidePanel.splitter().mouseDown();
        sidePanel.splitter().mouseUp();
        sidePanel.assertExpressionEquals("_size", "50%");

        /*test splitter dragging
         ----------------------
         by default minSize and maxSize not specified, so it should be equal "7px" and "100%", i.e. 7 and 200
        */
        sidePanel.splitter().dragAndDrop(0, -60);
        sidePanel.assertExpressionEquals("_size", "20%");
        sidePanel.splitter().dragAndDrop(60, 0);
        sidePanel.assertExpressionEquals("_size", "20%");
        sidePanel.splitter().dragAndDrop(0, -60);
        sidePanel.assertExpressionEquals("_size", "3.5%");
        sidePanel.splitter().dragAndDrop(0, 400);
        sidePanel.assertExpressionEquals("_size", "100%");
    }

     //@Test
    public void testBoxLayoutModel() {
        testAppFunctionalPage("/components/borderlayoutpanel/sidePanelft.jsf");

        SidePanelInspector sidePanel = sidePanel("formID:layoutSidePanelId");

        /* to make sure that sidePanel width/height the same */
        sidePanel.assertExpressionEquals("offsetWidth", 100);
        sidePanel.assertExpressionEquals("offsetHeight", 200);

        sidePanel.splitter().assertExpressionEquals("offsetWidth", 12);
        sidePanel.splitter().assertExpressionEquals("offsetHeight", 196);
        sidePanel.panel().assertExpressionEquals("offsetWidth", 80);
        sidePanel.panel().assertExpressionEquals("offsetHeight", 196);
        sidePanel.caption().assertExpressionEquals("offsetWidth", 68);
        sidePanel.caption().assertExpressionEquals("offsetHeight", 28);
        sidePanel.content().assertExpressionEquals("offsetWidth", 68);
        sidePanel.content().assertExpressionEquals("offsetHeight", 152);

        /* test SidePanel in collapsed mode */
        sidePanel.splitter().mouseDown();
        sidePanel.splitter().mouseUp();
        sidePanel.assertExpressionEquals("offsetWidth", 16);
        sidePanel.assertExpressionEquals("offsetHeight", 200);

        sidePanel.splitter().assertExpressionEquals("offsetWidth", 12);
        sidePanel.splitter().assertExpressionEquals("offsetHeight", 196);
        sidePanel.panel().assertExpressionEquals("offsetWidth", 0);
        sidePanel.panel().assertExpressionEquals("offsetHeight", 0);
        sidePanel.caption().assertExpressionEquals("offsetWidth", 0);
        sidePanel.caption().assertExpressionEquals("offsetHeight", 0);
        sidePanel.content().assertExpressionEquals("offsetWidth", 0);
        sidePanel.content().assertExpressionEquals("offsetHeight", 0);

        /* test SidePanel in small size mode */
        sidePanel.splitter().mouseDown();
        sidePanel.splitter().mouseUp();
        sidePanel.splitter().dragAndDrop(-72, 0);
        sidePanel.assertExpressionEquals("offsetWidth", 28);
        sidePanel.assertExpressionEquals("offsetHeight", 200);

        sidePanel.splitter().assertExpressionEquals("offsetWidth", 12);
        sidePanel.splitter().assertExpressionEquals("offsetHeight", 196);
        sidePanel.panel().assertExpressionEquals("offsetWidth", 8);
        sidePanel.panel().assertExpressionEquals("offsetHeight", 196);
        sidePanel.caption().assertExpressionEquals("offsetWidth", 0);
        sidePanel.caption().assertExpressionEquals("offsetHeight", 0);
        sidePanel.content().assertExpressionEquals("offsetWidth", 0);
        sidePanel.content().assertExpressionEquals("offsetHeight", 0);
    }

     //@Test
    public void testSizeLimitation() {
        testAppFunctionalPage("/components/borderlayoutpanel/sidePanelft.jsf");

        SidePanelInspector sidePanel = sidePanel("formID:sizeLimitedSidePanelId");

        /* to make sure that size limitation work on resizing */
        sidePanel.splitter().dragAndDrop(0, -200);
        sidePanel.assertExpressionEquals("_size", "25%");
        sidePanel.splitter().dragAndDrop(0, 200);
        sidePanel.assertExpressionEquals("_size", "75%");

        /* to make sure that size limitation ignored on collapse/restore */
        sidePanel.splitter().mouseDown();
        sidePanel.splitter().mouseUp();
        sidePanel.assertExpressionEquals("_size", "7px");
        sidePanel.splitter().mouseDown();
        sidePanel.splitter().mouseUp();
        sidePanel.assertExpressionEquals("_size", "75%");
    }

     //@Test
    public void testEvents() {
        testAppFunctionalPage("/components/borderlayoutpanel/sidePanelft.jsf");

        SidePanelInspector sidePanel = sidePanel("formID:eventsSidePanelId");
        ElementInspector sidePanelText = element("sidePanelTextId");

        /* test onsplitterdrag */
        sidePanelText.assertExpressionEquals("innerHTML", "size = 50%");
        sidePanel.splitter().dragAndDrop(0, -50);
        sidePanelText.assertExpressionEquals("innerHTML", "size = 25%");

        /* test oncollapse */
        sidePanel.splitter().mouseDown();
        sidePanel.splitter().mouseUp();
        sidePanelText.assertExpressionEquals("innerHTML", "size panel is collapsed");

        /* test onrestore */
        sidePanel.splitter().mouseDown();
        sidePanel.splitter().mouseUp();
        sidePanelText.assertExpressionEquals("innerHTML", "size panel is restored");
    }

     //@Test
    public void testFixedSidePanels() {
        testAppFunctionalPage("/components/borderlayoutpanel/sidePanelft.jsf");

        /* test SidePanel with collapsible = false and resizable = false */
        SidePanelInspector notFixedSidePane = sidePanel("formID:notFixedSidePanelId");

        notFixedSidePane.splitter().assertStyle("cursor: row-resize");
        notFixedSidePane.splitter().mouseDown();
        notFixedSidePane.splitter().mouseUp();
        notFixedSidePane.splitter().assertStyle("cursor: pointer");
        notFixedSidePane.assertExpressionEquals("_size", "7px");
        notFixedSidePane.splitter().mouseDown();
        notFixedSidePane.splitter().mouseUp();
        notFixedSidePane.assertExpressionEquals("_size", "50%");
        notFixedSidePane.splitter().dragAndDrop(0, 50);
        notFixedSidePane.assertExpressionEquals("_size", "75%");
        notFixedSidePane.splitter().dragAndDrop(0, -50);
        notFixedSidePane.assertExpressionEquals("_size", "50%");

        /* test SidePanel with collapsible = false */
        SidePanelInspector collapseFixedSidePanel = sidePanel("formID:collapseFixedSidePanelId");

        collapseFixedSidePanel.splitter().assertStyle("cursor: row-resize");
        collapseFixedSidePanel.assertExpressionEquals("_size", "50%");
        collapseFixedSidePanel.splitter().mouseDown();
        collapseFixedSidePanel.splitter().mouseUp();
        collapseFixedSidePanel.splitter().assertStyle("cursor: row-resize");
        collapseFixedSidePanel.assertExpressionEquals("_size", "50%");
        collapseFixedSidePanel.splitter().mouseDown();
        collapseFixedSidePanel.splitter().mouseUp();
        collapseFixedSidePanel.assertExpressionEquals("_size", "50%");
        collapseFixedSidePanel.splitter().dragAndDrop(0, 50);
        collapseFixedSidePanel.assertExpressionEquals("_size", "75%");
        collapseFixedSidePanel.splitter().dragAndDrop(0, -50);
        collapseFixedSidePanel.assertExpressionEquals("_size", "50%");

        /* test SidePanel with resizable = false */
        SidePanelInspector resizeFixedSidePanel = sidePanel("formID:resizeFixedSidePanelId");

        resizeFixedSidePanel.splitter().assertStyle("cursor: pointer");
        resizeFixedSidePanel.splitter().mouseDown();
        resizeFixedSidePanel.splitter().mouseUp();
        resizeFixedSidePanel.splitter().assertStyle("cursor: pointer");
        resizeFixedSidePanel.assertExpressionEquals("_size", "7px");
        resizeFixedSidePanel.splitter().mouseDown();
        resizeFixedSidePanel.splitter().mouseUp();
        resizeFixedSidePanel.assertExpressionEquals("_size", "50%");
        resizeFixedSidePanel.splitter().dragAndDrop(0, 50);
        resizeFixedSidePanel.assertExpressionEquals("_size", "50%");
        resizeFixedSidePanel.splitter().dragAndDrop(0, -50);
        resizeFixedSidePanel.assertExpressionEquals("_size", "50%");

        /* test SidePanel with collapsible = false and resizable = false */
        SidePanelInspector fullFixedSidePanel = sidePanel("formID:fullFixedSidePanelId");

        fullFixedSidePanel.splitter().assertStyle("cursor: auto");
        fullFixedSidePanel.splitter().mouseDown();
        fullFixedSidePanel.splitter().mouseUp();
        fullFixedSidePanel.splitter().assertStyle("cursor: auto");
        fullFixedSidePanel.assertExpressionEquals("_size", "50%");
        fullFixedSidePanel.splitter().mouseDown();
        fullFixedSidePanel.splitter().mouseUp();
        fullFixedSidePanel.assertExpressionEquals("_size", "50%");
        fullFixedSidePanel.splitter().dragAndDrop(0, 50);
        fullFixedSidePanel.assertExpressionEquals("_size", "50%");
        fullFixedSidePanel.splitter().dragAndDrop(0, -50);
        fullFixedSidePanel.assertExpressionEquals("_size", "50%");
    }

     //@Test
    public void testClientSideAPI() {
        testAppFunctionalPage("/components/borderlayoutpanel/sidePanelft.jsf");
        ElementInspector sidePanel = element("formID:defaultSidePanelId");

        sidePanel.evalExpression("resize('25%');");
        sidePanel.assertExpressionEquals("_size", "25%");
        sidePanel.evalExpression("collapse();");
        sidePanel.assertExpressionEquals("_size", "7px");
        sidePanel.evalExpression("restore();");
        sleep(1000);
        sidePanel.assertExpressionEquals("_size", "25%");
    }

     //@Test
    public void testSizeRendering() {
        testAppFunctionalPage("/components/borderlayoutpanel/sidePanelft.jsf");
        ElementInspector sidePanel = element("formID:layoutSidePanelId");

        sidePanel.evalExpression("resize('25.5%');");
        sidePanel.assertExpressionEquals("offsetWidth", 51);
        sidePanel.evalExpression("resize('25px');");
        sidePanel.assertExpressionEquals("offsetWidth", 25);
    }

     //@Test
    public void testStateStore() {
        testAppFunctionalPage("/components/borderlayoutpanel/sidePanelft.jsf");
        ElementInspector sidePanel = element("formID:defaultSidePanelId");

        sidePanel.evalExpression("resize('25.5%')");
        sidePanel.evalExpression("collapse()");
        getSelenium().submit("formID");
        ServerLoadingMode.getInstance().waitForLoad();
        sidePanel.assertExpressionEquals("_size", "7px");
        sidePanel.assertExpressionEquals("_collapsed", true);
        sidePanel.evalExpression("restore()");
        sidePanel.assertExpressionEquals("_collapsed", false);
        sidePanel.assertExpressionEquals("_size", "25.5%");
    }
}
