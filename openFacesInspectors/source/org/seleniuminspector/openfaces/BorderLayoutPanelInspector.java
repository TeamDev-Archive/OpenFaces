/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.seleniuminspector.openfaces;

import org.openfaces.renderkit.panel.BorderLayoutPanelRenderer;
import org.seleniuminspector.ElementByReferenceInspector;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.ElementByLocatorInspector;

/**
 * @author Andrii Gorbatov
 */

public class BorderLayoutPanelInspector extends ElementByReferenceInspector {

    public BorderLayoutPanelInspector(ElementInspector element) {
        super(element);
    }

    public BorderLayoutPanelInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
    }

    public ElementInspector content() {
        return new ElementByLocatorInspector(id() + BorderLayoutPanelRenderer.CONTENT_SUFFIX);
    }
}
