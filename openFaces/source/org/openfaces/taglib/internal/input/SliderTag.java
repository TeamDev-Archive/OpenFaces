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

package org.openfaces.taglib.internal.input;

import org.openfaces.component.FillDirection;
import org.openfaces.component.chart.Orientation;
import org.openfaces.component.input.Slider;
import org.openfaces.component.input.TextFieldState;
import org.openfaces.component.input.TicksAlignment;
import org.openfaces.taglib.internal.AbstractUIInputTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author : roman.nikolaienko
 */
public class SliderTag extends AbstractUIInputTag {
    @Override
    public String getComponentType() {
        return Slider.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return "org.openfaces.SliderRenderer";
    }

    @Override
    protected boolean isAutomaticValueAttributeHandling() {
        return false;
    }

    public void setComponentProperties(FacesContext context, UIComponent component) {
        super.setComponentProperties(context, component);
        setIntProperty(component, "barAutoRepeatClickDelay");
        setIntProperty(component, "transitionPeriod");
        setBooleanProperty(component, "disabled");
        setStringProperty(component, "label");
        setStringProperty(component, "tabindex");
        setStringProperty(component, "onchanging");
        setNumberProperty(component, "value");
        setNumberProperty(component, "maxValue");
        setNumberProperty(component, "minValue");
        setEnumerationProperty(component, "orientation", Orientation.class);
        setNumberProperty(component, "minorTickSpacing");
        setNumberProperty(component, "majorTickSpacing");
        setEnumerationProperty(component, "fillDirection", FillDirection.class);
        setBooleanProperty(component, "ticksVisible");
        setBooleanProperty(component, "ticksLabelsVisible");
        setEnumerationProperty(component, "ticksAlignment", TicksAlignment.class);
        setStringProperty(component, "majorTickImageUrl");
        setStringProperty(component, "minorTickImageUrl");
        setBooleanProperty(component, "barVisible");
        setBooleanProperty(component, "barCanChangeValue");
        setStringProperty(component, "barImageUrl");
        setStringProperty(component, "barDisabledImageUrl");

        setStringProperty(component, "barStartImageUrl");
        setStringProperty(component, "barStartDisabledImageUrl");
        setStringProperty(component, "barEndImageUrl");
        setStringProperty(component, "barEndDisabledImageUrl");

        setStringProperty(component, "dragHandleImageUrl");
        setStringProperty(component, "dragHandleDisabledImageUrl");
        setBooleanProperty(component, "snapToTicks");
        setEnumerationProperty(component, "textFieldState", TextFieldState.class);
        setStringProperty(component, "textFieldClass");
        setStringProperty(component, "textFieldStyle");
        setStringProperty(component, "textFieldDisabledClass");
        setStringProperty(component, "textFieldDisabledStyle");
        setBooleanProperty(component, "tooltipEnabled");
        setStringProperty(component, "tooltipClass");
        setStringProperty(component, "tooltipStyle");
        setBooleanProperty(component, "controlButtonsVisible");
        setStringProperty(component, "rightBottomButtonImageUrl");
        setStringProperty(component, "leftTopButtonImageUrl");
        setStringProperty(component, "rightBottomButtonDisabledImageUrl");
        setStringProperty(component, "leftTopButtonDisabledImageUrl");

        setStringProperty(component, "activeElementRolloverClass");
        setStringProperty(component, "activeElementPressedClass");

        setStringProperty(component, "activeElementRolloverStyle");
        setStringProperty(component, "activeElementPressedStyle");
        setStringProperty(component, "rightBottomButtonRolloverImageUrl");
        setStringProperty(component, "leftTopButtonRolloverImageUrl");
        setStringProperty(component, "dragHandleRolloverImageUrl");

    }
}
