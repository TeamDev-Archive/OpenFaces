package org.openfaces.component.filter;

import org.openfaces.component.OUIComponentBase;
import org.openfaces.component.filter.criterion.AndFilterCriterion;
import org.openfaces.component.filter.criterion.CompositeFilterCriterion;
import org.openfaces.component.filter.criterion.OrFilterCriterion;
import org.openfaces.component.filter.criterion.PropertyFilterCriterion;
import org.openfaces.component.filter.criterion.PropertyLocator;
import org.openfaces.renderkit.filter.FilterRow;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.ValueBindings;
import org.openfaces.util.ValueExpressionImpl;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Natalia Zolochevska
 */
public class CompositeFilter extends OUIComponentBase {
    public static final String COMPONENT_FAMILY = "org.openfaces.CompositeFilter";
    public static final String COMPONENT_TYPE = "org.openfaces.CompositeFilter";
    public static final String RENDERER_TYPE = "org.openfaces.CompositeFilterRenderer";

    private static final String DEFAULT_NO_FILTER_MESSAGE = "No filter";

    private LinkedHashMap<Integer, FilterRow> filterRows = new LinkedHashMap<Integer, FilterRow>();
    private int lastRowIndex;
    private String noFilterMessage;
    private CompositeFilterCriterion value;
    private Map<String, String> labels;
    private Converter operationConverter;

    /**
     * not to save in state, to be cached only for request time
     */
    private LinkedHashMap<String, FilterProperty> filterPropertiesMap = null;
    private LinkedHashMap<String, FilterProperty> filterPropertyNamesMap = null;


