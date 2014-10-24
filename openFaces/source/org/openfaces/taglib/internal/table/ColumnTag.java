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

import org.openfaces.component.table.CaseInsensitiveTextComparator;
import org.openfaces.component.table.Column;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Comparator;

/**
 * @author Pavel Kaplin
 */
public class ColumnTag extends BaseColumnTag {
    public static final String CASE_INSENSITIVE_TEXT_COMPARATOR = "caseInsensitiveText";

    public String getComponentType() {
        return Column.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.ColumnRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext context, UIComponent component) {
        super.setComponentProperties(context, component);
        Column column = ((Column) component);
        setValueExpressionProperty(component, "value");
        setValueExpressionProperty(component, "sortingExpression");
        setValueExpressionProperty(component, "groupingExpression");
        setConverterProperty(component, "converter");
        setConverterProperty(component, "groupingValueConverter");

        String sortingComparator = getPropertyValue("sortingComparator");
        if (sortingComparator != null) {
            ValueExpression comparatorExpression;
            if (!isValueReference(sortingComparator)) {
                if (CASE_INSENSITIVE_TEXT_COMPARATOR.equals(sortingComparator))
                    comparatorExpression = new CaseInsensitiveComparatorBinding();
                else
                    throw new IllegalArgumentException("sortingComparator attribute should either be defined as binding or as \"" + CASE_INSENSITIVE_TEXT_COMPARATOR + "\", but it is defined as follows: " + sortingComparator);
            } else {
                comparatorExpression = createValueExpression(context, "sortingComparator", sortingComparator);
            }
            column.setSortingComparatorExpression(comparatorExpression);
        }

        setClassProperty(component, "type");
    }

    static class CaseInsensitiveComparatorBinding extends ValueExpression implements Serializable {
        private static final long serialVersionUID = 1;
        private Comparator comparator = new CaseInsensitiveTextComparator();

        public Object getValue(ELContext facesContext) {
            return comparator;
        }

        public void setValue(ELContext elContext, Object o) {
            throw new UnsupportedOperationException();
        }

        public boolean isReadOnly(ELContext elContext) {
            return true;
        }

        public Class getType(ELContext elContext) {
            return null;
        }

        public Class getExpectedType() {
            return boolean.class;
        }

        public String getExpressionString() {
            return "";
        }

        public boolean equals(Object o) {
            return false;
        }

        public int hashCode() {
            return 0;
        }

        public boolean isLiteralText() {
            return false;
        }
    }
}
