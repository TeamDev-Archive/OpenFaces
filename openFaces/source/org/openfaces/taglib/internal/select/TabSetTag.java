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
package org.openfaces.taglib.internal.select;

import org.openfaces.component.select.TabAlignment;
import org.openfaces.component.select.TabPlacement;
import org.openfaces.event.SelectionChangeEvent;
import org.openfaces.taglib.internal.AbstractUIInputTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public class TabSetTag extends AbstractUIInputTag {
    private static final String COMPONENT_TYPE = "org.openfaces.TabSet";
    private static final String RENDERER_TYPE = "org.openfaces.TabSetRenderer";


    public String getComponentType() {
        return COMPONENT_TYPE;
    }

    public String getRendererType() {
        return RENDERER_TYPE;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setIntProperty(component, "selectedIndex");
        setEnumerationProperty(component, "alignment", TabAlignment.class);
        setEnumerationProperty(component, "placement", TabPlacement.class);

        setIntProperty(component, "gapWidth");

        setStringProperty(component, "tabStyle");
        setStringProperty(component, "rolloverTabStyle");
        setStringProperty(component, "selectedTabStyle");
        setStringProperty(component, "focusedTabStyle");
        setStringProperty(component, "rolloverSelectedTabStyle");
        setStringProperty(component, "emptySpaceStyle");

        setStringProperty(component, "frontBorderStyle");
        setStringProperty(component, "backBorderStyle");

        setStringProperty(component, "tabClass");
        setStringProperty(component, "rolloverTabClass");
        setStringProperty(component, "selectedTabClass");
        setStringProperty(component, "focusedTabClass");
        setStringProperty(component, "rolloverSelectedTabClass");
        setStringProperty(component, "emptySpaceClass");

        setBooleanProperty(component, "focusable");
        setStringProperty(component, "focusAreaStyle");
        setStringProperty(component, "focusAreaClass");

        setMethodExpressionProperty(facesContext, component, "selectionChangeListener",
                new Class[]{SelectionChangeEvent.class}, void.class);
    }
}
