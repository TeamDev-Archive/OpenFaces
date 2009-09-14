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
public class ContainsFilterCriterion extends OneParameterCriterion {

    public ContainsFilterCriterion() {
    }

    public ContainsFilterCriterion(Object value) {
        super(value);
    }

    public ContainsFilterCriterion(Object value, boolean caseSensitive) {
        super(value, caseSensitive);
    }

    @Override
    public OneParameterCriterion setValue(Object value) {
        return new ContainsFilterCriterion(value, isCaseSensitive());
    }


    public boolean acceptsValue(Object value) {
        Object filterValue = getValue();
        if (filterValue == null)
            return true;

        return objectToString(value).contains(objectToString(filterValue));
    }

    @Override
    public boolean acceptsAll() {
        return super.acceptsAll() || "".equals(getValue());
    }
}
