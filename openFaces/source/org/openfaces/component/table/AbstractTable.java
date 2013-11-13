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
package org.openfaces.component.table;

import org.openfaces.component.ComponentWithExternalParts;
import org.openfaces.component.FilterableComponent;
import org.openfaces.component.OUIData;
import org.openfaces.component.TableStyles;
import org.openfaces.component.filter.Filter;
import org.openfaces.component.table.impl.DynamicColumn;
import org.openfaces.component.table.impl.NodeInfo;
import org.openfaces.component.table.impl.RowComparator;
import org.openfaces.component.table.impl.TableDataModel;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.renderkit.table.TableStructure;
import org.openfaces.renderkit.table.TreeTableRenderer;
import org.openfaces.util.Components;
import org.openfaces.util.Environment;
import org.openfaces.util.Log;
import org.openfaces.util.ReflectionUtil;
import org.openfaces.util.ValueBindings;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PhaseId;
import javax.faces.event.PostRestoreStateEvent;
import javax.faces.model.DataModel;
import javax.faces.render.Renderer;
import java.util.*;

/**
 * @author Dmitry Pikhulya
 */
@ListenerFor(systemEventClass = PostRestoreStateEvent.class)
@ResourceDependencies({
        @ResourceDependency(name = "default.css", library = "openfaces"),
        @ResourceDependency(name = "jsf.js", library = "javax.faces")
})
public abstract class AbstractTable extends OUIData implements TableStyles, FilterableComponent, ComponentWithExternalParts {
    private static final String KEY_SORTING_TOGGLED = AbstractTable.class + ".processModelUpdates().sortingToggled";
    /*
   Implementation notes:
   - the full life-cycle for the selection child is intentionally ensured. Although implementation of
     selection doesn't require this itself, Ajax4jsf on selection's "onchange" event didn't work until the selection
     component was made to process all phases [JSFC-2049].
    */

    public static final String FACET_HEADER = "header";
    public static final String FACET_FOOTER = "footer";
    public static final String FACET_ABOVE = "above";
    public static final String FACET_BELOW = "below";

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

    private String subHeaderRowStyle;
    private String subHeaderRowClass;
    private String subHeaderRowSeparator;
    private String allRecordsFilterText;
    private String emptyRecordsFilterText;
    private String nonEmptyRecordsFilterText;

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
    private Iterable<String> columnsOrder;

    private String sortedAscendingImageUrl;
    private String sortedDescendingImageUrl;

    private List<SortingRule> incomingSortingRules;
    private boolean beforeUpdateValuesPhase = true;
    private List<BaseColumn> cachedAllColumns;
    private List<BaseColumn> cachedRenderedColumns;
    private Map<String, BaseColumn> cachedColumnsByIds;
    private String cachedClientId;
    private List<Filter> myFilters = new ArrayList<Filter>();
    private Integer autoFilterDelay;
    private Boolean deferBodyLoading;
    private Integer totalRowCount;
    private boolean implicitFacetsCreated;
    private Boolean unsortedStateAllowed;

    private Boolean keepSelectionVisible;
    private Boolean rowsDecodingRequired = true;

    private String onbeforeajaxreload;
    private String onafterajaxreload;

    private Boolean unDisplayedSelectionAllowed;


    public AbstractTable() {
        super.setUiDataValue(new TableDataModel(this));
    }

    @Override
    public Object processSaveState(FacesContext context) {
        AbstractTableSelection selection = getSelection();
        TableDataModel model = getModel();
        if (selection != null && selection.getModel() == null) {
            // Fix for JSFC-2945 (selection's model can be null if table is not rendered and selection's rowData is declared as constant)
            selection.setModel(model);
        }

        Object result = super.processSaveState(context);

        // Model instance is saved as is in the "server" state saving mode. The table reference is unnecessary in the
        // state and it contains references to the entire view state tree, so we're just cutting this reference to
        // prevent excessive memory consumption (see OF-196)
        model.setTable(null);

        return result;
    }

    @Override
    public Object saveState(FacesContext context) {
        remindStateSavingOfTableData();
        // It's possible that row index is not reset when performing partial Ajax request in
        // PartialViewContext.prepareAjaxPortions method: it specifies rowIndex to to a value other than -1 when
        // invoking UtilPhaseListener.findComponentById method.
        // The following two lines make sure row index is reset to -1 for getClientId() to return component's "native"
        // client id without row index, which is required for proper StateManagementStrategyImpl.saveComponentState
        // (see line 366 as of Mojarra 2.0.3).
        if (getRowIndex() != -1) setRowIndex(-1);

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
                useAjax, allRecordsFilterText, emptyRecordsFilterText, nonEmptyRecordsFilterText,
                subHeaderRowStyle, subHeaderRowClass, subHeaderRowSeparator, focusedStyle, focusedClass,
                rolloverRowStyle, rolloverRowClass, noDataRowStyle, noDataRowClass,
                noDataMessageAllowed, columnIndexVar, columnIdVar, saveAttachedState(context, columnsOrder),
                sortedAscendingImageUrl, sortedDescendingImageUrl, cachedClientId,
                autoFilterDelay, deferBodyLoading, totalRowCount, implicitFacetsCreated, unsortedStateAllowed,
                keepSelectionVisible, onbeforeajaxreload, onafterajaxreload, unDisplayedSelectionAllowed};
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

        allRecordsFilterText = (String) state[i++];
        emptyRecordsFilterText = (String) state[i++];
        nonEmptyRecordsFilterText = (String) state[i++];
        subHeaderRowStyle = (String) state[i++];
        subHeaderRowClass = (String) state[i++];
        subHeaderRowSeparator = (String) state[i++];

        focusedStyle = (String) state[i++];
        focusedClass = (String) state[i++];
        rolloverRowStyle = (String) state[i++];
        rolloverRowClass = (String) state[i++];

