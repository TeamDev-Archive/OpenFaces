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

import org.openfaces.component.table.Cell;
import org.openfaces.component.table.Column;
import org.openfaces.component.table.DataTable;
import org.openfaces.component.table.GroupHeader;
import org.openfaces.component.table.GroupHeaderOrFooter;
import org.openfaces.component.table.RowGrouping;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.List;

public class GroupHeaderRow extends GroupHeaderOrFooterRow {
    public static final String COMPONENT_TYPE = "org.openfaces.GroupHeaderRow";
    public static final String COMPONENT_FAMILY = "org.openfaces.GroupHeaderRow";

    public GroupHeaderRow() {
    }

    public GroupHeaderRow(DataTable dataTable) {
        super(dataTable);

    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    protected Class<? extends GroupHeaderOrFooter> getExpectedRowDataClass() {
        return GroupHeader.class;
    }

    @Override
    protected Cell createDefaultCell(DataTable dataTable) {
        Cell defaultCell = super.createDefaultCell(dataTable);
        defaultCell.getAttributes().put(SYNTHETIC_GROUP_HEADER_CELL_MARKER, true);
        return defaultCell;
    }

    @Override
    protected String getDefaultStyleClass() {
        return "o_groupHeader";
    }

    @Override
    protected String getCellContentFacetName() {
        return Column.FACET_GROUP_HEADER;
    }

    @Override
    protected List<HtmlOutputText> getDefaultChildList(DataTable dataTable, FacesContext context) {
        RowGrouping rowGrouping = dataTable.getRowGrouping();
        HtmlOutputText outputText = (HtmlOutputText) context.getApplication().createComponent(HtmlOutputText.COMPONENT_TYPE);
        outputText.setValueExpression("value", rowGrouping.getGroupHeaderTextExpression());
        return Collections.singletonList(outputText);
    }

    @Override
    public String getStyle() {
        return getExplicitlyAssociatedDataTable().getRowGrouping().getGroupHeaderRowStyle();
    }

    @Override
    public String getStyleClass() {
        return getExplicitlyAssociatedDataTable().getRowGrouping().getGroupHeaderRowClass();
    }
}
