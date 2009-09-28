package org.openfaces.component.filter;

/**
 * @author Natalia Zolochevska
 */
public enum OperationType {
    EQUALS("org.openfaces.filter.operation.equals", "Equals"), // todo: why have EQUALS, EQ and EXACT?
    CONTAINS("org.openfaces.filter.operation.contains", "Contains"),
    BEGINS("org.openfaces.filter.operation.begins", "Begins with"),
    ENDS("org.openfaces.filter.operation.ends", "Ends with"),
    EQ("org.openfaces.filter.operation.eq", "="),
    LE("org.openfaces.filter.operation.le", "<="),
    GE("org.openfaces.filter.operation.ge", ">="),
    GT("org.openfaces.filter.operation.gt", ">"),
    LT("org.openfaces.filter.operation.lt", "<"),
    BETWEEN("org.openfaces.filter.operation.between", "Between"),
    EXACT("org.openfaces.filter.operation.select", "Select");

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

}
