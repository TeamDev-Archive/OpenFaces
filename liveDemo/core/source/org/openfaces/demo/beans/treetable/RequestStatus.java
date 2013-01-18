/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.treetable;

public enum RequestStatus {
    PENDING("Pending"),
    IN_PROGRESS("In progress"),
    COMPLETED("Completed");

    private String name;

    private RequestStatus(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public static Object fromString(String str) {
        for (RequestStatus value : values()) {
            String valueAsString = value.toString();
            if (valueAsString.equals(str))
                return value;
        }
        throw new IllegalArgumentException(str);
    }

}
