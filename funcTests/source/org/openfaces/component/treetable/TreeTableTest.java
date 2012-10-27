/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.treetable;

import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.openfaces.test.RichFacesAjaxLoadingMode;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.LoadingMode;
import org.seleniuminspector.ServerLoadingMode;
import org.seleniuminspector.openfaces.OpenFacesAjaxLoadingMode;
import org.seleniuminspector.openfaces.TabSetInspector;
import org.seleniuminspector.openfaces.TreeTableInspector;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

/**
 * @author Darya Shumilina
 */
public class TreeTableTest extends OpenFacesTestCase {
    @Test
    public void testReRenderThroughA4J() {
        testAppFunctionalPage("/components/treetable/treeTable_a4j.jsf");

        List<ElementInspector> imgs;
        imgs = window().document().getElementsByTagName("img");
        for (int i = 0; i < 12; i++) {
            imgs.get(i).clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        }

        List<ElementInspector> divs = window().document().getElementsByTagName("div");
        String[] oldTreeValues = new String[45];
        for (int i = 0; i < oldTreeValues.length; i++) {
            oldTreeValues[i] = divs.get(i).text();
        }
        imgs = window().document().getElementsByTagName("img");
        for (int i = 0; i < 12; i++) {
            imgs.get(i).click();
        }

        element("formID:refresher").click();
        RichFacesAjaxLoadingMode.getInstance().waitForLoad();
        imgs = window().document().getElementsByTagName("img");
        for (int i = 0; i < 12; i++) {
            imgs.get(i).clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        }
        divs = window().document().getElementsByTagName("div");
        String[] newTreeValues = new String[45];
        for (int i = 0; i < newTreeValues.length; i++) {
            newTreeValues[i] = divs.get(i).text();
        }
        assertFalse(Arrays.equals(newTreeValues, oldTreeValues));
    }

    /**
     * Check simple TreeTable with static structure and verify rows content (with Ajax and without)
     */

    @Test
    public void testStaticStructure() {
        staticTreeStructure(OpenFacesAjaxLoadingMode.getInstance());
        staticTreeStructure(ServerLoadingMode.getInstance());
    }

    /**
     * Check simple TreeTable with dynamic structure and verify rows content (with Ajax and without)
     */
    @Test
    public void testDynamicStructure() {
        dynamicTreeStructure(OpenFacesAjaxLoadingMode.getInstance());
        dynamicTreeStructure(ServerLoadingMode.getInstance());
    }

    /**
     * Check TreeTable single selection
     */
    @Test
    public void testSingleSelectionAndKeyboardNavigation() {
        closeBrowser();
        testAppFunctionalPage("/components/treetable/treeTableSingleSelection.jsf");
        /*check selection and keyboard navigation on the simple TreeTable*/
        Actions click = new Actions(getDriver()).moveToElement(
                getDriver().findElement(By.id("formID:singleSelectionTreeTableID:0:categoryID")))
                .click();
        click.build().perform();

        ElementInspector emptyElement = element("empty");
        ElementInspector treeTable = element("formID:singleSelectionTreeTableID");
        treeTable.focus();
        for (int i = 1; i < 26; i++) {
            if (i == 1 || i == 4 || i == 16 || i == 19 || i == 21 || i == 24) {
                //click right arrow to expand first TreeTable node
                new Actions(getDriver()).sendKeys(Keys.ARROW_RIGHT).build().perform();
                OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
            }
            //get selected index value
            emptyElement.assertText(String.valueOf(i - 1));
            //click down arrow
            new Actions(getDriver()).sendKeys(Keys.ARROW_DOWN).build().perform();
        }
        //check mouse selection for the same TreeTable
        element("formID:singleSelectionTreeTableID:1:categoryID").click();
        emptyElement.assertText("3");
        element("formID:singleSelectionTreeTableID:4_1:nameID").click();
        emptyElement.assertText("22");

        /*Check TreeTable with defined 'nodePath' and 'nodeData' attributes*/
        element("formID:singleNodePathSelectionTreeTableID:3:categoryID").click();
        ElementInspector selectionNodePath = element("selectionNodePathID");
        String indexBeforeSubmitNodePathTreeTable = selectionNodePath.text();

        element("formID:singleNodeDataSelectionTreeTableID:1:categoryID").keyPress(KeyEvent.VK_RIGHT);
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        treeTable.keyPress(KeyEvent.VK_DOWN);
        ElementInspector selectionNodeData = element("selectionNodeDataID");
        String indexBeforeSubmitNodeDataTreeTable = selectionNodeData.text();
        element("formID:submitID").clickAndWait();

        selectionNodePath.assertText(indexBeforeSubmitNodePathTreeTable);
        selectionNodeData.assertText(indexBeforeSubmitNodeDataTreeTable);
    }

