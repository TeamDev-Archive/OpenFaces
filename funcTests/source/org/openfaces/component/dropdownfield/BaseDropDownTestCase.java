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
package org.openfaces.component.dropdownfield;

import com.thoughtworks.selenium.Selenium;
import org.openfaces.component.input.DropDownField;
import org.openfaces.test.OpenFacesTestCase;
import org.openfaces.test.RichFacesAjaxLoadingMode;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.html.InputInspector;
import org.seleniuminspector.html.TableInspector;
import org.seleniuminspector.openfaces.DropDownFieldInspector;
import org.seleniuminspector.openfaces.DropDownPopupInspector;
import org.seleniuminspector.openfaces.OpenFacesAjaxLoadingMode;

import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Darya Shumilina
 */
public abstract class BaseDropDownTestCase extends OpenFacesTestCase {

    protected void checkReRenderingThroughA4J(String pageUrl) {
        testAppFunctionalPage(pageUrl);
        DropDownFieldInspector dropDown = dropDownField("formID:dropDownID");
        List<String> oldValues = getDropDownItemTexts(dropDown);

        element("formID:refresher").click();
        RichFacesAjaxLoadingMode.getInstance().waitForLoad();
        List<String> newValues = getDropDownItemTexts(dropDown);
        assertFalse(newValues.equals(oldValues));
    }

    private List<String> getDropDownItemTexts(DropDownFieldInspector dropDown) {
        List<ElementInspector> items = dropDown.popup().items();
        List<String> result = new ArrayList<String>();
        for (int i = 0, count = items.size(); i < count; i++) {
            String itemText = items.get(i).text();
            result.add(itemText);
        }
        return result;
    }

    protected void checkManualListOpeningAndClosing(String pageUrl, boolean isDropDown) {
        testAppFunctionalPage(pageUrl);

        // with mouse help
        DropDownFieldInspector first = dropDownField("formID:first");
        if (isDropDown) {
            first.popup().assertVisible(false);
            first.button().mouseDown();
            first.popup().assertVisible(true);
            first.button().mouseDown();
        }

        // with keyboard help
        first.popup().assertVisible(false);
        first.field().keyDown(KeyEvent.VK_DOWN); // down key
        first.popup().assertVisible(true);
        first.popup().assertItemTexts(new String[]{"Red", "Yellow", "Blue"});

        // check with 'Esc' key
        first.field().keyPress(KeyEvent.VK_ESCAPE);
        first.popup().assertVisible(false);

        // check with 'Enter' key
        first.field().keyDown(KeyEvent.VK_DOWN);
        first.popup().assertVisible(true);
        first.field().keyDown(KeyEvent.VK_DOWN);
        first.field().keyPress(13);
        first.popup().assertVisible(false);
    }

    protected void checkListCorrectness(String pageUrl, boolean isDropDown) {
        testAppFunctionalPage(pageUrl);

        String[] referenceCollection = {"Red", "Yellow", "Blue"};
        DropDownFieldInspector first = dropDownField("formID:first");
        if (isDropDown)
            first.button().mouseDown();
        else
            first.field().keyDown(KeyEvent.VK_DOWN); // down key

        first.popup().assertItemTexts(referenceCollection);

        // check items defined by binding
        DropDownFieldInspector second = dropDownField("formID:second");
        if (isDropDown)
            second.button().mouseDown();
        else
            second.field().keyDown(KeyEvent.VK_DOWN);
        second.popup().assertItemTexts(referenceCollection);
    }

    protected void checkExpandedListOnPasteValue(String pageUrl){
        testAppFunctionalPage(pageUrl);
        DropDownFieldInspector textAlign = dropDownField("formID:textAlign");

        InputInspector field = dropDownField("formID:textAlign").field();
        WebElement field_ = getDriver().findElement(By.xpath(field.getXPath()));
        field_.sendKeys("Left");
        textAlign.field().assertValue("Left");

        field.keyPress(Keys.CONTROL);
        field.keyPress(86);

        textAlign.popup().assertVisible(true);
    }

