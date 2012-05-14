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

package org.openfaces.component.filter;

import org.openfaces.component.OUIComponentBase;
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author Natalia Zolochevska
 */
public class UIFilterProperty extends OUIComponentBase implements ValueHolder, Serializable, FilterProperty {

    public static final String COMPONENT_FAMILY = "org.openfaces.FilterProperty";
    public static final String COMPONENT_TYPE = "org.openfaces.FilterProperty";

    public static final String DEFAULT_PATTERN = ((SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT)).toPattern();

    private String name;
    private String value;
    private FilterType type = FilterType.TEXT;

    private Object dataProvider;
    private Converter converter;

    private Number maxValue;
    private Number minValue;
    private Number step;

    private String pattern;
    private TimeZone timeZone;

    private boolean caseSensitive;

    public UIFilterProperty() {
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getRendererType() {
        return null;
    }

    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, value, name, type, dataProvider, maxValue, minValue, step,
                pattern, timeZone, caseSensitive,
                saveAttachedState(context, converter)};
    }

    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        value = (String) state[i++];
        name = (String) state[i++];
        type = (FilterType) state[i++];
        dataProvider = state[i++];
        maxValue = (Number) state[i++];
        minValue = (Number) state[i++];
        step = (Number) state[i++];
        pattern = (String) state[i++];
        timeZone = (TimeZone) state[i++];
        caseSensitive = (Boolean) state[i++];
        converter = (Converter) restoreAttachedState(context, state[i++]);
    }

    public Object getValue() {
        if (value != null) return value;
        ValueExpression ve = getValueExpression("value");
        return ve != null ? ve.getValue(getFacesContext().getELContext()) : null;
    }


    public String getTitle() {
        return (String) getValue();
    }

    public void setValue(Object value) {
        this.value = (String) value;
    }

    public Object getLocalValue() {
        return null;
    }

    public String getName() {
        if (name == null) {
            return getTitle();
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FilterType getType() {
        return type;
    }

    public void setType(FilterType type) {
        this.type = type;
    }

    public Object getDataProvider() {
        if (dataProvider != null) return dataProvider;
        ValueExpression ve = getValueExpression("dataProvider");
        return ve != null ? ve.getValue(getFacesContext().getELContext()) : null;
    }

    public void setDataProvider(Object dataProvider) {
        this.dataProvider = dataProvider;
    }

    public Converter getConverter() {
        if (converter != null) return converter;
        ValueExpression ve = getValueExpression("converter");
        return ve != null ? (Converter) ve.getValue(getFacesContext().getELContext()) : null;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public Number getMaxValue() {
        return ValueBindings.get(this, "maxValue", maxValue, Integer.MAX_VALUE, true, Number.class);
    }

    public void setMaxValue(Number maxValue) {
        this.maxValue = maxValue;
    }

    public Number getMinValue() {
        return ValueBindings.get(this, "minValue", minValue, 0, true, Number.class);
    }

    public void setMinValue(Number minValue) {
        this.minValue = minValue;
    }

    public Number getStep() {
        return ValueBindings.get(this, "step", step, 1, true, Number.class);
    }

    public void setStep(Number step) {
        this.step = step;
    }

    public String getPattern() {
        return ValueBindings.get(this, "pattern", pattern, DEFAULT_PATTERN);
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public TimeZone getTimeZone() {
        return ValueBindings.get(this, "timeZone", timeZone, TimeZone.getDefault(), TimeZone.class);
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public boolean isCaseSensitive() {
        return ValueBindings.get(this, "caseSensitive", caseSensitive, false);
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public PropertyLocator getPropertyLocator() {
        return PropertyLocator.getDefaultInstance(getName());
    }
}