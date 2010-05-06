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
package org.openfaces.component.miscellaneous;

import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.openfaces.DropDownFieldFilterInspector;
import org.seleniuminspector.openfaces.DropDownFieldInspector;
import org.seleniuminspector.openfaces.FoldingPanelInspector;
import org.seleniuminspector.openfaces.SuggestionFieldInspector;
import org.seleniuminspector.openfaces.TabSetInspector;
import org.seleniuminspector.openfaces.TabbedPaneInspector;
import org.seleniuminspector.openfaces.OpenFacesAjaxLoadingMode;
import org.seleniuminspector.ServerLoadingMode;
import org.seleniuminspector.ElementInspector;

import java.awt.event.KeyEvent;

/**
 * @author Darya Shumilina
 */
public class LoadBundleTest extends OpenFacesTestCase {
    private static final String TODAY_AR = "\u0645\u0648\u064A\u0644\u0627";
    private static final String TODAY_ES = "informaci\u00F3n";
    private static final String TODAY_JA = "\u4ECA\u65E5";
    private static final String TODAY_RU = "\u0421\u0435\u0433\u043E\u0434\u043D\u044F";

    private static final String WEEK_AR = "\u0644\u0627\u0634\u0626";
    private static final String WEEK_ES = "instala\u00E7\u00E3o";
    private static final String WEEK_JA = "\u9031";
    private static final String WEEK_RU = "\u0421\u0431\u0440\u043E\u0441";

    @Test
    public void testWithDataTable() {
        testAppFunctionalPage("/components/loadbundle/withDataTable.jsf");

        String dataTableID = "formID:loadbundleDataTable";
        String firstHeader = dataTableID + ":first_header";
        String secondHeader = dataTableID + ":second_header";
        String firstFooter = dataTableID + ":first_footer";
        String secondFooter = dataTableID + ":second_footer";

        ElementInspector firstHeaderElement = element(firstHeader);
        ElementInspector secondHeaderElement = element(secondHeader);
        ElementInspector firstFooterElement = element(firstFooter);
        ElementInspector secondFooterElement = element(secondFooter);

        ElementInspector nextPaginationButton = dataTablePaginator("formID:loadbundleDataTable:paginator").nextPage();
        // perform pagination and verify data 'ar' locale
        for (int i = 0; i < 2; i++) {
            firstHeaderElement.assertText(TODAY_AR);
            secondHeaderElement.assertText(WEEK_AR);
            for (int j = 0; j < 3; j++) {
                element(dataTableID + ":" + j + ":first_body").assertText(TODAY_AR);
                element(dataTableID + ":" + j + ":second_body").assertText(WEEK_AR);
            }
            firstFooterElement.assertText(TODAY_AR);
            secondFooterElement.assertText(WEEK_AR);

            nextPaginationButton.clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        }

        TabSetInspector localeChanger = tabSet("formID:localeChanger");

        // perform sorting by first column and verify data 'es' locale
        localeChanger.tabs().get(1).clickAndWait();
        testAppFunctionalPage("/components/loadbundle/withDataTable.jsf"); // issue a GET request for view to update locale in JSP
        firstHeaderElement.clickAndWait(OpenFacesAjaxLoadingMode.getInstance());

        firstHeaderElement.assertText(TODAY_ES);
        secondHeaderElement.assertText(WEEK_ES);
        for (int j = 0; j < 3; j++) {
            element(dataTableID + ":" + j + ":first_body").assertText(TODAY_ES);
            element(dataTableID + ":" + j + ":second_body").assertText(WEEK_ES);
        }
        firstFooterElement.assertText(TODAY_ES);
        secondFooterElement.assertText(WEEK_ES);

        // perform sorting by second column and verify data 'ja' locale
        localeChanger.tabs().get(2).clickAndWait();
        testAppFunctionalPage("/components/loadbundle/withDataTable.jsf"); // issue a GET request for view to update locale in JSP
        secondHeaderElement.clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        firstHeaderElement.assertText(TODAY_JA);
        secondHeaderElement.assertText(WEEK_JA);
        for (int j = 0; j < 3; j++) {
            element(dataTableID + ":" + j + ":first_body").assertText(TODAY_JA);
            element(dataTableID + ":" + j + ":second_body").assertText(WEEK_JA);
        }
        firstFooterElement.assertText(TODAY_JA);
        secondFooterElement.assertText(WEEK_JA);

        // perform filtering by first column and verify data 'ru' locale
        localeChanger.tabs().get(3).clickAndWait();
        testAppFunctionalPage("/components/loadbundle/withDataTable.jsf"); // issue a GET request for view to update locale in JSP
        dataTable("formID:loadbundleDataTable").column(0).filter(DropDownFieldFilterInspector.class, "formID:loadbundleDataTable:filter1").makeFiltering(TODAY_RU);

        for (int i = 0; i < 2; i++) {
            firstHeaderElement.assertText(TODAY_RU);
            secondHeaderElement.assertText(WEEK_RU);
            for (int j = 0; j < 3; j++) {
                element(dataTableID + ":" + j + ":first_body").assertText(TODAY_RU);
                element(dataTableID + ":" + j + ":second_body").assertText(WEEK_RU);
            }
            firstFooterElement.assertText(TODAY_RU);
            secondFooterElement.assertText(WEEK_RU);

            nextPaginationButton.clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        }

        // reset tab index for further running tests
        localeChanger.tabs().get(0).clickAndWait();
    }

