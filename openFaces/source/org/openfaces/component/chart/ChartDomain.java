/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

/**
 * @author Ekaterina Shliakhovetskaya
 */
package org.openfaces.component.chart;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public enum ChartDomain {
    KEY("key"),
    VALUE("value"),
    BOTH("both"),
    NONE("none");

    private String name;

    ChartDomain(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
