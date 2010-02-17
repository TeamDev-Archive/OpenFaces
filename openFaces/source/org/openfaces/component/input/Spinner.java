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

package org.openfaces.component.input;

import org.openfaces.component.Side;
import org.openfaces.util.ValueBindings;
import org.openfaces.util.NullTypeELResolver;

import javax.faces.context.FacesContext;

/**
 * @author Alexander Golubev
 */
public class Spinner extends DropDownComponent {
    public static final String COMPONENT_TYPE = "org.openfaces.Spinner";
    public static final String COMPONENT_FAMILY = "org.openfaces.Spinner";

    private Boolean cycled;
    private Boolean typingAllowed;
    private Number maxValue;
    private Number minValue;
    private Number step;
    private String disabledIncreaseButtonImageUrl;
    private String disabledDecreaseButtonImageUrl;
    private String increaseButtonImageUrl;
    private String decreaseButtonImageUrl;

    public Spinner() {
        setRendererType("org.openfaces.SpinnerRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Side getButtonAlignment() {
        return super.getButtonAlignment();
    }

    @Override
    public void setButtonAlignment(Side buttonAlignment) {
        super.setButtonAlignment(buttonAlignment);
    }

    public boolean isCycled() {
        return ValueBindings.get(this, "cycled", cycled, false);
    }

    public void setCycled(boolean cycled) {
        this.cycled = cycled;
    }

    public boolean getTypingAllowed() {
        return ValueBindings.get(this, "typingAllowed", typingAllowed, true);
    }

    public void setTypingAllowed(boolean typingAllowed) {
        this.typingAllowed = typingAllowed;
    }

    public Number getMaxValue() {
        return ValueBindings.get(this, "maxValue", maxValue, null, Number.class);
    }

    public void setMaxValue(Number maxValue) {
        this.maxValue = maxValue;
    }

    public Number getStep() {
        return ValueBindings.get(this, "step", step, 1, Number.class);
    }

    public void setStep(Number step) {
        this.step = step;
    }

    public Number getMinValue() {
        return ValueBindings.get(this, "minValue", minValue, null, Number.class);
    }

    public void setMinValue(Number minValue) {
        this.minValue = minValue;
    }

    public String getDisabledIncreaseButtonImageUrl() {
        return ValueBindings.get(this, "disabledIncreaseButtonImageUrl", disabledIncreaseButtonImageUrl);
    }

    public void setDisabledIncreaseButtonImageUrl(String disabledIncreaseButtonImageUrl) {
        this.disabledIncreaseButtonImageUrl = disabledIncreaseButtonImageUrl;
    }

    public String getDisabledDecreaseButtonImageUrl() {
        return ValueBindings.get(this, "disabledDecreaseButtonImageUrl", disabledDecreaseButtonImageUrl);
    }

    public void setDisabledDecreaseButtonImageUrl(String disabledDecreaseButtonImageUrl) {
        this.disabledDecreaseButtonImageUrl = disabledDecreaseButtonImageUrl;
    }

    public String getIncreaseButtonImageUrl() {
        return ValueBindings.get(this, "increaseButtonImageUrl", increaseButtonImageUrl);
    }

    public void setIncreaseButtonImageUrl(String increaseButtonImageUrl) {
        this.increaseButtonImageUrl = increaseButtonImageUrl;
    }

    public String getDecreaseButtonImageUrl() {
        return ValueBindings.get(this, "decreaseButtonImageUrl", decreaseButtonImageUrl);
    }

    public void setDecreaseButtonImageUrl(String decreaseButtonImageUrl) {
        this.decreaseButtonImageUrl = decreaseButtonImageUrl;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),

                cycled,
                typingAllowed,
                maxValue,
                minValue,
                disabledIncreaseButtonImageUrl,
                disabledDecreaseButtonImageUrl,
                increaseButtonImageUrl,
                decreaseButtonImageUrl,
                step
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);

        cycled = (Boolean) values[i++];
        typingAllowed = (Boolean) values[i++];
        maxValue = (Number) values[i++];
        minValue = (Number) values[i++];
        disabledIncreaseButtonImageUrl = (String) values[i++];
        disabledDecreaseButtonImageUrl = (String) values[i++];
        increaseButtonImageUrl = (String) values[i++];
        decreaseButtonImageUrl = (String) values[i++];
        step = (Number) values[i++];
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
    public void setDisabledButtonClass(String disabledButtonClass) {
        super.setDisabledButtonClass(disabledButtonClass);
    }

    @Override
    public void setDisabledButtonImageUrl(String disabledButtonImageUrl) {
        super.setDisabledButtonImageUrl(disabledButtonImageUrl);
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
    public String getDisabledButtonImageUrl() {
        return super.getDisabledButtonImageUrl();
    }

    @Override
    public String getDisabledButtonStyle() {
        return super.getDisabledButtonStyle();
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
