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

import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.DataTable;
import org.openfaces.component.table.impl.DynamicCol;
import org.openfaces.component.table.RowGrouping;
import org.openfaces.component.table.SortingOrGroupingRule;
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
    private static final String DYNAMIC_SORTING_TOGGLE_CLASS = "o_table_sortingToggle";

    public static enum SortingToggleMode {
        OFF,
        AUTODETECT,
        FORCE_DYNAMIC
    }

    private String id;
    private BaseColumn column;
    private Object content;
    private int colSpan;
    private int rowIndexMin;
    private int rowIndexMax;
    private TableStructure tableStructure;

    private String cellTag;
    private boolean renderNonBreakable;
    private SortingToggleMode sortingToggleMode;
    private CellKind cellKind;
    private boolean escapeText;

    public HeaderCell(BaseColumn column, Object content, String cellTag, CellKind cellKind) {
        this(column, content, cellTag, cellKind, false, SortingToggleMode.OFF);
    }

    public HeaderCell(BaseColumn column, Object content, String cellTag, CellKind cellKind,
                      boolean renderNonBreakable, SortingToggleMode sortingToggleMode) {
        this.column = column;
        this.content = content;
        this.cellTag = cellTag;
        this.cellKind = cellKind;
        this.renderNonBreakable = renderNonBreakable;
        this.sortingToggleMode = sortingToggleMode;
    }

    public TableStructure getTableStructure() {
        return tableStructure;
    }

    public void setTableStructure(TableStructure tableStructure) {
        this.tableStructure = tableStructure;
    }

    public CellKind getCellKind() {
        return cellKind;
    }

    public void setSpans(int colSpan, int rowIndex1, int rowIndex2) {
        this.colSpan = colSpan;
        rowIndexMin = Math.min(rowIndex1, rowIndex2);
        rowIndexMax = Math.max(rowIndex1, rowIndex2);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void render(FacesContext context,
                       AdditionalContentWriter additionalContentWriter) throws IOException {
        HeaderRow parentRow = getParent(HeaderRow.class);
        List<HeaderRow> rows = parentRow != null ? parentRow.getRowsForSpans() : null;
        TableStructure tableStructure = this.tableStructure != null ? this.tableStructure : getParent(TableStructure.class);
        UIComponent table = tableStructure.getComponent();

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(cellTag, table);
        if (id != null)
            writer.writeAttribute("id", id, null);

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
        Runnable restoreVariables = dynamicCol != null ? dynamicCol.enterComponentContext() : null;
        try {
            if (content != null) {
                if (content instanceof UIComponent) {
                    UIComponent uiComponent = (UIComponent) content;
                    uiComponent.encodeAll(context);
                    if (TableStructure.isComponentEmpty(uiComponent) && tableStructure.isEmptyCellsTreatmentRequired())
                        Rendering.writeNonBreakableSpace(writer);
                } else if (content instanceof TableElement)
                    ((TableElement) content).render(context, null);
                else {
                    if (escapeText)
                        writer.writeText(content.toString(), null);
                    else
                        writer.write(content.toString());
                }
            } else if (tableStructure.isEmptyCellsTreatmentRequired())
                Rendering.writeNonBreakableSpace(writer);
        } finally {
            if (restoreVariables != null) restoreVariables.run();
        }

        if (sortingToggleMode == SortingToggleMode.FORCE_DYNAMIC ||
                (sortingToggleMode == SortingToggleMode.AUTODETECT && column != null && table instanceof AbstractTable))
            renderColumnSortingDirection(context, (AbstractTable) table, column);
        if (renderNonBreakable) {
            if (Environment.isExplorer())
                writer.endElement("span");
        }

        if (additionalContentWriter != null)
            additionalContentWriter.writeAdditionalContent(context);

        writer.endElement(cellTag);
    }

    protected void renderColumnSortingDirection(
            FacesContext facesContext, AbstractTable table, BaseColumn column) throws IOException {

        String imageUrl;
        if (sortingToggleMode == SortingToggleMode.AUTODETECT) {
            String columnId = column.getId();
            Boolean ascending = null;
            if (table instanceof DataTable) {
                DataTable dataTable = (DataTable) table;
                RowGrouping rowGrouping = dataTable.getRowGrouping();
                if (rowGrouping != null)
                    ascending = isColumnSortedAscending(columnId, rowGrouping.getGroupingRules());
            }
            if (ascending == null)
                ascending = isColumnSortedAscending(columnId, table.getSortingRules());

            if (ascending == null) return;

            imageUrl = ascending
                    ? getSortedAscendingImageUrl(facesContext, table)
                    : getSortedDescendingImageUrl(facesContext, table);
        } else if (sortingToggleMode == SortingToggleMode.FORCE_DYNAMIC) {
            imageUrl = null;
        } else {
            throw new IllegalStateException("This method should be invoked for either 'autodetect' or " +
                    "'force_dynamic' sorting toggle mode");
        }

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement("img", table);
        if (sortingToggleMode == SortingToggleMode.FORCE_DYNAMIC)
            writer.writeAttribute("class", DYNAMIC_SORTING_TOGGLE_CLASS, null);
        if (imageUrl != null)
            writer.writeAttribute("src", imageUrl, null);
        writer.writeAttribute("style", "margin-left: 5px", null);
        writer.endElement("img");
    }

    private Boolean isColumnSortedAscending(String columnId, List<? extends SortingOrGroupingRule> rules) {
        if (rules == null) return null;

        for (SortingOrGroupingRule rule : rules) {
            if (columnId.equals(rule.getColumnId()))
                return rule.isAscending();
        }
        return null;
    }

    public static String getSortedAscendingImageUrl(FacesContext facesContext, AbstractTable table) {
        String imageUrl = table.getSortedAscendingImageUrl();
        return Resources.getURL(facesContext, imageUrl, "table/ascending.gif");
    }

    public static String getSortedDescendingImageUrl(FacesContext facesContext, AbstractTable table) {
        String imageUrl = table.getSortedDescendingImageUrl();
        return Resources.getURL(facesContext, imageUrl, "table/descending.gif");
    }

}
