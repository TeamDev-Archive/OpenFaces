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

import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.DynamicCol;
import org.openfaces.util.Environment;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
class HeaderCell extends TableElement {
    private BaseColumn column;
    private Object content;
    private int colSpan;
    private int rowIndexMin;
    private int rowIndexMax;

    private String cellTag;
    private boolean renderNonBreakable;
    private boolean renderSortingToggle;
    private CellKind cellKind;
    private boolean escapeText;

    public HeaderCell(BaseColumn column, Object content, String cellTag, CellKind cellKind) {
        this(column, content, cellTag, cellKind, false, false);
    }

    public HeaderCell(BaseColumn column, Object content, String cellTag, CellKind cellKind, boolean renderNonBreakable, boolean renderSortingToggle) {
        this.column = column;
        this.content = content;
        this.cellTag = cellTag;
        this.cellKind = cellKind;
        this.renderNonBreakable = renderNonBreakable;
        this.renderSortingToggle = renderSortingToggle;
    }

    public CellKind getCellKind() {
        return cellKind;
    }

    public void setSpans(int colSpan, int rowIndex1, int rowIndex2) {
        this.colSpan = colSpan;
        rowIndexMin = Math.min(rowIndex1, rowIndex2);
        rowIndexMax = Math.max(rowIndex1, rowIndex2);
    }

    public BaseColumn getColumn() {
        return column;
    }

    public Object getContent() {
        return content;
    }

    public int getColSpan() {
        return colSpan;
    }

    public void setEscapeText(boolean escapeText) {
        this.escapeText = escapeText;
    }

    public interface AdditionalContentWriter {
        public void writeAdditionalContent(FacesContext context) throws IOException;
    }

    public void render(FacesContext facesContext,
                       AdditionalContentWriter additionalContentWriter) throws IOException {
        List<HeaderRow> rows = getParent(HeaderRow.class).getRowsForSpans();
        TableStructure tableStructure = getParent(TableStructure.class);
        UIComponent table = tableStructure.getComponent();
        if (column instanceof DynamicCol)
            ((DynamicCol) column).declareContextVariables();

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(cellTag, table);
        if (colSpan > 1)
            writer.writeAttribute("colspan", String.valueOf(colSpan), null);
        int rowSpan = 0;
        if (rowIndexMin != rowIndexMax) {
            if (rows == null)
                throw new IllegalArgumentException("'rows' parameter must be specified for vertically-spanned cells (rowIndexMin != rowIndexMax)");
            for (int i = rowIndexMin; i <= rowIndexMax; i++) {
                HeaderRow row = rows.get(i);
                if (row.isAtLeastOneComponentInThisRow())
                    rowSpan++;
            }
            if (rowSpan != 1)
                writer.writeAttribute("rowspan", String.valueOf(rowSpan), null);
        }

        if (renderNonBreakable) {
            if (Environment.isExplorer())
                writer.startElement("span", table);
            writer.writeAttribute("class", "o_noWrapHeaderCell", null);
        }
        DynamicCol dynamicCol = column instanceof DynamicCol ? (DynamicCol) column : null;
        if (dynamicCol != null) dynamicCol.declareContextVariables();
        try {
            if (content != null) {
                if (content instanceof UIComponent) {
                    UIComponent uiComponent = (UIComponent) content;
                    uiComponent.encodeAll(facesContext);
                    if (TableStructure.isComponentEmpty(uiComponent) && tableStructure.isEmptyCellsTreatmentRequired())
                        Rendering.writeNonBreakableSpace(writer);
                } else if (content instanceof TableElement)
                    ((TableElement) content).render(facesContext, null);
                else {
                    if (escapeText)
                        writer.writeText(content.toString(), null);
                    else
                        writer.write(content.toString());
                }
            } else if (tableStructure.isEmptyCellsTreatmentRequired())
                Rendering.writeNonBreakableSpace(writer);
        } finally {
            if (dynamicCol != null) dynamicCol.undeclareContextVariables();
        }

        if (renderSortingToggle && column != null && table instanceof AbstractTable)
            renderColumnSortingDirection(facesContext, (AbstractTable) table, column);
        if (renderNonBreakable) {
            if (Environment.isExplorer())
                writer.endElement("span");
        }

        if (additionalContentWriter != null)
            additionalContentWriter.writeAdditionalContent(facesContext);

        writer.endElement(cellTag);

        if (column instanceof DynamicCol)
            ((DynamicCol) column).undeclareContextVariables();
    }

    protected void renderColumnSortingDirection(
            FacesContext facesContext, AbstractTable table, BaseColumn column) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String thisColumnId = column.getId();
        String sortedColumnId = table.getSortColumnId();
        if (thisColumnId != null && thisColumnId.equals(sortedColumnId)) {
            String imageUrl = table.isSortAscending()
                    ? getSortedAscendingImageUrl(facesContext, table)
                    : getSortedDescendingImageUrl(facesContext, table);
            writer.startElement("img", table);
            writer.writeAttribute("src", imageUrl, null);
            writer.writeAttribute("style", "margin-left: 5px", null);
            writer.endElement("img");
        }
    }

    public static String getSortedAscendingImageUrl(FacesContext facesContext, AbstractTable table) {
        String imageUrl = table.getSortedAscendingImageUrl();
        return Resources.getURL(facesContext, imageUrl, AbstractTableRenderer.class, "ascending.gif");
    }

    public static String getSortedDescendingImageUrl(FacesContext facesContext, AbstractTable table) {
        String imageUrl = table.getSortedDescendingImageUrl();
        return Resources.getURL(facesContext, imageUrl, AbstractTableRenderer.class, "descending.gif");
    }

}