    protected void checkValueSelectionFromList(String pageUrl, boolean isDropDown) {
        testAppFunctionalPage(pageUrl);

        DropDownFieldInspector first = dropDownField("formID:first");
        first.field().keyDown(KeyEvent.VK_DOWN);
        first.popup().assertVisible(true);
        first.field().keyDown(KeyEvent.VK_DOWN);
        first.field().keyPress(13);
        first.field().assertValue("Red");

        DropDownFieldInspector second = dropDownField("formID:second");
        if (isDropDown) {
            second.button().mouseDown();
            second.popup().assertVisible(true);
            second.popup().items().get(0).click();
        } else {
            second.field().keyDown(KeyEvent.VK_DOWN);
            second.popup().assertVisible(true);
            second.field().keyDown(KeyEvent.VK_DOWN);
        }
        second.field().keyPress(13);
        sleep(1000);
        second.field().assertValue("Red");

        element("formID:submit").clickAndWait();
        element("formID:selectedValue").assertText("Red");
        element("formID:selectedValue2").assertText("Red");
    }

    protected void checkTypedValue(String pageUrl) {
        testAppFunctionalPage(pageUrl);

        // type own value
        DropDownFieldInspector first = dropDownField("formID:first");
        first.field().type("brown");
        first.field().assertValue("brown");

        DropDownFieldInspector second = dropDownField("formID:second");
        second.field().click();
        second.field().type("brown");
        second.field().assertValue("brown");

        element("formID:submit").clickAndWait();
        element("formID:selectedValue").assertText("brown");
        element("formID:selectedValue2").assertText("brown");
    }

    protected void checkAjaxBasedSuggestion(String pageUrl) {
        testAppFunctionalPage(pageUrl);

        // check auto-opening
        DropDownFieldInspector dropDownField = dropDownField("formID:plants");
        DropDownPopupInspector popup = dropDownField.popup();
        popup.itemsTable().assertVisible(false);
        dropDownField.field().keyPress('i');
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        popup.itemsTable().assertVisible(true);
        popup.items().get(0).assertText("Iberis");

        dropDownField.field().keyPress('r');
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        popup.items().get(0).assertText("Iresine");
        popup.items().get(1).assertText("Iris");
    }

    protected void checkClientBasedSuggestion(String pageUrl) {
        testAppFunctionalPage(pageUrl);

        // suggestionMode="substring"
        List<String> substringExpectedValues = Arrays.asList("AntiqueWhite", "FloralWhite", "GhostWhite", "NavajoWhite",
                "PapayaWhip", "Wheat", "White", "WhiteSmoke");
        int[] substringExpectedValuesIndexes = {1, 43, 47, 94, 106, 135, 136, 137};
        checkAutoOpeningFilteringCorrectness("formID:substringDD", "wh",
                substringExpectedValues, substringExpectedValuesIndexes);

        // suggestionMode="stringStart"
        List<String> stringStartExpectedValues = Arrays.asList("White", "WhiteSmoke");
        int[] stringStartExpectedValuesIndexes = {136, 137};
        checkAutoOpeningFilteringCorrectness("formID:stringStartDD", "white",
                stringStartExpectedValues, stringStartExpectedValuesIndexes);

        // suggestionMode="stringEnd"
        List<String> stringEndExpectedValues = Arrays.asList("AntiqueWhite", "FloralWhite", "GhostWhite", "NavajoWhite", "White");
        int[] stringEndExpectedValuesIndexes = {1, 43, 47, 94, 136};
        checkAutoOpeningFilteringCorrectness("formID:stringEndDD", "WHITE",
                stringEndExpectedValues, stringEndExpectedValuesIndexes);

        // suggestionMode="all"
        List<String> allExpectedValues = Arrays.asList("test1", "sample2", "demo3");
        int[] allIndexes = {0, 1, 2};
        checkAutoOpeningFilteringCorrectness("formID:allDD", "R", allExpectedValues, allIndexes);
    }

    private void checkAutoOpeningFilteringCorrectness(String dropDownId, String textToType,
                                                      List<String> expectedValues, int[] indexes) {
        DropDownFieldInspector dropDownField = dropDownField(dropDownId);
        dropDownField.popup().itemsTable().assertVisible(false);
        dropDownField.field().typeKeys(textToType);
        sleep(1500);
        dropDownField.popup().itemsTable().assertVisible(true);
        for (int i = 0; i < expectedValues.size(); i++) {
            String expectedValue = expectedValues.get(i);
            dropDownField.popup().items().get(indexes[i]).assertText(expectedValue);
            // todo: it seems that this test doesn't actually check which values are visible and will succeed on any list of visible suggestions. fix this
        }
    }

