/*
 * OpenFaces - JSF Component Library 3.0
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

import org.openfaces.renderkit.window.PopupLayerRenderer;
import org.seleniuminspector.ElementByReferenceInspector;
import org.seleniuminspector.ElementByLocatorInspector;
import org.seleniuminspector.ElementInspector;

/**
 * @author Dmitry Pikhulya
 */
public class PopupLayerInspector extends ElementByReferenceInspector {
    public PopupLayerInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
    }

    public PopupLayerInspector(ElementInspector element) {
        super(element);
    }

    public ElementInspector modalLayer() {
        return new ElementByLocatorInspector(id() + PopupLayerRenderer.BLOCKING_LAYER_SUFFIX);
    }
}
