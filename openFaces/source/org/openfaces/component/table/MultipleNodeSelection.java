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

import org.openfaces.renderkit.TableUtil;
import org.openfaces.renderkit.table.AbstractTableRenderer;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.ValueBindings;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class MultipleNodeSelection extends TreeTableSelection {
    public static final String COMPONENT_TYPE = "org.openfaces.MultipleNodeSelection";
    public static final String COMPONENT_FAMILY = "org.openfaces.MultipleNodeSelection";

    private static final String NODE_DATAS_PROPERTY = "nodeDatas";
    //  private static final String NODE_KEYS_PROPERTY = "nodeKeys";
    private static final String NODE_PATHS_PROPERTY = "nodePaths";
//  private static final String NODE_KEY_PATHS_PROPERTY = "nodeKeyPaths";

    private List<Object> nodeDatas;
    private List<Object> nodeKeys;
    private List<TreePath> nodePaths;
    private List<TreePath> nodeKeyPaths;

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void setNodeDatas(List<Object> nodeDatas) {
        this.nodeDatas = nodeDatas;
        nodeKeys = null;
        nodePaths = null;
        nodeKeyPaths = null;
    }

    protected void setNodeKeys(List<Object> nodeKeys) {
        this.nodeKeys = nodeKeys;
        nodeDatas = null;
        nodePaths = null;
        nodeKeyPaths = null;
    }

    public void setNodePaths(List<TreePath> nodePaths) {
        this.nodePaths = nodePaths;
        nodeKeyPaths = null;
        nodeDatas = null;
        nodeKeys = null;
    }

    protected void setNodeKeyPaths(List<TreePath> nodeKeyPaths) {
        this.nodeKeyPaths = nodeKeyPaths;
        nodePaths = null;
        nodeDatas = null;
        nodeKeys = null;
    }

    protected List getNodeKeys() {
        if (nodeKeys != null)
            return nodeKeys;
        if (nodeDatas != null) {
            int nodeDataCount = nodeDatas.size();
            List<Object> nodeKeys = new ArrayList<Object>(nodeDataCount);
            for (int i = 0; i < nodeDataCount; i++) {
                Object nodeData = nodeDatas.get(i);
                Object nodeKeyByData = getNodeKeyByData(nodeData);
                if (nodeKeyByData != null)
                    nodeKeys.add(nodeKeyByData);
            }

            return nodeKeys;
        }
        if (nodeKeyPaths != null) {
            int keyPathCount = nodeKeyPaths.size();
            List<Object> result = new ArrayList<Object>(keyPathCount);
            for (int i = 0; i < keyPathCount; i++) {
                TreePath keyPath = nodeKeyPaths.get(i);
                result.add(keyPath.getValue());
            }
            return result;
        }
        if (nodePaths != null) {
            int nodePathCount = nodePaths.size();
            List<Object> result = new ArrayList<Object>(nodePathCount);
            for (int i = 0; i < nodePathCount; i++) {
                TreePath nodePath = nodePaths.get(i);
                TreePath keyPath = getKeyPathByNodePath(nodePath);
                if (keyPath != null)
                    result.add(keyPath.getValue());
            }
            return result;
        }
        return Collections.EMPTY_LIST;
    }

    public List<Object> getNodeDatas() {
        if (nodeDatas != null)
            return nodeDatas;
        if (nodeKeys != null) {
            int nodeKeyCount = nodeKeys.size();
            List<Object> nodeDatas = new ArrayList<Object>(nodeKeyCount);
            for (int i = 0; i < nodeKeyCount; i++) {
                Object nodeKey = nodeKeys.get(i);
                Object nodeDataById = getNodeDataByKey(nodeKey);
                if (nodeDataById != null)
                    nodeDatas.add(nodeDataById);
            }
            return nodeDatas;
        }
        if (nodePaths != null) {
            int nodePathCount = nodePaths.size();
            List<Object> result = new ArrayList<Object>(nodePathCount);
            for (int i = 0; i < nodePathCount; i++) {
                TreePath nodePath = nodePaths.get(i);
                result.add(nodePath.getValue());
            }
            return result;
        }
        if (nodeKeyPaths != null) {
            int keyPathCount = nodeKeyPaths.size();
            List<Object> result = new ArrayList<Object>(keyPathCount);
            for (int i = 0; i < keyPathCount; i++) {
                TreePath keyPath = nodeKeyPaths.get(i);
                TreePath nodePath = getNodePathByKeyPath(keyPath);
                if (nodePath != null)
                    result.add(nodePath.getValue());
            }
            return result;
        }
        return Collections.emptyList();
    }

    public List<TreePath> getNodePaths() {
        if (nodePaths != null)
            return nodePaths;
        if (nodeKeyPaths != null) {
            int keyPathCount = nodeKeyPaths.size();
            List<TreePath> result = new ArrayList<TreePath>(keyPathCount);
            for (int i = 0; i < keyPathCount; i++) {
                TreePath keyPath = nodeKeyPaths.get(i);
                TreePath nodePath = getNodePathByKeyPath(keyPath);
                if (nodePath != null)
                    result.add(nodePath);
            }
            return result;
        }
        if (nodeDatas != null) {
            int nodeDataCount = nodeDatas.size();
            List<TreePath> result = new ArrayList<TreePath>(nodeDataCount);
            for (int i = 0; i < nodeDataCount; i++) {
                Object nodeData = nodeDatas.get(i);
                TreePath nodePath = getNodePathByData(nodeData);
                if (nodePath != null)
                    result.add(nodePath);
            }
            return result;
        }
        if (nodeKeys != null) {
            int nodeKeyCount = nodeKeys.size();
            List<TreePath> result = new ArrayList<TreePath>(nodeKeyCount);
            for (int i = 0; i < nodeKeyCount; i++) {
                Object nodeKey = nodeKeys.get(i);
                TreePath nodePath = getNodePathByKey(nodeKey);
                if (nodePath != null)
                    result.add(nodePath);
            }
            return result;
        }
        return Collections.emptyList();
    }

    protected List<TreePath> getNodeKeyPaths() {
        if (nodeKeyPaths != null)
            return nodeKeyPaths;
        if (nodePaths != null) {
            int nodePathCount = nodePaths.size();
            List<TreePath> result = new ArrayList<TreePath>(nodePathCount);
            for (int i = 0; i < nodePathCount; i++) {
                TreePath nodePath = nodePaths.get(i);
                TreePath keyPath = getKeyPathByNodePath(nodePath);
                if (keyPath != null)
                    result.add(keyPath);
            }
            return result;
        }
        if (nodeDatas != null) {
            int nodeDataCount = nodeDatas.size();
            List<TreePath> result = new ArrayList<TreePath>(nodeDataCount);
            for (int i = 0; i < nodeDataCount; i++) {
                Object nodeData = nodeDatas.get(i);
                TreePath keyPath = getKeyPathByData(nodeData);
                if (keyPath != null)
                    result.add(keyPath);
            }
            return result;
        }
        if (nodeKeys != null) {
            int nodeKeyCount = nodeKeys.size();
            List<TreePath> result = new ArrayList<TreePath>(nodeKeyCount);
            for (int i = 0; i < nodeKeyCount; i++) {
                Object nodeKey = nodeKeys.get(i);
                TreePath keyPath = getKeyPathByKey(nodeKey);
                if (keyPath != null)
                    result.add(keyPath);
            }
            return result;
        }
        return Collections.emptyList();
    }

    public Object saveState(FacesContext context) {
        rememberByKeys();
        Object superState = super.saveState(context);
        return new Object[]{superState, UIComponentBase.saveAttachedState(context, nodeKeyPaths)};
    }

    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        super.restoreState(context, stateArray[0]);
        setNodeKeyPaths((List<TreePath>) UIComponentBase.restoreAttachedState(context, stateArray[1]));
    }


    public void rememberByKeys() {
        setNodeKeyPaths(getNodeKeyPaths());
    }

    protected void readSelectionFromBinding() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

