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
public class JSFC_1105Test extends OpenFacesTestCase {

    // JSFC-1105 Client-side validation does'n work for RegExpValidator with "[-\+]?[0-9]*\.?[0-9]*" pattern
    @Test
    public void testRegExpValidator() {
        final String messageText = "its not correct number";
        final String validateButtonLocator = "//input[@value='Validate']";
        final String validatorFieldLocator = "form1:regExpValidatorField";

        Selenium selenium = getSelenium();
        testAppFunctionalPage("/requests/JSFC_1105.jsf");
        assertFalse(isMessageTextPresent(messageText));

        selenium.click(validateButtonLocator);

        assertFalse(isMessageTextPresent(messageText));
        selenium.type(validatorFieldLocator, "asd12313");

        selenium.click(validateButtonLocator);
        assertTrue(isMessageTextPresent(messageText));
        selenium.type(validatorFieldLocator, "123");

        selenium.click(validateButtonLocator);
        assertFalse(isMessageTextPresent(messageText));
        selenium.type(validatorFieldLocator, "123.5");

        selenium.click(validateButtonLocator);
        assertFalse(isMessageTextPresent(messageText));
        selenium.type(validatorFieldLocator, "123.5asd");

        selenium.click(validateButtonLocator);
        assertTrue(isMessageTextPresent(messageText));
    }

}