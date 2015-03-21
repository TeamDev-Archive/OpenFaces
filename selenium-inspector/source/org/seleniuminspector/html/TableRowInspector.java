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
package org.seleniuminspector.html;

import junit.framework.Assert;
import org.seleniuminspector.ElementByReferenceInspector;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.SubElementByPathInspector;

/**
 * @author Dmitry Pikhulya
 */
public class TableRowInspector extends ElementByReferenceInspector {
    private TableSectionInspector section;

    public TableRowInspector(TableSectionInspector section, int rowIndex) {
        super(new SubElementByPathInspector(section, "TR[" + rowIndex + "]"));
        this.section = section;
    }

    public TableRowInspector(TableSectionInspector section, ElementInspector row) {
        super(row);
        this.section = section;
    }

    protected TableSectionInspector getSection() {
        return section;
    }

    /**
     * Ensures that this row's cells are specified correctly according to the passed parameters.
     *
     * @param cellParamsArray that contains expected parameters for all of the row's cells. The size of this array
     *                        should be the same as the number of cells in this row.
     */
    public void assertCellParams(TableCellParams[] cellParamsArray) {
        Assert.assertEquals("Checking number of cells at " + this, String.valueOf(cellParamsArray.length),
                evalExpression("cells.length"));
        for (int i = 0, count = cellParamsArray.length; i < count; i++) {
            TableCellParams params = cellParamsArray[i];
            cell(i).assertStructureParams(params);
        }
    }

    public TableCellInspector cell(int cellIndex) {
        return new TableCellInspector(this, cellIndex);
    }

    public int cellCount() {
        return Integer.parseInt(evalExpression("cells.length"));
    }

    /**
     * Checks that all of the CSS property declarations specified in the passed style are actually applied to all of
     * this row's cells.
     */
    public void assertCellStyles(String style) {
        for (int i = 0, count = cellCount(); i < count; i++) {
            cell(i).assertStyle(style);
        }
    }

}