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

/**
 * @author Dmitry Pikhulya
 */
public class TableCellParams {
    private String text;
    private int colSpan;
    private int rowSpan;
    private String styleDeclaration;

    public TableCellParams(String text) {
        this(text, 1, 1);
    }

    public TableCellParams(String text, int colSpan, int rowSpan) {
        this.text = text;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
    }

    public TableCellParams(String text, int colSpan, int rowSpan, String styleDeclaration) {
        this.text = text;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
        this.styleDeclaration = styleDeclaration;
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

    public String getStyleDeclaration() {
        return styleDeclaration;
    }
}