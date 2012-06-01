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

import org.openfaces.component.LoadingMode;
import org.openfaces.component.panel.FoldingDirection;
import org.openfaces.component.panel.FoldingPanel;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public class FoldingPanelTag extends AbstractPanelWithCaptionTag {
    public String getComponentType() {
        return FoldingPanel.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.FoldingPanelRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setEnumerationProperty(component, "loadingMode", LoadingMode.class);
        setBooleanProperty(component, "expanded");
        setStringProperty(component, "onstatechange");
        setEnumerationProperty(component, "foldingDirection", FoldingDirection.class);

        setBooleanProperty(component, "focusable");

        setStringProperty(component, "focusedCaptionStyle");
        setStringProperty(component, "focusedCaptionClass");
        setStringProperty(component, "focusedContentStyle");
        setStringProperty(component, "focusedContentClass");
        setBooleanProperty(component, "toggleOnCaptionClick");
        setStringProperty(component, "rolloverTogglableCaptionStyle");
        setStringProperty(component, "rolloverTogglableCaptionClass");
    }
}
