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
package org.openfaces.component;

/**
 * Defines horizontal alignment of a rectangular shape relatively another rectangular shape.
 * @author Dmitry Pikhulya
 */
public enum HorizontalAlignment {
    LEFT_OUTSIDE("leftOutside"),
    LEFT("left"),
    CENTER("center"),
    RIGHT("right"),
    RIGHT_OUTSIDE("rightOutside");

    private final String name;

    HorizontalAlignment(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
