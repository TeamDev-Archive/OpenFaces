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
package org.openfaces.test;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.html.InputInspector;
import org.seleniuminspector.openfaces.CalendarInspector;
import org.seleniuminspector.openfaces.DropDownFieldInspector;
import org.seleniuminspector.openfaces.InputTextInspector;

import java.awt.event.KeyEvent;

/**
 * @author Tatyana Matveyeva
 */
public class ValidationTest extends OpenFacesTestCase {

     @Test
    public void testServerDefaultPresentation() {
        closeBrowser();
        testAppFunctionalPage("/components/validation/serverDefaultPresentation.jsf");
        isNotDefaultPresentation();
        element("testForm:fillInvalidData").click();
        ElementInspector submit = element("testForm:fmSubmit");
        submit.click();
        waitForPageToLoad();
        isDefaultPresentation();
        fillValidDataForClientSideAPI();
        submit.click();
        waitForPageToLoad();
        isNotDefaultPresentation();
    }

     @Test
    public void testClientDefaultPresentation() {
        closeBrowser();
        testAppFunctionalPage("/components/validation/clientDefaultPresentation.jsf");
        isNotDefaultPresentation();
        element("testForm:fillInvalidData").click();
        ElementInspector submit = element("testForm:fmSubmit");
        submit.click();
        sleep(1000);
        isDefaultPresentation();
        fillValidDataForClientSideAPI();
        submit.click();
        waitForPageToLoad();
        sleep(1000);
        isNotDefaultPresentation();
    }

     @Test
    public void testOFInputComponents() {
        testAppFunctionalPage("/components/validation/openFacesInputComponents.jsf");
        String formName;
        //Client-side validation
        formName = "clientValidation";
        ElementInspector clientSubmit = element(formName + ":fmSubmit");
        clientSubmit.click();
        isValidationFailed(formName);

        typeData(formName);
        clientSubmit.click();
        waitForPageToLoad();
        isValidationPassed(formName);
        //server-side validation
        formName = "serverValidation";
        ElementInspector serverSubmit = element(formName + ":fmSubmit");
        serverSubmit.click();
        waitForPageToLoad();
        isValidationFailed(formName);

        typeData(formName);
        serverSubmit.click();
        waitForPageToLoad();
        isValidationPassed(formName);
    }

     @Test
    public void testClientSideAPI() {
        testAppFunctionalPage("/components/validation/clientSideAPI.jsf");
        String formName;
        formName = "testForm";
        //Submit form without validation
        fillInvalidDataForClientSideAPI();
        ElementInspector withoutValidationBtn = element(formName + ":withoutValidation");
        withoutValidationBtn.click();
        waitForPageToLoad();
        isFailedClientSideAPI();
        fillValidDataForClientSideAPI();
        withoutValidationBtn.click();
        waitForPageToLoad();
        isPassedClientSideAPI();
        //validate form by ID
        fillInvalidDataForClientSideAPI();
        ElementInspector validateFormByIdBtn = element(formName + ":validateFormById");
        validateFormByIdBtn.click();
        isFailedClientSideAPI();
        fillValidDataForClientSideAPI();
        validateFormByIdBtn.click();
        isPassedClientSideAPI();
        //validate form
        fillInvalidDataForClientSideAPI();
        ElementInspector validateFormBtn = element(formName + ":validateForm");
        validateFormBtn.click();
        isFailedClientSideAPI();
        fillValidDataForClientSideAPI();
        validateFormBtn.click();
        isPassedClientSideAPI();
        //validate enclosing form
        fillInvalidDataForClientSideAPI();
        ElementInspector validateEnclFormBtn = element(formName + ":validateEnclForm");
        validateEnclFormBtn.click();
        isFailedClientSideAPI();
        fillValidDataForClientSideAPI();
        validateEnclFormBtn.click();
        isPassedClientSideAPI();
        //validate components by IDs
        fillInvalidDataForClientSideAPI();
        ElementInspector validateByIdBtn = element(formName + ":validateById");
        validateByIdBtn.click();
        isFailedClientSideAPI();
        fillValidDataForClientSideAPI();
        validateByIdBtn.click();
        isPassedClientSideAPI();
        //validate components
        fillInvalidDataForClientSideAPI();
        ElementInspector validateBtn = element(formName + ":validate");
        validateBtn.click();
        isFailedClientSideAPI();
        fillValidDataForClientSideAPI();
        validateBtn.click();
        isPassedClientSideAPI();
    }

