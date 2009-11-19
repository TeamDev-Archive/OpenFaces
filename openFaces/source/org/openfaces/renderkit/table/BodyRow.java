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

import org.openfaces.component.table.TableRow;
import org.openfaces.util.RenderingUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class BodyRow extends TableElement {
    private List<TableRow> applicableCustomRows;
    private String[][] attributes;
    private List<BodyCell> cells;
    private String style;
    private String styleClass;

    public BodyRow() {
    }

    public BodyRow(TableElement parent) {
        super(parent);
    }

    public void render(FacesContext context, HeaderCell.AdditionalContentWriter additionalContentWriter) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("tr", null);
        if (style != null || styleClass != null)
            RenderingUtil.writeStyleAndClassAttributes(writer, style, styleClass);
        writeCustomRowOrCellEvents(writer, applicableCustomRows);
        if (attributes != null)
            for (String[] attribute : attributes) {
                writer.writeAttribute(attribute[0], attribute[1], null);
            }

        for (int i = 0, count = cells.size(); i < count; i++) {
            BodyCell cell = cells.get(i);
            cell.render(context, i == count - 1 ? additionalContentWriter : null);
        }

        writer.endElement("tr");
    }

    public void setApplicableCustomRows(List<TableRow> applicableCustomRows) {
        this.applicableCustomRows = applicableCustomRows;
    }

    public static void writeCustomRowOrCellEvents(ResponseWriter writer, List<? extends UIComponent> customRowsOrCells) throws IOException {
        if (customRowsOrCells == null || customRowsOrCells.size() == 0)
            return;
        String[] eventNames = new String[]{
                "onclick", "ondblclick", "onmousedown", "onmouseover", "onmousemove",
                "onmouseout", "onmouseup", "onkeydown", "onkeyup", "onkeypress"};

        for (String eventName : eventNames) {
            String compoundEventHandler = null;
            for (UIComponent customRowOrCell : customRowsOrCells) {
                String eventHandler = (String) customRowOrCell.getAttributes().get(eventName);
                compoundEventHandler = RenderingUtil.joinScripts(compoundEventHandler, eventHandler);
            }
            if (compoundEventHandler != null && compoundEventHandler.length() > 0)
                writer.writeAttribute(eventName, compoundEventHandler, null);
        }
    }

    public void setAttributes(String[][] attributes) {
        this.attributes = attributes;
    }

    public List<BodyCell> getCells() {
        return cells;
    }

    public void setCells(List<BodyCell> cells) {
        this.cells = cells;
        for (BodyCell cell : cells) {
            cell.setParent(this);
        }
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }
}
