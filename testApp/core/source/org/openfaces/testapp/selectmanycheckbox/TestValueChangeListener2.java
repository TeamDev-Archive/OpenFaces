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

package org.openfaces.testapp.selectmanycheckbox;

import org.openfaces.testapp.ContextUtil;
import org.openfaces.testapp.selectoneradio.RadioTest;

import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

/**
 * @author Oleg Marshalenko
 */
public class TestValueChangeListener2 implements ValueChangeListener {

    public void processValueChange(ValueChangeEvent event) {
        ((ManyCheckboxTest) ContextUtil.resolveVariable("ManyCheckboxTest")).setTestValueChangeListener2(true);
        ((ManyCheckboxTest) ContextUtil.resolveVariable("ManyCheckboxTestRequest")).setTestValueChangeListener2(true);
    }

}
