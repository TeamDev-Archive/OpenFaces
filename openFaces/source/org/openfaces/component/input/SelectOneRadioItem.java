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

import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Author: Oleg Marshalenko
 * Date: Sep 18, 2009
 * Time: 3:48:43 PM
 */
public class SelectOneRadioItem extends UIComponentBase implements Serializable {
    private static final long serialVersionUID = 6188475522203035120L;

    public static final String COMPONENT_TYPE = "org.openfaces.SelectOneRadioItem";
    public static final String COMPONENT_FAMILY = "org.openfaces.SelectOneRadioItem";

    private Object value;
    private String itemLabel;

    private Boolean disabled;

    private String focusedItemStyle;
    private String focusedItemClass;
    private String selectedItemStyle;
    private String selectedItemClass;
    private String rolloverItemStyle;
    private String rolloverItemClass;
    private String pressedItemStyle;
    private String pressedItemClass;


    public SelectOneRadioItem() {
    }

    public SelectOneRadioItem(Object value) {
        this.value = value;
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Object getValue() {
        return ValueBindings.get(this, "value", value, Object.class);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getItemLabel() {
        return ValueBindings.get(this, "itemLabel", itemLabel);
    }

    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }

    public Boolean isDisabled() {
        return ValueBindings.get(this, "itemLabel", disabled, false);
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
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
        return new Object[]{
                super.saveState(context),
                saveAttachedState(context, value),
                itemLabel,
                disabled,
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
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        value = restoreAttachedState(context, values[i++]);
        itemLabel = (String) values[i++];
        disabled = (Boolean) values[i++];
        focusedItemStyle = (String) values[i++];
        focusedItemClass = (String) values[i++];
        selectedItemStyle = (String) values[i++];
        selectedItemClass = (String) values[i++];
        rolloverItemStyle = (String) values[i++];
        rolloverItemClass = (String) values[i++];
        pressedItemStyle = (String) values[i++];
        pressedItemClass = (String) values[i++];
    }
}
