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
 * @author Dmitry Pikhulya
 */
public class SubElementByPathInspector extends SubElementInspector {
    private String subElementPath;

    public SubElementByPathInspector(ElementInspector baseElement, String subElementPath) {
        super(baseElement);
        this.subElementPath = subElementPath;
    }

    public String getElementReferenceExpression() {
        return "this.getQ__findElementByPath(" + getBaseElement().getElementReferenceExpression() +
                ", '" + subElementPath + "', true)";
    }

    public String toString() {
        return getBaseElement().toString() + "/" + subElementPath;
    }
}