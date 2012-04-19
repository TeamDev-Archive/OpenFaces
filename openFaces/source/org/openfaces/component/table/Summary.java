/*
 * OpenFaces - JSF Component Library 2.0
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

import org.openfaces.component.OUIOutput;
import org.openfaces.component.table.impl.TableDataModel;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.ValueBindings;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Summary extends OUIOutput {
    public static final String COMPONENT_TYPE = "org.openfaces.Summary";
    public static final String COMPONENT_FAMILY = "org.openfaces.Summary";

    private DataTable table;
    private SummaryFunction function;

    public Summary() {
        setRendererType("org.openfaces.SummaryRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                function

        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        function = (SummaryFunction) state[i++];

    }

    public ValueExpression getBy() {
        return getValueExpression("by");
    }

    public void setBy(ValueExpression by) {
        setValueExpression("by", by);
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public DataTable getTable() {
        if (table == null) {
            table = Components.getParentWithClass(this, DataTable.class);
            if (table == null) throw new FacesException("The <o:summary> tag can only be used inside of " +
                    "<o:dataTable> component");
        }
        return table;
    }

    private ValueExpression getByExpression() {
        ValueExpression ve = getBy();
        if (ve != null) return ve;
        BaseColumn parentColumn = Components.getParentWithClass(this, BaseColumn.class);
        if (parentColumn == null) {
            DataTable table = getTable();
            if (table.getRowIndex() != -1) {
                // reset row index just to ensure a suffix-less table id in an exception message
                table.setRowIndex(-1);
            }
            throw new FacesException("Could not detect the summary calculation expression for " +
                    "<o:summary> component in the table " + table.getClientId(getFacesContext()) + ". " +
                    "Either the \"by\" attribute has to be specified or <o:summary> should be placed into a column's " +
                    "facet to derive the expression from that column automatically.");
        }
        ve = parentColumn.getColumnValueExpression();
        if (ve == null) {
            DataTable table = getTable();
            if (table.getRowIndex() != -1) {
                // reset row index just to ensure a suffix-less table id in an exception message
                table.setRowIndex(-1);
            }
            throw new FacesException("Could not detect the summary calculation expression for " +
                    "<o:summary> component in the table " + table.getClientId(getFacesContext()) + ". " +
                    "Neither the \"by\" attribute is specified, nor the value for the parent column could be " +
                    "detected (the column doesn't have the \"value\" attribute).");
        }
        return ve;
    }

    private Object getByValue(ELContext elContext) {
        Object value = getByExpression().getValue(elContext);
        return value;
    }


    private CalculationContext globalCalculationContext;
    private Map<RowGroup, CalculationContext> groupCalculationContexts;

    private CalculationContext getGlobalCalculationContext() {
        if (globalCalculationContext == null)
            globalCalculationContext = new CalculationContext();
        return globalCalculationContext;
    }

    private CalculationContext getGroupCalculationContext(RowGroup group) {
        if (groupCalculationContexts == null)
            groupCalculationContexts = new HashMap<RowGroup, CalculationContext>();
        CalculationContext calculationContext = groupCalculationContexts.get(group);
        if (calculationContext == null) {
            calculationContext = new CalculationContext();
            groupCalculationContexts.put(group, calculationContext);
        }
        return calculationContext;
    }


    public SummaryFunction getFunction() {
        return ValueBindings.get(this, "function", function, null);
    }

    public void setFunction(SummaryFunction function) {
        this.function = function;
    }

    private SummaryFunction getFunction(Object oneOfTheValues) {
        if (oneOfTheValues == null)
            return null;

        SummaryFunction userSpecifiedFunction = getFunction();
        if (userSpecifiedFunction != null) return userSpecifiedFunction;

        SumFunction sumFunction = new SumFunction();
        if (sumFunction.isApplicableForClass(oneOfTheValues.getClass()))
            return sumFunction;
        else
            return new CountFunction();
    }


    /**
     * The same Summary component can be rendered in different places depending on how it is declared. If it is declared
     * in the table's "header"/"footer" facet or the column's "header"/"footer" facet then the value for this Summary
     * component is calculated over all displayed rows, and it is rendered only once in the table's/column's
     * header/footer. In this case, such Summary is called a "global" one. But it can also be placed into one of the
     * grouping-related facets of the <o:column> tag, which will make the <o:summary> component to be rendered several
     * times -- once for each group (according to the facet in which it is declared). In this case, we have to maintain
     * several summary value calculator objects -- one for each RowGroup (which is identical to "one per each time it
     * is rendered"). This object contains all of the info for such single rendering instance.
     */
    private class CalculationContext {
        private SummaryFunction function;
        private SummaryFunction.Calculator calculator;
        private String renderedSummaryClientId;

        public Object getCalculator() {
            return calculator;
        }

        private void addValue(Object value) {
            if (calculator == null) {
                function = getFunction(value);
                if (function == null) return;
                calculator = function.startCalculation();
            }
            calculator.addValue(value);
        }


        public String getRenderedSummaryClientId() {
            return renderedSummaryClientId;
        }

        public void setRenderedSummaryClientId(String renderedSummaryClientId) {
            this.renderedSummaryClientId = renderedSummaryClientId;
        }

        public void renderInitScript(ScriptBuilder sb) throws IOException {
            if (renderedSummaryClientId == null) {
                // this summary component hasn't been rendered, and so...
                // - we cannot initialize it without knowing its actual client id which could only be known if it was
                //   rendered in the place where it is specified;
                // - there's no point in initializing it if it's not rendered.
                return;
            }
            Object summaryValue = calculator != null ? calculator.endCalculation() : "";
            Object summaryOutput = calculator != null ? function.getName() + "=" + summaryValue : "";
            sb.functionCall("O$.Summary._init", renderedSummaryClientId, summaryOutput).semicolon();
        }
    }

    private Boolean calculatedGlobally;
    private String calculatedForGroupsWithColumnId;
    private Boolean calculatedForAllGroups;

    private boolean getCalculatedGlobally() {
        calculateApplicability();
        return calculatedGlobally;
    }

    private String getCalculatedForGroupsWithColumnId() {
        calculateApplicability();
        return !calculatedForGroupsWithColumnId.equals("") ? calculatedForGroupsWithColumnId : null;
    }

    private boolean getCalculatedForAllGroups() {
        calculateApplicability();
        return calculatedForAllGroups;
    }

    private void calculateApplicability() {
        if (calculatedGlobally != null) {
            // applicability scope for this instance of the Summary component
            // has already been calculated for this rendering session
            return;
        }
        Components.FacetReference facetReference = Components.getParentFacetReference(this);
        if (facetReference == null)
            throw new FacesException("The <o:summary> tag can only be used inside of <o:dataTable> component.");
        UIComponent facetOwner = facetReference.getFacetOwner();
        if (!(facetOwner instanceof DataTable)
                && !(facetOwner instanceof Column)
                && !(facetOwner instanceof ColumnGroup))
            throw new FacesException("The <o:summary> tag must be placed as a facet inside of <o:dataTable>, " +
                    "<o:column> or <o:columnGroup> components.");
        AbstractTable facetOwnerTableReference = facetOwner instanceof DataTable
                ? (AbstractTable) facetOwner
                : ((BaseColumn) facetOwner).getTable();
        DataTable table = getTable();
        if (facetOwnerTableReference != table)
            throw new FacesException("The <o:summary> tag must be placed as a facet inside of <o:dataTable>, " +
                    "<o:column> or <o:columnGroup> components.");
        String facetName = facetReference.getFacetName();
        if (facetOwner == table || (equalsToOneOf(facetName,
                BaseColumn.FACET_HEADER,
                BaseColumn.FACET_SUB_HEADER,
                BaseColumn.FACET_FOOTER))) {
            // it is either in a table's facet or one column's "global" facets
            calculatedGlobally = true;
            calculatedForGroupsWithColumnId = "";
            calculatedForAllGroups = false;
        } else {
            // it is in one of the column grouping facets
            BaseColumn summaryOwnerColumn = (BaseColumn) facetOwner;
            RowGrouping rowGrouping = table.getRowGrouping();
            if (rowGrouping == null) {
                calculatedGlobally = false;
                calculatedForGroupsWithColumnId = "";
                calculatedForAllGroups = false;
            } else if (equalsToOneOf(facetName, BaseColumn.FACET_GROUP_HEADER, BaseColumn.FACET_GROUP_FOOTER)) {
                List<GroupingRule> groupingRules = rowGrouping.getGroupingRules();
                String summaryOwnerColumnId = summaryOwnerColumn.getId();
                boolean groupedByThisColumn = false;
                for (GroupingRule groupingRule : groupingRules) {
                    if (groupingRule.getColumnId().equals(summaryOwnerColumnId)) {
                        groupedByThisColumn = true;
                        break;
                    }
                }
                calculatedGlobally = false;
                calculatedForGroupsWithColumnId = groupedByThisColumn ? summaryOwnerColumnId : "";
                calculatedForAllGroups = false;
            } else if (equalsToOneOf(facetName, BaseColumn.FACET_IN_GROUP_HEADER, BaseColumn.FACET_IN_GROUP_FOOTER)) {
                calculatedGlobally = false;
                calculatedForGroupsWithColumnId = "";
                calculatedForAllGroups = table.getRenderedColumns().contains(summaryOwnerColumn);
            } else {
                throw new IllegalStateException("Unexpected placement of <o:summary> component: " +
                        "facet owner component is " + facetOwner.getClass().getName() +
                        "; facet name is \"" + facetName + "\"");
            }
        }
    }

    private boolean equalsToOneOf(String str, String... toOneOf) {
        for (String oneOf : toOneOf) {
            if (str.equals(oneOf))
                return true;
        }
        return false;
    }

    public void addCurrentRowValue() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();

        Object value = getByValue(elContext);
        if (getCalculatedGlobally()) {
            getGlobalCalculationContext().addValue(value);
        } else if (getCalculatedForAllGroups()) {
            List<RowGroup> containingGroups = getContainingRowGroupsForThisRow();
            for (RowGroup group : containingGroups) {
                CalculationContext calculationContext = getGroupCalculationContext(group);
                calculationContext.addValue(value);
            }
        } else {
            String colId = getCalculatedForGroupsWithColumnId();
            if (colId == null) {
                // this summary instance does not require calculation in the current table's grouping state
                return;
            }
            List<RowGroup> containingGroups = getContainingRowGroupsForThisRow();
            for (RowGroup group : containingGroups) {
                if (group.getColumnId().equals(colId)) {
                    CalculationContext calculationContext = getGroupCalculationContext(group);
                    calculationContext.addValue(value);
                    break;
                }
            }
        }
    }

    private List<RowGroup> getContainingRowGroupsForThisRow() {
        List<RowGroup> rowGroups = new ArrayList<RowGroup>();
        TableDataModel.RowInfo rowInfo = getTable().getModel().getRowInfo();
        addContainingRowGroupsForThisRow(rowGroups, rowInfo);
        return rowGroups;
    }

    private void addContainingRowGroupsForThisRow(List<RowGroup> rowGroups, TableDataModel.RowInfo rowInfo) {
        TableDataModel.RowInfo parentGroupRowInfo = rowInfo.getParentGroup();
        if (parentGroupRowInfo == null) return;
        GroupHeader groupHeader = (GroupHeader) parentGroupRowInfo.getRowData();
        RowGroup parentRowGroup = groupHeader.getRowGroup();
        rowGroups.add(parentRowGroup);
        addContainingRowGroupsForThisRow(rowGroups, parentGroupRowInfo);
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        super.encodeBegin(context);
        calculateApplicability();
        String clientId = getClientId(context);
        if (getCalculatedGlobally()) {
            getGlobalCalculationContext().setRenderedSummaryClientId(clientId);
        } else {
            Object rowData = getTable().getRowData();
            if (rowData instanceof GroupHeaderOrFooter) {
                GroupHeaderOrFooter groupHeaderOrFooter = (GroupHeaderOrFooter) rowData;
                RowGroup rowGroup = groupHeaderOrFooter.getRowGroup();
                CalculationContext groupCalculationContext = getGroupCalculationContext(rowGroup);
                groupCalculationContext.setRenderedSummaryClientId(clientId);
            } else {
                throw new IllegalStateException("The <o:summary> tag should either be placed into one of <o:dataTable> " +
                        "component's facets or in one of the <o:column>/<o:columnGroup> facets of the appropriate table " +
                        "component.");
            }
        }


    }

    public void encodeAfterCalculation(FacesContext context) throws IOException {
        ScriptBuilder sb = new ScriptBuilder();
        if (globalCalculationContext != null) {
            if (globalCalculationContext.getRenderedSummaryClientId() == null) {
                // this case might take place when this <o:summary> component is inserted into the "below" facet of
                // <o:dataTable>, in which case it didn't have a chance to be rendered up to this moment and so its
                // client id has not been initialized yet
                Components.FacetReference parentFacetReference = Components.getParentFacetReference(this);
                if (parentFacetReference != null && parentFacetReference.getFacetName().equals(AbstractTable.FACET_BELOW)) {
                    DataTable table = getTable();
                    int prevRowIndex = table.getRowIndex();
                    if (prevRowIndex != -1)
                        table.setRowIndex(-1);
                    try {
                        String clientId = getClientId(context);
                        globalCalculationContext.setRenderedSummaryClientId(clientId);
                    } finally {
                        table.setRowIndex(prevRowIndex);
                    }
                }
            }
            globalCalculationContext.renderInitScript(sb);
        }
        if (groupCalculationContexts != null) {
            Collection<CalculationContext> groupCalculationContexts = this.groupCalculationContexts.values();
            for (CalculationContext groupCalculationContext : groupCalculationContexts) {
                groupCalculationContext.renderInitScript(sb);
            }
        }
        Rendering.renderInitScript(context, sb, TableUtil.getTableUtilJsURL(context));


    }
}
