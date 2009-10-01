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
package org.openfaces.renderkit.filter;

import org.openfaces.component.FilterableComponent;
import org.openfaces.component.filter.ContainsFilterCriterion;
import org.openfaces.component.filter.Filter;
import org.openfaces.component.filter.FilterCriterion;
import org.openfaces.component.filter.OneParameterCriterion;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.ComponentUtil;
import org.openfaces.util.RawScript;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public abstract class FilterRenderer extends RendererBase {
    protected static final String DEFAULT_PREDEFINED_CRITERION_CLASS = "o_table_filter_predefined_criterion";

    protected String getFilterSubmissionScript(Filter filter) {
        UIComponent component = (UIComponent) filter.getFilteredComponent();
        Filter submittedFilter = ComponentUtil.isChildComponent(filter, component) ? null : filter;
        return new ScriptBuilder().functionCall("O$.Filters._filterComponent", component, submittedFilter, new RawScript("this")).
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
        filteredComponent.filterChanged(filter);
    }

    protected boolean isEmptyItem(Object item) {
        return item == null || item.equals("");
    }

    protected void setDecodedString(Filter filter, String searchString) {
        FilterCriterion oldCriterion = filter.getValue();
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
