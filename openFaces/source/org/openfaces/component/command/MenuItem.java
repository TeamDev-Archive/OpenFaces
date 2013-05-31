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
package org.openfaces.component.command;

import org.openfaces.component.OUICommand;
import org.openfaces.component.ajax.AjaxInitializer;
import org.openfaces.component.table.AbstractTable;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.util.Components;
import org.openfaces.util.ConvertibleToJSON;
import org.openfaces.util.DataUtil;
import org.openfaces.util.ValueBindings;

import javax.el.MethodExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * @author Vladimir Kurganov
 */
public class MenuItem extends OUICommand implements ConvertibleToJSON {
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

    private String selectedStyle;
    private String selectedClass;

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

    public JSONObject toJSONObject(Map params) throws JSONException{
        JSONObject obj = new JSONObject();
        obj.put("iconUrl", getIconUrl());
        obj.put("contentAreaStyle", getContentAreaStyle());
        obj.put("contentAreaClass", getContentAreaClass());
        obj.put("disabledIconUrl", getDisabledIconUrl());
        obj.put("selectedIconUrl", getSelectedIconUrl());

        obj.put("selectedDisabledIconUrl", getSelectedDisabledIconUrl());

        obj.put("oncontextmenu", getOncontextmenu());
        obj.put("onajaxstart", getOnajaxstart());
        obj.put("onajaxend", getOnajaxend());
        obj.put("onerror", getOnerror());
        obj.put("onsuccess", getOnsuccess());

        obj.put("onclick", getOnclick());
      /*  obj.put("ondblclick", getOndblclick());
        obj.put("onmousedown", getOnmousedown());
        obj.put("onmouseover", getOnmouseover());
        obj.put("onmousemove", getOnmousemove());
        obj.put("onmouseout", getOnmouseout());
        obj.put("onmouseup", getOnmouseup());
        obj.put("onfocus", getOnfocus());
        obj.put("onblur", getOnblur());
        obj.put("onkeydown", getOnkeydown());
        obj.put("onkeyup", getOnkeyup());
        obj.put("onkeypress", getOnkeypress());


        obj.put("accessKey", getAccessKey());
        obj.put("submenuImageUrl", getSubmenuImageUrl());
        obj.put("disabledSubmenuImageUrl", getDisabledSubmenuImageUrl());
        obj.put("selectedSubmenuImageUrl", getSelectedSubmenuImageUrl());
        obj.put("selectedDisabledSubmenuImageUrl", getSelectedDisabledSubmenuImageUrl());
        obj.put("selectedStyle", getSelectedStyle());

        obj.put("submenuIconAreaStyle", getSubmenuIconAreaStyle());
        obj.put("submenuIconAreaClass", getSubmenuIconAreaClass());
        obj.put("indentAreaStyle", getIndentAreaStyle());
        obj.put("indentAreaClass", getIndentAreaClass());      */

        obj.put("value", getValue());

        obj.put("style",getStyle());
        obj.put("styleClass", getStyleClass());



        obj.put("delay", getDelay());
        obj.put("disabledStyle", getDisabledStyle());
        obj.put("disabledClass", getDisabledClass());

        obj.put("id", getClientId(FacesContext.getCurrentInstance()));

        FacesContext context = (FacesContext) params.get("context");
        Iterable<String>  render = getRender();
        Iterable<String>  execute = getExecute();
        AjaxInitializer ajaxInitializer = new AjaxInitializer();
        if (render!=null && render.iterator().hasNext() ){
            obj.put("render", ajaxInitializer.getRenderArray(context, this, render));
        }
        if (execute!=null && execute.iterator().hasNext() ){
            obj.put("execute", ajaxInitializer.getExecuteParam(context, this, getExecute()));
        }
        MethodExpression action = getActionExpression();
        obj.put("action", action!=null ? action.getExpressionString() : null);

        return obj;
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

                disabledIconUrl,
                selectedIconUrl,
                selectedDisabledIconUrl,

                selectedStyle,
                selectedClass,
                contentAreaStyle,
                contentAreaClass,
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

        disabledIconUrl = (String) values[i++];
        selectedIconUrl = (String) values[i++];
        selectedDisabledIconUrl = (String) values[i++];

        selectedStyle = (String) values[i++];
        selectedClass = (String) values[i++];
        contentAreaStyle = (String) values[i++];
        contentAreaClass = (String) values[i++];
        indentAreaStyle = (String) values[i++];
        indentAreaClass = (String) values[i++];
        submenuIconAreaStyle = (String) values[i++];
        submenuIconAreaClass = (String) values[i++];
    }


    public void setupMenuItemParams(FacesContext context){

    }

    protected AbstractTable getTable(String tagName, MenuItem menuItem) {
        UIComponent parent = menuItem.getParent();
        while (parent != null && (parent instanceof MenuItem || parent instanceof PopupMenu || Components.isImplicitPanel(parent)))
            parent = parent.getParent();
        if (!(parent instanceof AbstractTable))
            throw new FacesException(tagName + " can only be inserted into the \"columnMenu\" facet of " +
                    "the <o:dataTable> or <o:treeTable> components (either directly or as its sub-menu).");
        return (AbstractTable) parent;
    }

}
