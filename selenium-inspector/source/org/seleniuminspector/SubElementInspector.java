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
 * @author Dmitry Pikhulya
 */
public abstract class SubElementInspector extends ElementInspector {
    private ElementInspector baseElement;

    public SubElementInspector(ElementInspector baseElement) {
        this.baseElement = baseElement;
    }

    protected ElementInspector getBaseElement() {
        return baseElement;
    }

    public void assertElementExists() {
        if (!elementExists()) {
            baseElement.assertElementExists();
            super.assertElementExists();
        }
    }
}