    public CompositeFilter() {
        setRendererType(RENDERER_TYPE);
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState,
                value, filterRows, lastRowIndex, noFilterMessage, labels, operationConverter};
    }


    @Override
    @SuppressWarnings("unchecked")
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        value = (CompositeFilterCriterion) state[i++];
        filterRows = (LinkedHashMap<Integer, FilterRow>) state[i++];
        lastRowIndex = (Integer) state[i++];
        noFilterMessage = (String) state[i++];
        labels = (Map<String, String>) state[i++];
        operationConverter = (Converter) state[i++];
    }

    @Override
    public void processRestoreState(FacesContext context, Object state) {
        Object ajaxState = AjaxUtil.retrieveAjaxStateObject(context, this);
        super.processRestoreState(context, ajaxState != null ? ajaxState : state);
    }

    public CompositeFilterCriterion getValue() {
        if (value != null) return value;
        ValueExpression ve = getValueExpression("value");
        return ve != null ? (CompositeFilterCriterion) ve.getValue(getFacesContext().getELContext()) : null;
    }

    public void setValue(CompositeFilterCriterion value) {
        this.value = value;
    }

    public String getNoFilterMessage() {
        return ValueBindings.get(this, "noFilterMessage", noFilterMessage, DEFAULT_NO_FILTER_MESSAGE);
    }

    public void setNoFilterMessage(String noFilterMessage) {
        this.noFilterMessage = noFilterMessage;
    }

    private Map<String, FilterProperty> getFilterPropertyNamesMap() {
        if (filterPropertyNamesMap == null) {
            Collection<UIComponent> children = getChildren();
            filterPropertyNamesMap = new LinkedHashMap<String, FilterProperty>(children.size());
            for (UIComponent child : children) {
                if (child instanceof FilterProperty) {
                    FilterProperty filterProperty = (FilterProperty) child;
                    filterPropertyNamesMap.put(filterProperty.getName(), filterProperty);
                }
            }
        }
        return filterPropertyNamesMap;
    }

    private Map<String, FilterProperty> getFilterPropertiesMap() {
        if (filterPropertiesMap == null) {
            Collection<UIComponent> children = getChildren();
            filterPropertiesMap = new LinkedHashMap<String, FilterProperty>(children.size());
            for (UIComponent child : children) {
                if (child instanceof FilterProperty) {
                    FilterProperty filterProperty = (FilterProperty) child;
                    filterPropertiesMap.put((String) filterProperty.getValue(), filterProperty);
                }
            }
        }
        return filterPropertiesMap;
    }

    public List<String> getProperties() {
        Map<String, FilterProperty> propertiesMap = getFilterPropertiesMap();
        Set<String> propertyValues = propertiesMap.keySet();
        List<String> result = new ArrayList<String>(propertyValues.size());
        for (String propertyValue : propertyValues) {
            FilterProperty filterProperty = propertiesMap.get(propertyValue);
            if (filterProperty.isRendered()) {
                result.add(propertyValue);
            }
        }
        return result;
    }

    public EnumSet<OperationType> getOperations(FilterProperty filterProperty) {
        FilterType filterType = filterProperty.getType();
        EnumSet<OperationType> operations = EnumSet.copyOf(filterType.getOperations());
        if (filterType.equals(FilterType.SELECT) && operations.contains(OperationType.EQUALS)) {
            if (filterProperty.getDataProvider() == null) {
                operations.remove(OperationType.EQUALS);
            }
        }
        return operations;
    }

    public FilterProperty getFilterProperty(String property) {
        Map<String, FilterProperty> propertiesMap = getFilterPropertiesMap();
        FilterProperty result = propertiesMap.get(property);
        return result;
    }

    public FilterProperty getFilterPropertyByName(String propertyName) {
        Map<String, FilterProperty> propertyNamesMap = getFilterPropertyNamesMap();
        FilterProperty result = propertyNamesMap.get(propertyName);
        return result;
    }

    public FilterRow getLastRow() {
        FilterRow result = null;
        for (FilterRow row : filterRows.values()) {
            result = row;
        }
        return result;
    }


    public FilterRow addFilterRow() {
        lastRowIndex++;
        FilterRow filterRow = new FilterRow(lastRowIndex);
        filterRow.setLastRow(true);
        FilterRow previousLastRow = getLastRow();
        filterRows.put(lastRowIndex, filterRow);
        if (previousLastRow != null) {
            previousLastRow.setLastRow(false);
        }
        return filterRow;
    }

    public void removeFilterRow(int index) {
        FilterRow filterRow = filterRows.remove(index);
        filterRow.removeInlineComponents(this);
        FilterRow lastRow = getLastRow();
        if (lastRow != null) {
            lastRow.setLastRow(true);
        }
    }

    public FilterRow getFilterRow(int index) {
        return filterRows.get(index);
    }

    public void clear() {
        for (FilterRow filterRow : getFilterRows()) {
            filterRow.removeInlineComponents(this);
        }
        filterRows.clear();
    }

    public void processUpdates(FacesContext context) {
        super.processUpdates(context);
        
        Map<PropertyLocator, List<FilterCriterion>> propertiesToCriterions =
                new LinkedHashMap<PropertyLocator, List<FilterCriterion>>();

        for (FilterRow filterRow : filterRows.values()) {
            PropertyFilterCriterion criterion = filterRow.updateRowModelFromEditors(context, this);
            if (criterion == null)
                continue;
            PropertyLocator property = criterion.getPropertyLocator();
            List<FilterCriterion> list = propertiesToCriterions.get(property);
            if (list == null) {
                list = new ArrayList<FilterCriterion>();
                propertiesToCriterions.put(property, list);
            }
            list.add(criterion);
        }

        AndFilterCriterion andCriterion = new AndFilterCriterion();
        for (List<FilterCriterion> list : propertiesToCriterions.values()) {
            FilterCriterion filterCriterion;
            if (list.size() == 1) {
                filterCriterion = list.get(0);
            } else {
                filterCriterion = new OrFilterCriterion(list);
            }
            andCriterion.getCriteria().add(filterCriterion);
        }

        if (!ValueBindings.set(this, "value", andCriterion))
            value = andCriterion;
    }

    public Collection<FilterRow> getFilterRows() {
        return filterRows.values();
    }

    public boolean isEmpty() {
        return filterRows.isEmpty();
    }

    public ValueExpression getNoFilterRowRendererExpression() {
        return new ValueExpressionImpl() {
            public Object getValue(ELContext elContext) {
                return isEmpty();
            }
        };
    }

    public Map<String, String> getLabels() {
        if (labels != null) return labels;
        ValueExpression ve = getValueExpression("labels");
        return ve != null ? (Map<String, String>) ve.getValue(getFacesContext().getELContext()) : null;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public Converter getOperationConverter() {
        if (operationConverter == null) {
            operationConverter = new OperationConverter(getLabels());
        }
        return operationConverter;
    }

    public void setOperationConverter(Converter operationConverter) {
        this.operationConverter = operationConverter;
    }

    private static class OperationConverter implements Converter, Serializable {
        private Map<String, String> nameToLabelMap;
        private Map<String, OperationType> labelToOperation = new HashMap<String, OperationType>();

        private OperationConverter(Map<String, String> nameToLabelMap) {
            this.nameToLabelMap = nameToLabelMap;
        }

        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            return labelToOperation.get(value);
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            OperationType operationType = (OperationType) value;
            String result = null;
            if (nameToLabelMap != null) {
                result = nameToLabelMap.get(operationType.getName());
            }
            if (result == null) {
                result = operationType.getDefaultLabel();
            }
            labelToOperation.put(result, operationType);
            return result;
        }
    }

}
