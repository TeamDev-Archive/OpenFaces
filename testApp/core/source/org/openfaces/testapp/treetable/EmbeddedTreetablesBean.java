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
package org.openfaces.testapp.treetable;

import org.openfaces.util.FacesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Dmitry Pikhulya
 */
public class EmbeddedTreetablesBean {

    private List<TreeTableData> treeTables;

    public EmbeddedTreetablesBean() {
        treeTables = new ArrayList<TreeTableData>();
        for (int i = 0; i < 5; i++)
            treeTables.add(new TreeTableData());
    }

    public List<TreeTableData> getTreeTables() {
        return treeTables;
    }

    public static class TreeTableData {
        private List<TreeTableItem> rootNodes;
        private TreeTableItem selectedNode;
        private boolean expanded = true;

        public TreeTableData() {
            rootNodes = createNodeList(3);
        }

        private List<TreeTableItem> createNodeList(int subLevels) {
            List<TreeTableItem> nodes = new ArrayList<TreeTableItem>();
            Random random = new Random();
            int nodeCount = random.nextInt(5) + 1;
            for (int i = 0; i < nodeCount; i++) {
                TreeTableItem node = TreeTableItem.createRandom();
                if (subLevels != 0)
                    node.setChildren(createNodeList(subLevels - 1));
                nodes.add(node);
            }
            return nodes;
        }

        public List<TreeTableItem> getChildNodes() {
            TreeTableItem parent = (TreeTableItem) FacesUtil.var("node");
            if(parent == null)
                return rootNodes;
            else
                return parent.getChildren();
        }

        public boolean getNodeHasChildren() {
            List childNodes = getChildNodes();
            return childNodes != null && childNodes.size() > 0;
        }

        public TreeTableItem getSelectedNode() {
            return selectedNode;
        }

        public void setSelectedNode(TreeTableItem selectedNode) {
            this.selectedNode = selectedNode;
        }

        public boolean isExpanded() {
            return expanded;
        }

        public void setExpanded(boolean expanded) {
            this.expanded = expanded;
        }
    }

    public void testAction () {
        TreeTableItem node = (TreeTableItem) FacesUtil.var("node");
        System.out.println("Node name: " + node.getName());
    }

}
