package org.openfaces.component.filter;

import com.sun.el.lang.ELArithmetic;
import static org.openfaces.component.filter.FilterCondition.*;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Natalia Zolochevska
 */
public enum FilterType {
    TEXT("text", EnumSet.of(EQUALS, CONTAINS, BEGINS, ENDS)),
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

    public static FilterType defineByClass(Class clazz){        
        if (ELArithmetic.isNumberType(clazz)){
            return NUMBER;
        }
        if (clazz.isAssignableFrom(Date.class)){
            return DATE;
        }
        return TEXT;
    }

}
