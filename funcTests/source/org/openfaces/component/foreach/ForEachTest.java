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

package org.openfaces.component.foreach;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.openfaces.ForEachInspector;
import org.seleniuminspector.openfaces.InputTextInspector;
import org.seleniuminspector.openfaces.OpenFacesAjaxLoadingMode;
import org.seleniuminspector.openfaces.SuggestionFieldInspector;

/**
 * @author Alexey Tarasyuk
 */
public class ForEachTest extends OpenFacesTestCase {

    @Test
    public void testForEachWithoutItems() {
        testAppFunctionalPage("/components/foreach/forEach.jsf");

        for (int i = 1; i <= 11; i++) {
            ElementInspector hot1Output = forEach("formID:fe1").item(i, "hot1");
            if (i <= 10) {
                hot1Output.assertElementExists(true);
                hot1Output.assertText(i + ", ");
            } else {
                hot1Output.assertElementExists(false);
            }
        }

        for (int i = 0; i <= 11; i++) {
            ElementInspector hot1Output = forEach("formID:fe2").item(i, "hot1");
            if (i % 2 != 0 && i != 11) {
                hot1Output.assertElementExists(true);
                hot1Output.assertText(i + ", ");
            } else {
                hot1Output.assertElementExists(false);
            }
        }

        for (int i = 10; i >= -11; i--) {
            ElementInspector hot1Output = forEach("formID:fe3").item(i, "hot1");
            if (i >= -10) {
                hot1Output.assertElementExists(true);
                hot1Output.assertText(i + ", ");
            } else {
                hot1Output.assertElementExists(false);
            }
        }
    }

