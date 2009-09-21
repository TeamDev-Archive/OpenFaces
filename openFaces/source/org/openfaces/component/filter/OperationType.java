package org.openfaces.component.filter;

/**
 * @author Natalia Zolochevska
 */
public enum OperationType {

    EQUALS("org.openfaces.filter.operation.equals", "equals"),
    CONTAINS("org.openfaces.filter.operation.contains", "contains"),
    BEGINS("org.openfaces.filter.operation.begins", "begins"),
    ENDS("org.openfaces.filter.operation.ends", "ends"),
    EQ("org.openfaces.filter.operation.eq", "="),
    LE("org.openfaces.filter.operation.le", "<="),
    GE("org.openfaces.filter.operation.ge", ">="),
    GT("org.openfaces.filter.operation.gt", ">"),
    LT("org.openfaces.filter.operation.lt", "<"),
    BETWEEN("org.openfaces.filter.operation.between", "between"),
    EXACT("org.openfaces.filter.operation.select", "select"),
    CUSTOM("org.openfaces.filter.operation.custom", "custom");

    /*private final static Map<String, OperationType> stringToEnum
            = new HashMap<String, OperationType>();

    static {
        for (OperationType operationType : values())
            stringToEnum.put(operationType.toString(), operationType);
    }*/

    private final String name;
    private final String defaultLabel;

    private OperationType(String name, String defaultLabel) {
        this.name = name;
        this.defaultLabel = defaultLabel;
    }

    public String getDefaultLabel() {
        return defaultLabel;
    }

    public String getName() {
        return name;
    }

    /*public static OperationType fromString(String name) {
        return stringToEnum.get(name);
    } */


}
