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

package org.openfaces.testapp.selectoneradio;

import org.openfaces.testapp.ContextUtil;

import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

/**
 * @author Vladimir Kurganov
 */
public class TestValueChangeListener1 implements ValueChangeListener {

    public void processValueChange(ValueChangeEvent event) {
        ((RadioTest) ContextUtil.resolveVariable("RadioTest")).setTestValueChangeListener1(true);
        ((RadioTest) ContextUtil.resolveVariable("RadioTestRequest")).setTestValueChangeListener1(true);
    }

}
