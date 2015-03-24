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
package org.seleniuminspector.html;

import org.seleniuminspector.ElementByLocatorInspector;
import org.seleniuminspector.ElementByReferenceInspector;
import org.seleniuminspector.ElementInspector;

/**
 * @author Andrii Gorbatov
 */
public class TextAreaInspector extends ElementByReferenceInspector {

    public TextAreaInspector(ElementInspector element) {
        super(element);
        assertNodeName("textarea");
    }

    public TextAreaInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
        assertNodeName("textarea");
    }

    public String name() {
        return evalExpression("name");
    }

    public String value() {
        return evalExpression("value");
    }

    public boolean disabled() {
        return evalBooleanExpression("disabled");
    }


    public void assertValue(String value) {
        assertExpressionEquals("value", value);
    }

    public void type(String text) {
        getSelenium().type(asSeleniumLocator(), text);
    }
}
