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

package org.openfaces.component.table.impl;

import org.openfaces.component.ContextDependentComponent;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.DataTable;
import org.openfaces.renderkit.table.TableBody;
import org.openfaces.renderkit.table.TableStructure;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class InGroupHeaderOrFooterCell extends GroupHeaderOrFooterCell {
    private static final ValueExpression ANY_COLUMN_VALUE_EXPRESSION = new AnyColumnValueExpression();

    private static List<UIComponent> emptyTextChildList;

    private static List<UIComponent> getEmptyTextChildList() {
        if (emptyTextChildList == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            HtmlOutputText outputText =
                    (HtmlOutputText) context.getApplication().createComponent(HtmlOutputText.COMPONENT_TYPE);
            outputText.setValue("");
            emptyTextChildList = Collections.singletonList((UIComponent) outputText);
        }
        return emptyTextChildList;
    }

    private TableBody tableBody;

    public InGroupHeaderOrFooterCell(DataTable dataTable, String facetName) {
        super(dataTable, getEmptyTextChildList(), facetName);
        tableBody = TableStructure.getCurrentInstance(getDataTable()).getBody();
    }

    @Override
    public ValueExpression getConditionExpression() {
        return ANY_COLUMN_VALUE_EXPRESSION;
    }

    @Override
    public List<UIComponent> getChildren() {
        BaseColumn actualCol = getCurrentlyRenderedColumn();
        if (actualCol instanceof ContextDependentComponent) {
            if (!((ContextDependentComponent) actualCol).isComponentInContext()) {
                // This method is expected to be called only from within the encodeAll method of this instance, and this
                // method sets up column's context. If this check fails then it means that this method is invoked from
                // somewhere else, and if that invocation is rightful then this cell's enterComponentContext method has
                // to be invoked prior to this
                throw new IllegalStateException("The current column is supposed to be in context when its children are retrieved");
            }
        }

        UIComponent headerOrFooterComponentFromFacet = actualCol.getFacet(getFacetName());
        if (headerOrFooterComponentFromFacet != null)
            return Collections.singletonList(headerOrFooterComponentFromFacet);
        else {
            // Returning an empty string output-text here...
            // A non-empty child list is required even when rendering a per-column group header cell for a column that
            // doesn't have its inGroupHeader facet specified. The reason is that if we return an empty child list then
            // the table rendering procedure will "think" that this cell doesn't want to override content rendering for
            // this cell, and regular column's children will be rendered, which will cause an error because the
            // currently rendered row is not a regular data row
            return getDefaultChildList();
        }
    }

    protected BaseColumn getCurrentlyRenderedColumn() {
        BaseColumn currentlyRenderedColumn = tableBody.getCurrentlyRenderedColumn();
        if (currentlyRenderedColumn == null)
            throw new IllegalStateException("Couldn't retrieve tableBody.currentlyRenderedColumn property. It is " +
                    "expected that rendering for this cell is initiated only from the TableBody cell rendering " +
                    "procedure where currentRenderedColumn is set explicitly prior to rendering the cell contents " +
                    "container (this custom cell implementation)");
        return currentlyRenderedColumn instanceof GroupingStructureColumn
                ? ((GroupingStructureColumn) currentlyRenderedColumn).getDelegate()
                : currentlyRenderedColumn;
    }

}
