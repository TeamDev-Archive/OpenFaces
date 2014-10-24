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
package org.openfaces.component.input;

import org.openfaces.component.Side;

/**
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

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getListClass() {
        return super.getListClass();
    }

    @Override
    public void setListClass(String listClass) {
        super.setListClass(listClass);
    }

    @Override
    public String getRolloverListClass() {
        return super.getRolloverListClass();
    }

    @Override
    public void setRolloverListClass(String rolloverListClass) {
        super.setRolloverButtonClass(rolloverListClass);
    }

    @Override
    public String getListStyle() {
        return super.getListStyle();
    }

    @Override
    public void setListStyle(String listStyle) {
        super.setListStyle(listStyle);
    }

    @Override
    public String getRolloverListStyle() {
        return super.getRolloverButtonClass();
    }

    @Override
    public void setRolloverListStyle(String rolloverListStyle) {
        super.setRolloverButtonClass(rolloverListStyle);
    }

    @Override
    public String getFieldStyle() {
        return super.getFieldStyle();
    }

    @Override
    public void setFieldStyle(String fieldStyle) {
        super.setFieldStyle(fieldStyle);
    }

    @Override
    public String getRolloverFieldStyle() {
        return super.getRolloverFieldStyle();
    }

    @Override
    public void setRolloverFieldStyle(String rolloverFieldStyle) {
        super.setRolloverFieldStyle(rolloverFieldStyle);
    }


    @Override
    public String getFieldClass() {
        return super.getFieldClass();
    }

    @Override
    public void setFieldClass(String fieldClass) {
        super.setFieldClass(fieldClass);
    }

    @Override
    public String getRolloverFieldClass() {
        return super.getRolloverFieldClass();
    }

    @Override
    public void setRolloverFieldClass(String rolloverFieldClass) {
        super.setRolloverFieldClass(rolloverFieldClass);
    }


    @Override
    public String getButtonStyle() {
        return super.getButtonStyle();
    }

    @Override
    public void setButtonStyle(String buttonStyle) {
        super.setButtonStyle(buttonStyle);
    }

    @Override
    public String getRolloverButtonStyle() {
        return super.getRolloverButtonStyle();
    }

    @Override
    public void setRolloverButtonStyle(String rolloverButtonStyle) {
        super.setRolloverButtonStyle(rolloverButtonStyle);
    }

    @Override
    public String getButtonImageUrl() {
        return super.getButtonImageUrl();
    }

    @Override
    public void setButtonImageUrl(String buttonImageUrl) {
        super.setButtonImageUrl(buttonImageUrl);
    }

    @Override
    public String getPressedButtonStyle() {
        return super.getPressedButtonStyle();
    }

    @Override
    public void setPressedButtonStyle(String pressedButtonStyle) {
        super.setPressedButtonStyle(pressedButtonStyle);
    }

    @Override
    public String getPressedButtonClass() {
        return super.getPressedButtonClass();
    }

    @Override
    public void setPressedButtonClass(String pressedButtonClass) {
        super.setPressedButtonClass(pressedButtonClass);
    }

    @Override
    public String getButtonClass() {
        return super.getButtonClass();
    }

    @Override
    public void setButtonClass(String buttonClass) {
        super.setButtonClass(buttonClass);
    }

    @Override
    public String getRolloverButtonClass() {
        return super.getRolloverButtonClass();
    }

    @Override
    public void setRolloverButtonClass(String rolloverButtonClass) {
        super.setRolloverButtonClass(rolloverButtonClass);
    }

    @Override
    public Side getButtonAlignment() {
        return super.getButtonAlignment();
    }

    @Override
    public void setButtonAlignment(Side buttonAlignment) {
        super.setButtonAlignment(buttonAlignment);
    }

    @Override
    public String getDisabledButtonStyle() {
        return super.getDisabledButtonStyle();
    }

    @Override
    public void setDisabledButtonStyle(String disabledButtonStyle) {
        super.setDisabledButtonStyle(disabledButtonStyle);
    }

    @Override
    public String getDisabledButtonClass() {
        return super.getDisabledButtonClass();
    }

    @Override
    public void setDisabledButtonClass(String disabledButtonClass) {
        super.setDisabledButtonClass(disabledButtonClass);
    }

    @Override
    public String getDisabledButtonImageUrl() {
        return super.getDisabledButtonImageUrl();
    }

    @Override
    public void setDisabledButtonImageUrl(String disabledButtonImageUrl) {
        super.setDisabledButtonImageUrl(disabledButtonImageUrl);
    }

    @Override
    public Boolean getChangeValueOnSelect() {
        return super.getChangeValueOnSelect();
    }

    @Override
    public void setChangeValueOnSelect(Boolean changeValueOnSelect) {
        super.setChangeValueOnSelect(changeValueOnSelect);
    }
}
