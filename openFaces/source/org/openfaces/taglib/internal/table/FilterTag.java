/*
 * Copyright (c) 1998-2009 TeamDev Ltd. All Rights Reserved.
 * Use is subject to license terms.
 */
package org.openfaces.taglib.internal.table;

import org.openfaces.taglib.internal.AbstractComponentTag;
import org.openfaces.component.table.Filter;
import org.openfaces.component.table.EqualsFilterCriterion;
import org.openfaces.component.table.ContainsFilterCriterion;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

/**
 * @author Dmitry Pikhulya
 */
public abstract class FilterTag extends AbstractComponentTag {
    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        Filter filter = (Filter) component;
        setPropertyBinding(component, "expression");
        String criterion = getPropertyValue("criterion");
        if (!setPropertyAsBinding(component, "criterion", criterion)) {
            if (criterion.equals("equals"))
                filter.setCriterion(new EqualsFilterCriterion());
            else if (criterion.equals("contains"))
                filter.setCriterion(new ContainsFilterCriterion());
            else
                throw new FacesException("Unknown filter's criterion attribute value: \"" + criterion + "\"; it should be one of: \"equals\" or \"contains\"");

        }
        setPropertyBinding(component, "options");
        setStringProperty(component, "for", false, false);

        setStringProperty(component, "allRecordsText");
        setStringProperty(component, "emptyRecordsText");
        setStringProperty(component, "nonEmptyRecordsText");

        setStringProperty(component, "promptText");
        setStringProperty(component, "promptTextStyle");
        setStringProperty(component, "promptTextClass");
    }
}
