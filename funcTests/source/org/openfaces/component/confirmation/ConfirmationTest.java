/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.confirmation;

import com.thoughtworks.selenium.Selenium;
import org.junit.Ignore;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.openfaces.test.RichFacesAjaxLoadingMode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.seleniuminspector.openfaces.ConfirmationInspector;
import org.seleniuminspector.openfaces.PopupLayerInspector;

/**
 * @author Darya Shumilina
 */
public class ConfirmationTest extends OpenFacesTestCase {

    //todo: uncomment when JSFC-3627 is fixed
    @Ignore
    @Test
    public void _testConfirmationReRenderThroughA4J() throws InterruptedException {
        testAppFunctionalPage("/components/confirmation/confirmation_a4j.jsf");
        Selenium selenium = getSelenium();
        ConfirmationInspector confirmation = confirmation("formID:confirmationID");
        PopupLayerInspector confirmedPopup = popupLayer("formID:confirmedPopup");
        for (int i = 0; i < 3; i++) {
            selenium.click("formID:button1");
            String oldSource = selenium.getHtmlSource();
            confirmation.okButton().click();
            confirmedPopup.assertVisible(true);
            selenium.click("formID:closer");
            selenium.click("formID:refresher");
            RichFacesAjaxLoadingMode.getInstance().waitForLoad();
            selenium.click("formID:button1");
            String newSource = selenium.getHtmlSource();
            sleep(500);
            confirmation.okButton().click();
            sleep(500);
            confirmedPopup.assertVisible(true);
            selenium.click("formID:closer");
            assertTrue(!newSource.equals(oldSource));
        }
    }

    //todo: uncomment when JSFC-3627 is fixed
    @Ignore
    @Test
    public void _testConfirmationInvocationThroughA4JControl() {
        testAppFunctionalPage("/components/confirmation/confirmation_a4j.jsf");
        Selenium selenium = getSelenium();
        ConfirmationInspector confirmation = confirmation("formID:confirmation_a4j_ID");
        PopupLayerInspector confirmedPopup = popupLayer("formID:confirmed_a4j_Popup");
        for (int i = 0; i < 3; i++) {
            selenium.click("formID:a4j_button");
            String oldSource = selenium.getHtmlSource();
            confirmation.okButton().click();
            confirmedPopup.assertVisible(true);
            selenium.click("formID:closer_a4j");
            selenium.click("formID:refresher_a4j");
            RichFacesAjaxLoadingMode.getInstance().waitForLoad();
            selenium.click("formID:button1");
            String newSource = selenium.getHtmlSource();
            confirmation.okButton().click();
            confirmedPopup.assertVisible(true);
            selenium.click("formID:closer_a4j");
            assertTrue(!newSource.equals(oldSource));
        }
    }

    @Test
    public void testForCommandButtonWithAction() {
        testAppFunctionalPage("/components/confirmation/confirmationTestInvokers.jsf");
        element("form1:buttonWithAction").click();
        confirmation("form1:confirmForButtonWithAction").okButton().clickAndWait();
        element("form1:actionConfirmedText1").assertText("Confirmed: true");
    }

    @Test
    public void testForCommandButtonInvoker() {
        testAppFunctionalPage("/components/confirmation/confirmationTestInvokers.jsf");
        Selenium selenium = getSelenium();
        element("form1:buttonWithOnclick").click();
        confirmation("form1:confirmForButtonWithOnclick").okButton().click();
        assertTrue(selenium.isTextPresent("button with onclick"));
    }

    @Test
    public void testForCommandLinkInvoker() {
        testAppFunctionalPage("/components/confirmation/confirmationTestInvokers.jsf");
        WebElement commandLink = getDriver().findElement(By.id("form1:commandLink"));
        commandLink.click();
        WebElement confirmForCommandLink = getDriver().findElement(By.id("form1:confirmForCommandLink::yes_button"));
        confirmForCommandLink.click();
        element("form1:actionConfirmedText").assertText("Confirmed: true");
    }

    @Test
    public void testForOutputLinkInvoker() {
        testAppFunctionalPage("/components/confirmation/confirmationTestInvokers.jsf");

        element("form1:outputLinkID").click();
        confirmation("form1:outputLinkWithOnclick").okButton().click();
        element("outputLinkPrint").assertText("h:outputLink works");

        //check server action
        //todo: uncomment if JSFC-2950 fixed
/*
    selenium.click("form1:outputLinkNavigator");
    selenium.click("form1:outputLinkNavigatorConfirmation::yes_button");
    waitForPageToLoad();
    assertEquals("TeamDev", selenium.getTitle());
*/
    }