    @Test
    public void testWithropDownField() {
        testAppFunctionalPage("/components/loadbundle/withDropDownField.jsf");

        DropDownFieldInspector dropDown = dropDownField("formID:plants");
        ElementInspector secondSuggestion = dropDown.popup().items().get(2);

        // check 'ar' locale
        dropDown.button().mouseDown();
        secondSuggestion.assertText(TODAY_AR);

        TabSetInspector localeChanger = tabSet("formID:localeChanger");
        // check 'es' locale
        localeChanger.tabs().get(1).clickAndWait();
        testAppFunctionalPage("/components/loadbundle/withDropDownField.jsf"); // issue a GET request for view to update locale in JSP
        dropDown.button().mouseDown();
        secondSuggestion.assertText(TODAY_ES);

        // check 'ja' locale
        localeChanger.tabs().get(2).clickAndWait();
        testAppFunctionalPage("/components/loadbundle/withDropDownField.jsf"); // issue a GET request for view to update locale in JSP
        dropDown.button().mouseDown();
        secondSuggestion.assertText(TODAY_JA);

        // check 'ru' locale
        localeChanger.tabs().get(3).clickAndWait();
        testAppFunctionalPage("/components/loadbundle/withDropDownField.jsf"); // issue a GET request for view to update locale in JSP
        dropDown.button().mouseDown();
        secondSuggestion.assertText(TODAY_RU);

        // reset tab index for further running tests
        localeChanger.tabs().get(0).clickAndWait();
    }

