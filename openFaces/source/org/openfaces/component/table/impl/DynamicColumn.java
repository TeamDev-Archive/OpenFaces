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
package org.openfaces.component.table.impl;

import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.Column;
import org.openfaces.component.table.Columns;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class DynamicColumn extends Column implements DynamicCol {
    public static final String COMPONENT_TYPE = "org.openfaces.DynamicColumn";
    public static final String COMPONENT_FAMILY = "org.openfaces.DynamicColumn";

    private static final String CONTEXT_ENTRANCE_STACK_KEY = DynamicColumn.class.getName() + ".contextEntranceStack";

    private Columns columns;
    private Object colData;
    private int colIndex;

    public DynamicColumn() {
        setRendererType("org.openfaces.DynamicColumnRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void setColumns(Columns columns) {
        this.columns = columns;
    }

    public void setColData(Object colData) {
        this.colData = colData;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }

    @Override
    public AbstractTable getTable() {
        return columns.getTable();
    }

    @Override
    public UIComponent getFacet(String name) {
        UIComponent component = super.getFacet(name);
        if (component != null)
            return component;
        return columns.getFacet(name);
    }

    @Override
    public UIComponent getHeader() {
        UIComponent header = super.getHeader();
        if (header != null) return header;
        return columns.getHeader();
    }

    @Override
    public UIComponent getFooter() {
        UIComponent footer = super.getFooter();
        if (footer != null) return footer;
        return columns.getFooter();
    }

    @Override
    public UIComponent getSubHeader() {
        UIComponent subHeader = super.getSubHeader();
        if (subHeader != null)
            return subHeader;
        return columns.getSubHeader();
    }

    @Override
    public UIComponent getGroupHeader() {
        UIComponent component = super.getGroupHeader();
        if (component != null)
            return component;
        return columns.getGroupHeader();
    }

    @Override
    public UIComponent getGroupFooter() {
        UIComponent component = super.getGroupFooter();
        if (component != null)
            return component;
        return columns.getGroupFooter();
    }

    @Override
    public UIComponent getInGroupHeader() {
        UIComponent component = super.getInGroupHeader();
        if (component != null)
            return component;
        return columns.getInGroupHeader();
    }

    @Override
    public UIComponent getInGroupFooter() {
        UIComponent component = super.getInGroupFooter();
        if (component != null)
            return component;
        return columns.getInGroupFooter();
    }

    @Override
    public Converter getConverter() {
        return columns.getConverter();
    }

    @Override
    public void setConverter(Converter converter) {
        columns.setConverter(converter);
    }

    private List<DynamicColumn> getContextEntranceStack(Map<String, Object> requestMap) {
        List<DynamicColumn> contextEntranceStack = (List<DynamicColumn>) requestMap.get(CONTEXT_ENTRANCE_STACK_KEY);
        if (contextEntranceStack == null) {
            contextEntranceStack = new ArrayList<DynamicColumn>();
            requestMap.put(CONTEXT_ENTRANCE_STACK_KEY, contextEntranceStack);
        }
        return contextEntranceStack;
    }

    public boolean isComponentInContext() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        final List<DynamicColumn> contextStack = getContextEntranceStack(requestMap);
        int contextStackSize = contextStack.size();
        if (contextStackSize == 0)
            return false;
        return contextStack.get(contextStackSize - 1) == this;
    }

    public Runnable enterComponentContext() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        final List<DynamicColumn> contextStack = getContextEntranceStack(requestMap);
        int contextStackSize = contextStack.size();
        if (contextStackSize > 0 && contextStack.get(contextStackSize - 1) == this) {
            // already in context
            return null;
        }
        contextStack.add(this);

        String var = columns.getVar();
        final Object prevVarValue = requestMap.put(var, colData);
        Object prevIndexVarValue = null;

        String indexVar = columns.getIndexVar();
        if (indexVar != null)
            prevIndexVarValue = requestMap.put(indexVar, colIndex);

        final int prevColIndex = columns.getColumnIndex();
        columns.setColumnIndex(colIndex);
        final Object finalPrevIndexVarValue = prevIndexVarValue;
        return new Runnable() {
            public void run() {
                int contextStackSize = contextStack.size();
                if (contextStackSize == 0) throw new IllegalStateException("DynamicColumn context entrance stack " +
                        "is empty upon an attempt to exit the context. The reason might be the unpaired " +
                        "\"enter\" and \"exit\" executions");
                DynamicColumn lastEntry = contextStack.remove(contextStackSize - 1);
                if (lastEntry != DynamicColumn.this) {
                    contextStack.add(lastEntry); // move the improperly-removed last entry back to its position
                    throw new IllegalStateException("DynamicColumn's \"exit component context\" runnable: " +
                            "An unpaired attempt to exit a context without exiting the other context is made");
                }

                Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();

                String var = columns.getVar();
                requestMap.put(var, prevVarValue);

                String indexVar = columns.getIndexVar();
                if (indexVar != null) {
                    requestMap.put(indexVar, finalPrevIndexVarValue);
                }

                columns.setColumnIndex(prevColIndex);
            }
        };
    }

    private Runnable exitContextRunnable;

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        exitContextRunnable = enterComponentContext();
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        if (exitContextRunnable != null) exitContextRunnable.run();
    }

    public List<UIComponent> getChildrenForProcessing() {
        return columns.getChildren();
    }

    public Map<String, UIComponent> getFacetsForProcessing() {
        Map<String, UIComponent> facets = new HashMap(columns.getFacets());
        facets.remove(Columns.FACET_COLUMN_COMPONENTS);
        facets.putAll(getFacets());
        return facets;
    }

    @Override
    public void processDecodes(FacesContext context) {
        Runnable exitContext = enterComponentContext();
        Collection<UIComponent> facets = getFacetCollection();
        for (UIComponent component : facets) {
            component.processDecodes(context);
        }
        if (exitContext != null) exitContext.run();
    }

    private Collection<UIComponent> getFacetCollection() {
        ArrayList<UIComponent> facets = new ArrayList<UIComponent>();
        UIComponent header = getHeader();
        UIComponent footer = getFooter();
        UIComponent subHeader = getSubHeader();
        if (header != null)
            facets.add(header);
        if (footer != null)
            facets.add(footer);
        if (subHeader != null)
            facets.add(subHeader);
        return facets;
    }

    @Override
    public void processValidators(FacesContext context) {
        Runnable restoreVariables = enterComponentContext();
        Collection<UIComponent> facets = getFacetCollection();
        for (UIComponent component : facets) {
            component.processValidators(context);
        }
        if (restoreVariables != null) restoreVariables.run();
    }

    @Override
    public void processUpdates(FacesContext context) {
        Runnable restoreVariables = enterComponentContext();
        Collection<UIComponent> facets = getFacetCollection();
        for (UIComponent component : facets) {
            component.processUpdates(context);
        }
        if (restoreVariables != null) restoreVariables.run();
    }

    @Override
    public ExpressionData getExpressionData(ValueExpression expression) {
        Runnable restoreVariables = enterComponentContext();
        try {
            return super.getExpressionData(expression);
        } finally {
            if (restoreVariables != null) restoreVariables.run();
        }
    }

    @Override
    public ValueExpression getColumnSortingExpression() {
        ValueExpression superExpression = super.getColumnSortingExpression();
        return superExpression != null ? new ContextDependentExpressionWrapper(this, superExpression) : null;
    }

    @Override
    public ValueExpression getColumnGroupingExpression() {
        ValueExpression superExpression = super.getColumnGroupingExpression();
        return superExpression != null ? new ContextDependentExpressionWrapper(this, superExpression) : null;
    }

    @Override
    public ValueExpression getSortingComparatorExpression() {
        ValueExpression superExpression = super.getSortingComparatorExpression();
        return superExpression != null ? new ContextDependentExpressionWrapper(this, superExpression) : null;
    }
}
