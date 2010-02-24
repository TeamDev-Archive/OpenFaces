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

import org.openfaces.component.FilterableComponent;
import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.renderkit.filter.FilterRow;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Components;
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

/**
 * @author Natalia Zolochevska
 */
public class CompositeFilter extends Filter {
    public static final String COMPONENT_FAMILY = "org.openfaces.CompositeFilter";
    public static final String COMPONENT_TYPE = "org.openfaces.CompositeFilter";
    public static final String RENDERER_TYPE = "org.openfaces.CompositeFilterRenderer";

    private static final String DEFAULT_NO_FILTER_MESSAGE = "No filter";

    private CompositeFilterCriterion value;
    private Map<String, String> labels;
    private String noFilterMessage;
    private Boolean autoDetect;

    private LinkedHashMap<Integer, FilterRow> filterRows = new LinkedHashMap<Integer, FilterRow>();
    private int lastRowIndex;
    private Converter conditionConverter;

    /**
     * not to save in state, to be cached only for request time
     */
    private List<FilterProperty> filterProperties = null;


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
                value, filterRows, lastRowIndex, noFilterMessage, labels, autoDetect, filterProperties, conditionConverter};
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
        autoDetect = (Boolean) state[i++];
        filterProperties = (List<FilterProperty>) state[i++];
        conditionConverter = (Converter) state[i++];
    }

    @Override
    public void processRestoreState(FacesContext context, Object state) {
        Object ajaxState = AjaxUtil.retrieveAjaxStateObject(context, this);
        super.processRestoreState(context, ajaxState != null ? ajaxState : state);
    }

    public Object getValue() {
        if (value != null) return value;
        ValueExpression ve = getValueExpression("value");
        return ve != null ? (CompositeFilterCriterion) ve.getValue(getFacesContext().getELContext()) : null;
    }

    public void setValue(Object value) {
        this.value = (CompositeFilterCriterion) value;
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

    public boolean getAutoDetect() {
        return ValueBindings.get(this, "autoDetect", autoDetect, true);
    }

    public void setAutoDetect(boolean autoDetect) {
        this.autoDetect = autoDetect;
    }


    private FilterProperty getColumnFilterProperty(FilterableComponent filteredComponent, BaseColumn column) {
        final TableUtil.ColumnExpressionData columnExpressionData = TableUtil.getColumnExpressionData(column);
        ValueExpression expression = columnExpressionData.getValueExpression();
        if (expression == null) return null;
        final PropertyLocator propertyLocator = new PropertyLocator(expression, filteredComponent);

        final String title = TableUtil.getColumnHeader(column);
        if (title == null) return null;

        final Class expressionType = columnExpressionData.getValueType();
        final Object dataProvider = expressionType.isEnum() ? expressionType.getEnumConstants() : null;
        final FilterType filterType = FilterType.defineByClass(expressionType);

        return new FilterProperty() {
            @Override
            public FilterType getType() {
                return filterType;
            }

            @Override
            public Object getValue() {
                return title;
            }

            @Override
            public Object getDataProvider() {
                return dataProvider;
            }

            @Override
            public PropertyLocator getPropertyLocator() {
                return propertyLocator;
            }

            @Override
            public Converter getConverter() {
                return columnExpressionData.getValueConverter();
            }
        };
    }

    private List<FilterProperty> getFilterProperties() {
        if (filterProperties == null) {
            if (getFor() != null && getAutoDetect()) {
                FilterableComponent filteredComponent = getFilteredComponent();
                if (filteredComponent instanceof AbstractTable) {
                    filterProperties = new ArrayList<FilterProperty>();
                    AbstractTable abstractTable = (AbstractTable) filteredComponent;
                    List<BaseColumn> columns = abstractTable.getAllColumns();
                    for (BaseColumn column : columns) {
                        FilterProperty filterProperty = getColumnFilterProperty(filteredComponent, column);
                        if (filterProperty != null) {
                            filterProperties.add(filterProperty);
                        }
                    }
                }
            }
            if (filterProperties == null) {
                filterProperties = Components.findChildrenWithClass(this, FilterProperty.class, true);
            }
        }
        return filterProperties;

    }


    public List<String> getFilterPropertiesTitles() {
        List<FilterProperty> filterProperties = getFilterProperties();
        List<String> result = new ArrayList<String>(filterProperties.size());
        for (FilterProperty filterProperty : filterProperties) {
            result.add((String) filterProperty.getValue());
        }
        return result;
    }

    public EnumSet<FilterCondition> getOperations(FilterProperty filterProperty) {
        FilterType filterType = filterProperty.getType();
        EnumSet<FilterCondition> operations = EnumSet.copyOf(filterType.getOperations());
        if (filterType.equals(FilterType.SELECT) && operations.contains(FilterCondition.EQUALS)) {
            if (filterProperty.getDataProvider() == null) {
                operations.remove(FilterCondition.EQUALS);
            }
        }
        return operations;
    }

    public FilterProperty getFilterPropertyByTitle(String title) {
        if (title == null) return null;
        for (FilterProperty filterProperty : getFilterProperties()) {
            if (title.equals(filterProperty.getValue())) {
                return filterProperty;
            }
        }
        return null;
    }

    public FilterProperty getFilterPropertyByPropertyLocator(PropertyLocator propertyLocator) {
        for (FilterProperty filterProperty : getFilterProperties()) {
            if (propertyLocator.equals(filterProperty.getPropertyLocator())) {
                return filterProperty;
            }
        }
        return null;
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
            ExpressionFilterCriterion criterion = filterRow.updateRowModelFromEditors(context, this);
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

    public void updateValueFromBinding(FacesContext context) {
    }

    public boolean getWantsRowList() {
        return false;
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


    public Converter getConditionConverter() {
        if (conditionConverter == null) {
            conditionConverter = new ConditionConverter(getLabels());
        }
        return conditionConverter;
    }

    public void setConditionConverter(Converter conditionConverter) {
        this.conditionConverter = conditionConverter;
    }

    private static class ConditionConverter implements Converter, Serializable {
        private Map<String, String> nameToLabelMap;
        private Map<String, FilterCondition> labelToCondition = new HashMap<String, FilterCondition>();

        private ConditionConverter(Map<String, String> nameToLabelMap) {
            this.nameToLabelMap = nameToLabelMap;
        }

        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            return labelToCondition.get(value);
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            FilterCondition filterCondition = (FilterCondition) value;
            String result = null;
            if (nameToLabelMap != null) {
                result = nameToLabelMap.get(filterCondition.getFullName());
            }
            if (result == null) {
                result = filterCondition.getDefaultLabel();
            }
            labelToCondition.put(result, filterCondition);
            return result;
        }
    }

}
