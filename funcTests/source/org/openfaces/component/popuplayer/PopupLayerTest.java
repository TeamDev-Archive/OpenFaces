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
package org.openfaces.component.popuplayer;

import org.junit.Ignore;
import org.junit.Test;
import org.openfaces.test.BaseSeleniumTest;
import org.openfaces.test.RichFacesAjaxLoadingMode;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.html.InputInspector;
import org.seleniuminspector.openfaces.PopupLayerInspector;

import java.awt.*;

/**
 * @author Darya Shumilina
 */
public class PopupLayerTest extends BaseSeleniumTest {
     @Test
    public void testReRenderThroughA4J() {
        testAppFunctionalPage("/components/popuplayer/popupLayer_a4j.jsf");
        PopupLayerInspector popupLayer = popupLayer("formID:popupLayerID");
        popupLayer.assertElementExists();
        popupLayer.assertVisible(false);
        element("formID:buttonID").click();
        String oldValue = popupLayer.text();
        popupLayer.assertVisible(true);
        element("formID:popupCloser").click();
        popupLayer.assertVisible(false);
        element("formID:refresher").click();
        RichFacesAjaxLoadingMode.getInstance().waitForLoad();
        element("formID:buttonID").click();
        String newValue = popupLayer.text();
        popupLayer.assertVisible(true);
        element("formID:popupCloser").click();
        popupLayer.assertVisible(false);
        popupLayer.assertElementExists();
        assertFalse(newValue.equals(oldValue));
    }

     @Test
    public void testWithA4JControls() {
        testAppFunctionalPage("/components/popuplayer/popupLayer_a4j.jsf");
        PopupLayerInspector popupLayer = popupLayer("formID:popupLayer_a4j_ID");
        popupLayer.assertElementExists();
        popupLayer.assertVisible(false);
        ElementInspector button = element("formID:button_a4j_ID");
        button.click();
        String oldValue = popupLayer.text();
        popupLayer.assertVisible(true);
        ElementInspector popupCloser = element("formID:popup_a4j_Closer");
        popupCloser.click();
        popupLayer.assertVisible(false);
        element("formID:refresher_a4j").click();
        RichFacesAjaxLoadingMode.getInstance().waitForLoad();
        button.click();
        String newValue = popupLayer.text();
        popupLayer.assertVisible(true);
        popupCloser.click();
        popupLayer.assertElementExists();
        popupLayer.assertVisible(false);
        assertFalse(newValue.equals(oldValue));
    }
    @Ignore
     @Test
    public void testPopupLayerVisibleByPageLoad() {
        testAppFunctionalPage("/components/popuplayer/popupLayerVisibleByLoad.jsf");

        PopupLayerInspector popupLayer = popupLayer("formID:visible");
        popupLayer.assertVisible(true);
        element("formID:submit").clickAndWait();
        popupLayer.assertVisible(true);
    }

    // showPopupLayer, hidePopupLayer
     @Test
     @Ignore
    public void testShowHideJSFunctions() {
        testAppFunctionalPage("/components/popuplayer/popupLayerJSFunctions.jsf");

        PopupLayerInspector popupLayer = popupLayer("formID:showHide");
        // show PopupLayer
        popupLayer.assertVisible(false);
        element("showPopupLayer").click();
        popupLayer.assertVisible(true);

        // make submit and check is PopupLayer in 'visible' state
        element("formID:submit").clickAndWait();
        popupLayer.assertVisible(true);

        // hide PopupLayer
        element("hidePopupLayer").click();
        popupLayer.assertVisible(false);
    }

    // showPopupLayerAtXY
     @Test
     @Ignore
    public void testShowPopupLayerAtXYJSFunction() {
        testAppFunctionalPage("/components/popuplayer/popupLayerJSFunctions.jsf");


        PopupLayerInspector popupLayer = popupLayer("formID:showAtXY");

        element("showPopupLayerAtXY").click();
        popupLayer.assertVisible(true);

        element("formID:submit").clickAndWait();
        popupLayer.assertVisible(true);
        popupLayer.assertPosition(250, 250);
    }

    // showCentered
     @Test
    @Ignore
    public void testShowPopupLayerCenteredJSFunction() {
        testAppFunctionalPage("/components/popuplayer/popupLayerJSFunctions.jsf");

        PopupLayerInspector popupLayer = popupLayer("formID:centered");
        element("showCentered").clickAndWait();

        int popupLayerWidth = 200;
        int popupLayerHeight = 200;
        Dimension windowSize = window().size();

        Point popupLayerPos = popupLayer.position();
        assertTrue(Math.abs(popupLayerPos.x - (windowSize.width - popupLayerWidth) / 2) <= 10);
        assertTrue(Math.abs(popupLayerPos.y - (windowSize.height - popupLayerHeight) / 2) <= 10);

        popupLayer.assertVisible(true);
        element("formID:submit").clickAndWait();
        popupLayer.assertVisible(true);
    }

    // setPopupLayerLeft, setPopupLayerTop
     @Test
     @Ignore
    public void testSetTopAndLeftJSFunctions() {
        testAppFunctionalPage("/components/popuplayer/popupLayerJSFunctions.jsf");

        PopupLayerInspector popupLayer = popupLayer("formID:setLeftTop");

        popupLayer.assertVisible(false);
        InputInspector leftCoordinateInput = new InputInspector("leftCoodinate");
        leftCoordinateInput.type("100");
        InputInspector topCoordinateInput = new InputInspector("topCoodinate");
        topCoordinateInput.type("200");
        ElementInspector topLeftButton = element("setPopupLayerTopLeft");
        topLeftButton.click();

        popupLayer.assertVisible(true);
        element("formID:submit").clickAndWait();
        popupLayer.assertVisible(true);

        popupLayer.assertPosition(100, 200);

        leftCoordinateInput.type("300");
        topCoordinateInput.type("500");
        topLeftButton.click();

        popupLayer.assertPosition(300, 500);
    }

     @Test
    public void testModality() {
        testAppFunctionalPage("/components/popuplayer/popupLayerStyling.jsf");
        popupLayer("formID:styled").modalLayer().assertStyle("background-color: beige");
    }

     @Test
     @Ignore
    public void testStyling() {
        testAppFunctionalPage("/components/popuplayer/popupLayerStyling.jsf");
        PopupLayerInspector popupLayer = popupLayer("formID:styled");

        popupLayer.assertStyle("width: 400px; height: 200px; border: 2px solid crimson; background: mistyrose; " +
                "text-decoration: underline; font-weight: bold; text-align:center; left: 200px; top: 200px;");

        popupLayer.mouseOver();
        popupLayer.mouseMove();

        popupLayer.assertStyle("width: 450px; height: 250px; border: 3px dashed springgreen; background: azure; " +
                "text-decoration: overline; font-weight: lighter;");
    }

}