/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.taglib.jsp.table;

import org.openfaces.taglib.internal.table.GroupingBoxTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

public class GroupingBoxJspTag extends AbstractComponentJspTag {

    public GroupingBoxJspTag() {
        super(new GroupingBoxTag());
    }


    public void setId(ValueExpression id) {
        getDelegate().setPropertyValue("id", id);
    }

    public void setStyleClass(ValueExpression styleClass) {
        getDelegate().setPropertyValue("styleClass", styleClass);
    }

    public void setStyle(ValueExpression style) {
        getDelegate().setPropertyValue("style", style);
    }

    public void setHeaderStyle(ValueExpression headerStyle) {
        getDelegate().setPropertyValue("headerStyle", headerStyle);
    }

    public void setHeaderStyleClass(ValueExpression headerStyleClass) {
        getDelegate().setPropertyValue("headerStyleClass", headerStyleClass);
    }

    public void setPromptText(ValueExpression promptText) {
        getDelegate().setPropertyValue("promptText", promptText);
    }

    public void setPromptTextStyle(ValueExpression promptTextStyle) {
        getDelegate().setPropertyValue("promptTextStyle", promptTextStyle);
    }

    public void setPromptTextStyleClass(ValueExpression promptTextStyleClass) {
        getDelegate().setPropertyValue("promptTextStyleClass", promptTextStyleClass);
    }

    public void setHeaderHorizOffset(ValueExpression headerHorizOffset) {
        getDelegate().setPropertyValue("headerHorizOffset", headerHorizOffset);
    }

    public void setHeaderVertOffset(ValueExpression headerVertOffset) {
        getDelegate().setPropertyValue("headerVertOffset", headerVertOffset);
    }

    public void setConnectorStyle(ValueExpression connectorStyle) {
        getDelegate().setPropertyValue("connectorStyle", connectorStyle);
    }
}
