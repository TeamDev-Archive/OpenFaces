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
import org.openfaces.util.RenderingUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class TableScrollingArea extends TableElement {
    private String cellpadding;
    private List<BaseColumn> columns;
    private List<? extends TableElement> rows;
    private boolean scrollable;
    private boolean indefiniteHight;

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

    public boolean isIndefiniteHight() {
        return indefiniteHight;
    }

    public void setIndefiniteHight(boolean indefiniteHight) {
        this.indefiniteHight = indefiniteHight;
    }

    public String getCellpadding() {
        return cellpadding;
    }

    public void setCellpadding(String cellpadding) {
        this.cellpadding = cellpadding;
    }

    public List<BaseColumn> getColumns() {
        return columns;
    }

    public List<? extends TableElement> getRows() {
        return rows;
    }

    public void render(
            FacesContext context,
            HeaderCell.AdditionalContentWriter additionalContentWriter
    ) throws IOException {
        UIComponent component = getParent(TableStructure.class).getComponent();
        ResponseWriter writer = context.getResponseWriter();
        if (indefiniteHight) {
            writer.startElement("div", component);
            writer.writeAttribute("class", "o_scrolling_area_container", null);
        }
        if (scrollable) {
            writer.startElement("div", component);
            writer.writeAttribute("class", "o_table_scrolling_area", null);
            if (indefiniteHight)
                writer.writeAttribute("style", "position: absolute; height: 0;", null);
        }
        writer.startElement("table", component);
        writer.writeAttribute("class", "o_scrolling_area_table", null);
        writer.writeAttribute("cellspacing", "0", null);
        RenderingUtil.writeAttribute(writer, "cellpadding", getCellpadding());
        writer.writeAttribute("border", "0", null);
        TableUtil.writeColumnTags(context, component, columns);
        writer.startElement("tbody", component);
        for (TableElement row : rows) {
            row.render(context, additionalContentWriter);
        }
        writer.endElement("tbody");
        writer.endElement("table");
        if (scrollable)
            writer.endElement("div");
        if (indefiniteHight)
            writer.endElement("div");
    }
}
