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
package org.openfaces.component.table;

import java.io.Serializable;

/**
 * @author Dmitry Pikhulya
 */
public class ColumnResizingState implements Serializable {
    private final String[] columnWidths;
    private final String tableWidth;

    public ColumnResizingState(String[] columnWidths, String tableWidth) {
        this.columnWidths = columnWidths;
        this.tableWidth = tableWidth;
    }

    public int getColumnCount() {
        return columnWidths.length;
    }

    public String getColumnWidth(int columnIndex) {
        return columnWidths[columnIndex];
    }

    public String getTableWidth() {
        return tableWidth;
    }
}
