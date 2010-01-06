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
package org.openfaces.component.util;

import org.openfaces.component.CompoundComponent;
import org.openfaces.component.window.Window;
import org.openfaces.renderkit.CompoundComponentRenderer;

import javax.faces.context.FacesContext;

/**
 * This component is under construction. API is subject to change. Please avoid using this component in a production
 * environment.
 *
 * @author Dmitry Pikhulya
 */
public class Debug extends Window implements CompoundComponent {
    public static final String COMPONENT_TYPE = "org.openfaces.Debug";
    public static final String COMPONENT_FAMILY = "org.openfaces.Debug";

    public Debug() {
        setRendererType("org.openfaces.DebugRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    protected String getDefaultCaptionText() {
        return "Debug";
    }

    public void createSubComponents(FacesContext context) {
        ((CompoundComponentRenderer) getRenderer(context)).createSubComponents(context, this);
    }
}
