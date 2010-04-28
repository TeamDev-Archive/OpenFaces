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
package org.openfaces.component.chart;

import org.openfaces.component.OUIOutput;
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public class LevelIndicator extends OUIOutput {
    public static final String COMPONENT_TYPE = "org.openfaces.LevelIndicator";
    public static final String COMPONENT_FAMILY = "org.openfaces.LevelIndicator";

    public static final String DEFAULT_COLORS = "green, yellow, red";
    public static final String DEFAULT_TRANSIENT_LEVELS = "0.3, 0.55, 0.75";
    
    private Orientation orientation;
    private Object value;
    private String indicatorStyle;
    private String indicatorClass;
    private String labelStyle;
    private String labelClass;
    private Integer indicatorSegmentSize;
    private FillDirection fillDirection;
    private Object colors;
    private Object transitionLevels;
    private Double colorBlendIntensity;

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

        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);

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
            return (value);
        } else {
            return (0.5);
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

    public Object getValue() {
        return ValueBindings.get(this, "value", value, 0.0d, Object.class);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getIndicatorStyle() {
        return ValueBindings.get(this, "indicatorStyle", indicatorStyle);
    }

    public void setIndicatorStyle(String indicatorStyle) {
        this.indicatorStyle = indicatorStyle;
    }

    public String getIndicatorClass() {
        return ValueBindings.get(this, "indicatorClass", indicatorClass);
    }

    public void setIndicatorClass(String indicatorClass) {
        this.indicatorClass = indicatorClass;
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

    public Integer getIndicatorSegmentSize() {
        return ValueBindings.get(this, "indicatorSegmentSize", indicatorSegmentSize, 3);
    }

    public void setIndicatorSegmentSize(Integer indicatorSegmentSize) {
        this.indicatorSegmentSize = indicatorSegmentSize;
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

    public Double getColorBlendIntensity() {
        return ValueBindings.get(this, "colorBlendIntensity", colorBlendIntensity, 0.35);
    }

    public void setColorBlendIntensity(Double colorBlendIntensity) {
        this.colorBlendIntensity = colorBlendIntensity;
    }
}
