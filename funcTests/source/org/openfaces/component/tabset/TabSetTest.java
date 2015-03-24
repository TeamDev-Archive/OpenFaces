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
package org.openfaces.component.tabset;

import org.junit.Test;
import org.openfaces.test.BaseSeleniumTest;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.html.TableCellInspector;
import org.seleniuminspector.html.TableInspector;
import org.openfaces.test.RichFacesAjaxLoadingMode;
import org.seleniuminspector.openfaces.TabSetInspector;

/**
 * @author Darya Shumilina
 */
public class TabSetTest extends BaseSeleniumTest {
     @Test
    public void testReRenderThroughA4J() {
        testAppFunctionalPage("/components/tabset/tabSet_a4j.jsf");

        element("formID:second").click();
        ElementInspector onchangePopup = element("formID:onchangePopup");
        onchangePopup.assertVisible(true);

        ElementInspector closerButton = element("formID:closer");
        closerButton.click();
        element("formID:refresher").click();
        RichFacesAjaxLoadingMode.getInstance().waitForLoad();
        element("formID:first").click();
        onchangePopup.assertVisible(true);
        closerButton.click();
    }

     @Test
    public void testA4JControlInside() {
        testAppFunctionalPage("/components/tabset/tabSet_a4j.jsf");

        element("formID:second_a4j").click();
        ElementInspector onchangePopup = element("formID:onchange_a4j_Popup");
        onchangePopup.assertVisible(true);

        ElementInspector closerButton = element("formID:closer_a4j");
        closerButton.click();
        element("formID:refresher_a4j").click();
        RichFacesAjaxLoadingMode.getInstance().waitForLoad();
        element("formID:first_a4j").click();
        onchangePopup.assertVisible(true);
        closerButton.click();
    }

     @Test
    public void testChangeSelectedTabWithoutSubmit() {
        testAppFunctionalPage("/components/tabset/tabSetChangeSelectionStyling.jsf");
        TabSetInspector tabSet = tabSet("formID:tabSetWithoutSubmit");

        /* verify that first tab is selected */
        // using getSelectedIndex() function
        ElementInspector getSelectedTab1Button = element("getSelectedTab1");
        getSelectedTab1Button.click();
        ElementInspector empty = element("empty");
        empty.assertText("0");

        // styles verification
        verifyStyles(tabSet, 0);

        /* swicth selected tab and make verifications again */
        tabSet.tabs().get(1).click();
        tabSet.tabs().get(1).mouseOut();
        getSelectedTab1Button.click();
        empty.assertText("1");

        //styles verification
        verifyStyles(tabSet, 1);
    }

     @Test
    public void testChangeSelectedTabWithSubmit() {
        closeBrowser();
        testAppFunctionalPage("/components/tabset/tabSetChangeSelectionStyling.jsf");
        TabSetInspector tabSet = tabSet("formID:tabSetWithSubmit");

        /* verify that first tab is selected */
        // using q_getTabSetSelectedIndex function
        ElementInspector getSelectedTab2Button = element("getSelectedTab2");
        getSelectedTab2Button.click();
        ElementInspector empty1 = element("empty1");
        ElementInspector selectedIndex = element("formID:selectedIndex");
        ElementInspector selectedValue = element("formID:selectedValue");

        empty1.assertText("0");
        selectedIndex.assertText("0");
        selectedValue.assertText("Blue");

        // styles verification
        verifyStyles(tabSet, 0);

        /* swicth selected tab and make verifications again */
        tabSet.tabs().get(1).mouseOut();
        tabSet.tabs().get(1).clickAndWait();

        getSelectedTab2Button.click();
        empty1.assertText("1");

        selectedIndex.assertText("1");
        selectedValue.assertText("Yellow");

        //styles verification
        verifyStyles(tabSet, 1);
    }

     @Test
    public void testClientSideAPI() {
        closeBrowser();
        testAppFunctionalPage("/components/tabset/tabSetChangeSelectionStyling.jsf");
        TabSetInspector tabSetWithoutSubmit = tabSet("formID:tabSetWithoutSubmit");
        TabSetInspector tabSetWithSubmit = tabSet("formID:tabSetWithSubmit");

        //check getSelectedIndex() for both TabSets
        ElementInspector getSelectedTab1Button = element("getSelectedTab1");
        getSelectedTab1Button.click();

        ElementInspector emptyDiv = element("empty");
        emptyDiv.assertText("0");
        verifyStyles(tabSetWithoutSubmit, 0);

        ElementInspector getSelectedTab2Button = element("getSelectedTab2");
        getSelectedTab2Button.click();
        ElementInspector empty1Div = element("empty1");
        empty1Div.assertText("0");
        verifyStyles(tabSetWithSubmit, 0);
        ElementInspector selectedIndexOutput = element("formID:selectedIndex");
        selectedIndexOutput.assertText("0");
        ElementInspector selectedValueOutput = element("formID:selectedValue");
        selectedValueOutput.assertText("Blue");

        // check setSelectedIndex() for both TabSets
        element("setSelectedTab1").click();
        getSelectedTab1Button.click();
        emptyDiv.assertText("1");
        verifyStyles(tabSetWithoutSubmit, 1);

        getSelectedTab2Button.click();
        element("setSelectedTab2").clickAndWait();
        getSelectedTab2Button.click();
        empty1Div.assertText("1");
        verifyStyles(tabSetWithSubmit, 1);
        selectedIndexOutput.assertText("1");
        selectedValueOutput.assertText("Yellow");
    }

