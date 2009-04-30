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

import org.openfaces.component.HorizontalAlignment;

/**
 *
 * The DropDownField is an input component which allows either type in a value or select one
 * of the values from an attached drop-down list. The component provides a way to show a list
 * of suggestions based on user input and the ability to auto-complete user input in the input
 * field. The drop-down list can be displayed in multiple columns and contain other JSF components.
 * 
 * @author Andriy Palval
 */
public class DropDownField extends DropDownFieldBase {
    public static final String COMPONENT_TYPE = "org.openfaces.DropDownField";
    public static final String COMPONENT_FAMILY = "org.openfaces.DropDownField";

    public DropDownField() {
        setRendererType("org.openfaces.DropDownFieldRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getListClass() {
        return super.getListClass();
    }

    public void setListClass(String listClass) {
        super.setListClass(listClass);
    }

    public String getRolloverListClass() {
        return super.getRolloverListClass();
    }

    public void setRolloverListClass(String rolloverListClass) {
        super.setRolloverButtonClass(rolloverListClass);
    }

    public String getListStyle() {
        return super.getListStyle();
    }

    public void setListStyle(String listStyle) {
        super.setListStyle(listStyle);
    }

    public String getRolloverListStyle() {
        return super.getRolloverButtonClass();
    }

    public void setRolloverListStyle(String rolloverListStyle) {
        super.setRolloverButtonClass(rolloverListStyle);
    }

    public String getFieldStyle() {
        return super.getFieldStyle();
    }

    public void setFieldStyle(String fieldStyle) {
        super.setFieldStyle(fieldStyle);
    }

    public String getRolloverFieldStyle() {
        return super.getRolloverFieldStyle();
    }

    public void setRolloverFieldStyle(String rolloverFieldStyle) {
        super.setRolloverFieldStyle(rolloverFieldStyle);
    }


    public String getFieldClass() {
        return super.getFieldClass();
    }

    public void setFieldClass(String fieldClass) {
        super.setFieldClass(fieldClass);
    }

    public String getRolloverFieldClass() {
        return super.getRolloverFieldClass();
    }

    public void setRolloverFieldClass(String rolloverFieldClass) {
        super.setRolloverFieldClass(rolloverFieldClass);
    }


    public String getButtonStyle() {
        return super.getButtonStyle();
    }

    public void setButtonStyle(String buttonStyle) {
        super.setButtonStyle(buttonStyle);
    }

    public String getRolloverButtonStyle() {
        return super.getRolloverButtonStyle();
    }

    public void setRolloverButtonStyle(String rolloverButtonStyle) {
        super.setRolloverButtonStyle(rolloverButtonStyle);
    }

    public String getButtonImageUrl() {
        return super.getButtonImageUrl();
    }

    public void setButtonImageUrl(String buttonImageUrl) {
        super.setButtonImageUrl(buttonImageUrl);
    }

    public String getPressedButtonStyle() {
        return super.getPressedButtonStyle();
    }

    public void setPressedButtonStyle(String pressedButtonStyle) {
        super.setPressedButtonStyle(pressedButtonStyle);
    }

    public String getPressedButtonClass() {
        return super.getPressedButtonClass();
    }

    public void setPressedButtonClass(String pressedButtonClass) {
        super.setPressedButtonClass(pressedButtonClass);
    }

    public String getButtonClass() {
        return super.getButtonClass();
    }

    public void setButtonClass(String buttonClass) {
        super.setButtonClass(buttonClass);
    }

    public String getRolloverButtonClass() {
        return super.getRolloverButtonClass();
    }

    public void setRolloverButtonClass(String rolloverButtonClass) {
        super.setRolloverButtonClass(rolloverButtonClass);
    }

    public HorizontalAlignment getButtonAlignment() {
        return super.getButtonAlignment();
    }

    public void setButtonAlignment(HorizontalAlignment buttonAlignment) {
        super.setButtonAlignment(buttonAlignment);
    }

    public String getDisabledButtonStyle() {
        return super.getDisabledButtonStyle();
    }

    public void setDisabledButtonStyle(String disabledButtonStyle) {
        super.setDisabledButtonStyle(disabledButtonStyle);
    }

    public String getDisabledButtonClass() {
        return super.getDisabledButtonClass();
    }

    public void setDisabledButtonClass(String disabledButtonClass) {
        super.setDisabledButtonClass(disabledButtonClass);
    }

    public String getDisabledButtonImageUrl() {
        return super.getDisabledButtonImageUrl();
    }

    public void setDisabledButtonImageUrl(String disabledButtonImageUrl) {
        super.setDisabledButtonImageUrl(disabledButtonImageUrl);
    }
}
