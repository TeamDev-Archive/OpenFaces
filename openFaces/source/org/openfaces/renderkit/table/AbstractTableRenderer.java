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

import org.openfaces.component.CaptionButton;
import org.openfaces.component.TableStyles;
import org.openfaces.component.command.MenuItem;
import org.openfaces.component.command.PopupMenu;
import org.openfaces.component.table.*;
import org.openfaces.component.table.impl.NodeInfo;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.AjaxPortionRenderer;
import org.openfaces.renderkit.CaptionButtonRenderer;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Components;
import org.openfaces.util.Environment;
import org.openfaces.util.Log;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AbstractTableRenderer extends RendererBase implements AjaxPortionRenderer {

    private static final String DEFAULT_SORTED_COLUMN_CLASS = null;//"o_table_sorted_column";
    private static final String DEFAULT_SORTED_COLUMN_HEADER_CLASS = "o_table_sorted_column_header";
    private static final String DEFAULT_SORTED_COLUMN_BODY_CLASS = "o_table_sorted_column_body";
    private static final String DEFAULT_SORTED_COLUMN_FOOTER_CLASS = "o_table_sorted_column_footer";
    public static final String DEFAULT_SORTABLE_HEADER_CLASS = "o_table_sortable_header";
    private static final String DEFAULT_SORTABLE_HEADER_ROLLOVER_CLASS = null;//"o_table_sortable_header_rollover";
    private static final String DEFAULT_FOCUSED_STYLE = "border: 1px dotted black;";

    private static final String FACET_COLUMN_MENU = "columnMenu";
    private static final String FACET_COLUMN_MENU_BUTTON = "columnMenuButton";
    public static final String DEFAULT_TOGGLE_CLASS_NAME = "o_treetable_expansionToggle";
    protected static final String SUB_ROWS_PORTION = "subRows:";

    public static String getTableJsURL(FacesContext context) {
        return Resources.internalURL(context, "table/table.js");
    }

    public static String treeTableJsURL(FacesContext context) {
        return Resources.internalURL(context, "table/treeTable.js");
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {

        if (!component.isRendered())
            return;

        final AbstractTable table = (AbstractTable) component;

        if (table.getUseAjax())
            AjaxUtil.prepareComponentForAjax(context, component);

        TableStructure tableStructure = createTableStructure(table);
        table.getAttributes().put(TableStructure.TABLE_STRUCTURE_ATTR, tableStructure);
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
            table.getAttributes().remove(TableStructure.TABLE_STRUCTURE_ATTR);
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

    protected void encodeScriptsAndStyles(FacesContext context, AbstractTable table) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("span", table);
        writer.writeAttribute("id", table.getClientId(context) + "::auxiliaryTags", null);
        writer.writeAttribute("style", "display: none", null);
        encodeAdditionalFeaturesSupport(context, table);
        Styles.renderStyleClasses(context, table);
        writer.endElement("span");
    }

    protected void encodeAdditionalFeaturesSupport(FacesContext context, AbstractTable table) throws IOException {
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

        Iterable<Summary> summaries = table.getSummaryComponents();
        for (Summary summary : summaries) {
            summary.encodeAfterCalculation(context);
        }
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

    protected void encodeAdditionalFeaturesOnBodyReload(FacesContext context, AbstractTable table, ScriptBuilder sb) throws IOException {
        encodeSortingSupportOnBodyReload(context, table, sb);

        AbstractTableSelection selection = table.getSelection();
        if (selection != null)
            selection.encodeOnBodyReload(context, sb);

        ColumnResizing columnResizing = table.getColumnResizing();
        if (columnResizing != null) {
            columnResizing.encodeOnBodyReload(context, sb);
        }

        ColumnReordering columnReordering = table.getColumnReordering();
        if (columnReordering != null) {
            columnReordering.encodeOnBodyReload(context, sb);
        }

    }

    protected void encodeSortingSupportOnBodyReload(FacesContext context, AbstractTable table, ScriptBuilder sb) {

    }

    private void encodeColumnMenuSupport(FacesContext context, AbstractTable table, ScriptBuilder buf) throws IOException {
        UIComponent component = Components.getFacet(table, FACET_COLUMN_MENU);
        if (component == null) return;
        if (!(component instanceof PopupMenu))
            throw new FacesException(
                    "The component inside of \"" + FACET_COLUMN_MENU + "\" facet must be a PopupMenu or descendant component, " +
                            "though the following component was found: " + component.getClass().getName() +
                            ". table id: \"" + table.getClientId(context) + "\"");
        UIComponent buttonComponent = Components.getFacet(table, FACET_COLUMN_MENU_BUTTON);
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

        // Rendering captionButton.js in this place of the generated HTML markup is required for fixing OF-69.
        // The reason is that IE cannot find the <script> tag for captionButton.js via the getElementsByTagName("script")
        // call in O$._markPreloadedLibraries (this <script> entry is just missing in this case, which seems to be some
        // IE bug) -- the result prior to fix was that ajaxUtil waited for captionButton.js to load indefinitely and failed
        // to invoke initialization scripts. Rendering captionButton.js in a less "deep" place of DOM solves the problem.
        Resources.renderJSLinkIfNeeded(context, Resources.utilJsURL(context));
        Resources.renderJSLinkIfNeeded(context, Resources.internalURL(context, "captionButton.js"));

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

        // todo: move item identification responsibility to the items themselves
        MenuItem sortAscMenuItem = null, sortDescMenuItem = null, hideColumnMenuItem = null,
                groupByColumnMenuItem = null, removeFromGroupingMenuItem = null, cancelGroupingMenuItem = null,
                resetSortingMenuItem = null;
        for (UIComponent child : columnMenu.getChildren()) {
            if (child instanceof SortAscendingMenuItem)
                sortAscMenuItem = (MenuItem) child;
            else if (child instanceof SortDescendingMenuItem)
                sortDescMenuItem = (MenuItem) child;
            else if (child instanceof HideColumnMenuItem)
                hideColumnMenuItem = (MenuItem) child;
            else if (child instanceof GroupByColumnMenuItem)
                groupByColumnMenuItem = (MenuItem) child;
            else if (child instanceof RemoveFromGroupingMenuItem)
                removeFromGroupingMenuItem = (MenuItem) child;
            else if (child instanceof CancelGroupingMenuItem)
                cancelGroupingMenuItem = (MenuItem) child;
            else if (child instanceof ResetSortingMenuItem)
                resetSortingMenuItem = (MenuItem) child;
        }

        buf.initScript(context, columnMenu, "O$.ColumnMenu._init", table, button,
                sortAscMenuItem, sortDescMenuItem, hideColumnMenuItem, groupByColumnMenuItem,
                removeFromGroupingMenuItem, cancelGroupingMenuItem, resetSortingMenuItem);

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
        return Resources.internalURL(context, "table/columnMenuDrop.gif");
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
        TableStructure tableStructure = TableStructure.getCurrentInstance(table);

        buf.initScript(context, table, "O$.Table._init",
                tableStructure.getInitParam(context, defaultStyles),
                table.getUseAjax(),
                Styles.getCSSClass(context, table, table.getRolloverStyle(),
                        StyleGroup.rolloverStyleGroup(), table.getRolloverClass()),
                getInitJsAPIFunctionName(),
                table.getDeferBodyLoading());
    }

    protected String getInitJsAPIFunctionName() {
        return "";
    }

    protected String[] getNecessaryJsLibs(FacesContext context) {
        return new String[]{
                Resources.utilJsURL(context),
                TableUtil.getTableUtilJsURL(context),
                getTableJsURL(context),
                Resources.jsonJsURL(context)
        };
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
        boolean atLeastOneColumnSortable1 = false;
        final JSONArray sortableColumnsIds = new JSONArray();
        for (BaseColumn column : table.getAllColumns()) {
            boolean sortable;
            Boolean columnSortableAttr = (Boolean) column.getAttributes().get("sortable");
            if (columnSortableAttr != null)
                sortable = columnSortableAttr;
            else {
                ValueExpression sortingExpression = column.getColumnSortingExpression();
                sortable = (sortingExpression != null);
            }
            if (sortable) sortableColumnsIds.put(column.getId());
            atLeastOneColumnSortable1 |= sortable;
        }
        boolean atLeastOneColumnSortable = atLeastOneColumnSortable1;
        if (!atLeastOneColumnSortable)
            return;

        getSortedColumnClass(table);
        getSortedColumnHeaderClass(table);

        ResponseWriter writer = context.getResponseWriter();
        Rendering.renderHiddenField(writer, getSortingFieldName(context, table), null);

        buf.initScript(context, table, "O$.Table._initSorting",
                table.getSortingRules(),
                sortableColumnsIds,
                table.getSortColumnIndex(),
                Styles.getCSSClass(context, table, table.getSortableHeaderStyle(), StyleGroup.regularStyleGroup(), getSortableHeaderClass(table)),
                Styles.getCSSClass(context, table, table.getSortableHeaderRolloverStyle(), StyleGroup.regularStyleGroup(), getSortableHeaderRolloverClass(table)),
                Styles.getCSSClass(context, table, table.getSortedColumnStyle(), StyleGroup.regularStyleGroup(), getSortedColumnClass(table)),
                Styles.getCSSClass(context, table, table.getSortedColumnHeaderStyle(), StyleGroup.regularStyleGroup(), getSortedColumnHeaderClass(table)),
                Styles.getCSSClass(context, table, table.getSortedColumnBodyStyle(), StyleGroup.regularStyleGroup(), getSortedColumnBodyClass(table)),
                Styles.getCSSClass(context, table, table.getSortedColumnFooterStyle(), StyleGroup.regularStyleGroup(), getSortedColumnFooterClass(table)),
                HeaderCell.getSortedAscendingImageUrl(context, table),
                HeaderCell.getSortedDescendingImageUrl(context, table),
                table.getUnsortedStateAllowed());
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

        decodeCheckboxColumnSupport(context, table);

        Scrolling scrolling = table.getScrolling();
        if (scrolling != null)
            scrolling.processDecodes(context);

        decodeColumnsOrder(context, table);

        decodeFoldingSupport(context, table);
    }

    protected void decodeColumnsOrder(FacesContext context, AbstractTable table) {
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String paramName = table.getClientId(context) + "::columnsOrder";
        String renderedColumns = params.get(paramName);
        if (renderedColumns == null)
            return;

        if (renderedColumns.length() == 0) {
            Log.log("columnsOrder list should contain at least one column");
            return;
        }
        List<String> columnIds = Arrays.asList(renderedColumns.split(","));
        table.getAttributes().put("submittedColumnsOrder", columnIds);
    }


    private void decodeKeyboardSupport(FacesContext context, AbstractTable table) {
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String focusedStr = requestParameterMap.get(getFocusFieldName(context, table));
        boolean focused = focusedStr != null && focusedStr.equals("true");
        table.getAttributes().put("focused", focused);
    }

    private void decodeSorting(FacesContext context, AbstractTable table) {
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String paramName = table.getClientId(context) + "::setSortingRules";
        String sortingRulesStr = requestParameterMap.get(paramName);
        if (Rendering.isNullOrEmpty(sortingRulesStr)) return;

        List<SortingRule> sortingRules = new ArrayList<SortingRule>();
        try {
            JSONArray sortingRulesJson = new JSONArray(sortingRulesStr);
            for (int i = 0, count = sortingRulesJson.length(); i < count; i++) {
                JSONObject jsonObject = sortingRulesJson.getJSONObject(i);
                sortingRules.add(new SortingRule(jsonObject));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        table.acceptNewSortingRules(sortingRules);
    }

    private void decodeColumnMenu(FacesContext context, AbstractTable table) {
        // todo: make these work through the new standard setColumnsOrder client-side API and remove the special
        // decoding cases here
        decodeColumnVisibility(context, table);
        decodeColumnHiding(context, table);
    }

    private void decodeColumnVisibility(FacesContext context, AbstractTable table) {
        //todo: [s.kurilin] We don't need it now. Remove it carefully.
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

    private void encodeCheckboxColumnSupport(
            FacesContext context,
            AbstractTable table,
            ScriptBuilder buf
    ) throws IOException {
        for (BaseColumn column : table.getRenderedColumns()) {
            if (!(column instanceof CheckboxColumn))
                continue;

            ((CheckboxColumn) column).encodeInitScript(context, buf);
        }
    }

    protected void decodeCheckboxColumnSupport(FacesContext context, AbstractTable table) {
        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();
        for (BaseColumn column : table.getRenderedColumns()) {
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
        AbstractTable table = (AbstractTable) component;
        if ("columnResizingState".equals(portionName)) {
            ColumnResizing columnResizing = table.getColumnResizing();
            // the execute phases are skipped for the "columnResizingState" Ajax request
            columnResizing.processDecodes(context);
            columnResizing.processValidators(context);
            columnResizing.processUpdates(context);
            return null;
        }
        if ("scrollingState".equals(portionName)) {
            Scrolling scrolling = table.getScrolling();
            // the execute phases are skipped for the "columnResizingState" Ajax request
            scrolling.processDecodes(context);
            scrolling.processValidators(context);
            scrolling.processUpdates(context);
            return null;
        }
        if ("rows".equals(portionName)) {
            beforeReloadingAllRows(context, table);
            JSONObject result = serveDynamicRowsRequest(context, table, 0, Integer.MAX_VALUE);

            ScriptBuilder sb = new ScriptBuilder();
            encodeAdditionalFeaturesOnBodyReload(context, table, sb);
            Rendering.renderInitScript(context, sb);

            return result;
        } else {
            throw new FacesException("Unknown portion name: " + portionName);
        }
    }

    protected void beforeReloadingAllRows(FacesContext context, AbstractTable table) {

    }

    protected JSONObject serveDynamicRowsRequest(
            FacesContext context,
            AbstractTable table,
            int rowIndex,
            int rowCount
    ) throws IOException {
        TableStructure tableStructure = createTableStructure(table);
        table.getAttributes().put(TableStructure.TABLE_STRUCTURE_ATTR, tableStructure);
        try {
            ResponseWriter responseWriter = context.getResponseWriter();
            Writer stringWriter = new StringWriter();
            ResponseWriter clonedResponseWriter = responseWriter.cloneWithWriter(stringWriter);
            context.setResponseWriter(clonedResponseWriter);
            try {
                if (rowCount > 0) {
                    List<BaseColumn> columns = table.getRenderedColumns();
                    List<BodyRow> rows = tableStructure.getBody().createRows(context, rowIndex, rowCount, columns);
                    if (tableStructure.getScrolling() == null) {
                        for (BodyRow row : rows) {
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
        } finally {
            table.getAttributes().remove(TableStructure.TABLE_STRUCTURE_ATTR);
        }

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
        List<String> rowKeys = tableStructure.getBody().getClientRowKeys();
        Rendering.addJsonParam(rowsInitInfo, "rowStylesMap", TableUtil.getStylesMapAsJSONObject(rowStylesMap));
        Rendering.addJsonParam(rowsInitInfo, "cellStylesMap", TableUtil.getStylesMapAsJSONObject(cellStylesMap));
        Rendering.addJsonParam(rowsInitInfo, "rowKeys", rowKeys);
    }

    public static boolean isAjaxRowLoadingInProgress(FacesContext context) {
        return isAjaxPortionRequestInProgress(context, "rows");
    }

    protected static boolean isAjaxPortionRequestInProgress(FacesContext context, String expectedPortionName) {
        boolean ajaxRequestInProgress = AjaxUtil.isAjaxRequest(context);
        if (!ajaxRequestInProgress)
            return false;

        List<String> portions = AjaxUtil.getAjaxPortionNames(context);
        if (portions == null)
            return false;

        for (String portionName : portions) {
            if (portionName.startsWith(expectedPortionName))
                return true;
        }
        return false;
    }

    protected static boolean encodeFoldingSupport(FacesContext context,
                                                  ScriptBuilder buf,
                                                  AbstractTable table) throws IOException {
        JSONObject treeStructure = formatTreeStructureMap(context, table, -1, -1);
        if (treeStructure == null) {
            // this can be the case for a grouping-enabled table which doesn't have any grouping rule configured,
            // which means that it is displayed as a plain list and no special hierarchy handling is required
            return false;
        }

        ResponseWriter writer = context.getResponseWriter();
        Rendering.renderHiddenField(writer, getExpandedNodesFieldName(context, table), null);

        JSONArray treeColumnParamsArray = new JSONArray();
        List<BaseColumn> renderedColumns = table.getAdaptedRenderedColumns();
        for (BaseColumn column : renderedColumns) {
            if (!(column instanceof TreeColumn))
                continue;
            TreeColumn treeColumn = (TreeColumn) column;
            Object columnParams = treeColumn.encodeParamsAsJsObject(context);
            treeColumnParamsArray.put(columnParams != null ? columnParams : JSONObject.NULL);
        }

        buf.initScript(context, table, "O$.TreeTable._initFolding",
                treeStructure,
                treeColumnParamsArray,
                DEFAULT_TOGGLE_CLASS_NAME,
                Resources.internalURL(context, "table/treeStructureSolid.png"));
        return true;
    }

    private static String getExpandedNodesFieldName(FacesContext context, AbstractTable treeTable) {
        return treeTable.getClientId(context) + "::expandedNodes";
    }

    protected static JSONObject formatTreeStructureMap(
            FacesContext context,
            AbstractTable table,
            int fromRowIndex,
            int rowCount) {
        JSONObject result = new JSONObject();
        Map<Object, ? extends NodeInfo> structureMap = table.getTreeStructureMap(context);
        if (structureMap == null)
            return null;
        Set<? extends Map.Entry<Object, ? extends NodeInfo>> entries = structureMap.entrySet();
        for (Map.Entry<Object, ? extends NodeInfo> entry : entries) {
            Object rowIndex = entry.getKey();
            if (fromRowIndex != -1) {
                if (!(rowIndex instanceof Integer))
                    continue;
                int intRowIndex = (Integer) rowIndex;
                if (intRowIndex < fromRowIndex || intRowIndex >= fromRowIndex + rowCount)
                    continue;
                intRowIndex -= fromRowIndex;
                rowIndex = intRowIndex;
            }
            NodeInfo expansionData = entry.getValue();
            boolean nodeHasChildren = expansionData.getNodeHasChildren();
            Object childCount = nodeHasChildren
                    ? (expansionData.getChildrenPreloaded() ? String.valueOf(expansionData.getChildNodeCount()) : "?")
                    : 0;
            try {
                result.put(String.valueOf(rowIndex), childCount);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    private void decodeFoldingSupport(FacesContext context, AbstractTable table) {
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String expandedNodes = requestParameterMap.get(getExpandedNodesFieldName(context, table));
        if (expandedNodes == null)
            return;
        String[] indexStrs = expandedNodes.split(",");
        Set<Integer> expandedRowIndexes = new HashSet<Integer>(indexStrs.length);
        for (String indexStr : indexStrs) {
            if (indexStr.length() == 0)
                continue;
            int rowIndex = Integer.parseInt(indexStr);
            expandedRowIndexes.add(rowIndex);
        }
        table.acceptNewExpandedRowIndexes(expandedRowIndexes);
    }

    public static boolean isAjaxFoldingInProgress(FacesContext context) {
        return isAjaxPortionRequestInProgress(context, SUB_ROWS_PORTION);
    }


}
