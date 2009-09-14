/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.table;

/**
 * @author Dmitry Pikhulya
 */
public abstract class OneParameterCriterion extends FilterCriterion {
    private Object value = "";
    private boolean caseSensitive;

    protected OneParameterCriterion() {
    }

    protected OneParameterCriterion(Object value) {
        this(value, false);
    }

    protected OneParameterCriterion(Object value, boolean caseSensitive) {
        this.value = value;
        this.caseSensitive = caseSensitive;
    }

    public Object getValue() {
        return value;
    }

    public abstract OneParameterCriterion setValue(Object value);

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OneParameterCriterion that = (OneParameterCriterion) o;

        if (caseSensitive != that.caseSensitive) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (caseSensitive ? 1 : 0);
        return result;
    }

    protected String objectToString(Object obj) {
        if (obj == null)
            return "";
        String strValue = obj.toString();
        if (!isCaseSensitive())
            strValue = strValue.toLowerCase();
        return strValue;
    }

    @Override
    public boolean acceptsAll() {
        return value == null || value.equals("");
    }
}
