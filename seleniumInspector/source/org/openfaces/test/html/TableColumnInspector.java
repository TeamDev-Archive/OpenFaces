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
package org.openfaces.test.html;

import org.openfaces.test.SubElementByPathInspector;

/**
 * @author Dmitry Pikhulya
 */
public class TableColumnInspector extends SubElementByPathInspector {
    private int columnIndex;

    public TableColumnInspector(TableInspector table, int columnIndex) {
        super(table, "COL[" + columnIndex + "]");
        this.columnIndex = columnIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    protected TableInspector getTable() {
        return (TableInspector) getBaseElement();
    }

    public int getHeaderCellCount() {
        TableSectionInspector header = getTable().header();
        if (!header.elementExists())
            return 0;
        return header.rowCount();
    }

    public TableCellInspector headerCell(int index) {
        return getTable().header().row(index).cell(columnIndex);
    }

    public int getBodyCellCount() {
        TableSectionInspector body = getTable().body();
        if (!body.elementExists())
            return 0;
        return body.rowCount();
    }

    public TableCellInspector bodyCell(int index) {
        return getTable().body().row(index).cell(columnIndex);
    }

    public int getFooterCellCount() {
        TableSectionInspector footer = getTable().footer();
        if (!footer.elementExists())
            return 0;
        return footer.rowCount();
    }

    public TableCellInspector footerCell(int index) {
        return getTable().footer().row(index).cell(columnIndex);
    }

}