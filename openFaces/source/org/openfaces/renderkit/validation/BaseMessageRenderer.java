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
package org.openfaces.renderkit.validation;

import org.openfaces.util.Rendering;

import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

/**
 * @author Vladimir Korenev
 */
public class BaseMessageRenderer extends Renderer {
    public static final String DEFAULT_PRESENTATION = "_of_defaultPresentation";

    private String getFor(UIComponent component) {
        if (component instanceof UIMessage) {
            return ((UIMessage) component).getFor();
        } else {
            return (String) component.getAttributes().get("for");
        }
    }

    protected UIComponent getForComponent(UIComponent messageComponent) {
        String forAttr = getFor(messageComponent);
        if (forAttr == null) {
            return null;
        }

        return messageComponent.findComponent(forAttr);
    }

    protected boolean isDefaultPresentation(UIComponent component) {
        Boolean defaultComponent = (Boolean) component.getAttributes().get(DEFAULT_PRESENTATION);
        return defaultComponent != null && defaultComponent;
    }

    protected String getForComponentClientId(FacesContext context, UIComponent uiComponent) {
        String forAttr = getFor(uiComponent);
        if (forAttr == null) {
            Rendering.logWarning(context, "'for' attribute is not specified for component " + uiComponent.getClientId(context));
            return null;
        }
        UIComponent forComponent = uiComponent.findComponent(forAttr);
        if (forComponent != null) {
            return forComponent.getClientId(context);
        }
        char separatorChar = UINamingContainer.getSeparatorChar(context);
        if (forAttr.length() > 0 && forAttr.charAt(0) == separatorChar) {
            //absolute id path
            return forAttr.substring(1);
        } else {
            //relative id path, we assume a component on the same level as the label component
            String labelClientId = uiComponent.getClientId(context);
            int colon = labelClientId.lastIndexOf(separatorChar);
            if (colon == -1) {
                return forAttr;
            } else {
                return labelClientId.substring(0, colon + 1) + forAttr;
            }
        }
    }
}
