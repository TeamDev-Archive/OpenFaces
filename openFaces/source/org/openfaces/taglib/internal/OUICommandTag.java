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
package org.openfaces.taglib.internal;

import org.openfaces.component.OUICommand;
import org.openfaces.event.UnifiedActionListener;

import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionListener;

/**
 * @author Dmitry Pikhulya
 */
public abstract class OUICommandTag extends AbstractComponentTag {
    @Override
    public void setComponentProperties(FacesContext context, UIComponent component) {
        super.setComponentProperties(context, component);

        OUICommand quiCommand = (OUICommand) component;
        setActionProperty(context, quiCommand);
        setActionListener(context, quiCommand);
        setBooleanProperty(component, "immediate");
        setBooleanProperty(component, "disabled");
        setStringProperty(component, "disabledStyle");
        setStringProperty(component, "disabledClass");

        boolean ajax = false;
        ajax |= setLiteralCollectionProperty(component, "render");
        ajax |= setLiteralCollectionProperty(component, "execute");
        setBooleanProperty(component, "executeRenderedComponents");

        if (ajax) {
            // todo: actionListener is set as value expression for Ajax-mode functions to be able to pass action
            // todo: expression to the client. Replace with ordinary server-side action decoding during Ajax action execution.
            setValueExpressionProperty(component, "actionListener");
        }

        setStringProperty(component, "onajaxstart");
        setStringProperty(component, "onajaxend");
        setStringProperty(component, "onerror");
        setStringProperty(component, "onsuccess");
        setIntProperty(component, "delay");
    }

    protected ActionListener createUnifiedActionListener(FacesContext context) {
        String propertyValue = getPropertyValue("actionListener");
        if (propertyValue != null) {
            return new UnifiedActionListener(getExpressionCreator(), context, propertyValue);
        }
        return null;
    }

    @Override
    protected void setActionListener(FacesContext context, ActionSource component) {
        ActionListener actionListener = createUnifiedActionListener(context);
        if (actionListener != null)
            component.addActionListener(actionListener);
    }
}
