/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.tomahawk;

import org.apache.myfaces.custom.tree2.HtmlTree;
import org.apache.myfaces.custom.tree2.TreeModel;
import org.apache.myfaces.custom.tree2.TreeModelBase;
import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeNodeBase;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import java.io.Serializable;

/**
 * @author Sean Schofield
 */
public class TreeBackerBean implements Serializable {
    
    private HtmlTree tree;

    private String nodePath;

    public TreeNode getTreeData() {
        TreeNode treeData = new TreeNodeBase("foo-folder", "Inbox", false);

// construct a set of fake data (normally your data would come from a database)

// populate Frank's portion of the tree
        TreeNodeBase personNode = new TreeNodeBase("person", "Frank Foo", false);
        personNode.getChildren().add(new TreeNodeBase("foo-folder", "Frank Foo Subnode", false));
        treeData.getChildren().add(personNode);

// populate Betty's portion of the tree
        personNode = new TreeNodeBase("person", "Betty Bar", false);
        personNode.getChildren().add(new TreeNodeBase("foo-folder", "Betty Bar Subnode", false));
        treeData.getChildren().add(personNode);

        return treeData;
    }

    /**
     * NOTE: This is just to show an alternative way of supplying tree data. You can supply either a
     * TreeModel or TreeNode.
     *
     * @return TreeModel
     */
    public TreeModel getExpandedTreeData() {
        return new TreeModelBase(getTreeData());
    }

    public void setTree(HtmlTree tree) {
        this.tree = tree;
    }

    public HtmlTree getTree() {
        return tree;
    }

    public String expandAll() {
        tree.expandAll();
        return null;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    public String getNodePath() {
        return nodePath;
    }

    public void checkPath(FacesContext context, UIComponent component, java.lang.Object value) {
// make sure path is valid (leaves cannot be expanded or renderer will complain)
        FacesMessage message = null;

        String[] path = tree.getPathInformation(value.toString());

        for (String nodeId : path) {
            try {
                tree.setNodeId(nodeId);
            } catch (Exception e) {
                throw new ValidatorException(message, e);
            }

            if (tree.getNode().isLeaf()) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Invalid node path (cannot expand a leaf): " + nodeId,
                        "Invalid node path (cannot expand a leaf): " + nodeId);
                throw new ValidatorException(message);
            }
        }
    }

    public void expandPath(ActionEvent event) {
        tree.expandPath(tree.getPathInformation(nodePath));
    }
}