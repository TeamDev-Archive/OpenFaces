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
    ON_FOCUS("focus"),
    ON_BLUR("blur"),
    ON_CLICK("click"),
    ON_DOUBLE_CLICK("dblclick"),
    ON_MOUSE_DOWN("mousedown"),
    ON_MOUSE_OVER("mouseover"),
    ON_MOUSE_MOVE("mousemove"),
    ON_MOUSE_UP("mouseup"),
    ON_MOUSE_OUT("mouseout"),

    ON_KEY_UP("keyup"),
    ON_KEY_DOWN("keydown"),
    ON_KEY_PRESS("keypress"),

    KEY_CTRL("ctrl"),
    KEY_V("v"),
    KEY_C("c"),

    ON_CHANGE("change");

    private String value;

    BaseActionLabels(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
