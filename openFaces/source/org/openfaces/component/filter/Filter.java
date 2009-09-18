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
package org.openfaces.component.filter;

import org.openfaces.component.CompoundComponent;
import org.openfaces.component.FilterableComponent;
import org.openfaces.util.ComponentUtil;
import org.openfaces.util.SelfScheduledAction;
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.FacesException;
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
public abstract class Filter extends UIComponentBase implements CompoundComponent {
    private static final String DEFAULT_ALL_RECORDS_CRITERION_NAME = "<All>";
    private static final String DEFAULT_EMPTY_RECORDS_CRITERION_NAME = "<Empty>";
    private static final String DEFAULT_NON_EMPTY_RECORDS_CRITERION_NAME = "<Non-empty>";

    private String _for;

    private FilterCriterion criterion;
    private boolean criterionModelUpdateRequired;

    private FilterableComponent filteredComponent;

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
    private String accesskey;
    private String tabindex;
    private String title;

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, _for, style, styleClass, predefinedCriterionStyle, predefinedCriterionClass,
                criterion, allRecordsText, emptyRecordsText, nonEmptyRecordsText,
                promptText, promptTextStyle, promptTextClass, caseSensitive, accesskey, tabindex, title};
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        _for = (String) state[i++];
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
        caseSensitive = (Boolean) state[i++];
        accesskey = (String) state[i++];
        tabindex = (String) state[i++];
        title = (String) state[i++];
    }

    public String getFor() {
        return _for;
    }

    public void setFor(String aFor) {
        _for = aFor;
    }

    public boolean isCaseSensitive() {
        return ValueBindings.get(this, "caseSensitive", caseSensitive, false);
    }

    public void setCaseSensitive(boolean caseSensitive) {
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
        Object filteredValue = getFilteredValueByData(facesContext, getFilteredComponent(), data);
        return criterion == null || criterion.acceptsValue(filteredValue);
    }

    private Object getFilteredValueByData(FacesContext facesContext, FilterableComponent filteredComponent, Object data) {
        ValueExpression criterionNameExpression = getFilterExpression();
        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        String var = getFilteredComponent().getVar();
        return filteredComponent.getFilteredValueByData(facesContext, requestMap, criterionNameExpression, var, data);
    }

    public boolean isAcceptingAllRecords() {
        return getCriterion() == null;
    }

    public FilterableComponent getFilteredComponent() {
        if (filteredComponent == null) {
            String aFor = getFor();
            if (aFor != null) {
                UIComponent referredComponent = ComponentUtil.referenceIdToComponent(this, aFor);
                if (referredComponent != null && !(referredComponent instanceof FilterableComponent))
                    throw new FacesException("Filter's \"for\" attribute must refer to a filterable component (DataTable or TreeTable)");
                filteredComponent = (FilterableComponent) referredComponent;
            } else {
                UIComponent component = getParent();
                while (component != null && !(component instanceof FilterableComponent))
                    component = component.getParent();
                filteredComponent = (FilterableComponent) component;
            }
        }
        return filteredComponent;
    }

    private ValueExpression getFilterExpression() {
        return getValueExpression("expression");
    }

    public String getAllRecordsText() {
        String result = ValueBindings.get(this, "allRecordsCriterionName", allRecordsText);
        if (result == null) {
            FilterableComponent filteredComponent = getFilteredComponent();
            if (filteredComponent != null)
                result = filteredComponent.getAllRecordsFilterText();
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
            FilterableComponent filteredComponent = getFilteredComponent();
            if (filteredComponent != null)
                result = filteredComponent.getEmptyRecordsFilterText();
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
            FilterableComponent filteredComponent = getFilteredComponent();
            if (filteredComponent != null)
                result = filteredComponent.getNonEmptyRecordsFilterText();
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
        String var = getFilteredComponent().getVar();
        Set<Object> criterionNamesSet = new TreeSet<Object>();
        FilterableComponent filteredComponent = getFilteredComponent();
        List originalRowList = filteredComponent.getRowListForFiltering(this);
        boolean thereAreNullValues = false;
        for (Object data : originalRowList) {
            Object value = filteredComponent.getFilteredValueByData(context, requestMap, expression, var, data);
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

    public void updateCriterionFromBinding(FacesContext context) {
        ValueExpression criterionExpression = getCriterionExpression();
        if (criterionExpression != null)
            criterion = (FilterCriterion) criterionExpression.getValue(context.getELContext());
        else {
            if (criterion instanceof OneParameterCriterion) {
                boolean caseSensitive = isCaseSensitive();
                OneParameterCriterion opc = (OneParameterCriterion) criterion;
                if (opc.isCaseSensitive() != caseSensitive)
                    criterion = opc.setCaseSensitive(caseSensitive);
            }
        }
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
    public boolean changeCriterion(FilterCriterion newCriterion) {
        FilterCriterion oldCriterion = getCriterion();
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

    @Override
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

    public void createSubComponents(FacesContext context) {
    }

    public void setParent(UIComponent parent) {
        super.setParent(parent);

        ComponentUtil.runWhenReady(new SelfScheduledAction() {
            public boolean executeIfReady() {
                FilterableComponent filteredComponent = getFilteredComponent();
                if (filteredComponent == null)
                    return false;
                List<Filter> filters = filteredComponent.getFilters();
                if (!filters.contains(Filter.this))
                    filters.add(Filter.this);
                return true;
            }
        });
    }

}
