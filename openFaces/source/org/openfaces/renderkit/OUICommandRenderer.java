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

package org.openfaces.renderkit;

import org.openfaces.component.OUICommand;
import org.openfaces.util.Rendering;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class OUICommandRenderer extends RendererBase {
    @Override
    public void decode(FacesContext context, UIComponent component) {
        Rendering.decodeBehaviors(context, component);
        OUICommand command = (OUICommand) component;
        Map<String, String> requestParameters = context.getExternalContext().getRequestParameterMap();
        String key = command.getActionTriggerParam();
        if (requestParameters.containsKey(key)) {
            component.queueEvent(new ActionEvent(component));
        }
    }

}
