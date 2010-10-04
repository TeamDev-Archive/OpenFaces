/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.twolistselection;

import org.junit.Ignore;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.openfaces.test.RichFacesAjaxLoadingMode;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.html.InputInspector;
import org.seleniuminspector.openfaces.TwoListSelectionInspector;

import java.util.List;

/**
 * @author Darya Shumilina
 */
public class TwoListSelectionTest extends OpenFacesTestCase {
    @Test
    @Ignore // revive this test when RichFaces 4 is fully functional
    public void testReRenderThroughA4J() {
        testAppFunctionalPage("/components/twolistselection/twoListSelection_a4j.jsf");
        String[] oldTLSValues = new String[7];
        for (int i = 0; i < oldTLSValues.length; i++) {
            oldTLSValues[i] = window().document().getElementsByTagName("option").get(i).text();
        }

        TwoListSelectionInspector twoListSelection = twoListSelection("formID:TLS_ID");
        twoListSelection.addAllButton().click();
        element("formID:refresher").click();
        RichFacesAjaxLoadingMode.getInstance().waitForLoad();

        twoListSelection.addAllButton().click();
        for (int i = 0; i < oldTLSValues.length; i++) {
            String newValue = window().document().getElementsByTagName("option").get(i).text();
            assertFalse(newValue.equals(oldTLSValues[i]));
        }
    }

    @Test
    public void testItemsVisibilityCorrectness() {
        itemsVisibilityCorrectness(twoListSelection("formID:first"));
        itemsVisibilityCorrectness(twoListSelection("formID:second"));
    }

    @Test
    public void testAddingRemoving() {
        addingRemoving(twoListSelection("formID:first"));
        addingRemoving(twoListSelection("formID:second"));
    }

    @Test
    public void testReodering() {
//    restartBrowser();

        reordering(twoListSelection("formID:first"));
        reordering(twoListSelection("formID:second"));
    }

    @Test
    @Ignore
    // todo: bring this test back when it's clear how to disable skinning in RichFaces 4 (M2)
    public void testStyling() {
        checkStyles(false);
        checkStyles(true);
    }

    @Test
    public void testClientSideAPI() {
        testAppFunctionalPage("/components/twolistselection/TLSJSFunctions.jsf");

        ElementInspector getValueButton = element("getValue");

        getValueButton.click();
        ElementInspector getOutputDiv = element("getOutput");
        getOutputDiv.assertText("itemV_3,itemV_5");
        TwoListSelectionInspector tls = twoListSelection("formID:withJSFunctions");
        checkLabelsCorrectness(tls, new String[]{"itemL_1", "itemL_2", "itemL_4", "itemL_6"},
                new String[]{"itemL_3", "itemL_5"}, null, null);
        ElementInspector serverValueOutput = element("formID:serverValue");
        serverValueOutput.assertText("itemV_3 itemV_5 ");

        element("setValue").click();
        getValueButton.click();
        getOutputDiv.assertText("itemV_1,itemV_2");
        checkLabelsCorrectness(tls, new String[]{"itemL_4", "itemL_6", "itemL_3", "itemL_5"},
                new String[]{"itemL_1", "itemL_2"}, null, null);

        element("formID:submit").clickAndWait();
        serverValueOutput.assertText("itemV_1 itemV_2 ");
    }

    @Test
    public void testValueChangeListener() {
//    restartBrowser();
        testAppFunctionalPage("/components/twolistselection/TLSValueChangeListener.jsf");

        ElementInspector asTagOutput = element("formID:asTagOutput");
        boolean initialValueAsAttribute = Boolean.parseBoolean(asTagOutput.text());
        ElementInspector asAttributeOutput = element("formID:asAttributeOutput");
        boolean initialValueAsTag = Boolean.parseBoolean(asAttributeOutput.text());

        element("asTagChanger").click();
        element("asAttributeChanger").click();

        element("formID:submit").clickAndWait();

        boolean resultantValueAsAttribute = Boolean.parseBoolean(asAttributeOutput.text());
        boolean resultantValueAsTag = Boolean.parseBoolean(asTagOutput.text());
        assertEquals(initialValueAsAttribute, !resultantValueAsAttribute);
        assertEquals(initialValueAsTag, !resultantValueAsTag);
    }

    private void checkLabelsCorrectness(TwoListSelectionInspector tls, String[] referenceLeftListValues, String[] referenceRightListValues,
                                        String referenceLeftHeader, String referenceRightHeader) {

        if (referenceLeftListValues != null) {
            String[] actualLeftListValues = tls.leftList().selectOptions();
            assertEquals(referenceLeftListValues.length, actualLeftListValues.length);
            for (int i = 0; i < actualLeftListValues.length; i++) {
                assertEquals(referenceLeftListValues[i], actualLeftListValues[i]);
            }
        }

        if (referenceRightListValues != null) {
            String[] actualRightListValues = tls.rightList().selectOptions();
            assertEquals(referenceRightListValues.length, actualRightListValues.length);
            for (int i = 0; i < actualRightListValues.length; i++) {
                assertEquals(referenceRightListValues[i], actualRightListValues[i]);
            }
        }

        if (referenceLeftHeader != null) {
            tls.leftListHeader().assertText(referenceLeftHeader);
        }

        if (referenceRightHeader != null) {
            tls.rightListHeader().assertText(referenceRightHeader);
        }

    }

