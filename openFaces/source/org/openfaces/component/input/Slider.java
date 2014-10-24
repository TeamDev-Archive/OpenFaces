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

import org.openfaces.component.FillDirection;
import org.openfaces.component.OUIInputBase;
import org.openfaces.component.chart.Orientation;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author : roman.nikolaienko
 */
public class Slider extends OUIInputBase {
    public static final String COMPONENT_TYPE = "org.openfaces.Slider";
    public static final String COMPONENT_FAMILY = "org.openfaces.Slider";

    private Number maxValue;
    private Number minValue;

    private Orientation orientation;
    private FillDirection fillDirection;

    private Number majorTickSpacing;
    private Number minorTickSpacing;

    private Boolean ticksVisible;
    private Boolean ticksLabelsVisible;

    private TicksAlignment ticksAlignment;
    private String majorTickImageUrl;
    private String minorTickImageUrl;

    private String activeElementRolloverClass;
    private String activeElementPressedClass;
    private String activeElementRolloverStyle;
    private String activeElementPressedStyle;

    private Boolean barVisible;
    private Boolean barCanChangeValue;
    private String barImageUrl;
    private String barDisabledImageUrl;
    private String barStartImageUrl;
    private String barStartDisabledImageUrl;
    private String barEndImageUrl;
    private String barEndDisabledImageUrl;

    private String dragHandleImageUrl;
    private String dragHandleDisabledImageUrl;
    private Boolean snapToTicks;

    private TextFieldState textFieldState;
    private String textFieldClass;
    private String textFieldStyle;
    private String textFieldDisabledClass;
    private String textFieldDisabledStyle;

    private Boolean tooltipEnabled;
    private String tooltipClass;
    private String tooltipStyle;

    private Boolean controlButtonsVisible;
    private String rightBottomButtonImageUrl;
    private String leftTopButtonImageUrl;
    private String rightBottomButtonDisabledImageUrl;
    private String leftTopButtonDisabledImageUrl;

    private String rightBottomButtonRolloverImageUrl;
    private String leftTopButtonRolloverImageUrl;
    private String dragHandleRolloverImageUrl;

    private String onchanging;

    private Integer transitionPeriod;
    private Integer barAutoRepeatClickDelay;

    public Slider() {
        super();
        setRendererType("org.openfaces.SliderRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                maxValue, minValue, orientation, majorTickSpacing, minorTickSpacing, fillDirection,
                ticksVisible, ticksLabelsVisible,
                //           majorTicksLabels,
                //           minorTicksLabels,
                ticksAlignment, majorTickImageUrl, minorTickImageUrl, barVisible, barCanChangeValue,
                barImageUrl, barDisabledImageUrl,
                barStartImageUrl, barStartDisabledImageUrl, barEndImageUrl, barEndDisabledImageUrl,
                dragHandleImageUrl, dragHandleDisabledImageUrl,
                snapToTicks, textFieldState, textFieldClass, textFieldStyle, textFieldDisabledClass,
                textFieldDisabledStyle, tooltipEnabled, tooltipClass, tooltipStyle, controlButtonsVisible,
                rightBottomButtonImageUrl, leftTopButtonImageUrl, rightBottomButtonDisabledImageUrl,
                leftTopButtonDisabledImageUrl, activeElementPressedClass, activeElementRolloverClass,
                activeElementPressedStyle, activeElementRolloverStyle, transitionPeriod, barAutoRepeatClickDelay,
                onchanging,
                rightBottomButtonRolloverImageUrl, leftTopButtonRolloverImageUrl, dragHandleRolloverImageUrl
        };
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        maxValue = (Number) values[i++];
        minValue = (Number) values[i++];
        orientation = (Orientation) values[i++];
        majorTickSpacing = (Number) values[i++];
        minorTickSpacing = (Number) values[i++];
        fillDirection = (FillDirection) values[i++];
        ticksVisible = (Boolean) values[i++];
        ticksLabelsVisible = (Boolean) values[i++];

        ticksAlignment = (TicksAlignment) values[i++];
        majorTickImageUrl = (String) values[i++];
        minorTickImageUrl = (String) values[i++];
        barVisible = (Boolean) values[i++];
        barCanChangeValue = (Boolean) values[i++];
        barImageUrl = (String) values[i++];
        barDisabledImageUrl = (String) values[i++];
        barStartImageUrl = (String) values[i++];
        barStartDisabledImageUrl = (String) values[i++];
        barEndImageUrl = (String) values[i++];
        barEndDisabledImageUrl = (String) values[i++];

        dragHandleImageUrl = (String) values[i++];
        dragHandleDisabledImageUrl = (String) values[i++];
        snapToTicks = (Boolean) values[i++];
        textFieldState = (TextFieldState) values[i++];
        textFieldClass = (String) values[i++];
        textFieldStyle = (String) values[i++];
        textFieldDisabledClass = (String) values[i++];
        textFieldDisabledStyle = (String) values[i++];
        tooltipEnabled = (Boolean) values[i++];
        tooltipClass = (String) values[i++];
        tooltipStyle = (String) values[i++];
        controlButtonsVisible = (Boolean) values[i++];
        rightBottomButtonImageUrl = (String) values[i++];
        leftTopButtonImageUrl = (String) values[i++];
        rightBottomButtonDisabledImageUrl = (String) values[i++];
        leftTopButtonDisabledImageUrl = (String) values[i++];
        activeElementPressedClass = (String) values[i++];
        activeElementRolloverClass = (String) values[i++];
        activeElementPressedStyle = (String) values[i++];
        activeElementRolloverStyle = (String) values[i++];
        transitionPeriod = (Integer) values[i++];
        barAutoRepeatClickDelay = (Integer) values[i++];
        onchanging = (String) values[i++];
        rightBottomButtonRolloverImageUrl = (String) values[i++];
        leftTopButtonRolloverImageUrl = (String) values[i++];
        dragHandleRolloverImageUrl = (String) values[i];

    }


