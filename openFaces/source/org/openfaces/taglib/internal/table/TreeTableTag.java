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
package org.openfaces.taglib.internal.table;

import org.openfaces.component.table.AllNodesCollapsed;
import org.openfaces.component.table.AllNodesExpanded;
import org.openfaces.component.table.AllNodesPreloaded;
import org.openfaces.component.table.NoNodesPreloaded;
import org.openfaces.component.table.SeveralLevelsExpanded;
import org.openfaces.component.table.SeveralLevelsPreloaded;
import org.openfaces.component.table.TreeTable;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public class TreeTableTag extends AbstractTableTag {
    private static final String LEVELS_EXPANDED_MODE = "levelsExpanded:";
    private static final String ALL_COLLAPSED_MODE = "allCollapsed";
    private static final String ALL_EXPANDED_MODE = "allExpanded";
    private static final String ALL_NODES_PRELOADED = "all";
    private static final String NO_NODES_PRELOADED = "none";
    private static final String LEVELS_PRELOADED = "levelsPreloaded:";

    public String getComponentType() {
        return TreeTable.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.TreeTableRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        TreeTable treeTable = ((TreeTable) component);

        setStringProperty(component, "nodeLevelVar");
        setStringProperty(component, "nodePathVar");
        setStringProperty(component, "nodeHasChildrenVar");
        setIntProperty(component, "sortLevel");
        setStringProperty(component, "filterAcceptedRowStyle");
        setStringProperty(component, "filterAcceptedRowClass");
        setStringProperty(component, "filterSubsidiaryRowStyle");
        setStringProperty(component, "filterSubsidiaryRowClass");
        setBooleanProperty(component, "foldingEnabled");
        String preloadedNodes = getPropertyValue("preloadedNodes");
        if (preloadedNodes != null)
            setPreloadedNodesProperty(facesContext, treeTable, preloadedNodes.trim());

        String expansionState = getPropertyValue("expansionState");
        if (expansionState != null)
            setExpansionStateProperty(facesContext, treeTable, expansionState.trim());

        setStringProperty(component, "textStyle");
        setStringProperty(component, "textClass");
    }

    private void setPreloadedNodesProperty(FacesContext facesContext, TreeTable treeTable, String preloadedNodes) {
        if (preloadedNodes.length() == 0)
            throw new IllegalArgumentException("Invalid value specified for preloadedNodes attribute: the value should not be empty");

        if (isValueReference(preloadedNodes)) {
            ValueExpression valueExpression = createValueExpression(facesContext, "preloadedNodes", preloadedNodes);
            treeTable.setValueExpression("preloadedNodes", valueExpression);
            return;
        }

        if (ALL_NODES_PRELOADED.equals(preloadedNodes))
            treeTable.setPreloadedNodes(new AllNodesPreloaded());
        else if (NO_NODES_PRELOADED.equals(preloadedNodes))
            treeTable.setPreloadedNodes(new NoNodesPreloaded());
        else if (preloadedNodes.startsWith(LEVELS_PRELOADED)) {
            String remainder = preloadedNodes.substring(LEVELS_PRELOADED.length());
            remainder = remainder.trim();
            int level;
            try {
                level = Integer.parseInt(remainder);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid level specified in preloadedNodes attribute. Integer number expected, but was: " + remainder);
            }
            if (level < 0)
                throw new IllegalArgumentException("Invalid level specified in preloadedNodes attribute. The number should be zero or a positive number: " + level);
            treeTable.setPreloadedNodes(new SeveralLevelsPreloaded(level));
        } else {
            throw new IllegalArgumentException("Invalid value specified for preloadedNodes attribute: " + preloadedNodes + " . It should be one of the following: " + ALL_NODES_PRELOADED + ", " + NO_NODES_PRELOADED + " or " + LEVELS_PRELOADED + "NUMBER");
        }

    }

    private void setExpansionStateProperty(FacesContext facesContext, TreeTable treeTable, String expansionState) {
        if (expansionState.length() == 0)
            throw new IllegalArgumentException("Invalid value specified for expansionState attribute: the value should not be empty");

        if (isValueReference(expansionState)) {
            ValueExpression ve = createValueExpression(facesContext, "expansionState", expansionState);
            treeTable.setValueExpression("expansionState", ve);
            return;
        }

        if (ALL_EXPANDED_MODE.equals(expansionState))
            treeTable.setExpansionState(new AllNodesExpanded());
        else if (ALL_COLLAPSED_MODE.equals(expansionState))
            treeTable.setExpansionState(new AllNodesCollapsed());
        else if (expansionState.startsWith(LEVELS_EXPANDED_MODE)) {
            String remainder = expansionState.substring(LEVELS_EXPANDED_MODE.length());
            remainder = remainder.trim();
            int level;
            try {
                level = Integer.parseInt(remainder);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid level specified in expansionState attribute. Integer number expected, but was: " + remainder);
            }
            if (level < 0)
                throw new IllegalArgumentException("Invalid level specified in expansionState attribute. The number should be zero or a positive number: " + level);
            treeTable.setExpansionState(new SeveralLevelsExpanded(level));
        } else {
            throw new IllegalArgumentException("Invalid value specified for expansionState attribute: " + expansionState + " . It should be one of the following: " + ALL_EXPANDED_MODE + ", " + ALL_COLLAPSED_MODE + " or " + LEVELS_EXPANDED_MODE + "NUMBER");
        }
    }
}
