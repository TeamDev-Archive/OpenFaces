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

/**
 * @author Ekaterina Shliakhovetskaya
 */
public enum CategoryAxisLabelPosition {
    STANDARD("standard"),
    UP_90("up_90"),
    UP_45("up_45"),
    DOWN_90("down_90"),
    DOWN_45("down_45");

    private String name;

    CategoryAxisLabelPosition(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