     @Test
    public void testComponentWideValidation() throws InterruptedException {
        closeBrowser();
        testAppFunctionalPage("/components/validation/componentWideValidation.jsf");
        InputTextInspector doubleRangeInput = inputText("testForm:validDROnSubmit");
        doubleRangeInput.type("1");
        InputTextInspector urlInput = inputText("testForm:urlOnSubmit");
        urlInput.type("not url");
        ElementInspector submit = element("testForm:submitBt");

        submit.click();
        ElementInspector onSubmitMessage = element("testForm:inputOnSubmitMessage");
        onSubmitMessage.assertContainsText("Value is required.");
        ElementInspector dateChooserMessage = element("testForm:dchOnSubmitInput");
        dateChooserMessage.assertContainsText("Value is required.");
        ElementInspector doubleRangeMessage = element("testForm:ValidDROnSubmitMessage");
        doubleRangeMessage.assertContainsText("Validation Error");
        ElementInspector urlMessage = element("testForm:UrlOnSubmitMessage");
        urlMessage.assertText("Validation error occurs Entered value is not url");
        ElementInspector inputOffMessage = element("testForm:inputOffMessage");
        inputOffMessage.assertText("");
        ElementInspector dateChooserOffMessage = element("testForm:dchOffInput");
        dateChooserOffMessage.assertText("");
        ElementInspector dateRangeOffMessage = element("testForm:ValidDROffMessage");
        dateRangeOffMessage.assertText("");
        ElementInspector urlOffMessage = element("testForm:UrlOffMessage");
        urlOffMessage.assertText("");
        ElementInspector onDemandMessage = element("testForm:inputOnDemandMessage");
        onDemandMessage.assertText("");
        ElementInspector dateChooserOnDemandMessage = element("testForm:dchOnDemandInput");
        dateChooserOnDemandMessage.assertText("");
        ElementInspector doubleRangeOnDemandMessage = element("testForm:ValidDROnDemandMessage");
        doubleRangeOnDemandMessage.assertText("");
        ElementInspector urlOnDemandMessage = element("testForm:UrlOnDemandMessage");
        urlOnDemandMessage.assertText("");
        inputText("testForm:inputOnSubmit").clear();
        inputText("testForm:inputOnSubmit").type("Text");
        dateChooser("testForm:dchOnSubmit").field().type("Mar 20, 2007");
        doubleRangeInput.clear();
        doubleRangeInput.type("0.1");
        urlInput.clear();
        urlInput.type("http://www.teamdev.com");
        InputTextInspector doubleRangeOffInput = inputText("testForm:validDROff");
        doubleRangeOffInput.clear();
        doubleRangeOffInput.type("1");
        InputTextInspector urlOffInput = inputText("testForm:urlOff");
        urlOffInput.clear();
        urlOffInput.type("not url");
        InputTextInspector dobleRangeOnDemandInput = inputText("testForm:validDROnDemand");
        dobleRangeOnDemandInput.clear();
        dobleRangeOnDemandInput.type("1");
        InputTextInspector urlOnDemand = inputText("testForm:urlOnDemand");
        urlOnDemand.clear();
        urlOnDemand.type("not url");

        submit.click();
        waitForPageToLoad();
        onSubmitMessage.assertText("");
        dateChooserMessage.assertText("");
        doubleRangeMessage.assertText("");
        urlMessage.assertText("");
        inputOffMessage.assertContainsText("Value is required.");
        dateChooserOffMessage.assertContainsText("Value is required.");

        assertTrue(dateRangeOffMessage.text().contains("Validation Error: Specified attribute is not between the expected values of 0.001 and 0.999.") ||
                dateRangeOffMessage.text().contains("Validation Error"));

        urlOffMessage.assertContainsText("Validation error occurs Entered value is not url");
        onDemandMessage.assertContainsText("Value is required.");
        dateChooserOnDemandMessage.assertContainsText("Value is required.");

        assertTrue(doubleRangeOnDemandMessage.text().contains("Validation Error: Specified attribute is not between the expected values of 0.001 and 0.999.") ||
                doubleRangeOnDemandMessage.text().contains("Validation Error"));

        urlOnDemandMessage.assertText("Validation error occurs Entered value is not url");
        inputText("testForm:inputOff").clear();
        inputText("testForm:inputOff").type("Text");
        dateChooser("testForm:dchOff").field().clear();
        dateChooser("testForm:dchOff").field().type("Mar 20, 2007");
        doubleRangeOffInput.clear();
        doubleRangeOffInput.type("0.1");
        inputText("testForm:urlOff").clear();
        inputText("testForm:urlOff").type("http://www.teamdev.com");
        inputText("testForm:inputOnDemand").clear();
        inputText("testForm:inputOnDemand").type("Text");
        dateChooser("testForm:dchOnDemand").field().clear();
        dateChooser("testForm:dchOnDemand").field().type("Mar 20, 2007");
        dobleRangeOnDemandInput.clear();
        dobleRangeOnDemandInput.type("0.1");
        urlOnDemand.clear();
        urlOnDemand.type("http://www.teamdev.com");
        submit.click();
        waitForPageToLoad();
        onSubmitMessage.assertText("");
        dateChooserMessage.assertText("");
        doubleRangeMessage.assertText("");
        urlMessage.assertText("");
        inputOffMessage.assertText("");
        dateChooserOffMessage.assertText("");
        dateRangeOffMessage.assertText("");
        urlOffMessage.assertText("");
        onDemandMessage.assertText("");
        dateChooserOnDemandMessage.assertText("");
        doubleRangeOnDemandMessage.assertText("");
        urlOnDemandMessage.assertText("");
    }

//  protected void assertSubstring(String substring, String fullString) {
//    assertTrue(fullString.contains(substring));
//  }

