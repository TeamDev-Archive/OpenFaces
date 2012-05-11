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
package org.openfaces.component.chart.impl;

import org.openfaces.component.chart.PieSectorInfo;
import org.openfaces.component.chart.SeriesInfo;

import java.io.Serializable;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class PieSectorInfoImpl implements PieSectorInfo, Serializable {
    private Object key;
    private Object value;
    private int index;
    private Object proportionalValue;
    private SeriesInfo series;
    private double seriesTotal;

    public SeriesInfo getSeries() {
        return series;
    }

    public void setSeries(SeriesInfo series) {
        this.series = series;
    }

    public void setSeriesTotal(double value) {
        seriesTotal = value;
    }

    public double getSeriesTotal() {
        return seriesTotal;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getProportionalValue() {
        return proportionalValue;
    }

    public void setProportionalValue(Object proportionalValue) {
        this.proportionalValue = proportionalValue;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Returns a hash code of the object.
     *
     * @return An int
     */
    @Override
    public int hashCode() {
        return key.hashCode() + value.hashCode();
    }

    /**
     * Returns <code>true</code> if this object is equal to the specified
     * object, and <code>false</code> otherwise.
     *
     * @param obj the other object.
     * @return A boolean.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof PieSectorInfo))
            return false;
        PieSectorInfo info = (PieSectorInfo) obj;
        return info.getKey().equals(key) && info.getValue().equals(value);

    }
}
