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
package org.openfaces.taglib.internal.table;

import org.openfaces.component.table.Columns;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class ColumnsTag extends AbstractComponentTag {

    public String getComponentType() {
        return Columns.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return null;
    }

    @Override
    public void setComponentProperties(FacesContext context, UIComponent component) {
        super.setComponentProperties(context, component);

        Columns columns = (Columns) component;

        setValueExpressionProperty(component, "value");
        setStringProperty(component, "var", false, true);
        setStringProperty(component, "indexVar");
        setValueExpressionProperty(component, "headerValue", getPropertyValue("header"), "header");
        setValueExpressionProperty(component, "footerValue", getPropertyValue("footer"), "footer");
        setValueExpressionProperty(component, "columnValue");
        setConverterProperty(component, "converter");

        setValueExpressionProperty(component, "columnId");
        setBooleanProperty(component, "columnRendered");

        setBooleanProperty(component, "sortingEnabled");
        setValueExpressionProperty(component, "sortingExpression");
        setValueExpressionProperty(component, "groupingExpression");
        String sortingComparator = getPropertyValue("sortingComparator");
        if (sortingComparator != null) {
            ValueExpression comparatorExpression;
            if (!isValueReference(sortingComparator)) {
                if (ColumnTag.CASE_INSENSITIVE_TEXT_COMPARATOR.equals(sortingComparator))
                    comparatorExpression = new ColumnTag.CaseInsensitiveComparatorBinding();
                else
                    throw new IllegalArgumentException("sortingComparator attribute should either be defined as binding or as \"" + ColumnTag.CASE_INSENSITIVE_TEXT_COMPARATOR + "\", but it is defined as follows: " + sortingComparator);
            } else {
                comparatorExpression = createValueExpression(context, "sortingComparator", sortingComparator);
            }
            columns.setValueExpression("sortingComparator", comparatorExpression);
        }

        setStringProperty(component, "width");
        setStringProperty(component, "align");
        setStringProperty(component, "valign");

        setBooleanProperty(component, "resizable");
        setStringProperty(component, "minResizingWidth");
        setBooleanProperty(component, "fixed");
        setBooleanProperty(component, "menuAllowed");

        setStringProperty(component, "headerStyle");
        setStringProperty(component, "headerClass");
        setStringProperty(component, "subHeaderStyle");
        setStringProperty(component, "subHeaderClass");
        setStringProperty(component, "footerStyle");
        setStringProperty(component, "footerClass");
        setStringProperty(component, "bodyStyle");
        setStringProperty(component, "bodyClass");

        setStringProperty(component, "headerOnclick");
        setStringProperty(component, "headerOndblclick");
        setStringProperty(component, "headerOnmousedown");
        setStringProperty(component, "headerOnmouseover");
        setStringProperty(component, "headerOnmousemove");
        setStringProperty(component, "headerOnmouseout");
        setStringProperty(component, "headerOnmouseup");

        setStringProperty(component, "bodyOnclick");
        setStringProperty(component, "bodyOndblclick");
        setStringProperty(component, "bodyOnmousedown");
        setStringProperty(component, "bodyOnmouseover");
        setStringProperty(component, "bodyOnmousemove");
        setStringProperty(component, "bodyOnmouseout");
        setStringProperty(component, "bodyOnmouseup");

        setStringProperty(component, "footerOnclick");
        setStringProperty(component, "footerOndblclick");
        setStringProperty(component, "footerOnmousedown");
        setStringProperty(component, "footerOnmouseover");
        setStringProperty(component, "footerOnmousemove");
        setStringProperty(component, "footerOnmouseout");
        setStringProperty(component, "footerOnmouseup");
        setClassProperty(component, "columnType");
    }
}