    protected void assertSubstringIgnoreCase(String substring, String fullString) {
        String fullUpperCase = fullString.toUpperCase();
        String subUpperCase = substring.toUpperCase();
        assertTrue(fullUpperCase.contains(subUpperCase));
    }

     @Test
    public void testOpenFacesValidators() {
        testAppFunctionalPage("/components/validation/openFacesValidators.jsf");
        //server-side
        final String serverValidationForm = "serverValidation:";
        isPassedOFValidators(serverValidationForm);
        element(serverValidationForm + "fillInvalid").click();
        ElementInspector serverValidationSubmit = element(serverValidationForm + "submitBt");
        serverValidationSubmit.click();
        waitForPageToLoad();
        isFailedOFValidators(serverValidationForm);
        element(serverValidationForm + "fillValid").click();
        serverValidationSubmit.click();
        waitForPageToLoad();
        isPassedOFValidators(serverValidationForm);
        //client-side
        final String clientValidationFormName = "clientValidation:";
        isPassedOFValidators(clientValidationFormName);
        element(clientValidationFormName + "fillInvalid").click();
        ElementInspector clientValidationSubmit = element(clientValidationFormName + "submitBt");
        clientValidationSubmit.click();
        isFailedOFValidators(clientValidationFormName);
        element(clientValidationFormName + "fillValid").click();
        clientValidationSubmit.click();
        waitForPageToLoad();
        isPassedOFValidators(clientValidationFormName);
    }

