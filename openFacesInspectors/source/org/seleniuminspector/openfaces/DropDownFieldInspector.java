/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.seleniuminspector.openfaces;

import org.openfaces.renderkit.input.DropDownComponentRenderer;
import org.seleniuminspector.ElementByLocatorInspector;
import org.seleniuminspector.ElementInspector;

/**
 * @author Dmitry Pikhulya
 */
public class DropDownFieldInspector extends DropDownComponentInspector {

    public DropDownFieldInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
    }

    public DropDownFieldInspector(ElementInspector element) {
        super(element);
    }

    public ElementInspector button() {
        return new ElementByLocatorInspector(id() + DropDownComponentRenderer.BUTTON_SUFFIX);
    }

}
