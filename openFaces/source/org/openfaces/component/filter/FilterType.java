package org.openfaces.component.filter;

import static org.openfaces.component.filter.OperationType.*;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Natalia Zolochevska
 */
public enum FilterType {

    TEXT("text", EnumSet.of(EQUALS, CONTAINS, BEGINS, ENDS)),
    NUMBER("number", EnumSet.of(EQ, LE, GE, GT, LT, BETWEEN)),
    SELECT("select", EnumSet.of(EXACT)),
    DATE("date", EnumSet.of(EQ, LE, GE, GT, LT, BETWEEN));

    private final static Map<String, FilterType> stringToEnum
            = new HashMap<String, FilterType>();

    static {
        for (FilterType filterType : values())
            stringToEnum.put(filterType.toString(), filterType);
    }

    private final String name;
    private final EnumSet<OperationType> operations;

    private FilterType(String name, EnumSet<OperationType> operations) {
        this.name = name;
        this.operations = operations;
    }

    public String toString() {
        return name;
    }

     public static FilterType fromString(String name) {
        return stringToEnum.get(name);
    }

    public EnumSet<OperationType> getOperations() {
        return operations;
    }   

}
