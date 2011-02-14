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

package org.openfaces.testapp.treetable;

import org.openfaces.component.table.FilterKind;
import org.openfaces.component.table.TreePath;
import org.openfaces.util.Faces;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
public class TreeTableColumnsBean {

    private List<TreeTableItem> rootMessages = new ArrayList<TreeTableItem>();

    private List<SelectItem> availableColumns = new ArrayList<SelectItem>();
    private List<String> selectedColumns = new ArrayList<String>();
    private List<SelectItem> usedColumns;
    private List<String> renderedColumns = new ArrayList<String>();
    private List<String> dynamicColumns = new ArrayList<String>();
    private List<String> dynamicColumns1 = Arrays.asList("1", "4");
    private static TreeTableItem treeTableItem = new TreeTableItem("child child 1", "Field 1.1.1.1", "Field 1.1.1.1", 3, 9, null);
    private static TreeTableItem treeTableItemChild = new TreeTableItem("child 1", "Field 1.1.1", "Field2.1.1", 2, 4, Arrays.asList(treeTableItem,
            new TreeTableItem("child child 2", "Field 1.1.1.2", "Field 2.1.1.2", 4, 16, null)));
    private static TreeTableItem treeTableItemRoot = new TreeTableItem("root 1", "Field 1.1", "Field 2.1", 1, 1, Arrays.asList(treeTableItemChild, new TreeTableItem("child 2", "Field 1.1.2", "Field 2.1.2", 5, 25, Arrays.asList(new TreeTableItem("child child 3", "Field 1.1.2.1", "Field 2.1.2.1", 6, 36, null))), new TreeTableItem("child 3", "Field 1.1.3", "Field 2.1.3", 7, 49, null)));

    public TreeTableColumnsBean() {
        rootMessages.add(treeTableItemRoot);
        rootMessages.add(new TreeTableItem("root 2", "Field 1.2", "Field 2.2", 8, 64, Arrays.asList(new TreeTableItem("child 4", "Field 1.2.1", "Field 2.2.1", 9, 81, Arrays.asList(new TreeTableItem("child child 4", "Field 1.2.1.1", "Field 2.2.1.1", 10, 100, null),
                new TreeTableItem("child child 5", "Field 1.2.1.2", "Field 2.2.1.2", 11, 121, null))), new TreeTableItem("child 5", "Field 1.2.2", "Field 2.2.2", 12, 144, Arrays.asList(new TreeTableItem("child child 6", "Field 1.2.2.1", "Field 2.2.2.1", 13, 169, null))), new TreeTableItem("child 6", "Field 1.2.3", "Field 2.2.3", 14, 196, null))));
        rootMessages.add(new TreeTableItem("root 3", "Field 1.3", "Field 2.3", 15, 225, null));
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

    public List getNodeChildren() {
        TreeTableItem item = getNode();
        return item != null ? item.getChildren() : rootMessages;
    }

    public boolean getNodeHasChildren() {
        TreeTableItem item = getNode();
        return item.hasChildren();
    }

    private TreeTableItem getNode() {
        return (TreeTableItem) Faces.var("node");
    }

    public boolean isColumnRendered() {
        String colData = getCurrentColumn();
        return renderedColumns.contains(colData);
    }

    public boolean isSortingEnabled() {
        String colData = getCurrentColumn();
        try {
            int colIndex = Integer.parseInt(colData);
            return colIndex % 2 == 0;
        } catch (NumberFormatException e) {
            // if col not a nubmer
            return true;
        }
    }

    private String getCurrentColumn() {
        return (String) Faces.var("col");
    }

    public FilterKind getFilterKind() {
        String col = getCurrentColumn();
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
            // if col not a nubmer
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

    public void updateColumnsList(ActionEvent event) {
        dynamicColumns = new ArrayList<String>();
        for (String mySelectedColumn : selectedColumns) {
            dynamicColumns.add(mySelectedColumn);
        }
    }

    public String getColumnStyle() {
        String col = getCurrentColumn();
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
        String col = getCurrentColumn();
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

    public List<SelectItem> getAvailableColumns() {
        return availableColumns;
    }

    public List<String> getSelectedColumns() {
        return selectedColumns;
    }

    public List<String> getRenderedColumns() {
        return renderedColumns;
    }

    public List<String> getDynamicColumns() {
        return dynamicColumns;
    }

    public void setSelectedColumns(List<String> selectedColumns) {
        this.selectedColumns = selectedColumns;
    }

    public void setRenderedColumns(List<String> renderedColumns) {
        this.renderedColumns = renderedColumns;
    }

    public void setUsedColumns(List<SelectItem> usedColumns) {
        this.usedColumns = usedColumns;
    }

    public List<String> getDynamicColumns1() {
        return dynamicColumns1;
    }

    public boolean isHighlightedByNodePath() {
        TreePath path = (TreePath) Faces.var("nodePath");
        TreePath pathToCompare = new TreePath(treeTableItem, new TreePath(treeTableItemChild, new TreePath(treeTableItemRoot, null)));
        return path.equals(pathToCompare);
    }
}