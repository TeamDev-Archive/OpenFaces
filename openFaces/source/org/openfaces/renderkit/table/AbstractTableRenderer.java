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

import org.openfaces.component.CaptionButton;
import org.openfaces.component.TableStyles;
import org.openfaces.component.command.PopupMenu;
import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.AbstractTableSelection;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.CheckboxColumn;
import org.openfaces.component.table.Column;
import org.openfaces.component.table.ColumnReordering;
import org.openfaces.component.table.ColumnResizing;
import org.openfaces.component.table.Scrolling;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.AjaxPortionRenderer;
import org.openfaces.renderkit.CaptionButtonRenderer;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Environment;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AbstractTableRenderer extends RendererBase implements AjaxPortionRenderer {

    private static final String DEFAULT_SORTED_COLUMN_CLASS = null;//"o_table_sorted_column";
    private static final String DEFAULT_SORTED_COLUMN_HEADER_CLASS = "o_table_sorted_column_header";
    private static final String DEFAULT_SORTED_COLUMN_BODY_CLASS = "o_table_sorted_column_body";
    private static final String DEFAULT_SORTED_COLUMN_FOOTER_CLASS = "o_table_sorted_column_footer";
    private static final String DEFAULT_SORTABLE_HEADER_CLASS = "o_table_sortable_header";
    private static final String DEFAULT_SORTABLE_HEADER_ROLLOVER_CLASS = null;//"o_table_sortable_header_rollover";
    private static final String DEFAULT_FOCUSED_STYLE = "border: 1px dotted black;";

    private static final String TABLE_STRUCTURE_ATTR = "_of_tableStructure";

    private static final String FACET_COLUMN_MENU = "columnMenu";
    private static final String FACET_COLUMN_MENU_BUTTON = "columnMenuButton";

    public static String getTableJsURL(FacesContext context) {
        return Resources.getInternalURL(context, AbstractTableRenderer.class, "table.js");
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered())
            return;

        final AbstractTable table = (AbstractTable) component;

        if (table.getUseAjax())
            AjaxUtil.prepareComponentForAjax(context, component);

        TableStructure tableStructure = createTableStructure(table);
        table.getAttributes().put(TABLE_STRUCTURE_ATTR, tableStructure);
        try {
            // this hack is needed for working around strange IE issue
            // JSFC-2081 ExpressionFilter drop-downs in TreeTable have improper style on demo (regression) - IE only
            encodeJsLinks(context);

            tableStructure.render(context, new HeaderCell.AdditionalContentWriter() {
                public void writeAdditionalContent(FacesContext context) throws IOException {
                    encodeScriptsAndStyles(context, table);
                }
            });
        } finally {
            table.getAttributes().remove(TABLE_STRUCTURE_ATTR);
        }
    }

    protected TableStructure createTableStructure(final AbstractTable table) {
        return new TableStructure(table, table) {
            protected String getAdditionalRowClass(FacesContext context, AbstractTable table, Object rowData, int rowIndex) {
                return AbstractTableRenderer.this.getAdditionalRowClass(context, table, rowData, rowIndex);
            }

            protected String[][] getBodyRowAttributes(FacesContext context, AbstractTable table) throws IOException {
                return AbstractTableRenderer.this.getBodyRowAttributes(context, table);
            }

            protected String getTextClass(AbstractTable table) {
                return AbstractTableRenderer.this.getTextClass(table);
            }

            protected String getTextStyle(AbstractTable table) {
                return AbstractTableRenderer.this.getTextStyle(table);
            }
        };
    }

    private void encodeJsLinks(FacesContext context) throws IOException {
        String[] libs = getNecessaryJsLibs(context);
        for (String lib : libs) {
            Resources.renderJSLinkIfNeeded(context, lib);
        }
    }

    protected String getTextClass(AbstractTable table) {
        return null;
    }

    protected String getTextStyle(AbstractTable table) {
        return null;
    }

    protected String getAdditionalRowClass(FacesContext context, AbstractTable table, Object rowData, int rowIndex) {
        return null;
    }


    protected String[][] getBodyRowAttributes(FacesContext context, AbstractTable table) throws IOException {
        return null;
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent uiComponent) throws IOException {
    }

    private TableStructure getTableStructure(AbstractTable table) {
        return (TableStructure) table.getAttributes().get(TABLE_STRUCTURE_ATTR);
    }

    protected void encodeScriptsAndStyles(FacesContext context, AbstractTable table) throws IOException {
        encodeAdditionalFeatureSupport(context, table);
        Styles.renderStyleClasses(context, table);
    }

    protected void encodeAdditionalFeatureSupport(FacesContext context, AbstractTable table) throws IOException {
        ScriptBuilder buf = new ScriptBuilder();

        encodeAdditionalFeaturesSupport_buf(context, table, buf);

        AbstractTableSelection selection = table.getSelection();
        if (selection != null)
            selection.registerSelectionStyle(context);

        Styles.renderStyleClasses(context, table); // encoding styles before scripts is important for tableUtil.js to be able to compute row and column styles correctly

        String[] libs = getNecessaryJsLibs(context);
        Rendering.renderInitScript(context, buf, libs);

        if (selection != null)
            selection.encodeAll(context);

        ColumnResizing columnResizing = table.getColumnResizing();
        if (columnResizing != null)
            columnResizing.encodeAll(context);

        ColumnReordering columnReordering = table.getColumnReordering();
        if (columnReordering != null)
            columnReordering.encodeAll(context);
    }

    protected void encodeAdditionalFeaturesSupport_buf(FacesContext context, AbstractTable table, ScriptBuilder buf) throws IOException {
        encodeInitialization(context, table, buf);
        encodeKeyboardSupport(context, table, buf);
        encodeSortingSupport(context, table, buf);
        encodeColumnMenuSupport(context, table, buf);

        if (!table.isDataSourceEmpty())
            preregisterNoFilterDataRowStyleForOpera(context, table);

        encodeCheckboxColumnSupport(context, table, buf);
    }

    private void encodeColumnMenuSupport(FacesContext context, AbstractTable table, ScriptBuilder buf) throws IOException {
        UIComponent component = table.getFacet(FACET_COLUMN_MENU);
        if (component == null) return;
        if (!(component instanceof PopupMenu))
            throw new FacesException(
                    "The component inside of \"" + FACET_COLUMN_MENU + "\" facet must be a PopupMenu or descendant component, " +
                            "though the following component was found: " + component.getClass().getName() +
                            ". table id: \"" + table.getClientId(context) + "\"");
        UIComponent buttonComponent = table.getFacet(FACET_COLUMN_MENU_BUTTON);
        if (buttonComponent != null && !(buttonComponent instanceof CaptionButton))
            throw new FacesException(
                    "The component inside of \"" + FACET_COLUMN_MENU_BUTTON + "\" facet must be a CaptionButton or descendant component, " +
                            "though the following component was found: " + buttonComponent.getClass().getName() +
                            ". table id: \"" + table.getClientId(context) + "\"");
        CaptionButton button = (CaptionButton) buttonComponent;
        boolean temporaryButton = false;
        if (button == null) {
            button = createDefaultColumnMenuButton(context);
            temporaryButton = true;
            table.getFacets().put(FACET_COLUMN_MENU_BUTTON, button);
        }
        if (button.getImageUrl() == null)
            button.setImageUrl(getDefaultColumnMenuBtnImage(context));

        button.getAttributes().put(CaptionButtonRenderer.ATTR_DEFAULT_STYLE_CLASS, "o_columnMenuInvoker");

        ResponseWriter writer = context.getResponseWriter();
        // mock table/tr enclosing tags must be rendered for IE8 to process the button's td tag properly 
        writer.startElement("table", table);
        writer.startElement("tr", table);
        button.encodeAll(context);
        writer.endElement("tr");
        writer.endElement("table");

        PopupMenu columnMenu = (PopupMenu) component;
        columnMenu.setStandalone(true);
        columnMenu.encodeAll(context);
        buf.initScript(context, columnMenu, "O$.ColumnMenu._init", table, button);

        if (temporaryButton)
            table.getFacets().remove(FACET_COLUMN_MENU_BUTTON);
    }

    private CaptionButton createDefaultColumnMenuButton(FacesContext context) {
        CaptionButton captionButton = new CaptionButton();
        captionButton.setId("_columnMenuButton" + Rendering.SERVER_ID_SUFFIX_SEPARATOR);
        captionButton.setImageUrl(getDefaultColumnMenuBtnImage(context));
        return captionButton;
    }

    private String getDefaultColumnMenuBtnImage(FacesContext context) {
        return Resources.getInternalURL(context, AbstractTableRenderer.class, "columnMenuDrop.gif", false);
    }

    private void preregisterNoFilterDataRowStyleForOpera(FacesContext context, AbstractTable table) {
        if (Environment.isOpera() || Environment.isUndefinedBrowser())
            TableStructure.getNoDataRowClassName(context, table);
    }

    private void encodeInitialization(
            FacesContext context,
            AbstractTable table,
            ScriptBuilder buf) throws IOException {
        TableStyles defaultStyles = TableStructure.getDefaultStyles(table);
        TableStructure tableStructure = getTableStructure(table);

        buf.initScript(context, table, "O$.Table._init",
                tableStructure.getInitParam(context, defaultStyles),
                table.getUseAjax(),
                Styles.getCSSClass(context, table, table.getRolloverStyle(),
                        StyleGroup.rolloverStyleGroup(), table.getRolloverClass()),
                getInitJsAPIFunctionName());
    }

    protected String getInitJsAPIFunctionName() {
        return "";
    }

    protected String[] getNecessaryJsLibs(FacesContext context) {
        return new String[]{
                Resources.getUtilJsURL(context),
                TableUtil.getTableUtilJsURL(context),
                getTableJsURL(context)};
    }

    private void encodeKeyboardSupport(FacesContext context, AbstractTable table, ScriptBuilder buf) throws IOException {
        boolean focusable = isKeyboardNavigationApplicable(table);
        if (!focusable)
            return;

        Boolean focusedAttr = (Boolean) table.getAttributes().get("focused");
        ResponseWriter writer = context.getResponseWriter();
        String focusFieldName = getFocusFieldName(context, table);
        String focused = String.valueOf(focusedAttr != null && focusedAttr);
        Rendering.renderHiddenField(writer, focusFieldName, focused);
        boolean tableIsPaginated = getUseKeyboardForPagination(table);
        boolean applyDefaultStyle = table.getApplyDefaultStyle();
        String focusedClass = Styles.getCSSClass_dontCascade(
                context, table, table.getFocusedStyle(), StyleGroup.selectedStyleGroup(), table.getFocusedClass(),
                applyDefaultStyle ? DEFAULT_FOCUSED_STYLE : null);

        boolean canPageBack = tableIsPaginated && canPageBack(table);
        boolean canPageForth = tableIsPaginated && canPageForth(table);
        boolean canSelectLastPage = tableIsPaginated && canSelectLastPage(table);
        buf.initScript(context, table, "O$.Table._initKeyboardNavigation",
                tableIsPaginated,
                focusedClass,
                canPageBack,
                canPageForth,
                canSelectLastPage,
                table.getTabindex());
    }

    protected boolean canSelectLastPage(AbstractTable table) {
        return false;
    }

    protected boolean canPageForth(AbstractTable table) {
        return false;
    }

    protected boolean canPageBack(AbstractTable table) {
        return false;
    }

    protected boolean getUseKeyboardForPagination(AbstractTable table) {
        return false;
    }

    private String getFocusFieldName(FacesContext context, AbstractTable table) {
        return table.getClientId(context) + "::focused";
    }

    private void encodeSortingSupport(FacesContext context, AbstractTable table, ScriptBuilder buf) throws IOException {
        List<BaseColumn> columns = table.getRenderedColumns();
        boolean atLeastOneColumnSortable1 = false;
        JSONArray columnSortableFlags = new JSONArray();
        int colCount = columns.size();
        for (int i = 0; i < colCount; i++) {
            BaseColumn column = columns.get(i);
            boolean sortable;
            Boolean columnSortableAttr = (Boolean) column.getAttributes().get("sortable");
            if (columnSortableAttr != null)
                sortable = columnSortableAttr;
            else {
                ValueExpression sortingExpression =
                        (column instanceof Column) ? ((Column) column).getSortingExpression() : null;
                sortable = (sortingExpression != null);
            }
            atLeastOneColumnSortable1 |= sortable;
            columnSortableFlags.put(sortable);
        }
        boolean atLeastOneColumnSortable = atLeastOneColumnSortable1;
        if (!atLeastOneColumnSortable)
            return;

        getSortedColumnClass(table);
        getSortedColumnHeaderClass(table);

        ResponseWriter writer = context.getResponseWriter();
        Rendering.renderHiddenField(writer, getSortingFieldName(context, table), null);

        String oppositeSortingDirectionImageUrl = table.isSortAscending()
                ? HeaderCell.getSortedDescendingImageUrl(context, table)
                : HeaderCell.getSortedAscendingImageUrl(context, table);
        JSONArray preloadedImageUrls = new JSONArray();
        preloadedImageUrls.put(oppositeSortingDirectionImageUrl);
        if (table.getSortColumnIndex() == -1) {
            String anotherSortingDirectionImageUrl = table.isSortAscending()
                    ? HeaderCell.getSortedAscendingImageUrl(context, table)
                    : HeaderCell.getSortedDescendingImageUrl(context, table);
            preloadedImageUrls.put(anotherSortingDirectionImageUrl);
        }

        buf.initScript(context, table, "O$.Table._initSorting",
                columnSortableFlags,
                table.getSortColumnIndex(),
                Styles.getCSSClass(context, table, table.getSortableHeaderStyle(), StyleGroup.regularStyleGroup(), getSortableHeaderClass(table)),
                Styles.getCSSClass(context, table, table.getSortableHeaderRolloverStyle(), StyleGroup.regularStyleGroup(), getSortableHeaderRolloverClass(table)),
                Styles.getCSSClass(context, table, table.getSortedColumnStyle(), StyleGroup.regularStyleGroup(), getSortedColumnClass(table)),
                Styles.getCSSClass(context, table, table.getSortedColumnHeaderStyle(), StyleGroup.regularStyleGroup(), getSortedColumnHeaderClass(table)),
                Styles.getCSSClass(context, table, table.getSortedColumnBodyStyle(), StyleGroup.regularStyleGroup(), getSortedColumnBodyClass(table)),
                Styles.getCSSClass(context, table, table.getSortedColumnFooterStyle(), StyleGroup.regularStyleGroup(), getSortedColumnFooterClass(table)),
                preloadedImageUrls);
    }

    private String getSortingFieldName(FacesContext context, UIComponent table) {
        return table.getClientId(context) + "::sorting";
    }

    private String getSortedColumnClass(AbstractTable table) {
        String sortedColumnClass = table.getSortedColumnClass();
        if (!table.getApplyDefaultStyle())
            return sortedColumnClass;
        return TableUtil.getClassWithDefaultStyleClass(table.getApplyDefaultStyle(), DEFAULT_SORTED_COLUMN_CLASS, sortedColumnClass);
    }

    private String getSortedColumnHeaderClass(AbstractTable table) {
        String sortedColumnHeaderClass = table.getSortedColumnHeaderClass();
        if (!table.getApplyDefaultStyle())
            return sortedColumnHeaderClass;
        return TableUtil.getClassWithDefaultStyleClass(table.getApplyDefaultStyle(), DEFAULT_SORTED_COLUMN_HEADER_CLASS, sortedColumnHeaderClass);
    }

    private String getSortedColumnBodyClass(AbstractTable table) {
        String sortedColumnBodyClass = table.getSortedColumnBodyClass();
        if (!table.getApplyDefaultStyle())
            return sortedColumnBodyClass;
        return TableUtil.getClassWithDefaultStyleClass(table.getApplyDefaultStyle(), DEFAULT_SORTED_COLUMN_BODY_CLASS, sortedColumnBodyClass);
    }

    private String getSortedColumnFooterClass(AbstractTable table) {
        String sortedColumnFooterClass = table.getSortedColumnFooterClass();
        if (!table.getApplyDefaultStyle())
            return sortedColumnFooterClass;
        return TableUtil.getClassWithDefaultStyleClass(table.getApplyDefaultStyle(), DEFAULT_SORTED_COLUMN_FOOTER_CLASS, sortedColumnFooterClass);
    }

    private String getSortableHeaderClass(AbstractTable table) {
        String sortableHeaderClass = table.getSortableHeaderClass();
        if (!table.getApplyDefaultStyle())
            return sortableHeaderClass;
        return TableUtil.getClassWithDefaultStyleClass(table.getApplyDefaultStyle(), DEFAULT_SORTABLE_HEADER_CLASS, sortableHeaderClass);
    }

    private String getSortableHeaderRolloverClass(AbstractTable table) {
        String sortableHeaderRolloverClass = table.getSortableHeaderRolloverClass();
        if (!table.getApplyDefaultStyle())
            return sortableHeaderRolloverClass;
        return TableUtil.getClassWithDefaultStyleClass(table.getApplyDefaultStyle(), DEFAULT_SORTABLE_HEADER_ROLLOVER_CLASS, sortableHeaderRolloverClass);
    }

    @Override
    public void decode(FacesContext context, UIComponent uiComponent) {
        super.decode(context, uiComponent);
        if (!uiComponent.isRendered())
            return;
        AbstractTable table = (AbstractTable) uiComponent;

        decodeKeyboardSupport(context, table);
        AbstractTableSelection selection = table.getSelection();
        if (selection != null)
            selection.processDecodes(context);

        ColumnResizing columnResizing = table.getColumnResizing();
        if (columnResizing != null)
            columnResizing.processDecodes(context);

        decodeSorting(context, table);
        decodeColumnMenu(context, table);

        decodeCheckboxColumns(context, table);

        Scrolling scrolling = table.getScrolling();
        if (scrolling != null)
            scrolling.processDecodes(context);

        ColumnReordering columnReordering = table.getColumnReordering();
        if (columnReordering != null)
            columnReordering.processDecodes(context);
    }


    private void decodeKeyboardSupport(FacesContext context, AbstractTable table) {
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String focusedStr = requestParameterMap.get(getFocusFieldName(context, table));
        boolean focused = focusedStr != null && focusedStr.equals("true");
        table.getAttributes().put("focused", focused);
    }

    private void decodeSorting(FacesContext context, AbstractTable table) {
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String sortingFieldName = getSortingFieldName(context, table);
        String sortingFieldValue = requestParameterMap.get(sortingFieldName);
        if (sortingFieldValue != null && sortingFieldValue.length() > 0) {
            int columnToToggle = Integer.parseInt(sortingFieldValue);
            table.toggleSorting(columnToToggle);
        }
    }

    private void decodeColumnMenu(FacesContext context, AbstractTable table) {
        decodeColumnVisibility(context, table);
        decodeColumnSorting(context, table);
        decodeColumnHiding(context, table);
    }

    private void decodeColumnVisibility(FacesContext context, AbstractTable table) {
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String fieldName = table.getClientId(context) + "::columnVisibility";
        String fieldValue = requestParameterMap.get(fieldName);
        if (fieldValue != null) {
            int columnToToggle = Integer.parseInt(fieldValue);
            List<BaseColumn> allColumns = table.getAllColumns();
            List<BaseColumn> renderedColumns = table.getRenderedColumns();
            BaseColumn column = allColumns.get(columnToToggle);
            boolean columnWasVisible = renderedColumns.contains(column);
            boolean showColumn = !columnWasVisible;
            applyColumnVisibility(table, column, showColumn);
        }
    }

    private void decodeColumnSorting(FacesContext context, AbstractTable table) {
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String sortAscendingStr = requestParameterMap.get(table.getClientId(context) + "::sortAscending");
        String sortDescendingStr = requestParameterMap.get(table.getClientId(context) + "::sortDescending");
        if (sortAscendingStr != null || sortDescendingStr != null) {
            int colIndex = sortAscendingStr != null ? Integer.parseInt(sortAscendingStr) : Integer.parseInt(sortDescendingStr);
            table.toggleSorting(colIndex, sortAscendingStr != null);
        }
    }

    private void decodeColumnHiding(FacesContext context, AbstractTable table) {
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String hideColumnStr = requestParameterMap.get(table.getClientId(context) + "::hideColumn");
        if (hideColumnStr != null) {
            List<BaseColumn> columns = table.getRenderedColumns();
            int colIndex = Integer.parseInt(hideColumnStr);
            BaseColumn column = columns.get(colIndex);
            applyColumnVisibility(table, column, false);
        }
    }

    private void applyColumnVisibility(AbstractTable table, BaseColumn column, boolean showColumn) {
        List<BaseColumn> allColumns = table.getAllColumns();
        List<BaseColumn> renderedColumns = table.getRenderedColumns();

        if (!showColumn && renderedColumns.size() == 1)
            showColumn = true; // don't allow to hide the last column
        List<String> newColumnsOrder = new ArrayList<String>();
        for (BaseColumn c : allColumns) {
            String cid = c.getId();
            if (c != column) {
                if (renderedColumns.contains(c))
                    newColumnsOrder.add(cid);
            } else {
                if (showColumn)
                    newColumnsOrder.add(cid);
            }
        }
        table.getAttributes().put("submittedColumnsOrder", newColumnsOrder);
    }


    protected boolean isKeyboardNavigationApplicable(AbstractTable table) {
        AbstractTableSelection selection = table.getSelection();
        boolean forSelection = selection != null && selection.isEnabled() && selection.isKeyboardSupport();
        boolean forPaging = getUseKeyboardForPagination(table);
        boolean result = forSelection || forPaging;
        return result;
    }

    private void encodeCheckboxColumnSupport(FacesContext context, AbstractTable table, ScriptBuilder buf) throws IOException {
        List<CheckboxColumn> checkboxColumns = new ArrayList<CheckboxColumn>(1);
        List<Integer> checkBoxColIndexes = new ArrayList<Integer>(1);
        List<BaseColumn> columns = table.getRenderedColumns();
        for (int i = 0, colIndex = 0, colCount = columns.size(); i < colCount; i++) {
            BaseColumn column = columns.get(i);
            if (column instanceof CheckboxColumn) {
                checkboxColumns.add((CheckboxColumn) column);
                checkBoxColIndexes.add(colIndex);
            }
            colIndex++;
        }
        int checkBoxColCount = checkboxColumns.size();
        if (checkBoxColCount == 0)
            return;

        ResponseWriter writer = context.getResponseWriter();
        for (int i = 0; i < checkBoxColCount; i++) {
            CheckboxColumn col = checkboxColumns.get(i);
            Rendering.renderHiddenField(writer, col.getClientId(context), "");
        }

        for (int checkBoxColIndex = 0; checkBoxColIndex < checkBoxColCount; checkBoxColIndex++) {
            CheckboxColumn col = checkboxColumns.get(checkBoxColIndex);
            Integer colIndex = checkBoxColIndexes.get(checkBoxColIndex);
            JSONArray checkedRowIndexes = new JSONArray();
            List<Integer> rowIndexes = col.encodeSelectionIntoIndexes();
            for (int j = 0, rowIndexCount = rowIndexes.size(); j < rowIndexCount; j++) {
                int checkedRowIdx = rowIndexes.get(j);
                checkedRowIndexes.put(checkedRowIdx);
            }

            buf.initScript(context, table, "O$.Table._initCheckboxCol",
                    colIndex,
                    col.getClientId(context),
                    checkedRowIndexes);
        }
    }

    protected void decodeCheckboxColumns(FacesContext context, AbstractTable table) {
        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();
        List<BaseColumn> columns = table.getRenderedColumns();
        for (BaseColumn column : columns) {
            if (!(column instanceof CheckboxColumn))
                continue;

            String colId = column.getClientId(context);
            String checkedRowIndexesStr = requestMap.get(colId);
            String[] indexes;
            if (checkedRowIndexesStr == null || checkedRowIndexesStr.length() == 0) {
                indexes = new String[0];
            } else {
                indexes = checkedRowIndexesStr.split(",");
            }
            List<Integer> rowIndexes = new ArrayList<Integer>(indexes.length);
            for (String indexStr : indexes) {
                Integer checkedRowIndex = new Integer(indexStr);
                rowIndexes.add(checkedRowIndex);
            }
            CheckboxColumn checkboxColumn = ((CheckboxColumn) column);
            checkboxColumn.decodeSelectionFromIndexes(rowIndexes);
        }
    }

    public JSONObject encodeAjaxPortion(FacesContext context, UIComponent component, String portionName, JSONObject jsonParam) throws IOException {
        if (!"rows".equals(portionName))
            throw new FacesException("Unknown portion name: " + portionName);
        AbstractTable table = (AbstractTable) component;
        return serveDynamicRowsRequest(context, table, 0, Integer.MAX_VALUE);
    }

    protected JSONObject serveDynamicRowsRequest(
            FacesContext context,
            AbstractTable table,
            int rowIndex,
            int rowCount
    ) throws IOException {
        TableStructure tableStructure = createTableStructure(table);
        ResponseWriter responseWriter = context.getResponseWriter();
        Writer stringWriter = new StringWriter();
        ResponseWriter clonedResponseWriter = responseWriter.cloneWithWriter(stringWriter);
        context.setResponseWriter(clonedResponseWriter);
        try {
            if (rowCount > 0) {
                List<BaseColumn> columns = table.getRenderedColumns();
                List<BodyRow> rows = tableStructure.getBody().createRows(context, rowIndex + 1, rowCount, columns);
                if (tableStructure.getScrolling() == null) {
                    for (int i = 0, count = rows.size(); i < count; i++) {
                        BodyRow row = rows.get(i);
                        row.render(context, null);
                    }
                } else {
                    if (rows.size() != 1)
                        throw new IllegalStateException("There should be one pseudo-row in the scrollable version");
                    BodyRow pseudoRow = rows.get(0);
                    List<BodyCell> cells = pseudoRow.getCells();
                    int ci = 0;
                    List<BodyRow> leftRows = tableStructure.getLeftFixedCols() > 0 ? getScrollingAreaRows(cells.get(ci++)) : null;
                    List<BodyRow> centerRows = getScrollingAreaRows(cells.get(ci++));
                    List<BodyRow> rightRows = tableStructure.getRightFixedCols() > 0 ? getScrollingAreaRows(cells.get(ci)) : null;
                    for (int i = 0, count = centerRows.size(); i < count; i++) {
                        if (leftRows != null)
                            leftRows.get(i).render(context, null);
                        centerRows.get(i).render(context, null);
                        if (rightRows != null)
                            rightRows.get(i).render(context, null);
                    }
                }

                AbstractTableSelection selection = table.getSelection();
                if (selection != null)
                    selection.encodeOnAjaxNodeFolding(context);
                for (BaseColumn column : columns) {
                    if (column instanceof CheckboxColumn)
                        ((CheckboxColumn) column).encodeOnAjaxNodeFolding(context);
                }
            }
        } finally {
            context.setResponseWriter(responseWriter);
        }
        table.setRowIndex(-1);
        responseWriter.write(stringWriter.toString());

        JSONObject rowsInitInfo = new JSONObject();
        fillDynamicRowsInitInfo(context, table, rowIndex, rowCount, tableStructure, rowsInitInfo);
        
        return rowsInitInfo;
    }

    private List<BodyRow> getScrollingAreaRows(BodyCell scrollingAreaCell) {
        TableScrollingArea area = (TableScrollingArea) scrollingAreaCell.getContent();
        return (List<BodyRow>) area.getRows();
    }

    protected void fillDynamicRowsInitInfo(FacesContext context,
                                           AbstractTable table,
                                           int rowIndex,
                                           int addedRowCount,
                                           TableStructure tableStructure,
                                           JSONObject rowsInitInfo) {
        Map<Object, String> rowStylesMap = tableStructure.getRowStylesMap();
        Map<Object, String> cellStylesMap = tableStructure.getCellStylesMap();
        Rendering.addJsonParam(rowsInitInfo, "rowStylesMap", TableUtil.getStylesMapAsJSONObject(rowStylesMap));
        Rendering.addJsonParam(rowsInitInfo, "cellStylesMap", TableUtil.getStylesMapAsJSONObject(cellStylesMap));
    }
}