    /**
     * Check TreeTable multiple selection
     */
    //todo: add checking multiple selection functionality with the 'Ctrl' key
    @Test
    public void testMultipleSelectionAndKeyboardNavigation() {
        testAppFunctionalPage("/components/treetable/treeTableMultipleSelection.jsf");
        ElementInspector categoryOutput = element("formID:multipleSelectionTreeTableID:0:categoryID");
        new Actions(getDriver()).moveToElement(
                getDriver().findElement(By.id("formID:multipleSelectionTreeTableID:0:categoryID")))
                .click().build().perform();


        TreeTableInspector multipleSelectionTreeTable = treeTable("formID:multipleSelectionTreeTableID");
        multipleSelectionTreeTable.focus();
        /*check keyboard navigation and selection*/
        //select root nodes
        for (int i = 0; i < 5; i++) {
            createEvent(multipleSelectionTreeTable, null, EventType.KEY, "keypress", KeyEvent.VK_DOWN, true);
        }
        ElementInspector emptyElement = element("empty");
        emptyElement.assertText("  0 1 2 3 4 5");

        for (int i = 0; i < 6; i++) {
            multipleSelectionTreeTable.getElementsByTagName("img").get(i).clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        }

        categoryOutput.click();
        for (int i = 0; i < 25; i++) {
            createEvent(multipleSelectionTreeTable, null, EventType.KEY, "keypress", KeyEvent.VK_DOWN, true);
        }


        emptyElement.assertText("  0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24");
        for (int i = 0; i < 6; i++) {
            multipleSelectionTreeTable.getElementsByTagName("img").get(i).clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        }

        categoryOutput.click();
        for (int i = 0; i < 5; i++) {
            createEvent(element("formID:multipleSelectionTreeTableID"), null, EventType.KEY, "keypress", KeyEvent.VK_DOWN, true);
        }
        emptyElement.assertText("  0 3 15 18 20 23");

        /*check 'nodePaths' and 'nodeDatas' attributes*/
        //verify is attributes took right initial values
        ElementInspector nodePathsOutput = element("formID:nodePathsID");
        nodePathsOutput.assertText("Document BrowsingNetwork Access");
        ElementInspector nodeDataOutput = element("formID:nodeDatasID");
        nodeDataOutput.assertText("Document BrowsingDocument Deletion");
        ElementInspector selectionNodePathsDiv = element("selectionNodePathsID");
        selectionNodePathsDiv.assertText("  1 5");
        ElementInspector selectionNodeDataDiv = element("selectionNodeDatasID");
        selectionNodeDataDiv.assertText("  1 4");

        //check root nodes
        element("formID:multipleNodePathsSelectionTreeTableID:0:categoryID").click();
        TreeTableInspector multipleNodePathsTreeTable = treeTable("formID:multipleNodePathsSelectionTreeTableID");
        new Actions(getDriver()).moveToElement(
                getDriver().findElement(By.xpath(multipleNodePathsTreeTable.getXPath())))
                .sendKeys(Keys.ARROW_DOWN).build().perform();
        element("formID:multipleNodeDatasSelectionTreeTableID:0:categoryID").click();
        TreeTableInspector multipleNodeDataTreeTable = treeTable("formID:multipleNodeDatasSelectionTreeTableID");
        new Actions(getDriver()).moveToElement(
                getDriver().findElement(By.xpath(multipleNodeDataTreeTable.getXPath())))
                .sendKeys(Keys.ARROW_DOWN).build().perform();

        //check root + expanded child nodes
        element("formID:multipleNodePathsSelectionTreeTableID:4:categoryID").click();

        //click right arrow
        new Actions(getDriver()).moveToElement(
                getDriver().findElement(By.xpath(multipleNodeDataTreeTable.getXPath())))
                .sendKeys(Keys.ARROW_RIGHT).build().perform();
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        for (int i = 0; i < 3; i++) {
            createEvent(multipleNodePathsTreeTable, null, EventType.KEY, "keypress", KeyEvent.VK_DOWN, true);
        }

        new Actions(getDriver()).moveToElement(
                getDriver().findElement(By.id("formID:multipleNodeDatasSelectionTreeTableID:2:categoryID")))
                .click().build().perform();

        multipleNodeDataTreeTable.keyPress(Keys.ARROW_RIGHT);
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        for (int i = 0; i < 3; i++) {
            createEvent(multipleNodeDataTreeTable, null, EventType.KEY, "keypress", KeyEvent.VK_DOWN, true);
        }
        element("formID:submitID").clickAndWait();
        nodePathsOutput.assertText("Document DeletionSemen SemenychIvan IvanychNetwork Access");
        nodeDataOutput.assertText("Document CreationAdministratorIvan IvanychDocument Modification");
        selectionNodePathsDiv.assertText("  4 5 6 7");
        selectionNodeDataDiv.assertText("  2 3 4 5");
        //todo: checking with collapsed child node should be added after fix of 'JSFC-2603'
    }

