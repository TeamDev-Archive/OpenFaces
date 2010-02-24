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
import org.openfaces.component.OUIComponentBase;
import org.openfaces.util.Components;
import org.openfaces.util.SelfScheduledAction;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public abstract class Filter extends OUIComponentBase {
    private String _for;
    private FilterableComponent filteredComponent;

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, _for};
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        _for = (String) state[i++];
    }


    public String getFor() {
        return _for;
    }

    public void setFor(String aFor) {
        _for = aFor;
    }


    public abstract Object getValue();

    public abstract void setValue(Object value);

    public abstract void updateValueFromBinding(FacesContext context);

    public abstract boolean getWantsRowList();

    public boolean isAcceptingAllRecords() {
        FilterCriterion criterion = (FilterCriterion) getValue();
        return criterion == null || criterion.acceptsAll();
    }

    public FilterableComponent getFilteredComponent() {
        if (filteredComponent == null) {
            String aFor = getFor();
            if (aFor != null) {
                UIComponent referredComponent = Components.referenceIdToComponent(this, aFor);
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

    public void setParent(UIComponent parent) {
        super.setParent(parent);

        Components.runWhenReady(new SelfScheduledAction() {
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
