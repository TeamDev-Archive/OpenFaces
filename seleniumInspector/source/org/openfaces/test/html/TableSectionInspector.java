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

import junit.framework.Assert;
import org.openfaces.test.ElementByReferenceInspector;
import org.openfaces.test.ElementInspector;
import org.openfaces.test.SubElementByPathInspector;

import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class TableSectionInspector extends ElementByReferenceInspector {
    private TableInspector table;
    private List<ElementInspector> rows;

    public TableSectionInspector(TableInspector table, String sectionTagName) {
        super(new SubElementByPathInspector(table, sectionTagName));
        this.table = table;
    }

    public TableSectionInspector(TableInspector table, List<ElementInspector> rows) {
        super(table);
        this.table = table;
        this.rows = rows;
    }

    public int rowCount() {
        int rowCount = rows != null ? rows.size() : childNodesByName("TR").size();
        return rowCount;
    }

    public TableRowInspector row(int rowIndex) {
        return createRowInspector(rowIndex);
    }

    protected TableRowInspector createRowInspector(int rowIndex) {
        if (rows == null)
            return new TableRowInspector(this, rowIndex);
        else
            return new TableRowInspector(this, rows.get(rowIndex));
    }

    /**
     * Checks the correctness of the specified style property declarations for all of the cells in the specified column.
     *
     * @param colIndex column index
     * @param style    CSS property declarations that should be checked
     */
    public void assertColumnCellStyles(int colIndex, String style) {
        for (int rowIndex = 0, rowCount = rowCount(); rowIndex < rowCount; rowIndex++) {
            TableRowInspector row = row(rowIndex);
            row.cell(colIndex).assertStyle(style);
        }
    }

    /**
     * Checks the specified CSS property declarations for all cells on a per-column basis. Each column
     * is checked for the styles specified in the appropriate entry of the array passed in the "columnStyles" parameter.
     *
     * @param columnStyles an array of style declarations for all columns. The size of this array should be equal to the
     *                     number of columns in the table.
     */
    public void assertColumnCellStyles(String[] columnStyles) {
        int colCount = columnStyles.length;
        Assert.assertEquals("Checking the number of columns", table.getColumnCount(), colCount);

        for (int colIndex = 0; colIndex < colCount; colIndex++) {
            String expectedColumnStyle = columnStyles[colIndex];
            assertColumnCellStyles(colIndex, expectedColumnStyle);
        }

    }

}