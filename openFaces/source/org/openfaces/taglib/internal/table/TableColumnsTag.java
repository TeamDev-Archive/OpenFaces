/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.taglib.internal.table;

import org.openfaces.component.table.FilterKind;
import org.openfaces.component.table.TableColumns;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class TableColumnsTag extends AbstractComponentTag {

    public String getComponentType() {
        return TableColumns.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return null;
    }

    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        TableColumns columns = (TableColumns) component;

        setPropertyBinding(component, "value");
        setStringProperty(component, "var", false, true);

        setPropertyBinding(component, "columnId");
        setBooleanProperty(component, "columnRendered");

        setBooleanProperty(component, "sortingEnabled");
        setPropertyBinding(component, "sortingExpression");
        String sortingComparator = getPropertyValue("sortingComparator");
        if (sortingComparator != null) {
            ValueExpression comparatorExpression;
            if (!isValueReference(sortingComparator)) {
                if (TableColumnTag.CASE_INSENSITIVE_TEXT_COMPARATOR.equals(sortingComparator))
                    comparatorExpression = new TableColumnTag.CaseInsensitiveComparatorBinding();
                else
                    throw new IllegalArgumentException("sortingComparator attribute should either be defined as binding or as \"" + TableColumnTag.CASE_INSENSITIVE_TEXT_COMPARATOR + "\", but it is defined as follows: " + sortingComparator);
            } else {
                comparatorExpression = createValueExpression(facesContext, "sortingComparator", sortingComparator);
            }
            columns.setValueExpression("sortingComparator", comparatorExpression);
        }

        setPropertyBinding(component, "filterExpression");
        setPropertyBinding(component, "filterValues");
        setPropertyBinding(component, "filterValue");
        setEnumerationProperty(component, "filterKind", FilterKind.class);

        setStringProperty(component, "width");
        setStringProperty(component, "align");
        setStringProperty(component, "valign");

        setBooleanProperty(component, "resizeable");
        setStringProperty(component, "minResizingWidth");

        setStringProperty(component, "headerStyle");
        setStringProperty(component, "headerClass");
        setStringProperty(component, "filterCellStyle");
        setStringProperty(component, "filterCellClass");
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

        setStringProperty(component, "filterPromptText");

        setStringProperty(component, "filterPromptTextStyle");
        setStringProperty(component, "filterPromptTextClass");
    }
}
