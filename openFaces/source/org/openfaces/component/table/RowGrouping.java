/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.table;

import org.openfaces.component.OUIComponentBase;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.PhaseId;
import java.util.ArrayList;
import java.util.List;

public class RowGrouping extends OUIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.RowGrouping";
    public static final String COMPONENT_FAMILY = "org.openfaces.RowGrouping";
    private static final String DEFAULT_GROUP_HEADER_TEXT_EXPRESSION = "#{columnHeader}: #{groupingValueString}";
    private static final String GROUP_HEADER_TEXT_ATTRIBUTE = "groupHeaderText";

    private List<GroupingRule> groupingRules = new ArrayList<GroupingRule>();
    private String columnHeaderVar = "columnHeader";
    private String groupingValueVar = "groupingValue";
    private String groupingValueStringVar = "groupingValueString";
    private DataTable dataTable;

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
                groupingValueStringVar
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

    }

    public List<GroupingRule> getGroupingRules() {
        return groupingRules;
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
            dataTable = Components.checkParentTag(this, DataTable.class);
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
        if (!(rowData instanceof RowGroupHeaderOrFooter)) {
            Components.setRequestVariable(getColumnHeaderVar(), null);
            Components.setRequestVariable(getGroupingValueVar(), null);
            Components.setRequestVariable(getGroupingValueStringVar(), null);
            return;
        }

        RowGroupHeaderOrFooter row = (RowGroupHeaderOrFooter) rowData;
        RowGroup rowGroup = row.getRowGroup();

        String columnId = rowGroup.getColumnId();
        Column column = (Column) dataTable.getColumnById(columnId);

        String columnHeader = TableUtil.getColumnHeader(column);
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

}
