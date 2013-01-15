/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.datatable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Darya Shumilina
 */
public class SelectionBean {

    private List<MultipleSelectionItem> multipleSelectionCollection = new ArrayList<MultipleSelectionItem>();
    private List<MultipleSelectionItem> selectedRows = new ArrayList<MultipleSelectionItem>();
    private List<Integer> rowIndexes = new ArrayList<Integer>();
    private MultipleSelectionItem selectedRow;
    private int rowIndex;

    public SelectionBean() {
        selectedRows.add(new MultipleSelectionItem("col1_row5", "col2_row5"));
        selectedRows.add(new MultipleSelectionItem("col1_row3", "col2_row3"));
        rowIndexes.add(1);
        rowIndexes.add(3);
        rowIndexes.add(5);
        selectedRow = new MultipleSelectionItem("col1_row1", "col2_row1");
        rowIndex = 3;

        multipleSelectionCollection.add(new MultipleSelectionItem("col1_row1", "col2_row1"));
        multipleSelectionCollection.add(new MultipleSelectionItem("col1_row2", "col2_row2"));
        multipleSelectionCollection.add(new MultipleSelectionItem("col1_row3", "col2_row3"));
        multipleSelectionCollection.add(new MultipleSelectionItem("col1_row4", "col2_row4"));
        multipleSelectionCollection.add(new MultipleSelectionItem("col1_row5", "col2_row5"));
        multipleSelectionCollection.add(new MultipleSelectionItem("col1_row6", "col2_row6"));
        multipleSelectionCollection.add(new MultipleSelectionItem("col1_row7", "col2_row7"));
        multipleSelectionCollection.add(new MultipleSelectionItem("col1_row8", "col2_row8"));
        multipleSelectionCollection.add(new MultipleSelectionItem("col1_row9", "col2_row9"));
    }

    public List<MultipleSelectionItem> getMultipleSelectionCollection() {
        return multipleSelectionCollection;
    }

    public List<MultipleSelectionItem> getSelectedRows() {
        return selectedRows;
    }

    public void setSelectedRows(List<MultipleSelectionItem> selectedRows) {
        this.selectedRows = selectedRows;
    }

    public List<Integer> getRowIndexes() {
        return rowIndexes;
    }

    public void setRowIndexes(List<Integer> rowIndexes) {
        this.rowIndexes = rowIndexes;
    }

    public MultipleSelectionItem getSelectedRow() {
        return selectedRow;
    }

    public void setSelectedRow(MultipleSelectionItem selectedRow) {
        this.selectedRow = selectedRow;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public static class MultipleSelectionItem implements Serializable {
        private String firstColumn;
        private String secondColumn;

        public MultipleSelectionItem(String firstColumn, String secondColumn) {
            this.firstColumn = firstColumn;
            this.secondColumn = secondColumn;
        }

        public String getFirstColumn() {
            return firstColumn;
        }

        public String getSecondColumn() {
            return secondColumn;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            MultipleSelectionItem that = (MultipleSelectionItem) o;

            if (!firstColumn.equals(that.firstColumn)) {
                return false;
            }
            if (!secondColumn.equals(that.secondColumn)) {
                return false;
            }

            return true;
        }

        public int hashCode() {
            int result;
            result = firstColumn.hashCode();
            result = 31 * result + secondColumn.hashCode();
            return result;
        }

    }

}