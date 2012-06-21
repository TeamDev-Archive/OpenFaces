/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.taglib.jsp.table;

import org.openfaces.taglib.internal.table.RowGroupingTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

public class RowGroupingJspTag extends AbstractComponentJspTag {

    public RowGroupingJspTag() {
        super(new RowGroupingTag());
    }

    public void setGroupingRules(ValueExpression groupingRules) {
        getDelegate().setPropertyValue("groupingRules", groupingRules);
    }

    public void setColumnHeaderVar(ValueExpression columnHeaderVar) {
        getDelegate().setPropertyValue("columnHeaderVar", columnHeaderVar);
    }

    public void setGroupingValueVar(ValueExpression groupingValueVar) {
        getDelegate().setPropertyValue("groupingValueVar", groupingValueVar);
    }

    public void setGroupHeaderText(ValueExpression groupHeaderText) {
        getDelegate().setPropertyValue("groupHeaderText", groupHeaderText);
    }

    public void setGroupingValueStringVar(ValueExpression groupingValueStringVar) {
        getDelegate().setPropertyValue("groupingValueStringVar", groupingValueStringVar);
    }

    public void setGroupOnHeaderClick(ValueExpression groupOnHeaderClick) {
        getDelegate().setPropertyValue("groupOnHeaderClick", groupOnHeaderClick);
    }

    public void setHideGroupingColumns(ValueExpression hideGroupingColumns) {
        getDelegate().setPropertyValue("hideGroupingColumns", hideGroupingColumns);
    }

    public void setExpansionState(ValueExpression expansionState) {
        getDelegate().setPropertyValue("expansionState", expansionState);
    }

    public void setSelectionMode(ValueExpression selectionMode) {
        getDelegate().setPropertyValue("selectionMode", selectionMode);
    }

    public void setGroupHeaderRowStyle(ValueExpression groupHeaderRowStyle) {
        getDelegate().setPropertyValue("groupHeaderRowStyle", groupHeaderRowStyle);
    }

    public void setGroupHeaderRowClass(ValueExpression groupHeaderRowClass) {
        getDelegate().setPropertyValue("groupHeaderRowClass", groupHeaderRowClass);
    }

    public void setGroupFooterRowStyle(ValueExpression groupFooterRowStyle) {
        getDelegate().setPropertyValue("groupFooterRowStyle", groupFooterRowStyle);
    }

    public void setGroupFooterRowClass(ValueExpression groupFooterRowClass) {
        getDelegate().setPropertyValue("groupFooterRowClass", groupFooterRowClass);
    }

    public void setInGroupHeaderRowStyle(ValueExpression inGroupHeaderRowStyle) {
        getDelegate().setPropertyValue("inGroupHeaderRowStyle", inGroupHeaderRowStyle);
    }

    public void setInGroupHeaderRowClass(ValueExpression inGroupHeaderRowClass) {
        getDelegate().setPropertyValue("inGroupHeaderRowClass", inGroupHeaderRowClass);
    }

    public void setInGroupFooterRowStyle(ValueExpression inGroupFooterRowStyle) {
        getDelegate().setPropertyValue("inGroupFooterRowStyle", inGroupFooterRowStyle);
    }

    public void setInGroupFooterRowClass(ValueExpression inGroupFooterRowClass) {
        getDelegate().setPropertyValue("inGroupFooterRowClass", inGroupFooterRowClass);
    }

    public void setInGroupFootersCollapsible(ValueExpression inGroupFootersCollapsible) {
        getDelegate().setPropertyValue("inGroupFootersCollapsible", inGroupFootersCollapsible);
    }

    public void setGroupFootersCollapsible(ValueExpression groupFootersCollapsible) {
        getDelegate().setPropertyValue("groupFootersCollapsible", groupFootersCollapsible);
    }
}
