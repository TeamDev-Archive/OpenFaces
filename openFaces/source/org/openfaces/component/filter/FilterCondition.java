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

package org.openfaces.component.filter;

/**
 * This enumeration defines a list of filter conditions that can be used by the filter components. 
 *
 * @author Natalia Zolochevska
 */
public enum FilterCondition {
    EMPTY("empty", "Empty"),
    EQUALS("equals", "Equals"),
    CONTAINS("contains", "Contains"),
    BEGINS_WITH("beginsWith", "Begins with"),
    ENDS_WITH("endsWith", "Ends with"),
    LESS("less", "<"),
    GREATER("greater", ">"),
    LESS_OR_EQUAL("lessOrEqual", "<="),
    GREATER_OR_EQUAL("greaterOrEqual", ">="),
    BETWEEN("between", "Between");

    private final String name;
    private final String defaultLabel;

    private FilterCondition(String name, String defaultLabel) {
        this.name = name;
        this.defaultLabel = defaultLabel;
    }

    public String getDefaultLabel() {
        return defaultLabel;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return "org.openfaces.filter.condition." + name;
    }

    public <T> T process(FilterConditionProcessor<T> processor) {
        switch (this) {
            case EMPTY:
                return processor.processEmpty();
            case EQUALS:
                return processor.processEquals();
            case CONTAINS:
                return processor.processContains();
            case BEGINS_WITH:
                return processor.processBegins();
            case ENDS_WITH:
                return processor.processEnds();
            case LESS:
                return processor.processLess();
            case GREATER:
                return processor.processGreater();
            case LESS_OR_EQUAL:
                return processor.processLessOrEqual();
            case GREATER_OR_EQUAL:
                return processor.processGreaterOrEqual();
            case BETWEEN:
                return processor.processBetween();
            default:
                throw new IllegalStateException("Unknown filter condition: " + this);
        }
    }

}
