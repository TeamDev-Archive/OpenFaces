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
package org.openfaces.taglib.internal.output;

import org.openfaces.component.output.GraphicText;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Darya Shumilina
 */
public class GraphicTextTag extends AbstractComponentTag {
    private static final String LEFT_TO_RIGHT = "leftToRight";
    private static final String RIGHT_TO_LEFT = "rightToLeft";
    private static final String TOP_TO_BOTTOM = "topToBottom";
    private static final String BOTTOM_TO_TOP = "bottomToTop";

    public static final String RENDERER_TYPE = "org.openfaces.GraphicTextRenderer";

    public String getComponentType() {
        return GraphicText.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return RENDERER_TYPE;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setStringProperty(component, "textStyle");
        setStringProperty(component, "title");
        setStringProperty(component, "lang");
        setObjectProperty(component, "value");
        String direction = getPropertyValue("direction");

        if (direction != null) {
            if (!setPropertyAsBinding(component, "direction", direction)) {
                int directionInteger;
                if (LEFT_TO_RIGHT.equals(direction)) {
                    directionInteger = GraphicText.LEFT_TO_RIGHT;
                } else if (RIGHT_TO_LEFT.equals(direction)) {
                    directionInteger = GraphicText.RIGHT_TO_LEFT;
                } else if (TOP_TO_BOTTOM.equals(direction)) {
                    directionInteger = GraphicText.TOP_TO_BOTTOM;
                } else if (BOTTOM_TO_TOP.equals(direction)) {
                    directionInteger = GraphicText.BOTTOM_TO_TOP;
                } else {
                    directionInteger = Integer.parseInt(direction);
                }
                setIntProperty(component, "direction", String.valueOf(directionInteger));
            }
        }

        setConverterProperty(facesContext, component, "converter");
    }

}