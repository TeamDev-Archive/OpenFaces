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
package org.openfaces.taglib.jsp.table;

import org.openfaces.taglib.internal.table.CheckboxColumnTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class CheckboxColumnJspTag extends BaseColumnJspTag {

    public CheckboxColumnJspTag() {
        super(new CheckboxColumnTag());
    }

    public void setRowIndexes(ValueExpression rowIndexes) {
        getDelegate().setPropertyValue("rowIndexes", rowIndexes);
    }

    public void setRowDatas(ValueExpression rowDatas) {
        getDelegate().setPropertyValue("rowDatas", rowDatas);
    }

    public void setNodePaths(ValueExpression nodePaths) {
        getDelegate().setPropertyValue("nodePaths", nodePaths);
    }

    public void setSortable(ValueExpression sortable) {
        getDelegate().setPropertyValue("sortable", sortable);
    }

}
