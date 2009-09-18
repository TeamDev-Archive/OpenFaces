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

import org.openfaces.component.filter.Filter;
import org.openfaces.component.filter.FilterCriterion;
import org.openfaces.component.filter.OneParameterCriterion;
import org.openfaces.component.filter.TextSearchFilter;
import org.openfaces.util.RawScript;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public abstract class TextSearchFilterRenderer extends FilterRenderer {

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ResourceUtil.renderJSLinkIfNeeded(ResourceUtil.getUtilJsURL(context), context);
        ResourceUtil.renderJSLinkIfNeeded(ResourceUtil.getFiltersJsURL(context), context);
        TextSearchFilter filter = (TextSearchFilter) component;

        UIInput inputComponent = (UIInput) filter.getSearchComponent();
        inputComponent.setValue(getStringValue(filter));
        configureInputComponent(context, filter, inputComponent);
        configureInputFromFilter(filter, inputComponent);

        inputComponent.encodeAll(context);

        StyleUtil.renderStyleClasses(context, component);
    }

    protected abstract void configureInputComponent(FacesContext context, Filter filter, UIInput inputComponent);

    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        TextSearchFilter filter = (TextSearchFilter) component;
        UIInput input = (UIInput) filter.getSearchComponent();
        String newSearchString = (String) input.getSubmittedValue();
        if (newSearchString == null) {
            newSearchString = "";
        }
        setDecodedString(filter, newSearchString);
    }

    protected String getFilterOnEnterScript(Filter filter) {
        UIComponent component = (UIComponent) filter.getFilteredComponent();
        return new ScriptBuilder().append("return ").functionCall("O$.Filters._filterFieldKeyPressHandler",
                component,
                filter,
                new RawScript("this"),
                new RawScript("event")).semicolon().toString();
    }

    protected String getStringValue(Filter filter) {
        FilterCriterion filterCriterion = filter.getCriterion();
        if (filterCriterion == null) {
            return "";
        }
        if (!(filterCriterion instanceof OneParameterCriterion)) {
            throw new IllegalStateException("Illegal filter criterion: " + filterCriterion);
        }
        OneParameterCriterion oneParameterCriterion = (OneParameterCriterion) filterCriterion;
        return oneParameterCriterion.getValue().toString();
    }

    private void configureInputFromFilter(Filter filter, UIInput input) {
        for (String attrName : getCopiedFilterAttributes()) {
            Object attrValue = filter.getAttributes().get(attrName);
            if (attrValue != null)
                input.getAttributes().put(attrName, attrValue);
        }
    }

    protected abstract String[] getCopiedFilterAttributes();

}
