/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.foldingpanel;

import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.openfaces.test.RichFacesAjaxLoadingMode;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.openfaces.FoldingPanelInspector;
import org.seleniuminspector.openfaces.OpenFacesAjaxLoadingMode;

/**
 * @author Darya Shumilina
 */
public class FoldingPanelTest extends OpenFacesTestCase {
    @Test
    public void testReRenderThroughA4J() {
        testAppFunctionalPage("/components/foldingpanel/foldingPanel_a4j.jsf");
        ElementInspector foldingPanelToggle = foldingPanel("formID:foldingPanelID").toggle();
        foldingPanelToggle.clickAndWait(OpenFacesAjaxLoadingMode.getInstance());

        ElementInspector foldingPanelCaption = element("formID:foldingPanelCaption");
        ElementInspector foldingPanelContent = element("formID:foldingPanelContent");

        String oldCaptionValue = foldingPanelCaption.text();
        String oldContentValue = foldingPanelContent.text();
        foldingPanelToggle.click();
        element("formID:refresher").click();
        RichFacesAjaxLoadingMode.getInstance().waitForLoad();
        foldingPanelToggle.clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        String newCaptionValue = foldingPanelCaption.text();
        String newContentValue = foldingPanelContent.text();
        assertFalse(newCaptionValue.equals(oldCaptionValue));
        assertFalse(newContentValue.equals(oldContentValue));
    }

    @Test
    public void testWithA4JControlsInside() {
        testAppFunctionalPage("/components/foldingpanel/foldingPanel_a4j.jsf");
        foldingPanel("formID:foldingPanel_a4j_ID").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());

        ElementInspector foldingPanelCaption = element("formID:foldingPanel_a4j_Caption");
        ElementInspector foldingPanelContent = element("formID:foldingPanel_a4j_Content");
        String oldCaptionValue = foldingPanelCaption.text();
        String oldContentValue = foldingPanelContent.text();

        FoldingPanelInspector foldingPanel = foldingPanel("formID:foldingPanel_a4j_ID");
        foldingPanel.toggle().click();
        element("formID:refresher_a4j").click();
        RichFacesAjaxLoadingMode.getInstance().waitForLoad();
        foldingPanel.toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());

        String newCaptionValue = foldingPanelCaption.text();
        String newContentValue = foldingPanelContent.text();
        assertFalse(newCaptionValue.equals(oldCaptionValue));
        assertFalse(newContentValue.equals(oldContentValue));
    }

    @Test
    public void testDefaultView() {
        testAppFunctionalPage("/components/foldingpanel/foldingPanel_defaultView.jsf");
        assertAppearanceNotChanged("FoldingPanelDefaultView");
    }

    @Test
    public void testDifferentLoadingModes() {
        testAppFunctionalPage("/components/foldingpanel/foldingPanelDifferentLoadingModes.jsf");

        //check 'server' loading mode
        foldingPanel("formID:serverFP").caption().assertVisible(true);
        ElementInspector serverImage = element("formID:serverImage");
        serverImage.assertElementExists(false);
        ElementInspector serverText = element("formID:serverText");
        serverText.assertElementExists(false);

        FoldingPanelInspector serverFoldingPanel = foldingPanel("formID:serverFP");
        serverFoldingPanel.assertElementExists();
        serverFoldingPanel.toggle().clickAndWait();
        serverImage.assertVisible(true);
        serverText.assertVisible(true);
        serverText.assertText("test server loading mode");

        //check 'client' loading mode
        element("formID:clientFP::caption").assertVisible(true); //todo: use FoldingPanelInspector in such cases
        ElementInspector clientImage = element("formID:clientImage");
        clientImage.assertElementExists();
        clientImage.assertVisible(false);
        ElementInspector clientText = element("formID:clientText");
        clientText.assertElementExists();
        clientText.assertVisible(false);

        FoldingPanelInspector clientFoldingPanel = foldingPanel("formID:clientFP");
        clientFoldingPanel.assertElementExists();
        clientFoldingPanel.toggle().click();
        clientImage.assertVisible(true);
        clientText.assertVisible(true);
        clientText.assertText("test client loading mode");

        //check 'ajax' loading mode
        FoldingPanelInspector ajaxFoldingPanel = foldingPanel("formID:ajaxFP");
        ajaxFoldingPanel.caption().assertVisible(true);
        ElementInspector ajaxImage = element("formID:ajaxImage");
        ajaxImage.assertElementExists(false);
        ElementInspector ajaxText = element("formID:ajaxText");
        ajaxText.assertElementExists(false);

        ajaxFoldingPanel.assertElementExists();
        ajaxFoldingPanel.toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        ajaxImage.assertVisible(true);
        ajaxText.assertVisible(true);
        ajaxText.assertText("test ajax loading mode");

        /*check that content loaded once*/
        //collapse all FoldingPanels
        serverFoldingPanel.toggle().click();
        clientFoldingPanel.toggle().click();
        ajaxFoldingPanel.toggle().click();

        serverFoldingPanel.toggle().click();
        serverImage.assertVisible(true);
        serverText.assertVisible(true);

        clientFoldingPanel.toggle().click();
        clientImage.assertVisible(true);
        clientText.assertVisible(true);

        ajaxFoldingPanel.toggle().click();
        ajaxImage.assertVisible(true);
        ajaxText.assertVisible(true);
    }

    @Test
    public void testStyles() {
        testAppFunctionalPage("/components/foldingpanel/foldingPanelStyles.jsf");

        FoldingPanelInspector foldingPanel = foldingPanel("formID:styledFP");
        foldingPanel.assertStyle("border: 5px solid orange;");
        foldingPanel.assertExpressionEquals("offsetWidth", 200);
        foldingPanel.caption().assertStyle("border: 2px dashed blue; background: LightBlue; color: DarkOrange; font-weight: bold;");

        ElementInspector toggle = foldingPanel.toggle();
        ElementInspector toggleImage = toggle.getElementsByTagName("img").get(0);
        toggleImage.assertAttributeStartsWith("src", "collapsed.gif");

        toggle.mouseOver();
        toggle.mouseMove();
        toggleImage.assertAttributeStartsWith("src", "collapsed_rollover.gif");
        toggle.mouseDown();
        toggleImage.assertAttributeStartsWith("src", "collapsed_pressed.gif");
        toggle.mouseUp();
        toggleImage.assertAttributeStartsWith("src", "collapsed_rollover.gif");
        toggle.click();
        toggle.mouseOut();
        foldingPanel.mouseMove();
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();

        foldingPanel.content().assertStyle("background: LightYellow; color: red; border: 2px solid springgreen;");

        toggleImage.assertAttributeStartsWith("src", "expanded.gif");

        //todo: uncomment it if 'JSFC-3294' in 'fixed' state
/*
    //expandedPressedImageUrl="expanded_pressed.gif"
    selenium.mouseDown(switchedId);
    assertTrue(selenium.getEval("var element = selenium.page().findElement('" + switchedId + "'); element.getAttribute('src');").startsWith("expanded_pressed.gif"));
    selenium.mouseUp(switchedId);
*/

        //expandedRolloverImageUrl="expanded_rollover.gif"
        toggle.mouseOver();
        toggle.mouseMove();
        toggleImage.assertAttributeStartsWith("src", "expanded_rollover.gif");
        toggle.mouseOut();
    }

}