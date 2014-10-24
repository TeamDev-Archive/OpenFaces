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
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.Rendering;

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
    private List<? extends AbstractRow> rows;
    private ScrollingType scrollingType;
    private boolean indefiniteHeight;
    private boolean addAdditionalRow;

    public TableScrollingArea(
            TableElement parent,
            List<BaseColumn> columns,
            List<? extends AbstractRow> rows,
            ScrollingType scrollingType,
            boolean addAdditionalRow) {
        super(parent);
        this.rows = rows;
        this.columns = columns;
        this.scrollingType = scrollingType;
        this.addAdditionalRow = addAdditionalRow;
    }

    public boolean isIndefiniteHeight() {
        return indefiniteHeight;
    }

    public void setIndefiniteHeight(boolean indefiniteHeight) {
        this.indefiniteHeight = indefiniteHeight;
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

    public List<? extends AbstractRow> getRows() {
        return rows;
    }

    public void render(
            FacesContext context,
            HeaderCell.AdditionalContentWriter additionalContentWriter
    ) throws IOException {
        UIComponent component = getParent(TableStructure.class).getComponent();
        ResponseWriter writer = context.getResponseWriter();
        if (indefiniteHeight) {
            writer.startElement("div", component);
            writer.writeAttribute("class", "o_scrolling_area_container", null);
        }
        if (scrollingType != ScrollingType.NONE) {
            writer.startElement("div", component);
            writer.writeAttribute("class", scrollingType == ScrollingType.HORIZONTAL
                    ? "o_horizontal_scrolling_area" : "o_table_scrolling_area", null);
            if (indefiniteHeight)
                writer.writeAttribute("style", "position: absolute; height: 0;", null);
            // horizontal and vertical scrolling areas require a spacer at the end of the area to accommodate for scroller width
            if (scrollingType == ScrollingType.HORIZONTAL) {
                writer.startElement("table", component);
                writer.writeAttribute("cellspacing", "0", null);
                writer.writeAttribute("cellpadding", "0", null);
                writer.writeAttribute("border", "0", null);
                writer.startElement("tr", component);
                writer.startElement("td", component);
            } else if (scrollingType == ScrollingType.VERTICAL) {
                writer.startElement("table", component);
                writer.writeAttribute("cellspacing", "0", null);
                writer.writeAttribute("cellpadding", "0", null);
                writer.writeAttribute("border", "0", null);
                writer.startElement("tr", component);
                writer.startElement("td", component);
            }
        }

        writeTable(context, writer, component, additionalContentWriter);

        if (scrollingType != ScrollingType.NONE) {
            if (scrollingType == ScrollingType.HORIZONTAL) {
                writer.endElement("td");
                writer.startElement("td", component);
                writeSpacer(context, writer, component);
                writer.endElement("td");
                writer.endElement("tr");
                writer.endElement("table");
            } else if (scrollingType == ScrollingType.VERTICAL) {
                writer.endElement("td");
                writer.endElement("tr");
                writer.startElement("tr", component);
                writer.startElement("td", component);
                writeSpacer(context, writer, component);
                writer.endElement("td");
                writer.endElement("tr");
                writer.endElement("table");
            }

            writer.endElement("div");
        }
        if (indefiniteHeight)
            writer.endElement("div");
    }

    private void writeSpacer(FacesContext context, ResponseWriter writer, UIComponent component) throws IOException {
        writer.startElement("div", component);
        writer.writeAttribute("class", "o_scrolling_area_spacer", null);
        writer.endElement("div");
    }

    private void writeTable(FacesContext context, ResponseWriter writer, UIComponent component, HeaderCell.AdditionalContentWriter additionalContentWriter) throws IOException {
        writer.startElement("table", component);
        writer.writeAttribute("class", "o_scrolling_area_table", null);
        writer.writeAttribute("cellspacing", "0", null);
        Rendering.writeAttribute(writer, "cellpadding", getCellpadding());
        writer.writeAttribute("border", "0", null);
        TableUtil.writeColumnTags(context, component, columns);
        writer.startElement("tbody", component);
        if (addAdditionalRow){
            writer.startElement("tr", component);
            writer.writeAttribute("style","display:none;",null);
            writer.writeAttribute("id","test",null);
            for (int i=0;i<columns.size();i++){
                writer.startElement("td", component);
                writer.endElement("td");
            }
            writer.endElement("tr");
        }
        for (AbstractRow row : rows) {
            row.render(context, additionalContentWriter);
        }
        writer.endElement("tbody");
        writer.endElement("table");
    }

    public boolean isContentSpecified() {
        for (AbstractRow row : rows) {
            if (row.isAtLeastOneComponentInThisRow())
                return true;
        }
        return false;
    }

    public enum ScrollingType {
        NONE, HORIZONTAL, VERTICAL, BOTH
    }
}
