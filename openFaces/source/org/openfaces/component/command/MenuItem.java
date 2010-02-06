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

import org.openfaces.component.OUICommand;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Vladimir Kurganov
 */
public class MenuItem extends OUICommand {
    public static final String COMPONENT_TYPE = "org.openfaces.MenuItem";
    public static final String COMPONENT_FAMILY = "org.openfaces.MenuItem";

    private String iconUrl;
    private String disabledIconUrl;
    private String selectedIconUrl;
    private String selectedDisabledIconUrl;

    private String accessKey;

    private String submenuImageUrl;
    private String disabledSubmenuImageUrl;
    private String selectedSubmenuImageUrl;
    private String selectedDisabledSubmenuImageUrl;

    private Boolean disabled;

    private String selectedStyle;
    private String selectedClass;
    private String disabledStyle;
    private String disabledClass;

    private String contentAreaStyle;
    private String contentAreaClass;
    private String submenuIconAreaStyle;
    private String submenuIconAreaClass;
    private String indentAreaStyle;
    private String indentAreaClass;


    public MenuItem() {
        setRendererType("org.openfaces.MenuItemRenderer");
    }

    public MenuItem(String value) {
        this();
        setValue(value);
    }

    public MenuItem(String value, PopupMenu submenu) {
        this(value);
        getChildren().add(submenu);
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }


