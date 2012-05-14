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

import org.jfree.ui.LengthAdjustmentType;

/**
 * @author Eugene Goncharov
 */
public enum MarkerLabelOffsetType {
    NO_CHANGE(LengthAdjustmentType.NO_CHANGE, "noChange"),
    EXPAND(LengthAdjustmentType.EXPAND, "expand"),
    CONTRACT(LengthAdjustmentType.CONTRACT, "contract");

    private final LengthAdjustmentType offsetType;
    private final String name;

    MarkerLabelOffsetType(LengthAdjustmentType offsetType, String name) {
        this.offsetType = offsetType;
        this.name = name;
    }

    public LengthAdjustmentType getOffsetType() {
        return offsetType;
    }

    @Override
    public String toString() {
        return name;
    }
}
