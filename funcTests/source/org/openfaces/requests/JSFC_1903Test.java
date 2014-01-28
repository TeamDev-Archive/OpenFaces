/*
 * OpenFaces - JSF Component Library 2.0
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

import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.openfaces.ConfirmationInspector;
import org.seleniuminspector.ElementInspector;

/**
 * @author Darya Shumilina
 */
public class JSFC_1903Test extends OpenFacesTestCase {

    // JSFC-1903 Infinite recursion in JS on invoking Confirmation by "invokerID" attribute
     @Test
    public void testConfirmationWithInvokerId() {
        testAppFunctionalPage("/requests/JSFC-1903.jsf");
        ElementInspector button = element("button1");
        button.click();

        ConfirmationInspector confirmation = confirmation("form1:conf1");
        confirmation.cancelButton().click();
        button.click();
        confirmation.okButton().click();
        assertEquals("!", window().document().getAlert());
        acceptAlert();
    }
}
