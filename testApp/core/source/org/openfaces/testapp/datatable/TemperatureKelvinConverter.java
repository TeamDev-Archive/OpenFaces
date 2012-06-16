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

package org.openfaces.testapp.datatable;

import javax.faces.convert.Converter;

/**
 * @author Dmitry Pikhulya
 */
public class TemperatureKelvinConverter extends AbstractTemperatureConverter {
    @Override
    protected double toNumberValue(Temperature t) {
        return t.toKelvinValue();
    }

    @Override
    protected Temperature fromNumberValue(double d) {
        return Temperature.fromKelvinValue(d);
    }

    @Override
    protected String getUnitSuffix() {
        return " °K";
    }
}
