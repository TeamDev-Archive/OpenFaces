/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component.input;

import org.openfaces.component.OUIInputBase;
import org.openfaces.util.NullTypeELResolver;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * Author: Oleg Marshalenko
 * Date: Sep 18, 2009
 * Time: 3:38:45 PM
 */
public class SelectOneRadio extends OUIInputBase {

    public static final String COMPONENT_TYPE = "org.openfaces.SelectOneRadio";
    public static final String COMPONENT_FAMILY = "org.openfaces.SelectOneRadio";

    public static final String LAYOUT_PAGE_DIRECTION = "pageDirection";
    public static final String LAYOUT_LINE_DIRECTION = "lineDirection";

    public SelectOneRadio() {
        setRendererType("org.openfaces.SelectOneRadioRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

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
    private String selectedStyle;
    private String selectedClass;
    private String unselectedStyle;
    private String unselectedClass;

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

    public String getUnselectedStyle() {
        return ValueBindings.get(this, "unselectedStyle", unselectedStyle);
    }

    public void setUnselectedStyle(String unselectedStyle) {
        this.unselectedStyle = unselectedStyle;
    }

    public String getUnselectedClass() {
        return ValueBindings.get(this, "unselectedClass", unselectedClass);
    }

    public void setUnselectedClass(String unselectedClass) {
        this.unselectedClass = unselectedClass;
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
                selectedStyle,
                selectedClass,
                unselectedStyle,
                unselectedClass
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
        selectedStyle = (String) values[i++];
        selectedClass = (String) values[i++];
        unselectedStyle = (String) values[i++];
        unselectedClass = (String) values[i];
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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Object getSubmittedValue() {
        if (super.getSubmittedValue() == null) {
            super.setSubmittedValue("");
        }
        return super.getSubmittedValue();
    }


}