//        ValueExpression selectedNodeKeyPaths = null;//getValueExpression(NODE_KEY_PATHS_PROPERTY);
        ELContext elContext = facesContext.getELContext();
//        if (selectedNodeKeyPaths != null) {
//            setNodeKeyPaths((List<TreePath>) selectedNodeKeyPaths.getValue(elContext));
//        } else {
        ValueExpression selectedNodePaths = getValueExpression(NODE_PATHS_PROPERTY);
        if (selectedNodePaths != null) {
            setNodePaths(objectToList(selectedNodePaths.getValue(elContext), NODE_PATHS_PROPERTY));
        } else {
//                ValueExpression selectedNodeKeys = null;//getValueExpression(NODE_KEYS_PROPERTY);
//                if (selectedNodeKeys != null) {
//                    setNodeKeys((List<Object>) selectedNodeKeys.getValue(elContext));
//                } else {
            ValueExpression selectedNodeDatas = getValueExpression(NODE_DATAS_PROPERTY);
            if (selectedNodeDatas != null) {
                setNodeDatas(objectToList(selectedNodeDatas.getValue(elContext), NODE_DATAS_PROPERTY));
            }
//                }
        }
//        }
    }

    protected void writeSelectionToBinding() {
//    setBoundPropertyValue(this, NODE_KEYS_PROPERTY, getNodeKeys());
        ValueBindings.setFromList(this, NODE_DATAS_PROPERTY, getNodeDatas());
//    setBoundPropertyValue(this, NODE_KEY_PATHS_PROPERTY, getNodeKeyPaths());
        ValueBindings.setFromList(this, NODE_PATHS_PROPERTY, getNodePaths());
    }

    public boolean isMultipleSelectionAllowed() {
        return true;
    }

    public void encodeOnAjaxNodeFolding(FacesContext context) throws IOException {
        AbstractTable table = getTable();

        List<Integer> selectedRowIndexes = encodeSelectionIntoIndexes();
        ScriptBuilder buf = new ScriptBuilder().functionCall("O$.TreeTable._setSelectedNodeIndexes",
                table,
                selectedRowIndexes).semicolon();

        Rendering.renderInitScript(context, buf,
                Resources.getUtilJsURL(context),
                TableUtil.getTableUtilJsURL(context),
                AbstractTableRenderer.getTableJsURL(context));
    }

    public List<TreePath> getSelectedNodeKeyPaths() {
        return getNodeKeyPaths();
    }

    protected List<Integer> encodeSelectionIntoIndexes() {
        List<TreePath> keyPaths = getNodeKeyPaths();
        List<Integer> rowIndexes;
        if (keyPaths != null) {
            int keyPathCount = keyPaths.size();
            rowIndexes = new ArrayList<Integer>(keyPathCount);
            for (int i = 0; i < keyPathCount; i++) {
                TreePath keyPath = keyPaths.get(i);
                int rowIndex = getRowIndexByNodeKeyPath(keyPath);
                if (rowIndex != -1)
                    rowIndexes.add(rowIndex);
            }
        } else
            rowIndexes = Collections.emptyList();

        return rowIndexes;
    }

    protected void decodeSelectionFromIndexes(List<Integer> indexes) {
        List<TreePath> allSelectedNodeKeys = (getNodeKeyPaths() != null)
                ? getNodeKeyPaths() : new ArrayList<TreePath>();

        List<TreePath> selectedKeyPaths = new ArrayList<TreePath>();
        for (Integer index : indexes) {
            TreePath keyPath = getNodeKeyPathByRowIndex(index);
            if (keyPath != null)
                selectedKeyPaths.add(keyPath);
        }

        processSelectedKeyPaths(selectedKeyPaths, allSelectedNodeKeys);

        setNodeKeyPaths(allSelectedNodeKeys);
    }

    private void processSelectedKeyPaths(List<TreePath> selectedKeyPaths, List<TreePath> allSelectedNodeKeys) {
        int rowCount = getModel().getRowCount();
        for (int i = 0; i < rowCount; i++) {
            getModel().setRowIndex(i);
            TreePath keyPath = getNodeKeyPathByRowIndex(i);
            if (keyPath != null) {
                if (!selectedKeyPaths.contains(keyPath)) {
                    if (allSelectedNodeKeys.contains(keyPath)) {
                        // Remove nodeKeyPath , because current node was unselected
                        allSelectedNodeKeys.remove(keyPath);
                    }
                } else {
                    if (!allSelectedNodeKeys.contains(keyPath)) {
                        // Add new nodeKeyPath   , because node was selected
                        allSelectedNodeKeys.add(keyPath);
                    }
                }
            }
        }
    }


}