    //Created two different tests because of JSFC-3572
    @Test
    public void testSortingAjax() {
        testSorting(OpenFacesAjaxLoadingMode.getInstance());
    }

    @Test
    public void testSortingServer() {
        testSorting(ServerLoadingMode.getInstance());
    }

    @Test
    public void testKeyboardNavigation() {
        testAppFunctionalPage("/components/treetable/treeTableKeyboardNavigation.jsf");

        TreeTableInspector treeTable = treeTable("formID:treeTableKeyboardNavigation");

        treeTable.focus();

        ElementInspector emptyElement = element("empty");
        //down arrow
        createEvent(treeTable, null, EventType.KEY, "keypress", KeyEvent.VK_DOWN, false);
        emptyElement.assertText("  0");

        //'End' button
                createEvent(treeTable, null, EventType.KEY, "keypress", KeyEvent.VK_END, false);
        emptyElement.assertText("  6");

        //up arrow
        createEvent(treeTable, null, EventType.KEY, "keypress", KeyEvent.VK_UP, false);
        emptyElement.assertText("  5");

        //'Home' button
        createEvent(treeTable, null, EventType.KEY, "keypress", KeyEvent.VK_HOME, false);
        emptyElement.assertText("  0");

        //left arrow
        createEvent(treeTable, null, EventType.KEY, "keypress", KeyEvent.VK_LEFT, false);

        //right arrow
        createEvent(treeTable, null, EventType.KEY, "keypress", KeyEvent.VK_RIGHT, false);

        //'minus' sign
        createEvent(treeTable, null, EventType.KEY, "keypress", KeyEvent.VK_SUBTRACT, false);

        //'plus' sign
        createEvent(treeTable, null, EventType.KEY, "keypress", KeyEvent.VK_ADD, false);

        //'Shift' + down arrow
        for (int i = 0; i < 6; i++) {
            createEvent(treeTable, null, EventType.KEY, "keypress", KeyEvent.VK_DOWN, true);
        }
        emptyElement.assertText("  0 1 2 3 4 5 6");
        //'Shift' + up arrow
        for (int i = 0; i < 6; i++) {
            createEvent(treeTable, null, EventType.KEY, "keypress", KeyEvent.VK_UP, true);
        }
        emptyElement.assertText("  0");

        //'Shift' + 'End'
        createEvent(treeTable, null, EventType.KEY, "keypress", KeyEvent.VK_END, true);
        emptyElement.assertText("  0 1 2 3 4 5 6");

        //'Shift' + 'Home'
        createEvent(treeTable, null, EventType.KEY, "keypress", KeyEvent.VK_HOME, true);
        emptyElement.assertText("  0");
    }

