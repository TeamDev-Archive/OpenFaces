/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2015, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.inspector.css;

/**
 * @author Max Yurin
 */
public enum BaseActionLabels {
    ON_FOCUS("onfocus"),
    ON_BLUR("onblur"),
    ON_CLICK("onclick"),
    ON_DOUBLE_CLICK("ondblclick"),
    ON_MOUSE_DOWN("onmousedown"),
    ON_MOUSE_OVER("onmouseover"),
    ON_MOUSE_MOVE("onmousemove"),
    ON_MOUSE_UP("onmouseup"),
    ON_MOUSE_OUT("onmouseout"),

    ON_KEY_UP("onkeyup"),
    ON_KEY_DOWN("onkeydown"),
    ON_KEY_PRESS("onkeypress"),

    KEY_CTRL("ctrl"),
    KEY_V("v"),
    KEY_C("c"),

    ON_CHANGE("onchange");

    private String value;

    BaseActionLabels(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
