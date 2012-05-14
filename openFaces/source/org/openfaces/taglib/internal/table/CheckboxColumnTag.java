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

import org.openfaces.component.table.CheckboxColumn;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public class CheckboxColumnTag extends BaseColumnTag {
    public String getComponentType() {
        return CheckboxColumn.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.CheckboxColumnRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setBooleanProperty(component, "sortable");
        setCollectionProperty(component, "rowIndexes");
        setCollectionProperty(component, "rowDatas");
        setCollectionProperty(component, "nodePaths");
        setBooleanProperty(component, "disabled");
        setBooleanProperty(component, "visible");
        setStringProperty(component, "onchange");
    }
}
