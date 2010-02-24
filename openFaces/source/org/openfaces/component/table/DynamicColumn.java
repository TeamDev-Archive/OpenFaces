/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.table;

import org.openfaces.util.Rendering;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class DynamicColumn extends Column implements DynamicCol {
    public static final String COMPONENT_TYPE = "org.openfaces.DynamicColumn";
    public static final String COMPONENT_FAMILY = "org.openfaces.DynamicColumn";

    private Columns columns;
    private Object colData;
    private Object prevVarValue;
    private int colIndex;

    public DynamicColumn() {
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
    public UIComponent getHeader() {
        return columns.getHeader();
    }

    @Override
    public void setHeader(UIComponent header) {
        columns.setHeader(header);
    }

    @Override
    public UIComponent getFooter() {
        return columns.getFooter();
    }

    @Override
    public void setFooter(UIComponent footer) {
        columns.setFooter(footer);
    }

    public void declareContextVariables() {
        Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        String var = columns.getVar();
        prevVarValue = requestMap.get(var);
        requestMap.put(var, colData);
        columns.setColumnIndex(colIndex);
    }

    public void undeclareContextVariables() {
        Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        requestMap.put(columns.getVar(), prevVarValue);
        prevVarValue = null;
        columns.setColumnIndex(-1);
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        declareContextVariables();
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context) throws IOException {
        Rendering.renderChildren(context, columns);
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        undeclareContextVariables();
    }

    public List getChildrenForProcessing() {
        return columns.getChildren();
    }

    public Map getFacetsForProcessing() {
        return columns.getFacets();
    }

    @Override
    public void processDecodes(FacesContext context) {
        declareContextVariables();
        Collection<UIComponent> facets = getFacetCollection();
        for (UIComponent component : facets) {
            component.processDecodes(context);
        }
        undeclareContextVariables();
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
        declareContextVariables();
        Collection<UIComponent> facets = getFacetCollection();
        for (UIComponent component : facets) {
            component.processValidators(context);
        }
        undeclareContextVariables();
    }

    @Override
    public void processUpdates(FacesContext context) {
        declareContextVariables();
        Collection<UIComponent> facets = getFacetCollection();
        for (UIComponent component : facets) {
            component.processUpdates(context);
        }
        undeclareContextVariables();
    }

}