    /**
     * TODO Check TreeTable features combination: sorting, selection and filtering with different filter kinds
     */
    @Test
    public void testTreeTableFeaturesCombination() {

    }

    /**
     * TODO Check TreeTable function 'q_refreshTreeTable'
     */
    @Test
    public void test_q_refreshTreeTable() {

    }

    /**
     * TODO Check <o:selectAllCheckbox> tag inside checkbox column
     */
    @Test
    public void testSelectAllCheckboxInsideCheckboxColumnFunctionality() {

    }

    /**
     * TODO Check <o:selectAllCheckbox> tag inside selection column
     */
    @Test
    public void testSelectAllCheckboxInsideSelectionColumnFunctionality() {

    }

    /**
     * TODO Check preloaded loading mode
     */
    @Test
    public void testPreloadedLoadingMode() {

    }

    /**
     * TODO Test <o:treeColumn> tag functionality
     */
    @Test
    public void testTreeColumnFunctionality() {

    }

    /**
     * TODO No data row funtionality
     */
    @Test
    public void testNoDataRowFunctionality() {

    }

    private void staticTreeStructure(LoadingMode loadingMode) {
        testAppFunctionalPage("/components/treetable/treeTableStaticStructure.jsf");
        TabSetInspector loadingModes = tabSet("formID:loadingModes");
        if (loadingMode instanceof ServerLoadingMode) {
            loadingModes.tabs().get(1).clickAndWait(loadingMode);
        }
        TreeTableInspector treeTable = treeTable("formID:treeTableStaticStructureID");
        treeTable.bodyRow(0).cell(0).assertText("Colors");

        treeTable.bodyRow(0).expansionToggle().clickAndWait(loadingMode);
        treeTable.bodyRow(1).cell(0).assertText("Warm colors");
        treeTable.bodyRow(2).cell(0).assertText("Cold colors");

        treeTable.bodyRow(1).expansionToggle().clickAndWait(loadingMode);
        treeTable.bodyRow(2).cell(0).assertText("Red");
        treeTable.bodyRow(3).cell(0).assertText("Yellow");

        treeTable.bodyRow(4).expansionToggle().clickAndWait(loadingMode);
        treeTable.bodyRow(5).cell(0).assertText("Blue");
        treeTable.bodyRow(6).cell(0).assertText("Purple");

        if (loadingMode instanceof ServerLoadingMode) {
            // reset page index for further test to run correctly
            loadingModes.tabs().get(0).clickAndWait(loadingMode);
        }

    }

