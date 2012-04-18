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
package org.openfaces.taglib.internal.table;

import org.openfaces.component.table.Summary;
import org.openfaces.component.table.SummaryFunction;
import org.openfaces.taglib.internal.AbstractComponentTag;
import org.openfaces.util.ApplicationParams;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

public class SummaryTag extends AbstractComponentTag {

    public String getComponentType() {
        return Summary.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.SummaryRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setValueExpressionProperty(component, "by");
        setSummaryFunctionProperty(component, "function");
    }

    private void setSummaryFunctionProperty(UIComponent component, String propertyName) {
        String function = getPropertyValue(propertyName);
        if (function == null) return;

        if (setAsValueExpressionIfPossible(component, propertyName, function)) return;

        List<SummaryFunction> summaryFunctions = ApplicationParams.getSummaryFunctions();
        for (SummaryFunction summaryFunction : summaryFunctions) {
            String name = summaryFunction.getName();
            if (name.toLowerCase().equals(propertyName)) {
                component.getAttributes().put(propertyName, summaryFunction);
            }
        }
    }
}
