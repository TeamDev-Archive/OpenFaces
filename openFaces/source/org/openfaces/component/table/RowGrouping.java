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

import org.openfaces.component.ComponentConfigurator;
import org.openfaces.component.OUIComponentBase;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.ValueBindings;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.PhaseId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RowGrouping extends OUIComponentBase implements ComponentConfigurator {
    public static final String COMPONENT_TYPE = "org.openfaces.RowGrouping";
    public static final String COMPONENT_FAMILY = "org.openfaces.RowGrouping";
    private static final String DEFAULT_GROUP_HEADER_TEXT_EXPRESSION = "#{columnHeader}: #{groupingValueString}";
    private static final String GROUP_HEADER_TEXT_ATTRIBUTE = "groupHeaderText";

    private List<GroupingRule> groupingRules;
    private String columnHeaderVar = "columnHeader";
    private String groupingValueVar = "groupingValue";
    private String groupingValueStringVar = "groupingValueString";
    private Boolean groupOnHeaderClick;
    private Boolean hideGroupingColumns;
    private ExpansionState expansionState = new AllNodesExpanded();
    private RowGroupingSelectionMode selectionMode;

    private boolean beforeUpdateValuesPhase = true;
    private DataTable dataTable;
    private List<GroupingRule> incomingGroupingRules;
    private String groupHeaderRowStyle;
    private String groupHeaderRowClass;
    private String groupFooterRowStyle;
    private String groupFooterRowClass;
    private String inGroupHeaderRowStyle;
    private String inGroupHeaderRowClass;
    private String inGroupFooterRowStyle;
    private String inGroupFooterRowClass;
    private Boolean inGroupFootersCollapsible;
    private Boolean groupFootersCollapsible;

    public RowGrouping() {
        setRendererType("org.openfaces.RowGroupingRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                saveAttachedState(context, groupingRules),
                columnHeaderVar,
                groupingValueVar,
                groupingValueStringVar,
                groupOnHeaderClick,
                hideGroupingColumns,
                expansionState,
                selectionMode,
                inGroupFootersCollapsible,
                groupFootersCollapsible,

                groupHeaderRowStyle,
                groupHeaderRowClass,
                groupFooterRowStyle,
                groupFooterRowClass
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        groupingRules = (List<GroupingRule>) restoreAttachedState(context, state[i++]);
        columnHeaderVar = (String) state[i++];
        groupingValueVar = (String) state[i++];
        groupingValueStringVar = (String) state[i++];
        groupOnHeaderClick = (Boolean) state[i++];
        hideGroupingColumns = (Boolean) state[i++];
        expansionState = (ExpansionState) state[i++];
        selectionMode = (RowGroupingSelectionMode) state[i++];
        inGroupFootersCollapsible = (Boolean) state[i++];
        groupFootersCollapsible = (Boolean) state[i++];

        groupHeaderRowStyle = (String) state[i++];
        groupHeaderRowClass = (String) state[i++];
        groupFooterRowStyle = (String) state[i++];
        groupFooterRowClass = (String) state[i++];

        beforeUpdateValuesPhase = true;
        incomingGroupingRules = null;
    }

    @Override
    public void processUpdates(FacesContext context) {
        beforeUpdateValuesPhase = false;
        super.processUpdates(context);

        if (incomingGroupingRules != null) {
            acceptNewGroupingRules(incomingGroupingRules);
            incomingGroupingRules = null;
        }
        if (groupingRules != null && ValueBindings.set(this, "groupingRules", groupingRules))
            groupingRules = null;

        ValueExpression expansionStateExpression = getValueExpression("expansionState");
        if (expansionStateExpression != null)
            expansionStateExpression.setValue(context.getELContext(), expansionState);
    }

    public void acceptNewGroupingRules(List<GroupingRule> groupingRules) {
        if (beforeUpdateValuesPhase) {
            incomingGroupingRules = groupingRules;
            return;
        }
        DataTable dataTable = getDataTable();
        for (GroupingRule sortingRule : groupingRules) {
            String columnId = sortingRule.getColumnId();
            BaseColumn column = dataTable.getColumnById(columnId);
            if (column == null) throw new IllegalArgumentException("Column by id not found: " + columnId);
            if (!column.isColumnGroupable())
                throw new IllegalArgumentException("Column (id = " + columnId + ") is not groupable. Column class is " +
                        column.getClass() + " ; specify sortingExpression or groupingExpression for <o:column> to " +
                        "make it groupable");
        }

        setGroupingRules(groupingRules);
    }

    public List<GroupingRule> getGroupingRules() {
        return ValueBindings.get(this, "groupingRules", groupingRules, Collections.emptyList(), List.class);
    }

    public void setGroupingRules(List<GroupingRule> groupingRules) {
        if (groupingRules == null)
            throw new IllegalArgumentException("groupingRules cannot be null");
        this.groupingRules = groupingRules;
    }

    public String getColumnHeaderVar() {
        return columnHeaderVar;
    }

    public void setColumnHeaderVar(String columnHeaderVar) {
        this.columnHeaderVar = columnHeaderVar;
    }

    public String getGroupingValueVar() {
        return groupingValueVar;
    }

    public void setGroupingValueVar(String groupingValueVar) {
        this.groupingValueVar = groupingValueVar;
    }

    public String getGroupingValueStringVar() {
        return groupingValueStringVar;
    }

    public void setGroupingValueStringVar(String groupingValueStringVar) {
        this.groupingValueStringVar = groupingValueStringVar;
    }

    public ValueExpression getGroupHeaderTextExpression() {
        ValueExpression expression = getValueExpression("groupHeaderText");
        if (expression == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            ELContext elContext = context.getELContext();
            expression = context.getApplication().getExpressionFactory().createValueExpression(
                    elContext, DEFAULT_GROUP_HEADER_TEXT_EXPRESSION, Object.class);
            setGroupHeaderTextExpression(expression);
        }
        return expression;
    }

    public void setGroupHeaderTextExpression(ValueExpression expression) {
        setValueExpression(GROUP_HEADER_TEXT_ATTRIBUTE, expression);
    }

    protected DataTable getDataTable() {
        if (dataTable == null) {
            dataTable = (DataTable) Components.checkParentTag(this, DataTable.class);
        }
        return dataTable;
    }

    private boolean _evaluatingConverterInsideSetupCurrentRowVariables;

    public void setupCurrentRowVariables() {
        PhaseId currentPhaseId = FacesContext.getCurrentInstance().getCurrentPhaseId();
        if (currentPhaseId == PhaseId.RESTORE_VIEW) {
            // no grouping-related variables are required on the restore view phase, and an attempt to calculate
            // them during this stage can fail because no data model has been initialized yet
            return;
        }
        if (_evaluatingConverterInsideSetupCurrentRowVariables) {
            // Prevent endless recursion in the column.getGroupingValueConverter() call (see below).
            // The grouping-related variables are not required during calculation of groupingValueConverter,
            // so we can just skip this method if _evaluatingConverterInsideSetupCurrentRowVariables == true
            return;
        }
        DataTable dataTable = getDataTable();
        Object rowData = dataTable.isRowAvailable() ? dataTable.getRowData() : null;
        if (!(rowData instanceof GroupHeaderOrFooter)) {
            Components.setRequestVariable(getColumnHeaderVar(), null);
            Components.setRequestVariable(getGroupingValueVar(), null);
            Components.setRequestVariable(getGroupingValueStringVar(), null);
            return;
        }

        GroupHeaderOrFooter row = (GroupHeaderOrFooter) rowData;
        RowGroup rowGroup = row.getRowGroup();

        String columnId = rowGroup.getColumnId();
        Column column = (Column) dataTable.getColumnById(columnId);

        String columnHeader = column.getColumnHeader();
        Components.setRequestVariable(getColumnHeaderVar(), columnHeader);

        Object groupingValue = rowGroup.getGroupingValue();
        Components.setRequestVariable(getGroupingValueVar(), groupingValue);

        Converter groupingValueConverter;
        _evaluatingConverterInsideSetupCurrentRowVariables = true;
        // The upcoming getGroupingValueConverter call can invoke table's setRowIndex, which will result
        // re-entering this setupCurrentRowVariables method, so we should prevent endless recursion here.
        try {
            groupingValueConverter = column.getGroupingValueConverter();
        } finally {
            _evaluatingConverterInsideSetupCurrentRowVariables = false;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        if (groupingValueConverter == null) {
            groupingValueConverter = groupingValue != null
                    ? Rendering.getConverterForType(context, groupingValue.getClass()) : null;
        }
        String groupingValueStr = groupingValueConverter != null ?
                groupingValueConverter.getAsString(context, column, groupingValue) : groupingValue != null ?
                groupingValue.toString() :
                "";
        Components.setRequestVariable(getGroupingValueStringVar(), groupingValueStr);
    }

    public boolean getGroupOnHeaderClick() {
        return ValueBindings.get(this, "groupOnHeaderClick", groupOnHeaderClick, false);
    }

    public void setGroupOnHeaderClick(boolean groupOnHeaderClick) {
        this.groupOnHeaderClick = groupOnHeaderClick;
    }

    public boolean getHideGroupingColumns() {
        return ValueBindings.get(this, "hideGroupingColumns", hideGroupingColumns, true);
    }

    public void setHideGroupingColumns(boolean hideGroupingColumns) {
        this.hideGroupingColumns = hideGroupingColumns;
    }

    protected boolean isNodeExpanded(TreePath keyPath) {
        if (keyPath == null)
            return false;
        return expansionState.isNodeExpanded(keyPath);
    }

    protected void setNodeExpanded(TreePath keyPath, boolean expanded) {
        boolean oldExpansion = isNodeExpanded(keyPath);
        if (expanded == oldExpansion)
            return;
        expansionState = expansionState.getMutableExpansionState();
        expansionState.setNodeExpanded(keyPath, expanded);
    }

    protected void beforeEncode() {
        ValueExpression expansionStateExpression = getValueExpression("expansionState");
        if (expansionStateExpression != null) {
            FacesContext context = getFacesContext();
            ExpansionState newExpansionState = (ExpansionState) expansionStateExpression.getValue(context.getELContext());
            if (newExpansionState != null)
                setExpansionState(newExpansionState);
        }
    }

    public ExpansionState getExpansionState() {
        return expansionState;
    }

    public void setExpansionState(ExpansionState expansionState) {
        this.expansionState = expansionState;
    }

    public RowGroupingSelectionMode getSelectionMode() {
        return ValueBindings.get(this, "selectionMode", selectionMode, RowGroupingSelectionMode.DATA_ROWS, RowGroupingSelectionMode.class);
    }

    public void setSelectionMode(RowGroupingSelectionMode selectionMode) {
        this.selectionMode = selectionMode;
    }

    public UIComponent getConfiguredComponent() {
        return getDataTable();
    }

    public String getGroupHeaderRowStyle() {
        return ValueBindings.get(this, "groupHeaderRowStyle", groupHeaderRowStyle);
    }

    public void setGroupHeaderRowStyle(String groupHeaderRowStyle) {
        this.groupHeaderRowStyle = groupHeaderRowStyle;
    }

    public String getGroupHeaderRowClass() {
        return ValueBindings.get(this, "groupHeaderRowClass", groupHeaderRowClass);
    }

    public void setGroupHeaderRowClass(String groupHeaderRowClass) {
        this.groupHeaderRowClass = groupHeaderRowClass;
    }

    public String getGroupFooterRowStyle() {
        return ValueBindings.get(this, "groupFooterRowStyle", groupFooterRowStyle);
    }

    public void setGroupFooterRowStyle(String groupFooterRowStyle) {
        this.groupFooterRowStyle = groupFooterRowStyle;
    }

    public String getGroupFooterRowClass() {
        return ValueBindings.get(this, "groupFooterRowClass", groupFooterRowClass);
    }

    public void setGroupFooterRowClass(String groupFooterRowClass) {
        this.groupFooterRowClass = groupFooterRowClass;
    }

    public String getInGroupHeaderRowStyle() {
        return ValueBindings.get(this, "inGroupHeaderRowStyle", inGroupHeaderRowStyle);
    }

    public void setInGroupHeaderRowStyle(String inGroupHeaderRowStyle) {
        this.inGroupHeaderRowStyle = inGroupHeaderRowStyle;
    }

    public String getInGroupHeaderRowClass() {
        return ValueBindings.get(this, "inGroupHeaderRowClass", inGroupHeaderRowClass);
    }

    public void setInGroupHeaderRowClass(String inGroupHeaderRowClass) {
        this.inGroupHeaderRowClass = inGroupHeaderRowClass;
    }

    public String getInGroupFooterRowStyle() {
        return ValueBindings.get(this, "inGroupFooterRowStyle", inGroupFooterRowStyle);
    }

    public void setInGroupFooterRowStyle(String inGroupFooterRowStyle) {
        this.inGroupFooterRowStyle = inGroupFooterRowStyle;
    }

    public String getInGroupFooterRowClass() {
        return ValueBindings.get(this, "inGroupFooterRowClass", inGroupFooterRowClass);
    }

    public void setInGroupFooterRowClass(String inGroupFooterRowClass) {
        this.inGroupFooterRowClass = inGroupFooterRowClass;
    }

    public boolean getInGroupFootersCollapsible() {
        return ValueBindings.get(this, "inGroupFootersCollapsible", inGroupFootersCollapsible, true);
    }

    public void setInGroupFootersCollapsible(boolean inGroupFootersCollapsible) {
        this.inGroupFootersCollapsible = inGroupFootersCollapsible;
    }

    public boolean getGroupFootersCollapsible() {
        return ValueBindings.get(this, "groupFootersCollapsible", groupFootersCollapsible, true);
    }

    public void setGroupFootersCollapsible(boolean groupFootersCollapsible) {
        this.groupFootersCollapsible = groupFootersCollapsible;
    }
}
