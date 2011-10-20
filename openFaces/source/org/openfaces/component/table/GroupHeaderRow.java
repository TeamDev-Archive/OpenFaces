/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.table;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.List;

public class GroupHeaderRow extends GroupHeaderOrFooterRow {
    public static final String COMPONENT_TYPE = "org.openfaces.GroupHeaderRow";
    public static final String COMPONENT_FAMILY = "org.openfaces.GroupHeaderRow";

    private List<UIComponent> cells;

    public GroupHeaderRow() {
    }

    @Override
    protected Class<? extends RowGroupHeaderOrFooter> getExpectedRowDataClass() {
        return RowGroupHeader.class;
    }

    public GroupHeaderRow(DataTable dataTable) {
        super(dataTable);

        Cell groupHeaderCell = createDefaultCell(dataTable);
        cells = Collections.singletonList((UIComponent) groupHeaderCell);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),

        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);

    }

    private Cell createDefaultCell(DataTable dataTable) {
        FacesContext context = FacesContext.getCurrentInstance();
        RowGrouping rowGrouping = dataTable.getRowGrouping();
        HtmlOutputText outputText = (HtmlOutputText) context.getApplication().createComponent(HtmlOutputText.COMPONENT_TYPE);
        outputText.setValueExpression("value", rowGrouping.getGroupHeaderTextExpression());

        Cell groupHeaderCell = new Cell();
        groupHeaderCell.getChildren().add(outputText);
        int allColumnCount = dataTable.getAllColumns().size();
        groupHeaderCell.setSpan(allColumnCount);
        return groupHeaderCell;
    }

    @Override
    protected String getDefaultStyleClass() {
        return "o_rowGroupHeader";
    }

    @Override
    public List<UIComponent> getChildren() {
        if (super.getChildCount() > 0)
            return super.getChildren();
        else
            return cells;
    }

    @Override
    public int getChildCount() {
        int explicitlySpecifiedChildren = super.getChildCount();
        if (explicitlySpecifiedChildren > 0) {
            return explicitlySpecifiedChildren;
        } else
            return cells.size();
    }

}
