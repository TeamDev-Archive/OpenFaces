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

package org.openfaces.component.select;

import org.openfaces.component.OUIInputBase;
import org.openfaces.util.NullTypeELResolver;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Oleg Marshalenko
 */
public abstract class OUISelectManyInputBase extends OUIInputBase {
    public static final String LAYOUT_PAGE_DIRECTION = "pageDirection";
    public static final String LAYOUT_LINE_DIRECTION = "lineDirection";

    private String accesskey;
    private String tabindex;
    private String title;

    private String dir;
    private String lang;
    private String onselect;

    private Boolean readonly;
    private String border;
    private String layout;

    private String selectedImageUrl;
    private String unselectedImageUrl;

    private String rolloverSelectedImageUrl;
    private String rolloverUnselectedImageUrl;

    private String pressedSelectedImageUrl;
    private String pressedUnselectedImageUrl;

    private String disabledSelectedImageUrl;
    private String disabledUnselectedImageUrl;

    private String enabledStyle;
    private String enabledClass;
    private String disabledStyle;
    private String disabledClass;

    private String focusedItemStyle;
    private String focusedItemClass;
    private String selectedItemStyle;
    private String selectedItemClass;
    private String rolloverItemStyle;
    private String rolloverItemClass;
    private String pressedItemStyle;
    private String pressedItemClass;

    public String getAccesskey() {
        return ValueBindings.get(this, "accesskey", accesskey);
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public String getTabindex() {
        return ValueBindings.get(this, "tabindex", tabindex);
    }

    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }

    public String getTitle() {
        return ValueBindings.get(this, "title", title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDir() {
        return ValueBindings.get(this, "dir", dir);
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getLang() {
        return ValueBindings.get(this, "lang", lang);
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getOnselect() {
        return ValueBindings.get(this, "onselect", onselect);
    }

    public void setOnselect(String onselect) {
        this.onselect = onselect;
    }

    public boolean isReadonly() {
        return ValueBindings.get(this, "readonly", readonly, false);
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public String getBorder() {
        return ValueBindings.get(this, "border", border);
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getLayout() {
        return ValueBindings.get(this, "layout", layout, LAYOUT_LINE_DIRECTION);
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getSelectedImageUrl() {
        return ValueBindings.get(this, "selectedImageUrl", selectedImageUrl);
    }

    public void setSelectedImageUrl(String selectedImageUrl) {
        this.selectedImageUrl = selectedImageUrl;
    }

    public String getUnselectedImageUrl() {
        return ValueBindings.get(this, "unselectedImageUrl", unselectedImageUrl);
    }

    public void setUnselectedImageUrl(String unselectedImageUrl) {
        this.unselectedImageUrl = unselectedImageUrl;
    }

    public String getRolloverSelectedImageUrl() {
        return ValueBindings.get(this, "rolloverSelectedImageUrl", rolloverSelectedImageUrl);
    }

    public void setRolloverSelectedImageUrl(String rolloverSelectedImageUrl) {
        this.rolloverSelectedImageUrl = rolloverSelectedImageUrl;
    }

    public String getRolloverUnselectedImageUrl() {
        return ValueBindings.get(this, "rolloverUnselectedImageUrl", rolloverUnselectedImageUrl);
    }

    public void setRolloverUnselectedImageUrl(String rolloverUnselectedImageUrl) {
        this.rolloverUnselectedImageUrl = rolloverUnselectedImageUrl;
    }

    public String getPressedSelectedImageUrl() {
        return ValueBindings.get(this, "pressedSelectedImageUrl", pressedSelectedImageUrl);
    }

    public void setPressedSelectedImageUrl(String pressedSelectedImageUrl) {
        this.pressedSelectedImageUrl = pressedSelectedImageUrl;
    }

    public String getPressedUnselectedImageUrl() {
        return ValueBindings.get(this, "pressedUnselectedImageUrl", pressedUnselectedImageUrl);
    }

    public void setPressedUnselectedImageUrl(String pressedUnselectedImageUrl) {
        this.pressedUnselectedImageUrl = pressedUnselectedImageUrl;
    }

    public String getDisabledSelectedImageUrl() {
        return ValueBindings.get(this, "disabledSelectedImageUrl", disabledSelectedImageUrl);
    }

    public void setDisabledSelectedImageUrl(String disabledSelectedImageUrl) {
        this.disabledSelectedImageUrl = disabledSelectedImageUrl;
    }

    public String getDisabledUnselectedImageUrl() {
        return ValueBindings.get(this, "disabledUnselectedImageUrl", disabledUnselectedImageUrl);
    }

    public void setDisabledUnselectedImageUrl(String disabledUnselectedImageUrl) {
        this.disabledUnselectedImageUrl = disabledUnselectedImageUrl;
    }

    public String getEnabledStyle() {
        return ValueBindings.get(this, "enabledStyle", enabledStyle);
    }

    public void setEnabledStyle(String enabledStyle) {
        this.enabledStyle = enabledStyle;
    }

    public String getEnabledClass() {
        return ValueBindings.get(this, "enabledClass", enabledClass);
    }

    public void setEnabledClass(String enabledClass) {
        this.enabledClass = enabledClass;
    }

    public String getDisabledStyle() {
        return ValueBindings.get(this, "disabledStyle", disabledStyle);
    }

    public void setDisabledStyle(String disabledStyle) {
        this.disabledStyle = disabledStyle;
    }

    public String getDisabledClass() {
        return ValueBindings.get(this, "disabledClass", disabledClass);
    }

    public void setDisabledClass(String disabledClass) {
        this.disabledClass = disabledClass;
    }

    public String getFocusedItemStyle() {
        return ValueBindings.get(this, "focusedItemStyle", focusedItemStyle);
    }

    public void setFocusedItemStyle(String focusedItemStyle) {
        this.focusedItemStyle = focusedItemStyle;
    }

    public String getFocusedItemClass() {
        return ValueBindings.get(this, "focusedItemClass", focusedItemClass);
    }

    public void setFocusedItemClass(String focusedItemClass) {
        this.focusedItemClass = focusedItemClass;
    }

    public String getSelectedItemStyle() {
        return ValueBindings.get(this, "selectedItemStyle", selectedItemStyle);
    }

    public void setSelectedItemStyle(String selectedItemStyle) {
        this.selectedItemStyle = selectedItemStyle;
    }

    public String getSelectedItemClass() {
        return ValueBindings.get(this, "selectedItemClass", selectedItemClass);
    }

    public void setSelectedItemClass(String selectedItemClass) {
        this.selectedItemClass = selectedItemClass;
    }

    public String getRolloverItemStyle() {
        return ValueBindings.get(this, "rolloverItemStyle", rolloverItemStyle);
    }

    public void setRolloverItemStyle(String rolloverItemStyle) {
        this.rolloverItemStyle = rolloverItemStyle;
    }

    public String getRolloverItemClass() {
        return ValueBindings.get(this, "rolloverItemClass", rolloverItemClass);
    }

    public void setRolloverItemClass(String rolloverItemClass) {
        this.rolloverItemClass = rolloverItemClass;
    }

    public String getPressedItemStyle() {
        return ValueBindings.get(this, "pressedItemStyle", pressedItemStyle);
    }

    public void setPressedItemStyle(String pressedItemStyle) {
        this.pressedItemStyle = pressedItemStyle;
    }

    public String getPressedItemClass() {
        return ValueBindings.get(this, "pressedItemClass", pressedItemClass);
    }

    public void setPressedItemClass(String pressedItemClass) {
        this.pressedItemClass = pressedItemClass;
    }

    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context),
                accesskey,
                tabindex,
                title,
                dir,
                lang,
                onselect,
                readonly,
                border,
                layout,
                selectedImageUrl,
                unselectedImageUrl,
                rolloverSelectedImageUrl,
                rolloverUnselectedImageUrl,
                pressedSelectedImageUrl,
                pressedUnselectedImageUrl,
                disabledSelectedImageUrl,
                disabledUnselectedImageUrl,
                enabledStyle,
                enabledClass,
                disabledStyle,
                disabledClass,
                focusedItemStyle,
                focusedItemClass,
                selectedItemStyle,
                selectedItemClass,
                rolloverItemStyle,
                rolloverItemClass,
                pressedItemStyle,
                pressedItemClass
        };
    }

    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        accesskey = (String) values[i++];
        tabindex = (String) values[i++];
        title = (String) values[i++];
        dir = (String) values[i++];
        lang = (String) values[i++];
        onselect = (String) values[i++];
        readonly = (Boolean) values[i++];
        border = (String) values[i++];
        layout = (String) values[i++];
        selectedImageUrl = (String) values[i++];
        unselectedImageUrl = (String) values[i++];
        rolloverSelectedImageUrl = (String) values[i++];
        rolloverUnselectedImageUrl = (String) values[i++];
        pressedSelectedImageUrl = (String) values[i++];
        pressedUnselectedImageUrl = (String) values[i++];
        disabledSelectedImageUrl = (String) values[i++];
        disabledUnselectedImageUrl = (String) values[i++];
        enabledStyle = (String) values[i++];
        enabledClass = (String) values[i++];
        disabledStyle = (String) values[i++];
        disabledClass = (String) values[i++];
        focusedItemStyle = (String) values[i++];
        focusedItemClass = (String) values[i++];
        selectedItemStyle = (String) values[i++];
        selectedItemClass = (String) values[i++];
        rolloverItemStyle = (String) values[i++];
        rolloverItemClass = (String) values[i++];
        pressedItemStyle = (String) values[i++];
        pressedItemClass = (String) values[i++];
    }

    @Override
    public void updateModel(FacesContext context) {
        boolean intercept = isLocalValueSet() && getLocalValue() == null && !NullTypeELResolver.isActive();

        if (intercept) {
            NullTypeELResolver.setActive(true);
        }
        try {
            super.updateModel(context);
        } finally {
            if (intercept) {
                NullTypeELResolver.setActive(false);
            }
        }
    }
    
}
