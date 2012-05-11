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

import org.openfaces.util.ValueBindings;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class SingleNodeSelection extends TreeTableSelection {
    public static final String COMPONENT_TYPE = "org.openfaces.SingleNodeSelection";
    public static final String COMPONENT_FAMILY = "org.openfaces.SingleNodeSelection";

    private static final String NODE_DATA_PROPERTY = "nodeData";
    //  private static final String NODE_KEY_PROPERTY = "nodeKey";
    //  private static final String NODE_KEY_PATH_PROPERTY = "nodeKeyPath";
    private static final String NODE_PATH_PROPERTY = "nodePath";

    private Object nodeData;
    private Object nodeKey;
    private TreePath nodePath;
    private TreePath nodeKeyPath;

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void setNodeData(Object nodeData) {
        this.nodeData = nodeData;
        nodeKey = null;
        nodePath = null;
        nodeKeyPath = null;
    }

    protected void setNodeKey(Object nodeKey) {
        this.nodeKey = nodeKey;
        nodeData = null;
        nodePath = null;
        nodeKeyPath = null;
    }

    public void setNodePath(TreePath nodePath) {
        this.nodePath = nodePath;
        nodeKey = null;
        nodeData = null;
        nodeKeyPath = null;
    }

    protected void setNodeKeyPath(TreePath nodeKeyPath) {
        this.nodeKeyPath = nodeKeyPath;
        nodeKey = null;
        nodeData = null;
        nodePath = null;
    }

    protected Object getNodeKey() {
        if (nodeKey != null)
            return nodeKey;
        if (nodeData != null)
            return getNodeKeyByData(nodeData);
        if (nodeKeyPath != null)
            return nodeKeyPath.getValue();
        if (nodePath != null)
            return getNodeKeyByData(nodePath.getValue());

        return null;
    }

    public Object getNodeData() {
        if (nodeData != null)
            return nodeData;
        if (nodeKey != null)
            return getNodeDataByKey(nodeKey);
        if (nodePath != null)
            return nodePath.getValue();
        if (nodeKeyPath != null)
            return getNodeDataByKey(nodeKeyPath.getValue());
        return null;
    }

    public TreePath getNodePath() {
        if (nodePath != null)
            return nodePath;
        if (nodeData != null)
            return getNodePathByData(nodeData);
        if (nodeKey != null)
            return getNodePathByKey(nodeKey);
        if (nodeKeyPath != null)
            return getNodePathByKeyPath(nodeKeyPath);
        return null;
    }

    protected TreePath getNodeKeyPath() {
        if (nodeKeyPath != null)
            return nodeKeyPath;
        if (nodeKey != null)
            return getKeyPathByKey(nodeKey);
        if (nodeData != null)
            return getKeyPathByData(nodeData);
        if (nodePath != null)
            return getKeyPathByNodePath(nodePath);
        return null;
    }

    @Override
    public Object saveState(FacesContext context) {
        rememberByKeys();
        Object superState = super.saveState(context);
        return new Object[]{superState, nodeKeyPath};
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        super.restoreState(context, stateArray[0]);
        setNodeKeyPath((TreePath) stateArray[1]);
    }


    public void rememberByKeys() {
        setNodeKeyPath(getNodeKeyPath());
    }

    protected void readSelectionFromBinding() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

//        ValueExpression selectedKeyPath = null;//getValueExpression(NODE_KEY_PATH_PROPERTY);
        ELContext elContext = facesContext.getELContext();
//        if (selectedKeyPath != null) {
//            setNodeKeyPath((TreePath) selectedKeyPath.getValue(elContext));
//        } else {
        ValueExpression selectedNodePath = getValueExpression(NODE_PATH_PROPERTY);
        if (selectedNodePath != null) {
            setNodePath((TreePath) selectedNodePath.getValue(elContext));
        } else {
//                ValueExpression selectedNodeKey = null;//getValueExpression(NODE_KEY_PROPERTY);
//                if (selectedNodeKey != null) {
//                    setNodeKey(selectedNodeKey.getValue(elContext));
//                } else {
            ValueExpression selectedNodeData = getValueExpression(NODE_DATA_PROPERTY);
            if (selectedNodeData != null)
                setNodeData(selectedNodeData.getValue(elContext));
//                }
        }
//        }
    }

    @Override
    protected void writeSelectionToBinding() {
//    setBoundPropertyValue(this, NODE_KEY_PROPERTY, getNodeKey());
        ValueBindings.set(this, NODE_DATA_PROPERTY, getNodeData());
        ValueBindings.set(this, NODE_PATH_PROPERTY, getNodePath());
//    setBoundPropertyValue(this, NODE_KEY_PATH_PROPERTY, getNodeKeyPath());
    }

    @Override
    public Mode getSelectionMode() {
        return Mode.SINGLE;
    }

    @Override
    protected List<Integer> encodeSelectionIntoIndexes() {
        int rowIndex = getRowIndexByNodeKeyPath(getNodeKeyPath());
        return Collections.singletonList(rowIndex);
    }

    @Override
    protected void decodeSelectionFromIndexes(List<?> indexes) {
        Integer index = (Integer) indexes.get(0);
        setNodeKeyPath(getNodeKeyPathByRowIndex(index));
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    @Override
    public List<TreePath> getSelectedNodeKeyPaths() {
        TreePath path = getNodeKeyPath();
        List<TreePath> emptyPath = Collections.emptyList();
        return path != null ? Collections.singletonList(path) : emptyPath;
    }
}