    public String getIconUrl() {
        return ValueBindings.get(this, "iconUrl", iconUrl);
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getAccessKey() {
        return ValueBindings.get(this, "accessKey", accessKey);
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSubmenuImageUrl() {
        return ValueBindings.get(this, "submenuImageUrl", submenuImageUrl);
    }

    public void setSubmenuImageUrl(String submenuImageUrl) {
        this.submenuImageUrl = submenuImageUrl;
    }

    public String getDisabledSubmenuImageUrl() {
        return ValueBindings.get(this, "disabledSubmenuImageUrl", disabledSubmenuImageUrl);
    }

    public void setDisabledSubmenuImageUrl(String disabledSubmenuImageUrl) {
        this.disabledSubmenuImageUrl = disabledSubmenuImageUrl;
    }

    public String getDisabledIconUrl() {
        return ValueBindings.get(this, "disabledImageUrl", disabledIconUrl);
    }

    public void setSelectedDisabledSubmenuImageUrl(String selectedDisabledSubmenuImageUrl) {
        this.selectedDisabledSubmenuImageUrl = selectedDisabledSubmenuImageUrl;
    }

    public String getSelectedDisabledSubmenuImageUrl() {
        return ValueBindings.get(this, "selectedDisabledImageUrl", selectedDisabledSubmenuImageUrl);
    }

    public void setDisabledIconUrl(String disabledIconUrl) {
        this.disabledIconUrl = disabledIconUrl;
    }

    public String getSelectedIconUrl() {
        return ValueBindings.get(this, "selectedIconUrl", selectedIconUrl);
    }

    public void setSelectedIconUrl(String selectedIconUrl) {
        this.selectedIconUrl = selectedIconUrl;
    }

    public String getSelectedDisabledIconUrl() {
        return ValueBindings.get(this, "selectedDisabledIconUrl", selectedDisabledIconUrl);
    }

    public void setSelectedDisabledIconUrl(String selectedDisabledIconUrl) {
        this.selectedDisabledIconUrl = selectedDisabledIconUrl;
    }


    public String getSelectedSubmenuImageUrl() {
        return ValueBindings.get(this, "selectedSubmenuImageUrl", selectedSubmenuImageUrl);
    }

    public void setSelectedSubmenuImageUrl(String selectedSubmenuImageUrl) {
        this.selectedSubmenuImageUrl = selectedSubmenuImageUrl;
    }

    public boolean isDisabled() {
        return ValueBindings.get(this, "disabled", disabled, false);
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getDisabledClass() {
        return ValueBindings.get(this, "disabledClass", disabledClass);
    }

    public void setDisabledClass(String disabledClass) {
        this.disabledClass = disabledClass;
    }

    public String getDisabledStyle() {
        return ValueBindings.get(this, "disabledStyle", disabledStyle);
    }

    public void setDisabledStyle(String disabledStyle) {
        this.disabledStyle = disabledStyle;
    }

    public String getIndentAreaStyle() {
        return ValueBindings.get(this, "indentAreaStyle", indentAreaStyle);
    }

    public void setIndentAreaStyle(String indentAreaStyle) {
        this.indentAreaStyle = indentAreaStyle;
    }

    public String getIndentAreaClass() {
        return ValueBindings.get(this, "indentAreaClass", indentAreaClass);
    }

    public void setIndentAreaClass(String indentAreaClass) {
        this.indentAreaClass = indentAreaClass;
    }

    public String getSubmenuIconAreaStyle() {
        return ValueBindings.get(this, "submenuIconAreaStyle", submenuIconAreaStyle);
    }

    public void setSubmenuIconAreaStyle(String submenuIconAreaStyle) {
        this.submenuIconAreaStyle = submenuIconAreaStyle;
    }

    public String getSubmenuIconAreaClass() {
        return ValueBindings.get(this, "submenuIconAreaClass", submenuIconAreaClass);
    }

    public void setSubmenuIconAreaClass(String submenuIconAreaClass) {
        this.submenuIconAreaClass = submenuIconAreaClass;
    }

    public String getSelectedStyle() {
        return ValueBindings.get(this, "selectedStyle", selectedStyle);
    }

    public void setSelectedStyle(String selectedStyle) {
        this.selectedStyle = selectedStyle;
    }

    public String getSelectedClass() {
        return ValueBindings.get(this, "selectedClass", selectedClass);
    }

    public void setSelectedClass(String selectedClass) {
        this.selectedClass = selectedClass;
    }

    public String getContentAreaClass() {
        return ValueBindings.get(this, "contentAreaClass", contentAreaClass);
    }

    public void setContentAreaClass(String contentAreaClass) {
        this.contentAreaClass = contentAreaClass;
    }

    public String getContentAreaStyle() {
        return ValueBindings.get(this, "contentAreaStyle", contentAreaStyle);
    }

    public void setContentAreaStyle(String contentStyle) {
        contentAreaStyle = contentStyle;
    }

    @Override
    public Object saveState(FacesContext facesContext) {
        return new Object[]{
                super.saveState(facesContext),
                iconUrl,
                accessKey,

                submenuImageUrl,
                disabledSubmenuImageUrl,
                selectedSubmenuImageUrl,
                selectedDisabledSubmenuImageUrl,

                disabled,
                disabledIconUrl,
                selectedIconUrl,
                selectedDisabledIconUrl,

                selectedStyle,
                selectedClass,
                contentAreaStyle,
                contentAreaClass,
                disabledStyle,
                disabledClass,
                indentAreaStyle,
                indentAreaClass,
                submenuIconAreaStyle,
                submenuIconAreaClass,
        };
    }

    @Override
    public void restoreState(FacesContext facesContext, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(facesContext, values[i++]);
        iconUrl = (String) values[i++];
        accessKey = (String) values[i++];

        submenuImageUrl = (String) values[i++];
        disabledSubmenuImageUrl = (String) values[i++];
        selectedSubmenuImageUrl = (String) values[i++];
        selectedDisabledSubmenuImageUrl = (String) values[i++];

        disabled = (Boolean) values[i++];
        disabledIconUrl = (String) values[i++];
        selectedIconUrl = (String) values[i++];
        selectedDisabledIconUrl = (String) values[i++];

        selectedStyle = (String) values[i++];
        selectedClass = (String) values[i++];
        contentAreaStyle = (String) values[i++];
        contentAreaClass = (String) values[i++];
        disabledStyle = (String) values[i++];
        disabledClass = (String) values[i++];
        indentAreaStyle = (String) values[i++];
        indentAreaClass = (String) values[i++];
        submenuIconAreaStyle = (String) values[i++];
        submenuIconAreaClass = (String) values[i++];
    }

}
