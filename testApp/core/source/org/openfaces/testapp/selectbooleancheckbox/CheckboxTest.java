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
package org.openfaces.testapp.selectbooleancheckbox;

import java.util.Arrays;

/**
 * @author Dmitry Pikhulya
 */
public class CheckboxTest {

    private boolean state1;
    private Boolean state2;
    private Iterable<String> states= Arrays.asList("undefined", "selected");

    public boolean isState1() {
        return state1;
    }

    public void setState1(boolean state1) {
        this.state1 = state1;
    }

    public Boolean getState2() {
        return state2;
    }

    public void setState2(Boolean state2) {
        this.state2 = state2;
    }

    public Iterable<String> getStates() {
        return states;
    }

    public void setStates(Iterable<String> states) {
        this.states = states;
    }
}
