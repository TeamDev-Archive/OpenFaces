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

import org.openfaces.taglib.internal.table.DynamicTreeStructureTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class DynamicTreeStructureJspTag extends AbstractComponentJspTag {

    public DynamicTreeStructureJspTag() {
        super(new DynamicTreeStructureTag());
    }

    public void setNodeKey(ValueExpression nodeKey) {
        getDelegate().setPropertyValue("nodeKey", nodeKey);
    }

    public void setNodeChildren(ValueExpression nodeChildren) {
        getDelegate().setPropertyValue("nodeChildren", nodeChildren);
    }

    public void setNodeHasChildren(ValueExpression nodeHasChildren) {
        getDelegate().setPropertyValue("nodeHasChildren", nodeHasChildren);
    }

}
