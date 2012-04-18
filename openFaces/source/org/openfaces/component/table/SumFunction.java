/*
 * OpenFaces - JSF Component Library 2.0
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
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class SumFunction extends SummaryFunction {

    @Override
    public Calculator startCalculation() {
        return new Calculator() {
            private OrdinalType ordinalType;

            @Override
            public void addValue(Object value) {
                if (value == null) return;

                if (ordinalType == null) {
                    Class valueClass = value.getClass();
                    ordinalType = getTypeForClass(valueClass);
                }

                if (accumulator == null) {
                    accumulator = ordinalType.getZero();
                    if (accumulator == null)
                        throw new FacesException("This operation (" + getClass().getName() +
                                ") is not applicable to this type of objects (" + ordinalType.getClass().getName() + ")");
                }

                accumulator = ordinalType.add(accumulator, value);
            }
        };
    }

    @Override
    public String getName() {
        return "Sum";
    }

    private OrdinalType getTypeForClass(Class valueClass) {
        if (valueClass == null)
            throw new IllegalArgumentException("valueClass shouldn't be null");
        List<OrdinalType> ordinalTypes = ApplicationParams.getOrdinalTypes();
        for (OrdinalType type : ordinalTypes) {
            if (type.isApplicableForClass(valueClass))
                return type;
        }
        throw new FacesException("Unsupported object type for summary calculation: " + valueClass.getName() +
                "; could not find an appropriate OrdinalType implementation that describes operations on " +
                "this type of objects.");
    }
}
