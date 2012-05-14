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

package org.openfaces.component.table;

import java.util.Comparator;

/**
 * Describes a type of objects that can be used in the DataTable's Summary calculation feature. This class defines a
 * sub-set of operations over the values of the type described by this instance that are required for the summary
 * calculation functions.
 *
 * @author Dmitry Pikhulya
 */
public abstract class OrdinalType {
    public abstract boolean isApplicableForClass(Class valueClass);

    /**
     * The sum of two values. This method does not explicitly checks the types of the values and expects both values to
     * be of a proper type applicable to this OrdinalType instance (as defined by the isApplicableForClass method)
     */
    public Object add(Object value1, Object value2) {
        throw new UnsupportedOperationException();
    }

    /**
     * Divides the specified custom-typed value by the double value, and returns the result as the same custom-typed
     * value.
     */
    public Object divide(Object value, double by) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return a comparator that can compare the values of the described type. This method can return null if the values
     * of this type are supposed to implement the Comparable interface themselves.
     */
    public Comparator getComparator() {
        return null;
    }
}
