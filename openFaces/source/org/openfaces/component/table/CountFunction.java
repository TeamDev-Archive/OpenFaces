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

/**
 * @author Dmitry Pikhulya
 */
public class CountFunction extends SummaryFunction {
    @Override
    public boolean isApplicableForClass(Class valueClass) {
        return true;
    }

    @Override
    public Calculator startCalculation() {
        return new Calculator() {
            @Override
            public void addValue(Object value) {
                if (accumulator == null)
                    accumulator = 1;
                else
                    accumulator = ((Number) accumulator).intValue() + 1;
            }
        };
    }

    @Override
    public String getName() {
        return "Count";
    }
}
