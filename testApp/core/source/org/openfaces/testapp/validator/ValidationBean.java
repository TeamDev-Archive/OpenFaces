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
package org.openfaces.testapp.validator;

import java.io.Serializable;
import java.util.Date;

public class ValidationBean implements Serializable {
    private int integerValue;
    private int integerValue2;
    private double doubleValue;
    private byte byteValue;
    private short shortValue;
    private float floatValue;
    private Date dateValue1;
    private boolean firstLoad = true;
    private boolean firstLoad2 = true;

    public ValidationBean() {
        integerValue = 10;
        doubleValue = 10.04;
        byteValue = 127;
        shortValue = 32767;
        floatValue = 123;
    }

    public int getIntegerValue() {
        return integerValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public byte getByteValue() {
        return byteValue;
    }

    public short getShortValue() {
        return shortValue;
    }

    public void setIntegerValue(int integerValue) {
        this.integerValue = integerValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public void setByteValue(byte byteValue) {
        this.byteValue = byteValue;
    }

    public void setShortValue(short shortValue) {
        this.shortValue = shortValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public Date getDateValue1() {
        return dateValue1;
    }

    public void setDateValue1(Date dateValue1) {
        this.dateValue1 = dateValue1;
    }

    public int getIntegerValue2() {
        return integerValue2;
    }

    public void setIntegerValue2(int integerValue2) {
        this.integerValue2 = integerValue2;
    }

    public boolean isFirstLoad() {
        return firstLoad;
    }

    public void setFirstLoad(boolean firstLoad) {
        this.firstLoad = firstLoad;
    }

    public boolean isFirstLoad2() {
        return firstLoad2;
    }

    public void setFirstLoad2(boolean firstLoad2) {
        this.firstLoad2 = firstLoad2;
    }
}