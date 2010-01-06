/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.taglib.internal.table;

import org.openfaces.component.table.ColumnReordering;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class ColumnReorderingTag extends AbstractComponentTag {

    public String getComponentType() {
        return ColumnReordering.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.ColumnReorderingRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setStringProperty(component, "draggedCellStyle");
        setStringProperty(component, "draggedCellClass");
        setDoubleProperty(component, "draggedCellTransparency");

        setStringProperty(component, "autoScrollAreaStyle");
        setStringProperty(component, "autoScrollAreaClass");
        setDoubleProperty(component, "autoScrollAreaTransparency");
        setStringProperty(component, "autoScrollLeftImageUrl");
        setStringProperty(component, "autoScrollRightImageUrl");

        setStringProperty(component, "dropTargetStyle");
        setStringProperty(component, "dropTargetClass");
        setStringProperty(component, "dropTargetTopImageUrl");
        setStringProperty(component, "dropTargetBottomImageUrl");
    }
}
