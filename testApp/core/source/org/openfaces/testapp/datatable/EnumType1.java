/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.datatable;

/**
 * @author Dmitry Pikhulya
 */
public enum EnumType1 {
    OPTION1,
    OPTION2,
    OPTION3,
    OPTION4,
    OPTION5;

    @Override
    public String toString() {
        String str = super.toString();
        return str.substring(0, 1) + str.substring(1).toLowerCase();
    }
}
