/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
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

/**
 * @author Dmitry Pikhulya
 */
public class TemperatureType extends OrdinalType {
    @Override
    public boolean isApplicableForClass(Class valueClass) {
        return Temperature.class.isAssignableFrom(valueClass);
    }

    @Override
    public Object add(Object value1, Object value2) {
        Temperature t1 = (Temperature) value1;
        Temperature t2 = (Temperature) value2;
        return Temperature.fromKelvinValue(t1.toKelvinValue() + t2.toKelvinValue());
    }

    @Override
    public Object divide(Object value, double by) {
        Temperature t = (Temperature) value;
        return Temperature.fromKelvinValue(t.toKelvinValue() / by);
    }
}
