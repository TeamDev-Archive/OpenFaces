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

package org.openfaces.testapp.output;

import org.openfaces.component.output.HintLabel;

import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;

/**
 * @author Andrew Palval
 */
public class OutputTestBean {
    String value = "It's required to make the following call in the beginning of encodeBegin method. It will register the ajaxUtil.js library and make sure that there are no non-transient components for AJAX to work correctly.";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void textValue(ValueChangeEvent event) {
        UIComponent component = event.getComponent();
        HintLabel testComponent = (HintLabel) component.findComponent("a1");
        testComponent.setValue(event.getNewValue());
    }

}
