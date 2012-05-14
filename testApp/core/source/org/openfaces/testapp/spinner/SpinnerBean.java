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

package org.openfaces.testapp.spinner;

import java.math.BigDecimal;

/**
 * @author Alexander Golubev
 */
public class SpinnerBean {

    private int value = 1;
    private float value1 = 0;
    private long value2 =10000;
    private BigDecimal value3 = new BigDecimal(0);
    private Long value4 = 0L;
    private Number value5 = null;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public float getValue1() {
        return value1;
    }

    public void setValue1(float value1) {
        this.value1 = value1;
    }

    public long getValue2() {
        return value2;
    }

    public void setValue2(long value2) {
        this.value2 = value2;
    }

    public BigDecimal getValue3() {
        return value3;
    }

    public void setValue3(BigDecimal value3) {
        this.value3 = value3;
    }

    public Long getValue4() {
        return value4;
    }

    public void setValue4(Long value4) {
        this.value4 = value4;
    }

    public Number getValue5() {
        return value5;
    }

    public void setValue5(Number value5) {
        this.value5 = value5;
    }
}
