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

import org.openfaces.component.table.DataTablePaginator;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public class DataTablePaginatorTag extends AbstractComponentTag {
    public String getComponentType() {
        return DataTablePaginator.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.DataTablePaginatorRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setBooleanProperty(component, "showIfOnePage");
        setStringProperty(component, "pageNumberPrefix");
        setBooleanProperty(component, "showPageCount");
        setStringProperty(component, "pageCountPreposition");
        setStringProperty(component, "pageNumberFieldStyle");
        setStringProperty(component, "pageNumberFieldClass");

        setStringProperty(component, "nextImageUrl");
        setStringProperty(component, "previousImageUrl");
        setStringProperty(component, "firstImageUrl");
        setStringProperty(component, "lastImageUrl");
        setStringProperty(component, "nextDisabledImageUrl");
        setStringProperty(component, "previousDisabledImageUrl");
        setStringProperty(component, "firstDisabledImageUrl");
        setStringProperty(component, "lastDisabledImageUrl");
        setBooleanProperty(component, "showDisabledImages");

        setStringProperty(component, "nextText");
        setStringProperty(component, "previousText");
        setStringProperty(component, "firstText");
        setStringProperty(component, "lastText");
    }
}
