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

import org.jfree.chart.labels.ItemLabelAnchor;

/**
 * @author Eugene Goncharov
 */
public enum ChartLabelPosition {
    CENTER(ItemLabelAnchor.CENTER, "center"),
    OUTSIDE_TOP(ItemLabelAnchor.OUTSIDE12, "outsideTop"), OUTSIDE_LEFT(ItemLabelAnchor.OUTSIDE9, "outsideLeft"),
    OUTSIDE_RIGHT(ItemLabelAnchor.OUTSIDE3, "outsideRight"), OUTSIDE_BOTTOM(ItemLabelAnchor.OUTSIDE6, "outsideBottom"),
    INSIDE_TOP(ItemLabelAnchor.INSIDE12, "insideTop"), INSIDE_LEFT(ItemLabelAnchor.INSIDE9, "insideLeft"),
    INSIDE_RIGHT(ItemLabelAnchor.INSIDE3, "insideRight"), INSIDE_BOTTOM(ItemLabelAnchor.INSIDE6, "insideBottom");

    ChartLabelPosition(ItemLabelAnchor anchor, String name) {
        this.labelAnchor = anchor;
        this.name = name;
    }

    private final ItemLabelAnchor labelAnchor;
    private final String name;

    public ItemLabelAnchor getLabelAnchor() {
        return labelAnchor;
    }


    @Override
    public String toString() {
        return name;
    }
}
