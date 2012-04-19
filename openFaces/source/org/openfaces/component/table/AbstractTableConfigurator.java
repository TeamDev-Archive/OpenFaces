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

package org.openfaces.component.table;

import org.openfaces.component.ComponentConfigurator;
import org.openfaces.util.Components;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AbstractTableConfigurator extends UIComponentBase implements ComponentConfigurator {
    private AbstractTable table;

    public UIComponent getConfiguredComponent() {
        return getTable();
    }

    public AbstractTable getTable() {
        if (table == null) {
            table = (AbstractTable) Components.checkParentTag(this, DataTable.class, TreeTable.class);
        }
        return table;
    }

    protected void setTable(AbstractTable table) {
        this.table = table;
    }
}
