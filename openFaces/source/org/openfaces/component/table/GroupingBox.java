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
package org.openfaces.component.table;

import org.openfaces.component.OUIComponentBase;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

public class GroupingBox extends OUIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.GroupingBox";
    public static final String COMPONENT_FAMILY = "org.openfaces.GroupingBox";
    private String id;
    private String headerStyle;
    private String headerClass;
    private String promptText;
    private String promptTextStyle;
    private String promptTextClass;
    private String headerHorizOffset;
    private String headerVertOffset;
    private String connectorStyle;

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
                id,
                headerStyle, headerClass,
                promptText, promptTextStyle, promptTextClass,
                headerHorizOffset, headerVertOffset,
                connectorStyle
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        id = (String) state[i++];
        headerStyle = (String) state[i++];
        headerClass = (String) state[i++];
        promptText = (String) state[i++];
        promptTextStyle = (String) state[i++];
        promptTextClass = (String) state[i++];
        headerHorizOffset = (String) state[i++];
        headerVertOffset = (String) state[i++];
        connectorStyle = (String) state[i++];
    }

    public String getId() {
        return ValueBindings.get(this, "id", id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeaderStyle() {
        return ValueBindings.get(this, "headerStyle", headerStyle);
    }

    public void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle;
    }

    public String getHeaderClass() {
        return ValueBindings.get(this, "headerClass", headerClass);
    }

    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }

    public String getPromptText() {
        return ValueBindings.get(this, "promptText", promptText, "Drag a column header here to group by that column");
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

    public String getPromptTextClass() {
        return ValueBindings.get(this, "promptTextClass", promptTextClass);
    }

    public void setPromptTextClass(String promptTextClass) {
        this.promptTextClass = promptTextClass;
    }

    public String getHeaderHorizOffset() {
        return ValueBindings.get(this, "headerHorizOffset", headerHorizOffset, "5px");
    }

    public void setHeaderHorizOffset(String headerHorizOffset) {
        this.headerHorizOffset = headerHorizOffset;
    }

    public String getHeaderVertOffset() {
        return ValueBindings.get(this, "headerVertOffset", headerVertOffset, "50%");
    }

    public void setHeaderVertOffset(String headerVertOffset) {
        this.headerVertOffset = headerVertOffset;
    }

    public String getConnectorStyle() {
        return ValueBindings.get(this, "connectorStyle", connectorStyle, "1px solid #404040");
    }

    public void setConnectorStyle(String connectorStyle) {
        this.connectorStyle = connectorStyle;
    }
}
