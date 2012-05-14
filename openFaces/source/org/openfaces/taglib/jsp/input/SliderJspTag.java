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

package org.openfaces.taglib.jsp.input;

import org.openfaces.taglib.internal.input.SliderTag;
import org.openfaces.taglib.jsp.UIInputJspTag;

import javax.el.ValueExpression;

/**
 * @author : roman.nikolaienko
 */
public class SliderJspTag extends UIInputJspTag {

    public SliderJspTag() {
        super(new SliderTag());
    }

    public void setTransitionPeriod(ValueExpression transitionPeriod) {
        getDelegate().setPropertyValue("transitionPeriod", transitionPeriod);
    }

    public void setBarAutoRepeatClickDelay(ValueExpression barAutoRepeatClickDelay) {
        getDelegate().setPropertyValue("barAutoRepeatClickDelay", barAutoRepeatClickDelay);
    }

    public void setMaxValue(ValueExpression maxValue) {
        getDelegate().setPropertyValue("maxValue", maxValue);
    }

    public void setMinValue(ValueExpression minValue) {
        getDelegate().setPropertyValue("minValue", minValue);
    }

    public void setOrientation(ValueExpression orientation) {
        getDelegate().setPropertyValue("orientation", orientation);
    }

    public void setMajorTickSpacing(ValueExpression majorTickSpacing) {
        getDelegate().setPropertyValue("majorTickSpacing", majorTickSpacing);
    }

    public void setMinorTickSpacing(ValueExpression minorTickSpacing) {
        getDelegate().setPropertyValue("minorTickSpacing", minorTickSpacing);
    }

    public void setFillDirection(ValueExpression fillDirection) {
        getDelegate().setPropertyValue("fillDirection", fillDirection);
    }

    public void setTicksVisible(ValueExpression ticksVisible) {
        getDelegate().setPropertyValue("ticksVisible", ticksVisible);
    }

    public void setTicksLabelsVisible(ValueExpression ticksLabelsVisible) {
        getDelegate().setPropertyValue("ticksLabelsVisible", ticksLabelsVisible);
    }

    public void setTicksAlignment(ValueExpression ticksAlignment) {
        getDelegate().setPropertyValue("ticksAlignment", ticksAlignment);
    }

    public void setMajorTickImageUrl(ValueExpression majorTickImageUrl) {
        getDelegate().setPropertyValue("majorTickImageUrl", majorTickImageUrl);
    }

    public void setMinorTickImageUrl(ValueExpression minorTickImageUrl) {
        getDelegate().setPropertyValue("minorTickImageUrl", minorTickImageUrl);
    }

    public void setBarVisible(ValueExpression barVisible) {
        getDelegate().setPropertyValue("barVisible", barVisible);
    }

    public void setBarCanChangeValue(ValueExpression barCanChangeValue) {
        getDelegate().setPropertyValue("barCanChangeValue", barCanChangeValue);
    }

    public void setBarImageUrl(ValueExpression barImageUrl) {
        getDelegate().setPropertyValue("barImageUrl", barImageUrl);
    }

    public void setBarDisabledImageUrl(ValueExpression barDisabledImageUrl) {
        getDelegate().setPropertyValue("barDisabledImageUrl", barDisabledImageUrl);
    }

    public void setBarStartImageUrl(ValueExpression barStartImageUrl) {
        getDelegate().setPropertyValue("barStartImageUrl", barStartImageUrl);
    }

    public void setBarStartDisabledImageUrl(ValueExpression barStartDisabledImageUrl) {
        getDelegate().setPropertyValue("barStartDisabledImageUrl", barStartDisabledImageUrl);
    }

    public void setBarEndImageUrl(ValueExpression barEndImageUrl) {
        getDelegate().setPropertyValue("barEndImageUrl", barEndImageUrl);
    }

    public void setBarEndDisabledImageUrl(ValueExpression barEndDisabledImageUrl) {
        getDelegate().setPropertyValue("barEndDisabledImageUrl", barEndDisabledImageUrl);
    }


    public void setDragHandleImageUrl(ValueExpression dragHandleImageUrl) {
        getDelegate().setPropertyValue("dragHandleImageUrl", dragHandleImageUrl);
    }

    public void setDragHandleDisabledImageUrl(ValueExpression dragHandleDisabledImageUrl) {
        getDelegate().setPropertyValue("dragHandleDisabledImageUrl", dragHandleDisabledImageUrl);
    }

