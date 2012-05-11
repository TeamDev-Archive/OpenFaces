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
package org.openfaces.util;

/**
 * @author Dmitry Pikhulya
 */
public class Enumerations {
    private Enumerations() {
    }

    public static <T extends Enum> T valueByString(Class<T> enumClass, String str, String attributeName) {
        StringBuilder allowedValues = new StringBuilder(100);
        for (T value : enumClass.getEnumConstants()) {
            String valueAsString = value.toString();
            if (valueAsString.equals(str))
                return value;
            if (allowedValues.length() > 0)
                allowedValues.append(", ");
            allowedValues.append('"').append(valueAsString).append('"');
        }
        throw new IllegalArgumentException("Invalid " + attributeName + " string: \"" + str +
                "\"; must be one of: " + allowedValues);
    }

    public static <T extends Enum> T valueByString(Class<T> enumClass, String str, T defaultValue, String attributeName) {
        if (str == null || str.length() == 0)
            return defaultValue;
        StringBuilder allowedValues = new StringBuilder(100);
        for (T value : enumClass.getEnumConstants()) {
            String valueAsString = value.toString();
            if (valueAsString.equals(str))
                return value;
            if (allowedValues.length() > 0)
                allowedValues.append(", ");
            allowedValues.append('"').append(valueAsString).append('"');
        }
        throw new IllegalArgumentException("Invalid " + attributeName + " string: \"" + str +
                "\"; must be one of: " + allowedValues);
    }

}
