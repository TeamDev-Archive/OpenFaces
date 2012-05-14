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

import org.openfaces.renderkit.panel.SidePanelRenderer;
import org.seleniuminspector.ElementByReferenceInspector;
import org.seleniuminspector.ElementByLocatorInspector;
import org.seleniuminspector.ElementInspector;

/**
 * @author Andrii Gorbatov
 */

public class SidePanelInspector extends ElementByReferenceInspector {

    public SidePanelInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
    }

    public SidePanelInspector(ElementInspector element) {
        super(element);
    }

    public ElementInspector splitter() {
        return new ElementByLocatorInspector(id() + SidePanelRenderer.SPLITTER_SUFFIX);
    }

    public ElementInspector panel() {
        return new ElementByLocatorInspector(id() + SidePanelRenderer.PANEL_SUFFIX);
    }

    public ElementInspector caption() {
        return new ElementByLocatorInspector(id() + SidePanelRenderer.CAPTION_SUFFIX);
    }

    public ElementInspector content() {
        return new ElementByLocatorInspector(id() + SidePanelRenderer.CONTENT_SUFFIX);
    }
}
