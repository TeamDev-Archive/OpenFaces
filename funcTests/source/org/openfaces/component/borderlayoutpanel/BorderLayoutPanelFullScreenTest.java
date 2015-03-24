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

import com.thoughtworks.selenium.Selenium;
import org.junit.Ignore;
import org.junit.Test;
import org.openfaces.test.BaseSeleniumTest;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.LoadingMode;

import java.awt.*;

/**
 * @author Alexey Tarasyuk
 */
public class BorderLayoutPanelFullScreenTest extends BaseSeleniumTest {

    protected boolean getStartBrowserForEachTestMethod() {
        return true;
    }

    @Test
    @Ignore
    // todo: investigate why this test fails on build server (but works locally)
    public void testRendering() {
        testAppFunctionalPage("/components/borderlayoutpanel/borderLayoutPanel1ft.jsf");
//    saveScreenshotToFile("c:/temp/BorderLayoutPanelFullScreenTest.testRendering.1.png");

        // test elements rendering
        element("formID:borderLayoutPanel1").assertElementExists();
        element("formID:borderLayoutPanel2").assertElementExists();
        element("formID:borderLayoutPanel3").assertElementExists();
        element("formID:borderLayoutPanel4").assertElementExists();
        element("formID:sidePanel1").assertElementExists();
        element("formID:sidePanel2").assertElementExists();
        element("formID:sidePanel3").assertElementExists();
        element("formID:sidePanel4").assertElementExists();
        element("formID:sidePanel11").assertElementExists();
        element("formID:sidePanel21").assertElementExists();
        element("formID:sidePanel31").assertElementExists();
        element("formID:sidePanel41").assertElementExists();
        element("formID:borderLayoutPanel1::content").assertElementExists();
        element("formID:borderLayoutPanel2::content").assertElementExists();
        element("formID:borderLayoutPanel3::content").assertElementExists();
        element("formID:borderLayoutPanel4::content").assertElementExists();

        // window resizing was added before window size measurement to overcome strange intermittent size measuremt failures
        window().evalExpression("resizeBy(5, 5)");
        sleep(2000);
//    saveScreenshotToFile("c:/temp/BorderLayoutPanelFullScreenTest.testRendering.2.png");

        Dimension windowSize = window().size();

        // test positions & sizes

        ElementInspector borderLayoutPanel = element("formID:borderLayoutPanel0");
        borderLayoutPanel.assertElementExists();
        borderLayoutPanel.assertExpressionEquals("offsetWidth", windowSize.width);
        borderLayoutPanel.assertExpressionEquals("offsetHeight", windowSize.height);

        ElementInspector content = element("formID:borderLayoutPanel0::content");
        content.assertElementExists();
        content.assertWidth(windowSize.width / 2 - 6, 1);
        content.assertHeight(windowSize.height / 2 - 5, 1);
//    saveScreenshotToFile("c:/temp/BorderLayoutPanelFullScreenTest.testRendering.3.png");
    }

     @Test
    @Ignore
    // todo: investigate why this test fails on build server (but works locally)
    public void testContentResizeOnWindowResize() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/borderlayoutpanel/borderLayoutPanel1ft.jsf");

        // window resizing was added before window size measurement to overcome strange intermittent size measuremt failures
        window().evalExpression("resizeBy(5, 5)");
        sleep(2000);
        Dimension windowSize = window().size();

        ElementInspector content = element("formID:borderLayoutPanel0::content");
        int width = content.evalIntExpression("offsetWidth");
        int height = content.evalIntExpression("offsetHeight");
        content.assertExpressionEquals("offsetWidth", windowSize.width / 2 - 6, 1);
        content.assertExpressionEquals("offsetHeight", windowSize.height / 2 - 5, 1);
        window().evalExpression("resizeBy(-100, -100)");
        sleep(2000);
        selenium.waitForCondition("window.document.getElementById('textDiv').innerHTML == \"window resized\"", "10000");
        content.assertExpressionEquals("offsetWidth", width - 50);
        content.assertExpressionEquals("offsetHeight", height - 50);
    }

}