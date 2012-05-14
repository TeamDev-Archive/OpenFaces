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

import org.openfaces.component.table.BaseColumn;

import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class SpannedTableCell {
    private BaseColumn column;
    private final List applicableTableCells;

    public SpannedTableCell(BaseColumn column, List applicableTableCells) {
        this.column = column;
        this.applicableTableCells = applicableTableCells;
    }

    public BaseColumn getColumn() {
        return column;
    }

    public void setColumn(BaseColumn column) {
        this.column = column;
    }

    public List getApplicableTableCells() {
        return applicableTableCells;
    }
}
