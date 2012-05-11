/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.renderkit.table;

import org.openfaces.component.table.Cell;
import org.openfaces.component.table.Row;

import javax.faces.component.UIComponent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class CustomContentCellRenderingInfo extends CustomCellRenderingInfo implements Serializable {
    private int tableRowDeclarationIndex;
    private int tableCellDeclarationIndex;

    public CustomContentCellRenderingInfo(int tableRowDeclarationIndex, int tableCellDeclarationIndex) {
        this.tableRowDeclarationIndex = tableRowDeclarationIndex;
        this.tableCellDeclarationIndex = tableCellDeclarationIndex;
    }

    public Cell findTableCell(List customRows) {
        if (tableRowDeclarationIndex == -1) {
            // can be the case for synthetic group header row cells
            return null;
        }
        Row tableRow = (Row) customRows.get(tableRowDeclarationIndex);
        List<Cell> cells = new ArrayList<Cell>();
        List<UIComponent> children = tableRow.getChildren();
        for (UIComponent child : children) {
            if (child instanceof Cell)
                cells.add((Cell) child);
        }
        Cell result = cells.get(tableCellDeclarationIndex);
        return result;
    }
}
