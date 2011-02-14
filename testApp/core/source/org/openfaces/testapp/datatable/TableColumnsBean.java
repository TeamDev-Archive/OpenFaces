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

package org.openfaces.testapp.datatable;

import org.openfaces.component.table.FilterKind;
import org.openfaces.util.Faces;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
public class TableColumnsBean {
    private List<TableItem> tableValue = new ArrayList<TableItem>();
    private List<SelectItem> availableColumns = new ArrayList<SelectItem>();
    private List<String> selectedColumns = new ArrayList<String>();
    private List<SelectItem> usedColumns;
    private List<String> renderedColumns = new ArrayList<String>();
    private List<String> dynamicColumns = new ArrayList<String>();
    private List<String> dynamicColumns2 = Arrays.asList("1", "2", "3");

    public TableColumnsBean() {
        for (int i = 0; i < 25; i++) {
            tableValue.add(new TableItem("Name " + i, "Field 1." + i, "Field 2." + i, i, i * i));
        }
        for (int i = 0; i < 8; i++) {
            availableColumns.add(new SelectItem(String.valueOf(i), String.valueOf(i)));
            selectedColumns.add(String.valueOf(i));
            renderedColumns.add(String.valueOf(i));
            dynamicColumns.add(String.valueOf(i));
        }
        String columnName = "Character";
        availableColumns.add(new SelectItem(columnName, columnName));
        selectedColumns.add(columnName);
        renderedColumns.add(columnName);
        dynamicColumns.add(columnName);
    }

    public boolean isColumnRendered() {
        String colData = Faces.var("col", String.class);
        return renderedColumns.contains(colData);
    }

    public boolean isSortingEnabled() {
        String colData = Faces.var("col", String.class);
        try {
            int colIndex = Integer.parseInt(colData);
            return colIndex % 2 == 0;
        } catch (NumberFormatException e) {
            // if col not a number
            return true;
        }
    }

    public FilterKind getFilterKind() {
        String col = Faces.var("col", String.class);
        try {
            int colIndex = Integer.parseInt(col);
            if (colIndex < 4) {
                return FilterKind.DROP_DOWN_FIELD;
            } else if (colIndex == 4) {
                return null;
            } else {
                return FilterKind.COMBO_BOX;
            }
        } catch (NumberFormatException e) {
            // if col not a number
            return FilterKind.SEARCH_FIELD;
        }
    }

    public List<SelectItem> getUsedColumns() {
        usedColumns = new ArrayList<SelectItem>();
        for (String mySelectedColumn : selectedColumns) {
            usedColumns.add(new SelectItem(mySelectedColumn, mySelectedColumn));
        }
        return usedColumns;
    }

    public void setUsedColumns(List<SelectItem> usedColumns) {
        this.usedColumns = usedColumns;
    }

    public void updateColumnsList(ActionEvent event) {
        dynamicColumns = new ArrayList<String>();
        for (String mySelectedColumn : selectedColumns) {
            dynamicColumns.add(mySelectedColumn);
        }

    }

    public List<String> getDynamicColumns() {
        return dynamicColumns;
    }

    public String getColumnStyle() {
        String col = Faces.var("col", String.class);
        String defaultStyle = "width: 150px; text-align: left;";
        String columnStyle;
        try {
            if (Integer.parseInt(col) < 5) {
                columnStyle = "width: 50px; text-align: right;";
            } else {
                columnStyle = "width: 100px; text-align: right;";
            }
        } catch (NumberFormatException e) {
            // if col not a nubmer
            columnStyle = defaultStyle;
        }
        return columnStyle;
    }

    public List getFilterValues() {
        String col = Faces.var("col", String.class);
        List<String> filterValues = new ArrayList<String>();
        for (int i = 0; i < 9; i++) {
            filterValues.add(String.valueOf(i));
        }
        try {
            int conIndex = Integer.parseInt(col);
            if (conIndex < 4) {
                return filterValues;
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            // if col not a nubmer
            return null;
        }
    }

    public List<String> getRenderedColumns() {
        return renderedColumns;
    }

    public void setRenderedColumns(List<String> renderedColumns) {
        this.renderedColumns = renderedColumns;
    }

    public List<String> getSelectedColumns() {
        return selectedColumns;
    }

    public void setSelectedColumns(List<String> selectedColumns) {
        this.selectedColumns = selectedColumns;
    }

    public List<TableItem> getTableValue() {
        return tableValue;
    }

    public List<SelectItem> getAvailableColumns() {
        return availableColumns;
    }

    public List<String> getDynamicColumns2() {
        return dynamicColumns2;
    }
}
