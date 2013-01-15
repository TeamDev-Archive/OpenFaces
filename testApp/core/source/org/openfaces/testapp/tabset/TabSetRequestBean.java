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
package org.openfaces.testapp.tabset;

import java.util.Random;

/**
 * @author Darya Shumilina
 */
public class TabSetRequestBean {

    public String getFirst() {
        Random rand = new Random();
        return "Generated first value: " + rand.nextInt(37000);
    }

    public void setFirst(String first) {
    }

    public String getSecond() {
        Random rand = new Random();
        return "Generated second value: " + rand.nextInt(37000);
    }

    public void setSecond(String second) {
    }
}