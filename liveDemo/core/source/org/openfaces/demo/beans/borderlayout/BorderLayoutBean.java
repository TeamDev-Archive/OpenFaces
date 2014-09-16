/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.borderlayout;

import org.openfaces.component.table.AllNodesExpanded;
import org.openfaces.component.table.ExpansionState;
import org.openfaces.util.Faces;

import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BorderLayoutBean implements Serializable {

    public static enum ComponentLevel {
        ROOT, CATEGORY, COMPONENT
    }

    private static final String TREE_NODE_VAR = "treeNode";
    private static final String COMPONENTS_ROOT = "OpenFaces Components";

    private final Map<String, List<String>> componentsByCategories;

    private String selectedComponent;

	private ExpansionState expansionState = new AllNodesExpanded();


    public BorderLayoutBean() {
        List<String> componentsList = new ArrayList<String>();
        componentsList.add("Data Table");
        componentsList.add("Hint Label");
        componentsList.add("Tabbed Pane");

        List<String> utilityComponentsList = new ArrayList<String>();
        utilityComponentsList.add("Focus");
        utilityComponentsList.add("ScrollPosition");
        utilityComponentsList.add("LoadBundle");

        componentsByCategories = new HashMap<String, List<String>>();
        componentsByCategories.put("Components", componentsList);
        componentsByCategories.put("Utility Components", utilityComponentsList);
    }

    public String getSelectedComponent() {
        return selectedComponent;
    }

    public void setSelectedComponent(String selectedComponent) {
        this.selectedComponent = selectedComponent;
    }

    public boolean isMayBeSelected() {
        return  selectedComponent != null
                && !COMPONENTS_ROOT.equals(selectedComponent)
                && !componentsByCategories.keySet().contains(selectedComponent);
    }

    public String getNormalizedSelectedComponent() {
        return getSelectedComponent() == null ? null : getSelectedComponent().replace(" ", "");
    }

    public void setNormalizedSelectedComponent(String value) {
        //need this setted for using in inputHidden  
    }

	public ExpansionState getExpansionState() {
		return expansionState;
	}

	public void setExpansionState(ExpansionState expansionState) {
		this.expansionState = expansionState;
	}


	public List<String> getNodeChildren() {
		final Object treeNode = getTreeNode();
		final ComponentLevel level = getTreeLevel(treeNode);

		switch (level) {
			case ROOT:
                return Arrays.asList(COMPONENTS_ROOT);

			case CATEGORY:
                return new ArrayList<String>(componentsByCategories.keySet());

			case COMPONENT:
				final String category = (String) treeNode;
				return new ArrayList<String>(componentsByCategories.get(category));
		}

		return null;
	}

	private ComponentLevel getTreeLevel(Object treeNode) {

		if (treeNode == null) {
			return ComponentLevel.ROOT;
		}

        if (COMPONENTS_ROOT.equals(treeNode)) {
            return ComponentLevel.CATEGORY;
        }

        if (componentsByCategories.keySet().contains(treeNode)) {
            return ComponentLevel.COMPONENT;
        }

        return null;
	}

	private Object getTreeNode() {
		return Faces.var(TREE_NODE_VAR);
	}

	public boolean isNodeHasChildren() {
        Object node = getTreeNode();

		return COMPONENTS_ROOT.equals(node)
                || componentsByCategories.keySet().contains(node);
	}

    public boolean isComponentNode() {
        return getTreeLevel() == ComponentLevel.CATEGORY;
    }

	public ComponentLevel getTreeLevel() {
		return getTreeLevel(getTreeNode());
	}

    public String selectComponent(AjaxBehaviorEvent event) {
        
        if (COMPONENTS_ROOT.equals(selectedComponent)
                || componentsByCategories.keySet().contains(selectedComponent)) {
            this.selectedComponent = null;
        }

        return "success";
    }
    
}