     @Test
    public void testStandardValidators() {
        testAppFunctionalPage("/components/validation/standardValidators.jsf");
        //server-side
        final String serverValidationFormName = "serverValidation:";
        isPassedStandardValidators(serverValidationFormName);
        element(serverValidationFormName + "fillInvalid").click();
        ElementInspector serverValidationSubmit = element(serverValidationFormName + "submitBt");
        serverValidationSubmit.clickAndWait();
        waitForPageToLoad();
        isFailedStandardValidators(serverValidationFormName);
        element(serverValidationFormName + "fillValid").click();
        serverValidationSubmit.clickAndWait();
        isPassedStandardValidators(serverValidationFormName);
        //client-side
        final String clientValidationFormName = "clientValidation:";
        isPassedStandardValidators(clientValidationFormName);
        element(clientValidationFormName + "fillInvalid").click();
        ElementInspector clientValidationSubmit = element(clientValidationFormName + "submitBt");
        clientValidationSubmit.click();
        isFailedStandardValidators(clientValidationFormName);
        element(clientValidationFormName + "fillValid").click();
        clientValidationSubmit.click();
        waitForPageToLoad();
        isPassedStandardValidators(clientValidationFormName);
    }

     @Test
    public void testWithStandardInputs() {
        testAppFunctionalPage("/components/validation/validationWithStandardInputs.jsf");
        //server-side failed
        final String serverValidationFormName = "serverValidation:";
        isPassedStandardInputs(serverValidationFormName);
        ElementInspector serverValidationSubmit = element(serverValidationFormName + "submitBt");
        serverValidationSubmit.click();
        waitForPageToLoad();
        isFailedStandardInputs(serverValidationFormName);
        element(serverValidationFormName + "selectBooleanCheckboxMessage").assertText("this checkBox should be checked");
        element(serverValidationFormName + "selectOneMenuMessage").assertText("Should be 'dogs'");
        //client-side failed
        final String clientValidationFormName = "clientValidation:";
        isPassedStandardInputs(clientValidationFormName);
        ElementInspector clientValidationSubmit = element(clientValidationFormName + "submitBt");
        clientValidationSubmit.click();
        isFailedStandardInputs(clientValidationFormName);
        //server-side passed
        testAppFunctionalPage("/components/validation/validationWithStandardInputs.jsf");
        fillDataStandardInputs(serverValidationFormName);
        serverValidationSubmit.click();
        waitForPageToLoad();
        isPassedStandardInputs(serverValidationFormName);
        //client-side passed
        new InputInspector(clientValidationFormName + "inputSecret").type("password");
        clientValidationSubmit.click();
        waitForPageToLoad();
        isPassedStandardInputs(clientValidationFormName);
    }

    private void fillDataStandardInputs(String formName) {
        Selenium selenium = getSelenium();
        selenium.type(formName + "inputSecret", "password");
        selenium.type(formName + "inputText", "item 1");
        selenium.type(formName + "inputTextarea", "text");
        selenium.click(formName + "selectBooleanCheckbox");
        selenium.click("//*[@value='item 1'][@name='" + formName + "selectManyCheckbox']");
        selenium.addSelection(formName + "selectManyListbox", "label=dogs");
        selenium.addSelection(formName + "selectManyMenu", "label=dogs");
        selenium.select(formName + "selectOneListbox", "label=dogs");
        selenium.select(formName + "selectOneMenu", "label=dogs");
        selenium.click("//*[@value='item 1'][@name='" + formName + "selectOneRadio']");
    }

    private void isPassedStandardInputs(String formName) {
        element(formName + "inputSecretMessage").assertText("");
        element(formName + "inputTextMessage").assertText("");
        element(formName + "inputTextareaMessage").assertText("");
        element(formName + "selectManyCheckboxMessage").assertText("");
        element(formName + "selectManyListboxMessage").assertText("");
        element(formName + "selectManyMenuMessage").assertText("");
        element(formName + "selectOneListboxMessage").assertText("");
        element(formName + "selectOneRadioMessage").assertText("");
    }

