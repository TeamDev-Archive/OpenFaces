/*
 * OpenFaces - JSF Component Library 3.0
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
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

/**
 * This component is under construction. API is subject to change. Please avoid using this component in a production
 * environment.
 *
 * @author Dmitry Pikhulya
 */
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
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

    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        super.processEvent(event);
        if (event instanceof PostAddToViewEvent) {
            createSubComponents(getFacesContext());
        }
    }

    public void createSubComponents(FacesContext context) {
        ((CompoundComponentRenderer) getRenderer(context)).createSubComponents(context, this);
    }
}
