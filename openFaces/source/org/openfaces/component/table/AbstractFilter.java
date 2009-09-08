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
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AbstractFilter extends UIComponentBase implements CompoundComponent {
    private static final String DEFAULT_ALL_RECORDS_CRITERION_NAME = "<All>";
    private static final String DEFAULT_EMPTY_RECORDS_CRITERION_NAME = "<Empty>";
    private static final String DEFAULT_NON_EMPTY_RECORDS_CRITERION_NAME = "<Non-empty>";

    private FilterCriterion criterion;
    private boolean criterionModelUpdateRequired;

    private AbstractTable table;

    private String style;
    private String styleClass;
    private String predefinedCriterionStyle;
    private String predefinedCriterionClass;
    private Boolean caseSensitive;

    protected String allRecordsText;
    protected String emptyRecordsText;
    protected String nonEmptyRecordsText;

    private String promptText;

    private String promptTextStyle;
    private String promptTextClass;

    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, style, styleClass, predefinedCriterionStyle, predefinedCriterionClass,
                criterion, allRecordsText, emptyRecordsText, nonEmptyRecordsText,
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
        criterion = (FilterCriterion) state[i++];
        allRecordsText = (String) state[i++];
        emptyRecordsText = (String) state[i++];
        nonEmptyRecordsText = (String) state[i++];

        promptText = (String) state[i++];
        promptTextStyle = (String) state[i++];
        promptTextClass = (String) state[i++];
    }

    public Boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(Boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
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
        Object filteredValue = getFilteredValueByData(facesContext, getTable(), data);
        return criterion == null || criterion.acceptsValue(filteredValue);
    }

    private Object getFilteredValueByData(FacesContext facesContext, AbstractTable table, Object data) {
        ValueExpression criterionNameExpression = getFilterExpression();
        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        String var = getTable().getVar();
        return table.getFilteredValueByData(facesContext, requestMap, criterionNameExpression, var, data);
    }

    public boolean isAcceptingAllRecords() {
        return getCriterion() == null;
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

    private ValueExpression getFilterExpression() {
        return getValueExpression("expression");
    }

    public String getAllRecordsText() {
        String result = ValueBindings.get(this, "allRecordsCriterionName", allRecordsText);
        if (result == null) {
            AbstractTable table = getTable();
            if (table != null)
                result = table.getAllRecordsFilterName();
        }
        if (result == null)
            result = DEFAULT_ALL_RECORDS_CRITERION_NAME;
        return result;
    }

    public void setAllRecordsText(String allRecordsText) {
        this.allRecordsText = allRecordsText;
    }

    public String getEmptyRecordsText() {
        String result = ValueBindings.get(this, "emptyRecordsCriterionName", emptyRecordsText);
        if (result == null) {
            AbstractTable table = getTable();
            if (table != null)
                result = table.getEmptyRecordsFilterName();
        }
        if (result == null)
            result = DEFAULT_EMPTY_RECORDS_CRITERION_NAME;
        return result;
    }

    public void setEmptyRecordsText(String value) {
        emptyRecordsText = value;
    }

    public String getNonEmptyRecordsText() {
        String result = ValueBindings.get(this, "nonEmptyRecordsCriterionName", nonEmptyRecordsText);
        if (result == null) {
            AbstractTable table = getTable();
            if (table != null)
                result = table.getNonEmptyRecordsFilterName();
        }
        if (result == null)
            result = DEFAULT_NON_EMPTY_RECORDS_CRITERION_NAME;
        return result;
    }

    public void setNonEmptyRecordsText(String value) {
        nonEmptyRecordsText = value;
    }

    public ValueExpression getOptionsExpression() {
        return getValueExpression("options");
    }

    public void setOptionsExpression(ValueExpression optionsExpression) {
        setValueExpression("options", optionsExpression);
    }

    public boolean getWantsRowList() {
        if (!isShowingPredefinedCriterionNames())
            return false;
        ValueExpression filterValuesExpression = getOptionsExpression();
        return filterValuesExpression == null;
    }

    protected boolean isShowingPredefinedCriterionNames() {
        return false;
    }

    public Collection<Object> calculateAllCriterionNames(FacesContext context) {
        ValueExpression valuesExpression = getOptionsExpression();
        if (valuesExpression != null) {
            Iterable values = (Iterable) valuesExpression.getValue(context.getELContext());
            List<Object> result = new ArrayList<Object>();
            if (values != null) {
                for (Object value : values) {
                    result.add(value);
                }
            }
            return result;
        }
        ValueExpression expression = getFilterExpression();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        String var = getTable().getVar();
        Set<Object> criterionNamesSet = new TreeSet<Object>();
        List originalRowList = getTable().getRowListForFiltering(this);
        AbstractTable table = getTable();
        boolean thereAreNullValues = false;
        for (Object data : originalRowList) {
            Object value = table.getFilteredValueByData(context, requestMap, expression, var, data);
            if (value == null)
                thereAreNullValues = true;
            else
                criterionNamesSet.add(value);
        }
        List<Object> list = new ArrayList<Object>();
        if (thereAreNullValues)
            list.add(null);
        list.addAll(criterionNamesSet);
        return list;
    }

    public void updateSearchStringFromBinding(FacesContext context) {
        ValueExpression valueExpression = getCriterionExpression();
        if (valueExpression != null)
            criterion = (FilterCriterion) valueExpression.getValue(context.getELContext());
    }

    public FilterCriterion getCriterion() {
        return criterion;
    }

    public void setCriterion(FilterCriterion criterion) {
        this.criterion = criterion;
    }

    /**
     * @param newCriterion new search criterion
     * @return true if the new criterion results in the different filtering behavior as opposed to this filter's previous
     *         criterion
     */
    public boolean changeSearchString(FilterCriterion newCriterion) {
        FilterCriterion oldCriterion = getCriterion();
        setCriterionColumnId(newCriterion);
        criterionModelUpdateRequired = true;

        if (isAllRecordsCriterion(newCriterion)) {
            if (isAllRecordsCriterion(oldCriterion))
                return false;
        } else if (newCriterion.equals(oldCriterion))
            return false;
        setCriterion(newCriterion);
        return true;
    }

    private boolean isAllRecordsCriterion(FilterCriterion criterion) {
        return criterion == null || criterion.acceptsAll();
    }

    public void setCriterionExpression(ValueExpression expression) {
        setValueExpression("criterion", expression);
    }

    public ValueExpression getCriterionExpression() {
        return getValueExpression("criterion");
    }

    public void processUpdates(FacesContext context) {
        super.processUpdates(context);
        if (!criterionModelUpdateRequired)
            return;

        ValueExpression criterionExpression = getCriterionExpression();
        if (criterionExpression != null) {
            criterionExpression.setValue(context.getELContext(), criterion);
            criterionModelUpdateRequired = false;
        }
    }

    public FilterCriterion getFilterCriterion() {
        FilterCriterion criterion = getCriterion();
        setCriterionColumnId(criterion);
        return criterion;
    }

    private void setCriterionColumnId(FilterCriterion criterion) {
        if (criterion instanceof ColumnFilterCriterion) {
            ((ColumnFilterCriterion) criterion).setColumnId(getColumnId());
        }
    }

    protected String getColumnId() {
        TableColumn col = (TableColumn) getParent();
        return col.getId();
    }


    public void createSubComponents(FacesContext context) {
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
//        if (getFilterExpression() == null)
//            throw new FacesException("The filter's \"expression\" attribute must be specified. Filter's clientId: " + getClientId(getFacesContext()));
        super.encodeBegin(context);
    }
}
