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

import org.openfaces.util.RenderingUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class HeaderRow {
    private final boolean atLeastOneComponentInThisRow;
    private final List<HeaderCell> cells;

    public HeaderRow(boolean atLeastOneComponentInThisRow, List<HeaderCell> cells) {
        this.atLeastOneComponentInThisRow = atLeastOneComponentInThisRow;
        this.cells = cells;
    }

    public boolean isAtLeastOneComponentInThisRow() {
        return atLeastOneComponentInThisRow;
    }

    public List<HeaderCell> getCells() {
        return cells;
    }

    public void render(FacesContext context, UIComponent table, List<HeaderRow> rows,
                       HeaderCell.AdditionalContentWriter lastCellContentAppender) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("tr", table);
        for (int i = 0, count = cells.size(); i < count; i++) {
            HeaderCell cell = cells.get(i);
            cell.render(context, rows, i == count - 1 ? lastCellContentAppender : null);
        }
        writer.endElement("tr");
        RenderingUtil.writeNewLine(context.getResponseWriter());
    }
}
