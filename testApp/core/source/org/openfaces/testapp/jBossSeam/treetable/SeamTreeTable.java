/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.jBossSeam.treetable;

import org.openfaces.component.table.ExpansionState;
import org.openfaces.component.table.TextFilterCriterion;

import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
public interface SeamTreeTable {
    public List getNodeChildren();

    public ExpansionState getForumTreeTableExpansionState1();

    public void setForumTreeTableExpansionState1(ExpansionState forumTreeTableExpansionState1);

    public ExpansionState getForumTreeTableExpansionState2();

    public void setForumTreeTableExpansionState2(ExpansionState forumTreeTableExpansionState2);

    public ExpansionState getForumTreeTableExpansionState3();

    public void setForumTreeTableExpansionState3(ExpansionState forumTreeTableExpansionState3);

    public String getDateCategory();

    public TextFilterCriterion getFilterValue();

    public void setFilterValue(TextFilterCriterion filterValue);

    public String sortByFirstColumn();

    public TreeTableItem getSingleSelectionItem();

    public void setSingleSelectionItem(TreeTableItem singleSelectionItem);

    public List<TreeTableItem> getMultipleSelectionItems();

    public void setMultipleSelectionItems(List<TreeTableItem> multipleSelectionItems);

    public void destroy();
}
