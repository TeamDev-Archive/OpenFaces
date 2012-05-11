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

package org.openfaces.component.filter;

import static org.openfaces.component.filter.FilterCondition.*;
import org.openfaces.util.ReflectionUtil;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Natalia Zolochevska
 */
public enum FilterType {
    TEXT("text", EnumSet.of(EQUALS, CONTAINS, BEGINS_WITH, ENDS_WITH)),
    NUMBER("number", EnumSet.of(EQUALS, LESS_OR_EQUAL, GREATER_OR_EQUAL, GREATER, LESS, BETWEEN)),
    SELECT("select", EnumSet.of(EQUALS)),
    DATE("date", EnumSet.of(EQUALS, LESS_OR_EQUAL, GREATER_OR_EQUAL, GREATER, LESS, BETWEEN));

    private final static Map<String, FilterType> stringToEnum = new HashMap<String, FilterType>();

    static {
        for (FilterType filterType : values())
            stringToEnum.put(filterType.toString(), filterType);
    }

    private final String name;
    private final EnumSet<FilterCondition> operations;

    private FilterType(String name, EnumSet<FilterCondition> operations) {
        this.name = name;
        this.operations = operations;
    }

    public String toString() {
        return name;
    }

    public static FilterType fromString(String name) {
        return stringToEnum.get(name);
    }

    public EnumSet<FilterCondition> getOperations() {
        return operations;
    }

    public static FilterType defineByClass(Class clazz) {
        if (ReflectionUtil.isNumberType(clazz)) {
            return NUMBER;
        }
        if (Date.class.isAssignableFrom(clazz)) {
            return DATE;
        }
        if (clazz.isEnum()) {
            return SELECT;
        }
        return TEXT;
    }

}