    @Test
    public void testNestedForEach() {
        testAppFunctionalPage("/components/foreach/forEach.jsf");
        //for 1 to 10 step 1
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                ElementInspector hot1Output = forEach("formID:fe4")
                        .item(i, "fe5", ForEachInspector.class)
                        .item(j, "hot1");
                hot1Output.assertElementExists(true);
                hot1Output.assertText(i + "x" + j + ", ");
            }
        }
    }

    @Test
    public void testEmptySet() {
        testAppFunctionalPage("/components/foreach/forEach.jsf");
        ForEachInspector forEach = forEach("formID:fe6");
        forEach.item(0, "hot1").assertElementExists(false);
        forEach.item(0, "hot2").assertElementExists(false);
        forEach.item(0, "qit1").assertElementExists(false);
        forEach.item(0, "hcl1").assertElementExists(false);
        forEach.item(0, "fp1").assertElementExists(false);
    }

    @Test
    public void testList() {
        testAppFunctionalPage("/components/foreach/forEach.jsf");

        ForEachInspector forEach = forEach("formID:fe7");

        ElementInspector outputElement = forEach.item(0, "hot1");
        outputElement.assertElementExists();
        outputElement.assertText("a. ");

        ElementInspector inputElement = forEach.item(0, "qit1");
        inputElement.assertElementExists();
        inputElement.assertAttribute("value", "item A");

        outputElement = forEach.item(1, "hot1");
        outputElement.assertElementExists();
        outputElement.assertText("b. ");

        inputElement = forEach.item(1, "qit1");
        inputElement.assertElementExists();
        inputElement.assertAttribute("value", "item B");

        outputElement = forEach.item(2, "hot1");
        outputElement.assertElementExists();
        outputElement.assertText("c. ");

        inputElement = forEach.item(2, "qit1");
        inputElement.assertElementExists();
        inputElement.assertAttribute("value", "item C");

        outputElement = forEach.item(3, "hot1");
        outputElement.assertElementExists(false);

        inputElement = forEach.item(3, "qit1");
        inputElement.assertElementExists(false);
    }

    @Test
    public void testScalarData() {
        testAppFunctionalPage("/components/foreach/forEach.jsf");

        ForEachInspector forEach = forEach("formID:fe8");

        ElementInspector inputElement = forEach.item(0, "qit1");
        inputElement.assertElementExists();
        inputElement.assertAttribute("value", "field 1");

        inputElement = forEach.item(0, "qit2");
        inputElement.assertElementExists();
        inputElement.assertAttribute("value", "field 2");

        inputElement = forEach.item(1, "qit1");
        inputElement.assertElementExists(false);

        inputElement = forEach.item(1, "qit2");
        inputElement.assertElementExists(false);
    }

    @Test
    public void testDecode() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/foreach/forEach.jsf");
        selenium.submit("formID");
        waitForPageToLoad();

        ForEachInspector forEach = forEach("formID:fe7");

        ElementInspector outputElement = forEach.item(0, "hot1");
        outputElement.assertElementExists();
        outputElement.assertText("a. ");

        ElementInspector inputElement = forEach.item(0, "qit1");
        inputElement.assertElementExists();
        inputElement.assertAttribute("value", "item A");

        outputElement = forEach.item(1, "hot1");
        outputElement.assertElementExists();
        outputElement.assertText("b. ");

        inputElement = forEach.item(1, "qit1");
        inputElement.assertElementExists();
        inputElement.assertAttribute("value", "item B");

        outputElement = forEach.item(2, "hot1");
        outputElement.assertElementExists();
        outputElement.assertText("c. ");

        inputElement = forEach.item(2, "qit1");
        inputElement.assertElementExists();
        inputElement.assertAttribute("value", "item C");

        outputElement = forEach.item(3, "hot1");
        outputElement.assertElementExists(false);

        inputElement = forEach.item(3, "qit1");
        inputElement.assertElementExists(false);
    }

    @Test
    public void testUpdateModelValuesPhase() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/foreach/forEach.jsf");

        ForEachInspector forEach = forEach("formID:fe7");

        ElementInspector key0 = forEach.item(0, "hot1");
        ElementInspector key1 = forEach.item(1, "hot1");
        ElementInspector key2 = forEach.item(2, "hot1");
        InputTextInspector value0 = forEach.item(0, "qit1", InputTextInspector.class);
        InputTextInspector value1 = forEach.item(1, "qit1", InputTextInspector.class);
        InputTextInspector value2 = forEach.item(2, "qit1", InputTextInspector.class);

        key0.assertText("a. ");
        key1.assertText("b. ");
        key2.assertText("c. ");
        value0.assertValue("item A");
        value1.assertValue("item B");
        value2.assertValue("item C");

        value0.type("item F");
        value1.type("item E");
        value2.type("item D");
        selenium.submit("formID");
        waitForPageToLoad();

        key0.assertText("a. ");
        key1.assertText("b. ");
        key2.assertText("c. ");
        value0.assertValue("item F");
        value1.assertValue("item E");
        value2.assertValue("item D");
    }

    @Test
    public void testEvents() {
        testAppFunctionalPage("/components/foreach/forEach.jsf");

        ForEachInspector forEach = forEach("formID:fe7");

        ElementInspector key0 = forEach.item(0, "hot1");
        ElementInspector key1 = forEach.item(1, "hot1");
        ElementInspector key2 = forEach.item(2, "hot1");
        ElementInspector value0 = forEach.item(0, "qit1");
        ElementInspector value1 = forEach.item(1, "qit1");
        ElementInspector value2 = forEach.item(2, "qit1");
        ElementInspector killer0 = forEach.item(0, "hcl1");
        ElementInspector killer1 = forEach.item(1, "hcl1");

        key0.assertText("a. ");
        key1.assertText("b. ");
        key2.assertText("c. ");
        value0.assertAttribute("value", "item A");
        value1.assertAttribute("value", "item B");
        value2.assertAttribute("value", "item C");

        killer1.clickAndWait();

        key0.assertText("a. ");
        key1.assertText("c. ");
        key2.assertElementExists(false);
        value0.assertAttribute("value", "item A");
        value1.assertAttribute("value", "item C");
        value2.assertElementExists(false);

        killer0.clickAndWait();

        key0.assertText("c. ");
        key1.assertElementExists(false);
        key2.assertElementExists(false);
        value0.assertAttribute("value", "item C");
        value1.assertElementExists(false);
        value2.assertElementExists(false);

        killer0.clickAndWait();

        key0.assertElementExists(false);
        key1.assertElementExists(false);
        key2.assertElementExists(false);
        value0.assertElementExists(false);
        value1.assertElementExists(false);
        value2.assertElementExists(false);
    }

    @Test
    public void testConversion() {
        testAppFunctionalPage("/components/foreach/forEach.jsf");

        ForEachInspector forEach = forEach("formID:fe9");

        ElementInspector message0 = forEach.item(0, "hm1");
        ElementInspector message1 = forEach.item(1, "hm1");
        ElementInspector message2 = forEach.item(2, "hm1");
        ElementInspector message3 = forEach.item(3, "hm1");

        message0.assertVisible(false);
        message1.assertVisible(false);
        message2.assertVisible(false);
        message3.assertVisible(false);

        forEach.item(0, "qit1", InputTextInspector.class).type("10");
        forEach.item(1, "qit1", InputTextInspector.class).type("10.1");
        forEach.item(2, "qit1", InputTextInspector.class).type("ten");
        getSelenium().submit("formID");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();

        message0.assertVisible(false);
        message1.assertVisible(true);
        message2.assertVisible(true);
        message3.assertVisible(false);
    }

    @Test
    public void testValidation() {
        testAppFunctionalPage("/components/foreach/forEach.jsf");

        ForEachInspector forEach = forEach("formID:fe9");

        ElementInspector message0 = forEach.item(0, "hm2");
        ElementInspector message1 = forEach.item(1, "hm2");
        ElementInspector message2 = forEach.item(2, "hm2");
        ElementInspector message3 = forEach.item(3, "hm2");

        message0.assertVisible(false);
        message1.assertVisible(false);
        message2.assertVisible(false);
        message3.assertVisible(false);

        forEach.item(0, "qit2", InputTextInspector.class).type("1");
        forEach.item(1, "qit2", InputTextInspector.class).type("12345");
        forEach.item(2, "qit2", InputTextInspector.class).type("123456");
        getSelenium().submit("formID");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();

        message0.assertVisible(true);
        message1.assertVisible(false);
        message2.assertVisible(true);
        message3.assertVisible(false);
    }

    @Test
    public void testAjax() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/foreach/forEach.jsf");

        ForEachInspector forEach = forEach("formID:fe10");
        SuggestionFieldInspector suggestionField = suggestionField(forEach.item(0, "sf1").asSeleniumLocator());

        ElementInspector dropDownItem0 = suggestionField.popup().items().get(0);
        ElementInspector dropDownItem1 = suggestionField.popup().items().get(1);
        ElementInspector dropDownItem2 = suggestionField.popup().items().get(2);

        dropDownItem0.assertElementExists(false);
        dropDownItem1.assertElementExists(false);
        dropDownItem2.assertElementExists(false);
        suggestionField.keyPress('a');
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        dropDownItem0.assertVisible(true);
        dropDownItem1.assertVisible(true);
        dropDownItem2.assertVisible(true);
        dropDownItem0.click();
        sleep(200);
        selenium.submit("formID");
        waitForPageToLoad();
        dropDownItem0.assertElementExists(false);
        dropDownItem1.assertElementExists(false);
        dropDownItem2.assertElementExists(false);

        suggestionField = suggestionField(forEach.item(1, "sf1").asSeleniumLocator());
        dropDownItem0 = suggestionField.popup().items().get(0);
        dropDownItem1 = suggestionField.popup().items().get(1);
        dropDownItem2 = suggestionField.popup().items().get(2);

        dropDownItem0.assertElementExists(false);
        dropDownItem1.assertElementExists(false);
        dropDownItem2.assertElementExists(false);
        sleep(3000);
        suggestionField.keyPress('a');
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        dropDownItem0.assertVisible(true);
        dropDownItem1.assertVisible(true);
        dropDownItem2.assertVisible(true);
        dropDownItem2.click();
        sleep(200);
        selenium.submit("formID");
        waitForPageToLoad();
        dropDownItem0.assertElementExists(false);
        dropDownItem1.assertElementExists(false);
        dropDownItem2.assertElementExists(false);

        suggestionField = suggestionField(forEach.item(2, "sf1").asSeleniumLocator());
        dropDownItem0 = suggestionField.popup().items().get(0);
        dropDownItem1 = suggestionField.popup().items().get(1);
        dropDownItem2 = suggestionField.popup().items().get(2);

        dropDownItem0.assertElementExists(false);
        dropDownItem1.assertElementExists(false);
        dropDownItem2.assertElementExists(false);
        suggestionField.keyPress('a');
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        dropDownItem0.assertVisible(true);
        dropDownItem1.assertVisible(true);
        dropDownItem2.assertVisible(true);
        dropDownItem1.click();
        sleep(200);
        selenium.submit("formID");
        waitForPageToLoad();
        dropDownItem0.assertElementExists(false);
        dropDownItem1.assertElementExists(false);
        dropDownItem2.assertElementExists(false);
    }

    @Test
    public void testWrapperTag() {
        testAppFunctionalPage("/components/foreach/forEach.jsf");
        ElementInspector wrapper_blank = element("formID:fe11");
        wrapper_blank.assertElementExists(false);

        ElementInspector wrapper_div = element("formID:fe12");
        wrapper_div.assertElementExists();
        wrapper_div.assertNodeName("div");
        wrapper_div.assertText("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, ");
        wrapper_div.assertStyle("border: 2px solid gray;");

        ElementInspector wrapper_table = element("formID:fe13");
        wrapper_table.assertElementExists();
        wrapper_table.assertNodeName("table");
        for (int i = 0; i < 10; i++) {
            ForEachInspector wrapper_tr = forEach("formID:fe13").item(i, "fe14", ForEachInspector.class);
            wrapper_tr.assertElementExists();
            wrapper_tr.assertNodeName("tr");
            for (int j = 0; j < 10; j++) {
                ForEachInspector wrapper_td = wrapper_tr.item(j, "fe15", ForEachInspector.class);
                wrapper_td.assertElementExists();
                wrapper_td.assertNodeName("td");
                ElementInspector text = wrapper_td.item(0, "hot1");
                text.assertElementExists();
                text.assertText(" " + i + "x" + j + " ");
            }
        }
    }
}