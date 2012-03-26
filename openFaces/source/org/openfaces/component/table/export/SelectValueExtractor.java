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

package org.openfaces.component.table.export;

import org.openfaces.renderkit.select.SelectUtil;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Applicable for components UISelectItems or UISelectItem
 *
 * @author Natalia.Zolochevska@Teamdev.com
 */
public class SelectValueExtractor implements ComponentDataExtractor {

    private String separator = " ";

    public SelectValueExtractor() {
    }

    public SelectValueExtractor(String separator) {
        this.separator = separator;
    }

    public boolean isApplicableFor(UIComponent component) {
        List<SelectItem> selectItems = SelectUtil.collectSelectItems(component);
        return selectItems != null && !selectItems.isEmpty();
    }

    public Object getData(UIComponent component) {
        List<SelectItem> selectItems = SelectUtil.collectSelectItems(component);

        Object value = getValue(component);
        if (value instanceof Iterable<?>) {
            List<String> labels = new ArrayList<String>(selectItems.size());
            for (Object item : ((Iterable<Object>) value)) {
                for (SelectItem selectItem : selectItems) {
                    if (selectItem.getValue().equals(item)) {
                        labels.add(selectItem.getLabel());
                    }
                }
            }
            StringBuilder labelsString = new StringBuilder();
            boolean first = true;
            for (String item : labels) {
                if (first)
                    first = false;
                else
                    labelsString.append(separator);
                labelsString.append(item);
            }
            return labelsString.toString();


        } else {
            for (SelectItem selectItem : selectItems) {
                if (selectItem.getValue().equals(value)) {
                    return selectItem.getLabel();
                }
            }
            return null;
        }

    }

    private static Object getValue(UIComponent component) {
        UIOutput uiOutput = (UIOutput) component;
        FacesContext context = FacesContext.getCurrentInstance();
        ValueExpression expression = uiOutput.getValueExpression("value");
        Object value = expression.getValue(context.getELContext());
        return value;
    }
}
