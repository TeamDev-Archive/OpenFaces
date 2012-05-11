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
 * @author Eugene Goncharov
 */
public enum FillDirection {
    //todo: API revisions -- rename/unify and try to combine with other existing enums. See TabAlignment
    FROM_START("fromStart"),
    FROM_END("fromEnd");

    private String name;

    FillDirection(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
