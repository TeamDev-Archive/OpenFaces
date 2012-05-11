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
package org.openfaces.renderkit;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class TableRenderer {
    private String id;
    private String styleClass;
    private Integer cellspacing;
    private Integer cellpadding;
    private Integer border;
    private ThreadLocal<UIComponent> cellComponent = new ThreadLocal<UIComponent>();

    public TableRenderer() {
    }

    public TableRenderer(String id, int cellspacing, int cellpadding, int border, String styleClass) {
        this.id = id;
        this.cellspacing = cellspacing;
        this.cellpadding = cellpadding;
        this.border = border;
        this.styleClass = styleClass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public Integer getCellspacing() {
        return cellspacing;
    }

    public void setCellspacing(Integer cellspacing) {
        this.cellspacing = cellspacing;
    }

    public Integer getCellpadding() {
        return cellpadding;
    }

    public void setCellpadding(Integer cellpadding) {
        this.cellpadding = cellpadding;
    }

    public Integer getBorder() {
        return border;
    }

    public void setBorder(Integer border) {
        this.border = border;
    }

    public void render(UIComponent component, int rowCount, int colCount) throws IOException {
        render(component, false, rowCount, colCount);
    }

    public void render(UIComponent component, boolean renderColTags, int rowCount, int colCount) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("table", component);
        writeTableAttributes(context, writer, component);
        if (renderColTags) {
            for (int colIndex = 0; colIndex < colCount; colIndex++) {
                writer.startElement("col", null);
                writeColAttributes(writer, colIndex);
                writer.endElement("col");
            }
        }
        encodeTHead(writer, component);
        writer.startElement("tbody", component);
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            writer.startElement("tr", component);
            writeRowAttributes(writer, rowIndex);
            for (int colIndex = 0; colIndex < colCount; colIndex++) {
                writer.startElement("td", component);
                writeCellAttributes(writer, rowIndex, colIndex);
                encodeCellContents(context, writer, component, rowIndex, colIndex);
                writer.endElement("td");
            }
            writer.endElement("tr");
        }
        writer.endElement("tbody");
        encodeTFoot(writer, component);
        writer.endElement("table");
    }

    protected void writeRowAttributes(ResponseWriter writer, int rowIndex) throws IOException {
    }

    protected void encodeTHead(ResponseWriter writer, UIComponent component) throws IOException {

    }

    protected void encodeTFoot(ResponseWriter writer, UIComponent component) throws IOException {

    }

    protected void writeColAttributes(ResponseWriter writer, int colIndex) throws IOException {
    }

    protected void writeTableAttributes(FacesContext context, ResponseWriter writer, UIComponent component) throws IOException {
        if (id != null)
            writer.writeAttribute("id", id, null);
        if (cellspacing != null)
            writer.writeAttribute("cellspacing", cellspacing.toString(), null);
        if (cellpadding != null)
            writer.writeAttribute("cellpadding", cellpadding.toString(), null);
        if (border != null)
            writer.writeAttribute("border", border.toString(), null);
        if (styleClass != null)
            writer.writeAttribute("class", styleClass, null);
    }

    protected void encodeCellContents(FacesContext context, ResponseWriter writer, UIComponent component, int rowIndex, int colIndex) throws IOException {
        UIComponent cellComponent = this.cellComponent.get();
        if (cellComponent != null)
            cellComponent.encodeAll(context);
    }

    public void render(UIComponent parent, UIComponent[][] components) throws IOException {
        int rowCount = components.length;
        int maxColCount = 0;
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            UIComponent[] rowComponents = components[rowIndex];
            maxColCount = Math.max(maxColCount, rowComponents.length);
        }

        FacesContext context = FacesContext.getCurrentInstance();
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("table", parent);
        writeTableAttributes(context, writer, parent);       
        writer.startElement("tbody", parent);
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            if (!isRowVisible(rowIndex))
                continue;
            writer.startElement("tr", parent);
            writeRowAttributes(writer, rowIndex);
            UIComponent[] rowComponents = components[rowIndex];
            for (int cellIndex = 0, cellCount = rowComponents.length; cellIndex < maxColCount; cellIndex++) {
                writer.startElement("td", parent);
                boolean lastCellInIncompleteRow = cellIndex == cellCount - 1 && cellCount < maxColCount;
                if (lastCellInIncompleteRow)
                    writer.writeAttribute("colspan", String.valueOf(1 + maxColCount - cellCount), null);
                writeCellAttributes(writer, rowIndex, cellIndex);

                UIComponent cellComponent = rowComponents[cellIndex];
                this.cellComponent.set(cellComponent);
                encodeCellContents(context, writer, parent, rowIndex, cellIndex);
                this.cellComponent.remove();

                writer.endElement("td");
                if (lastCellInIncompleteRow)
                    break;
            }
            writer.endElement("tr");
        }
        writer.endElement("tbody");
        writer.endElement("table");
    }

    protected void writeCellAttributes(ResponseWriter writer, int rowIndex, int cellIndex) throws IOException {
    }

    protected boolean isRowVisible(int rowIndex) {
        return true;
    }
}
