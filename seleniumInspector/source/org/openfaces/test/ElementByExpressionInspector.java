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
package org.openfaces.test;

/**
 * Defines an inspector for an element retrieved with an associated script. Note that like for Selenium.getEval method,
 * the script runs in context of "selenium" object. Use "window" to refer to the tested window.
 *
 * @author Dmitry Pikhulya
 */
public class ElementByExpressionInspector extends ElementInspector {
    private String elementRetrievalExpression;

    public ElementByExpressionInspector(String elementRetrievalExpression) {
        this.elementRetrievalExpression = elementRetrievalExpression;
    }

    public String getElementReferenceExpression() {
        return elementRetrievalExpression;
    }

    public String toString() {
        return "elementByExpression[" + elementRetrievalExpression + "]";
    }

}