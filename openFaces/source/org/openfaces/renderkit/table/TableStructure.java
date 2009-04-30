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
package org.openfaces.renderkit.table;

import org.openfaces.component.TableStyles;
import org.openfaces.component.table.BaseColumn;

import javax.faces.component.UIComponent;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class TableStructure {
    private final UIComponent table;
    private final TableStyles tableStyles;
    private final List<BaseColumn> columns;

    private final TableHeader header;
    private final TableFooter footer;


    public TableStructure(UIComponent component, TableStyles tableStyles) {
        table = component;
        this.tableStyles = tableStyles;
        columns = tableStyles.getColumnsForRendering();

        header = new TableHeader(this);
        footer = new TableFooter(this);
    }

    public UIComponent getTable() {
        return table;
    }

    public TableStyles getTableStyles() {
        return tableStyles;
    }

    public List<BaseColumn> getColumns() {
        return columns;
    }

    public TableHeader getHeader() {
        return header;
    }

    public TableFooter getFooter() {
        return footer;
    }
}