    private void dynamicTreeStructure(LoadingMode loadingMode) {
        testAppFunctionalPage("/components/treetable/treeTableDynamicStructure.jsf");
        TabSetInspector loadingModes = tabSet("formID:loadingModes");
        if (loadingMode instanceof ServerLoadingMode) {
            loadingModes.tabs().get(1).clickAndWait(loadingMode);
        }

        element("formID:dynamicTreeStructureID:0:categoryID").assertText("User Management");
        element("formID:dynamicTreeStructureID:1:categoryID").assertText("Document Browsing");
        element("formID:dynamicTreeStructureID:2:categoryID").assertText("Document Creation");
        element("formID:dynamicTreeStructureID:3:categoryID").assertText("Document Modification");
        element("formID:dynamicTreeStructureID:4:categoryID").assertText("Document Deletion");
        element("formID:dynamicTreeStructureID:5:categoryID").assertText("Network Access");

        window().document().getElementsByTagName("img").get(0).clickAndWait(loadingMode);
        element("formID:dynamicTreeStructureID:0_0:nameID").assertText("Manager");
        element("formID:dynamicTreeStructureID:0_1:nameID").assertText("Administrator");

        window().document().getElementsByTagName("img").get(1).clickAndWait(loadingMode);
        element("formID:dynamicTreeStructureID:1_0:nameID").assertText("Guest");
        element("formID:dynamicTreeStructureID:1_1:nameID").assertText("Regular User 1");
        element("formID:dynamicTreeStructureID:1_2:nameID").assertText("Regular User 2");
        element("formID:dynamicTreeStructureID:1_3:nameID").assertText("Regular User 3");
        element("formID:dynamicTreeStructureID:1_4:nameID").assertText("Regular User 4");
        element("formID:dynamicTreeStructureID:1_5:nameID").assertText("Regular User 5");
        element("formID:dynamicTreeStructureID:1_6:nameID").assertText("Regular User 6");
        element("formID:dynamicTreeStructureID:1_7:nameID").assertText("Regular User 7");
        element("formID:dynamicTreeStructureID:1_8:nameID").assertText("Regular User 8");
        element("formID:dynamicTreeStructureID:1_9:nameID").assertText("Regular User 9");
        element("formID:dynamicTreeStructureID:1_10:nameID").assertText("Regular User 10");

        window().document().getElementsByTagName("img").get(2).clickAndWait(loadingMode);
        element("formID:dynamicTreeStructureID:2_0:nameID").assertText("Administrator");
        element("formID:dynamicTreeStructureID:2_1:nameID").assertText("Ivan Ivanych");

        window().document().getElementsByTagName("img").get(3).clickAndWait(loadingMode);
        element("formID:dynamicTreeStructureID:3_0:nameID").assertText("Ivan Ivanych");

        window().document().getElementsByTagName("img").get(4).clickAndWait(loadingMode);
        element("formID:dynamicTreeStructureID:4_0:nameID").assertText("Semen Semenych");
        element("formID:dynamicTreeStructureID:4_1:nameID").assertText("Ivan Ivanych");

        window().document().getElementsByTagName("img").get(5).clickAndWait(loadingMode);
        element("formID:dynamicTreeStructureID:5_0:nameID").assertText("Ivan Ivanych");

        if (loadingMode instanceof ServerLoadingMode) {
            // reset page index for further test to run correctly
            loadingModes.tabs().get(0).clickAndWait(loadingMode);
        }
    }