    private void isFailedStandardInputs(String formName) {
        element(formName + "inputSecretMessage").assertContainsText("Value is required.");
        element(formName + "inputTextMessage").assertContainsText("Value is required.");
        element(formName + "inputTextareaMessage").assertContainsText("Value is required.");
        element(formName + "selectManyCheckboxMessage").assertContainsText("Value is required.");
        element(formName + "selectManyListboxMessage").assertContainsText("Value is required.");
        element(formName + "selectManyMenuMessage").assertContainsText("Value is required.");
        element(formName + "selectOneListboxMessage").assertContainsText("Value is required.");
        element(formName + "selectOneRadioMessage").assertContainsText("Value is required.");
    }


    private void isPassedStandardValidators(String formName) {
        element(formName + "RequiredMessage").assertText("");
        element(formName + "ValidDRMessage").assertText("");
        element(formName + "ValidLRMessage").assertText("");
        element(formName + "ValidLMessage").assertText("");
        element(formName + "IntConvMessage").assertText("");
        element(formName + "DoubleConvMessage").assertText("");
        element(formName + "ByteConvMessage").assertText("");
        element(formName + "ShortConvMessage").assertText("");
        element(formName + "FloatConvMessage").assertText("");
        element(formName + "DateConvMessage").assertText("");
        element(formName + "DateConv2Message").assertText("");
        element(formName + "NumberConvMessage").assertText("");
    }

    private void isFailedStandardValidators(String formName) {
        Selenium selenium = getSelenium();
        assertTrue(selenium.getText(formName + "RequiredMessage").contains("Validation Error"));

        ElementInspector doubleRangeMsg = element(formName + "ValidDRMessage");
        assertTrue(
                doubleRangeMsg.text().contains("Validation Error: Specified attribute is not between the expected values of 0.001 and 0.999.") ||
                        doubleRangeMsg.text().contains("Validation Error: Specified attribute is not between the expected values of 0,001 and 0,999.") ||
                        doubleRangeMsg.text().equals("Validation Error"));

        ElementInspector longRangeMsg = element(formName + "ValidLRMessage");
        assertTrue(
                longRangeMsg.text().contains("Validation Error: Specified attribute is not between the expected values of 10 and 100.") ||
                        longRangeMsg.text().equals("Validation Error"));


        element(formName + "ValidLMessage").assertContainsText("Validation Error");
        if (formName.equals("serverValidation:")) {
            assertSubstringIgnoreCase("serverValidation:intConv: 'Non-integer value' must be a number consisting of one or more digits.", selenium.getText(formName + "IntConvMessage"));
            assertSubstringIgnoreCase("serverValidation:doubleConv: 'Non-double value' must be a number consisting of one or more digits.", selenium.getText(formName + "DoubleConvMessage"));
            assertSubstringIgnoreCase("serverValidation:byteConv: '2V0' must be a number between 0 and 255.", selenium.getText(formName + "ByteConvMessage"));
            assertSubstringIgnoreCase("serverValidation:shortConv: '999999999' must be a number consisting of one or more digits.", selenium.getText(formName + "ShortConvMessage"));
            assertSubstringIgnoreCase("serverValidation:floatConv: '2V0' must be a number consisting of one or more digits.", selenium.getText(formName + "FloatConvMessage"));
            assertSubstringIgnoreCase("serverValidation:dateConv: '12/02/2007' could not be understood as a date.", selenium.getText(formName + "DateConvMessage"));
            assertSubstringIgnoreCase("serverValidation:dateConv2: '12/02/2007' could not be understood as a date and time.", selenium.getText(formName + "DateConv2Message"));
            assertSubstringIgnoreCase("serverValidation:numberConv: 'Not Number' is not a number pattern.", selenium.getText(formName + "NumberConvMessage"));
        } else {
            assertSubstringIgnoreCase("Conversion error", selenium.getText(formName + "IntConvMessage"));
            assertSubstringIgnoreCase("Conversion error", selenium.getText(formName + "DoubleConvMessage"));
            assertSubstringIgnoreCase("Conversion error", selenium.getText(formName + "ByteConvMessage"));
            assertSubstringIgnoreCase("Conversion error", selenium.getText(formName + "ShortConvMessage"));
            assertSubstringIgnoreCase("Conversion error", selenium.getText(formName + "FloatConvMessage"));
            assertSubstringIgnoreCase("Conversion error", selenium.getText(formName + "DateConvMessage"));
            assertSubstringIgnoreCase("Conversion error", selenium.getText(formName + "DateConv2Message"));
            assertSubstringIgnoreCase("Conversion error", selenium.getText(formName + "NumberConvMessage"));
        }
    }


