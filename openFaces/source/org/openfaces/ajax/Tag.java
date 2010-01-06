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

package org.openfaces.ajax;

/**
 * Enumeration for HTML tag constants
 *
 * @author Alexander Yevsyukov
 */
public enum Tag {
    HEAD("head"),
    SCRIPT("script");

    public final String name;
    public final String start;
    public final String end;

    Tag(String name) {
        this.name = name;
        this.start = '<' + name + '>';
        this.end = "</" + name + '>';
    }

    /**
     * @return name of the tag
     */
    @Override
    public String toString() {
        return name;
    }
}
