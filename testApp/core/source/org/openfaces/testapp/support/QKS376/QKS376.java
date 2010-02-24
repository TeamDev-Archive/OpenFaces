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

package org.openfaces.testapp.support.QKS376;

import org.openfaces.util.Faces;
import org.openfaces.component.table.ExpansionState;
import org.openfaces.component.table.SeveralLevelsExpanded;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QKS376 {
    private List<TreeTableItems> root = new ArrayList<TreeTableItems>();
    private ExpansionState treeTableExpansionState = new SeveralLevelsExpanded(0);


    public QKS376() {
        root.add(new TreeTableItems(false, "Name1", Arrays.asList(new TreeTableItems(false, "Name2", Arrays.asList(new TreeTableItems(false, "Name3", null),
                new TreeTableItems(false, "Name4", Arrays.asList(new TreeTableItems(false, "Name4", null),
                        new TreeTableItems(false, "Name5", null))))))));
        root.add(new TreeTableItems(false, "Name6", Arrays.asList(new TreeTableItems(false, "Name7", Arrays.asList(new TreeTableItems(false, "Name8", null),
                new TreeTableItems(false, "Name9", Arrays.asList(new TreeTableItems(false, "Name14", null),
                        new TreeTableItems(false, "Name15", null))))))));
        root.add(new TreeTableItems(false, "Name16", Arrays.asList(new TreeTableItems(false, "Name26", Arrays.asList(new TreeTableItems(false, "Name36", null),
                new TreeTableItems(false, "Name46", Arrays.asList(new TreeTableItems(false, "Name46", null),
                        new TreeTableItems(false, "Name56", null))))))));
    }


    public List getRoot() {
        return root;
    }

    public ExpansionState getTreeTableExpansionState() {
        return treeTableExpansionState;
    }


    public void setTreeTableExpansionState(ExpansionState treeTableExpansionState) {
        this.treeTableExpansionState = treeTableExpansionState;
    }

    public List getNodeChildren() {
        TreeTableItems tree = (TreeTableItems) Faces.var("tree");
        return tree != null ? tree.getReplies() : root;
    }

    public String updateTreeTable() {
        TreeTableItems tree = (TreeTableItems) Faces.var("tree");
        boolean checked = tree.isChecked();
        if (tree.getReplies() != null) {
            recursiveUpdate(tree, checked);
        }
        return null;
    }

    public void recursiveUpdate(TreeTableItems item, boolean checked) {
        if (item.getReplies() != null) {
            for (Object o : item.getReplies()) {
                            TreeTableItems child = (TreeTableItems) o;
                            child.setChecked(checked);
                            recursiveUpdate(child, checked);
                        }
        }
    }
}
