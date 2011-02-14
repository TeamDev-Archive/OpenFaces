/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
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
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;

/**
 * @author Darya Shumilina
 */
public class JSFC_1746Test extends OpenFacesTestCase {

    // JSFC-1746 Exception after "Clear selection" for Data Table
    @Test
    public void testNoExceptionAfterClientClearSelection() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/tableDemo.jsf");
        selenium.click("form1:clearSelectionLink");
        selenium.click("//input[@value='Show Checked Users (via form submit) ->']");
        waitForPageToLoad();
        assertEquals("Table Demo", selenium.getTitle());
    }

}