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

import org.openfaces.util.ApplicationParams;

import javax.faces.FacesException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public abstract class SummaryFunction implements Serializable {

    public boolean isApplicableForClass(Class valueClass) {
        OrdinalType typeForClass = getTypeForClass(valueClass, false);
        if (typeForClass == null) {
            // the passed class cannot be recognized by any of the registered OrdinalTypes
            return false;
        }

        return isApplicableForOrdinalType(typeForClass);
    }

    protected boolean isApplicableForOrdinalType(OrdinalType ordinalType) {
        return true;
    }

    private static OrdinalType getTypeForClass(Class valueClass, boolean throwExceptionOnUnsupportedClass) {
        if (valueClass == null) {
            if (throwExceptionOnUnsupportedClass)
                throw new IllegalArgumentException("valueClass shouldn't be null");
            else
                return null;
        }
        List<OrdinalType> ordinalTypes = ApplicationParams.getOrdinalTypes();
        for (OrdinalType type : ordinalTypes) {
            if (type.isApplicableForClass(valueClass))
                return type;
        }
        if (throwExceptionOnUnsupportedClass)
            throw new FacesException("Unsupported object type for summary calculation: " + valueClass.getName() +
                    "; could not find an appropriate OrdinalType implementation that describes operations on " +
                    "this type of objects.");
        else
            return null;
    }


    public abstract Calculator startCalculation();

    public abstract String getName();

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SummaryFunction)) return false;
        SummaryFunction that = (SummaryFunction) obj;
        return getName().equals(that.getName());
    }

    @Override
    public String toString() {
        return getName();
    }

    public static abstract class Calculator {
        protected Object accumulator;

        private OrdinalType ordinalType;
        private Comparator comparator;

        private boolean calculationCompleted;

        public abstract void addValue(Object value);

        /**
         * The endCalculation method can be invoked only once on the same instance of Calculator.
         * <p/>
         * This method can return <code>null</code> in a "no result" case, which can happen when this calculator was not provided
         * any data values for calculation.
         */
        public Object endCalculation() {
            if (calculationCompleted) throw new IllegalStateException("Calculation for this instance has already " +
                    "been completed. The endCalculation method can be invoked only once per each Calculator instance.");
            calculationCompleted = true;
            return accumulator;

        }

        /**
         * Returns the OrdinalType instance that describes operations on objects of the same type as of the passed one.
         */
        protected OrdinalType getOrdinalType(Object value) {
            if (value == null) return null;
            if (ordinalType == null) {
                Class valueClass = value.getClass();
                ordinalType = getTypeForClass(valueClass, true);
            }
            return ordinalType;
        }

        /**
         * Detects the Comparator for the objects of the same type as of the passed one, as defined by the OrdinalType
         * that corresponds to this type of objects.
         */
        protected Comparator getComparator(Object value) {
            if (value == null)
                throw new IllegalArgumentException("value shouldn't be null");
            if (comparator == null) {
                OrdinalType type = getOrdinalType(value);
                comparator = type.getComparator();
                if (comparator == null)
                    comparator = new Comparator() {
                        public int compare(Object o1, Object o2) {
                            return ((Comparable) o1).compareTo(o2);
                        }
                    };
            }
            return comparator;
        }

    }
}
