/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.datatable;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public abstract class AbstractTemperatureConverter implements Converter, StateHolder {
    private Integer maxFractionDigits = 1;
    private boolean _transient;
    private NumberFormat numberFormat;

    public int getMaxFractionDigits() {
        return maxFractionDigits != null ? maxFractionDigits : 0;
    }

    public void setMaxFractionDigits(int maxFractionDigits) {
        this.maxFractionDigits = maxFractionDigits;
        numberFormat = null;
    }

    public Object saveState(FacesContext context) {
        return new Object[] {
                maxFractionDigits
        };
    }

    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        maxFractionDigits = (Integer) state[i++];
    }

    public boolean isTransient() {
        return _transient;
    }

    public void setTransient(boolean newTransientValue) {
        _transient = newTransientValue;
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        double valueAsDouble;
        String numberPart = value.substring(0, value.length() - getUnitSuffix().length());
        try {
            valueAsDouble = getNumberFormat().parse(numberPart).doubleValue();
        } catch (ParseException e) {
            throw new ConverterException(e);
        }
        return fromNumberValue(valueAsDouble);
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        double numericValue = toNumberValue((Temperature) value);
        NumberFormat numberFormat = getNumberFormat();
        return numberFormat.format(numericValue) + getUnitSuffix();
    }

    private NumberFormat getNumberFormat() {
        if (numberFormat == null) {
            Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            numberFormat = NumberFormat.getNumberInstance(locale);
            if (maxFractionDigits != null)
                numberFormat.setMaximumFractionDigits(maxFractionDigits);
        }
        return numberFormat;
    }

    protected abstract double toNumberValue(Temperature t);

    protected abstract Temperature fromNumberValue(double d);

    protected abstract String getUnitSuffix();
}
