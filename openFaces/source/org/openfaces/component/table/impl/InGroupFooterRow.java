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

import org.openfaces.component.table.Column;
import org.openfaces.component.table.DataTable;
import org.openfaces.component.table.GroupHeaderOrFooter;
import org.openfaces.component.table.InGroupFooter;

public class InGroupFooterRow extends InGroupHeaderOrFooterRow {
    public static final String COMPONENT_TYPE = "org.openfaces.InGroupFooterRow";
    public static final String COMPONENT_FAMILY = "org.openfaces.InGroupFooterRow";

    public InGroupFooterRow() {
    }

    public InGroupFooterRow(DataTable dataTable) {
        super(dataTable);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    protected Class<? extends GroupHeaderOrFooter> getExpectedRowDataClass() {
        return InGroupFooter.class;
    }

    @Override
    protected String getDefaultStyleClass() {
        return "o_inGroupFooter";
    }

    @Override
    protected String getCellContentFacetName() {
        return Column.FACET_IN_GROUP_FOOTER;
    }

    @Override
    public String getStyle() {
        return getExplicitlyAssociatedDataTable().getRowGrouping().getInGroupFooterRowStyle();
    }

    @Override
    public String getStyleClass() {
        return getExplicitlyAssociatedDataTable().getRowGrouping().getInGroupFooterRowClass();
    }


}
