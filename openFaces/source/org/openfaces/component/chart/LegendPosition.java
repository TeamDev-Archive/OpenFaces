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

/**
 * @author Dmitry Pikhulya
 */
package org.openfaces.component.chart;

import org.jfree.ui.RectangleEdge;

public enum LegendPosition {
    TOP("top", RectangleEdge.TOP),
    BOTTOM("bottom", RectangleEdge.BOTTOM),
    LEFT("left", RectangleEdge.LEFT),
    RIGHT("right", RectangleEdge.RIGHT);

    private String name;
    private RectangleEdge rectangleEdge;

    private LegendPosition(String name, RectangleEdge rectangleEdge) {
        this.name = name;
        this.rectangleEdge = rectangleEdge;
    }

    @Override
    public String toString() {
        return name;
    }

    public RectangleEdge toRectangleEdge() {
        return rectangleEdge;
    }

}
