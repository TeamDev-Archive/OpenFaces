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
package org.openfaces.testapp.hintlabel;

import java.util.Random;

/**
 * @author Darya Shumilina
 */
public class HintLabelRequestBean {

    public HintLabelRequestBean() {
    }

    public String getTitle() {
        Random rand = new Random();
        return "Generated value: " + String.valueOf(rand.nextInt(37000));
    }

    public void setTitle(String title) {
    }

    public String getHint() {
        Random rand = new Random();
        return "Generated value for the hint: " + String.valueOf(rand.nextInt(37000));
    }

    public void setHint(String hint) {
    }
}