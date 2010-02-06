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
package org.openfaces.component.command;

import org.openfaces.util.ValueBindings;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

/**
 * @author Vladimir Kurganov
 */
public class MenuSeparator extends UIOutput {

    public static final String COMPONENT_TYPE = "org.openfaces.MenuSeparator";
    public static final String COMPONENT_FAMILY = "org.openfaces.MenuSeparator";

    private String style;
    private String styleClass;
    private String indentStyle;
    private String indentClass;

    public MenuSeparator() {
        setRendererType("org.openfaces.MenuSeparatorRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getStyle() {
        return ValueBindings.get(this, "style", style);
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return ValueBindings.get(this, "styleClass", styleClass);
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }


    public String getIndentStyle() {
        return ValueBindings.get(this, "indentStyle", indentStyle);
    }

    public void setIndentStyle(String indentStyle) {
        this.indentStyle = indentStyle;
    }

    public String getIndentClass() {
        return ValueBindings.get(this, "indentClass", indentClass);
    }

    public void setIndentClass(String indentClass) {
        this.indentClass = indentClass;
    }


    @Override
    public Object saveState(FacesContext facesContext) {
        return new Object[]{
                super.saveState(facesContext),
                style, styleClass, indentStyle, indentClass
        };
    }

    @Override
    public void restoreState(FacesContext facesContext, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(facesContext, values[i++]);
        style = (String) values[i++];
        styleClass = (String) values[i];
        indentStyle = (String) values[i];
        indentClass = (String) values[i];
    }

}