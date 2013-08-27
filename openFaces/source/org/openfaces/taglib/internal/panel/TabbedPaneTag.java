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
package org.openfaces.taglib.internal.panel;

import org.openfaces.component.panel.TabbedPane;
import org.openfaces.component.select.TabAlignment;
import org.openfaces.component.select.TabPlacement;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public class TabbedPaneTag extends MultiPageContainerTag {

    public String getComponentType() {
        return TabbedPane.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.TabbedPaneRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setIntProperty(component, "tabGapWidth");

        setStringProperty(component, "rolloverStyle");
        setStringProperty(component, "rolloverClass");

        setStringProperty(component, "rolloverContainerStyle");
        setStringProperty(component, "rolloverContainerClass");

        TabbedPane tabbedPane = (TabbedPane) component;

        setEnumerationProperty(component, "tabAlignment", TabAlignment.class);
        setEnumerationProperty(component, "tabPlacement", TabPlacement.class);
        setStringProperty(component, "tabStyle");
        setStringProperty(component, "rolloverTabStyle");
        setStringProperty(component, "selectedTabStyle");
        setStringProperty(component, "focusedTabStyle");
        setStringProperty(component, "rolloverSelectedTabStyle");
        setStringProperty(component, "tabEmptySpaceStyle");
        setStringProperty(component, "frontBorderStyle");
        setStringProperty(component, "backBorderStyle");
        setStringProperty(component, "tabClass");
        setStringProperty(component, "rolloverTabClass");
        setStringProperty(component, "selectedTabClass");
        setStringProperty(component, "focusedTabClass");
        setStringProperty(component, "rolloverSelectedTabClass");
        setStringProperty(component, "tabEmptySpaceClass");

        setBooleanProperty(component, "focusable");
        setStringProperty(component, "focusAreaClass");
        setStringProperty(component, "focusAreaStyle");
        setStringProperty(component, "disabledStyle");
        setStringProperty(component, "disabledClassStyle");

        String onselectionchange = getPropertyValue("onselectionchange");
        if (!setAsValueExpressionIfPossible(component, "onselectionchange", onselectionchange))
            tabbedPane.setOnchange(onselectionchange);
        setBooleanProperty(component, "mirrorTabSetVisible");
    }
}
