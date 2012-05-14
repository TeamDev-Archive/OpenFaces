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

import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.ColumnGroup;
import org.openfaces.renderkit.RendererBase;

import javax.faces.component.UIComponent;

/**
 * @author Dmitry Pikhulya
 */
public class BaseColumnRenderer extends RendererBase {
    protected AbstractTable getTable(BaseColumn column) {
        UIComponent parent = column.getParent();
        while (parent instanceof ColumnGroup)
            parent = parent.getParent();
        if (parent == null || !(parent instanceof AbstractTable))
            throw new RuntimeException("Columns can only be inserted into a either DataTable or TreeTable. Column id: " + column.getId() +
                    "; column class: " + column.getClass());
        return (AbstractTable) parent;
    }
}
