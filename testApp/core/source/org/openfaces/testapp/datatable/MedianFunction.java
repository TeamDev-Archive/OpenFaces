/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.datatable;

import org.openfaces.component.table.OrdinalType;
import org.openfaces.component.table.SummaryFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class MedianFunction extends SummaryFunction {
    @Override
    public String getName() {
        return "Median";
    }

    @Override
    public Calculator startCalculation() {
        return new Calculator() {
            private List values = new ArrayList();

            @Override
            public void addValue(Object value) {
                values.add(value);
            }

            @Override
            public Object endCalculation() {
                super.endCalculation();
                int size = values.size();
                if (size == 0) return null;

                // sort the list
                Object sampleValue = values.get(0);
                OrdinalType ordinalType = getOrdinalType(sampleValue);
                if (sampleValue instanceof Comparable)
                    Collections.sort(values);
                else {
                    Collections.sort(values, ordinalType.getComparator());
                }

                // median is calculated as the middle item in the list for lists with an odd number of items
                // and as an average of two middle items for lists with an even number of rows
                if (size % 2 == 1) {
                    return values.get(size / 2);
                } else {
                    int middleIndex = size / 2;
                    Object value1 = values.get(middleIndex - 1);
                    Object value2 = values.get(middleIndex);

                    Object sum = ordinalType.add(value1, value2);
                    return ordinalType.divide(sum, 2);
                }
            }
        };
    }

}
