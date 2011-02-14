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
package org.seleniuminspector.openfaces;

import junit.framework.Assert;

import java.util.Arrays;

import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.html.TableSectionInspector;

/**
 * @author Dmitry Pikhulya
 */
public class DataTableInspector extends AbstractTableInspector {
    public DataTableInspector(String locator) {
        super(locator);
    }

    public DataTableInspector(ElementInspector tableElement) {
        super(tableElement);
    }

    protected TableSectionInspector createSectionInspector(String sectionTagName) {
        return new DataTableSectionInspector(this, sectionTagName);
    }

    public DataTableRowInspector headerRow(int rowIndex) {
        return (DataTableRowInspector) header().row(rowIndex);
    }

    public DataTableRowInspector bodyRow(int rowIndex) {
        return (DataTableRowInspector) body().row(rowIndex);
    }

    public DataTableRowInspector footerRow(int rowIndex) {
        return (DataTableRowInspector) footer().row(rowIndex);
    }

    public void checkSelectedIndex(int expectedRowIndex) {
        int selectedRowIndex = selectedRowIndex();
        Assert.assertEquals("Checking datatable selected index:" + this, expectedRowIndex, selectedRowIndex);
    }

    public int selectedRowIndex() {
        return evalIntExpression("getSelectedRowIndex();");
    }

    public int[] selectedRowIndexes() {
        String indexesAsString = evalExpression("getSelectedRowIndexes();");
        String[] indexesAsStringArray = indexesAsString.split(",");

        int[] indexes = new int[indexesAsStringArray.length];
        for (int i = 0; i < indexesAsStringArray.length; i++) {
            indexes[i] = Integer.parseInt(indexesAsStringArray[i]);
        }

        return indexes;
    }

    public void checkSelectedIndexes(int... expectedIndexes) {
        int[] selectedIndexes = selectedRowIndexes();
        Assert.assertTrue("Checking datatable selected indexes:" + this, Arrays.equals(selectedIndexes, expectedIndexes));
    }

    public int rowCount() {
        return evalIntExpression("getRowCount();");
    }

    public void makeAndCheckSingleSelection(int columnIndex, int rowNumber) {
        column(columnIndex).bodyCell(rowNumber).click();
        checkSelectedIndex(rowNumber);
    }
}
