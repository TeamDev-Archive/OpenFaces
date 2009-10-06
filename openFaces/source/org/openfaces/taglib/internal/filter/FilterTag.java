/*
 * Copyright (c) 1998-2009 TeamDev Ltd. All Rights Reserved.
 * Use is subject to license terms.
 */
package org.openfaces.taglib.internal.filter;

import org.openfaces.component.filter.Filter;
import org.openfaces.component.filter.OperationType;
import org.openfaces.component.filter.criterion.PropertyFilterCriterion;
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
        String criterion = getPropertyValue("value");
        if (!setPropertyAsBinding(component, "value", criterion)) {
            if (criterion.equals("equals"))
                filter.setValue(new PropertyFilterCriterion(null, OperationType.EQUALS, null));
            else if (criterion.equals("contains"))
                filter.setValue(new PropertyFilterCriterion(null, OperationType.CONTAINS, null));
            else
                throw new FacesException("Unknown filter's criterion attribute value: \"" + criterion + "\"; it should be one of: \"equals\" or \"contains\"");

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
}
