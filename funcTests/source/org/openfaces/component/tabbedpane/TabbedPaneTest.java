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
package org.openfaces.component.tabbedpane;

import com.thoughtworks.selenium.Selenium;
import org.junit.Ignore;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.openfaces.test.RichFacesAjaxLoadingMode;
import org.seleniuminspector.ClientLoadingMode;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.LoadingMode;
import org.seleniuminspector.ServerLoadingMode;
import org.seleniuminspector.html.InputInspector;
import org.seleniuminspector.openfaces.OpenFacesAjaxLoadingMode;
import org.seleniuminspector.openfaces.TabSetInspector;
import org.seleniuminspector.openfaces.TabbedPaneInspector;

/**
 * @author Darya Shumilina
 */
public class TabbedPaneTest extends OpenFacesTestCase {
    @Test
    public void testReRenderThroughA4J() {
        testAppFunctionalPage("/components/tabbedpane/tabbedPane_a4j.jsf");

        ElementInspector firstTab = element("formID:tab_first");
        String oldTabFirst = firstTab.text();
        ElementInspector secondTab = element("formID:tab_second");
        String oldTabSecond = secondTab.text();
        ElementInspector firstContent = element("formID:first_content");
        String oldContentFirst = firstContent.text();

        secondTab.clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        ElementInspector secondContent = element("formID:second_content");
        String oldContentSecond = secondContent.text();

        element("formID:refresher").click();

        RichFacesAjaxLoadingMode.getInstance().waitForLoad();

        String newTabFirst = firstTab.text();
        String newTabSecond = secondTab.text();
        String newContentSecond = secondContent.text();

        firstTab.clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        String newContentFirst = firstContent.text();
        assertFalse(newTabFirst.equals(oldTabFirst));
        assertFalse(newTabSecond.equals(oldTabSecond));
        assertFalse(newContentFirst.equals(oldContentFirst));
        assertFalse(newContentSecond.equals(oldContentSecond));
    }

    @Test
    public void testWithA4JControlsInside() {
        testAppFunctionalPage("/components/tabbedpane/tabbedPane_a4j.jsf");

        InputInspector firstTab = new InputInspector("formID:tab_first_a4j");
        String oldTabFirst = firstTab.value();
        InputInspector secondTab = new InputInspector("formID:tab_second_a4j");
        String oldTabSecond = secondTab.value();
        ElementInspector firstTabContent = element("formID:first_content_a4j");
        String oldContentFirst = firstTabContent.text();

        secondTab.clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        ElementInspector secondTabContent = element("formID:second_content_a4j");
        String oldContentSecond = secondTabContent.text();

        element("formID:refresher_a4j").click();

        RichFacesAjaxLoadingMode.getInstance().waitForLoad();

        String newTabFirst = firstTab.value();
        String newTabSecond = secondTab.value();
        String newContentSecond = secondTabContent.text();

        firstTab.clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        String newContentFirst = firstTabContent.text();
        assertFalse(newTabFirst.equals(oldTabFirst));
        assertFalse(newTabSecond.equals(oldTabSecond));
        assertFalse(newContentFirst.equals(oldContentFirst));
        assertFalse(newContentSecond.equals(oldContentSecond));
    }

    //todo: uncomment when the  JSFC-3629 is fixed
    @Ignore
    @Test
    public void _testTabSelectionChangeClientLoadingMode() {
        checkSelectionChange(ClientLoadingMode.getInstance());
    }

    //todo: uncomment when the  JSFC-3629 is fixed
    @Ignore
    @Test
    public void _testTabSelectionChangeServerLoadingMode() {
        testAppFunctionalPage("/components/tabbedpane/tabbedPaneChangeSelectionStyling.jsf");
        checkSelectionChange(ServerLoadingMode.getInstance());
    }

    //todo: uncomment when the  JSFC-3629 is fixed
    @Ignore
    @Test
    public void _testTabSelectionChangeAjaxLoadingMode() {
        testAppFunctionalPage("/components/tabbedpane/tabbedPaneChangeSelectionStyling.jsf");
        checkSelectionChange(OpenFacesAjaxLoadingMode.getInstance());
    }

    //todo: uncomment when the  JSFC-3629 is fixed
    @Ignore
    @Test
    public void _testClientSideAPI() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/tabbedpane/tabbedPaneChangeSelectionStyling.jsf");
        TabbedPaneInspector tabbedPane = tabbedPane("formID:clientTP");

        // check getSelectedIndex()
        selenium.click("clientGetSelected");
        assertEquals("0", selenium.getText("empty_client"));
        verifyStyles(tabbedPane, 0);