    @Test
    public void testWithFoldingPanel() {
        testAppFunctionalPage("/components/loadbundle/withFoldingPanel.jsf");

        FoldingPanelInspector foldingPanel = foldingPanel("formID:loadBundleFoldingPanel");

        // check 'ar' locale
        foldingPanel.caption().assertText(TODAY_AR);
        foldingPanel.setExpanded(true);
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        foldingPanel.content().assertText(TODAY_AR + WEEK_AR);

        TabSetInspector localeChanger = tabSet("formID:localeChanger");

        // check 'es' locale
        localeChanger.tabs().get(1).clickAndWait();
        testAppFunctionalPage("/components/loadbundle/withFoldingPanel.jsf"); // issue a GET request for view to update locale in JSP
        foldingPanel.caption().assertText(TODAY_ES);
        foldingPanel.setExpanded(true);
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        foldingPanel.content().assertText(TODAY_ES + WEEK_ES);

        // check 'ja' locale
        localeChanger.tabs().get(2).clickAndWait();
        testAppFunctionalPage("/components/loadbundle/withFoldingPanel.jsf"); // issue a GET request for view to update locale in JSP
        foldingPanel.caption().assertText(TODAY_JA);
        foldingPanel.setExpanded(true);
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        foldingPanel.content().assertText(TODAY_JA + WEEK_JA);

        // check 'ru' locale
        localeChanger.tabs().get(3).clickAndWait();
        testAppFunctionalPage("/components/loadbundle/withFoldingPanel.jsf"); // issue a GET request for view to update locale in JSP
        foldingPanel.caption().assertText(TODAY_RU);
        foldingPanel.setExpanded(true);
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        foldingPanel.content().assertText(TODAY_RU + WEEK_RU);

        // reset tab index for further running tests
        localeChanger.tabs().get(0).clickAndWait();
    }

    @Test
    public void testWithSuggestionField() {
        testAppFunctionalPage("/components/loadbundle/withSuggestionField.jsf");

        String suggestionFieldId = "formID:plants";
        SuggestionFieldInspector suggestionField = suggestionField(suggestionFieldId);
        ElementInspector secondSuggestion = suggestionField.popup().items().get(2);

        // check 'ar' locale
        suggestionField.keyDown(KeyEvent.VK_DOWN);
        secondSuggestion.assertText(WEEK_AR);

        TabSetInspector localeChanger = tabSet("formID:localeChanger");

        // check 'es' locale
        localeChanger.tabs().get(1).clickAndWait();
        testAppFunctionalPage("/components/loadbundle/withSuggestionField.jsf"); // issue a GET request for view to update locale in JSP
        suggestionField.keyDown(KeyEvent.VK_DOWN);
        secondSuggestion.assertText(WEEK_ES);

        // check 'ja' locale
        localeChanger.tabs().get(2).clickAndWait();
        testAppFunctionalPage("/components/loadbundle/withSuggestionField.jsf"); // issue a GET request for view to update locale in JSP
        suggestionField.keyDown(KeyEvent.VK_DOWN);
        secondSuggestion.assertText(WEEK_JA);

        // check 'ru' locale
        localeChanger.tabs().get(3).clickAndWait();
        testAppFunctionalPage("/components/loadbundle/withSuggestionField.jsf"); // issue a GET request for view to update locale in JSP
        suggestionField.keyDown(KeyEvent.VK_DOWN);
        secondSuggestion.assertText(WEEK_RU);

        // reset tab index for further running tests
        localeChanger.tabs().get(0).clickAndWait();
    }

