/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.table;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class ColumnResizingState implements Serializable {
    private Map<String, String> columnWidths = new HashMap<String, String>();
    private String tableWidth;

    public ColumnResizingState() {
    }

    public int getColumnCount() {
        return columnWidths.size();
    }

    public String getColumnWidth(String columnId) {
        return columnWidths.get(columnId);
    }

    public void setColumnWidth(String columnId, String width) {
        columnWidths.put(columnId, width);
    }

    public String getTableWidth() {
        return tableWidth;
    }

    public void setTableWidth(String tableWidth) {
        this.tableWidth = tableWidth;
    }
}
