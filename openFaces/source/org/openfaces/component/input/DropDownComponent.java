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
import org.openfaces.component.OUIInputText;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Andrew Palval
 */
public abstract class DropDownComponent extends OUIInputText {

    private Side buttonAlignment;

    private String fieldStyle;
    private String rolloverFieldStyle;
    private String disabledFieldStyle;
    private String buttonStyle;
    private String rolloverButtonStyle;
    private String pressedButtonStyle;
    private String listStyle;
    private String rolloverListStyle;
    private String disabledStyle;
    private String disabledButtonStyle;

    private String disabledClass;
    private String fieldClass;
    private String rolloverFieldClass;
    private String disabledFieldClass;
    private String buttonClass;
    private String disabledButtonClass;
    private String rolloverButtonClass;
    private String pressedButtonClass;
    private String listClass;
    private String rolloverListClass;

    private String buttonImageUrl;
    private String disabledButtonImageUrl;


    protected String getListClass() {
        return ValueBindings.get(this, "listClass", listClass);
    }

    protected void setListClass(String listClass) {
        this.listClass = listClass;
    }

    protected String getRolloverListClass() {
        return ValueBindings.get(this, "rolloverListClass", rolloverListClass);
    }

    protected void setRolloverListClass(String rolloverListClass) {
        this.rolloverListClass = rolloverListClass;
    }

    protected String getListStyle() {
        return ValueBindings.get(this, "listStyle", listStyle);
    }

    protected void setListStyle(String listStyle) {
        this.listStyle = listStyle;
    }

    protected String getRolloverListStyle() {
        return ValueBindings.get(this, "rolloverListStyle", rolloverListStyle);
    }

    protected void setRolloverListStyle(String rolloverListStyle) {
        this.rolloverListStyle = rolloverListStyle;
    }

    public String getDisabledFieldStyle() {
        return ValueBindings.get(this, "disabledFieldStyle", disabledFieldStyle);
    }

