/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2015, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.inspector.components.table;

/**
 * @author Max Yurin
 */
public class TableCellParams {
    private String text;
    private int colSpan;
    private int rowSpan;
    private String style;

    public TableCellParams(String text) {
        this.text = text;
        this.colSpan = 1;
        this.rowSpan = 1;
    }

    public TableCellParams(String text, int colSpan, int rowSpan, String style) {
        this.text = text;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
        this.style = style;
    }

    public String getText() {
        return text;
    }

    public int getColSpan() {
        return colSpan;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public String getStyle() {
        return style;
    }
}
