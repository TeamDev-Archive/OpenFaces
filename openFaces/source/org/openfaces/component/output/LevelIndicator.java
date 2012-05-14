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
package org.openfaces.component.output;

import org.openfaces.component.FillDirection;
import org.openfaces.component.OUIOutput;
import org.openfaces.component.chart.Orientation;
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public class LevelIndicator extends OUIOutput {
    public static final String COMPONENT_TYPE = "org.openfaces.LevelIndicator";
    public static final String COMPONENT_FAMILY = "org.openfaces.LevelIndicator";

    private Orientation orientation;
    private String displayAreaStyle;
    private String displayAreaClass;
    private String labelStyle;
    private String labelClass;
    private Integer segmentSize;
    private FillDirection fillDirection;
    private Object colors;
    private Object transitionLevels;
    private Double inactiveSegmentIntensity;

    public LevelIndicator() {
        setRendererType("org.openfaces.LevelIndicatorRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                saveAttachedState(context, orientation),
                displayAreaStyle,
                displayAreaClass,
                labelStyle,
                labelClass,
                segmentSize,
                saveAttachedState(context, fillDirection),
                saveAttachedState(context, colors),
                saveAttachedState(context, transitionLevels),
                inactiveSegmentIntensity
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);

        orientation = (Orientation) restoreAttachedState(context, state[i++]);
        displayAreaStyle = (String) state[i++];
        displayAreaClass = (String) state[i++];
        labelStyle = (String) state[i++];
        labelClass = (String) state[i++];
        segmentSize = (Integer) state[i++];
        fillDirection = (FillDirection) restoreAttachedState(context, state[i++]);
        colors = restoreAttachedState(context, state[i++]);
        transitionLevels = restoreAttachedState(context, state[i++]);
        inactiveSegmentIntensity = (Double) state[i++];
    }

    public Orientation getOrientation() {
        return ValueBindings.get(this, "orientation", orientation, Orientation.HORIZONTAL, Orientation.class);
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public Double getLevel() {
        Double value = (Double) getValue();
        if (value != null) {
            return value;
        } else {
            return 0.5;
        }
    }

    public void setLevel(Double level) {
        setValue(level);
    }


    // ---------------------------------------------------------------- Bindings


    public ValueBinding getValueBinding(String name) {
        if ("level".equals(name)) {
            return (super.getValueBinding("value"));
        } else {
            return (super.getValueBinding(name));
        }
    }


    public void setValueBinding(String name, ValueBinding binding) {
        if ("level".equals(name)) {
            super.setValueBinding("value", binding);
        } else {
            super.setValueBinding(name, binding);
        }
    }

    public ValueExpression getValueExpression(String name) {
        if ("level".equals(name)) {
            return (super.getValueExpression("value"));
        } else {
            return (super.getValueExpression(name));
        }
    }

    public void setValueExpression(String name, ValueExpression binding) {
        if ("level".equals(name)) {
            super.setValueExpression("value", binding);
        } else {
            super.setValueExpression(name, binding);
        }
    }

    public String getDisplayAreaStyle() {
        return ValueBindings.get(this, "displayAreaStyle", displayAreaStyle);
    }

    public void setDisplayAreaStyle(String displayAreaStyle) {
        this.displayAreaStyle = displayAreaStyle;
    }

    public String getDisplayAreaClass() {
        return ValueBindings.get(this, "displayAreaClass", displayAreaClass);
    }

    public void setDisplayAreaClass(String displayAreaClass) {
        this.displayAreaClass = displayAreaClass;
    }

    public String getLabelStyle() {
        return ValueBindings.get(this, "labelStyle", labelStyle);
    }

    public void setLabelStyle(String labelStyle) {
        this.labelStyle = labelStyle;
    }

    public String getLabelClass() {
        return ValueBindings.get(this, "labelClass", labelClass);
    }

    public void setLabelClass(String labelClass) {
        this.labelClass = labelClass;
    }

    public Integer getSegmentSize() {
        return ValueBindings.get(this, "segmentSize", segmentSize, 3);
    }

    public void setSegmentSize(Integer segmentSize) {
        this.segmentSize = segmentSize;
    }

    public FillDirection getFillDirection() {
        return ValueBindings.get(this, "fillDirection", fillDirection, FillDirection.FROM_START, FillDirection.class);
    }

    public void setFillDirection(FillDirection fillDirection) {
        this.fillDirection = fillDirection;
    }

    public Object getColors() {
        return ValueBindings.get(this, "colors", colors, Object.class);
    }

    public void setColors(Object colors) {
        this.colors = colors;
    }

    public Object getTransitionLevels() {
        return ValueBindings.get(this, "transitionLevels", transitionLevels, Object.class);
    }

    public void setTransitionLevels(Object transitionLevels) {
        this.transitionLevels = transitionLevels;
    }

    public Double getInactiveSegmentIntensity() {
        return ValueBindings.get(this, "inactiveSegmentIntensity", inactiveSegmentIntensity, 0.2);
    }

    public void setInactiveSegmentIntensity(Double inactiveSegmentIntensity) {
        this.inactiveSegmentIntensity = inactiveSegmentIntensity;
    }
}
