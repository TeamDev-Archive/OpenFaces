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

import org.openfaces.util.ValueBindings;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class TreeNode extends UIOutput {
    public static final String COMPONENT_TYPE = "org.openfaces.TreeNode";
    public static final String COMPONENT_FAMILY = "org.openfaces.TreeNode";

    private Object nodeKey;

    public TreeNode() {
        setRendererType(null);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, nodeKey};
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        super.restoreState(context, stateArray[0]);
        nodeKey = stateArray[1];
    }

    public Object getNodeKey() {
        Object nodeKey = ValueBindings.get(this, "nodeKey", this.nodeKey, Object.class);
        if (nodeKey != null)
            return nodeKey;
        else
            return getValue();
    }

    public void setNodeKey(Object nodeKey) {
        this.nodeKey = nodeKey;
    }
}
