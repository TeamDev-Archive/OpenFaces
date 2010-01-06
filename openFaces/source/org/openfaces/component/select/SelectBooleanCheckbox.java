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
package org.openfaces.component.select;

import org.openfaces.component.OUIInputBase;
import org.openfaces.util.NullTypeELResolver;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

/**
 * @author Roman Porotnikov
 */
public class SelectBooleanCheckbox extends OUIInputBase {
    public static final String SELECTED_STATE = "selected";
    public static final String UNSELECTED_STATE = "unselected";
    public static final String UNDEFINED_STATE = "undefined";

    /**
     * This enumeration is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public enum BooleanObjectValue {
        TRUE, FALSE, NULL
    }

    public static final String COMPONENT_TYPE = "org.openfaces.SelectBooleanCheckbox";
    public static final String COMPONENT_FAMILY = "org.openfaces.SelectBooleanCheckbox";

    public SelectBooleanCheckbox() {
        setRendererType("org.openfaces.SelectBooleanCheckboxRenderer");
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

    private Boolean triStateAllowed;
    private Iterable<String> stateList;

    private String selectedImageUrl;
    private String unselectedImageUrl;
    private String undefinedImageUrl;

    private String rolloverSelectedImageUrl;
    private String rolloverUnselectedImageUrl;
    private String rolloverUndefinedImageUrl;

    private String pressedSelectedImageUrl;
    private String pressedUnselectedImageUrl;
    private String pressedUndefinedImageUrl;

    private String disabledSelectedImageUrl;
    private String disabledUnselectedImageUrl;
    private String disabledUndefinedImageUrl;

    private String selectedStyle;
    private String selectedClass;
    private String unselectedStyle;
    private String unselectedClass;
    private String undefinedStyle;
    private String undefinedClass;

    public boolean isSelected() {
        return (Boolean.TRUE.equals(getValue()));
    }

    public void setSelected(boolean selected) {
        setValue(Boolean.valueOf(selected));
    }

    public boolean isDefined() {
        return getValue() != null;
    }

    public void setDefined(boolean defined) {
        if (defined) {
            if (getValue() == null) {
                setValue(Boolean.FALSE);
            }
        } else {
            setValue(null);
        }
    }

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

    public boolean isTriStateAllowed() {
        return ValueBindings.get(this, "triStateAllowed", triStateAllowed, false);
    }

    public void setTriStateAllowed(boolean triStateAllowed) {
        this.triStateAllowed = triStateAllowed;
    }

    public void setStateList(Iterable<String> stateList) {
        this.stateList = stateList;
    }

    public Iterable<String> getStateList() {
        return ValueBindings.get(this, "stateList", stateList, Iterable.class);
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

    public String getUndefinedImageUrl() {
        return ValueBindings.get(this, "undefinedImageUrl", undefinedImageUrl);
    }

    public void setUndefinedImageUrl(String undefinedImageUrl) {
        this.undefinedImageUrl = undefinedImageUrl;
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

    public String getRolloverUndefinedImageUrl() {
        return ValueBindings.get(this, "rolloverUndefinedImageUrl", rolloverUndefinedImageUrl);
    }

    public void setRolloverUndefinedImageUrl(String rolloverUndefinedImageUrl) {
        this.rolloverUndefinedImageUrl = rolloverUndefinedImageUrl;
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

    public String getPressedUndefinedImageUrl() {
        return ValueBindings.get(this, "pressedUndefinedImageUrl", pressedUndefinedImageUrl);
    }

    public void setPressedUndefinedImageUrl(String pressedUndefinedImageUrl) {
        this.pressedUndefinedImageUrl = pressedUndefinedImageUrl;
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

    public String getDisabledUndefinedImageUrl() {
        return ValueBindings.get(this, "disabledUndefinedImageUrl", disabledUndefinedImageUrl);
    }

    public void setDisabledUndefinedImageUrl(String disabledUndefinedImageUrl) {
        this.disabledUndefinedImageUrl = disabledUndefinedImageUrl;
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

    public String getUndefinedStyle() {
        return ValueBindings.get(this, "undefinedStyle", undefinedStyle);
    }

    public void setUndefinedStyle(String undefinedStyle) {
        this.undefinedStyle = undefinedStyle;
    }

    public String getUndefinedClass() {
        return ValueBindings.get(this, "undefinedClass", undefinedClass);
    }

    public void setUndefinedClass(String undefinedClass) {
        this.undefinedClass = undefinedClass;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context),
                accesskey,
                tabindex,
                title,
                triStateAllowed,
                saveAttachedState(context, stateList),
                dir,
                lang,
                onselect,
                selectedImageUrl,
                unselectedImageUrl,
                undefinedImageUrl,
                rolloverSelectedImageUrl,
                rolloverUnselectedImageUrl,
                rolloverUndefinedImageUrl,
                pressedSelectedImageUrl,
                pressedUnselectedImageUrl,
                pressedUndefinedImageUrl,
                disabledSelectedImageUrl,
                disabledUnselectedImageUrl,
                disabledUndefinedImageUrl,
                selectedStyle,
                selectedClass,
                unselectedStyle,
                unselectedClass,
                undefinedStyle,
                undefinedClass
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        accesskey = (String) values[i++];
        tabindex = (String) values[i++];
        title = (String) values[i++];
        triStateAllowed = (Boolean) values[i++];
        stateList = (Iterable<String>) restoreAttachedState(context, values[i++]);
        dir = (String) values[i++];
        lang = (String) values[i++];
        onselect = (String) values[i++];
        selectedImageUrl = (String) values[i++];
        unselectedImageUrl = (String) values[i++];
        undefinedImageUrl = (String) values[i++];
        rolloverSelectedImageUrl = (String) values[i++];
        rolloverUnselectedImageUrl = (String) values[i++];
        rolloverUndefinedImageUrl = (String) values[i++];
        pressedSelectedImageUrl = (String) values[i++];
        pressedUnselectedImageUrl = (String) values[i++];
        pressedUndefinedImageUrl = (String) values[i++];
        disabledSelectedImageUrl = (String) values[i++];
        disabledUnselectedImageUrl = (String) values[i++];
        disabledUndefinedImageUrl = (String) values[i++];
        selectedStyle = (String) values[i++];
        selectedClass = (String) values[i++];
        unselectedStyle = (String) values[i++];
        unselectedClass = (String) values[i++];
        undefinedStyle = (String) values[i++];
        undefinedClass = (String) values[i++];
    }

    @Override
    protected Object getConvertedValue(FacesContext context, Object newSubmittedValue) throws ConverterException {
        BooleanObjectValue booleanObjectValue = (BooleanObjectValue) newSubmittedValue;

        switch (booleanObjectValue) {
            case TRUE:
                return Boolean.TRUE;
            case FALSE:
                return Boolean.FALSE;
            default:
                return null;
        }
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
