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
package org.openfaces.requests;

import com.thoughtworks.selenium.Selenium;
import org.junit.Ignore;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.openfaces.TreeTableInspector;
import org.seleniuminspector.openfaces.OpenFacesAjaxLoadingMode;

/**
 * @author Pavel Kaplin
 */

public class JSFC_2294Test extends OpenFacesTestCase {

     @Test @Ignore

    public void testThereIsNoJSErrorOnAjaxTreeTableReloading() {
        Selenium selenium = getSelenium();
        liveDemoPage("/treetable/TreeTable_selectionAndKeyboard.jsf");
        assertTrue(selenium.getLocation().endsWith("/treetable/TreeTable_selectionAndKeyboard.jsf"));
        TreeTableInspector treeTable = treeTable("form1:requestsTreeTable");
        treeTable.bodyRow(1).click();
        treeTable.bodyRow(1).doubleClick();
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        treeTable.bodyRow(2).clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
    }
}
