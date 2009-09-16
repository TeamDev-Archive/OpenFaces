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
public class EqualsFilterCriterion extends OneParameterCriterion {

    public EqualsFilterCriterion() {
    }

    public EqualsFilterCriterion(Object value) {
        super(value);
    }

    public EqualsFilterCriterion(Object value, boolean caseSensitive) {
        super(value, caseSensitive);
    }

    public boolean acceptsValue(Object value) {
        Object filterValue = getValue();
        if (filterValue == null || filterValue.equals(""))
            return true;
        if (!(value instanceof String) || isCaseSensitive())
            return value.equals(filterValue);
        return objectToString(value).equals(objectToString(filterValue));

    }

    @Override
    public OneParameterCriterion setValue(Object value) {
        return new EqualsFilterCriterion(value, isCaseSensitive());
    }

    @Override
    public OneParameterCriterion setCaseSensitive(boolean caseSensitive) {
        return new EqualsFilterCriterion(getValue(), caseSensitive);
    }
}