    public Number getMaxValue() {
        return ValueBindings.get(this, "maxValue", maxValue, 100, Number.class);
    }

    public void setMaxValue(Number maxValue) {
        this.maxValue = maxValue;
    }

    public Number getMinValue() {
        return ValueBindings.get(this, "minValue", minValue, 0, Number.class);
    }

    public void setMinValue(Number minValue) {
        this.minValue = minValue;
    }

    public Orientation getOrientation() {
        return ValueBindings.get(this, "orientation", orientation, Orientation.HORIZONTAL, Orientation.class);
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public Number getMajorTickSpacing() {
        return ValueBindings.get(this, "majorTickSpacing", majorTickSpacing, 10, Number.class);
    }

    public void setMajorTickSpacing(Number majorTickSpacing) {
        this.majorTickSpacing = majorTickSpacing;
    }

    public Number getMinorTickSpacing() {
        return ValueBindings.get(this, "minorTickSpacing", minorTickSpacing, 5, Number.class);
    }

    public void setMinorTickSpacing(Number minorTickSpacing) {
        this.minorTickSpacing = minorTickSpacing;
    }

    public FillDirection getFillDirection() {
        return ValueBindings.get(this, "fillDirection", fillDirection, FillDirection.FROM_START, FillDirection.class);
    }

    public void setFillDirection(FillDirection fillDirection) {
        this.fillDirection = fillDirection;
    }

    public boolean isTicksVisible() {
        return ValueBindings.get(this, "ticksVisible", ticksVisible, true);
    }

    public void setTicksVisible(boolean ticksVisible) {
        this.ticksVisible = ticksVisible;
    }

    public boolean isTicksLabelsVisible() {
        return ValueBindings.get(this, "ticksLabelsVisible", ticksLabelsVisible, true);
    }

    public void setTicksLabelsVisible(boolean ticksLabelsVisible) {
        this.ticksLabelsVisible = ticksLabelsVisible;
    }

    public TicksAlignment getTicksAlignment() {
        return ValueBindings.get(this, "ticksAlignment", ticksAlignment,
                getOrientation().equals(Orientation.HORIZONTAL) ?
                        TicksAlignment.LEFT_OR_TOP :
                        TicksAlignment.RIGHT_OR_BOTTOM, TicksAlignment.class);
    }

    public void setTicksAlignment(TicksAlignment ticksAlignment) {
        this.ticksAlignment = ticksAlignment;
    }

    public String getMajorTickImageUrl() {
        return ValueBindings.get(this, "majorTickImageUrl", this.majorTickImageUrl);
    }

    public void setMajorTickImageUrl(String majorTickImageUrl) {
        this.majorTickImageUrl = majorTickImageUrl;
    }

    public String getMinorTickImageUrl() {
        return ValueBindings.get(this, "minorTickImageUrl", this.minorTickImageUrl);
    }

    public void setMinorTickImageUrl(String minorTickImageUrl) {
        this.minorTickImageUrl = minorTickImageUrl;
    }

    public boolean isBarVisible() {
        return ValueBindings.get(this, "barVisible", barVisible, true);
    }

    public void setBarVisible(boolean barVisible) {
        this.barVisible = barVisible;
    }

    public boolean isBarCanChangeValue() {
        return ValueBindings.get(this, "barCanChangeValue", barCanChangeValue, true);
    }

    public void setBarCanChangeValue(boolean barCanChangeValue) {
        this.barCanChangeValue = barCanChangeValue;
    }

    public String getBarImageUrl() {
        return ValueBindings.get(this, "barImageUrl", this.barImageUrl);
    }

    public void setBarImageUrl(String barImageUrl) {
        this.barImageUrl = barImageUrl;
    }

    public String getBarDisabledImageUrl() {
        return ValueBindings.get(this, "barDisabledImageUrl", this.barDisabledImageUrl);
    }

    public void setBarDisabledImageUrl(String barDisabledImageUrl) {
        this.barDisabledImageUrl = barDisabledImageUrl;
    }

    public String getBarStartImageUrl() {
        return ValueBindings.get(this, "barStartImageUrl", this.barStartImageUrl);
    }

    public void setBarStartImageUrl(String barStartImageUrl) {
        this.barStartImageUrl = barStartImageUrl;
    }

    public String getBarStartDisabledImageUrl() {
        return ValueBindings.get(this, "barStartDisabledImageUrl", this.barStartDisabledImageUrl);
    }

    public void setBarStartDisabledImageUrl(String barStartDisabledImageUrl) {
        this.barStartDisabledImageUrl = barStartDisabledImageUrl;
    }

    public String getBarEndImageUrl() {
        return ValueBindings.get(this, "barEndImageUrl", this.barEndImageUrl);
    }

    public void setBarEndImageUrl(String barEndImageUrl) {
        this.barEndImageUrl = barEndImageUrl;
    }

    public String getBarEndDisabledImageUrl() {
        return ValueBindings.get(this, "barEndDisabledImageUrl", this.barEndDisabledImageUrl);
    }

    public void setBarEndDisabledImageUrl(String barEndDisabledImageUrl) {
        this.barEndDisabledImageUrl = barEndDisabledImageUrl;
    }

    public String getDragHandleImageUrl() {
        return ValueBindings.get(this, "dragHandleImageUrl", this.dragHandleImageUrl);
    }

    public void setDragHandleImageUrl(String dragHandleImageUrl) {
        this.dragHandleImageUrl = dragHandleImageUrl;
    }

    public String getDragHandleDisabledImageUrl() {
        return ValueBindings.get(this, "dragHandleDisabledImageUrl", this.dragHandleDisabledImageUrl);
    }

    public void setDragHandleDisabledImageUrl(String dragHandleDisabledImageUrl) {
        this.dragHandleDisabledImageUrl = dragHandleDisabledImageUrl;
    }

    public String getActiveElementRolloverClass() {
        return ValueBindings.get(this, "activeElementRolloverClass", this.activeElementRolloverClass);
    }

    public void setActiveElementRolloverClass(String activeElementRolloverClass) {
        this.activeElementRolloverClass = activeElementRolloverClass;
    }

    public String getActiveElementPressedClass() {
        return ValueBindings.get(this, "activeElementPressedClass", this.activeElementPressedClass);
    }

    public void setActiveElementPressedClass(String activeElementPressedClass) {
        this.activeElementPressedClass = activeElementPressedClass;
    }

    public String getActiveElementRolloverStyle() {
        return ValueBindings.get(this, "activeElementRolloverStyle", this.activeElementRolloverStyle);
    }

    public void setActiveElementRolloverStyle(String activeElementRolloverStyle) {
        this.activeElementRolloverStyle = activeElementRolloverStyle;
    }

    public String getActiveElementPressedStyle() {
        return ValueBindings.get(this, "activeElementPressedStyle", this.activeElementPressedStyle);
    }

    public void setActiveElementPressedStyle(String activeElementPressedStyle) {
        this.activeElementPressedStyle = activeElementPressedStyle;
    }

    public boolean isSnapToTicks() {
        return ValueBindings.get(this, "snapToTicks", this.snapToTicks, true);
    }

    public void setSnapToTicks(boolean snapToTicks) {
        this.snapToTicks = snapToTicks;
    }

    public TextFieldState getTextFieldState() {
        return ValueBindings.get(this, "textFieldState", this.textFieldState, TextFieldState.OFF, TextFieldState.class);
    }

    public void setTextFieldState(TextFieldState textFieldState) {
        this.textFieldState = textFieldState;
    }

    public String getTextFieldClass() {
        return ValueBindings.get(this, "textFieldClass", this.textFieldClass);
    }

    public void setTextFieldClass(String textFieldClass) {
        this.textFieldClass = textFieldClass;
    }

    public String getTextFieldStyle() {
        return ValueBindings.get(this, "textFieldStyle", this.textFieldStyle);
    }

    public void setTextFieldStyle(String textFieldStyle) {
        this.textFieldStyle = textFieldStyle;
    }

    public String getTextFieldDisabledClass() {
        return ValueBindings.get(this, "textFieldDisabledClass", this.textFieldDisabledClass);
    }

    public void setTextFieldDisabledClass(String textFieldDisabledClass) {
        this.textFieldDisabledClass = textFieldDisabledClass;
    }

    public String getTextFieldDisabledStyle() {
        return ValueBindings.get(this, "textFieldDisabledStyle", this.textFieldDisabledStyle);
    }

    public void setTextFieldDisabledStyle(String textFieldDisabledStyle) {
        this.textFieldDisabledStyle = textFieldDisabledStyle;
    }

    public boolean isTooltipEnabled() {
        return ValueBindings.get(this, "tooltipEnabled", this.tooltipEnabled, true);
    }

    public void setTooltipEnabled(boolean tooltipEnabled) {
        this.tooltipEnabled = tooltipEnabled;
    }

    public String getTooltipClass() {
        return ValueBindings.get(this, "tooltipClass", this.tooltipClass);
    }

    public void setTooltipClass(String tooltipClass) {
        this.tooltipClass = tooltipClass;
    }

    public String getTooltipStyle() {
        return ValueBindings.get(this, "tooltipStyle", this.tooltipStyle);
    }

    public void setTooltipStyle(String tooltipStyle) {
        this.tooltipStyle = tooltipStyle;
    }

    public Boolean isControlButtonsVisible() {
        return ValueBindings.get(this, "controlButtonsVisible", this.controlButtonsVisible, false);
    }

    public void setControlButtonsVisible(Boolean controlButtonsVisible) {
        this.controlButtonsVisible = controlButtonsVisible;
    }

    public String getRightBottomButtonImageUrl() {
        return ValueBindings.get(this, "rightBottomButtonImageUrl", this.rightBottomButtonImageUrl);
    }

    public void setRightBottomButtonImageUrl(String rightBottomButtonImageUrl) {
        this.rightBottomButtonImageUrl = rightBottomButtonImageUrl;
    }

    public String getLeftTopButtonImageUrl() {
        return ValueBindings.get(this, "leftTopButtonImageUrl", this.leftTopButtonImageUrl);
    }

    public void setLeftTopButtonImageUrl(String leftTopButtonImageUrl) {
        this.leftTopButtonImageUrl = leftTopButtonImageUrl;
    }

    public String getRightBottomButtonDisabledImageUrl() {
        return ValueBindings.get(this, "rightBottomButtonDisabledImageUrl", this.rightBottomButtonDisabledImageUrl);
    }

    public void setRightBottomButtonDisabledImageUrl(String rightBottomButtonDisabledImageUrl) {
        this.rightBottomButtonDisabledImageUrl = rightBottomButtonDisabledImageUrl;
    }

    public String getLeftTopButtonDisabledImageUrl() {
        return ValueBindings.get(this, "leftTopButtonDisabledImageUrl", this.leftTopButtonDisabledImageUrl);
    }

    public void setLeftTopButtonDisabledImageUrl(String leftTopButtonDisabledImageUrl) {
        this.leftTopButtonDisabledImageUrl = leftTopButtonDisabledImageUrl;
    }

    public String getOnchanging() {
        return ValueBindings.get(this, "onchanging", this.onchanging);
    }

    public void setOnchanging(String onchanging) {
        this.onchanging = onchanging;
    }

    public Integer getTransitionPeriod() {
        return ValueBindings.get(this, "transitionPeriod", transitionPeriod, 100, Integer.class);
    }

    public void setTransitionPeriod(Integer transitionPeriod) {
        this.transitionPeriod = transitionPeriod;
    }

    public Integer getBarAutoRepeatClickDelay() {
        return ValueBindings.get(this, "barAutoRepeatClickDelay", barAutoRepeatClickDelay, 300, Integer.class);
    }

    public void setBarAutoRepeatClickDelay(Integer barAutoRepeatClickDelay) {
        this.barAutoRepeatClickDelay = barAutoRepeatClickDelay;
    }

    public String getDragHandleRolloverImageUrl() {
        return ValueBindings.get(this, "dragHandleRolloverImageUrl", dragHandleRolloverImageUrl);
    }

    public void setDragHandleRolloverImageUrl(String dragHandleRolloverImageUrl) {
        this.dragHandleRolloverImageUrl = dragHandleRolloverImageUrl;
    }

    public String getRightBottomButtonRolloverImageUrl() {
        return ValueBindings.get(this, "rightBottomButtonRolloverImageUrl", rightBottomButtonRolloverImageUrl);
    }

    public void setRightBottomButtonRolloverImageUrl(String rightBottomButtonRolloverImageUrl) {
        this.rightBottomButtonRolloverImageUrl = rightBottomButtonRolloverImageUrl;
    }

    public String getLeftTopButtonRolloverImageUrl() {
        return ValueBindings.get(this, "leftTopButtonRolloverImageUrl", leftTopButtonRolloverImageUrl);
    }

    public void setLeftTopButtonRolloverImageUrl(String leftTopButtonRolloverImageUrl) {
        this.leftTopButtonRolloverImageUrl = leftTopButtonRolloverImageUrl;
    }
}
