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
public enum Border {
    ALL("border"),
    BORDER_RIGHT("border-right"),
    BORDER_LEFT("border-left"),
    BORDER_TOP("border-top"),
    BORDER_BOTTOM("border-bottom");

    private String value;

    Border(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
