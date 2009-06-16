/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
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
import org.seleniuminspector.ElementInspector;

/**
 * @author Darya Shumilina
 */
public class FocusTest extends OpenFacesTestCase {

    private final static String FOCUSED = "FOCUSED";

    @Test
    public void testAutoSaveFocusWithInputTextAndCommandButton() {
        testAppFunctionalPage("/components/scrollfocus/focus.jsf");

        //check default 'true' value for the 'autoSaveFocus' attribute

        ElementInspector submit = element("formID:submit");
        submit.clickAndWait();
        element("out1").assertText(FOCUSED);

        //check autoSaveFocus=false
        testAppFunctionalPage("/components/scrollfocus/focus2.jsf");
        submit.clickAndWait();
        element("out").assertText(FOCUSED);
    }

    @Test
    public void testFocusByPageLoadWithInputText() {
        testAppFunctionalPage("/components/scrollfocus/focus.jsf");
        element("out").assertText(FOCUSED);
    }

    @Test
    public void testFocusWithQKDataTable() {
        testAppFunctionalPage("/components/scrollfocus/focusWithDataTable.jsf");

        //check focus by page load
        element("out").assertText(FOCUSED);

        //check autoSaveFocus
        element("formID:byAfterSubmission").click();
        element("formID:submit").clickAndWait();
        element("out1").assertText(FOCUSED);
    }

/*
  public void testFocusWithQKDateChooser() {
    Selenium selenium = getSelenium();
    testAppPage("/functionalTesting/scrollfocus/focusWithDateChooser.jsf");

    //check focus by page load
    assertEquals("FOCUSED", selenium.getText("out"));

    //check autoSaveFocus
    selenium.click("formID:byAfterSubmission");
    selenium.click("formID:submit");
    waitForPageToLoad();
    assertEquals("FOCUSED", selenium.getText("out1"));
  }
*/

}