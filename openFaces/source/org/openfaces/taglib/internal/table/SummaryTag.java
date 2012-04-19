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
import org.openfaces.taglib.internal.OUIOutputTag;
import org.openfaces.util.ApplicationParams;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

public class SummaryTag extends OUIOutputTag {

    public String getComponentType() {
        return Summary.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.SummaryRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext context, UIComponent component) {
        super.setComponentProperties(context, component);

        setValueExpressionProperty(component, "by");
        setSummaryFunctionProperty(component, "function");
        setConverterProperty(context, component, "converter");
    }

    private void setSummaryFunctionProperty(UIComponent component, String propertyName) {
        String functionStr = getPropertyValue(propertyName);
        if (functionStr == null) return;

        if (setAsValueExpressionIfPossible(component, propertyName, functionStr)) return;

        List<SummaryFunction> registeredFunctions = ApplicationParams.getSummaryFunctions();
        for (SummaryFunction function : registeredFunctions) {
            String name = function.getName();
            if (functionStr.equals(name.toLowerCase())) {
                component.getAttributes().put(propertyName, function);
                return;
            }
        }
        throw new FacesException("Invalid value of the " + propertyName + " attribute. No such standard function with " +
                "the following name is available or registered in the application: \"" + functionStr + "\"");
    }
}