    private void itemsVisibilityCorrectness(TwoListSelectionInspector tls) {
        testAppFunctionalPage("/components/twolistselection/TLSBasicFeatures.jsf");

        tls.leftList().assertVisible(true);
        tls.rightList().assertVisible(true);

        checkLabelsCorrectness(tls, new String[]{"item_Label_1", "item_Label_2", "item_Label_4", "item_Label_6", "item_Label_7"},
                new String[]{"item_Label_3", "item_Label_5"}, "Available items", "Selected items");
    }

    private void addingRemoving(TwoListSelectionInspector tls) {
        testAppFunctionalPage("/components/twolistselection/TLSBasicFeatures.jsf");

        //check TLS state by page load
        checkLabelsCorrectness(tls, new String[]{"item_Label_1", "item_Label_2", "item_Label_4", "item_Label_6", "item_Label_7"},
                new String[]{"item_Label_3", "item_Label_5"}, "Available items", "Selected items");
        ElementInspector tlsOutput = element(tls.id() + "Output");
        tlsOutput.assertText("item_Value_3 item_Value_5 ");

        //add one item
        tls.leftList().selectedItem(1);
        tls.addButton().click();
        checkLabelsCorrectness(tls, new String[]{"item_Label_1", "item_Label_4", "item_Label_6", "item_Label_7"},
                new String[]{"item_Label_3", "item_Label_5", "item_Label_2"}, "Available items", "Selected items");

        ElementInspector submit = element("formID:submit");
        submit.clickAndWait();
        tlsOutput.assertText("item_Value_3 item_Value_5 item_Value_2 ");

        //add one item by doubleclick
        tls.leftList().doubleClick();
        checkLabelsCorrectness(tls, new String[]{"item_Label_1", "item_Label_4", "item_Label_6"},
                new String[]{"item_Label_3", "item_Label_5", "item_Label_2", "item_Label_7"}, "Available items", "Selected items");
        submit.clickAndWait();
        tlsOutput.assertText("item_Value_3 item_Value_5 item_Value_2 item_Value_7 ");

        //remove one item
        tls.rightList().selectedItem(1);
        tls.removeButton().click();
        checkLabelsCorrectness(tls, new String[]{"item_Label_1", "item_Label_4", "item_Label_6", "item_Label_5"},
                new String[]{"item_Label_3", "item_Label_2", "item_Label_7"}, "Available items", "Selected items");
        submit.clickAndWait();
        tlsOutput.assertText("item_Value_3 item_Value_2 item_Value_7 ");

        //remove one item by doubleclick
        tls.rightList().doubleClick();
        checkLabelsCorrectness(tls, new String[]{"item_Label_1", "item_Label_4", "item_Label_5", "item_Label_6", "item_Label_2"},
                new String[]{"item_Label_3", "item_Label_7"}, "Available items", "Selected items");
        submit.clickAndWait();
        tlsOutput.assertText("item_Value_3 item_Value_7 ");

        //add all
        tls.addAllButton().click();
        checkLabelsCorrectness(tls, new String[]{"item_Label_4", "item_Label_6"},
                new String[]{"item_Label_3", "item_Label_7", "item_Label_1", "item_Label_2", "item_Label_5"},
                "Available items", "Selected items");
        submit.clickAndWait();
        tlsOutput.assertText("item_Value_3 item_Value_7 item_Value_1 item_Value_2 item_Value_5 ");

        //remove all
        tls.removeAllButton().click();
        checkLabelsCorrectness(tls,
                new String[]{"item_Label_4", "item_Label_6", "item_Label_3", "item_Label_7", "item_Label_1", "item_Label_2", "item_Label_5"},
                new String[]{""},
                "Available items", "Selected items");
        submit.clickAndWait();
        tlsOutput.assertText("none");
    }

    private void reordering(TwoListSelectionInspector tls) {
        testAppFunctionalPage("/components/twolistselection/TLSBasicFeatures.jsf");

        tls.addAllButton().click();
        checkLabelsCorrectness(tls, new String[]{"item_Label_4", "item_Label_6"},
                new String[]{"item_Label_3", "item_Label_5", "item_Label_1", "item_Label_2", "item_Label_7"}, "Available items", "Selected items");

        //check 'down' button
        tls.rightList().selectedItem(0);
        tls.moveDownButton().click();
        tls.moveDownButton().click();

        checkLabelsCorrectness(tls, new String[]{"item_Label_4", "item_Label_6"},
                new String[]{"item_Label_5", "item_Label_1", "item_Label_3", "item_Label_2", "item_Label_7"}, "Available items", "Selected items");

        //check 'up' button
        tls.rightList().unselectedItem(2);
        tls.rightList().selectedItem(3);
        tls.moveUpButton().click();
        tls.moveUpButton().click();

        checkLabelsCorrectness(tls, new String[]{"item_Label_4", "item_Label_6"},
                new String[]{"item_Label_5", "item_Label_2", "item_Label_1", "item_Label_3", "item_Label_7"}, "Available items", "Selected items");

        //check sorting
        tls.rightListHeader().click();
        checkLabelsCorrectness(tls, new String[]{"item_Label_4", "item_Label_6"},
                new String[]{"item_Label_1", "item_Label_2", "item_Label_3", "item_Label_5", "item_Label_7"}, "Available items", "Selected items");
        tls.rightListHeader().click();
        checkLabelsCorrectness(tls, new String[]{"item_Label_4", "item_Label_6"},
                new String[]{"item_Label_7", "item_Label_5", "item_Label_3", "item_Label_2", "item_Label_1"}, "Available items", "Selected items");
    }

