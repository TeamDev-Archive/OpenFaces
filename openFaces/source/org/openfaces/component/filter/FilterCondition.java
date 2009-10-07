package org.openfaces.component.filter;

/**
 * @author Natalia Zolochevska
 */
public enum FilterCondition {
    EMPTY("empty", "Empty"),
    EQUALS("equals", "Equals"),
    CONTAINS("contains", "Contains"),
    BEGINS("begins", "Begins with"),
    ENDS("ends", "Ends with"),
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
            case BEGINS:
                return processor.processBegins();
            case ENDS:
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
