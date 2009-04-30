/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.demo.beans.datatable;

import java.io.Serializable;

public class PaymentItem implements Serializable {
    private String quarter;

    private float dept1;
    private float dept2;
    private float dept3;
    private float dept4;

    public PaymentItem(String quarter, float d1, float d2, float d3, float d4) {
        this.quarter = quarter;
        dept1 = d1;
        dept2 = d2;
        dept3 = d3;
        dept4 = d4;
    }

    public float getDept1() {
        return dept1;
    }

    public float getDept2() {
        return dept2;
    }

    public float getDept3() {
        return dept3;
    }

    public float getDept4() {
        return dept4;
    }

    public String getQuarter() {
        return quarter;
    }
}
