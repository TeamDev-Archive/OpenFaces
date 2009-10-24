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

import org.openfaces.component.TableStyles;
import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.AbstractTableSelection;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.CheckboxColumn;
import org.openfaces.component.table.ColumnResizing;
import org.openfaces.component.table.TableColumn;
import org.openfaces.org.json.JSONArray;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.EnvironmentUtil;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.StyleUtil;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AbstractTableRenderer extends RendererBase {

    private static final String DEFAULT_SORTED_COLUMN_CLASS = null;//"o_table_sorted_column";
    private static final String DEFAULT_SORTED_COLUMN_HEADER_CLASS = "o_table_sorted_column_header";
    private static final String DEFAULT_SORTED_COLUMN_BODY_CLASS = "o_table_sorted_column_body";
    private static final String DEFAULT_SORTED_COLUMN_FOOTER_CLASS = "o_table_sorted_column_footer";
    private static final String DEFAULT_SORTABLE_HEADER_CLASS = "o_table_sortable_header";
    private static final String DEFAULT_SORTABLE_HEADER_ROLLOVER_CLASS = null;//"o_table_sortable_header_rollover";
    private static final String DEFAULT_FOCUSED_STYLE = "border: 1px dotted black;";

    private static final String TABLE_STRUCTURE_ATTR = "_of_tableStructure";


    public static String getTableJsURL(FacesContext facesContext) {
        return ResourceUtil.getInternalResourceURL(facesContext, AbstractTableRenderer.class, "table.js");
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
            public void encodeScriptsAndStyles(FacesContext facesContext) throws IOException {
                AbstractTableRenderer.this.encodeScriptsAndStyles(facesContext, table);
            }

            protected String getAdditionalRowClass(FacesContext facesContext, AbstractTable table, Object rowData, int rowIndex) {
                return AbstractTableRenderer.this.getAdditionalRowClass(facesContext, table, rowData, rowIndex);
            }

            protected String[][] getBodyRowAttributes(FacesContext facesContext, AbstractTable table) throws IOException {
                return AbstractTableRenderer.this.getBodyRowAttributes(facesContext, table);
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
            ResourceUtil.renderJSLinkIfNeeded(lib, context);
        }
    }

    protected String getTextClass(AbstractTable table) {
        return null;
    }

    protected String getTextStyle(AbstractTable table) {
        return null;
    }

    protected String getAdditionalRowClass(FacesContext facesContext, AbstractTable table, Object rowData, int rowIndex) {
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
    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
    }

    private TableStructure getTableStructure(AbstractTable table) {
        return (TableStructure) table.getAttributes().get(TABLE_STRUCTURE_ATTR);
    }

    protected void encodeScriptsAndStyles(FacesContext facesContext, AbstractTable table) throws IOException {
        encodeAdditionalFeatureSupport(facesContext, table);
        StyleUtil.renderStyleClasses(facesContext, table);
    }

    protected void encodeAdditionalFeatureSupport(FacesContext facesContext, AbstractTable table) throws IOException {
        ScriptBuilder buf = new ScriptBuilder();

        encodeAdditionalFeaturesSupport_buf(facesContext, table, buf);

        AbstractTableSelection selection = table.getSelection();
        if (selection != null)
            selection.registerSelectionStyle(facesContext);

        StyleUtil.renderStyleClasses(facesContext, table); // encoding styles before scripts is important for tableUtil.js to be able to compute row and column styles correctly

        String[] libs = getNecessaryJsLibs(facesContext);
        RenderingUtil.renderInitScript(facesContext, buf, libs);

        if (selection != null)
            selection.encodeAll(facesContext);

        ColumnResizing columnResizing = table.getColumnResizing();
        if (columnResizing != null)
            columnResizing.encodeAll(facesContext);
    }

    protected void encodeAdditionalFeaturesSupport_buf(FacesContext facesContext, AbstractTable table, ScriptBuilder buf) throws IOException {
        encodeInitialization(facesContext, table, buf);
        encodeKeyboardSupport(facesContext, table, buf);
        encodeSortingSupport(facesContext, table, buf);

        if (!table.isDataSourceEmpty())
            preregisterNoFilterDataRowStyleForOpera(facesContext, table);

        encodeCheckboxColumnSupport(facesContext, table, buf);
    }

    private void preregisterNoFilterDataRowStyleForOpera(FacesContext facesContext, AbstractTable table) {
        if (EnvironmentUtil.isOpera() || EnvironmentUtil.isUndefinedBrowser())
            TableStructure.getNoDataRowClassName(facesContext, table);
    }

    private void encodeInitialization(
            FacesContext facesContext,
            AbstractTable table,
            ScriptBuilder buf) throws IOException {
        TableStyles defaultStyles = TableStructure.getDefaultStyles(table);
        TableStructure tableStructure = getTableStructure(table);

        buf.initScript(facesContext, table, "O$.Table._init",
                TableUtil.getStructureAndStyleParams(
                        facesContext, defaultStyles,
                        tableStructure),
                table.getUseAjax(),
                StyleUtil.getCSSClass(facesContext, table, table.getRolloverStyle(),
                        StyleGroup.rolloverStyleGroup(), table.getRolloverClass()),
                getInitJsAPIFunctionName());
    }

    protected String getInitJsAPIFunctionName() {
        return "";
    }

    protected String[] getNecessaryJsLibs(FacesContext context) {
        return new String[]{
                ResourceUtil.getUtilJsURL(context),
                TableUtil.getTableUtilJsURL(context),
                getTableJsURL(context)};
    }

    private void encodeKeyboardSupport(FacesContext facesContext, AbstractTable table, ScriptBuilder buf) throws IOException {
        boolean focusable = isKeyboardNavigationApplicable(table);
        if (!focusable)
            return;

        Boolean focusedAttr = (Boolean) table.getAttributes().get("focused");
        ResponseWriter writer = facesContext.getResponseWriter();
        String focusFieldName = getFocusFieldName(facesContext, table);
        String focused = String.valueOf(focusedAttr != null && focusedAttr);
        RenderingUtil.renderHiddenField(writer, focusFieldName, focused);
        boolean tableIsPaginated = getUseKeyboardForPagination(table);
        boolean applyDefaultStyle = table.getApplyDefaultStyle();
        String focusedClass = StyleUtil.getCSSClass_dontCascade(
                facesContext, table, table.getFocusedStyle(), StyleGroup.selectedStyleGroup(), table.getFocusedClass(),
                applyDefaultStyle ? DEFAULT_FOCUSED_STYLE : null);

        boolean canPageBack = tableIsPaginated && canPageBack(table);
        boolean canPageForth = tableIsPaginated && canPageForth(table);
        boolean canSelectLastPage = tableIsPaginated && canSelectLastPage(table);
        buf.initScript(facesContext, table, "O$.Table._initKeyboardNavigation",
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
        List<BaseColumn> columns = table.getColumnsForRendering();
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
                        (column instanceof TableColumn) ? ((TableColumn) column).getSortingExpression() : null;
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
        RenderingUtil.renderHiddenField(writer, getSortingFieldName(context, table), null);

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
                StyleUtil.getCSSClass(context, table, table.getSortableHeaderStyle(), StyleGroup.regularStyleGroup(), getSortableHeaderClass(table)),
                StyleUtil.getCSSClass(context, table, table.getSortableHeaderRolloverStyle(), StyleGroup.regularStyleGroup(), getSortableHeaderRolloverClass(table)),
                StyleUtil.getCSSClass(context, table, table.getSortedColumnStyle(), StyleGroup.regularStyleGroup(), getSortedColumnClass(table)),
                StyleUtil.getCSSClass(context, table, table.getSortedColumnHeaderStyle(), StyleGroup.regularStyleGroup(), getSortedColumnHeaderClass(table)),
                StyleUtil.getCSSClass(context, table, table.getSortedColumnBodyStyle(), StyleGroup.regularStyleGroup(), getSortedColumnBodyClass(table)),
                StyleUtil.getCSSClass(context, table, table.getSortedColumnFooterStyle(), StyleGroup.regularStyleGroup(), getSortedColumnFooterClass(table)),
                preloadedImageUrls);
    }

    private String getSortingFieldName(FacesContext facesContext, UIComponent table) {
        return table.getClientId(facesContext) + "::sorting";
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
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        super.decode(facesContext, uiComponent);
        if (!uiComponent.isRendered())
            return;
        AbstractTable table = (AbstractTable) uiComponent;

        decodeKeyboardSupport(facesContext, table);
        AbstractTableSelection selection = table.getSelection();
        if (selection != null)
            selection.processDecodes(facesContext);

        ColumnResizing columnResizing = table.getColumnResizing();
        if (columnResizing != null)
            columnResizing.processDecodes(facesContext);

        decodeSorting(facesContext, table);

        decodeCheckboxColumns(facesContext, table);
    }


    private void decodeKeyboardSupport(FacesContext facesContext, AbstractTable table) {
        Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        String focusedStr = requestParameterMap.get(getFocusFieldName(facesContext, table));
        boolean focused = focusedStr != null && focusedStr.equals("true");
        table.getAttributes().put("focused", focused);
    }

    private void decodeSorting(FacesContext facesContext, AbstractTable table) {
        Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        String sortingFieldName = getSortingFieldName(facesContext, table);
        String sortingFieldValue = requestParameterMap.get(sortingFieldName);
        if (sortingFieldValue != null && sortingFieldValue.length() > 0) {
            int columnToToggle = Integer.parseInt(sortingFieldValue);
            table.toggleSorting(columnToToggle);
        }
    }

    protected boolean isKeyboardNavigationApplicable(AbstractTable table) {
        AbstractTableSelection selection = table.getSelection();
        boolean forSelection = selection != null && selection.isEnabled() && selection.isKeyboardSupport();
        boolean forPaging = getUseKeyboardForPagination(table);
        boolean result = forSelection || forPaging;
        return result;
    }

    private void encodeCheckboxColumnSupport(FacesContext facesContext, AbstractTable table, ScriptBuilder buf) throws IOException {
        List<CheckboxColumn> checkboxColumns = new ArrayList<CheckboxColumn>(1);
        List<Integer> checkBoxColIndexes = new ArrayList<Integer>(1);
        List<BaseColumn> columns = table.getColumnsForRendering();
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

        ResponseWriter writer = facesContext.getResponseWriter();
        for (int i = 0; i < checkBoxColCount; i++) {
            CheckboxColumn col = checkboxColumns.get(i);
            RenderingUtil.renderHiddenField(writer, col.getClientId(facesContext), "");
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

            buf.initScript(facesContext, table, "O$.Table._initCheckboxCol",
                    colIndex,
                    col.getClientId(facesContext),
                    checkedRowIndexes);
        }
    }

    protected void decodeCheckboxColumns(FacesContext facesContext, AbstractTable table) {
        Map<String, String> requestMap = facesContext.getExternalContext().getRequestParameterMap();
        List<BaseColumn> columns = table.getColumnsForRendering();
        for (BaseColumn column : columns) {
            if (!(column instanceof CheckboxColumn))
                continue;

            String colId = column.getClientId(facesContext);
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
}
