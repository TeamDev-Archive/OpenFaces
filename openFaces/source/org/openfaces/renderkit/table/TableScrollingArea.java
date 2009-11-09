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

import org.openfaces.component.table.BaseColumn;
import org.openfaces.renderkit.TableUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class TableScrollingArea extends TableElement {
    private List<BaseColumn> columns;
    private List<? extends TableElement> rows;
    private boolean scrollable;

    public TableScrollingArea(
            TableElement parent,
            List<BaseColumn> columns,
            List<? extends TableElement> rows,
            boolean scrollable) {
        super(parent);
        this.rows = rows;
        this.columns = columns;
        this.scrollable = scrollable;
    }

    public void render(
            FacesContext context,
            HeaderCell.AdditionalContentWriter additionalContentWriter
    ) throws IOException {
        UIComponent component = getParent(TableStructure.class).getComponent();
        ResponseWriter writer = context.getResponseWriter();
        if (scrollable) {
            writer.startElement("div", component);
            writer.writeAttribute("class", "o_table_scrolling_area", null);
        }
        writer.startElement("table", component);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("border", "0", null);
        TableUtil.writeColumnTags(context, component, columns);
        writer.startElement("tbody", component);
        for (TableElement row : rows) {
            row.render(context, additionalContentWriter);
        }
        writer.endElement("tbody");
        writer.endElement("table");
        if (scrollable) {
            writer.endElement("div");
        }
    }

    public List<BaseColumn> getColumns() {
        return columns;
    }

    public List<? extends TableElement> getRows() {
        return rows;
    }
}