    @Test
    public void testWithTabbedPane() {
        testAppFunctionalPage("/components/loadbundle/withTabbedPane.jsf");
        TabSetInspector localeChanger = tabSet("formID:localeChanger");

        TabbedPaneInspector tabbedPane = tabbedPane("formID:tabbedPaneID");
        TabSetInspector tabSet = tabbedPane.tabSet();

        ElementInspector secondTab = element("formID:secondTabID");

        // check 'ar' locale
        tabSet.tabs().get(0).assertText(TODAY_AR);
        secondTab.assertText(WEEK_AR);

        tabbedPane.contentPanes().get(0).assertText(WEEK_AR);
        tabbedPane.setPageIndex(1);
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        tabbedPane.contentPanes().get(1).assertText(TODAY_AR);

        // check 'es' locale
        localeChanger.setTabIndex(1, ServerLoadingMode.getInstance());
        testAppFunctionalPage("/components/loadbundle/withTabbedPane.jsf"); // issue a GET request for view to update locale in JSP
        tabbedPane.setPageIndex(0, OpenFacesAjaxLoadingMode.getInstance());
        tabSet.tabs().get(0).assertText(TODAY_ES);
        secondTab.assertText(WEEK_ES);

        tabbedPane.contentPanes().get(0).assertText(WEEK_ES);
        tabbedPane.setPageIndex(1);
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        tabbedPane.contentPanes().get(1).assertText(TODAY_ES);

        // check 'ja' locale
        localeChanger.setTabIndex(2, ServerLoadingMode.getInstance());
        testAppFunctionalPage("/components/loadbundle/withTabbedPane.jsf"); // issue a GET request for view to update locale in JSP
        tabbedPane.setPageIndex(0, OpenFacesAjaxLoadingMode.getInstance());
        tabSet.tabs().get(0).assertText(TODAY_JA);
        secondTab.assertText(WEEK_JA);

        tabbedPane.contentPanes().get(0).assertText(WEEK_JA);
        tabbedPane.setPageIndex(1);
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        tabbedPane.contentPanes().get(1).assertText(TODAY_JA);

        // check 'ru' locale
        localeChanger.setTabIndex(3, ServerLoadingMode.getInstance());
        testAppFunctionalPage("/components/loadbundle/withTabbedPane.jsf"); // issue a GET request for view to update locale in JSP
        tabbedPane.setPageIndex(0, OpenFacesAjaxLoadingMode.getInstance());
        tabSet.tabs().get(0).assertText(TODAY_RU);
        secondTab.assertText(WEEK_RU);

        tabbedPane.contentPanes().get(0).assertText(WEEK_RU);
        tabbedPane.setPageIndex(1);
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        tabbedPane.contentPanes().get(1).assertText(TODAY_RU);

        // reset tab index for further running tests
        localeChanger.tabs().get(0).clickAndWait();
    }

    @Test
    public void testWithTreeTable() {
        testAppFunctionalPage("/components/loadbundle/withTreeTable.jsf");

        String treeTableId = "formID:loadBundleTree";

        // check 'ar' locale
        element(treeTableId + ":0:categoryID").assertText(TODAY_AR);
        window().document().getElementsByTagName("img").get(0).clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        element(treeTableId + ":0_0:nameID").assertText(WEEK_AR);
        element(treeTableId + ":0_1:nameID").assertText(WEEK_AR);

        TabSetInspector localeChanger = tabSet("formID:localeChanger");

        // check 'es' locale
        element("formID:collapseNodesBtn").clickAndWait();
        localeChanger.tabs().get(1).clickAndWait();
        testAppFunctionalPage("/components/loadbundle/withTreeTable.jsf"); // issue a GET request for view to update locale in JSP
        element(treeTableId + ":2:categoryID").assertText(TODAY_ES);
        window().document().getElementsByTagName("img").get(1).clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        element(treeTableId + ":1_0:nameID").assertText(WEEK_ES);
        element(treeTableId + ":1_1:nameID").assertText(WEEK_ES);

        // check 'ja' locale
        element("formID:collapseNodesBtn").clickAndWait();
        localeChanger.tabs().get(2).clickAndWait();
        testAppFunctionalPage("/components/loadbundle/withTreeTable.jsf"); // issue a GET request for view to update locale in JSP
        element(treeTableId + ":3:categoryID").assertText(TODAY_JA);
        window().document().getElementsByTagName("img").get(2).clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        element(treeTableId + ":2_0:nameID").assertText(WEEK_JA);
        element(treeTableId + ":2_1:nameID").assertText(WEEK_JA);

        // check 'ru' locale
        element("formID:collapseNodesBtn").clickAndWait();
        localeChanger.tabs().get(3).clickAndWait();
        testAppFunctionalPage("/components/loadbundle/withTreeTable.jsf"); // issue a GET request for view to update locale in JSP
        element(treeTableId + ":4:categoryID").assertText(TODAY_RU);
        window().document().getElementsByTagName("img").get(3).clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        element(treeTableId + ":3_0:nameID").assertText(WEEK_RU);

        // reset tab index for further running tests
        localeChanger.tabs().get(0).clickAndWait();
    }

}