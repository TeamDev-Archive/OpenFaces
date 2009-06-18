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
import org.openfaces.util.RenderingUtil;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class TableColumn extends BaseColumn implements CompoundComponent {
    public static final String COMPONENT_TYPE = "org.openfaces.TableColumn";
    public static final String COMPONENT_FAMILY = "org.openfaces.TableColumn";

    public static final String COMBO_BOX_FILTER_CHILD_ID_SUFFIX = "combo_box_o_auto_filter";
    public static final String DROP_DOWN_FILTER_CHILD_ID_SUFFIX = "drop_down_o_auto_filter";
    public static final String SEARCH_FIELD_FILTER_CHILD_ID_SUFFIX = "search_field_o_auto_filter";

    private FilterKind filterKind = FilterKind.SEARCH_FIELD;

    private String filterPromptText;

    private String filterPromptTextStyle;
    private String filterPromptTextClass;

    public TableColumn() {
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, filterKind, filterPromptText, filterPromptTextStyle, filterPromptTextClass};
    }

    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        filterKind = (FilterKind) state[i++];

        filterPromptText = (String) state[i++];

        filterPromptTextStyle = (String) state[i++];
        filterPromptTextClass = (String) state[i++];
    }

    public FilterKind getFilterKind() {
        ValueExpression ve = getValueExpression("filterKind");
        FilterKind filterKind = null;
        if (ve != null) {
            ELContext elContext = FacesContext.getCurrentInstance().getELContext();
            filterKind = (FilterKind) ve.getValue(elContext);
        }

        return (ve != null) ? filterKind : this.filterKind;
    }

    public void setFilterKind(FilterKind filterKind) {
        if (filterKind == null)
            throw new IllegalArgumentException("FilterKind property can be set to null only through value binding.");
        this.filterKind = filterKind;
    }

    public ValueExpression getSortingExpression() {
        return getValueExpression("sortingExpression");
    }

    public void setSortingExpression(ValueExpression sortingExpression) {
        setValueExpression("sortingExpression", sortingExpression);
    }

    public ValueExpression getSortingComparatorBinding() {
        return getValueExpression("sortingComparator");
    }

    public void setSortingComparatorExpression(ValueExpression sortingComparatorBinding) {
        setValueExpression("sortingComparator", sortingComparatorBinding);
    }


    public ValueExpression getFilterExpression() {
        return getValueExpression("filterExpression");
    }

    public void setFilterExpression(ValueExpression filterExpression) {
        setValueExpression("filterExpression", filterExpression);
    }

    public void setFilterValuesExpression(ValueExpression filterValuesExpression) {
        setValueExpression("filterValues", filterValuesExpression);
    }

    public ValueExpression getFilterValuesExpression() {
        return getValueExpression("filterValues");
    }

    public void setFilterValueExpression(ValueExpression filterValueExpression) {
        setValueExpression("filterValue", filterValueExpression);
    }

    public ValueExpression getFilterValueExpression() {
        return getValueExpression("filterValue");
    }

    public String getFilterPromptText() {
        return ValueBindings.get(this, "filterPromptText", filterPromptText);
    }

    public void setFilterPromptText(String promptText) {
        filterPromptText = promptText;
    }

    public String getFilterPromptTextStyle() {
        return ValueBindings.get(this, "filterPomptTextStyle", filterPromptTextStyle);
    }

    public void setFilterPromptTextStyle(String promptTextStyle) {
        filterPromptTextStyle = promptTextStyle;
    }

    public String getFilterPromptTextClass() {
        return ValueBindings.get(this, "filterPromptTextClass", filterPromptTextClass);
    }

    public void setFilterPromptTextClass(String promptTextClass) {
        filterPromptTextClass = promptTextClass;
    }

    public DataTableFilter getSubHeader() {
        ValueExpression filterExpression = getFilterExpression();
        ValueExpression filterValuesExpression = getFilterValuesExpression();
        ValueExpression filterValueExpression = getFilterValueExpression();
        if (filterExpression == null && filterValuesExpression == null)
            return null;

        FilterKind filterKind = this.getFilterKind();
        DataTableFilter filter;
        if (filterKind == null) {
            return null;
        } else if (filterKind.equals(FilterKind.COMBO_BOX)) {
            filter = (DataTableFilter) RenderingUtil.getFacet(
                    this, COMBO_BOX_FILTER_CHILD_ID_SUFFIX, ComboBoxDataTableFilter.class);
        } else if (filterKind.equals(FilterKind.DROP_DOWN_FIELD)) {
            filter = (DataTableFilter) RenderingUtil.getFacet(
                    this, DROP_DOWN_FILTER_CHILD_ID_SUFFIX, DropDownFieldDataTableFilter.class);

            filter.setPromptText(getFilterPromptText());
            filter.setPromptTextStyle(getFilterPromptTextStyle());
            filter.setPromptTextClass(getFilterPromptTextClass());

        } else if (filterKind.equals(FilterKind.SEARCH_FIELD)) {
            filter = (DataTableFilter) RenderingUtil.getFacet(
                    this, SEARCH_FIELD_FILTER_CHILD_ID_SUFFIX, SearchFieldDataTableFilter.class);

            filter.setPromptText(getFilterPromptText());
            filter.setPromptTextStyle(getFilterPromptTextStyle());
            filter.setPromptTextClass(getFilterPromptTextClass());
        } else
            throw new IllegalStateException("Unknown filter kind: " + filterKind);

        filter.setFilterValuesExpressionExpression(filterExpression);
        filter.setFilterValuesExpression(filterValuesExpression);
        filter.setSearchStringExpression(filterValueExpression);

        AbstractTable table = getTable();
        if (table != null) {
            filter.setAllRecordsCriterionName(table.getAllRecordsFilterName());
            filter.setEmptyRecordsCriterionName(table.getEmptyRecordsFilterName());
            filter.setNonEmptyRecordsCriterionName(table.getNonEmptyRecordsFilterName());
        }

        filter.setRendered(true);

        return filter;
    }

    public void createSubComponents(FacesContext context) {
        ValueExpression filterExpression = getFilterExpression();
        ValueExpression filterValuesExpression = getFilterValuesExpression();
        if (filterExpression == null && filterValuesExpression == null)
            return;

        ValueExpression filterKindExpression = getValueExpression("filterKind");

        if (filterKindExpression == null) {
            createSingleFilter(context);
        } else {
            createMultipleFilters();
        }
    }

    private void createSingleFilter(FacesContext context) {
        FilterKind filterKind = getFilterKind();
        DataTableFilter filter;
        if (filterKind == null) {
            throw new IllegalStateException("FilterKind property can be set to null only through value binding.");
        } else if (filterKind.equals(FilterKind.COMBO_BOX)) {
            filter = RenderingUtil.getOrCreateFacet(
                    context, this, ComboBoxDataTableFilter.COMPONENT_TYPE,
                    COMBO_BOX_FILTER_CHILD_ID_SUFFIX, ComboBoxDataTableFilter.class);
        } else if (filterKind.equals(FilterKind.DROP_DOWN_FIELD)) {
            filter = RenderingUtil.getOrCreateFacet(
                    context, this, DropDownFieldDataTableFilter.COMPONENT_TYPE,
                    DROP_DOWN_FILTER_CHILD_ID_SUFFIX, DropDownFieldDataTableFilter.class);
        } else if (filterKind.equals(FilterKind.SEARCH_FIELD)) {
            filter = RenderingUtil.getOrCreateFacet(
                    context, this, SearchFieldDataTableFilter.COMPONENT_TYPE,
                    SEARCH_FIELD_FILTER_CHILD_ID_SUFFIX, SearchFieldDataTableFilter.class);
        } else
            throw new IllegalStateException("Unknown filter kind: " + filterKind);

        initializeFilter(filter);
    }

    private void createMultipleFilters() {
        createFilter(ComboBoxDataTableFilter.COMPONENT_TYPE,
                COMBO_BOX_FILTER_CHILD_ID_SUFFIX, ComboBoxDataTableFilter.class);
        createFilter(DropDownFieldDataTableFilter.COMPONENT_TYPE,
                DROP_DOWN_FILTER_CHILD_ID_SUFFIX, DropDownFieldDataTableFilter.class);
        createFilter(SearchFieldDataTableFilter.COMPONENT_TYPE,
                SEARCH_FIELD_FILTER_CHILD_ID_SUFFIX, SearchFieldDataTableFilter.class);
    }

    private void createFilter(String componentType, String identifier, Class componentClass) {
        FacesContext context = FacesContext.getCurrentInstance();

        DataTableFilter dataTableFilter = (DataTableFilter) RenderingUtil.getOrCreateFacet(
                context, this, componentType,
                identifier, componentClass);

        initializeFilter(dataTableFilter);
    }

    private void initializeFilter(DataTableFilter filter) {
        filter.setStyle("width: 100%");
        filter.setRendered(false);
    }
}