    @Test
    public void testConfirmationHTMLInvoker() {
        testAppFunctionalPage("/components/confirmation/confirmationTestInvokers.jsf");
        element("htmlButton").click();
        confirmation("form1:invokedByHTMLElementID").okButton().click();
        assertTrue(getSelenium().isTextPresent("HTML button"));
    }

    @Test
    public void testRunConfirmedFunctionFunction() {
        testAppFunctionalPage("/components/confirmation/confirmationTestInvokers.jsf");
        element("form1:linkInvokerID1").click();
        confirmation("form1:q_runConfirmedFunctionID").okButton().click();
        element("empty3").assertText("runConfirmedFunction works");
    }

    @Test
    public void testSetConfirmationTextsFunction() {
        testAppFunctionalPage("/components/confirmation/confirmationTestInvokers.jsf");
        element("form1:linkInvokerID2").click();
        ConfirmationInspector confirmation = confirmation("form1:setConfirmationTexts");
        confirmation.message().assertText("header custom text");
        confirmation.details().assertText("details custom text");
        confirmation.okButton().assertValue("accept");
        confirmation.cancelButton().assertValue("decline");
        confirmation.okButton().click();
    }

    //todo: uncomment this test if JSFC-2683 is fixed
    @Ignore
    @Test
    public void _testInputTextInside() {
        testAppFunctionalPage("/components/confirmation/confirmationContentWithArbitraryComponents.jsf");
        Selenium selenium = getSelenium();
        selenium.click("form:withInputTextInside");
        selenium.isVisible("form:confirm_withInputTextInside");
        //try to type in caption
        assertEquals("Caption input", selenium.getText("form:captionInputID"));
        selenium.type("form:captionInputID", "Test ");
        assertEquals("Test Caption input", selenium.getText("form:captionInputID"));

        //try to type in icon
        assertEquals("Icon input", selenium.getText("form:iconInputID"));
        selenium.type("form:iconInputID", "Test ");
        assertEquals("Test Icon input", selenium.getText("form:iconInputID"));

        //try to type in message
        assertEquals("Message input", selenium.getText("form:messageInputID"));
        selenium.type("form:messageInputID", "Test ");
        assertEquals("Test Message input", selenium.getText("form:messageInputID"));

        //try to type in details
        assertEquals("Details input", selenium.getText("form:detailsInputID"));
        selenium.type("form:detailsInputID", "Test ");
        assertEquals("Test Details input", selenium.getText("form:detailsInputID"));

        selenium.click("form:confirm_withInputTextInside::yes_button");
        assertTrue(selenium.isAlertPresent());
        assertEquals("Done", selenium.getAlert());
    }

    @Test
    public void testOutputTextInside() {
        testAppFunctionalPage("/components/confirmation/confirmationContentWithArbitraryComponents.jsf");

        element("form:withOutputTextInside").click();
        element("form:detailsOutputID").assertText("Details output");
        element("form:messageOutputID").assertText("Message output");
        element("form:captionOutputID").assertText("Caption output");
        element("form:iconOutputID").assertText("Icon output");
        confirmation("form:confirm_withOutputTextInside").okButton().click();
        getSelenium().isTextPresent("Confirmation with output text");
    }

    @Test
    public void testGraphicImageInside() {
        testAppFunctionalPage("/components/confirmation/confirmationContentWithArbitraryComponents.jsf");

        element("form:withGraphicImageInside").click();
        element("form:captionImageID").assertVisible(true);
        element("form:iconImageID").assertVisible(true);
        element("form:detailsImageID").assertVisible(true);
        element("form:messageImageID").assertVisible(true);
        confirmation("form:confirm_withGraphicImageInside").okButton().click();
        getSelenium().isTextPresent("Confirmation with image");
    }

