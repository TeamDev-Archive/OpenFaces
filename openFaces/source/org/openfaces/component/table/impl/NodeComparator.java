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

package org.openfaces.component.table.impl;

import org.openfaces.component.table.TreeTable;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.util.Comparator;
import java.util.Map;

/**
* @author Dmitry Pikhulya
*/
public class NodeComparator extends RowComparator {

    public NodeComparator(
            TreeTable treeTable,
            FacesContext facesContext,
            ValueExpression sortingExpression,
            Comparator<Object> sortingComparator,
            Map<String, Object> requestMap,
            boolean sortAscending) {
        super(treeTable, facesContext, sortingExpression, sortingComparator, requestMap, sortAscending);
    }

}