    public void setDisabledFieldStyle(String disabledFieldStyle) {
        this.disabledFieldStyle = disabledFieldStyle;
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

    public String getDisabledFieldClass() {
        return ValueBindings.get(this, "disabledFieldClass", disabledFieldClass);
    }

    public void setDisabledFieldClass(String disabledFieldClass) {
        this.disabledFieldClass = disabledFieldClass;
    }

    protected String getDisabledButtonStyle() {
        return ValueBindings.get(this, "disabledButtonStyle", disabledButtonStyle);
    }

    protected void setDisabledButtonStyle(String disabledButtonStyle) {
        this.disabledButtonStyle = disabledButtonStyle;
    }

    protected String getDisabledButtonClass() {
        return ValueBindings.get(this, "disabledButtonClass", disabledButtonClass);
    }

    protected void setDisabledButtonClass(String disabledButtonClass) {
        this.disabledButtonClass = disabledButtonClass;
    }

    protected String getDisabledButtonImageUrl() {
        return ValueBindings.get(this, "disabledButtonImageUrl", disabledButtonImageUrl);
    }

    protected void setDisabledButtonImageUrl(String disabledButtonImageUrl) {
        this.disabledButtonImageUrl = disabledButtonImageUrl;
    }

    protected String getPressedButtonStyle() {
        return ValueBindings.get(this, "pressedButtonStyle", pressedButtonStyle);
    }

    protected void setPressedButtonStyle(String pressedButtonStyle) {
        this.pressedButtonStyle = pressedButtonStyle;
    }

    protected String getPressedButtonClass() {
        return ValueBindings.get(this, "pressedButtonClass", pressedButtonClass);
    }

    protected void setPressedButtonClass(String pressedButtonClass) {
        this.pressedButtonClass = pressedButtonClass;
    }

    protected String getFieldClass() {
        return ValueBindings.get(this, "fieldClass", fieldClass);
    }

    protected void setFieldClass(String fieldClass) {
        this.fieldClass = fieldClass;
    }

    protected String getRolloverFieldClass() {
        return ValueBindings.get(this, "rolloverFieldClass", rolloverFieldClass);
    }

    protected void setRolloverFieldClass(String rolloverFieldClass) {
        this.rolloverFieldClass = rolloverFieldClass;
    }

    protected String getButtonClass() {
        return ValueBindings.get(this, "buttonClass", buttonClass);
    }

    protected void setButtonClass(String buttonClass) {
        this.buttonClass = buttonClass;
    }

    protected String getRolloverButtonClass() {
        return ValueBindings.get(this, "rolloverButtonClass", rolloverButtonClass);
    }

    protected void setRolloverButtonClass(String rolloverButtonClass) {
        this.rolloverButtonClass = rolloverButtonClass;
    }

    protected Side getButtonAlignment() {
        return ValueBindings.get(this, "buttonAlignment", buttonAlignment, Side.RIGHT, Side.class);
    }

    protected void setButtonAlignment(Side buttonAlignment) {
        this.buttonAlignment = buttonAlignment;
    }

    protected String getButtonStyle() {
        return ValueBindings.get(this, "buttonStyle", buttonStyle);
    }

    protected void setButtonStyle(String buttonStyle) {
        this.buttonStyle = buttonStyle;
    }

    protected String getRolloverButtonStyle() {
        return ValueBindings.get(this, "rolloverButtonStyle", rolloverButtonStyle);
    }

    protected void setRolloverButtonStyle(String rolloverButtonStyle) {
        this.rolloverButtonStyle = rolloverButtonStyle;
    }

    protected String getFieldStyle() {
        return ValueBindings.get(this, "fieldStyle", fieldStyle);
    }

    protected void setFieldStyle(String fieldStyle) {
        this.fieldStyle = fieldStyle;
    }

    protected String getRolloverFieldStyle() {
        return ValueBindings.get(this, "rolloverFieldStyle", rolloverFieldStyle);
    }

    protected void setRolloverFieldStyle(String rolloverFieldStyle) {
        this.rolloverFieldStyle = rolloverFieldStyle;
    }

    protected String getButtonImageUrl() {
        return ValueBindings.get(this, "buttonImageUrl", buttonImageUrl);
    }

    protected void setButtonImageUrl(String buttonImageUrl) {
        this.buttonImageUrl = buttonImageUrl;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),

                buttonAlignment,

                listStyle,
                rolloverListStyle,
                disabledStyle,
                fieldStyle,
                rolloverFieldStyle,
                disabledFieldStyle,
                buttonStyle,
                rolloverButtonStyle,
                pressedButtonStyle,
                disabledButtonStyle,

                disabledClass,
                listClass,
                rolloverListClass,
                fieldClass,
                rolloverFieldClass,
                disabledFieldClass,
                buttonClass,
                disabledButtonClass,
                rolloverButtonClass,
                pressedButtonClass,

                buttonImageUrl,
                disabledButtonImageUrl,
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);

        buttonAlignment = (Side) values[i++];

        listStyle = (String) values[i++];
        rolloverListStyle = (String) values[i++];
        disabledStyle = (String) values[i++];
        fieldStyle = (String) values[i++];
        rolloverFieldStyle = (String) values[i++];
        disabledFieldStyle = (String) values[i++];
        buttonStyle = (String) values[i++];
        rolloverButtonStyle = (String) values[i++];
        pressedButtonStyle = (String) values[i++];
        disabledButtonStyle = (String) values[i++];

        disabledClass = (String) values[i++];
        listClass = (String) values[i++];
        rolloverListClass = (String) values[i++];
        fieldClass = (String) values[i++];
        rolloverFieldClass = (String) values[i++];
        disabledFieldClass = (String) values[i++];
        buttonClass = (String) values[i++];
        disabledButtonClass = (String) values[i++];
        rolloverButtonClass = (String) values[i++];
        pressedButtonClass = (String) values[i++];

        buttonImageUrl = (String) values[i++];
        disabledButtonImageUrl = (String) values[i++];
    }
}
