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

package org.openfaces.component.input;

/**
 * @author andrii.loboda
 */
public enum FileUploadMode {
    PARALLEL("parallel"),
    SEQUENTIAL("sequential");

    private String value;
    private FileUploadMode(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