    protected void checkAutoCompletionFeature(String pageUrl) {
        testAppFunctionalPage(pageUrl);

        InputInspector field = dropDownField("formID:substringDD").field();
        WebElement field_ = getDriver().findElement(By.xpath(field.getXPath()));
        field_.sendKeys("w");
        field_.sendKeys("h");
        sleep(700);
        field.assertValue("Wheat");

        field_.sendKeys("i");
        sleep(700);
        field.assertValue("White");
    }


    protected void checkStyles(boolean makeSubmit, boolean isFocused, boolean isDisabled, String pageUrl, boolean isDropDown) {
        Selenium selenium = getSelenium();
        testAppFunctionalPage(pageUrl);

        ElementInspector submitElement = element("formID:submit");

        if (makeSubmit) {
            submitElement.clickAndWait();
        }
        DropDownFieldInspector dropDown = dropDownField("formID:styled");

        ElementInspector button = dropDown.button();
        ElementInspector field = dropDown.field();
        DropDownPopupInspector popup = dropDown.popup();
        List<ElementInspector> items = popup.items();

        if (isFocused) {
            if (isDropDown)
                field.focus();
            else
                dropDown.focus();
        } else {
            if (isDropDown)
                field.fireEvent("onblur");
            else
                dropDown.fireEvent("onblur");
        }

        if (!isDisabled) {
            dropDown.assertExpressionEquals("offsetWidth", 230, 5);

            if (isFocused) {
                dropDown.assertStyle("border: 1px solid yellow;");
            }

            if (!isDropDown) {
                dropDown.assertStyle("background: GreenYellow; font-weight: bold;");
            }

            if (isDropDown) {
                button.assertStyle("border: 1px solid blue; background: PowderBlue;");

                button.childNodes().get(0).assertAttributeStartsWith("src", "dropdown_arrow.gif");

                field.assertStyle("background: GreenYellow; font-weight: bold;");

                button.mouseDown();
                button.assertStyle("border: 1px solid OrangeRed; background: PaleVioletRed;");
                button.mouseUp();

                if (!isFocused) {
                    field.fireEvent("onblur");
                }
            }

            if (!isDropDown) {
                dropDown.keyDown(KeyEvent.VK_DOWN);
                if (!isFocused) {
                    dropDown.fireEvent("onblur");
                }
            }

            ElementInspector item2 = items.get(2);
            if (isDropDown)
                item2.assertStyle("font-size: 18px;");
            else
                item2.childNodesByName("td").get(0).assertStyle("font-size: 18px;");
            item2.assertStyle("color: Teal;");

            popup.assertStyle("border: 1px dotted red");

            TableInspector itemsTable = popup.itemsTable();
            ElementInspector firstFooterCell = itemsTable.subElement("tfoot[0]/tr[0]/td[0]");
            firstFooterCell.assertStyle("color: silver");

            itemsTable.subElement("thead[0]/tr[0]").assertStyle("text-decoration: underline");
            ElementInspector firstHeaderCell = itemsTable.subElement("thead[0]/tr[0]/td[0]");
            firstHeaderCell.assertStyle("color: red");

            items.get(1).assertStyle("background: bisque;");

            items.get(2).subElement("td[0]").assertStyle("border-right: 1px solid blue; border-bottom: 2px dotted DarkOliveGreen");

            firstHeaderCell.assertStyle("border-right: 2px dashed springgreen; border-bottom: 2px solid fuchsia");
            firstFooterCell.assertStyle("border-right: 3px solid black; border-top: 2px solid green");

            selenium.mouseMove("formID:styled");
            if (isDropDown) {
                if (!isFocused) { // focusedStyle has higher prioriry than rolloverStyle
                    field.assertStyle("border: 2px dashed MediumSpringGreen;");
                }
                button.assertStyle("border: 2px solid darkgreen; background: green;");
            }

            if (!isFocused) { // focusedStyle has higher prioriry than rolloverStyle
                dropDown.assertStyle("border: 1px dotted darkblue;");
            }

            popup.assertStyle("border: 3px dotted pink");

            // rolloverListItemStyle="color: orange; font: Comic Sans MS; background: LightGreen;"
            submitElement.mouseMove();
            items.get(2).mouseMove();
            items.get(2).assertStyle("color: orange");
        } else {
            element("formID:makeDisabled").clickAndWait();

            if (isDropDown) {
                button.childNodes().get(0).assertAttributeStartsWith("src", "dropdown_arrow_disabled.gif");

                button.assertStyle("background: pink;");

                field.assertStyle("background: yellow; border: 2px solid green;");
            }

            // disabledStyle="width: 400px;"
            dropDown.assertExpressionEquals("offsetWidth", 400, 4);
            if (!isDropDown) {
                dropDown.assertStyle("background: yellow; border: 2px solid green;");
            }
        }
    }

