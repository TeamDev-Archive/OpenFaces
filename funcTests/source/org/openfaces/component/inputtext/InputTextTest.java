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

package org.openfaces.component.inputtext;

import com.thoughtworks.selenium.Selenium;
import org.junit.Ignore;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.openfaces.test.RichFacesAjaxLoadingMode;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.openfaces.InputTextInspector;

import java.awt.event.KeyEvent;

/**
 * @author Vladimir Kurganov
 */
public class InputTextTest extends OpenFacesTestCase {
    @Test
    public void testTypedValue() {
        testAppFunctionalPage("/components/inputtext/inputTextFieldBaseFunctionality.jsf");

        ElementInspector value = element("formID:value");
        ElementInspector value2 = element("formID:value2");
        value.assertText("");
        value2.assertText("");
        InputTextInspector first = inputText("formID:first");
        InputTextInspector second = inputText("formID:second");
        first.assertValue("");
        second.assertValue("some prompt text");

        ElementInspector submit = element("formID:submit");

        // fill by empty values
        first.clear();
        first.assertValue("");
        second.clear();
        second.assertValue("");
        value.click();
        second.assertValue("some prompt text");
        submit.clickAndWait();
        value.assertText("");
        value2.assertText("");
        first.assertValue("");
        second.assertValue("some prompt text");

        // type own value
        first.clear();
        second.clear();
        first.type("val1");
        first.assertValue("val1");
        second.type("val2");
        second.assertValue("val2");
        submit.clickAndWait();
        value.assertText("val1");
        value2.assertText("val2");
        first.assertValue("val1");
        second.assertValue("val2");
    }

    @Test
    public void testFocusAndBlurEvent() {
        testAppFunctionalPage("/components/inputtext/inputTextFieldBaseFunctionality.jsf");

        ElementInspector value = element("formID:value");
        ElementInspector value2 = element("formID:value2");
        value.assertText("");
        value2.assertText("");
        InputTextInspector first = inputText("formID:first");
        InputTextInspector second = inputText("formID:second");
        first.assertValue("");
        second.assertValue("some prompt text");

        first.focus();
        first.assertValue("");
        first.fireEvent("onblur");
        first.assertValue("");

        second.focus();
        second.assertValue("");
        second.fireEvent("onblur");
        second.assertValue("some prompt text");

        first.type("val1");
        first.assertValue("val1");
        first.fireEvent("onblur");
        first.assertValue("val1");

        second.type("val2");
        second.assertValue("val2");
        second.fireEvent("onblur");
        second.assertValue("val2");
    }

    @Test
    public void testStyles() {
        testAppFunctionalPage("/components/inputtext/inputTextStyling.jsf");

        checkStyles();

        element("formID:submit").clickAndWait();
        checkStyles();
    }

    private void checkStyles() {
        InputTextInspector firstInput = inputText("formID:first");
        InputTextInspector secondInput = inputText("formID:second");

        firstInput.assertStyle("background: yellow; border: 3px solid pink; color: brown; font-weight: lighter");
        firstInput.assertWidth(160, 6); // width: 160px in strict mode results in adding 3*2 border width to the resulting element width

        secondInput.assertStyle("background: beige; border: 3px solid pink; color: brown; font-weight: bold");
        secondInput.assertWidth(160, 6); // width: 160px in strict mode results in adding 3*2 border width to the resulting element width

        // focus elements
        // first input
        firstInput.fireEvent("onfocus");
        firstInput.assertStyle("background: beige; border: 1px solid green; color: brown; font-weight: bold");
        firstInput.assertWidth(160, 2);
        firstInput.fireEvent("onblur");

        // second input
        secondInput.fireEvent("onfocus");
        secondInput.assertStyle("background: beige; border: 1px solid green; color: brown; font-weight: bold");
        secondInput.assertWidth(160, 2);
        secondInput.fireEvent("onblur");

        // rollover elements
        // first input
        firstInput.mouseOver();
        firstInput.mouseMove();
        firstInput.assertStyle("background: red; border: 4px dotted darkblue; color: brown; font-weight: lighter");
        firstInput.assertWidth(160, 8);
        firstInput.mouseOut();

        // second input
        secondInput.mouseOver();
        secondInput.mouseMove();
        secondInput.assertStyle("background: red; border: 4px dotted darkblue; color: brown; font-weight: bold");
        secondInput.assertWidth(160, 8);
        secondInput.mouseOut();
    }

