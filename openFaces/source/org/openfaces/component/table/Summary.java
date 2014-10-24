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

import org.openfaces.component.OUIOutput;
import org.openfaces.component.command.MenuItem;
import org.openfaces.component.command.PopupMenu;
import org.openfaces.component.table.impl.DynamicColumn;
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
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.NumberConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Summary extends OUIOutput {
    public static final String COMPONENT_TYPE = "org.openfaces.Summary";
    public static final String COMPONENT_FAMILY = "org.openfaces.Summary";

    private static final String DEFAULT_PATTERN = "#{function}: #{valueString}";
    private static final String ATTR_PATTERN = "pattern";
    private static final String FACET_POPUP_MENUS_CONTAINER = "_summariesPopupMenusContainer";
    private static final String ATTR_FUNCTION_NAME = "_functionName";

    private StampStates<StampState> stampStates = new StampStates<StampState>(this, StampState.class) {
        @Override
        protected String getStampContextId(FacesContext context) {
            Columns columns = getColumns();
            if (columns == null) return null;
            int columnIndex = columns.getColumnIndex();
            List<DynamicColumn> dynamicColumns = columns.toColumnList(context);
            DynamicColumn dynamicColumn = dynamicColumns.get(columnIndex);
            return dynamicColumn.getId();
        }

        @Override
        protected boolean isDefaultStateId(String stampContextId) {
            return stampContextId == null;
        }

        private Columns[] columnsReference;
        private Columns getColumns() {
            if (Summary.this.getParent() == null) {
                // invoking this method during attribute processing phase when the component is not embedded into view
                // yet shouldn't result in caching the null columns reference, which cannot be calculated at this stage
                return null;
            }
            if (columnsReference == null) {
                columnsReference = new Columns[]{Components.getParentWithClass(Summary.this, Columns.class)};
            }
            return columnsReference[0];
        }
    };

    private boolean implicitMode;

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

    protected StampState stampState() {
        return stampStates.currentState(getFacesContext());
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                stampStates.saveState(context),
                implicitMode
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        stampStates.restoreState(context, state[i++]);
        implicitMode = (Boolean) state[i++];
    }

    public static class StampState extends StampStates.State {
        private SummaryFunction function;
        private Boolean functionEditable;
        private UsageContext usageContext;

        private String originalClientId;

        private CalculationContext globalCalculationContext;
        private Map<RowGroup, CalculationContext> groupCalculationContexts;

        @Override
        public Object saveState(FacesContext context) {
            return new Object[]{
                    super.saveState(context),
                    function,
                    functionEditable,
                    originalClientId
            };
        }

        @Override
        public void restoreState(FacesContext context, Object stateObj) {
            Object[] state = (Object[]) stateObj;
            int i = 0;
            super.restoreState(context, state[i++]);
            function = (SummaryFunction) state[i++];
            functionEditable = (Boolean) state[i++];
            originalClientId  = (String) state[i++];
        }

        public CalculationContext getGlobalCalculationContext() {
            if (globalCalculationContext == null)
                globalCalculationContext = new CalculationContext();
            return globalCalculationContext;
        }

        public CalculationContext getGroupCalculationContext(RowGroup group) {
            if (groupCalculationContexts == null)
                groupCalculationContexts = new HashMap<RowGroup, CalculationContext>();
            CalculationContext calculationContext = groupCalculationContexts.get(group);
            if (calculationContext == null) {
                calculationContext = new CalculationContext();
                groupCalculationContexts.put(group, calculationContext);
            }
            return calculationContext;
        }

        private UsageContext getUsageContext() {
            if (usageContext == null)
                usageContext = new UsageContext((Summary) getComponent());
            return usageContext;
        }

        private DataTable getTable() {
            return getUsageContext().getTable();
        }

        private Summaries getSummaries() {
            return getUsageContext().getTable().getSummaries();
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

            public void renderInitScript(ScriptBuilder sb, PopupMenu popupMenu, MenuItem selectedMenuItem) throws IOException {
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
                FacesContext context = FacesContext.getCurrentInstance();
                if (converter == null && summaryValue != null) {
                    Class cls = summaryValue.getClass();
                    if (cls.equals(Double.class) || cls.equals(Float.class)) {
                        NumberConverter numberConverter = new NumberConverter();
                        numberConverter.setGroupingUsed(false);
                        numberConverter.setMaxFractionDigits(2);
                        converter = numberConverter;
                    } else {
                        converter = Rendering.getConverterForType(context, cls);
                    }
                }

                Object summaryValueStr = summaryValue != null
                        ? (converter != null ? converter.getAsString(context, getSummary(), summaryValue) : summaryValue.toString())
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
                        popupMenu,
                        selectedMenuItem,
                        popupMenu != null ? Resources.internalURL(context, SelectBooleanCheckboxImageManager.DEFAULT_SELECTED_IMAGE) : null,
                        popupMenu != null ? Resources.internalURL(context, SelectBooleanCheckboxImageManager.DEFAULT_UNSELECTED_IMAGE) : null
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

        private Object getByValue(ELContext elContext) {
            UsageContext usageContext = getUsageContext();
            SummaryFunction function = usageContext.getFunction();
            if (function instanceof CountFunction) {
                // the actual value does not matter in case of the "Count" function, and we should allow the user not
                // to specify the "by" expression for this function, so we're skipping the actual "by" value calculation
                // attempt below.
                return "";
            }
            Object value = usageContext.getByValue(elContext);
            return value;
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

        private PopupMenu getMenuWithFunctions(FacesContext context, List<SummaryFunction> applicableFunctions) {
            PopupMenu popupMenu = null;
            UIComponent popupMenuContainer = getPopupMenuContainer(context);
            List<UIComponent> children = popupMenuContainer.getChildren();
            final String summaryFunctionsAttribute = "_summaryFunctions";
            for (UIComponent child : children) {
                PopupMenu m = (PopupMenu) child;
                List<SummaryFunction> summaryFunctions = (List<SummaryFunction>) m.getAttributes().get(summaryFunctionsAttribute);
                if (applicableFunctions.equals(summaryFunctions)) {
                    popupMenu = m;
                }
            }

            if (popupMenu == null) {
                final String idCounterAttr = "_idCounter";
                Integer idCounter = (Integer) popupMenuContainer.getAttributes().get(idCounterAttr);
                if (idCounter == null) idCounter = 0;
                popupMenu = Components.createComponent(context,
                        PopupMenu.COMPONENT_TYPE, PopupMenu.class, popupMenuContainer, "popupMenu" + idCounter++);
                popupMenuContainer.getAttributes().put(idCounterAttr, idCounter);
                popupMenu.setStandalone(true);

                popupMenu.getAttributes().put(summaryFunctionsAttribute, applicableFunctions);
                for (int i = 0, count = applicableFunctions.size(); i < count; i++) {
                    SummaryFunction function = applicableFunctions.get(i);
                    MenuItem menuItem = Components.createComponent(context, MenuItem.COMPONENT_TYPE, MenuItem.class, popupMenu, "item" + i);
                    String functionName = function.getName();
                    menuItem.setValue(functionName);
                    menuItem.setOnclick(new ScriptBuilder().functionCall(
                            "O$.Summary._setFunction", functionName.toLowerCase()
                    ).toString());
                    menuItem.setIconUrl(Resources.internalURL(context,
                            SelectBooleanCheckboxImageManager.DEFAULT_UNSELECTED_IMAGE));
                    menuItem.getAttributes().put(ATTR_FUNCTION_NAME, functionName);
                    popupMenu.getChildren().add(menuItem);
                }

                popupMenuContainer.getChildren().add(popupMenu);
            }

            return popupMenu;
        }

        private UIComponent getPopupMenuContainer(FacesContext context) {
            DataTable table = getTable();
            UIComponent facetContainer = table.getFacet(FACET_POPUP_MENUS_CONTAINER);
            if (facetContainer == null) {
                facetContainer = Components.createComponent(context, HtmlPanelGroup.COMPONENT_TYPE, UIComponent.class, table, "_popupMenuContainer_");
                table.getFacets().put(FACET_POPUP_MENUS_CONTAINER, facetContainer);
            }
            return facetContainer;
        }

        public void encodeAfterCalculation(FacesContext context) throws IOException {
            UsageContext usageContext = getUsageContext();
            if (!usageContext.isApplicableInThisContext()) return;

            MenuItem selectedMenuItem = null;
            PopupMenu popupMenu;
            if (getSummary().getFunctionEditable()) {
                List<SummaryFunction> applicableFunctions = usageContext.getApplicableFunctions();
                popupMenu = getMenuWithFunctions(context, applicableFunctions);
                encodePopupMenu(context, popupMenu);

                SummaryFunction currentFunction = usageContext.getFunction();
                String currentFunctionName = currentFunction.getName();
                List<UIComponent> children = popupMenu.getChildren();
                for (UIComponent child : children) {
                    if (!(child instanceof MenuItem)) continue;
                    MenuItem menuItem = (MenuItem) child;
                    String functionName = (String) child.getAttributes().get(ATTR_FUNCTION_NAME);
                    if (functionName.equals(currentFunctionName)) {
                        selectedMenuItem = menuItem;
                        break;
                    }
                }

            } else {
                popupMenu = null;
            }

            ScriptBuilder sb = new ScriptBuilder();
            if (globalCalculationContext != null) {
                if (globalCalculationContext.getRenderedSummaryClientId() == null) {
                    // this case might take place when this <o:summary> component is inserted into the "below" facet of
                    // <o:dataTable>, in which case it didn't have a chance to be rendered up to this moment and so its
                    // client id has not been initialized yet
                    Components.FacetReference parentFacetReference = Components.getParentFacetReference(getSummary());
                    if (parentFacetReference != null && parentFacetReference.getFacetName().equals(AbstractTable.FACET_BELOW)) {
                        DataTable table = getTable();
                        int prevRowIndex = table.getRowIndex();
                        if (prevRowIndex != -1) table.setRowIndex(-1);
                        try {
                            String clientId = Components.getFreshClientId(getSummary(), context);
                            globalCalculationContext.setRenderedSummaryClientId(clientId);
                        } finally {
                            if (prevRowIndex != -1) table.setRowIndex(prevRowIndex);
                        }
                    }
                }
                globalCalculationContext.renderInitScript(sb, popupMenu, selectedMenuItem);
            }
            if (groupCalculationContexts != null) {
                Collection<CalculationContext> groupCalculationContexts = this.groupCalculationContexts.values();
                for (CalculationContext groupCalculationContext : groupCalculationContexts) {
                    groupCalculationContext.renderInitScript(sb, popupMenu, selectedMenuItem);
                }
            }

            Rendering.renderInitScript(context, sb, TableUtil.getTableUtilJsURL(context));
        }

        private Summary getSummary() {
            return (Summary) getComponent();
        }

        private void encodePopupMenu(FacesContext context, PopupMenu popupMenu) throws IOException {
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            final String renderedMenusParam = Summary.class.getName() + ".renderedMenus";
            Set<PopupMenu> renderedMenus = (Set<PopupMenu>) requestMap.get(renderedMenusParam);
            if (renderedMenus == null) {
                renderedMenus = new HashSet<PopupMenu>();
                requestMap.put(renderedMenusParam, renderedMenus);
            }

            if (renderedMenus.contains(popupMenu)) return;
            renderedMenus.add(popupMenu);

            popupMenu.encodeAll(context);
        }

        private ValueExpression getPatternExpression() {
            Summary summary = getSummary();
            ValueExpression expression = summary.getPattern();
            if (expression == null) {
                Summaries summaries = getSummaries();
                if (summaries != null) {
                    expression = summaries.getPattern();
                }
                if (expression == null) {
                    FacesContext context = FacesContext.getCurrentInstance();
                    ELContext elContext = context.getELContext();
                    expression = context.getApplication().getExpressionFactory().createValueExpression(
                            elContext, DEFAULT_PATTERN, Object.class);
                }
                summary.setPattern(expression);
            }
            return expression;
        }

    }


    public ValueExpression getBy() {
        return getValueExpression("by");
    }

    public void setBy(ValueExpression by) {
        setValueExpression("by", by);
    }

    public ValueExpression getPattern() {
        return getValueExpression(ATTR_PATTERN);
    }

    public void setPattern(ValueExpression valueExpression) {
        setValueExpression(ATTR_PATTERN, valueExpression);
    }

    public boolean getFunctionEditable() {
        StampState stampState = stampState();
        DataTable table = getParent() != null ? stampState.getUsageContext().getTable() : null;
        Summaries summaries = table != null ? table.getSummaries() : null;
        boolean defaultValue = summaries != null ? summaries.getFunctionEditable() : true;
        return ValueBindings.get(this, "functionEditable", stampState.functionEditable, defaultValue);
    }

    public void setFunctionEditable(boolean functionEditable) {
        stampState().functionEditable = functionEditable;
    }

    private static class UsageContext {
        private Summary summary;
        private DataTable table;
        private BaseColumn column;
        private boolean insideColumnsComponent;
        private String facetName;

        private Boolean applicableInThisContext;
        /**
         * "Common location" means that the summary is not bound to any table's column, but to the whole table or
         * column group
         */
        private boolean commonLocation;
        /**
         * "Global calculation" means that according to the summary component's declaration location it should be
         * calculated over all displayed rows and not over the individual groups (when row grouping feature is used)
         */
        private Boolean calculatedGlobally;
        private String calculatedForGroupsWithColumnId;
        private Boolean calculatedForAllGroups;

        private SummaryFunction function;

        public UsageContext(Summary summary) {
            this.summary = summary;

            Components.FacetReference facetReference = Components.getParentFacetReference(summary);
            FacesContext context = FacesContext.getCurrentInstance();
            if (facetReference == null)
                throw new FacesException("The <o:summary> tag can only be used inside of one of the facets of the " +
                        "<o:dataTable> component or inside of the facets of one of its <o:column> or <o:columnGroup> " +
                        "tags, but it wasn't placed into a facet: " +
                        summary.getClientId(context));
            UIComponent facetOwner = facetReference.getFacetOwner();

            AbstractTable abstractTable;
            if (facetOwner instanceof DataTable) {
                abstractTable = (AbstractTable) facetOwner;
            } else if (facetOwner instanceof Column || facetOwner instanceof ColumnGroup) {
                this.column = (BaseColumn) facetOwner;
                abstractTable = column.getTable();
                if (abstractTable == null)
                    throw new FacesException("The <o:summary> component is placed into a misplaced column component -" +
                            "the parent table for that column (" +
                            facetOwner.getClientId(context) +
                            ") could not be found in the components tree.");
            } else if (facetOwner instanceof Columns) {
                Columns columns = (Columns) facetOwner;
                abstractTable = columns.getTable();
                if (abstractTable == null)
                    throw new FacesException("The <o:summary> component is placed into a misplaced <o:columns> component -" +
                            "the parent table for that component (" +
                            facetOwner.getClientId(context) +
                            ") could not be found in the components tree.");
                int columnIndex = columns.getColumnIndex();
                List<DynamicColumn> dynamicColumns = columns.toColumnList(context);
                this.insideColumnsComponent = true;
                this.column = dynamicColumns.get(columnIndex);//columnIndex != -1 ? dynamicColumns.get(columnIndex) : null;
            } else {
                throw new FacesException("The <o:summary> tag must be placed as a facet inside of <o:dataTable>, " +
                        "<o:column> or <o:columnGroup> components. This summary component (" +
                        summary.getClientId(context) +
                        ") was placed into the \"" + facetReference.getFacetName() +
                        "\" facet of component with class " +
                        facetOwner.getClass().getName() + " (clientId=\"" +
                        facetOwner.getClientId(context) + "\"");
            }
            if (!(abstractTable instanceof DataTable)) {
                // e.g. in case of an attempt to use in inside of TreeTable
                throw new FacesException("The <o:summary> component can only be used with <o:dataTable> component " +
                        "currently: " + summary.getClientId(context));
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

            commonLocation = column == null || column instanceof ColumnGroup;
        }

        /**
         * Applicable for the implicitly-created Summary instances. This method checks if this implicitly-created
         * instance is applicable in the context where it is created. Applicable means that summary can be calculated
         * here and should be displayed. In other words this is a mechanism that detects whether summary should be
         * displayed in any given context (a facet inside of any given column).
         */
        public boolean isApplicableInThisContext() {
            if (applicableInThisContext == null) {
                if (!summary.implicitMode) {
                    applicableInThisContext = true;
                } else {
                    ValueExpression byExpression = getByExpression(false);
                    if (byExpression == null) {
                        applicableInThisContext = false;
                    } else {
                        SummaryFunction function = detectFunctionByColumn(byExpression);
                        applicableInThisContext = function != null && !(function instanceof CountFunction);
                    }
                }
            }
            return applicableInThisContext;
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
            return getByExpression(true);
        }

        public Object getByValue(ELContext elContext) {
            DynamicColumn dynamicColumn = column instanceof DynamicColumn ? (DynamicColumn) column : null;
            Runnable restoreVariables = dynamicColumn != null ? dynamicColumn.enterComponentContext() : null;
            Object value = getByExpression().getValue(elContext);
            if (restoreVariables != null) restoreVariables.run();
            return value;
        }

        private ValueExpression getByExpression(boolean throwExceptions) {
            if (byExpression != null)
                return byExpression;

            byExpression = summary.getBy();
            if (byExpression != null)
                return byExpression;

            if (column == null) {
                if (!throwExceptions) return null;
                if (table.getRowIndex() != -1) {
                    // reset row index just to ensure a suffix-less table id in an exception message
                    table.setRowIndex(-1);
                }
                throw new FacesException("Could not detect the summary calculation expression for <o:summary> " +
                        "component in the table " + table.getClientId(FacesContext.getCurrentInstance()) + ". Either " +
                        "the \"by\" attribute has to be specified or <o:summary> should be placed into a column's " +
                        "facet to derive the expression from that column automatically.");
            }
            if (!(column instanceof Column)) {
                if (!throwExceptions) return null;
                throw new FacesException("<o:summary> component can only be used inside of <o:column>, but not other " +
                        "types of column tags (" + column.getClass().getName() + ")");
            }
            byExpression = column.getColumnValueExpression();
            // the summary's expression is not specified explicitly and it's not inside of a column whose value
            // expression can be detected
            if (byExpression == null) {
                if (!throwExceptions) return null;
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

        /**
         * Gets the user-specified function or detects it automatically if not specified in this Summary component
         * explicitly.
         */
        public SummaryFunction getFunction() {
            if (function != null) return function;

            function = summary.getFunction();
            if (function != null) return function;

            function = detectFunctionByColumn(commonLocation ? null : getByExpression());

            return function;
        }

        private SummaryFunction detectFunctionByColumn(ValueExpression byExpression) {
            if (commonLocation) {
                return new CountFunction();
            } else {
                BaseColumn.ExpressionData expressionData = column.getExpressionData(byExpression);
                Class valueType = expressionData.getValueType();
                return getDefaultFunctionForType(valueType);
            }
        }

        public List<SummaryFunction> getApplicableFunctions() {
            if (commonLocation) {
                ValueExpression byExpression = summary.getBy();
                List<BaseColumn> allColumns = table.getAllColumns();
                BaseColumn anyColumn = allColumns.size() > 0 ? allColumns.get(0) : null;
                if (byExpression != null && anyColumn != null) {
                    BaseColumn.ExpressionData expressionData = anyColumn.getExpressionData(byExpression);
                    Class valueType = expressionData.getValueType();
                    return getFunctionsForType(valueType);
                } else {
                    return Collections.singletonList((SummaryFunction) new CountFunction());
                }
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


    public SummaryFunction getFunction() {
        return ValueBindings.get(this, "function", stampState().function, SummaryFunction.class);
    }

    public void setFunction(SummaryFunction function) {
        stampState().function = function;
    }


    public void addCurrentRowValue() {
        stampState().addCurrentRowValue();
    }

    @Override
    public void decode(FacesContext context) {
        super.decode(context);
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String functionName = requestParameterMap.get(stampState().originalClientId + "::setFunction");
        if (functionName != null) {
            SummaryFunction summaryFunction = ApplicationParams.getSummaryFunctionByName(functionName);
            if (summaryFunction == null)
                throw new IllegalArgumentException("Invalid summary function name -- no function with this name could be found: " + summaryFunction);
            setFunction(summaryFunction);
        }
    }

    @Override
    public void processUpdates(FacesContext context) {
        super.processUpdates(context);
        StampState stampState = stampState();
        if (stampState.function != null && ValueBindings.set(this, "function", stampState.function))
            stampState.function = null;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        StampState stampState = stampState();
        UsageContext usageContext = stampState.getUsageContext();
        if (!usageContext.isApplicableInThisContext()) return;

        String clientId = Components.getFreshClientId(this, context);

        DataTable table = stampState.getTable();// getTable invocation validates the parent tag
        super.encodeBegin(context);
        if (usageContext.getCalculatedGlobally()) {
            stampState.getGlobalCalculationContext().setRenderedSummaryClientId(clientId);
        } else {
            Object rowData = table.getRowData();
            if (rowData instanceof GroupHeaderOrFooter) {
                GroupHeaderOrFooter groupHeaderOrFooter = (GroupHeaderOrFooter) rowData;
                RowGroup rowGroup = groupHeaderOrFooter.getRowGroup();
                StampState.CalculationContext groupCalculationContext = stampState.getGroupCalculationContext(rowGroup);
                groupCalculationContext.setRenderedSummaryClientId(clientId);
            } else {
                throw new IllegalStateException("The <o:summary> tag should either be placed into one of <o:dataTable> " +
                        "component's facets or in one of the <o:column>/<o:columnGroup> facets of the appropriate table " +
                        "component.");
            }
        }
    }

    public void encodeAfterCalculation(FacesContext context) throws IOException {
        stampState().encodeAfterCalculation(context);
    }

    public void prepare(FacesContext context) {
        StampState stampState = stampState();
        UsageContext usageContext = stampState.getUsageContext();
        if (usageContext.getTable().getRowIndex() != -1)
            throw new IllegalArgumentException("table's rowIndex is expected to be -1 when invoking the " +
                    "prepare method for it to be able to detect its 'original' client id");
        stampState.originalClientId = Components.getFreshClientId(this, context);
    }


}