    //todo: uncomment when JSFC-3627 is fixed
    @Ignore
    @Test
    public void _testConfirmationClientSideEvents() {
        testAppFunctionalPage("/components/confirmation/confirmation.jsf");
        Selenium selenium = getSelenium();
        // onclick event
        selenium.click("onclickInvoker1");
        confirmation("fn:onclick_conf").okButton().click();
        assertTrue(selenium.isTextPresent("onclick action"));
        assertTrue(selenium.isTextPresent("onclick works"));
        assertTrue(selenium.isTextPresent("click"));

        // ondblclick
        selenium.click("ondblclick");
        selenium.doubleClick("fn:ondblclick_conf");
        confirmation("fn:ondblclick_conf").okButton().click();
        assertTrue(selenium.isTextPresent("ondblclick action"));
        assertTrue(selenium.isTextPresent("ondblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        // onmousedown
        selenium.click("onmousedown");
        selenium.mouseDown("fn:onmousedown_conf");
        confirmation("fn:onmousedown_conf").okButton().click();
        assertTrue(selenium.isTextPresent("onmousedown action"));
        assertTrue(selenium.isTextPresent("onmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        // onmouseover
        selenium.click("onmouseover");
        selenium.mouseOver("fn:onmouseover_conf");
        confirmation("fn:onmouseover_conf").okButton().click();
        assertTrue(selenium.isTextPresent("onmouseover action"));
        assertTrue(selenium.isTextPresent("onmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        // onmouseup
        selenium.click("onmouseup");
        selenium.mouseUp("fn:onmouseup_conf");
        confirmation("fn:onmouseup_conf").okButton().click();
        assertTrue(selenium.isTextPresent("onmouseup action"));
        assertTrue(selenium.isTextPresent("onmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        // onmouseout
        selenium.click("onmouseout");
        selenium.mouseOut("fn:onmouseout_conf");
        confirmation("fn:onmouseout_conf").okButton().click();
        assertTrue(selenium.isTextPresent("onmouseout action"));
        assertTrue(selenium.isTextPresent("onmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        // onmousemove
        selenium.click("onmousemove");
        selenium.mouseMove("fn:onmousemove_conf");
        confirmation("fn:onmousemove_conf").okButton().click();
        assertTrue(selenium.isTextPresent("onmousemove action"));
        assertTrue(selenium.isTextPresent("onmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));

        // onkeydown
        selenium.click("onkeydown");
        selenium.keyDown("fn:onkeydown_conf", "13");
        confirmation("fn:onkeydown_conf").okButton().click();
        assertTrue(selenium.isTextPresent("onkeydown action"));
        assertTrue(selenium.isTextPresent("onkeydown works"));
        assertTrue(selenium.isTextPresent("onkeydown"));

        // onkeypress
        selenium.click("onkeypress");
        selenium.keyPress("fn:onkeypress_conf", "13");
        confirmation("fn:onkeypress_conf").okButton().click();
        assertTrue(selenium.isTextPresent("onkeypress action"));
        assertTrue(selenium.isTextPresent("onkeypress works"));
        assertTrue(selenium.isTextPresent("onkeypress"));

        // onkeyup
        selenium.click("onkeyup");
        selenium.keyUp("fn:onkeyup_conf", "13");
        confirmation("fn:onkeyup_conf").okButton().click();
        assertTrue(selenium.isTextPresent("onkeyup action"));
        assertTrue(selenium.isTextPresent("onkeyup works"));
        assertTrue(selenium.isTextPresent("onkeyup"));
    }

    @Test
    public void testStyles() {
        testAppFunctionalPage("/components/confirmation/confirmationStyles.jsf");

        element("invoker").click();
        ConfirmationInspector confirmation = confirmation("form1:styled");
        confirmation.buttonArea().assertStyle("background: blue");
        confirmation.assertStyle("border: 3px solid black");
        confirmation.cancelButton().assertStyle("border: 1px dashed black");
        confirmation.caption().assertStyle("border: 1px dashed white");
        confirmation.details().assertStyle("color: red");
        confirmation.iconArea().assertStyle("border: 1px solid orange");
        confirmation.message().assertStyle("color: blue");
        confirmation.content().assertStyle("background: beige");
        confirmation.modalLayer().assertStyle("background: gray");
        confirmation.okButton().assertStyle("border: 1px dashed pink");

        confirmation.okButton().mouseOver();
        confirmation.okButton().mouseMove();

        confirmation.okButton().assertStyle("border: 3px dashed pink");
        confirmation.buttonArea().assertStyle("background: azure");
        confirmation.assertStyle("border: 2px dotted blue");

        confirmation.okButton().mouseOut();
        confirmation.cancelButton().mouseOver();
        confirmation.cancelButton().mouseMove();

        confirmation.cancelButton().assertStyle("border: 3px dashed black");
        confirmation.details().assertStyle("font-weight: bold");
        confirmation.iconArea().assertStyle("border: 3px solid red");
        confirmation.message().assertStyle("color: green");
        confirmation.content().assertStyle("background: orange");
    }

}