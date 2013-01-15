/*
 * OpenFaces - JSF Component Library 2.0
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

/**
 * @author Dmitry Pikhulya
 */
public class Temperature implements Comparable {
    private double kelvinValue;

    public static Temperature fromKelvinValue(double kelvinValue) {
        Temperature newInstance = new Temperature();
        newInstance.kelvinValue = kelvinValue;
        return newInstance;
    }

    public static Temperature fromCelciusValue(double celciusValue) {
        Temperature newInstance = new Temperature();
        newInstance.kelvinValue = celciusValue + 273.15;
        return newInstance;
    }

    public static Temperature fromFahrenheitValue(double fahrenheitValue) {
        Temperature newInstance = new Temperature();
        newInstance.kelvinValue = (fahrenheitValue + 459.67) * 5 / 9;
        return newInstance;
    }

    public double toKelvinValue() {
        return kelvinValue;
    }

    public double toCelciusValue() {
        return kelvinValue - 273.15;
    }

    public double toFahrenheitValue() {
        return kelvinValue * 9 / 5 - 459.67;
    }

    @Override
    public String toString() {
        return String.valueOf(toCelciusValue());
    }

    public int compareTo(Object o) {
        Temperature that = (Temperature) o;
        double thisValue = this.kelvinValue;
        double thatValue = that.kelvinValue;
        if (thisValue < thatValue) return -1;
        if (thisValue > thatValue) return 1;
        return 0;
    }
}
