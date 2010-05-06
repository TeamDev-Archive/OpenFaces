/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.tomahawk;

import org.apache.myfaces.custom.tree.DefaultMutableTreeNode;
import org.apache.myfaces.custom.tree.model.DefaultTreeModel;

import java.io.Serializable;

/**
 * <p>
 * Bean holding the tree hierarchy.
 * </p>
 *
 * @author <a href="mailto:dlestrat@apache.org">David Le Strat</a>
 */
public class TreeTableBean implements Serializable {
    /**
     * serial id for serialisation versioning
     */
    private static final long serialVersionUID = 1L;

    private DefaultTreeModel treeModel;

    /**
     * @param treeModel The treeModel.
     */
    public TreeTableBean(DefaultTreeModel treeModel) {
        this.treeModel = treeModel;
    }

    /**
     * <p>
     * Default constructor.
     * </p>
     */
    public TreeTableBean() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new TreeItem(1, "XY", "9001", "XY 9001"));
        DefaultMutableTreeNode a = new DefaultMutableTreeNode(new TreeItem(2, "A", "9001", "A 9001"));
        root.insert(a);
        DefaultMutableTreeNode b = new DefaultMutableTreeNode(new TreeItem(3, "B", "9001", "B 9001"));
        root.insert(b);
        DefaultMutableTreeNode c = new DefaultMutableTreeNode(new TreeItem(4, "C", "9001", "C 9001"));
        root.insert(c);

        DefaultMutableTreeNode node = new DefaultMutableTreeNode(new TreeItem(5, "a1", "9002", "a1 9002"));
        a.insert(node);
        node = new DefaultMutableTreeNode(new TreeItem(6, "a2", "9002", "a2 9002"));
        a.insert(node);
        node = new DefaultMutableTreeNode(new TreeItem(7, "a3", "9002", "a3 9002"));
        a.insert(node);
        node = new DefaultMutableTreeNode(new TreeItem(8, "b", "9002", "b 9002"));
        b.insert(node);

        a = node;
        node = new DefaultMutableTreeNode(new TreeItem(9, "x1", "9003", "x1 9003"));
        a.insert(node);
        node = new DefaultMutableTreeNode(new TreeItem(9, "x2", "9003", "x2 9003"));
        a.insert(node);

        this.treeModel = new DefaultTreeModel(root);
    }

    /**
     * @return Returns the treeModel.
     */
    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    /**
     * @param treeModel The treeModel to set.
     */
    public void setTreeModel(DefaultTreeModel treeModel) {
        this.treeModel = treeModel;
    }
}