    @Test
    public void testValueChangeListener() {
        testAppFunctionalPage("/components/inputtext/inputTextValueChangeListener.jsf");

        ElementInspector asAttributeOutput = element("formID:asAttributeOutput");
        ElementInspector asTagOutput = element("formID:asTagOutput");
        asAttributeOutput.assertText("0");
        asTagOutput.assertText("false");

        inputText("formID:first").type("Yellow");
        inputText("formID:second").type("Yellow");

        element("formID:submit").clickAndWait();

        asAttributeOutput.assertText("1");
        asTagOutput.assertText("true");
    }

    @Test
    public void testClientSideEvents() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/inputtext/inputTextEvents.jsf");

        // onfocus
        element("formID:focusBlur").click();
        assertTrue(selenium.isTextPresent("onfocus works"));
        assertTrue(selenium.isTextPresent("focus"));

        // onclick
        element("formID:clickDblclick").click();
        assertTrue(selenium.isTextPresent("onclick works"));
        assertTrue(selenium.isTextPresent("click"));

        // onblur
        selenium.fireEvent("formID:focusBlur", "blur");
        assertTrue(selenium.isTextPresent("onblur works"));
        assertTrue(selenium.isTextPresent("blur"));

        // onmouseover
        element("formID:mouseoverMouseout").mouseOver();
        assertTrue(selenium.isTextPresent("onmouseover works"));
        assertTrue(selenium.isTextPresent("mouseover"));

        // onmouseout
        element("formID:mouseoverMouseout").mouseOut();
        assertTrue(selenium.isTextPresent("onmouseout works"));
        assertTrue(selenium.isTextPresent("mouseout"));

        // onmouseup
        element("formID:mouseupMousedown").mouseUp();
        assertTrue(selenium.isTextPresent("onmouseup works"));
        assertTrue(selenium.isTextPresent("mouseup"));

        // onmousedown
        element("formID:mouseupMousedown").mouseDown();
        assertTrue(selenium.isTextPresent("onmousedown works"));
        assertTrue(selenium.isTextPresent("mousedown"));

        InputTextInspector keyChangeInput = inputText("formID:keyChange");

        // onkeypress
        keyChangeInput.keyPress(KeyEvent.VK_O);
        assertTrue(selenium.isTextPresent("onkeypress works"));
        assertTrue(selenium.isTextPresent("keypress"));

        // onkeydown
        keyChangeInput.keyDown(KeyEvent.VK_P);
        assertTrue(selenium.isTextPresent("onkeydown works"));
        assertTrue(selenium.isTextPresent("keydown"));

        // onkeyup
        keyChangeInput.keyUp(KeyEvent.VK_E);
        assertTrue(selenium.isTextPresent("onkeyup works"));
        assertTrue(selenium.isTextPresent("keyup"));

        // onchange
        keyChangeInput.type("change");
        selenium.fireEvent("formID:keyChange", "blur");
        assertTrue(selenium.isTextPresent("onchange works"));
        assertTrue(selenium.isTextPresent("change"));

        // ondblclick
        element("formID:clickDblclick").doubleClick();
        assertTrue(selenium.isTextPresent("ondblclick works"));
        assertTrue(selenium.isTextPresent("dblclick"));

        // onmousemove
        element("formID:mousemove").mouseMove();
        assertTrue(selenium.isTextPresent("onmousemove works"));
        assertTrue(selenium.isTextPresent("mousemove"));
    }

    @Test
    @Ignore
    // revive this test when RichFaces 4 is fully functional
    public void testReRenderingThroughA4J() {
        testAppFunctionalPage("/components/inputtext/inputText_a4j.jsf");

        InputTextInspector inputText = inputText("formID:inputTextID");
        inputText.assertValue("ajax4jsf");
        inputText.type("change value");
        element("formID:refresher").click();
        RichFacesAjaxLoadingMode.getInstance().waitForLoad();
        inputText.assertValue("ajax4jsf");
        inputText.assertWidth(230, 4);

        // check rollover style
        inputText.mouseOver();
        inputText.mouseMove();
        inputText.assertStyle("border: 2px dotted darkblue");
        inputText.assertWidth(230, 4);
        inputText.mouseOut();

        inputText.clear();
        inputText.fireEvent("onblur");
        sleep(1000);
        inputText.assertValue("ajax4jsf prompt text");
        inputText.assertStyle("background: burlywood; color: #7e7e7e");
    }

}
