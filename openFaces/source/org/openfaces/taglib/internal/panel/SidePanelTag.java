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

import org.openfaces.component.panel.SidePanel;
import org.openfaces.component.panel.SidePanelAlignment;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Alexey Tarasyuk
 */
public class SidePanelTag extends AbstractPanelWithCaptionTag {
    public String getComponentType() {
        return SidePanel.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.SidePanelRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setStringProperty(component, "size");
        setStringProperty(component, "minSize");
        setStringProperty(component, "maxSize");
        setEnumerationProperty(component, "alignment", SidePanelAlignment.class);
        setStringProperty(component, "splitterStyle");
        setStringProperty(component, "splitterRolloverStyle");
        setStringProperty(component, "splitterClass");
        setStringProperty(component, "splitterRolloverClass");
        setBooleanProperty(component, "resizable");
        setBooleanProperty(component, "collapsible");
        setBooleanProperty(component, "collapsed");
        setStringProperty(component, "onsplitterdrag");
        setStringProperty(component, "oncollapse");
        setStringProperty(component, "onrestore");
        setStringProperty(component, "onmaximize");
    }
}