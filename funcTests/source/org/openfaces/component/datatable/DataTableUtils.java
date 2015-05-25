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
package org.openfaces.component.datatable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Darya Shumilina
 */
class DataTableUtils {

    /**
     * List contains data for test samples:
     * -- PopupLayer inside DataTable
     * -- HintLabel inside DataTable
     * -- Major DataTable features testing
     */
    protected static final List<TestDataTableItem> TWO_STRING_COLUMN_LIST = Collections.unmodifiableList(Arrays.asList(
            new TestDataTableItem("col3_row1", "col4_row1"),
            new TestDataTableItem("col3_row2", "col4_row2"),
            new TestDataTableItem("col3_row3", "col4_row3"),
            new TestDataTableItem("col3_row4", "col4_row4"),
            new TestDataTableItem("col3_row5", "col4_row5"),
            new TestDataTableItem("col3_row6", "col4_row6"),
            new TestDataTableItem("col3_row7", "col4_row7"),
            new TestDataTableItem("col3_row8", "col4_row8"),
            new TestDataTableItem("col3_row9", "col4_row9")));

    protected static final List<FeaturesCombinationTestDataTableItem> FEATURES_COMBINATION_LIST = Collections.unmodifiableList(Arrays.asList(
            new FeaturesCombinationTestDataTableItem("id_1", "criterion_1,2,3", "criterion_1,4,7", "1"),
            new FeaturesCombinationTestDataTableItem("id_2", "criterion_1,2,3", "criterion_2,5,8", "2"),
            new FeaturesCombinationTestDataTableItem("id_3", "criterion_1,2,3", "criterion_3,6,9", "3"),
            new FeaturesCombinationTestDataTableItem("id_4", "criterion_4,5,6", "criterion_2,5,8", "4"),
            new FeaturesCombinationTestDataTableItem("id_5", "criterion_4,5,6", "criterion_1,4,7", "5"),
            new FeaturesCombinationTestDataTableItem("id_6", "criterion_4,5,6", "criterion_3,6,9", "6"),
            new FeaturesCombinationTestDataTableItem("id_7", "criterion_7,8,9", "criterion_1,4,7", "7"),
            new FeaturesCombinationTestDataTableItem("id_8", "criterion_7,8,9", "criterion_3,6,9", "8"),
            new FeaturesCombinationTestDataTableItem("id_9", "criterion_7,8,9", "criterion_2,5,8", "9")));

    protected static List<TestDataTableItem> getCurrentPageFromReferenceValues(List<TestDataTableItem> referenceValues, int pageNumber) {
        List<TestDataTableItem> result = new ArrayList<TestDataTableItem>();
        if (pageNumber == 1) result = referenceValues.subList(0, 3);
        else if (pageNumber == 2) result = referenceValues.subList(3, 6);
        else if (pageNumber == 3) result = referenceValues.subList(6, 9);
        return result;
    }

    protected static List<FeaturesCombinationTestDataTableItem> getFilteredValuesFeaturesCombinationTable(String criterion) {
        List<FeaturesCombinationTestDataTableItem> listInstance = FEATURES_COMBINATION_LIST;
        List<FeaturesCombinationTestDataTableItem> result = new ArrayList<FeaturesCombinationTestDataTableItem>();

        for (FeaturesCombinationTestDataTableItem currentItem : listInstance) {
            if (currentItem.getFirstColumn().equals(criterion) ||
                    currentItem.getSecondColumn().equals(criterion) ||
                    currentItem.getThirdColumn().equals(criterion) ||
                    currentItem.getFourthColumn().equals(criterion))
                result.add(currentItem);
        }
        return result;
    }

    protected static class TestDataTableItem {
        private String firstColumn;
        private String secondColumn;

        public TestDataTableItem(String firstColumn, String secondColumn) {
            this.firstColumn = firstColumn;
            this.secondColumn = secondColumn;
        }


        public String getFirstColumn() {
            return firstColumn;
        }

        public String getSecondColumn() {
            return secondColumn;
        }
    }

    protected static class FeaturesCombinationTestDataTableItem {
        private String firstColumn;
        private String secondColumn;
        private String thirdColumn;
        private String fourthColumn;

        public FeaturesCombinationTestDataTableItem(String firstColumn, String secondColumn, String thirdColumn, String fourthColumn) {
            this.firstColumn = firstColumn;
            this.secondColumn = secondColumn;
            this.thirdColumn = thirdColumn;
            this.fourthColumn = fourthColumn;
        }


        public String getFirstColumn() {
            return firstColumn;
        }

        public String getSecondColumn() {
            return secondColumn;
        }

        public String getThirdColumn() {
            return thirdColumn;
        }

        public String getFourthColumn() {
            return fourthColumn;
        }
    }

    public static class TableColumn {
        private int rowIndex;
        private List<String> columnCells = newArrayList();

        public TableColumn(int rowIndex) {
            this.rowIndex = rowIndex;
        }

        public int getRowIndex() {
            return rowIndex;
        }

        public List<String> getAll(){
            return columnCells;
        }

        public String next() {
            return columnCells.iterator().next();
        }

        public boolean hasNext() {
            return columnCells.iterator().hasNext();
        }

        public void add(String s) {
            columnCells.add(s);
        }
    }
}