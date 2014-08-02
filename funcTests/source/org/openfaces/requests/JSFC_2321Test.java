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
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;

/**
 * @author Pavel Kaplin
 */
public class JSFC_2321Test extends OpenFacesTestCase {
     @Test
    public void testGlobalMessagesAreShown() throws Exception {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/requests/JSFC-2321.jsf");
        element("fm:btn").clickAndWait();
        assertTrue(selenium.isTextPresent("Some message"));
    }
}