    private void testSorting(LoadingMode loadingMode) {
        testAppFunctionalPage("/components/treetable/treeTableSorting.jsf");
        TabSetInspector loadingModes = tabSet("formID:loadingModes");
        if (loadingMode instanceof ServerLoadingMode) {
            loadingModes.tabs().get(1).clickAndWait(loadingMode);
        }

        TreeTableInspector treeTable1 = treeTable("formID:treeTableStaticStructureID");
        /* check simple sorting in static TreeTable */
        treeTable1.headerRow(0).cell(0).clickAndWait(loadingMode);
        treeTable1.toggleAllNodes(loadingMode);
        // verify TreeTable content after corting
        treeTable1.assertBodyRowTexts("Colors", "Cold colors", "Purple", "purple", "Warm colors", "Red", "Yellow");
        // perform sorting and content verification for expanded TreeTable
        treeTable1.headerRow(0).cell(0).clickAndWait(loadingMode);
        treeTable1.assertBodyRowTexts("Colors", "Warm colors", "Yellow", "Red", "Cold colors", "purple", "Purple");

        /* check dynamic sortable TreeTable with sortLevel = 0 */
        TreeTableInspector treeTable2 = treeTable("formID:sortLevelZero");
        treeTable2.headerRow(0).cell(0).clickAndWait(loadingMode);
        treeTable2.assertBodyRowTexts("Document Browsing", "Document Creation",
                "Document Deletion", "Document Modification", "Network Access", "User Management");
        treeTable2.toggleAllNodes(loadingMode);
        // verify TreeTable content after sorting
        treeTable2.assertBodyRowTexts("Document Browsing", "Guest", "Regular User 1", "Regular User 2", "Regular User 3", "Regular User 4",
                "Regular User 5", "Regular User 6", "Regular User 7", "Regular User 8", "Regular User 9", "Regular User 10", "Document Creation",
                "Administrator", "Ivan Ivanych", "Document Deletion", "Semen Semenych", "Ivan Ivanych", "Document Modification", "Ivan Ivanych",
                "Network Access", "Ivan Ivanych", "User Management", "Manager", "Administrator");
        // perform sorting and content verification for expanded TreeTable
        element("formID:sortLevelZero:sortLevelZero_header").clickAndWait(loadingMode);
        treeTable2.assertBodyRowTexts("User Management", "Manager", "Administrator", "Network Access", "Ivan Ivanych",
                "Document Modification", "Ivan Ivanych", "Document Deletion", "Semen Semenych", "Ivan Ivanych",
                "Document Creation", "Administrator", "Ivan Ivanych", "Document Browsing", "Guest", "Regular User 1",
                "Regular User 2", "Regular User 3", "Regular User 4", "Regular User 5", "Regular User 6", "Regular User 7",
                "Regular User 8", "Regular User 9", "Regular User 10");

        /* check dynamic sortable TreeTable with sortLevel = 1 */
        TreeTableInspector treeTable3 = treeTable("formID:sortLevelOne");
        treeTable3.headerRow(0).cell(0).clickAndWait(loadingMode);
        treeTable3.assertBodyRowTexts("User Management", "Document Browsing",
                "Document Creation", "Document Modification", "Document Deletion", "Network Access");
        treeTable3.toggleAllNodes(loadingMode);
        treeTable3.assertBodyRowTexts("User Management", "Administrator", "Manager", "Document Browsing",
                "Guest", "Regular User 1", "Regular User 10", "Regular User 2", "Regular User 3", "Regular User 4",
                "Regular User 5", "Regular User 6", "Regular User 7", "Regular User 8", "Regular User 9", "Document Creation",
                "Administrator", "Ivan Ivanych", "Document Modification", "Ivan Ivanych", "Document Deletion", "Ivan Ivanych",
                "Semen Semenych", "Network Access", "Ivan Ivanych");
        // perform sorting and content verification for expanded TreeTable
        treeTable3.headerRow(0).cell(0).clickAndWait(loadingMode);
        treeTable3.assertBodyRowTexts("User Management", "Manager", "Administrator",
                "Document Browsing", "Regular User 9", "Regular User 8", "Regular User 7", "Regular User 6", "Regular User 5",
                "Regular User 4", "Regular User 3", "Regular User 2", "Regular User 10", "Regular User 1", "Guest", "Document Creation",
                "Ivan Ivanych", "Administrator", "Document Modification", "Ivan Ivanych", "Document Deletion", "Semen Semenych",
                "Ivan Ivanych", "Network Access", "Ivan Ivanych");

        /* check TreeTable with binded ' sortingComparator' */
        TreeTableInspector treeTable4 = treeTable("formID:treeTableBindedSortingComparatorID");
        treeTable4.toggleAllNodes(loadingMode);

        treeTable4.headerRow(0).cell(0).clickAndWait(loadingMode);
        treeTable4.assertBodyRowTexts("Colors", "Middle", "Blue", "Green", "Cold colors", "Purple", "Blue Dark");

        treeTable4.headerRow(0).cell(0).clickAndWait(loadingMode);
        treeTable4.assertBodyRowTexts("Colors", "Cold colors", "Blue Dark", "Purple", "Middle", "Green", "Blue");

        /* check TreeTable with binded ' sortingComparator=caseInsensitiveText' */
        TreeTableInspector treeTable5 = treeTable("formID:treeTableSortingComparatorID");
        treeTable5.headerRow(0).cell(0).clickAndWait(loadingMode);
        treeTable5.toggleAllNodes(loadingMode);
        treeTable5.assertBodyRowTexts("Colors", "Cold colors", "purple", "Purple");

        treeTable5.headerRow(0).cell(0).clickAndWait(loadingMode);
        treeTable5.assertBodyRowTexts("Colors", "Cold colors", "purple", "Purple");

        if (loadingMode instanceof ServerLoadingMode) {
            // reset page index for further test to run correctly
            loadingModes.tabs().get(0).clickAndWait(loadingMode);
        }

    }

}