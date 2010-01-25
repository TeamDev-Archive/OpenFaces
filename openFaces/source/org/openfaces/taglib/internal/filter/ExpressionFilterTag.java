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
package org.openfaces.taglib.internal.filter;

import org.openfaces.component.filter.ExpressionFilter;
import org.openfaces.component.filter.FilterCondition;
import org.openfaces.component.filter.ExpressionFilterCriterion;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public abstract class ExpressionFilterTag extends FilterTag {
    @Override
    public void setComponentProperties(FacesContext context, UIComponent component) {
        super.setComponentProperties(context, component);

        ExpressionFilter filter = (ExpressionFilter) component;

        String expression = getPropertyValue("expression");
        if (expression != null) {
            if (getExpressionCreator().isValueReference("expression", expression))
                filter.setExpression(createValueExpression(context, "expression", expression));
            else
                filter.setExpression(expression);
        }

        String condition = getPropertyValue("condition");
        if (condition != null)
            setCondition(filter, condition);
        setPropertyBinding(component, "value");

        setPropertyBinding(component, "options");
        setConverterProperty(context, component, "converter");
        setBooleanProperty(component, "caseSensitive");

        setStringProperty(component, "allRecordsText");
        setStringProperty(component, "emptyRecordsText");
        setStringProperty(component, "nonEmptyRecordsText");

        setStringProperty(component, "promptText");
        setStringProperty(component, "promptTextStyle");
        setStringProperty(component, "promptTextClass");

        setStringProperty(component, "title");
        setStringProperty(component, "accesskey");
        setStringProperty(component, "tabindex");
        setIntProperty(component, "autoFilterDelay");
    }

    private void setCondition(ExpressionFilter filter, String value) {
        String[] parts = value.split(" ");
        String conditionName = parts[parts.length - 1];
        FilterCondition condition = null;
        String possibleConditionsStr = "";
        for (FilterCondition c : FilterCondition.values()) {
            if (c == FilterCondition.EMPTY || c == FilterCondition.BETWEEN)
                continue; // these are not applicable for one-property filters (which are ancesotrs of ExpressionFilter)
            if (possibleConditionsStr.length() > 0)
                possibleConditionsStr += ", ";
            String n = c.getName();
            possibleConditionsStr += n;
            if (conditionName.equals(n))
                condition = c;
        }
        if (parts.length > 2 || condition == null)
            throw new FacesException("Improper 'condition' attribute value: \"" + value + "\". It should be of \"<condition>\" or \"not <condition>\", where <condition> is one of: " + possibleConditionsStr +"; but it was: " + value);
        boolean inverse = false;
        if (parts.length == 2) {
            if (!parts[0].equals("not"))
                throw new FacesException("Improper 'condition' attribute value: \"" + value + "\". It should be of \"<condition>\" or \"not <condition>\", where <condition> is one of: " + possibleConditionsStr +"; but it was: " + value);
            inverse = true;
        }
        ExpressionFilterCriterion filterCriterion = new ExpressionFilterCriterion(condition, inverse);
        filter.setValue(filterCriterion);
        filter.getAttributes().put("condition", filterCriterion);
    }
}
