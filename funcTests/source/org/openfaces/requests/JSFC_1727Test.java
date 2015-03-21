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
import org.openfaces.test.BaseSeleniumTest;
import org.seleniuminspector.ElementInspector;

/**
 * @author Darya Shumilina
 */
public class JSFC_1727Test extends BaseSeleniumTest {

    // JSFC-1927 Non modal popup hides after submit
     @Test
     @Ignore
    public void testNonModalPopupStateSaving() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/requests/JSFC-1727.jsf");

        ElementInspector popupLayer = element("form1:p");
        popupLayer.assertVisible(false);

        selenium.click("//input[@value='Show']");
        popupLayer.assertVisible(true);

        ElementInspector submit = element("form1:submit");
        submit.clickAndWait();
        popupLayer.assertVisible(true);

        selenium.click("//input[@value='Hide']");
        popupLayer.assertVisible(false);
        submit.clickAndWait();
        popupLayer.assertVisible(false);
    }

}