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
import org.openfaces.component.command.MenuItem;
import org.openfaces.component.command.PopupMenu;
import org.openfaces.component.table.impl.TableDataModel;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.renderkit.select.SelectBooleanCheckboxImageManager;
import org.openfaces.util.ApplicationParams;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.ValueBindings;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Summary extends OUIOutput {
    public static final String COMPONENT_TYPE = "org.openfaces.Summary";
    public static final String COMPONENT_FAMILY = "org.openfaces.Summary";

    private static final String DEFAULT_PATTERN = "#{function}: #{valueString}";
    private static final String ATTR_PATTERN = "pattern";
    private static final String FACET_POPUP_MENU = "_popupMenu";

    private SummaryFunction function;
    private boolean implicitMode;
    private boolean contextMenuAdded;
    private String originalClientId;

    private UsageContext usageContext;

    public Summary() {
        this(false);
    }

    public Summary(boolean implicitMode) {
        setRendererType("org.openfaces.SummaryRenderer");
        this.implicitMode = implicitMode;
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                function,
                implicitMode,
                contextMenuAdded,
                originalClientId

        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        function = (SummaryFunction) state[i++];
        implicitMode = (Boolean) state[i++];
        contextMenuAdded = (Boolean) state[i++];
        originalClientId  = (String) state[i++];
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
        return getUsageContext().getTable();
    }

    public ValueExpression getPattern() {
        return getValueExpression(ATTR_PATTERN);
    }

    private ValueExpression getPatternExpression() {
        ValueExpression expression = getPattern();
        if (expression == null) {
            Summaries summaries = getUsageContext().getTable().getSummaries();
            if (summaries != null) {
                expression = summaries.getPattern();
            }
            if (expression == null) {
                FacesContext context = FacesContext.getCurrentInstance();
                ELContext elContext = context.getELContext();
                expression = context.getApplication().getExpressionFactory().createValueExpression(
                        elContext, DEFAULT_PATTERN, Object.class);
            }
            setPattern(expression);
        }
        return expression;
    }

    public void setPattern(ValueExpression valueExpression) {
        setValueExpression(ATTR_PATTERN, valueExpression);
    }

    private static class UsageContext {
        private Summary summary;
        private DataTable table;
        private BaseColumn column;
        private String facetName;

        private Boolean calculatedGlobally;
        private String calculatedForGroupsWithColumnId;
        private Boolean calculatedForAllGroups;

        public UsageContext(Summary summary) {
            this.summary = summary;

            Components.FacetReference facetReference = Components.getParentFacetReference(summary);
            if (facetReference == null)
                throw new FacesException("The <o:summary> tag can only be used inside of one of the facets of the " +
                        "<o:dataTable> component or inside of the facets of one of its <o:column> or <o:columnGroup> " +
                        "tags.");
            UIComponent facetOwner = facetReference.getFacetOwner();

            AbstractTable abstractTable;
            if (facetOwner instanceof DataTable) {
                abstractTable = (AbstractTable) facetOwner;
            } else if (facetOwner instanceof Column || facetOwner instanceof ColumnGroup) {
                this.column = (BaseColumn) facetOwner;
                abstractTable = column.getTable();
                if (abstractTable == null)
                    throw new FacesException("The <o:summary> component is placed into a misplaced column component -" +
                            "the parent table for that column (" + facetOwner.getClientId(FacesContext.getCurrentInstance()) +
                            ") could not be found in the components tree.");
            } else {
                throw new FacesException("The <o:summary> tag must be placed as a facet inside of <o:dataTable>, " +
                        "<o:column> or <o:columnGroup> components.");
            }
            if (!(abstractTable instanceof DataTable)) {
                // e.g. in case of an attempt to use in inside of TreeTable
                throw new FacesException("The <o:summary> component can only be used with <o:dataTable> component currently.");
            }
            this.table = (DataTable) abstractTable;

            this.facetName = facetReference.getFacetName();
            if (facetOwner == table || (equalsToOneOf(facetName,
                    BaseColumn.FACET_HEADER,
                    BaseColumn.FACET_SUB_HEADER,
                    BaseColumn.FACET_FOOTER))) {
                // it is either in a table's facet or one column's "global" facets
                this.calculatedGlobally = true;
                this.calculatedForGroupsWithColumnId = "";
                this.calculatedForAllGroups = false;
            } else {
                // it is in one of the column's grouping facets
                this.calculatedGlobally = false;

                RowGrouping rowGrouping = table.getRowGrouping();
                if (rowGrouping == null) {
                    // no calculation for this summary component is required at all since it's inside of the
                    // grouping-related facets, but grouping is turned off currently
                    this.calculatedForGroupsWithColumnId = "";
                    this.calculatedForAllGroups = false;
                } else if (equalsToOneOf(facetName, BaseColumn.FACET_GROUP_HEADER, BaseColumn.FACET_GROUP_FOOTER)) {
                    List<GroupingRule> groupingRules = rowGrouping.getGroupingRules();
                    String summaryOwnerColumnId = column.getId();
                    boolean groupedByThisColumn = false;
                    for (GroupingRule groupingRule : groupingRules) {
                        if (groupingRule.getColumnId().equals(summaryOwnerColumnId)) {
                            groupedByThisColumn = true;
                            break;
                        }
                    }
                    this.calculatedForGroupsWithColumnId = groupedByThisColumn ? summaryOwnerColumnId : "";
                    this.calculatedForAllGroups = false;
                } else if (equalsToOneOf(facetName, BaseColumn.FACET_IN_GROUP_HEADER, BaseColumn.FACET_IN_GROUP_FOOTER)) {
                    this.calculatedForGroupsWithColumnId = "";
                    this.calculatedForAllGroups = table.getRenderedColumns().contains(column);
                } else {
                    throw new IllegalStateException("Unexpected placement of <o:summary> component: " +
                            "facet owner component is " + facetOwner.getClass().getName() +
                            "; facet name is \"" + facetName + "\"");
                }
            }

        }

        /**
         * Applicable for the implicitly-created Summary instances. This method checks if this implicitly-created
         * instance is applicable in the context where it is created. Applicable means that summary can be calculated
         * here and should be displayed. In other words this is a mechanism that detects whether summary should be
         * displayed in any given context (a facet inside of any given column).
         */
        public boolean isApplicableInThisContext() {
            if (!summary.implicitMode) return true;

            SummaryFunction function = detectFunctionByColumn(facetName, column, getByExpression());
            return function != null && !(function instanceof CountFunction);
        }


        public boolean getCalculatedGlobally() {
            return calculatedGlobally;
        }

        public String getCalculatedForGroupsWithColumnId() {
            return !calculatedForGroupsWithColumnId.equals("") ? calculatedForGroupsWithColumnId : null;
        }

        public boolean getCalculatedForAllGroups() {
            return calculatedForAllGroups;
        }

        public DataTable getTable() {
            return table;
        }

        private ValueExpression byExpression;

        public ValueExpression getByExpression() {
            if (byExpression != null)
                return byExpression;

            byExpression = summary.getBy();
            if (byExpression != null)
                return byExpression;

            if (column == null) {
                DataTable table = summary.getTable();
                if (table.getRowIndex() != -1) {
                    // reset row index just to ensure a suffix-less table id in an exception message
                    table.setRowIndex(-1);
                }
                throw new FacesException("Could not detect the summary calculation expression for <o:summary> " +
                        "component in the table " + table.getClientId(FacesContext.getCurrentInstance()) + ". Either " +
                        "the \"by\" attribute has to be specified or <o:summary> should be placed into a column's " +
                        "facet to derive the expression from that column automatically.");
            }
            if (!(column instanceof Column))
                throw new FacesException("<o:summary> component can only be used inside of <o:column>, but not other " +
                        "types of column tags (" + column.getClass().getName() + ")");
            byExpression = column.getColumnValueExpression();
            // the summary's expression is not specified explicitly and it's not inside of a column whose value
            // expression can be detected
            if (byExpression == null) {
                DataTable table = summary.getTable();
                if (table.getRowIndex() != -1) {
                    // reset row index just to ensure a suffix-less table id in an exception message
                    table.setRowIndex(-1);
                }
                throw new FacesException("Could not detect the summary calculation expression for " +
                        "<o:summary> component in the table " + table.getClientId(FacesContext.getCurrentInstance()) +
                        ". Neither the \"by\" attribute is specified, nor the value for the parent column could be " +
                        "detected (the column doesn't have the \"value\" attribute).");
            }
            return byExpression;
        }

        private SummaryFunction function;

        /**
         * Gets the user-specified function or detects it automatically if not specified in this Summary component
         * explicitly.
         */
        public SummaryFunction getFunction() {
            if (function != null) return function;

            function = summary.getFunction();
            if (function != null) return function;

            function = detectFunctionByColumn(facetName, column, getByExpression());

            return function;
        }

        private static SummaryFunction detectFunctionByColumn(String facetName, BaseColumn column, ValueExpression byExpression) {
            if (column == null ||
                    column instanceof ColumnGroup ||
                    equalsToOneOf(facetName, BaseColumn.FACET_GROUP_HEADER, BaseColumn.FACET_GROUP_FOOTER)) {
                return new CountFunction();
            } else {
                BaseColumn.ExpressionData expressionData = column.getExpressionData(byExpression);
                Class valueType = expressionData.getValueType();
                return getDefaultFunctionForType(valueType);
            }
        }

        public List<SummaryFunction> getApplicableFunctions() {
            if (column == null ||
                    column instanceof ColumnGroup ||
                    equalsToOneOf(facetName, BaseColumn.FACET_GROUP_HEADER, BaseColumn.FACET_GROUP_FOOTER)) {
                return Collections.singletonList((SummaryFunction) new CountFunction());
            } else {
                ValueExpression byExpression = getByExpression();
                BaseColumn.ExpressionData expressionData = column.getExpressionData(byExpression);
                Class valueType = expressionData.getValueType();
                return getFunctionsForType(valueType);
            }
        }

        private Converter getConverter() {
            return summary.getConverter();
        }

        private static boolean equalsToOneOf(String str, String... toOneOf) {
            for (String oneOf : toOneOf) {
                if (str.equals(oneOf))
                    return true;
            }
            return false;
        }
    }

    private static List<SummaryFunction> getFunctionsForType(Class valueType) {
        List<SummaryFunction> functions = new ArrayList<SummaryFunction>();

        List<SummaryFunction> allFunctions = ApplicationParams.getSummaryFunctions();
        for (SummaryFunction fn : allFunctions) {
            if (fn.isApplicableForClass(valueType)) {
                functions.add(fn);
            }
        }
        return functions;

    }

    private static SummaryFunction getDefaultFunctionForType(Class valueType) {
        List<SummaryFunction> applicableFunctions = getFunctionsForType(valueType);
        if (applicableFunctions.size() > 0)
            return applicableFunctions.get(0);
        return new CountFunction();
    }


    private UsageContext getUsageContext() {
        if (usageContext == null)
            usageContext = new UsageContext(this);
        return usageContext;
    }

    private Object getByValue(ELContext elContext) {
        UsageContext usageContext = getUsageContext();
        SummaryFunction function = usageContext.getFunction();
        if (function instanceof CountFunction) {
            // the actual value does not matter in case of the "Count" function, and we should allow the user not
            // to specify the "by" expression for this function, so we're skipping the actual "by" value calculation
            // attempt below.
            return "";
        }
        Object value = usageContext.getByExpression().getValue(elContext);
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
        private SummaryFunction.Calculator calculator;
        private String renderedSummaryClientId;

        public Object getCalculator() {
            return calculator;
        }

        private void addValue(Object value) {
            if (calculator == null) {
                SummaryFunction function = getUsageContext().getFunction();
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
            UsageContext usageContext = getUsageContext();
            SummaryFunction function = usageContext.getFunction();
            Converter converter = usageContext.getConverter();
            FacesContext context = getFacesContext();
            if (converter == null && summaryValue != null)
                converter = Rendering.getConverterForType(context, summaryValue.getClass());

            Object summaryValueStr = summaryValue != null
                    ? (converter != null ? converter.getAsString(context, Summary.this, summaryValue) : summaryValue.toString())
                    : null;

            ValueExpression patternExpression = getPatternExpression();
            Components.setRequestVariable("value", summaryValue);
            Components.setRequestVariable("valueString", summaryValueStr);
            Components.setRequestVariable("function", function);
            String summaryOutput = calculator != null && summaryValueStr != null
                    ? patternExpression.getValue(context.getELContext()).toString()
                    : "";
            Components.restoreRequestVariable("value");
            Components.restoreRequestVariable("valueString");
            Components.restoreRequestVariable("function");

            sb.functionCall("O$.Summary._init",
                    renderedSummaryClientId,
                    originalClientId,
                    summaryOutput,
                    usageContext.getTable(),
                    getPopupMenu()
            ).semicolon();
        }
    }

    public void addCurrentRowValue() {
        if (!getUsageContext().isApplicableInThisContext()) return;

        ELContext elContext = FacesContext.getCurrentInstance().getELContext();

        Object value = getByValue(elContext);
        if (getUsageContext().getCalculatedGlobally()) {
            getGlobalCalculationContext().addValue(value);
        } else if (getUsageContext().getCalculatedForAllGroups()) {
            List<RowGroup> containingGroups = getContainingRowGroupsForThisRow();
            for (RowGroup group : containingGroups) {
                CalculationContext calculationContext = getGroupCalculationContext(group);
                calculationContext.addValue(value);
            }
        } else {
            String colId = getUsageContext().getCalculatedForGroupsWithColumnId();
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

    @Override
    public void decode(FacesContext context) {
        super.decode(context);
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String functionName = requestParameterMap.get(originalClientId + "::setFunction");
        if (functionName != null) {
            SummaryFunction summaryFunction = ApplicationParams.getSummaryFunctionByName(functionName);
            if (summaryFunction == null)
                throw new IllegalArgumentException("Invalid summary function name -- no function with this name could be found: " + summaryFunction);
            setFunction(summaryFunction);
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
        if (!getUsageContext().isApplicableInThisContext()) return;

        super.encodeBegin(context);
        String clientId = getClientId(context);
        if (getUsageContext().getCalculatedGlobally()) {
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
        if (!getUsageContext().isApplicableInThisContext()) return;
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

        encodePopupMenu(context);
        Rendering.renderInitScript(context, sb, TableUtil.getTableUtilJsURL(context));
    }

    private void encodePopupMenu(FacesContext context) throws IOException {
        PopupMenu popupMenu = getPopupMenu();
        SummaryFunction currentFunction = usageContext.getFunction();
        String currentFunctionName = currentFunction.getName();
        List<UIComponent> children = popupMenu.getChildren();
        for (UIComponent child : children) {
            if (!(child instanceof MenuItem)) continue;
            MenuItem menuItem = (MenuItem) child;
            String functionName = (String) child.getAttributes().get("_functionName");
            menuItem.setIconUrl(Resources.internalURL(context, null,
                    functionName.equals(currentFunctionName)
                            ? SelectBooleanCheckboxImageManager.DEFAULT_SELECTED_IMAGE
                            : SelectBooleanCheckboxImageManager.DEFAULT_UNSELECTED_IMAGE,
                    false));
        }

        popupMenu.encodeAll(context);
    }


    public void createContextMenu(FacesContext context) {
        if (contextMenuAdded) return;
        contextMenuAdded = true;

        UsageContext usageContext = getUsageContext();
        if (usageContext.getTable().getRowIndex() != -1)
            throw new IllegalArgumentException("table's rowIndex is expected to be -1 when invoking the " +
                    "createContextMenu method for it to be able to detect its 'original' client id");
        originalClientId = getClientId(context);

        Application application = context.getApplication();
        PopupMenu popupMenu = (PopupMenu) application.createComponent(PopupMenu.COMPONENT_TYPE);
        popupMenu.setStandalone(true);
        List<SummaryFunction> applicableFunctions = usageContext.getApplicableFunctions();
        for (SummaryFunction function : applicableFunctions) {
            MenuItem menuItem = (MenuItem) application.createComponent(MenuItem.COMPONENT_TYPE);
            String functionName = function.getName();
            menuItem.setValue(functionName);
            menuItem.setOnclick(new ScriptBuilder().functionCall(
                    "O$.Summary._setFunction", functionName.toLowerCase()
            ).toString());
            menuItem.getAttributes().put("_functionName", functionName);
            popupMenu.getChildren().add(menuItem);
        }

        getFacets().put(FACET_POPUP_MENU, popupMenu);
    }

    private PopupMenu getPopupMenu() {
        return (PopupMenu) Components.getFacet(this, FACET_POPUP_MENU);
    }

}
