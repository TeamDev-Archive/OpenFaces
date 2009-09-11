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
package org.openfaces.component.table;

import org.openfaces.component.OUIData;
import org.openfaces.component.TableStyles;
import org.openfaces.component.FilterableComponent;
import org.openfaces.util.ValueBindings;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.renderkit.table.AbstractTableRenderer;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.ComponentUtil;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AbstractTable extends OUIData implements TableStyles, FilterableComponent {
    /*
   Implementation notes:
   - the full life-cycle for the selection child is intentionally ensured. Although implementation of
     selection doesn't require this itself, Ajax4jsf on selection's "onchange" event didn't work until the selection
     component was made to process all phases [JSFC-2049].
    */

    private List<SortingRule> sortingRules;

    private String cellspacing;
    private String cellpadding;
    private Integer border;
    private String align;
    private String bgcolor;
    private String dir;
    private String rules;
    private String width;
    private String tabindex;

    private String focusedStyle;
    private String focusedClass;

    private String headerSectionStyle;
    private String headerSectionClass;
    private String bodySectionStyle;
    private String bodySectionClass;
    private String footerSectionStyle;
    private String footerSectionClass;
    private String bodyRowStyle;
    private String bodyRowClass;
    private String bodyOddRowStyle;
    private String bodyOddRowClass;
    private String headerRowStyle;
    private String headerRowClass;
    private String commonHeaderRowStyle;
    private String commonHeaderRowClass;
    private String footerRowStyle;
    private String footerRowClass;
    private String commonFooterRowStyle;
    private String commonFooterRowClass;

    private String filterRowStyle;
    private String filterRowClass;
    private String filterRowSeparator;
    private String allRecordsFilterName;
    private String emptyRecordsFilterName;
    private String nonEmptyRecordsFilterName;

    private String rolloverRowStyle;
    private String rolloverRowClass;
    private String noDataRowStyle;
    private String noDataRowClass;

    private String sortedColumnStyle;
    private String sortedColumnClass;
    private String sortedColumnHeaderStyle;
    private String sortedColumnHeaderClass;
    private String sortedColumnBodyStyle;
    private String sortedColumnBodyClass;
    private String sortedColumnFooterStyle;
    private String sortedColumnFooterClass;
    private String sortableHeaderStyle;
    private String sortableHeaderClass;
    private String sortableHeaderRolloverStyle;
    private String sortableHeaderRolloverClass;

    private String horizontalGridLines;
    private String verticalGridLines;
    private String commonHeaderSeparator;
    private String commonFooterSeparator;
    private String headerHorizSeparator;
    private String headerVertSeparator;
    private String multiHeaderSeparator;
    private String multiFooterSeparator;
    private String footerHorizSeparator;
    private String footerVertSeparator;

    private Boolean applyDefaultStyle;
    private Boolean useAjax;

    private Boolean noDataMessageAllowed;
    private String columnIdVar;
    private String columnIndexVar;
    private List<String> columnsOrder;

    private String sortedAscendingImageUrl;
    private String sortedDescendingImageUrl;

    private int toggleColumnSorting = -1;
    private boolean beforeUpdateValuesPhase = true;
    private List<BaseColumn> cachedAllColumns;
    private List<BaseColumn> cachedColumnsForRendering;
    private String cachedClientId;
    private List<AbstractFilter> myFilters = new ArrayList<AbstractFilter>();

    public AbstractTable() {
        super.setUiDataValue(new TableDataModel(this));
    }

    @Override
    public Object processSaveState(FacesContext context) {
        AbstractTableSelection selection = getSelection();
        if (selection != null && selection.getModel() == null) {
            // Fix for JSFC-2945 (selection's model can be null if table is not rendered and selection's rowData is declared as constant)
            TableDataModel model = getModel();
            selection.setModel(model);
        }

        return super.processSaveState(context);
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, saveAttachedState(context, sortingRules),
                align, bgcolor, dir, rules, width, tabindex, border, cellspacing, cellpadding,
                applyDefaultStyle, headerSectionStyle, headerSectionClass, bodySectionStyle,
                bodySectionClass, footerSectionStyle, footerSectionClass,
                bodyRowStyle, bodyRowClass, bodyOddRowStyle, bodyOddRowClass, headerRowStyle, headerRowClass,
                commonHeaderRowStyle, commonHeaderRowClass, footerRowStyle, footerRowClass, commonFooterRowStyle,
                commonFooterRowClass, sortedColumnStyle, sortedColumnClass, sortedColumnHeaderStyle, sortedColumnHeaderClass,
                sortedColumnBodyStyle, sortedColumnBodyClass, sortedColumnFooterStyle, sortedColumnFooterClass,
                sortableHeaderStyle, sortableHeaderClass, sortableHeaderRolloverStyle, sortableHeaderRolloverClass,
                horizontalGridLines, verticalGridLines, commonHeaderSeparator, commonFooterSeparator,
                headerHorizSeparator, headerVertSeparator, multiHeaderSeparator, multiFooterSeparator,
                footerHorizSeparator, footerVertSeparator,
                useAjax, allRecordsFilterName, emptyRecordsFilterName, nonEmptyRecordsFilterName,
                filterRowStyle, filterRowClass, filterRowSeparator, focusedStyle, focusedClass,
                rolloverRowStyle, rolloverRowClass, noDataRowStyle, noDataRowClass,
                noDataMessageAllowed, columnIndexVar, columnIdVar, saveAttachedState(context, columnsOrder),
                sortedAscendingImageUrl, sortedDescendingImageUrl, cachedClientId};
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        sortingRules = (List<SortingRule>) restoreAttachedState(context, state[i++]);
        align = (String) state[i++];
        bgcolor = (String) state[i++];
        dir = (String) state[i++];
        rules = (String) state[i++];
        width = (String) state[i++];
        tabindex = (String) state[i++];
        border = (Integer) state[i++];
        cellspacing = (String) state[i++];
        cellpadding = (String) state[i++];

        applyDefaultStyle = (Boolean) state[i++];
        headerSectionStyle = (String) state[i++];
        headerSectionClass = (String) state[i++];
        bodySectionStyle = (String) state[i++];
        bodySectionClass = (String) state[i++];
        footerSectionStyle = (String) state[i++];
        footerSectionClass = (String) state[i++];
        bodyRowStyle = (String) state[i++];
        bodyRowClass = (String) state[i++];
        bodyOddRowStyle = (String) state[i++];
        bodyOddRowClass = (String) state[i++];
        headerRowStyle = (String) state[i++];
        headerRowClass = (String) state[i++];
        commonHeaderRowStyle = (String) state[i++];
        commonHeaderRowClass = (String) state[i++];
        footerRowStyle = (String) state[i++];
        footerRowClass = (String) state[i++];
        commonFooterRowStyle = (String) state[i++];
        commonFooterRowClass = (String) state[i++];
        sortedColumnStyle = (String) state[i++];
        sortedColumnClass = (String) state[i++];
        sortedColumnHeaderStyle = (String) state[i++];
        sortedColumnHeaderClass = (String) state[i++];
        sortedColumnBodyStyle = (String) state[i++];
        sortedColumnBodyClass = (String) state[i++];
        sortedColumnFooterStyle = (String) state[i++];
        sortedColumnFooterClass = (String) state[i++];
        sortableHeaderStyle = (String) state[i++];
        sortableHeaderClass = (String) state[i++];
        sortableHeaderRolloverStyle = (String) state[i++];
        sortableHeaderRolloverClass = (String) state[i++];

        horizontalGridLines = (String) state[i++];
        verticalGridLines = (String) state[i++];
        commonHeaderSeparator = (String) state[i++];
        commonFooterSeparator = (String) state[i++];
        headerHorizSeparator = (String) state[i++];
        headerVertSeparator = (String) state[i++];
        multiHeaderSeparator = (String) state[i++];
        multiFooterSeparator = (String) state[i++];
        footerHorizSeparator = (String) state[i++];
        footerVertSeparator = (String) state[i++];

        useAjax = (Boolean) state[i++];

        allRecordsFilterName = (String) state[i++];
        emptyRecordsFilterName = (String) state[i++];
        nonEmptyRecordsFilterName = (String) state[i++];
        filterRowStyle = (String) state[i++];
        filterRowClass = (String) state[i++];
        filterRowSeparator = (String) state[i++];

        focusedStyle = (String) state[i++];
        focusedClass = (String) state[i++];
        rolloverRowStyle = (String) state[i++];
        rolloverRowClass = (String) state[i++];

        noDataRowStyle = (String) state[i++];
        noDataRowClass = (String) state[i++];
        noDataMessageAllowed = (Boolean) state[i++];
        columnIndexVar = (String) state[i++];
        columnIdVar = (String) state[i++];
        columnsOrder = (List) restoreAttachedState(context, state[i++]);
        sortedAscendingImageUrl = (String) state[i++];
        sortedDescendingImageUrl = (String) state[i++];
        cachedClientId = (String) state[i++];

        beforeUpdateValuesPhase = true;
        toggleColumnSorting = -1;
    }

    protected void afterRestoreState(FacesContext context) {
        TableDataModel model = (TableDataModel) getUiDataValue();
        model.setTable(this);

        AbstractTableSelection selection = getSelection();
        if (selection != null)
            selection.setModel(model);

        List<BaseColumn> columns = getAllColumns();
        for (int colIndex = 0, columnCount = columns.size(); colIndex < columnCount; colIndex++) {
            BaseColumn column = columns.get(colIndex);
            if (column instanceof CheckboxColumn) {
                ((CheckboxColumn) column).assignDataModel();
            }
        }
    }

    protected void beforeProcessDecodes(FacesContext context) {
        cachedAllColumns = null;
        cachedColumnsForRendering = null;
    }

    public void invokeBeforeProcessDecodes(FacesContext context) {
        beforeProcessDecodes(context);
    }

    @Override
    public void processRestoreState(FacesContext context, Object state) {
        Object ajaxState = AjaxUtil.retrieveAjaxStateObject(context, this);
        super.processRestoreState(context, ajaxState != null ? ajaxState : state);
        afterRestoreState(context);
    }

    protected TableDataModel getModel() {
        return (TableDataModel) getUiDataValue();
    }

    @Override
    public Object getValue() { // needed for UISeamCommandBase to work with DataTable/TreeTable correctly (JSFC-2585)
        Object data = getModel().getWrappedData();
        if (data instanceof ValueExpression) {
            FacesContext facesContext = getFacesContext();
            data = ((ValueExpression) data).getValue(facesContext.getELContext());
        }
        return data;
    }

    @Override
    public void setValue(Object value) {
        getModel().setWrappedData(value);
    }

    public String getHeaderSectionStyle() {
        return ValueBindings.get(this, "headerSectionStyle", headerSectionStyle);
    }

    public void setHeaderSectionStyle(String headerSectionStyle) {
        this.headerSectionStyle = headerSectionStyle;
    }

    public String getHeaderSectionClass() {
        return ValueBindings.get(this, "headerSectionClass", headerSectionClass);
    }

    public void setHeaderSectionClass(String headerSectionClass) {
        this.headerSectionClass = headerSectionClass;
    }

    public String getBodySectionStyle() {
        return ValueBindings.get(this, "bodySectionStyle", bodySectionStyle);
    }

    public void setBodySectionStyle(String bodySectionStyle) {
        this.bodySectionStyle = bodySectionStyle;
    }

    public String getBodySectionClass() {
        return ValueBindings.get(this, "bodySectionClass", bodySectionClass);
    }

    public void setBodySectionClass(String bodySectionClass) {
        this.bodySectionClass = bodySectionClass;
    }


    public String getFooterSectionStyle() {
        return ValueBindings.get(this, "footerSectionStyle", footerSectionStyle);
    }

    public void setFooterSectionStyle(String footerSectionStyle) {
        this.footerSectionStyle = footerSectionStyle;
    }

    public String getFooterSectionClass() {
        return ValueBindings.get(this, "footerSectionClass", footerSectionClass);
    }

    public void setFooterSectionClass(String footerSectionClass) {
        this.footerSectionClass = footerSectionClass;
    }


    @Override
    protected List<BaseColumn> getColumnsForProcessing() {
        return getColumnsForRendering();
    }

    public List<BaseColumn> getColumnsForRendering() {
        if (cachedColumnsForRendering == null)
            cachedColumnsForRendering = calculateColumnsForRendering();
        return cachedColumnsForRendering;
    }

    private List<BaseColumn> calculateColumnsForRendering() {
        List<String> columnsOrder = getColumnsOrder();
        if (columnsOrder == null) {
            List<BaseColumn> allColumns = getAllColumns();
            List<BaseColumn> renderedColumns = new ArrayList<BaseColumn>(allColumns.size());
            for (BaseColumn column : allColumns) {
                if (column.isRendered())
                    renderedColumns.add(column);
            }
            return renderedColumns;
        }
        List<BaseColumn> allColumns = getAllColumns();
        List<BaseColumn> result = new ArrayList<BaseColumn>();
        for (String columnId : columnsOrder) {
            if (columnId == null)
                throw new IllegalStateException(
                        "columnsOrder collection shouldn't contain null entries; table's clientId = " + getClientId(getFacesContext()));
            /*if (!(s instanceof String))
                throw new IllegalStateException(
                        "columnsOrder collection should only contain String instances which specify id's of columns in order " +
                                "of their appearance, but an instance of " + s.getClass() + " was found; table's clientId = " + getClientId(getFacesContext()));*/
            BaseColumn colById = findColumnById(allColumns, columnId);
            if (colById == null)
                throw new IllegalStateException("columnsOrder collection contains an id that doesn't point to an existing column: " + columnId + "; table's clientId = " + getClientId(getFacesContext()));
            if (colById.isRendered())
                result.add(colById);
        }
        return result;
    }

    private BaseColumn findColumnById(List<BaseColumn> allColumns, String columnId) {
        BaseColumn colById = null;
        int allColCount = allColumns.size();
        for (int colIndex = 0; colIndex < allColCount; colIndex++) {
            BaseColumn col = allColumns.get(colIndex);
            if (columnId.equals(col.getId())) {
                colById = col;
                break;
            }
        }
        return colById;
    }

    public List<BaseColumn> getAllColumns() {
        if (cachedAllColumns == null)
            cachedAllColumns = calculateAllColumns();
        return cachedAllColumns;
    }

    private List<BaseColumn> calculateAllColumns() {
        FacesContext context = FacesContext.getCurrentInstance();
        List<UIComponent> children = getChildren();
        if (children == null) {
            return Collections.emptyList();
        }
        List<BaseColumn> columns = TableUtil.getColumnsFromList(context, children);
        return Collections.unmodifiableList(columns);
    }


    public String getCellspacing() {
        return ValueBindings.get(this, "cellspacing", cellspacing);
    }

    public void setCellspacing(String cellspacing) {
        this.cellspacing = cellspacing;
    }

    public String getCellpadding() {
        return ValueBindings.get(this, "cellpadding", cellpadding);
    }

    public void setCellpadding(String cellpadding) {
        this.cellpadding = cellpadding;
    }

    public int getBorder() {
        return ValueBindings.get(this, "border", border, Integer.MIN_VALUE);
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public List<String> getColumnsOrder() {
        return ValueBindings.get(this, "columnsOrder", columnsOrder, List.class);
    }

    public void setColumnsOrder(List<String> columnsOrder) {
        this.columnsOrder = columnsOrder;
    }


    public String getSortedAscendingImageUrl() {
        return ValueBindings.get(this, "sortedAscendingImageUrl", sortedAscendingImageUrl);
    }

    public void setSortedAscendingImageUrl(String sortedAscendingImageUrl) {
        this.sortedAscendingImageUrl = sortedAscendingImageUrl;
    }

    public String getSortedDescendingImageUrl() {
        return ValueBindings.get(this, "sortedDescendingImageUrl", sortedDescendingImageUrl);
    }

    public void setSortedDescendingImageUrl(String sortedDescendingImageUrl) {
        this.sortedDescendingImageUrl = sortedDescendingImageUrl;
    }

    protected BaseColumn getColumn(int colIndex) {
        if (colIndex < -1)
            throw new IllegalArgumentException("colIndex can't be less than -1");
        if (colIndex == -1)
            return null;

        List<BaseColumn> columns = getColumnsForRendering();
        int colCount = columns.size();
        if (colIndex >= colCount)
            throw new IllegalArgumentException("colIndex points to a non-existing column: " + colIndex + ". Number of columns is " + colCount);
        BaseColumn column = columns.get(colIndex);
        return column;
    }

    public String getAlign() {
        return ValueBindings.get(this, "align", align);
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getBgcolor() {
        return ValueBindings.get(this, "bgcolor", bgcolor);
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getDir() {
        return ValueBindings.get(this, "dir", dir);
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getRules() {
        return ValueBindings.get(this, "rules", rules);
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getWidth() {
        return ValueBindings.get(this, "width", width);
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getTabindex() {
        return ValueBindings.get(this, "tabindex", tabindex);
    }

    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }

    public String getHorizontalGridLines() {
        return ValueBindings.get(this, "horizontalGridLines", horizontalGridLines);
    }

    public void setHorizontalGridLines(String horizontalGridLines) {
        this.horizontalGridLines = horizontalGridLines;
    }

    public String getVerticalGridLines() {
        return ValueBindings.get(this, "verticalGridLines", verticalGridLines);
    }

    public void setVerticalGridLines(String verticalGridLines) {
        this.verticalGridLines = verticalGridLines;
    }

    public String getCommonHeaderSeparator() {
        return ValueBindings.get(this, "commonHeaderSeparator", commonHeaderSeparator);
    }

    public void setCommonHeaderSeparator(String commonHeaderSeparator) {
        this.commonHeaderSeparator = commonHeaderSeparator;
    }

    public String getCommonFooterSeparator() {
        return ValueBindings.get(this, "commonFooterSeparator", commonFooterSeparator);
    }

    public void setCommonFooterSeparator(String commonFooterSeparator) {
        this.commonFooterSeparator = commonFooterSeparator;
    }

    public String getHeaderHorizSeparator() {
        return ValueBindings.get(this, "headerHorizSeparator", headerHorizSeparator);
    }

    public void setHeaderHorizSeparator(String headerHorizSeparator) {
        this.headerHorizSeparator = headerHorizSeparator;
    }

    public String getHeaderVertSeparator() {
        return ValueBindings.get(this, "headerVertSeparator", headerVertSeparator);
    }

    public void setHeaderVertSeparator(String headerVertSeparator) {
        this.headerVertSeparator = headerVertSeparator;
    }

    public String getMultiHeaderSeparator() {
        return ValueBindings.get(this, "multiHeaderSeparator", multiHeaderSeparator);
    }

    public void setMultiHeaderSeparator(String multiHeaderSeparator) {
        this.multiHeaderSeparator = multiHeaderSeparator;
    }

    public String getMultiFooterSeparator() {
        return ValueBindings.get(this, "multiFooterSeparator", multiFooterSeparator);
    }

    public void setMultiFooterSeparator(String multiFooterSeparator) {
        this.multiFooterSeparator = multiFooterSeparator;
    }

    public String getFooterHorizSeparator() {
        return ValueBindings.get(this, "footerHorizSeparator", footerHorizSeparator);
    }

    public void setFooterHorizSeparator(String footerHorizSeparator) {
        this.footerHorizSeparator = footerHorizSeparator;
    }

    public String getFooterVertSeparator() {
        return ValueBindings.get(this, "footerVertSeparator", footerVertSeparator);
    }

    public void setFooterVertSeparator(String footerVertSeparator) {
        this.footerVertSeparator = footerVertSeparator;
    }

    public String getBodyRowStyle() {
        return ValueBindings.get(this, "bodyRowStyle", bodyRowStyle);
    }

    public void setBodyRowStyle(String bodyRowStyle) {
        this.bodyRowStyle = bodyRowStyle;
    }

    public String getBodyRowClass() {
        return ValueBindings.get(this, "bodyRowClass", bodyRowClass);
    }

    public void setBodyRowClass(String bodyRowClass) {
        this.bodyRowClass = bodyRowClass;
    }

    public String getBodyOddRowStyle() {
        return ValueBindings.get(this, "bodyOddRowStyle", bodyOddRowStyle);
    }

    public void setBodyOddRowStyle(String bodyOddRowStyle) {
        this.bodyOddRowStyle = bodyOddRowStyle;
    }

    public String getBodyOddRowClass() {
        return ValueBindings.get(this, "bodyOddRowClass", bodyOddRowClass);
    }

    public void setBodyOddRowClass(String bodyOddRowClass) {
        this.bodyOddRowClass = bodyOddRowClass;
    }

    public String getHeaderRowStyle() {
        return ValueBindings.get(this, "headerRowStyle", headerRowStyle);
    }

    public void setHeaderRowStyle(String headerRowStyle) {
        this.headerRowStyle = headerRowStyle;
    }

    public String getHeaderRowClass() {
        return ValueBindings.get(this, "headerRowClass", headerRowClass);
    }

    public void setHeaderRowClass(String headerRowClass) {
        this.headerRowClass = headerRowClass;
    }

    public String getCommonHeaderRowStyle() {
        return ValueBindings.get(this, "commonHeaderRowStyle", commonHeaderRowStyle);
    }

    public void setCommonHeaderRowStyle(String commonHeaderRowStyle) {
        this.commonHeaderRowStyle = commonHeaderRowStyle;
    }

    public String getCommonHeaderRowClass() {
        return ValueBindings.get(this, "commonHeaderRowClass", commonHeaderRowClass);
    }

    public void setCommonHeaderRowClass(String commonHeaderRowClass) {
        this.commonHeaderRowClass = commonHeaderRowClass;
    }

    public String getFooterRowStyle() {
        return ValueBindings.get(this, "footerRowStyle", footerRowStyle);
    }

    public void setFooterRowStyle(String footerRowStyle) {
        this.footerRowStyle = footerRowStyle;
    }

    public String getFooterRowClass() {
        return ValueBindings.get(this, "footerRowClass", footerRowClass);
    }

    public void setFooterRowClass(String footerRowClass) {
        this.footerRowClass = footerRowClass;
    }

    public String getCommonFooterRowStyle() {
        return ValueBindings.get(this, "commonFooterRowStyle", commonFooterRowStyle);
    }

    public void setCommonFooterRowStyle(String commonFooterRowStyle) {
        this.commonFooterRowStyle = commonFooterRowStyle;
    }

    public String getCommonFooterRowClass() {
        return ValueBindings.get(this, "commonFooterRowClass", commonFooterRowClass);
    }

    public void setCommonFooterRowClass(String commonFooterRowClass) {
        this.commonFooterRowClass = commonFooterRowClass;
    }

    public boolean getApplyDefaultStyle() {
        return ValueBindings.get(this, "applyDefaultStyle", applyDefaultStyle, true);
    }

    public void setApplyDefaultStyle(boolean applyDefaultStyle) {
        this.applyDefaultStyle = applyDefaultStyle;
    }

    public boolean getUseAjax() {
        return ValueBindings.get(this, "useAjax", useAjax, true);
    }

    public void setUseAjax(boolean useAjax) {
        this.useAjax = useAjax;
    }

    public String getSortedColumnStyle() {
        return ValueBindings.get(this, "sortedColumnStyle", sortedColumnStyle);
    }

    public void setSortedColumnStyle(String style) {
        sortedColumnStyle = style;
    }

    public String getSortedColumnClass() {
        return ValueBindings.get(this, "sortedColumnClass", sortedColumnClass);
    }

    public void setSortedColumnClass(String styleClass) {
        sortedColumnClass = styleClass;
    }

    public String getSortedColumnHeaderStyle() {
        return ValueBindings.get(this, "sortedColumnHeaderStyle", sortedColumnHeaderStyle);
    }

    public void setSortedColumnHeaderStyle(String style) {
        sortedColumnHeaderStyle = style;
    }

    public String getSortedColumnHeaderClass() {
        return ValueBindings.get(this, "sortedColumnHeaderClass", sortedColumnHeaderClass);
    }

    public void setSortedColumnHeaderClass(String styleClass) {
        sortedColumnHeaderClass = styleClass;
    }

    public String getSortedColumnBodyStyle() {
        return ValueBindings.get(this, "sortedColumnBodyStyle", sortedColumnBodyStyle);
    }

    public void setSortedColumnBodyStyle(String style) {
        sortedColumnBodyStyle = style;
    }

    public String getSortedColumnBodyClass() {
        return ValueBindings.get(this, "sortedColumnBodyClass", sortedColumnBodyClass);
    }

    public void setSortedColumnBodyClass(String styleClass) {
        sortedColumnBodyClass = styleClass;
    }

    public String getSortedColumnFooterStyle() {
        return ValueBindings.get(this, "sortedColumnFooterStyle", sortedColumnFooterStyle);
    }

    public void setSortedColumnFooterStyle(String style) {
        sortedColumnFooterStyle = style;
    }

    public String getSortedColumnFooterClass() {
        return ValueBindings.get(this, "sortedColumnFooterClass", sortedColumnFooterClass);
    }

    public void setSortedColumnFooterClass(String styleClass) {
        sortedColumnFooterClass = styleClass;
    }

    public String getSortableHeaderStyle() {
        return ValueBindings.get(this, "sortableHeaderStyle", sortableHeaderStyle);
    }

    public void setSortableHeaderStyle(String sortableHeaderStyle) {
        this.sortableHeaderStyle = sortableHeaderStyle;
    }

    public String getSortableHeaderClass() {
        return ValueBindings.get(this, "sortableHeaderClass", sortableHeaderClass);
    }

    public void setSortableHeaderClass(String sortableHeaderClass) {
        this.sortableHeaderClass = sortableHeaderClass;
    }

    public String getSortableHeaderRolloverStyle() {
        return ValueBindings.get(this, "sortableHeaderRolloverStyle", sortableHeaderRolloverStyle);
    }

    public void setSortableHeaderRolloverStyle(String sortableHeaderRolloverStyle) {
        this.sortableHeaderRolloverStyle = sortableHeaderRolloverStyle;
    }

    public String getSortableHeaderRolloverClass() {
        return ValueBindings.get(this, "sortableHeaderRolloverClass", sortableHeaderRolloverClass);
    }

    public void setSortableHeaderRolloverClass(String sortableHeaderRolloverClass) {
        this.sortableHeaderRolloverClass = sortableHeaderRolloverClass;
    }

    public AbstractTableSelection getSelection() {
        return findSelectionChild();
    }

    public ColumnResizing getColumnResizing() {
        return findColumnResizingChild();
    }

    public void setSelection(AbstractTableSelection newSelection) {
        AbstractTableSelection oldSelection = findSelectionChild();
        if (oldSelection != null) {
            getChildren().remove(oldSelection);
            oldSelection.setModel(null);
        }

        if (newSelection != null) {
            getChildren().add(newSelection);
            newSelection.setModel(getModel());
        }
    }

    private AbstractTableSelection findSelectionChild() {
        List<UIComponent> children = getChildren();
        AbstractTableSelection selection = null;
        for (UIComponent child : children) {
            if (child instanceof AbstractTableSelection) {
                if (selection != null)
                    throw new RuntimeException("There should be only one selection child under this component: " + getId());
                selection = (AbstractTableSelection) child;
            }
        }
        return selection;
    }

    private ColumnResizing findColumnResizingChild() {
        List<UIComponent> children = getChildren();
        ColumnResizing columnResizing = null;
        for (UIComponent child : children) {
            if (child instanceof ColumnResizing) {
                if (columnResizing != null)
                    throw new RuntimeException("There should be only one <o:columnResizing> child under this component: " + getId());
                columnResizing = (ColumnResizing) child;
            }
        }
        return columnResizing;
    }


    protected void rememberSelectionByKeys() {
        AbstractTableSelection selection = getSelection();
        if (selection != null)
            selection.rememberByKeys();

        List<BaseColumn> columns = getAllColumns();
        for (int i = 0, columnCount = columns.size(); i < columnCount; i++) {
            BaseColumn column = columns.get(i);
            if (column instanceof CheckboxColumn) {
                ((CheckboxColumn) column).rememberByKeys();
            }
        }

    }

    private List<TableColumns> findColumnsComponents() {
        List<TableColumns> columnsComponents = new ArrayList<TableColumns>();
        List<UIComponent> children = getChildren();
        for (UIComponent component : children) {
            if (component instanceof TableColumns)
                columnsComponents.add((TableColumns) component);
        }
        return columnsComponents;
    }

    @Override
    public void processDecodes(FacesContext context) {
        beforeProcessDecodes(context);
        super.processDecodes(context);
        if (!isRendered())
            return;
        if (getRowIndex() != -1)
            setRowIndex(-1);
    }

    @Override
    public void processValidators(FacesContext context) {
        super.processValidators(context);
        if (!isRendered())
            return;
        if (getRowIndex() != -1)
            setRowIndex(-1);
        List<AbstractFilter> filters = getFilters();
        for (AbstractFilter filter : filters) {
            filter.processValidators(context);
        }
        AbstractTableSelection tableSelection = getSelection();
        if (tableSelection != null)
            tableSelection.processValidators(context);
    }

    @Override
    public void processUpdates(FacesContext context) {
        super.processUpdates(context);
        if (!isRendered())
            return;

        processModelUpdates(context);

        processModelDependentUpdates(context);

        ValueExpression sortColumnIdExpression = getValueExpression("sortColumnId");
        ELContext elContext = context.getELContext();
        if (sortColumnIdExpression != null)
            sortColumnIdExpression.setValue(elContext, getSortColumnId());
        ValueExpression sortAscendingExpression = getValueExpression("sortAscending");
        if (sortAscendingExpression != null)
            sortAscendingExpression.setValue(elContext, isSortAscending());

        List<AbstractFilter> filters = getFilters();
        for (AbstractFilter filter : filters) {
            filter.processUpdates(context);
        }

        ColumnResizing columnResizing = getColumnResizing();
        if (columnResizing != null)
            columnResizing.processUpdates(context);
    }

    protected void processModelUpdates(FacesContext context) {
        beforeUpdateValuesPhase = false;
        if (toggleColumnSorting != -1) {
            toggleSorting(toggleColumnSorting);
            toggleColumnSorting = -1;
        }
    }

    protected void processModelDependentUpdates(FacesContext context) {
        AbstractTableSelection selection = getSelection();
        if (selection != null) {
            selection.processUpdates(context);
            selection.beforeInvokeApplication();
        }

        List<BaseColumn> columns = getAllColumns();
        for (BaseColumn column : columns) {
            if (column instanceof CheckboxColumn) {
                column.processUpdates(context);
            }
        }

    }

    public void invokeBeforeRenderResponse(FacesContext context) {
        beforeRenderResponse(context);
    }

    protected void beforeRenderResponse(FacesContext context) {
        cachedAllColumns = null;
        cachedColumnsForRendering = null;

        List<AbstractFilter> filters = getFilters();
        int i = 0;
        while (i < filters.size()) {
            AbstractFilter filter = filters.get(i);
            filter.updateSearchStringFromBinding(context);
            i++;
        }

        AbstractTableSelection selection = getSelection();
        if (selection != null) {
            TableDataModel model = getModel();
            if (selection.getModel() == null)
                selection.setModel(model);
            if (model.getWrappedData() != null) // don't do it on the first rendering
                selection.rememberByKeys(); // for selection to be retained by id if data changes in the setWrappedData call
            selection.beforeEncode();
        }
        setUnavailableRowIndexes(null);

        checkSortingColumnsValid();
    }

    private void checkSortingColumnsValid() {
        List<SortingRule> sortingRules = getSortingRules();
        if (sortingRules == null || sortingRules.size() == 0)
            return;
        List<BaseColumn> allColumns = getAllColumns();
        for (SortingRule sortingRule : sortingRules) {
            String columnId = sortingRule.getSortColumnId();
            if (!isColumnIdValid(allColumns, columnId)) {
                setSortingRules(null);
                break;
            }
        }
    }

    private boolean isColumnIdValid(List<BaseColumn> allColumns, String columnId) {
        for (BaseColumn column : allColumns) {
            String colId = column.getId();
            if (colId.equals(columnId))
                return true;
        }
        return false;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        List<TableColumns> columnsComponents = findColumnsComponents();
        for (TableColumns tableColumns : columnsComponents) {
            tableColumns.resetCachedColumnList();
        }

        getAttributes().put(AbstractTableRenderer.CUSTOM_ROW_RENDERING_INFOS_KEY, new HashMap());
        super.encodeBegin(context);
    }

    public void setSortColumnId(String sortColumnId) {
        List<SortingRule> sortingRules = getSortingRules();
        if (sortColumnId != null) {
            if (sortingRules == null)
                sortingRules = new ArrayList<SortingRule>(1);
            SortingRule rule;
            if (sortingRules.size() == 0) {
                rule = new SortingRule();
                sortingRules.add(rule);
            } else {
                rule = sortingRules.get(0);
            }
            rule.setSortColumnId(sortColumnId);
        } else
            sortingRules = null;
        setSortingRules(sortingRules);
    }

    public int getSortColumnIndex() {
        String sortColumnId = getSortColumnId();
        List<BaseColumn> columns = getColumnsForRendering();
        for (int i = 0, count = columns.size(); i < count; i++) {
            BaseColumn column = columns.get(i);
            String id = column.getId();
            if (id != null && id.equals(sortColumnId))
                return i;
        }
        return -1;
    }

    public void setSortColumnIndex(int colIndex) {
        BaseColumn column = getColumn(colIndex);
        if (column == null) {
            setSortColumnId(null);
            return;
        }
        setSortColumnId(column.getId());
    }

    public String getFilterRowStyle() {
        return ValueBindings.get(this, "filterRowStyle", filterRowStyle);
    }

    public void setFilterRowStyle(String filterRowStyle) {
        this.filterRowStyle = filterRowStyle;
    }

    public String getFilterRowClass() {
        return ValueBindings.get(this, "filterRowClass", filterRowClass);
    }

    public void setFilterRowClass(String filterRowClass) {
        this.filterRowClass = filterRowClass;
    }

    public String getFilterRowSeparator() {
        return ValueBindings.get(this, "filterRowSeparator", filterRowSeparator);
    }

    public void setFilterRowSeparator(String filterRowSeparator) {
        this.filterRowSeparator = filterRowSeparator;
    }

    public String getAllRecordsFilterName() {
        return ValueBindings.get(this, "allRecordsFilterName", allRecordsFilterName);
    }

    public void setAllRecordsFilterName(String allRecordsFilterName) {
        this.allRecordsFilterName = allRecordsFilterName;
    }

    public String getEmptyRecordsFilterName() {
        return ValueBindings.get(this, "emptyRecordsFilterName", emptyRecordsFilterName);
    }

    public void setEmptyRecordsFilterName(String emptyRecordsFilterName) {
        this.emptyRecordsFilterName = emptyRecordsFilterName;
    }

    public String getNonEmptyRecordsFilterName() {
        return ValueBindings.get(this, "nonEmptyRecordsFilterName", nonEmptyRecordsFilterName);
    }

    public void setNonEmptyRecordsFilterName(String nonEmptyRecordsFilterName) {
        this.nonEmptyRecordsFilterName = nonEmptyRecordsFilterName;
    }

    public String getSortColumnId() {
        List<SortingRule> sortingRules = getSortingRules();
        if (sortingRules == null)
            return null;
        if (sortingRules.size() == 0)
            return null;
        SortingRule sortingRule = sortingRules.get(0);
        return sortingRule.getSortColumnId();
    }

    public boolean isSortAscending() {
        List<SortingRule> sortingRules = getSortingRules();
        if (sortingRules == null)
            return true;
        if (sortingRules.size() == 0)
            return true;
        SortingRule sortingRule = sortingRules.get(0);
        return sortingRule.isSortAscending();
    }

    public void setSortAscending(boolean value) {
        List<SortingRule> sortingRules = getSortingRules();
        SortingRule rule;
        if (sortingRules == null)
            sortingRules = new ArrayList<SortingRule>(1);
        if (sortingRules.size() == 0) {
            rule = new SortingRule();
            sortingRules.add(rule);
        } else {
            rule = sortingRules.get(0);
        }
        rule.setSortAscending(value);
        setSortingRules(sortingRules);
    }


    protected List<SortingRule> getSortingRules() {
        return sortingRules;
    }

    protected void setSortingRules(List<SortingRule> sortingRules) {
        this.sortingRules = sortingRules;
    }

    public void toggleSorting(int columnIndex) {
        rememberSelectionByKeys();
        if (beforeUpdateValuesPhase) {
            toggleColumnSorting = columnIndex;
            return;
        }

        List<BaseColumn> columns = getColumnsForRendering();
        if (columnIndex < 0 || columnIndex >= columns.size())
            throw new IllegalArgumentException("columnIndex out of range. No of columns available: " + columns.size());
        BaseColumn column = columns.get(columnIndex);
        if (!(column instanceof TableColumn || column instanceof SelectionColumn || column instanceof CheckboxColumn))
            throw new IllegalArgumentException("Column is not sortable at index (" + columnIndex + "). Column class is " + column.getClass());
        String columnId = column.getId();
        if (columnId != null && columnId.equals(getSortColumnId())) {
            boolean newDirection = !isSortAscending();
            setSortAscending(newDirection);
        } else {
            setSortColumnId(columnId);
            setSortAscending(true);
        }
    }

    protected Comparator<Object> createRowDataComparator(List<SortingRule> sortingRules) {
        if (sortingRules == null)
            return null;
        int sortingRuleCount = sortingRules.size();
        if (sortingRuleCount == 0)
            return null;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        final Comparator<Object>[] comparators = new Comparator[sortingRuleCount];
        int readyComparatorCount = 0;
        for (int i = 0; i < sortingRuleCount; i++) {
            SortingRule rule = sortingRules.get(i);
            Comparator<Object> ruleComparator = createRuleComparator(facesContext, rule);
            if (ruleComparator == null)
                continue;
            comparators[readyComparatorCount++] = ruleComparator;
        }
        final int readyComparatorCount1 = readyComparatorCount;
        return new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                for (int i = 0; i < readyComparatorCount1; i++) {
                    Comparator<Object> comparator = comparators[i];
                    int result = comparator.compare(o1, o2);
                    if (result != 0)
                        return result;
                }
                return 0;
            }
        };
    }

    protected Comparator<Object> createRuleComparator(final FacesContext facesContext, SortingRule rule) {
        String sortedColumnId = rule.getSortColumnId();
        if (sortedColumnId == null)
            return null;
        List<BaseColumn> allColumns = getAllColumns();
        BaseColumn column = findColumnById(allColumns, sortedColumnId);
        boolean ordinaryColumn = column instanceof TableColumn;
        if (column == null ||
                !(ordinaryColumn || column instanceof CheckboxColumn || column instanceof SelectionColumn))
            return null;
        ValueExpression sortingExpression;
        if (ordinaryColumn) {
            TableColumn tableColumn = (TableColumn) column;
            sortingExpression = tableColumn.getSortingExpression();
        } else {
            sortingExpression = itemSelectedExpressionForColumn(column);
        }
        if (sortingExpression == null)
            return null;

        ValueExpression sortingComparatorBinding = ordinaryColumn ? ((TableColumn) column).getSortingComparatorBinding() : null;
        Comparator<Object> comparator = sortingComparatorBinding != null
                ? (Comparator<Object>) sortingComparatorBinding.getValue(facesContext.getELContext())
                : null;

        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        boolean sortAscending = rule.isSortAscending();
        return createRowComparator(facesContext, sortingExpression, comparator, requestMap, sortAscending);
    }

    private ValueExpression itemSelectedExpressionForColumn(final BaseColumn column) {
        final Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        final String var = getVar();
        if (column instanceof SelectionColumn || column instanceof CheckboxColumn)
            return new ValueExpression() {
                public Object getValue(ELContext context) {
                    boolean thisRowSelected = isRowInColumnSelected(column, requestMap, var);
                    return !thisRowSelected;
                }

                public void setValue(ELContext context, Object value) {
                    throw new UnsupportedOperationException();
                }

                public boolean isReadOnly(ELContext context) {
                    return true;
                }

                public Class getType(ELContext context) {
                    return Boolean.class;
                }

                public Class getExpectedType() {
                    return Boolean.class;
                }

                public String getExpressionString() {
                    return null;
                }

                public boolean equals(Object o) {
                    return false;
                }

                public int hashCode() {
                    return 0;
                }

                public boolean isLiteralText() {
                    return false;
                }
            };
        else
            throw new IllegalArgumentException("Unknown column type: " + column.getClass().getName());
    }

    protected abstract boolean isRowInColumnSelected(BaseColumn column, Map<String, Object> requestMap, String var);

    protected RowComparator createRowComparator(
            FacesContext facesContext,
            ValueExpression sortingExpression,
            Comparator<Object> valueComparator,
            Map<String, Object> requestMap,
            boolean sortAscending) {
        return new RowComparator(facesContext, sortingExpression, valueComparator, requestMap, sortAscending);
    }

    protected List<AbstractFilter> getActiveFilters() {
        List<AbstractFilter> filters = new ArrayList<AbstractFilter>();

        List<AbstractFilter> allFilters = getFilters();
        for (AbstractFilter filter : allFilters) {
            if (filter.isAcceptingAllRecords())
                continue;
            filters.add(filter);
        }
        return filters;
    }

    @Override
    public void setParent(UIComponent parent) {
        super.setParent(parent);
        // register filters if not registered yet
        ComponentUtil.runScheduledActions();
    }

    public List<AbstractFilter> getFilters() {
        return myFilters;
    }

    public Object getFilteredValueByData(
            FacesContext facesContext,
            Map<String, Object> requestMap,
            ValueExpression criterionNameExpression,
            String var,
            Object data) {
        requestMap.get(var);
        requestMap.put(var, data);
        Object result = criterionNameExpression.getValue(facesContext.getELContext());
        return result;
    }

    public String getRolloverRowStyle() {
        return ValueBindings.get(this, "rolloverRowStyle", rolloverRowStyle);
    }

    public void setRolloverRowStyle(String rolloverRowStyle) {
        this.rolloverRowStyle = rolloverRowStyle;
    }

    public String getFocusedStyle() {
        return ValueBindings.get(this, "focusedStyle", focusedStyle);
    }

    public void setFocusedStyle(String focusedStyle) {
        this.focusedStyle = focusedStyle;
    }

    public String getFocusedClass() {
        return ValueBindings.get(this, "focusedClass", focusedClass);
    }

    public void setFocusedClass(String focusedClass) {
        this.focusedClass = focusedClass;
    }

    public String getRolloverRowClass() {
        return ValueBindings.get(this, "rolloverRowClass", rolloverRowClass);
    }

    public void setRolloverRowClass(String rolloverRowClass) {
        this.rolloverRowClass = rolloverRowClass;
    }

    public UIComponent getNoDataMessage() {
        return getFacet("noDataMessage");
    }

    public UIComponent getNoFilterDataMessage() {
        return getFacet("noFilterDataMessage");
    }

    public String getNoDataRowStyle() {
        return ValueBindings.get(this, "noDataRowStyle", noDataRowStyle);
    }

    public void setNoDataRowStyle(String noDataRowStyle) {
        this.noDataRowStyle = noDataRowStyle;
    }

    public String getNoDataRowClass() {
        return ValueBindings.get(this, "noDataRowClass", noDataRowClass);
    }

    public void setNoDataRowClass(String noDataRowClass) {
        this.noDataRowClass = noDataRowClass;
    }

    public boolean getNoDataMessageAllowed() {
        return ValueBindings.get(this, "noDataMessageAllowed", noDataMessageAllowed, true);
    }

    public void setNoDataMessageAllowed(boolean noDataMessageAllowed) {
        this.noDataMessageAllowed = noDataMessageAllowed;
    }

    public abstract boolean isDataSourceEmpty();

    public String getColumnIdVar() {
        return columnIdVar;
    }

    public void setColumnIdVar(String columnIdVar) {
        this.columnIdVar = columnIdVar;
    }

    public String getColumnIndexVar() {
        return columnIndexVar;
    }

    public void setColumnIndexVar(String columnIndexVar) {
        this.columnIndexVar = columnIndexVar;
    }

    protected void updateSortingFromBindings() {
        TableDataModel model = getModel();
        model.startUpdate();
        try {
            FacesContext facesContext = getFacesContext();
            ValueExpression sortAscendingExpression = getValueExpression("sortAscending");
            ELContext elContext = facesContext.getELContext();
            if (sortAscendingExpression != null) {
                Object sortAscendingObj = sortAscendingExpression.getValue(elContext);
                if (!(sortAscendingObj instanceof Boolean))
                    throw new IllegalArgumentException("Illegal value returned from the 'sortAscending' attribute binding of DataTable orTreeTable with client-id (" + getClientId(facesContext) + "). " +
                            "It should be an instance of java.lang.Boolean, but was: " + (sortAscendingObj != null ? sortAscendingObj.getClass().getName() : "null"));
                boolean newSortAscending = (Boolean) sortAscendingObj;
                if (isSortAscending() != newSortAscending)
                    setSortAscending(newSortAscending);
            }
            ValueExpression sortColumnIdExpression = getValueExpression("sortColumnId");
            if (sortColumnIdExpression != null) {
                Object sortColumnIdObj = sortColumnIdExpression.getValue(elContext);
                if (sortColumnIdObj != null && !(sortColumnIdObj instanceof String))
                    throw new IllegalArgumentException("Illegal value returned from the 'sortColumnId' attribute binding of DataTable or TreeTable with client-id (" + getClientId(facesContext) + "). " +
                            "It should be an instance of java.lang.String, but was: " + sortColumnIdObj.getClass().getName());
                String newSortColumnId = (String) sortColumnIdObj;
                String oldSortColumnId = getSortColumnId();
                if (((newSortColumnId == null) != (oldSortColumnId == null)) ||
                        (newSortColumnId != null && !newSortColumnId.equals(oldSortColumnId)))
                    setSortColumnId(newSortColumnId);
            }
        } finally {
            model.endUpdate();
        }
    }

    protected boolean isRowsDecodingRequired() {
        String forceRowsDecodingStr = (String) getAttributes().get("forceRowsDecoding");
        boolean forceRowsDecoding = forceRowsDecodingStr != null && Boolean.valueOf(forceRowsDecodingStr);
        if (forceRowsDecoding)
            return true;

        if (getSelection() != null)
            return true;

        List<BaseColumn> columns = getColumnsForProcessing();
        for (BaseColumn column : columns) {
            if (column instanceof CheckboxColumn)
                return true;
            List<UIComponent> columnChildren = column.getChildren();
            for (UIComponent columnChild : columnChildren) {
                if (isComponentRequiresDecoding(columnChild))
                    return true;
            }
        }

        List<TableRow> customRows = getCustomRows();
        for (TableRow customRow : customRows) {
            List<UIComponent> rowChildren = customRow.getChildren();
            for (UIComponent rowChild : rowChildren) {
                if (!(rowChild instanceof TableCell))
                    continue;
                List<UIComponent> cellChildren = rowChild.getChildren();
                for (UIComponent cellChild : cellChildren) {
                    if (isComponentRequiresDecoding(cellChild))
                        return true;
                }
            }
        }

        return false;
    }

    private boolean isComponentRequiresDecoding(UIComponent component) {
        boolean thisComponentRequiresDecoding = component instanceof EditableValueHolder || component instanceof ActionSource;
        if (thisComponentRequiresDecoding)
            return true;
        List<UIComponent> children = component.getChildren();
        for (UIComponent subComponent : children) {
            if (isComponentRequiresDecoding(subComponent))
                return true;
        }
        return false;
    }

    protected class RowComparator implements Comparator<Object> {
        private final FacesContext facesContext;
        private final ValueExpression sortingExpressionBinding;
        private final Comparator<Object> valueComparator;
        private final Map<String, Object> requestMap;
        private final boolean sortAscending;

        protected final String var;

        public RowComparator(
                FacesContext facesContext,
                ValueExpression valueExpression,
                Comparator<Object> valueComparator,
                Map<String, Object> requestMap,
                boolean sortAscending) {
            this.facesContext = facesContext;
            sortingExpressionBinding = valueExpression;
            this.valueComparator = valueComparator;
            this.requestMap = requestMap;
            var = getVar();
            this.sortAscending = sortAscending;
        }

        public int compare(Object o1, Object o2) {
            populateSortingExpressionParams(requestMap, o1);
            ELContext elContext = facesContext.getELContext();
            Object value1 = sortingExpressionBinding.getValue(elContext);
            populateSortingExpressionParams(requestMap, o2);
            Object value2 = sortingExpressionBinding.getValue(elContext);
            clearSortingExpressionParams(requestMap);
            int result;
            if (value1 == null)
                result = (value2 == null) ? 0 : -1;
            else if (value2 == null)
                result = 1;
            else if (valueComparator != null) {
                result = valueComparator.compare(value1, value2);
            } else if (value1 instanceof Comparable) {
                result = ((Comparable) value1).compareTo(value2);
            } else {
                throw new RuntimeException("The values to be sorted must implement the Comparable interface: " + value1.getClass());
            }
            if (!sortAscending)
                result = -result;
            return result;
        }

        protected void clearSortingExpressionParams(Map requestMap) {
            requestMap.remove(var);
        }

        protected void populateSortingExpressionParams(Map<String, Object> requestMap, Object collectionObject) {
            requestMap.put(var, collectionObject);
        }
    }

    @Override
    public String getClientId(FacesContext context) {
        String clientId = getStandardClientId(context);
        int rowIndex = getRowIndex();
        if (rowIndex == -1)
            return clientId;
        else
            return clientId + NamingContainer.SEPARATOR_CHAR + rowIndex;
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        cachedClientId = null;
    }

    private String getStandardClientId(FacesContext context) {
        if (cachedClientId != null)
            return cachedClientId;

        String id = getId();
        if (id == null) {
            UIViewRoot viewRoot = context.getViewRoot();
            id = viewRoot.createUniqueId();
            setId(id);
        }

        UIComponent namingContainer = getParent();
        while (namingContainer != null && !(namingContainer instanceof NamingContainer))
            namingContainer = namingContainer.getParent();

        String parentId = namingContainer != null ? namingContainer.getClientId(context) + NamingContainer.SEPARATOR_CHAR : "";
        String clientId = parentId + id;

        Renderer renderer = getRenderer(context);
        if (renderer != null)
            clientId = renderer.convertClientId(context, clientId);

        cachedClientId = clientId;
        return clientId;
    }

    public int getTotalRowCount() {
        return getModel().getTotalRowCount();
    }
}
