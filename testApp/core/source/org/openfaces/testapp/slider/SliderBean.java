/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.slider;

import org.openfaces.component.FillDirection;
import org.openfaces.component.chart.Orientation;
import org.openfaces.component.input.TextFieldState;
import org.openfaces.component.input.TicksAlignment;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : roman.nikolaienko
 */
public class SliderBean {
    private Number value = 50;
    private Number maxValue = 100;
    private Number minValue = 0;
    private Number majorTickSpacing = 10;
    private Number minorTickSpacing = 5;
    private Integer transitionPeriod = 300;
    private Integer barAutoRepeatClickDelay = 500;

    private Orientation orientation = Orientation.HORIZONTAL;
    private FillDirection fillDirection = FillDirection.FROM_START;
    private TicksAlignment ticksAlignment = TicksAlignment.LEFT_OR_TOP;
    private TextFieldState textFieldState = TextFieldState.OFF;

    private List<SelectItem> orientationList = new ArrayList<SelectItem>();
    private List<SelectItem> ticksAlignmentList = new ArrayList<SelectItem>();
    private List<SelectItem> fillDirectionList = new ArrayList<SelectItem>();
    private List<SelectItem> textFieldStateList = new ArrayList<SelectItem>();


    private boolean ticksVisible = true;
    private boolean ticksLabelsVisible = true;
    private boolean barVisible = true;
    private boolean barCanChangeValue = true;
    private boolean snapToTicks = true;
    private boolean tooltipEnabled = true;
    private boolean disabled = false;
    private boolean controlButtonsVisible = false;

    private double convertToUAH;
    private double convertToEUR;

    public SliderBean() {
        for (Orientation o : Orientation.values()) {
            orientationList.add(new SelectItem(o));
        }
        for (TicksAlignment o : TicksAlignment.values()) {
            ticksAlignmentList.add(new SelectItem(o));
        }
        for (FillDirection o : FillDirection.values()) {
            fillDirectionList.add(new SelectItem(o));
        }
        for (TextFieldState o : TextFieldState.values()) {
            textFieldStateList.add(new SelectItem(o));
        }

    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public Number getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Number maxValue) {
        this.maxValue = maxValue;
    }

    public Number getMinValue() {
        return minValue;
    }

    public void setMinValue(Number minValue) {
        this.minValue = minValue;
    }

    public Number getMajorTickSpacing() {
        return majorTickSpacing;
    }

    public void setMajorTickSpacing(Number majorTickSpacing) {
        this.majorTickSpacing = majorTickSpacing;
    }

    public Number getMinorTickSpacing() {
        return minorTickSpacing;
    }

    public void setMinorTickSpacing(Number minorTickSpacing) {
        this.minorTickSpacing = minorTickSpacing;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public FillDirection getFillDirection() {
        return fillDirection;
    }

    public void setFillDirection(FillDirection fillDirection) {
        this.fillDirection = fillDirection;
    }

    public TicksAlignment getTicksAlignment() {
        return ticksAlignment;
    }

    public void setTicksAlignment(TicksAlignment ticksAlignment) {
        this.ticksAlignment = ticksAlignment;
    }

    public TextFieldState getTextFieldState() {
        return textFieldState;
    }

    public void setTextFieldState(TextFieldState textFieldState) {
        this.textFieldState = textFieldState;
    }

    public boolean isTicksVisible() {
        return ticksVisible;
    }

    public void setTicksVisible(boolean ticksVisible) {
        this.ticksVisible = ticksVisible;
    }

    public boolean isTicksLabelsVisible() {
        return ticksLabelsVisible;
    }

    public void setTicksLabelsVisible(boolean ticksLabelsVisible) {
        this.ticksLabelsVisible = ticksLabelsVisible;
    }

    public boolean isBarVisible() {
        return barVisible;
    }

    public void setBarVisible(boolean barVisible) {
        this.barVisible = barVisible;
    }

    public boolean isBarCanChangeValue() {
        return barCanChangeValue;
    }

    public void setBarCanChangeValue(boolean barCanChangeValue) {
        this.barCanChangeValue = barCanChangeValue;
    }

    public boolean isSnapToTicks() {
        return snapToTicks;
    }

    public void setSnapToTicks(boolean snapToTicks) {
        this.snapToTicks = snapToTicks;
    }

    public boolean isTooltipEnabled() {
        return tooltipEnabled;
    }

    public void setTooltipEnabled(boolean tooltipEnabled) {
        this.tooltipEnabled = tooltipEnabled;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isControlButtonsVisible() {
        return controlButtonsVisible;
    }

    public void setControlButtonsVisible(boolean controlButtonsVisible) {
        this.controlButtonsVisible = controlButtonsVisible;
    }

    public Integer getTransitionPeriod() {
        return transitionPeriod;
    }

    public void setTransitionPeriod(Integer transitionPeriod) {
        this.transitionPeriod = transitionPeriod;
    }

    public Integer getBarAutoRepeatClickDelay() {
        return barAutoRepeatClickDelay;
    }

    public void setBarAutoRepeatClickDelay(Integer barAutoRepeatClickDelay) {
        this.barAutoRepeatClickDelay = barAutoRepeatClickDelay;
    }

    public String getConvertToUAH() {
        return String.valueOf(value.doubleValue() * 7.9);
    }

    public String getConvertToEUR() {
        return String.valueOf(value.doubleValue() * 0.77);
    }

    public List<SelectItem> getOrientationList() {
        return orientationList;
    }

    public List<SelectItem> getTicksAlignmentList() {
        return ticksAlignmentList;
    }

    public List<SelectItem> getFillDirectionList() {
        return fillDirectionList;
    }

    public List<SelectItem> getTextFieldStateList() {
        return textFieldStateList;
    }


}
