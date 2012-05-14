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

import org.jfree.ui.TextAnchor;

/**
 * @author Eugene Goncharov
 */
public enum MarkerLabelTextAnchor {
    TOP_LEFT(TextAnchor.TOP_LEFT, "topLeft"),
    TOP_CENTER(TextAnchor.TOP_CENTER, "topCenter"),
    TOP_RIGHT(TextAnchor.TOP_RIGHT, "topRight"),
    HALF_ASCENT_LEFT(TextAnchor.HALF_ASCENT_LEFT, "halfAscentLeft"),
    HALF_ASCENT_CENTER(TextAnchor.HALF_ASCENT_CENTER, "halfAscentCenter"),
    HALF_ASCENT_RIGHT(TextAnchor.HALF_ASCENT_RIGHT, "halfAscentRight"),
    CENTER_LEFT(TextAnchor.CENTER_LEFT, "centerLeft"),
    CENTER(TextAnchor.CENTER, "center"),
    CENTER_RIGHT(TextAnchor.CENTER_RIGHT, "centerRight"),
    BASELINE_LEFT(TextAnchor.BASELINE_LEFT, "baselineLeft"),
    BASELINE_CENTER(TextAnchor.BASELINE_CENTER, "baselineCenter"),
    BASELINE_RIGHT(TextAnchor.BASELINE_RIGHT, "baselineRight"),
    BOTTOM_LEFT(TextAnchor.BOTTOM_LEFT, "bottomLeft"),
    BOTTOM_CENTER(TextAnchor.BOTTOM_CENTER, "bottomCenter"),
    BOTTOM_RIGHT(TextAnchor.BOTTOM_RIGHT, "bottomRight");

    private final org.jfree.ui.TextAnchor anchor;
    private final String name;

    MarkerLabelTextAnchor(TextAnchor anchor, String name) {
        this.anchor = anchor;
        this.name = name;
    }

    public TextAnchor getAnchor() {
        return anchor;
    }

    @Override
    public String toString() {
        return name;
    }
}
