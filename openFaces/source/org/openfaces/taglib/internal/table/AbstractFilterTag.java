/*
 * Copyright (c) 1998-2009 TeamDev Ltd. All Rights Reserved.
 * Use is subject to license terms.
 */
package org.openfaces.taglib.internal.table;

import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AbstractFilterTag extends AbstractComponentTag {
    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setPropertyBinding(component, "expression");
        setPropertyBinding(component, "criterion");
        setPropertyBinding(component, "options");

        setStringProperty(component, "allRecordsText");
        setStringProperty(component, "emptyRecordsText");
        setStringProperty(component, "nonEmptyRecordsText");

        setStringProperty(component, "promptText");
        setStringProperty(component, "promptTextStyle");
        setStringProperty(component, "promptTextClass");
    }
}