    private void isFailedOFValidators(String formName) {
        element(formName + "equalMessage").assertText("Validation error occurs These values are not equal");
        element(formName + "urlMessage").assertText("Validation error occurs Entered value is not url");
        element(formName + "emailMessage").assertText("Validation error occurs Entered value is not e-mail");
        element(formName + "regExpMessage").assertText("Validation error occurs Entered value is not correct number");
        element(formName + "customMessage").assertText("Validation error occurs Number should be equal 10");
    }

    private void isPassedOFValidators(String formName) {
        element(formName + "equalMessage").assertText("");
        element(formName + "urlMessage").assertText("");
        element(formName + "emailMessage").assertText("");
        element(formName + "regExpMessage").assertText("");
        element(formName + "customMessage").assertText("");
    }


    private void typeData(String formName) {
        calendar(formName + ":c").today().mouseUp();
        dateChooser(formName + ":dch").field().type("Jan 17, 2007");
        twoListSelection(formName + ":tls").addAllButton().click();
        DropDownFieldInspector dropDownField = dropDownField(formName + ":ddf");
        dropDownField.field().type("test");
        dropDownField.field().keyPress(KeyEvent.VK_4);
    }

//  private void clearData(String formName) {
//    Selenium selenium = getSelenium();
//    selenium.mouseUp(formName + ":c::cell_2_6");
//    selenium.mouseUp(formName + ":c" + "::none");
//    selenium.type(formName + ":dch::field", "");
//    selenium.click(formName + ":tls::remove_all");
//    selenium.type(formName + ":ddf::field", "");
//  }

    private void isValidationPassed(String formName) {
        element(formName + ":cMessage").assertContainsText("");
        element(formName + ":dchMessage").assertContainsText("");
        element(formName + ":tlsMessage").assertContainsText("");
        element(formName + ":ddfMessage").assertContainsText("");
    }

    private void isValidationFailed(String formName) {
        element(formName + ":cMessage").assertContainsText("Value is required.");
        element(formName + ":dchMessage").assertContainsText("Value is required.");
        element(formName + ":tlsMessage").assertContainsText("Value is required.");
        element(formName + ":ddfMessage").assertContainsText("Value is required.");
    }

    private void isFailedClientSideAPI() {
        element("testForm:RequiredMessage").assertContainsText("Validation Error");
        element("testForm:CMessage").assertContainsText("Value is required.");
        element("testForm:DchMessage").assertContainsText("Value is required.");
        element("testForm:TlsMessage").assertContainsText("Value is required.");
        element("testForm:DdfMessage").assertContainsText("Value is required.");
        assertTrue(element("testForm:MessageValidDR").text().contains("Validation Error: Specified attribute is not between the expected values of 0.001 and 0.999.") ||
                element("testForm:MessageValidDR").text().equals("Validation Error"));
        element("testForm:EqualMessage").assertContainsText("Validation error occurs These values are not equal");
        element("testForm:UrlMessage").assertContainsText("Validation error occurs Entered value is not url");
        element("testForm:EmailMessage").assertContainsText("Validation error occurs Entered value is not e-mail");
        element("testForm:RegExpMessage").assertContainsText("Validation error occurs Entered value is not correct number");
        element("testForm:CustomMessage").assertContainsText("Validation error occurs Number should be equal 10");
    }

