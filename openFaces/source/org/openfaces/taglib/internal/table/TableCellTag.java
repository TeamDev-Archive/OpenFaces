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

import org.openfaces.component.table.TableCell;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class TableCellTag extends AbstractComponentTag {
    public String getComponentType() {
        return TableCell.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return null;
    }

    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        TableCell tableCell = (TableCell) component;

        String columnIds = getPropertyValue("columnIds");
        if (columnIds != null) {
            if (isValueReference(columnIds))
                setObjectProperty(component, "columnIds", columnIds);
            else {
                String[] ids = columnIds.split(",");
                tableCell.setColumnIds(ids);
            }
        }
        setPropertyBinding(component, "condition");

//    String span = gePropertyValue("span");
//    if ("all".equals(span))
//      tableCell.setSpan(0);
//    else
        setIntProperty(component, "span");
    }
}
