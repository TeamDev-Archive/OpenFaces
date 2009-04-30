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
package org.openfaces.test.html;

import junit.framework.Assert;
import org.openfaces.test.SubElementByExpressionInspector;

/**
 * @author Dmitry Pikhulya
 */
public class TableCellInspector extends SubElementByExpressionInspector {
    public TableCellInspector(TableRowInspector row, int cellIndex) {
        super(row, "cells[" + cellIndex + "]");
    }

    public int colSpan() {
        String colSpanStr = evalExpression("colSpan");
        if (colSpanStr == null || colSpanStr.length() == 0)
            return 1;
        return Integer.parseInt(colSpanStr);
    }

    public int rowSpan() {
        String rowSpanStr = evalExpression("rowSpan");
        if (rowSpanStr == null || rowSpanStr.length() == 0)
            return 1;
        return Integer.parseInt(rowSpanStr);
    }

    public void assertStructureParams(TableCellParams params) {
        if (params.getText() != null)
            Assert.assertEquals("Checking cell text at " + this, params.getText(), text());
        Assert.assertEquals("Checking cell colSpan at " + this, params.getColSpan(), colSpan());
        Assert.assertEquals("Checking cell rowSpan at " + this, params.getRowSpan(), rowSpan());
        String style = params.getStyleDeclaration();
        if (style != null)
            assertStyle(style);
    }

    public boolean isChecked() {
        String checked = subElement("input").evalExpression("checked");
        return checked.equals("true");
    }

    public void assertChecked(boolean checked) {
        Assert.assertEquals("Checking table selection check-box state: " + this, checked, isChecked());
    }
}