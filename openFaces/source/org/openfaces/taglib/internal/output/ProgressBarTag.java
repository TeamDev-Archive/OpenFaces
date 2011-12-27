/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.taglib.internal.output;

import org.openfaces.component.output.LabelAlignment;
import org.openfaces.component.output.ProgressBar;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class ProgressBarTag extends AbstractComponentTag {

    public String getComponentType() {
        return ProgressBar.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.ProgressBarRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setNumberProperty(component, "value");

        setStringProperty(component,"processedStyle");
        setStringProperty(component,"processedClass");
        setStringProperty(component,"notProcessedStyle");
        setStringProperty(component,"notProcessedClass");
        setStringProperty(component,"labelStyle");
        setStringProperty(component,"labelClass");

        setStringProperty(component,"labelFormat");
        setEnumerationProperty(component, "labelAlignment", LabelAlignment.class);
        setStringProperty(component,"processedImg");
        setStringProperty(component,"notProcessedImg");
    }
}
