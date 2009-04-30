/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.table;

import org.openfaces.component.CompoundComponent;
import org.openfaces.component.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Dmitry Pikhulya
 */
public abstract class DataTableFilter extends UIComponentBase implements CompoundComponent {
    private static final String DEFAULT_ALL_RECORDS_CRITERION_NAME = "<All>";
    private static final String DEFAULT_EMPTY_RECORDS_CRITERION_NAME = "<Empty>";
    private static final String DEFAULT_NON_EMPTY_RECORDS_CRITERION_NAME = "<Non-empty>";

    private String style;
    private String styleClass;
    private String predefinedCriterionStyle;
    private String predefinedCriterionClass;
    private AbstractTable table;
    protected String allRecordsCriterionName;
    protected String emptyRecordsCriterionName;
    protected String nonEmptyRecordsCriterionName;
    private FilterCriterion searchString;
    private boolean searchStringModelUpdateRequired;

    private String promptText;

    private String promptTextStyle;
    private String promptTextClass;

    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, style, styleClass, predefinedCriterionStyle, predefinedCriterionClass,
                searchString, allRecordsCriterionName, emptyRecordsCriterionName, nonEmptyRecordsCriterionName,
                promptText, promptTextStyle, promptTextClass};
    }

    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        style = (String) state[i++];
        styleClass = (String) state[i++];
        predefinedCriterionStyle = (String) state[i++];
        predefinedCriterionClass = (String) state[i++];
        searchString = (FilterCriterion) state[i++];
        allRecordsCriterionName = (String) state[i++];
        emptyRecordsCriterionName = (String) state[i++];
        nonEmptyRecordsCriterionName = (String) state[i++];

        promptText = (String) state[i++];
        promptTextStyle = (String) state[i++];
        promptTextClass = (String) state[i++];
    }

    public String getStyle() {
        return ValueBindings.get(this, "style", style);
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return ValueBindings.get(this, "styleClass", styleClass);
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getPredefinedCriterionStyle() {
        return ValueBindings.get(this, "predefinedCriterionStyle", predefinedCriterionStyle);
    }

    public void setPredefinedCriterionStyle(String style) {
        predefinedCriterionStyle = style;
    }

    public String getPredefinedCriterionClass() {
        return ValueBindings.get(this, "predefinedCriterionClass", predefinedCriterionClass);
    }

    public void setPredefinedCriterionClass(String styleClass) {
        predefinedCriterionClass = styleClass;
    }

    public String getPromptText() {
        return ValueBindings.get(this, "promptText", promptText);
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    public String getPromptTextStyle() {
        return ValueBindings.get(this, "promptTextStyle", promptTextStyle);
    }

    public void setPromptTextStyle(String promptTextStyle) {
        this.promptTextStyle = promptTextStyle;
    }

    public String getPromptTextClass() {
        return ValueBindings.get(this, "promptTextClass", promptTextClass);
    }

    public void setPromptTextClass(String promptTextClass) {
        this.promptTextClass = promptTextClass;
    }

    public boolean acceptsData(FacesContext facesContext, Object data) {
        String filteredValueString = getFilteredValueStringByData(facesContext, getTable(), data);
        return acceptsString(filteredValueString, getSearchString());
    }

    private String getFilteredValueStringByData(FacesContext facesContext, AbstractTable table, Object data) {
        ValueExpression criterionNameExpression = getFilterValuesExpressionExpression();
        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        String var = getTable().getVar();
        return getFilteredValueStringByData(table, facesContext, criterionNameExpression, requestMap, var, data);
    }

    abstract protected boolean acceptsString(String filteredValueString, FilterCriterion searchString);

    public boolean isAcceptingAllRecords() {
        return getSearchString() == null;
    }

    protected AbstractTable getTable() {
        if (table == null) {
            UIComponent component = getParent();
            while (component != null && !(component instanceof AbstractTable))
                component = component.getParent();
            table = (AbstractTable) component;
        }
        return table;
    }

    public ValueExpression getFilterValuesExpressionExpression() {
        return getValueExpression("filterValuesExpression");
    }

    public void setFilterValuesExpressionExpression(ValueExpression expression) {
        setValueExpression("filterValuesExpression", expression);
    }

    public String getAllRecordsCriterionName() {
        String result = ValueBindings.get(this, "allRecordsCriterionName", allRecordsCriterionName);
        if (result == null)
            result = DEFAULT_ALL_RECORDS_CRITERION_NAME;
        return result;
    }

    public void setAllRecordsCriterionName(String allRecordsCriterionName) {
        this.allRecordsCriterionName = allRecordsCriterionName;
    }

    public String getEmptyRecordsCriterionName() {
        String result = ValueBindings.get(this, "emptyRecordsCriterionName", emptyRecordsCriterionName);
        if (result == null)
            result = DEFAULT_EMPTY_RECORDS_CRITERION_NAME;
        return result;
    }

    public void setEmptyRecordsCriterionName(String value) {
        emptyRecordsCriterionName = value;
    }

    public String getNonEmptyRecordsCriterionName() {
        String result = ValueBindings.get(this, "nonEmptyRecordsCriterionName", nonEmptyRecordsCriterionName);
        if (result == null)
            result = DEFAULT_NON_EMPTY_RECORDS_CRITERION_NAME;
        return result;
    }

    public void setNonEmptyRecordsCriterionName(String value) {
        nonEmptyRecordsCriterionName = value;
    }

    protected String getFilteredValueStringByData(
            AbstractTable table,
            FacesContext facesContext,
            ValueExpression criterionNameExpression,
            Map<String, Object> requestMap,
            String var,
            Object data) {
        Object filterValueForRow = table.getFilteredValueByData(facesContext, requestMap, criterionNameExpression, var, data);
        return filterValueToString(filterValueForRow);
    }

    private String filterValueToString(Object filterValue) {
        String result = (filterValue == null) ? "" : filterValue.toString();
        return result.trim();
    }

    public ValueExpression getFilterValuesExpression() {
        return getValueExpression("filterValues");
    }

    public void setFilterValuesExpression(ValueExpression valuesBinding) {
        setValueExpression("filterValues", valuesBinding);
    }

    public boolean getWantsRowList() {
        if (!isShowingPredefinedCriterionNames())
            return false;
        ValueExpression filterValuesExpression = getFilterValuesExpression();
        return filterValuesExpression == null;
    }

    protected boolean isShowingPredefinedCriterionNames() {
        return false;
    }

    public Collection<String> calculateAllCriterionNames(FacesContext context) {
        ValueExpression valuesExpression = getFilterValuesExpression();
        if (valuesExpression != null) {
            List<String> result = new ArrayList<String>();
            Collection values = (Collection) valuesExpression.getValue(context.getELContext());
            if (values != null) {
                for (Object value : values) {
                    String valueString = filterValueToString(value);
                    result.add(valueString);
                }
            }
            return result;
        }
        ValueExpression expression = getFilterValuesExpressionExpression();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        String var = getTable().getVar();
        Set<String> criterionNamesSet = new TreeSet<String>();
        List originalRowList = getTable().getRowListForFiltering(this);
        AbstractTable table = getTable();
        for (Object data : originalRowList) {
            String criterionName = getFilteredValueStringByData(table, context, expression, requestMap, var, data);
            criterionNamesSet.add(criterionName);
        }
        return criterionNamesSet;
    }

    public void updateSearchStringFromBinding(FacesContext context) {
        ValueExpression valueExpression = getSearchStringExpression();
        if (valueExpression != null)
            searchString = (FilterCriterion) valueExpression.getValue(context.getELContext());
    }

    public FilterCriterion getSearchString() {
        return searchString;
    }

    public void setSearchString(FilterCriterion searchString) {
        this.searchString = searchString;
    }

    /**
     * @param newCriterion new search criterion
     * @return true if the new criterion results in the different filtering behavior as opposed to this filter's previous
     *         criterion
     */
    public boolean changeSearchString(FilterCriterion newCriterion) {
        FilterCriterion oldCriterion = getSearchString();
        setColumnId(newCriterion);
        searchStringModelUpdateRequired = true;

        if (isAllRecordsCriterion(newCriterion)) {
            if (isAllRecordsCriterion(oldCriterion))
                return false;
        } else if (newCriterion.equals(oldCriterion))
            return false;
        setSearchString(newCriterion);
        return true;
    }

    private boolean isAllRecordsCriterion(FilterCriterion criterion) {
        if (criterion == null)
            return true;

        return criterion instanceof TextFilterCriterion && ((TextFilterCriterion) criterion).getText().length() == 0;
    }

    public void setSearchStringExpression(ValueExpression filterValueExpression) {
        setValueExpression("searchString", filterValueExpression);
    }

    public ValueExpression getSearchStringExpression() {
        return getValueExpression("searchString");
    }

    public void processUpdates(FacesContext context) {
        super.processUpdates(context);
        if (!searchStringModelUpdateRequired)
            return;

        ValueExpression valueExpression = getSearchStringExpression();
        if (valueExpression != null) {
            valueExpression.setValue(context.getELContext(), searchString);
            searchStringModelUpdateRequired = false;
        }
    }

    public FilterCriterion getFilterCriterion() {
        FilterCriterion searchString = getSearchString();
        setColumnId(searchString);
        return searchString;
    }

    private void setColumnId(FilterCriterion searchString) {
        if (searchString instanceof ColumnFilterCriterion) {
            ((ColumnFilterCriterion) searchString).setColumnId(getColumnId());
        }
    }

    protected String getColumnId() {
        TableColumn col = (TableColumn) getParent();
        return col.getId();
    }


    public void createSubComponents(FacesContext context) {
    }
}
