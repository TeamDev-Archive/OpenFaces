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
package org.seleniuminspector;

/**
 * Inspects an element by the specified Selenium locator.
 *
 * @author Dmitry Pikhulya
 */
public class ElementByLocatorInspector extends ElementInspector {
    private String locator;

    public ElementByLocatorInspector(String locator) {
        this.locator = locator;
    }

    public String getLocator() {
        return locator;
    }

    public String getElementReferenceExpression() {
        // Here is universal selenium locator (xpath, id, etc.)
        return  "document.SeI.findElement(" + escapeStringForJSAndQuote(locator) + ")";
    }
    @Override
    public String asSeleniumLocator() {
        return locator;
    }
    public String toString() {
        return "elementByLocator[" + locator + "]";
    }
}
