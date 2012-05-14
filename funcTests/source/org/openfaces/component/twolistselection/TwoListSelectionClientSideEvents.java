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
package org.openfaces.component.twolistselection;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.openfaces.TwoListSelectionInspector;

/**
 * @author Darya Shumilina
 */
public class TwoListSelectionClientSideEvents extends OpenFacesTestCase {
    @Test
    public void testTLSClientSideEvents() {

        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/twolistselection/twoListSelection.jsf");

        //onadd
        twoListSelection("formID:onaddID").addAllButton().click();
        assertTrue(selenium.isTextPresent("onadd works"));
        //todo: uncomment if JSFC-2698 and JSFC-1459 fixed
        /*assertTrue(selenium.isTextPresent("add"));*/

        //onremove
        TwoListSelectionInspector onremoveTwoListSelection = twoListSelection("formID:onremoveID");
        onremoveTwoListSelection.addAllButton().click();
        onremoveTwoListSelection.removeAllButton().click();
        assertTrue(selenium.isTextPresent("onremove works"));
        //todo: uncomment if JSFC-2698 and JSFC-1459 fixed
        /*assertTrue(selenium.isTextPresent("remove"));*/
    }

}