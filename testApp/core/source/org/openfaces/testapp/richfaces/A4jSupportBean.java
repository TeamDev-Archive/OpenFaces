/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.richfaces;

import java.util.Date;
import java.util.Random;

/**
 * @author Tatyana Matveyeva
 */
public class A4jSupportBean {

    private Date onchangeValue;
    private Date onperiodchangeValue;
    private int counter = 0;

    public String increase() {
        Random random = new Random();
        counter = random.nextInt(100);
        return null;
    }

    public Date getOnchangeValue() {
        return onchangeValue;
    }

    public void setOnchangeValue(Date onchangeValue) {
        this.onchangeValue = onchangeValue;
    }

    public Date getOnperiodchangeValue() {
        return onperiodchangeValue;
    }

    public void setOnperiodchangeValue(Date onperiodchangeValue) {
        this.onperiodchangeValue = onperiodchangeValue;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
