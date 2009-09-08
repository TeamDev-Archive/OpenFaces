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
import org.openfaces.component.table.DataTable;
import org.openfaces.component.table.AbstractFilter;
import org.openfaces.component.table.FilterCriterion;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.StyleUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class AbstractFilterRenderer extends RendererBase {
    protected static final String DEFAULT_PREDEFINED_CRITERION_CLASS = "o_table_filter_predefined_criterion";

    protected String getFilterSubmissionScript(AbstractFilter filter, FacesContext context) {
        AbstractTable table = getTable(filter);
        String tableId = table.getClientId(context);
        return "O$.Table._filterDataTable('" + tableId + "', this);";
    }

    AbstractTable getTable(AbstractFilter filter) {
        for (UIComponent parent = filter.getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof AbstractTable)
                return (AbstractTable) parent;
        }
        throw new IllegalStateException("Couldn't find DataTable or TreeTable where this filter is embedded. Filter's clientId = " + filter.getClientId(FacesContext.getCurrentInstance()));
    }

    protected String getPredefinedCriterionClass(FacesContext context, AbstractFilter filter) {
        String predefinedCriterionStyle = filter.getPredefinedCriterionStyle();
        return StyleUtil.getCSSClass(context, filter, predefinedCriterionStyle, DEFAULT_PREDEFINED_CRITERION_CLASS, filter.getPredefinedCriterionClass());
    }

    protected void setDecodedSearchString(AbstractFilter filter, FilterCriterion newCriterion) {
        if (!filter.changeSearchString(newCriterion))
            return;
        AbstractTable table = getTable(filter);
        if (table instanceof DataTable) {
            DataTable dataTable = ((DataTable) table);
            if (dataTable.getPageIndex() > 0)
                dataTable.setPageIndex(0);
        }
    }

    protected boolean isEmptyItem(Object item) {
        return item == null || item.equals("");
    }
}