    private void checkStyles(boolean makeSubmit) {
        testAppFunctionalPage("/components/twolistselection/TLSStyled.jsf");

        if (makeSubmit) {
            element("formID:submit").clickAndWait();
        }

        String tlsId = "formID:styled";
        TwoListSelectionInspector tls = twoListSelection(tlsId);
        ElementInspector leftList = tls.leftList();
        ElementInspector rightList = tls.rightList();
        ElementInspector leftHeader = tls.leftListHeader();
        ElementInspector rightHeader = tls.rightListHeader();
        InputInspector addBtn = tls.addButton();
        InputInspector addAllBtn = tls.addAllButton();
        InputInspector removeBtn = tls.removeButton();
        InputInspector removeAllBtn = tls.removeAllButton();
        InputInspector moveUpBtn = tls.moveUpButton();
        InputInspector moveDownBtn = tls.moveDownButton();

        String allButtonsStyle = "color: red; background-color: yellow; border: 2px solid green;";
        addAllBtn.assertStyle(allButtonsStyle);
        addBtn.assertStyle(allButtonsStyle);
        removeBtn.assertStyle(allButtonsStyle);
        removeAllBtn.assertStyle(allButtonsStyle);
        moveUpBtn.assertStyle(allButtonsStyle);
        moveDownBtn.assertStyle(allButtonsStyle);

        //headerStyle="color: pink; border: 2px dashed blue; background: beige;"
        leftHeader.assertText("Available items");
        rightHeader.assertText("Selected items");
        leftHeader.assertStyle("background-color: beige; color: pink; border: 2px dashed blue;");
        rightHeader.assertStyle("background-color: beige; color: pink; border: 2px dashed blue;");

        leftList.assertStyle("background-color: azure;");
        rightList.assertStyle("background-color: azure;");
        List<ElementInspector> options = leftList.childNodesByName("option");
        for (int i = 0; i < 6; i++) {
            ElementInspector option = options.get(i);
            option.assertText("itemL_" + (i + 1));
            option.assertStyle("font-weight: bold");
        }

        //style="width: 500px; border: 3px dotted brown;"
        //todo: see 'JSFC-3222' issue. If it in 'fixed' state - correct 'width' property value.
        if (IS_FACELETS)
            tls.assertWidth(500);
        else
            tls.assertWidth(516);

        addAllBtn.assertExpressionEquals("title", "add all hint");
        addAllBtn.assertValue("add all");

        addBtn.assertExpressionEquals("title", "add hint");
        addBtn.assertValue("add");

        moveDownBtn.assertExpressionEquals("title", "move down hint");
        moveDownBtn.assertValue("move down");

        moveUpBtn.assertExpressionEquals("title", "move up hint");
        moveDownBtn.assertValue("move down");

        removeAllBtn.assertExpressionEquals("title", "remove all hint");
        removeAllBtn.assertValue("remove all");

        removeBtn.assertExpressionEquals("title", "remove hint");
        removeBtn.assertValue("remove");
    }

    @Test
    public void testDefaultView() {
        testAppFunctionalPage("/components/twolistselection/twoListSelection_defaultView.jsf");
        assertAppearanceNotChanged("TwoListSelectionDefaultView");
    }

    private void checkTabindex(ElementInspector element, String expectedTabindex) {
        String tabindex = getSelenium().getAttribute("//*[@id='" + element.id() + "']/@tabindex");
        assertEquals(expectedTabindex, tabindex);
    }

    @Test
    public void testTabindex() {
        testAppFunctionalPage("/components/twolistselection/twoListSelection_tabindex.jsf");

        final String tslTabindex = "123";

        TwoListSelectionInspector tls = twoListSelection("form1:tls1");

        checkTabindex(tls.leftList(), tslTabindex);
        checkTabindex(tls.rightList(), tslTabindex);

        checkTabindex(tls.addAllButton(), tslTabindex);
        checkTabindex(tls.removeButton(), tslTabindex);
        checkTabindex(tls.addButton(), tslTabindex);
        checkTabindex(tls.removeAllButton(), tslTabindex);

        checkTabindex(tls.moveUpButton(), tslTabindex);
        checkTabindex(tls.moveDownButton(), tslTabindex);

    }
}