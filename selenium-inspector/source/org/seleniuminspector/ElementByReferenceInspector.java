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
 * Inspects the same element as the associated element inspector. This class is mainly useful for extending by other
 * specialized inspectors to provide a flexible way of referring to other elements.
 * <p/>
 * See TableInspector as an example.
 *
 * @author Dmitry Pikhulya
 */
public class ElementByReferenceInspector extends ElementInspector {
    private ElementInspector referredElement;

    public ElementByReferenceInspector(ElementInspector element) {
        referredElement = element;
    }

    public String getElementReferenceExpression() {
        return referredElement.getElementReferenceExpression();
    }

    public String toString() {
        return "elementByRef[" + referredElement.toString() + "]";
    }

    @Override
    public String asSeleniumLocator() {
        return referredElement.asSeleniumLocator();
    }
}
