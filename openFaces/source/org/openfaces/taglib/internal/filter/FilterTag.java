/*
 * Copyright (c) 1998-2009 TeamDev Ltd. All Rights Reserved.
 * Use is subject to license terms.
 */
package org.openfaces.taglib.internal.filter;

import org.openfaces.component.filter.Filter;
import org.openfaces.component.filter.FilterCondition;
import org.openfaces.component.filter.PropertyFilterCriterion;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public abstract class FilterTag extends AbstractComponentTag {
    @Override
    public void setComponentProperties(FacesContext context, UIComponent component) {
        super.setComponentProperties(context, component);

        Filter filter = (Filter) component;

        String expression = getPropertyValue("expression");
        if (getExpressionCreator().isValueReference("expression", expression))
            filter.setExpression(createValueExpression(context, "expression", expression));
        else
            filter.setExpression(expression);

        String value = getPropertyValue("value");
        if (!setPropertyAsBinding(component, "value", value)) {
            setValueAsCondition(filter, value);
        }

        setPropertyBinding(component, "options");
        setStringProperty(component, "for", false, false);
        setConverterProperty(context, component, "converter");

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

    private void setValueAsCondition(Filter filter, String value) {
        String[] parts = value.split(" ");
        String conditionName = parts[parts.length - 1];
        FilterCondition condition = null;
        String possibleConditionsStr = "";
        for (FilterCondition c : FilterCondition.values()) {
            if (c == FilterCondition.EMPTY || c == FilterCondition.BETWEEN)
                continue; // these are not applicable for one-property filters (which are ancesotrs of Filter)
            if (possibleConditionsStr.length() > 0)
                possibleConditionsStr += ", ";
            String n = c.getName();
            possibleConditionsStr += n;
            if (conditionName.equals(n))
                condition = c;
        }
        if (parts.length > 2 || condition == null)
            throw new FacesException("Improper 'criterion' attribute value: \"" + value + "\". It should be of \"<condition>\" or \"not <condition>\", where <condition> is one of: " + possibleConditionsStr +"; but it was: " + value);
        boolean inverse = false;
        if (parts.length == 2) {
            if (!parts[0].equals("not"))
                throw new FacesException("Improper 'criterion' attribute value: \"" + value + "\". It should be of \"<condition>\" or \"not <condition>\", where <condition> is one of: " + possibleConditionsStr +"; but it was: " + value);
            inverse = true;
        }
        filter.setValue(new PropertyFilterCriterion(null, condition, null, inverse));
    }
}