     @Test
    public void testSelectionChangeListener() {
        testAppFunctionalPage("/components/tabset/tabSetChangeListeners.jsf");

        TabSetInspector attributeTabSet = tabSet("formID:selectionChangeListenerAttributeWithoutSubmit");
        attributeTabSet.tabs().get(1).click();
        TabSetInspector tagTabSet = tabSet("formID:selectionChangeListenerTagWithoutSubmit");
        tagTabSet.tabs().get(1).click();

        ElementInspector submit = element("formID:submit");
        submit.clickAndWait();

        ElementInspector out3Element = element("formID:out3");
        out3Element.assertText("true");
        ElementInspector out4Element = element("formID:out4");
        out4Element.assertText("true");

        attributeTabSet.tabs().get(0).click();
        tagTabSet.tabs().get(0).click();

        submit.clickAndWait();

        out3Element.assertText("false");
        out4Element.assertText("false");
    }

    //todo: fill the method body if 'JSFC-2994' is in 'fixed' state
     @Test
    public void testValueChangeListener() {
//    Selenium selenium = getSelenium();
//    testAppPage("functionalTesting/tabset/tabSetChangeListeners.jsf");
    }

    private void verifyStyles(TabSetInspector tabSet, int selectedTabIndex) {
        ElementInspector firstTab = tabSet.tabs().get(0);
        ElementInspector secondTab = tabSet.tabs().get(1);
        TableInspector tabSetAsTable = new TableInspector(tabSet);
        TableCellInspector emptySpaceCell = tabSetAsTable.body().row(0).cell(4);

        emptySpaceCell.assertStyle("background: beige");

        // backBorderStyle="2px solid SpringGreen"
        if (selectedTabIndex == 0) {
            secondTab.assertStyle("border-left: 2px solid SpringGreen; border-right: 2px solid SpringGreen;");
        } else if (selectedTabIndex == 1) {
            firstTab.assertStyle("border-left: 2px solid SpringGreen; border-right: 2px solid SpringGreen");
        }

        // frontBorderStyle="3px dashed OliveDrab"
        if (selectedTabIndex == 0) {
            secondTab.assertStyle("border-bottom: 3px dashed OliveDrab");
            firstTab.assertStyle("border-right: 3px dashed OliveDrab");
        } else if (selectedTabIndex == 1) {
            firstTab.assertStyle("border-bottom: 3px dashed OliveDrab");
            secondTab.assertStyle("border-right: 3px dashed OliveDrab");
        }

        emptySpaceCell.assertStyle("border-bottom: 3px dashed OliveDrab");

        tabSetAsTable.body().row(0).cell(0).assertStyle("border-bottom: 3px dashed OliveDrab");

        // tabStyle="background: azure; border-top: 1px dotted darkgreen; width: 70px;"
        if (selectedTabIndex == 0) {
            secondTab.assertStyle("background: azure; width: 70px; border-top: 1px dotted darkgreen");
        } else if (selectedTabIndex == 1) {
            firstTab.assertStyle("background: azure; width: 70px; border-top: 1px dotted darkgreen");
        }

        // selectedTabStyle="background: pink; border-top: 1px solid black;"
        if (selectedTabIndex == 0) {
            firstTab.assertStyle("background: pink; width: 70px; border-top: 1px solid black");
        } else if (selectedTabIndex == 1) {
            firstTab.assertStyle("background: azure; width: 70px; border-top: 1px dotted darkgreen");
        }

        // rolloverSelectedTabStyle="border: 2px dashed blue;"
        if (selectedTabIndex == 0) {
            firstTab.mouseOver();
            //todo: uncomment it if JSFC-2991 is in fixed state
/*
      assertBorderPropertyRenderedWell(selenium, firstTabId, null, WIDTH, "top", "2px");
      assertBorderPropertyRenderedWell(selenium, firstTabId, null, STYLE, "top", "dashed");
      assertBorderPropertyRenderedWell(selenium, firstTabId, null, COLOR, "top", "blue");
*/

            firstTab.assertStyle("border-bottom: 2px dashed blue");

            firstTab.mouseOut();
        } else if (selectedTabIndex == 1) {
            secondTab.mouseOver();
            //todo: uncomment it if JSFC-2991 is in fixed state
/*
      assertBorderPropertyRenderedWell(selenium, secondTabId, null, WIDTH, "top", "2px");
      assertBorderPropertyRenderedWell(selenium, secondTabId, null, STYLE, "top", "dashed");
      assertBorderPropertyRenderedWell(selenium, secondTabId, null, COLOR, "top", "blue");
*/

            secondTab.assertStyle("border-bottom: 2px dashed blue");

            secondTab.mouseOut();
        }

        // rolloverTabStyle="background: brown; font-weight: bold; border: 2px dotted green;"
        if (selectedTabIndex == 0) {
            secondTab.mouseOver();

            secondTab.assertStyle("background: brown; font-weight: bold; border: 2px dotted green");

            secondTab.mouseOut();
        } else if (selectedTabIndex == 1) {
            firstTab.mouseOver();

            firstTab.assertStyle("background: brown; font-weight: bold; border: 2px dotted green");

            firstTab.mouseOut();
        }

        // style="width: 300px;"
        tabSet.assertStyle("width: 300px");
    }

     @Test
    public void testDefaultView() {
        testAppFunctionalPage("/components/tabset/tabSet_defaultView.jsf");
        assertAppearanceNotChanged("TabSetDefaultView");
    }

}