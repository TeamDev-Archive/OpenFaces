/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.renderkit.table;

import org.openfaces.util.Rendering;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class HeaderRow extends AbstractRow {
    private final List<HeaderCell> cells;
    private boolean atLeastOneComponentInThisRow;
    private List<HeaderRow> rowsForSpans;

    public HeaderRow(TableHeaderOrFooter parent, boolean atLeastOneComponentInThisRow, List<HeaderCell> cells) {
        super(parent);
        this.atLeastOneComponentInThisRow = atLeastOneComponentInThisRow;
        this.cells = cells;
        for (HeaderCell cell : cells) {
            cell.setParent(this);
        }
    }

    public boolean isAtLeastOneComponentInThisRow() {
        return atLeastOneComponentInThisRow;
    }

    public void setAtLeastOneComponentInThisRow(boolean rowContentSpecified) {
        atLeastOneComponentInThisRow = rowContentSpecified;
    }

    public List<HeaderCell> getCells() {
        return cells;
    }

    public void render(FacesContext context,
                       HeaderCell.AdditionalContentWriter lastCellContentAppender) throws IOException {
        if (!isAtLeastOneComponentInThisRow())
            return;
        ResponseWriter writer = context.getResponseWriter();
        TableStructure tableStructure = getParent(TableStructure.class);
        writer.startElement("tr", tableStructure.getComponent());
        for (int i = 0, count = cells.size(); i < count; i++) {
            HeaderCell cell = cells.get(i);
            cell.render(context, i == count - 1 ? lastCellContentAppender : null);
        }
        writer.endElement("tr");
        Rendering.writeNewLine(context.getResponseWriter());
    }

    public List<HeaderRow> getRowsForSpans() {
        return rowsForSpans;
    }

    public void setRowsForSpans(List<HeaderRow> rows) {
        this.rowsForSpans = rows;
    }
}