    public void setSnapToTicks(ValueExpression snapToTicks) {
        getDelegate().setPropertyValue("snapToTicks", snapToTicks);
    }

    public void setTextFieldState(ValueExpression textFieldState) {
        getDelegate().setPropertyValue("textFieldState", textFieldState);
    }

    public void setTextFieldClass(ValueExpression textFieldClass) {
        getDelegate().setPropertyValue("textFieldClass", textFieldClass);
    }

    public void setTextFieldStyle(ValueExpression textFieldStyle) {
        getDelegate().setPropertyValue("textFieldStyle", textFieldStyle);
    }

    public void setTextFieldDisabledClass(ValueExpression textFieldDisabledClass) {
        getDelegate().setPropertyValue("textFieldDisabledClass", textFieldDisabledClass);
    }

    public void setTextFieldDisabledStyle(ValueExpression textFieldDisabledStyle) {
        getDelegate().setPropertyValue("textFieldDisabledStyle", textFieldDisabledStyle);
    }

    public void setTooltipEnabled(ValueExpression tooltipEnabled) {
        getDelegate().setPropertyValue("tooltipEnabled", tooltipEnabled);
    }

    public void setTooltipClass(ValueExpression tooltipClass) {
        getDelegate().setPropertyValue("tooltipClass", tooltipClass);
    }

    public void setTooltipStyle(ValueExpression tooltipStyle) {
        getDelegate().setPropertyValue("tooltipStyle", tooltipStyle);
    }

    public void setControlButtonsVisible(ValueExpression controlButtonsVisible) {
        getDelegate().setPropertyValue("controlButtonsVisible", controlButtonsVisible);
    }

    public void setRightBottomButtonImageUrl(ValueExpression rightBottomButtonImageUrl) {
        getDelegate().setPropertyValue("rightBottomButtonImageUrl", rightBottomButtonImageUrl);
    }

    public void setLeftTopButtonImageUrl(ValueExpression leftTopButtonImageUrl) {
        getDelegate().setPropertyValue("leftTopButtonImageUrl", leftTopButtonImageUrl);
    }

    public void setRightBottomButtonDisabledImageUrl(ValueExpression rightBottomButtonDisabledImageUrl) {
        getDelegate().setPropertyValue("rightBottomButtonDisabledImageUrl", rightBottomButtonDisabledImageUrl);
    }

    public void setLeftTopButtonDisabledImageUrl(ValueExpression leftTopButtonDisabledImageUrl) {
        getDelegate().setPropertyValue("leftTopButtonDisabledImageUrl", leftTopButtonDisabledImageUrl);
    }

    public void setOnchanging(ValueExpression onchanging) {
        getDelegate().setPropertyValue("onchanging", onchanging);
    }

    public void setActiveElementRolloverClass(ValueExpression activeElementRolloverClass) {
        getDelegate().setPropertyValue("activeElementRolloverClass", activeElementRolloverClass);
    }

    public void setActiveElementFocusedClass(ValueExpression activeElementFocusedClass) {
        getDelegate().setPropertyValue("activeElementPressedClass", activeElementFocusedClass);
    }

    public void setActiveElementRolloverStyle(ValueExpression activeElementRolloverStyle) {
        getDelegate().setPropertyValue("activeElementRolloverStyle", activeElementRolloverStyle);
    }

    public void setActiveElementFocusedStyle(ValueExpression activeElementFocusedStyle) {
        getDelegate().setPropertyValue("activeElementPressedStyle", activeElementFocusedStyle);
    }


    public void setRightBottomButtonRolloverImageUrl(ValueExpression rightBottomButtonRolloverImageUrl) {
        getDelegate().setPropertyValue("rightBottomButtonRolloverImageUrl", rightBottomButtonRolloverImageUrl);
    }

    public void setLeftTopButtonRolloverImageUrl(ValueExpression leftTopButtonRolloverImageUrl) {
        getDelegate().setPropertyValue("leftTopButtonRolloverImageUrl", leftTopButtonRolloverImageUrl);
    }

    public void setDragHandleRolloverImageUrl(ValueExpression dragHandleRolloverImageUrl) {
        getDelegate().setPropertyValue("dragHandleRolloverImageUrl", dragHandleRolloverImageUrl);
    }
}
