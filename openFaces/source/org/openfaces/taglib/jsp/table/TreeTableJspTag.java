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
package org.openfaces.taglib.jsp.table;

import org.openfaces.taglib.internal.table.TreeTableTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class TreeTableJspTag extends AbstractTableJspTag {

    public TreeTableJspTag() {
        super(new TreeTableTag());
    }

    public void setSortLevel(ValueExpression sortLevel) {
        getDelegate().setPropertyValue("sortLevel", sortLevel);
    }

    public void setNodeLevelVar(ValueExpression nodeLevelVar) {
        getDelegate().setPropertyValue("nodeLevelVar", nodeLevelVar);
    }

    public void setNodePathVar(ValueExpression nodePathVar) {
        getDelegate().setPropertyValue("nodePathVar", nodePathVar);
    }

    public void setNodeHasChildrenVar(ValueExpression nodeHasChildrenVar) {
        getDelegate().setPropertyValue("nodeHasChildrenVar", nodeHasChildrenVar);
    }

    public void setExpansionState(ValueExpression expansionState) {
        getDelegate().setPropertyValue("expansionState", expansionState);
    }

    public void setFilterAcceptedRowStyle(ValueExpression filterAcceptedRowStyle) {
        getDelegate().setPropertyValue("filterAcceptedRowStyle", filterAcceptedRowStyle);
    }

    public void setFilterAcceptedRowClass(ValueExpression filterAcceptedRowClass) {
        getDelegate().setPropertyValue("filterAcceptedRowClass", filterAcceptedRowClass);
    }

    public void setFilterSubsidiaryRowStyle(ValueExpression filterSubsidiaryRowStyle) {
        getDelegate().setPropertyValue("filterSubsidiaryRowStyle", filterSubsidiaryRowStyle);
    }

    public void setFilterSubsidiaryRowClass(ValueExpression filterSubsidiaryRowClass) {
        getDelegate().setPropertyValue("filterSubsidiaryRowClass", filterSubsidiaryRowClass);
    }

    public void setTextStyle(ValueExpression textStyle) {
        getDelegate().setPropertyValue("textStyle", textStyle);
    }

    public void setTextClass(ValueExpression textClass) {
        getDelegate().setPropertyValue("textClass", textClass);
    }

    public void setFoldingEnabled(ValueExpression foldingEnabled) {
        getDelegate().setPropertyValue("foldingEnabled", foldingEnabled);
    }

    public void setPreloadedNodes(ValueExpression preloadedNodes) {
        getDelegate().setPropertyValue("preloadedNodes", preloadedNodes);
    }
}
