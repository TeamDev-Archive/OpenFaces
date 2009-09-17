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
package org.openfaces.renderkit.table;

import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.ContainsFilterCriterion;
import org.openfaces.component.table.DataTable;
import org.openfaces.component.table.Filter;
import org.openfaces.component.table.FilterCriterion;
import org.openfaces.component.table.OneParameterCriterion;
import org.openfaces.component.FilterableComponent;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.StyleUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.RawScript;
import org.openfaces.util.ComponentUtil;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

/**
 * @author Dmitry Pikhulya
 */
public class FilterRenderer extends RendererBase {
    protected static final String DEFAULT_PREDEFINED_CRITERION_CLASS = "o_table_filter_predefined_criterion";

    protected String getFilterSubmissionScript(Filter filter) {
        UIComponent table = (UIComponent) filter.getFilteredComponent();
        Filter submittedFilter = ComponentUtil.isChildComponent(filter, table) ? null : filter;
        return new ScriptBuilder().functionCall("O$.Table._filterComponent", table, submittedFilter, new RawScript("this")).
                semicolon().toString();
    }

    protected String getPredefinedCriterionClass(FacesContext context, Filter filter) {
        String predefinedCriterionStyle = filter.getPredefinedCriterionStyle();
        return StyleUtil.getCSSClass(context, filter, predefinedCriterionStyle, DEFAULT_PREDEFINED_CRITERION_CLASS, filter.getPredefinedCriterionClass());
    }

    protected void setDecodedCriterion(Filter filter, FilterCriterion newCriterion) {
        if (!filter.changeCriterion(newCriterion))
            return;
        FilterableComponent filteredComponent = filter.getFilteredComponent();
        if (filteredComponent instanceof DataTable) {
            DataTable dataTable = ((DataTable) filteredComponent);
            if (dataTable.getPageIndex() > 0)
                dataTable.setPageIndex(0);
        }
    }

    protected boolean isEmptyItem(Object item) {
        return item == null || item.equals("");
    }

    protected void setDecodedString(Filter filter, String searchString) {
        FilterCriterion oldCriterion = filter.getCriterion();
        FilterCriterion newCriterion;
        if (oldCriterion instanceof OneParameterCriterion)
            newCriterion = ((OneParameterCriterion) oldCriterion).setValue(searchString);
        else
            newCriterion = createDefaultCriterion(filter, searchString);
        setDecodedCriterion(filter, newCriterion);
    }

    protected FilterCriterion createDefaultCriterion(Filter filter, String searchString) {
        return new ContainsFilterCriterion(searchString, filter.isCaseSensitive());
    }
}
