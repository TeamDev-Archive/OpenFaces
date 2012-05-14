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

package org.openfaces.component.chart;

import org.jfree.ui.RectangleAnchor;

/**
 * @author Eugene Goncharov
 */
public enum MarkerLabelAnchor {
    CENTER(RectangleAnchor.CENTER, "center"),
    TOP(RectangleAnchor.TOP, "top"),
    TOP_LEFT(RectangleAnchor.TOP_LEFT, "topLeft"),
    TOP_RIGHT(RectangleAnchor.TOP_RIGHT, "topRight"),
    BOTTOM(RectangleAnchor.BOTTOM, "bottom"),
    BOTTOM_LEFT(RectangleAnchor.BOTTOM_LEFT, "bottomLeft"),
    BOTTOM_RIGHT(RectangleAnchor.BOTTOM_RIGHT, "bottomRight"),
    LEFT(RectangleAnchor.LEFT, "left"),
    RIGHT(RectangleAnchor.RIGHT, "right");

    private final RectangleAnchor anchor;
    private final String name;

    MarkerLabelAnchor(RectangleAnchor anchor, String name) {
        this.anchor = anchor;
        this.name = name;
    }

    public RectangleAnchor getAnchor() {
        return anchor;
    }

    @Override
    public String toString() {
        return name;
    }
}