        // check setSelectedIndex()
        selenium.click("clientSetSelected");
        selenium.click("clientGetSelected");
        assertEquals("1", selenium.getText("empty_client"));
        verifyStyles(tabbedPane, 1);
    }

    //todo: uncomment when JSFC-3626 is fixed
    @Test
    public void testSelectionChangeListener() {
        //todo: uncomment if JSFC-3001 is in 'fixed' state
        //checkSelectionChangeListener("ajax");
        checkSelectionChangeListener(ClientLoadingMode.getInstance());
        checkSelectionChangeListener(ServerLoadingMode.getInstance());
    }

    private void checkSelectionChangeListener(LoadingMode loadingMode) {
        testAppFunctionalPage("/components/tabbedpane/tabbedPaneSelectionChangeListener.jsf");

        ElementInspector asAttributeOutputText = element("formID:asAttributeOutput");
        boolean initialValueAsAttribute = Boolean.parseBoolean(asAttributeOutputText.text());
        ElementInspector asTagOutputText = element("formID:asTagOutput");
        boolean initialValueAsTag = Boolean.parseBoolean(asTagOutputText.text());

        TabSetInspector loadingModesTabSet = tabSet("formID:loadingModes");

        if (loadingMode instanceof ClientLoadingMode) {
            loadingModesTabSet.tabs().get(1).clickAndWait();
        } else if (loadingMode instanceof ServerLoadingMode) {
            loadingModesTabSet.tabs().get(2).clickAndWait();
        }

        tabbedPane("formID:asAttribute").tabSet().tabs().get(1).clickAndWait(loadingMode);
        tabbedPane("formID:asTag").tabSet().tabs().get(1).clickAndWait(loadingMode);

        if (loadingMode instanceof  ClientLoadingMode|| loadingMode instanceof OpenFacesAjaxLoadingMode) {
            element("formID:submit").clickAndWait();
        }

        boolean resultantValueAsAttribute = Boolean.parseBoolean(asAttributeOutputText.text());
        boolean resultantValueAsTag = Boolean.parseBoolean(asTagOutputText.text());

        assertEquals(initialValueAsAttribute, !resultantValueAsAttribute);
        assertEquals(initialValueAsTag, !resultantValueAsTag);
    }

    private void checkSelectionChange(LoadingMode loadingMode) {
        testAppFunctionalPage("/components/tabbedpane/tabbedPaneChangeSelectionStyling.jsf");

        TabbedPaneInspector tabbedPane = tabbedPane("formID:" + loadingMode + "TP");
        ElementInspector firstPane = tabbedPane.contentPanes().get(0);
        ElementInspector secondPane = tabbedPane.contentPanes().get(1);
        ElementInspector firstTab = tabbedPane.tabSet().tabs().get(0);
        ElementInspector secondTab = tabbedPane.tabSet().tabs().get(1);

        verifyStyles(tabbedPane, 0);

        ElementInspector getSelectedElement = element(loadingMode + "GetSelected");
        getSelectedElement.click();
        ElementInspector emptyElement = element("empty_" + loadingMode);
        emptyElement.assertText("0");
        firstPane.assertText("First tab content");

        secondTab.click();
        loadingMode.waitForLoad();

        verifyStyles(tabbedPane, 1);
        getSelectedElement.click();
        emptyElement.assertText("1");
        secondPane.assertText("Second tab content");

        firstTab.click();
        if (loadingMode instanceof ServerLoadingMode) {
            loadingMode.waitForLoad();
        }
        verifyStyles(tabbedPane, 0);
        getSelectedElement.click();
        emptyElement.assertText("0");
        firstPane.assertText("First tab content");
    }

    private void verifyStyles(TabbedPaneInspector tabbedPane, int selectedTabIndex) {
        TabSetInspector tabSet = tabbedPane.tabSet();
        ElementInspector firstTab = tabSet.tabs().get(0);
        ElementInspector secondTab = tabSet.tabs().get(1);
        ElementInspector firstPane = tabbedPane.contentPanes().get(0);
        ElementInspector secondPane = tabbedPane.contentPanes().get(1);
        String emptySpacePath_0 = "tbody[0]/tr[0]/td[0]";
        String emptySpacePath_1 = "tbody[0]/tr[0]/td[2]";
        String emptySpacePath_2 = "tbody[0]/tr[0]/td[4]";
        String containerPath = "tbody[0]/tr[0]/td[0]";

        // frontBorderStyle="2px solid orange"
        if (selectedTabIndex == 0) {
            firstTab.assertStyle("border-right: 2px solid orange; border-top: 2px solid orange");
            secondTab.assertStyle("border-bottom: 2px solid orange");
        } else if (selectedTabIndex == 1) {
            secondTab.assertStyle("border-right: 2px solid orange; border-top: 2px solid orange");
            firstTab.assertStyle("border-bottom: 2px solid orange");
        }

        firstPane.parentNode().assertStyle("border-right: 2px solid orange; border-bottom: 2px solid orange");
        tabSet.subElement(emptySpacePath_0).assertStyle("border-bottom: 2px solid orange");
        tabSet.subElement(emptySpacePath_1).assertStyle("border-bottom: 2px solid orange");
        tabSet.subElement(emptySpacePath_2).assertStyle("border-bottom: 2px solid orange");

        // backBorderStyle="3px dashed magenta"
        if (selectedTabIndex == 0) {
            secondTab.assertStyle("border-top: 3px dashed magenta; border-right: 3px dashed magenta");
        } else if (selectedTabIndex == 1) {
            firstTab.assertStyle("border-top: 3px dashed magenta; border-right: 3px dashed magenta");
        }

        // tabGapWidth="15"
        tabSet.subElement(emptySpacePath_0).assertWidth(15, 0);
        tabSet.subElement(emptySpacePath_1).assertWidth(15, 0);

        // containerStyle="background: gray; text-decoration: underline; color: red;"
        if (selectedTabIndex == 0) {
            firstPane.subElement(containerPath).assertStyle("background: gray; text-decoration: underline; color: red");
        } else if (selectedTabIndex == 1) {
            secondPane.subElement(containerPath).assertStyle("background: gray; text-decoration: underline; color: red");
        }

        // selectedTabStyle="background: DarkGreen; font-weight: bold; color: white;"
        if (selectedTabIndex == 0) {
            firstTab.assertStyle("background: DarkGreen; font-weight: bold; color: white");
        } else if (selectedTabIndex == 1) {
            secondTab.assertStyle("background: DarkGreen; font-weight: bold; color: white");
        }

        // tabEmptySpaceStyle="background: LightBlue;"
        tabSet.subElement(emptySpacePath_0).assertStyle("background: LightBlue");
        tabSet.subElement(emptySpacePath_1).assertStyle("background: LightBlue");
        tabSet.subElement(emptySpacePath_2).assertStyle("background: LightBlue");

        // tabStyle="color: red;"
        if (selectedTabIndex == 0) {
            secondTab.assertStyle("color: red");
        } else if (selectedTabIndex == 1) {
            firstTab.assertStyle("color: red");
        }

        // rolloverContainerStyle="background: red; font-weight: bold; color: gray;"
        if (selectedTabIndex == 0) {
            firstPane.mouseOver();
            firstPane.subElement(containerPath).assertStyle("background: red; font-weight: bold; color: gray");
        } else if (selectedTabIndex == 1) {
            secondPane.mouseOver();
            secondPane.subElement(containerPath).assertStyle("background: red; font-weight: bold; color: gray");
        }

        // rolloverStyle="border: 2px dashed brown;"
        tabbedPane.assertStyle("border: 2px dashed brown");
        if (selectedTabIndex == 0)
            firstPane.mouseOut();
        else if (selectedTabIndex == 1)
            secondPane.mouseOut();

        //todo: uncomment if JSFC-2996 is in 'fixed' state
        //rolloverSelectedTabStyle="background: pink;"
/*
    if (selectedTabIndex == 0) {
      selenium.mouseOver(firstTabId);
      assertCSSPropertyRenderedWell(selenium, firstTabId, null, BACKGROUND_COLOR, "pink", 0);
      selenium.mouseOut(firstTabId);
    } else if (selectedTabIndex == 1) {
      selenium.mouseOver(secondTabId);
      assertCSSPropertyRenderedWell(selenium, secondTabId, null, BACKGROUND_COLOR, "pink", 0);
      selenium.mouseOut(secondTabId);
    }
*/

        // rolloverTabStyle="background: yellow;"
        if (selectedTabIndex == 0) {
            secondTab.mouseOver();
            secondTab.assertStyle("background: yellow");
            secondTab.mouseOut();
        } else if (selectedTabIndex == 1) {
            firstTab.mouseOver();
            firstTab.assertStyle("background: yellow");
            firstTab.mouseOut();
        }

        // style="height: 200px; width: 250px;"
        tabbedPane.assertSize(200, 250);
    }

    @Test
    public void testDefaultView() {
        testAppFunctionalPage("/components/tabbedpane/tabbedpane_defaultView.jsf");
        assertAppearanceNotChanged("TabbedPaneDefaultView");
    }
}