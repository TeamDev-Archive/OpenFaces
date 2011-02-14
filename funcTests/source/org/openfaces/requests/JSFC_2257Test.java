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

import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.html.TextAreaInspector;

/**
 * @author Pavel Kaplin
 */
public class JSFC_2257Test extends OpenFacesTestCase {
    @Test
    public void testTextAreaIsNotAffectedByOpenFacesValidation() throws Exception {
        testAppFunctionalPage("/requests/JSFC_2257.jsf");

        assertPageContainsErrorIcon(false);
        TextAreaInspector textArea = new TextAreaInspector("fm:ta");
        textArea.type("Some text");
        ElementInspector button = element("fm:bt");
        button.clickAndWait();
        textArea.assertValue("Some text");
        textArea.type("");
        assertPageContainsErrorIcon(false);
        button.click();
        assertPageContainsErrorIcon(true);
    }
}