    /**
     * The following js-functions are checked here: getDropDownValue(), setDropDownValue(value),
     * dropDown(), isOpened(), closeUp()
     *
     * @param pageUrl page url
     */
    protected void checkClintSideAPI(String pageUrl) {
        testAppFunctionalPage(pageUrl);

        element("valueSetter").click();
        DropDownFieldInspector dropDownField = dropDownField("formID:testableDropDown");
        dropDownField.field().assertValue("Red");

        element("valueGetter").click();
        element("out1").assertText("Red");

        element("openDropDown").click();
        dropDownField.popup().assertVisible(true);
        ElementInspector out2Element = element("out2");
        out2Element.assertText("true");

        sleep(5500);
        dropDownField.popup().assertVisible(false);
        out2Element.assertText("false");

        element("formID:submit").clickAndWait();
        element("formID:valueDD").assertText("Value: Red");
    }

    protected void checkValueChangeListener(String pageUrl) {
        testAppFunctionalPage(pageUrl);

        ElementInspector asAttributeOutput = element("formID:asAttributeOutput");
        ElementInspector asTagOutput = element("formID:asTagOutput");
        asAttributeOutput.assertText("0");
        asTagOutput.assertText("false");

        dropDownField("formID:asAttributeDD").field().type("Yellow");
        dropDownField("formID:asTagDD").field().type("Yellow");

        element("formID:submit").clickAndWait();

        asAttributeOutput.assertText("1");
        asTagOutput.assertText("true");
    }

    protected void checkNoCachingHighlight(String pageUrl){
        testAppFunctionalPage(pageUrl);
        DropDownFieldInspector dropdown = dropDownField("formID:plantsNoCaching");
        dropdown.field().keyDown(KeyEvent.VK_DOWN);
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        dropdown.popup().items().get(1).click();
        dropdown.field().keyDown(KeyEvent.VK_DOWN);
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        dropdown.popup().items().get(1).assertStyle("background-color: black;");
    }

    protected void checkAfterRenderHighlight(String pageUrl){
        testAppFunctionalPage(pageUrl);
        DropDownFieldInspector dropdown = dropDownField("formID:plantsAfterRender");
        dropdown.field().keyDown(KeyEvent.VK_DOWN);
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        dropdown.popup().items().get(1).click();
        element("formID:renderPlants").click();
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        dropdown.field().keyDown(KeyEvent.VK_DOWN);
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        dropdown.popup().items().get(1).assertStyle("background-color: black;");
    }

    protected void checkNoCashingAutoComplete(String pageUrl){
        testAppFunctionalPage(pageUrl);
        DropDownFieldInspector dropdown = dropDownField("formID:plantsNoCaching");
        WebElement field_ = getDriver().findElement(By.xpath(dropdown.field().getXPath()));
        field_.sendKeys("a");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        field_.sendKeys("b");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        element("formID:selectedRangeSize").assertText("5");
    }

    protected void checkNoFieldCleanOnCustomInput(String pageUrl){
        testAppFunctionalPage(pageUrl);
        DropDownFieldInspector dropdown = dropDownField("formID:plantsCustomValue");
        WebElement field = getDriver().findElement(By.xpath(dropdown.field().getXPath()));
        field.sendKeys("a");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        field.sendKeys("a");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        field.sendKeys("a");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        dropdown.field().keyDown(KeyEvent.VK_ESCAPE);
        dropdown.field().keyDown(KeyEvent.VK_DOWN);
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        dropdown.field().assertValue("aaa");
    }
}