    private void isPassedClientSideAPI() {
        element("testForm:RequiredMessage").assertText("");
        element("testForm:CMessage").assertText("");
        element("testForm:DchMessage").assertText("");
        element("testForm:TlsMessage").assertText("");
        element("testForm:DdfMessage").assertText("");
        element("testForm:MessageValidDR").assertText("");
        element("testForm:EqualMessage").assertText("");
        element("testForm:UrlMessage").assertText("");
        element("testForm:EmailMessage").assertText("");
        element("testForm:RegExpMessage").assertText("");
        element("testForm:CustomMessage").assertText("");

    }

    private void fillInvalidDataForClientSideAPI() {
        getDriver().findElement(By.id("testForm:required")).clear();

        CalendarInspector calendar = calendar("testForm:c");
        calendar.selectCalendarCell(1, 6);
        calendar.selectCalendarCell(2, 6);
        calendar.none().mouseUp();
        dateChooser("testForm:dch").field().clear();
        twoListSelection("testForm:tls").removeAllButton().click();

        DropDownFieldInspector dropDownField = dropDownField("testForm:ddf");
        dropDownField.field().clear();
        dropDownField.field().type("t");
        dropDownField.field().keyPress(Keys.BACK_SPACE);
        element("testForm:fillInvalidData").click();
    }

    private void fillValidDataForClientSideAPI() {
        inputText("testForm:required").type("Text");
        calendar("testForm:c").today().mouseUp();
        dateChooser("testForm:dch").field().type("Mar 19, 2007");
        twoListSelection("testForm:tls").addAllButton().click();
        element("testForm:fillValidData").click();
        DropDownFieldInspector dropDownField = dropDownField("testForm:ddf");
        dropDownField.field().type("Value");
        dropDownField.field().keyPress(Keys.NUMPAD4);
    }

    private void isDefaultPresentation() {
        element("dfm0").assertVisible(true);
        element("testForm:required").assertStyle("background-color: #F8D3D4");
        element("dfm1").assertVisible(true);
        element("testForm:c").assertStyle("background-color: #F8D3D4");
        element("dfm2").assertVisible(true);
        element("testForm:dch").assertStyle("background-color: #F8D3D4");
        element("dfm3").assertVisible(true);
        element("testForm:tls").assertStyle("background-color: #F8D3D4");
        element("dfm4").assertVisible(true);
        element("testForm:ddf").assertStyle("background-color: #F8D3D4");
        element("dfm5").assertVisible(true);
        element("testForm:validDR").assertStyle("background-color: #F8D3D4");
        element("dfm6").assertVisible(true);
        element("testForm:equal2").assertStyle("background-color: #F8D3D4");
        element("dfm7").assertVisible(true);
        element("testForm:url").assertStyle("background-color: #F8D3D4");
        element("dfm8").assertVisible(true);
        element("testForm:email").assertStyle("background-color: #F8D3D4");
        element("dfm9").assertVisible(true);
        element("testForm:regExp").assertStyle("background-color: #F8D3D4");
        element("dfm10").assertVisible(true);
        element("testForm:custom").assertStyle("background-color: #F8D3D4");
    }

    private void isNotDefaultPresentation() {
        element("dfm0").assertElementExists(false);
        element("dfm1").assertElementExists(false);
        element("dfm2").assertElementExists(false);
        element("dfm3").assertElementExists(false);
        element("dfm4").assertElementExists(false);
        element("dfm5").assertElementExists(false);
        element("dfm6").assertElementExists(false);
        element("dfm7").assertElementExists(false);
        element("dfm8").assertElementExists(false);
        element("dfm9").assertElementExists(false);
        element("dfm10").assertElementExists(false);
    }

}