        noDataRowStyle = (String) state[i++];
        noDataRowClass = (String) state[i++];
        noDataMessageAllowed = (Boolean) state[i++];
        columnIndexVar = (String) state[i++];
        columnIdVar = (String) state[i++];
        columnsOrder = (Iterable<String>) restoreAttachedState(context, state[i++]);
        sortedAscendingImageUrl = (String) state[i++];
        sortedDescendingImageUrl = (String) state[i++];
        cachedClientId = (String) state[i++];
        autoFilterDelay = (Integer) state[i++];
        deferBodyLoading = (Boolean) state[i++];
        totalRowCount = (Integer) state[i++];
        implicitFacetsCreated = (Boolean) state[i++];
        unsortedStateAllowed = (Boolean) state[i++];

        keepSelectionVisible = (Boolean) state[i++];

        onbeforeajaxreload = (String) state[i++];
        onafterajaxreload = (String) state[i++];

        unDisplayedSelectionAllowed = (Boolean) state[i++];

        beforeUpdateValuesPhase = true;
        incomingSortingRules = null;
    }

    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        super.processEvent(event);
        if (event instanceof PostRestoreStateEvent) {
            afterRestoreState(getFacesContext());
        }
    }

    protected void afterRestoreState(FacesContext context) {
        TableDataModel model = (TableDataModel) getUiDataValue();
        model.setTable(this);

        AbstractTableSelection selection = getSelection();
        if (selection != null)
            selection.setModel(model);

        List<BaseColumn> columns = getAllColumns();
        for (BaseColumn column : columns) {
            if (column instanceof CheckboxColumn) {
                ((CheckboxColumn) column).assignDataModel();
            }
        }
    }

    protected void beforeProcessDecodes(FacesContext context) {
        cachedAllColumns = null;
        cachedRenderedColumns = null;
    }

    @Override
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        if (context.getFacesContext().getCurrentPhaseId() != PhaseId.RESTORE_VIEW && context.getFacesContext().getCurrentPhaseId() != PhaseId.RENDER_RESPONSE
                && getRowIndex() == -1 /* beforeProcessDecodes is not expected to be invoked in the midst of iteration over its rows */)
            invokeBeforeProcessDecodes(context.getFacesContext());
        return super.visitTree(context, callback);
    }

    public void invokeBeforeProcessDecodes(FacesContext context) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        String key = AbstractTable.class.getName() + ".beforeProcessDecodesInvoked_" + getStandardClientId(context);
        if (requestMap.containsKey(key)) return;
        requestMap.put(key, true);
        if (getRowIndex() != -1)
            throw new IllegalArgumentException("beforeProcessDecodes is not expected to be invoked in the midst of iteration over its rows");
        beforeProcessDecodes(context);
    }

    protected TableDataModel getModel() {
        TableDataModel uiDataValue = (TableDataModel) getUiDataValue();
        AbstractTable table = uiDataValue.getTable();
        if (table == null) {
            // The model's table reference is normally cleared before the state is saved
            // (see AbstractTable.processSaveState), and then initialized again after restoring state
            // (see AbstractTable.afterRestoreState). It is possible that the same instance of a table component gets
            // through the state saving phase and then rerendered without restoring the state though. It is the case
            // when the table component is bound to a session-scoped value, where it is created explicitly, and the same
            // page is opened with the HTTP GET request for the second time (for an application that has a "server"
            // state saving mode). Hence we have this check that guards against such a situation when there's an attempt
            // to use a model without the table reference in the beginning of a lifecycle. E.g. this fix is important
            // for DataTable.testPagination test to work properly.
            uiDataValue.setTable(this);
        }

        return uiDataValue;
    }

    @Override
    public void setRowIndex(int rowIndex) {
        int prevRowIndex = getRowIndex();
        if (prevRowIndex == rowIndex)
            return;
        // it is important implicit column facets, such as the ones implicitly created for the summary calculation
        // feature, to be created before the first setRowIndex call because changing the set of facets afterwards
        // might break its state saving mechanism
        ensureImplicitFacetsCreated();

        super.setRowIndex(rowIndex);

        List<UIComponent> components = getAdditionalComponentsRequiringClientIdReset();
        for (UIComponent component : components) {
            Components.clearCachedClientIds(component);
        }
    }

    private void ensureImplicitFacetsCreated() {
        if (!implicitFacetsCreated) {
            createImplicitColumnFacets(false);
            implicitFacetsCreated = true;
        }
        createImplicitColumnFacets(true);
    }

    private List<UIComponent> additionalComponentsRequiringClientIdReset;

    /**
     * Some column facets, which are rendered several times per table, such as "inColumnHeader" might contain some
     * components, such as <o:summary>, which are sensitive to the fact that their client id is not repeated when the
     * same component is rendered in different rows. Hence this resets their client ids
     *
     * @return
     */
    private List<UIComponent> getAdditionalComponentsRequiringClientIdReset() {
        if (additionalComponentsRequiringClientIdReset == null) {
            additionalComponentsRequiringClientIdReset = new ArrayList<UIComponent>();
            List<BaseColumn> columns = getAllColumns();
            for (BaseColumn column : columns) {
                Map<String, UIComponent> facets = column.getFacets();
                for (Map.Entry<String, UIComponent> entry : facets.entrySet()) {
                    String facetName = entry.getKey();
                    if (!(
                            facetName.equals(BaseColumn.FACET_HEADER) ||
                                    facetName.equals(BaseColumn.FACET_SUB_HEADER) ||
                                    facetName.equals(BaseColumn.FACET_FOOTER)
                    ))
                        additionalComponentsRequiringClientIdReset.add(entry.getValue());
                }
            }
        }
        return additionalComponentsRequiringClientIdReset;
    }

    public boolean isRowAvailable() {
        DataModel dataModel = getDataModel();
        if (dataModel != getModel())
            throw new IllegalStateException("table.getDataModel() != table.getModel(). It's possible that " +
                    "getDataModel() during state restoring before table's StateHelper has been restored. table id = " + getId());
        return dataModel.isRowAvailable();
    }

    public UIComponent getHeader() {
        return Components.getFacet(this, FACET_HEADER);
    }



    public void setHeader(UIComponent component) {
        getFacets().put(FACET_HEADER, component);
    }

    public UIComponent getFooter() {
        return Components.getFacet(this, FACET_FOOTER);
    }

    public void setFooter(UIComponent component) {
        getFacets().put(FACET_FOOTER, component);
    }

    public UIComponent getAbove() {
        UIComponent component = Components.getFacet(this, FACET_ABOVE);
        ensureFacetHasOwnId(component, FACET_ABOVE);
        return component;
    }

    public void setAbove(UIComponent component) {
        getFacets().put(FACET_ABOVE, component);
    }

    public UIComponent getBelow() {
        UIComponent component = Components.getFacet(this, FACET_BELOW);
        ensureFacetHasOwnId(component, FACET_BELOW);
        return component;
    }

    public void setBelow(UIComponent component) {
        getFacets().put(FACET_BELOW, component);
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
        return getRenderedColumns();
    }

    public List<BaseColumn> getRenderedColumns() {
        if (cachedRenderedColumns == null) {
            cachedRenderedColumns = Collections.unmodifiableList(calculateRenderedColumns());
        }
        return cachedRenderedColumns;
    }

    private List<BaseColumn> calculateRenderedColumns() {
        Iterable<String> columnsOrder = getColumnsOrder();
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

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public BaseColumn findColumnById(List<BaseColumn> allColumns, String columnId) {
        BaseColumn colById = null;
        for (BaseColumn col : allColumns) {
            if (columnId.equals(col.getId())) {
                colById = col;
                break;
            }
        }
        return colById;
    }

    protected void createImplicitColumnFacets(boolean forDynamicColumns) {
        List<BaseColumn> allColumns = getAllColumns();
        for (BaseColumn column : allColumns) {
            boolean thisIsADynamicColumn = column instanceof DynamicColumn;
            if (forDynamicColumns == thisIsADynamicColumn)
                column.createImplicitFacets();
        }
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

    // todo: rename columnsOrder attribute to renderedColumnIds
    public Iterable<String> getColumnsOrder() {
        return ValueBindings.get(this, "columnsOrder", columnsOrder, Iterable.class);
    }

    public void setColumnsOrder(Iterable<String> columnsOrder) {
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

        List<BaseColumn> columns = getRenderedColumns();
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

    @Override
    protected List<UIComponent> getExtensionComponents() {

        ArrayList<UIComponent> components = new ArrayList<UIComponent>(Arrays.asList(
                getSelection(),
                getColumnReordering(),
                getColumnResizing(),
                getScrolling(),
                getAbove(),
                getBelow()));
        List<Columns> columnsComponents = Components.findChildrenWithClass(this, Columns.class);
        components.addAll(columnsComponents);
        return components;
    }

    public AbstractTableSelection getSelection() {
        return findSelectionChild();
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
        List<AbstractTableSelection> selectionChildren =
                Components.findChildrenWithClass(this, AbstractTableSelection.class, true, false);
        if (selectionChildren.size() > 1)
            throw new RuntimeException("There should be only one selection child with its rendered attribute set to true under this component: " + getId());
        return selectionChildren.size() > 0 ? selectionChildren.get(0) : null;
    }

    public ColumnResizing getColumnResizing() {
        return Components.findChildWithClass(this, ColumnResizing.class, "<o:columnResizing>");
    }

    public ColumnReordering getColumnReordering() {
        return Components.findChildWithClass(this, ColumnReordering.class, "<o:columnReordering>");
    }

    public Scrolling getScrolling() {
        return Components.findChildWithClass(this, Scrolling.class, "<o:scrolling>");
    }

    private Iterable<Summary> summaries;

    public Iterable<Summary> getSummaryComponents() {
        if (summaries == null) {
            ensureImplicitFacetsCreated();
            Set<ColumnGroup> columnGroups = new HashSet<ColumnGroup>();
            List<UIComponent> facets = new ArrayList<UIComponent>();
            // components returned as DynamicColumn's facets report the Columns component and not the DynamicColumn
            // against which they were retrieved, and since we need the actual columns for each of the Summary component
            // we're maintaining an auxiliary facetOwners list here
            List<UIComponent> facetOwners = new ArrayList<UIComponent>();

            Collection<UIComponent> tableFacets = this.getFacets().values();
            for (UIComponent tableFacet : tableFacets) {
                facets.add(tableFacet);
                facetOwners.add(this);
            }

            List<BaseColumn> allColumns = getAllColumns();
            for (BaseColumn column : allColumns) {
                for (UIComponent parent = column.getParent(); parent instanceof ColumnGroup; parent = parent.getParent())
                    columnGroups.add((ColumnGroup) parent);
                List<UIComponent> applicableFacets = Components.getFacets(column,
                        BaseColumn.FACET_HEADER, BaseColumn.FACET_SUB_HEADER, BaseColumn.FACET_FOOTER,
                        BaseColumn.FACET_GROUP_HEADER,
                        BaseColumn.FACET_IN_GROUP_HEADER, BaseColumn.FACET_IN_GROUP_FOOTER,
                        BaseColumn.FACET_GROUP_FOOTER);
                for (UIComponent applicableFacet : applicableFacets) {
                    facets.add(applicableFacet);
                    facetOwners.add(column);
                }
            }

            for (ColumnGroup columnGroup : columnGroups) {
                List<UIComponent> applicableFacets = Components.getFacets(columnGroup,
                        BaseColumn.FACET_HEADER, BaseColumn.FACET_FOOTER
                );
                for (UIComponent applicableFacet : applicableFacets) {
                    facets.add(applicableFacet);
                    facetOwners.add(columnGroup);
                }
            }

            final List<Summary> summaries = new ArrayList<Summary>();
            final List<UIComponent> summaryOwners = new ArrayList<UIComponent>();
            for (int i = 0, facetsSize = facets.size(); i < facetsSize; i++) {
                UIComponent facet = facets.get(i);
                UIComponent facetOwner = facetOwners.get(i);
                List<Summary> summariesInThisComponent = (facet instanceof Summary)
                        ? Collections.singletonList((Summary) facet)
                        : Components.findChildrenWithClass(facet, Summary.class, true, true);
                for (Summary summary : summariesInThisComponent) {
                    summaries.add(summary);
                    summaryOwners.add(facetOwner);
                }
            }

            // the custom iterable implementation is required here instead of just returning the summaries variable
            // in order for the variables of dynamic columns, which might be the parents for any of the returned Summary
            // components, to be set up properly during the iteration over the returned collection.
            Iterable<Summary> result = new Iterable<Summary>() {
                public Iterator iterator() {
                    final ListIterator<Summary> summaryListIterator = summaries.listIterator();
                    return new Iterator() {
                        private Runnable restoreDynamicColumnVariables;

                        private void undeclareLatestDynamicColumnVariables() {
                            if (restoreDynamicColumnVariables != null) {
                                restoreDynamicColumnVariables.run();
                                restoreDynamicColumnVariables = null;
                            }
                        }

                        public boolean hasNext() {
                            boolean hasNext = summaryListIterator.hasNext();
                            if (!hasNext) {
                                undeclareLatestDynamicColumnVariables();
                            }
                            return hasNext;
                        }

                        public Summary next() {
                            if (!hasNext())
                                throw new NoSuchElementException();
                            undeclareLatestDynamicColumnVariables();

                            int nextIndex = summaryListIterator.nextIndex();
                            UIComponent summaryOwner = summaryOwners.get(nextIndex);
                            if (summaryOwner instanceof DynamicColumn) {
                                DynamicColumn dynamicColumn = (DynamicColumn) summaryOwner;
                                restoreDynamicColumnVariables = dynamicColumn.enterComponentContext();
                            }
                            return summaryListIterator.next();
                        }

                        public void remove() {
                            throw new UnsupportedOperationException();
                        }
                    };
                }
            };

            int rowIndex = getRowIndex();
            if (rowIndex != -1) setRowIndex(-1);
            FacesContext facesContext = getFacesContext();
            for (Summary summary : result) {
                summary.prepare(facesContext);
            }
            if (rowIndex != -1) setRowIndex(rowIndex);
            this.summaries = result;
        }
        return summaries;
    }

    private Sorting sorting;

    public Sorting getSorting() {
        if (sorting == null) {
            sorting = Components.findChildWithClass(this, Sorting.class, "<o:sorting>");
            if (sorting == null) {
                sorting = new Sorting(false);
            }
        }
        return sorting;
    }

    protected void rememberSelectionByKeys() {
        AbstractTableSelection selection = getSelection();
        if (selection != null)
            selection.rememberByKeys();

        List<BaseColumn> columns = getAllColumns();
        for (BaseColumn column : columns) {
            if (column instanceof CheckboxColumn) {
                ((CheckboxColumn) column).rememberByKeys();
            }
        }

    }

    @Override
    public void processDecodes(FacesContext context) {
        invokeBeforeProcessDecodes(context);
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
        List<Filter> filters = getFilters();
        for (Filter filter : filters) {
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

        List<Filter> filters = getFilters();
        for (Filter filter : filters) {
            filter.processUpdates(context);
        }

        ValueExpression displayedRowDatasExpression = getValueExpression("displayedRowDatas");
        if (displayedRowDatasExpression != null)
            ValueBindings.setFromList(this, "displayedRowDatas", getDisplayedRowDatas());

        ColumnResizing columnResizing = getColumnResizing();
        if (columnResizing != null)
            columnResizing.processUpdates(context);

        Scrolling scrolling = getScrolling();
        if (scrolling != null)
            scrolling.processUpdates(context);

        Iterable<String> submittedColumnsOrder = (Iterable<String>) getAttributes().get("submittedColumnsOrder");
        if (submittedColumnsOrder != null) {
            getAttributes().remove("submittedColumnsOrder");
            setColumnsOrder(submittedColumnsOrder);
            if (columnsOrder != null && ValueBindings.set(this, "columnsOrder", columnsOrder)) {
                columnsOrder = null;
            }
        }
    }

    protected void processModelUpdates(FacesContext context) {
        beforeUpdateValuesPhase = false;
        if (incomingSortingRules != null) {
            acceptNewSortingRules(incomingSortingRules);
            incomingSortingRules = null;
            TableUtil.markSortingToggledInThisRequest(context);
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
        Components.fixImplicitPanelIdsForMojarra_2_0_3(this, true);

        cachedAllColumns = null;
        cachedColumnsByIds = null;
        cachedRenderedColumns = null;
        resetColumns(this);

        // Check for columns inside columnGroup OFCS-74
        List<ColumnGroup> columnGroupComponents = Components.findChildrenWithClass(this, ColumnGroup.class);
        for (ColumnGroup group : columnGroupComponents) {
            resetColumns(group);
        }

        ensureImplicitFacetsCreated();

        getAttributes().put(TableStructure.CUSTOM_ROW_RENDERING_INFOS_KEY, new HashMap());


        List<Filter> filters = getFilters();
        for (Filter filter : filters) {
            filter.updateValueFromBinding(context);
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

        validateSortingGroupingColumns();
        // ensure facets has own ids in calls to these methods
        getAbove();
        getBelow();


        updateModel();
        totalRowCount = getModel().getTotalRowCount();
    }

    private void resetColumns(UIComponent component) {
        List<Columns> columnsComponents = Components.findChildrenWithClass(component, Columns.class);
        for (Columns columns : columnsComponents) {
            columns.resetCachedColumnList();
        }
    }

    protected void updateModel() {

    }

    private void remindStateSavingOfTableData() {
        // modify uiDataValue property to move it to the delta map in StateHolder and clear "initial state" flag, which
        // turns on state saving for TableDataModel
        getStateHelper().put(PropertyKeys.uiDataValue, getUiDataValue());
        clearInitialState();
    }

    protected void validateSortingGroupingColumns() {
        List<SortingRule> sortingRules = getSortingRules();
        validateSortingOrGroupingRules(sortingRules);
    }

    protected <E extends SortingOrGroupingRule> void validateSortingOrGroupingRules(List<E> rules) {
        validateSortingOrGroupingRules(rules, false);
    }

    protected <E extends SortingOrGroupingRule> void validateSortingOrGroupingRules(
            List<E> rules,
            boolean exceptionOnInvalidColumns) {
        if (rules == null || rules.size() == 0)
            return;
        for (Iterator<? extends SortingOrGroupingRule> iterator = rules.iterator(); iterator.hasNext(); ) {
            SortingOrGroupingRule rule = iterator.next();
            String columnId = rule.getColumnId();
            if (getColumnById(columnId) == null) {
                if (exceptionOnInvalidColumns)
                    throw new IllegalArgumentException("Error checking incoming sorting or grouping rules. " +
                            "There is no column with this id: \"" + columnId + "\"");
                iterator.remove();
                break;
            }
        }
    }

    public BaseColumn getColumnById(String columnId) {
        if (cachedColumnsByIds == null) cachedColumnsByIds = new HashMap<String, BaseColumn>();

        BaseColumn result = cachedColumnsByIds.get(columnId);
        if (result == null) {
            List<BaseColumn> allColumns = getAllColumns();
            for (BaseColumn column : allColumns) {
                String thisColId = column.getId();
                if (thisColId.equals(columnId)) {
                    result = column;
                    break;
                }
            }
        }
        return result;
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
            rule.setColumnId(sortColumnId);
        } else
            sortingRules = null;
        setSortingRules(sortingRules);
    }

    public int getSortColumnIndex() {
        String sortColumnId = getSortColumnId();
        List<BaseColumn> columns = getRenderedColumns();
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

    public String getSubHeaderRowStyle() {
        return ValueBindings.get(this, "subHeaderRowStyle", subHeaderRowStyle);
    }

    public void setSubHeaderRowStyle(String subHeaderRowStyle) {
        this.subHeaderRowStyle = subHeaderRowStyle;
    }

    public String getSubHeaderRowClass() {
        return ValueBindings.get(this, "subHeaderRowClass", subHeaderRowClass);
    }

    public void setSubHeaderRowClass(String subHeaderRowClass) {
        this.subHeaderRowClass = subHeaderRowClass;
    }

    public String getSubHeaderRowSeparator() {
        return ValueBindings.get(this, "subHeaderRowSeparator", subHeaderRowSeparator);
    }

    public void setSubHeaderRowSeparator(String subHeaderRowSeparator) {
        this.subHeaderRowSeparator = subHeaderRowSeparator;
    }

    public String getAllRecordsFilterText() {
        return ValueBindings.get(this, "allRecordsFilterText", allRecordsFilterText);
    }

    public void setAllRecordsFilterText(String allRecordsFilterText) {
        this.allRecordsFilterText = allRecordsFilterText;
    }

    public String getEmptyRecordsFilterText() {
        return ValueBindings.get(this, "emptyRecordsFilterText", emptyRecordsFilterText);
    }

    public void setEmptyRecordsFilterText(String emptyRecordsFilterText) {
        this.emptyRecordsFilterText = emptyRecordsFilterText;
    }

    public String getNonEmptyRecordsFilterText() {
        return ValueBindings.get(this, "nonEmptyRecordsFilterText", nonEmptyRecordsFilterText);
    }

    public void setNonEmptyRecordsFilterText(String nonEmptyRecordsFilterText) {
        this.nonEmptyRecordsFilterText = nonEmptyRecordsFilterText;
    }

    public String getSortColumnId() {
        List<SortingRule> sortingRules = getSortingRules();
        if (sortingRules == null)
            return null;
        if (sortingRules.size() == 0)
            return null;
        SortingRule sortingRule = sortingRules.get(0);
        return sortingRule.getColumnId();
    }

    public boolean isSortAscending() {
        List<SortingRule> sortingRules = getSortingRules();
        if (sortingRules == null)
            return true;
        if (sortingRules.size() == 0)
            return true;
        SortingRule sortingRule = sortingRules.get(0);
        return sortingRule.isAscending();
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
        rule.setAscending(value);
        setSortingRules(sortingRules);
    }


    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public List<SortingRule> getSortingRules() {
        return sortingRules;
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public void setSortingRules(List<SortingRule> sortingRules) {
        this.sortingRules = sortingRules;
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public void acceptNewSortingRules(List<SortingRule> sortingRules) {
        rememberSelectionByKeys();
        if (beforeUpdateValuesPhase) {
            incomingSortingRules = sortingRules;
            return;
        }
        for (SortingRule sortingRule : sortingRules) {
            String columnId = sortingRule.getColumnId();
            BaseColumn column = getColumnById(columnId);
            if (column == null) throw new IllegalArgumentException("Column by id not found: " + columnId);
            if (!(column instanceof Column || column instanceof SelectionColumn || column instanceof CheckboxColumn))
                throw new IllegalArgumentException("Column (id = " + columnId + ") is not sortable. Column class is " + column.getClass());
        }
        setSortingRules(sortingRules);
    }

    public Object getRowKey() {
        return getModel().getRowKey();
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public String getClientRowKey() {
        Object rowKey = getRowKey();
        if (rowKey == null) return null;
        return rowKey.toString();
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public Comparator<Object> createRowDataComparator(
            List<GroupingRule> groupingRules,
            List<SortingRule> sortingRules) {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        List<SortingOrGroupingRule> rules = new ArrayList<SortingOrGroupingRule>();
        if (groupingRules != null) rules.addAll(groupingRules);
        if (sortingRules != null) rules.addAll(sortingRules);
        if (rules.size() == 0)
            return null;

        final List<Comparator<Object>> comparators = new ArrayList<Comparator<Object>>();

        for (SortingOrGroupingRule rule : rules) {
            Comparator<Object> ruleComparator = createRuleComparator(facesContext, rule);
            if (ruleComparator != null)
                comparators.add(ruleComparator);
        }

        return new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                for (Comparator<Object> comparator : comparators) {
                    int result = comparator.compare(o1, o2);
                    if (result != 0)
                        return result;
                }
                return 0;
            }
        };
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public RowComparator createRuleComparator(final FacesContext facesContext, SortingOrGroupingRule rule) {
        String columnId = rule.getColumnId();
        if (columnId == null)
            return null;
        List<BaseColumn> allColumns = getAllColumns();
        BaseColumn column = findColumnById(allColumns, columnId);
        boolean ordinaryColumn = column instanceof Column;
        if (column == null ||
                !(ordinaryColumn || column instanceof CheckboxColumn || column instanceof SelectionColumn))
            return null;
        ValueExpression sortingExpression;
        if (ordinaryColumn) {
            Column tableColumn = (Column) column;
            if (rule instanceof GroupingRule)
                sortingExpression = tableColumn.getColumnGroupingExpression();
            else
                sortingExpression = null;
            if (sortingExpression == null)
                sortingExpression = tableColumn.getColumnSortingExpression();
        } else {
            sortingExpression = itemSelectedExpressionForColumn(column);
        }
        if (sortingExpression == null)
            return null;

        ValueExpression sortingComparatorBinding = ordinaryColumn ? ((Column) column).getSortingComparatorExpression() : null;
        Comparator<Object> comparator = sortingComparatorBinding != null
                ? (Comparator<Object>) sortingComparatorBinding.getValue(facesContext.getELContext())
                : null;

        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        boolean sortAscending = rule.isAscending();
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
        return new RowComparator(this, facesContext, sortingExpression, valueComparator, requestMap, sortAscending);
    }

    protected List<Filter> getActiveFilters() {
        List<Filter> filters = new ArrayList<Filter>();

        List<Filter> allFilters = getFilters();
        for (Filter filter : allFilters) {
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
        Components.runScheduledActions();
    }

    public List<Filter> getFilters() {
        return myFilters;
    }


    public Object getFilteredValueByData(
            FacesContext context,
            Object data,
            Object expression
    ) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        String var = getVar();
        Object rowData = setupDataRetrievalContext(data, requestMap, var);
        Object result = expression instanceof ValueExpression
                ? ((ValueExpression) expression).getValue(context.getELContext())
                : ReflectionUtil.readProperty(rowData, (String) expression);
        return result;
    }

    protected Object setupDataRetrievalContext(Object data, Map<String, Object> requestMap, String var) {
        requestMap.get(var);
        requestMap.put(var, data);
        return data;
    }

    public void filterChanged(Filter filter) {
    }

    public int getAutoFilterDelay() {
        return ValueBindings.get(this, "autoFilterDelay", autoFilterDelay, 700);
    }

    public void setAutoFilterDelay(int autoFilterDelay) {
        this.autoFilterDelay = autoFilterDelay;
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
        return Components.getFacet(this, "noDataMessage");
    }

    public UIComponent getNoFilterDataMessage() {
        return Components.getFacet(this, "noFilterDataMessage");
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

    public Boolean getKeepSelectionVisible() {
        return ValueBindings.get(this, "keepSelectionVisible", keepSelectionVisible, false);
    }

    public void setKeepSelectionVisible(Boolean keepSelectionVisible) {
        this.keepSelectionVisible = keepSelectionVisible;
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
            List<UIComponent> columnChildren = !(column instanceof SyntheticColumn)
                    ? column.getChildren() : ((SyntheticColumn) column).getChildrenForProcessing();
            for (UIComponent columnChild : columnChildren) {
                if (decodingRequiringComponent(columnChild) != null)
                    return true;
            }
        }

        List<Row> customRows = getCustomRows();
        for (Row customRow : customRows) {
            List<UIComponent> rowChildren = customRow.getChildren();
            for (UIComponent rowChild : rowChildren) {
                if (!(rowChild instanceof Cell))
                    continue;
                List<UIComponent> cellChildren = rowChild.getChildren();
                for (UIComponent cellChild : cellChildren) {
                    if (decodingRequiringComponent(cellChild) != null)
                        return true;
                }
            }
        }

        return false;
    }

    private UIComponent decodingRequiringComponent(UIComponent component) {
        boolean thisComponentRequiresDecoding = component instanceof EditableValueHolder || component instanceof ActionSource;
        if (thisComponentRequiresDecoding)
            return component;
        List<UIComponent> children = component.getChildren();
        for (UIComponent subComponent : children) {
            UIComponent decodable = decodingRequiringComponent(subComponent);
            if (decodable != null)
                return decodable;
        }
        return null;
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public int getRowIndexByClientSuffix(String id) {
        return Integer.parseInt(id);
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public boolean getDeferBodyLoading() {
        return ValueBindings.get(this, "deferBodyLoading", deferBodyLoading, false);
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public void setDeferBodyLoading(boolean deferBodyLoading) {
        this.deferBodyLoading = deferBodyLoading;
    }

    protected abstract void restoreRows(boolean forceDecoding);

    /**
     * @return null means that no table rows are organized as a plain list and not a hierarchy currently
     */
    public Map<Object, ? extends NodeInfo> getTreeStructureMap(FacesContext context) {
        return null;
    }

    public int getNodeLevel() {
        return 0;
    }

    public boolean getNodeHasChildren() {
        return false;
    }

    public String getTextStyle() {
        return null;
    }

    public String getTextClass() {
        return null;
    }

    public boolean isNodeExpanded() {
        return true;
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public List<BaseColumn> getAdaptedRenderedColumns() {
        return getRenderedColumns();
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public void acceptNewExpandedRowIndexes(Set indexes) {
        FacesContext context = getFacesContext();
        boolean dontCollapseNodes = isReloadingThisComponentWithA4j() || TreeTableRenderer.isAjaxFoldingInProgress(context);
        List storedRowKeys = getModel().getStoredRowKeys();
        int rowCount = storedRowKeys.size();
        for (int i = 0; i < rowCount; i++) {
            Object storedRowKey = storedRowKeys.get(i);
            TreePath keyPath;
            if (storedRowKey instanceof TreePath) {
                // the case for TreeTable
                keyPath = (TreePath) storedRowKey;
            } else {
                // the case for DataTable where row keys are plain row data objects, or row header/row footer objects
                keyPath = new TreePath(storedRowKey, null);
            }
            boolean expanded = isNodeExpanded(keyPath);
            boolean shouldBeExpanded = indexes.contains(Integer.valueOf(i));
            if (expanded == shouldBeExpanded)
                continue;
            if (!shouldBeExpanded && dontCollapseNodes)
                continue;
            if (!getNodeHasChildren(i)) { // rows without children should have expanded state by default
                setNodeExpanded(keyPath, true);
            } else {
                setNodeExpanded(keyPath, shouldBeExpanded);
            }
        }
    }

    private boolean isReloadingThisComponentWithA4j() {
        // needed for JSFC-2526. It doesn't seem possible to know the a4j rerendered components on the decoding stage,
        // so we suppose that this component is rerendered if it is an A4j request.
        return Environment.isAjax4jsfRequest();
    }

    protected abstract boolean getNodeHasChildren(int rowIndex);

    protected abstract boolean isNodeExpanded(TreePath keyPath);

    protected abstract void setNodeExpanded(TreePath keyPath, boolean expanded);

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public Runnable populateSortingExpressionParams(final String var, final Map<String, Object> requestMap, Object collectionObject) {
        final Object prevValue = requestMap.put(var, collectionObject);
        return new Runnable() {
            public void run() {
                requestMap.put(var, prevValue);
            }
        };
    }

    @Override
    public String getClientId(FacesContext context) {
        String clientId = getStandardClientId(context);
        int index = getRowIndex();
        String suffix = index != -1 ? getRowClientSuffixByIndex(index) : null;
        if (suffix == null)
            return clientId;
        else
            return clientId + UINamingContainer.getSeparatorChar(context) + suffix;
    }

    protected String getRowClientSuffixByIndex(int index) {
        return String.valueOf(index);
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

        String parentId = "";
        if (namingContainer != null) {
            String containerClientId = namingContainer.getContainerClientId(context);
            if (containerClientId != null)
                parentId = containerClientId + UINamingContainer.getSeparatorChar(context);
        }
        String clientId = parentId + id;

        Renderer renderer = getRenderer(context);
        if (renderer != null)
            clientId = renderer.convertClientId(context, clientId);

        cachedClientId = clientId;
        return clientId;
    }

    /**
     * @return the total number of rows on all table's pages (if pagination if used) according to the current filtering
     *         parameters
     */
    public int getRowCount(DataScope scope) {
        switch (scope) {
            case DISPLAYED_ROWS:
                return getDataModel().getRowCount();
            case FILTERED_ROWS:
                if (totalRowCount == null)
                    totalRowCount = getModel().getTotalRowCount();
                return totalRowCount;
            default:
                throw new IllegalStateException("Unknown scope: " + scope);
        }
    }

    public Collection<Object> getRowDatas(DataScope scope) {
        if (getRowCount() == 0 && !isRowsDecodingRequired()) {
            restoreRows(true);
        }

        switch (scope) {
            case DISPLAYED_ROWS:
                return getDisplayedRowDatas();
            case FILTERED_ROWS:
                return getFilteredRowDatas();
            default:
                throw new IllegalStateException("Unknown scope: " + scope);
        }

    }

    private Collection<Object> getFilteredRowDatas() {
        TableDataModel model = getModel();
        int savedPageIndex = model.getPageIndex();
        int savedRowIndex = model.getRowIndex();
        List<Object> allRows = new ArrayList<Object>();
        try {
            for (int pageIndex = 0, pageCount = model.getPageCount(); pageIndex < pageCount; pageIndex++) {
                model.setPageIndex(pageIndex);
                for (int rowIndex = 0, rowCount = model.getRowCount(); rowIndex < rowCount || rowCount == -1; rowIndex++) {
                    model.setRowIndex(rowIndex);
                    if (!model.isRowAvailable()) break;
                    Object rowData = model.getRowData();
                    allRows.add(rowData);
                }
            }
        } finally {
            model.setPageIndex(savedPageIndex);
            model.setRowIndex(savedRowIndex);
        }
        return allRows;
    }

    public Collection<String> getExternalPartIds() {
        FacesContext context = getFacesContext();
        List<String> result = new ArrayList<String>();
        addExternalPartIdFromComponent(context, result, getAbove());
        addExternalPartIdFromComponent(context, result, getBelow());
        return result;
    }

    private void addExternalPartIdFromComponent(FacesContext context, List<String> ids, UIComponent component) {
        if (component == null) return;

        if (componentIdSpecified(component)) {
            ids.add(component.getClientId(context));
            return;
        }
        for (UIComponent child : component.getChildren()) {
            addExternalPartIdFromComponent(context, ids, child);
        }
    }

    private boolean componentIdSpecified(UIComponent component) {
        String id = component.getId();
        return id != null && !id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX);
    }

    private void ensureFacetHasOwnId(UIComponent facet, String facetName) {
        if (facet == null) return;
        String id = facet.getId();
        if (id == null || id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX)) {
            Log.log("OpenFaces warning: component " + facet.getClass().getName() + " in \"" + facetName + "\" facet " +
                    "of component with id \"" + getClientId(getFacesContext()) + "\" doesn't have its id attribute specified. " +
                    "You might need to specify id for a component inside of this facet if it isn't updated properly " +
                    "during Ajax actions such as pagination or others. See the \"Specifying the content of the above and below facets\" " +
                    "documentation section for details: http://openfaces.org/documentation/developersGuide/datatable.html" +
                    "#DataTable-Specifyingthecontentofthe%22above%22and%22below%22facets");
//            String facetAutoIdCounterKey = "facetAutoIdCounter";
//            Integer counter = (Integer) getStateHelper().get(facetAutoIdCounterKey);
//            if (counter == null)
//                counter = 0;
//            counter++;
//            getStateHelper().put(facetAutoIdCounterKey, counter);
//            facet.setId(getId() + Rendering.SERVER_ID_SUFFIX_SEPARATOR + "facetAutoId_" + counter);
        }
    }

    /**
     * Returns the list of row data objects, which corresponds to all of the currently displayed records. That is, for
     * the DataTable, which is currently filtered, this method will return only the objects that match the current
     * filtering criteria, and if pagination is used, then only the current page objects are returned.
     */
    public List<Object> getDisplayedRowDatas() {
        if (getRowCount() == 0 && !isRowsDecodingRequired()) {
            restoreRows(true);
        }
        List<Object> rowDatas = new ArrayList<Object>();
        int oldRowIndex = getRowIndex();
        try {
            int rowCount = getRowCount();
            int rows = (rowCount != -1) ? rowCount : Integer.MAX_VALUE; // getRowCount can return -1, which means that iteration should be performed until isRowAvailable returns false
            for (int rowIndex = getFirst(); rowIndex < rows; rowIndex++) {
                setRowIndex(rowIndex);
                if (!isRowAvailable())
                    break;
                Object rowData = getRowData();
                rowDatas.add(rowData);
            }
        } finally {
            setRowIndex(oldRowIndex);
        }

        return rowDatas;
    }

    public void export(DataScope scope, TableDataFormatter formatter) {
        String fileName = getId();
        if (fileName == null)
            fileName = "tableData";
        export(scope, formatter, fileName + "." + formatter.getDefaultFileExtension());
    }

    protected void checkExportSupportedInCurrentState() {
        // Export is allowed in all states by default. Subclasses can define their own state checks.
    }

    public void export(DataScope scope, TableDataFormatter formatter, String fileName) {
        TableDataExtractor extractor = new TableDataExtractor(scope);
        export(scope, extractor, formatter, fileName);
    }

    public void export(DataScope scope, TableDataExtractor extractor, TableDataFormatter formatter, String fileName) {
        checkExportSupportedInCurrentState();
        FacesContext context = FacesContext.getCurrentInstance();
        TableData tableData = extractor.extract(this);
        formatter.sendResponse(context, tableData, fileName);
    }


    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     * <p/>
     * This method is required for BaseColumn.getExpressionData to be able to detect column type during model
     * construction, when the of displayed rows is still in progress (row data objects have been retrieved but not
     * grouped yet). The BaseColumn.getExpressionData method relies on row variables such as row data and row index
     * variables to be set for proper type detection, and hence we should provide arbitrary values of the proper
     * actual user-specified type. So this method just takes the first retrieved row data (from the intermediate, yet
     * ungrouped, list) for populating the variable(s).
     */
    public Runnable populateRowVariablesWithAnyModelValue() {
        List<TableDataModel.RowInfo> allRetrievedRows = getModel().getAllRetrievedRows();
        int retrievedRowCount = allRetrievedRows != null ? allRetrievedRows.size() : 0;
        if (retrievedRowCount == 0) return null;
        TableDataModel.RowInfo rowInfo;
        int i = 0;
        do {
            rowInfo = allRetrievedRows.get(i++);
        } while ((rowInfo == null || rowInfo.getRowData() == null) && i < retrievedRowCount);
        if (rowInfo == null || rowInfo.getRowData() == null) return null;
        Object rowData = rowInfo.getRowData();
        Components.setRequestVariable(getVar(), rowData);

        return new Runnable() {
            public void run() {
                restoreRowVariables();
            }
        };
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     * <p/>
     * Must be invoked after the populateRowVariablesWithAnyModelValue call. Restores original row variables as they
     * were before the preceding populateRowVariablesWithAnyModelValue call.
     */
    public void restoreRowVariables() {
        Components.restoreRequestVariable(getVar());
    }


    public boolean getUnsortedStateAllowed() {
        return ValueBindings.get(this, "unsortedStateAllowed", unsortedStateAllowed, false);
    }

    public void setUnsortedStateAllowed(boolean unsortedStateAllowed) {
        this.unsortedStateAllowed = unsortedStateAllowed;
    }

    public Boolean getRowsDecodingRequired() {
        return rowsDecodingRequired;
    }

    public void setRowsDecodingRequired(Boolean rowsDecodingRequired) {
        this.rowsDecodingRequired = rowsDecodingRequired;
    }

    public Integer getTotalRowCount() {
        return totalRowCount;
    }

    public void setTotalRowCount(Integer totalRowCount) {
        this.totalRowCount = totalRowCount;
    }

    public String getOnbeforeajaxreload() {
        return ValueBindings.get(this, "onbeforeajaxreload", onbeforeajaxreload);
    }

    public void setOnbeforeajaxreload(String onbeforeajaxreload) {
        this.onbeforeajaxreload = onbeforeajaxreload;
    }

    public String getOnafterajaxreload() {
        return ValueBindings.get(this, "onafterajaxreload", onafterajaxreload);
    }

    public void setOnafterajaxreload(String onafterajaxreload) {
        this.onafterajaxreload = onafterajaxreload;
    }

    public Boolean getUnDisplayedSelectionAllowed() {
        return ValueBindings.get(this, "unDisplayedSelectionAllowed", unDisplayedSelectionAllowed, true);
    }

    public void setUnDisplayedSelectionAllowed(Boolean unDisplayedSelectionAllowed) {
        this.unDisplayedSelectionAllowed = unDisplayedSelectionAllowed;
    }
}
