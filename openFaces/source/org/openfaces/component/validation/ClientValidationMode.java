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

/**
 * @author Kharchenko
 */
package org.openfaces.component.validation;

import org.openfaces.util.Enumerations;

public enum ClientValidationMode {
    OFF("off"),
    ON_SUBMIT("onSubmit"),
    ON_DEMAND("onDemand");

    private final String name;

    ClientValidationMode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static ClientValidationMode fromString(String value) {
        return Enumerations.valueByString(ClientValidationMode.class, value, null, "clientValidation");
    }
}
