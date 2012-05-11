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
import org.openfaces.component.table.GroupFooter;
import org.openfaces.component.table.GroupHeaderOrFooter;

public class GroupFooterRow extends GroupHeaderOrFooterRow {
    public static final String COMPONENT_TYPE = "org.openfaces.GroupFooterRow";
    public static final String COMPONENT_FAMILY = "org.openfaces.GroupFooterRow";

    public GroupFooterRow() {
    }

    public GroupFooterRow(DataTable dataTable) {
        super(dataTable);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    protected Class<? extends GroupHeaderOrFooter> getExpectedRowDataClass() {
        return GroupFooter.class;
    }


    @Override
    protected String getDefaultStyleClass() {
        return "o_groupFooter";
    }

    @Override
    protected String getCellContentFacetName() {
        return Column.FACET_GROUP_FOOTER;
    }

    @Override
    public String getStyle() {
        return getExplicitlyAssociatedDataTable().getRowGrouping().getGroupFooterRowStyle();
    }

    @Override
    public String getStyleClass() {
        return getExplicitlyAssociatedDataTable().getRowGrouping().getGroupFooterRowClass();
    }

    @Override
    protected Cell createDefaultCell(DataTable dataTable) {
        Cell defaultCell = super.createDefaultCell(dataTable);
        defaultCell.getAttributes().put(SYNTHETIC_GROUP_HEADER_CELL_MARKER, true);
        return defaultCell;
    }



}
