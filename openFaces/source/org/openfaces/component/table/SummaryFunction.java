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

import java.io.Serializable;

/**
 * @author Dmitry Pikhulya
 */
public abstract class SummaryFunction implements Serializable {

    public abstract Calculator startCalculation();

    public abstract String getName();

    public abstract class Calculator {
        protected Object accumulator;
        private boolean calculationCompleted;

        public abstract void addValue(Object value);

        /**
         * The endCalculation method can be invoked only once on the same instance of Calculator.
         */
        public Object endCalculation() {
            if (calculationCompleted) throw new IllegalStateException("Calculation for this instance has already " +
                    "been completed. The endCalculation method can be invoked only once per each Calculator instance.");
            calculationCompleted = true;
            return accumulator;

        }

}
}
