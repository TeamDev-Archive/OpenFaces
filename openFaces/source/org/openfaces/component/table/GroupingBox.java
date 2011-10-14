/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.table;

import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

public class GroupingBox extends org.openfaces.component.OUIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.GroupingBox";
    public static final String COMPONENT_FAMILY = "org.openfaces.GroupingBox";
    private String id;
    private String styleClass;
    private String style;
    private String headerStyle;
    private String headerStyleClass;
    private String promptText;
    private String promptTextStyle;
    private String promptTextStyleClass;

    public GroupingBox() {
        setRendererType("org.openfaces.GroupingBoxRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
            super.saveState(context),

        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);

    }

    public String getId() {
        return ValueBindings.get(this, "id", id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStyleClass() {
        return ValueBindings.get(this, "styleClass", styleClass);
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getStyle() {
        return ValueBindings.get(this, "style", style);
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getHeaderStyle() {
        return ValueBindings.get(this, "headerStyle", headerStyle);
    }

    public void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle;
    }

    public String getHeaderStyleClass() {
        return ValueBindings.get(this, "headerStyleClass", headerStyleClass);
    }

    public void setHeaderStyleClass(String headerStyleClass) {
        this.headerStyleClass = headerStyleClass;
    }

    public String getPromptText() {
        return ValueBindings.get(this, "promptText", promptText, "Drag a column header here.");
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    public String getPromptTextStyle() {
        return ValueBindings.get(this, "promptTextStyle", promptTextStyle);
    }

    public void setPromptTextStyle(String promptTextStyle) {
        this.promptTextStyle = promptTextStyle;
    }

    public String getPromptTextStyleClass() {
        return ValueBindings.get(this, "promptTextStyleClass", promptTextStyleClass);
    }

    public void setPromptTextStyleClass(String promptTextStyleClass) {
        this.promptTextStyleClass = promptTextStyleClass;
    }
}
