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
package org.openfaces.taglib.jsp.table;

import org.openfaces.taglib.internal.table.CellTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class CellJspTag extends AbstractComponentJspTag {

    public CellJspTag() {
        super(new CellTag());
    }

    public void setColumnIds(ValueExpression column) {
        getDelegate().setPropertyValue("columnIds", column);
    }

    public void setCondition(ValueExpression column) {
        getDelegate().setPropertyValue("condition", column);
    }

    public void setSpan(ValueExpression span) {
        getDelegate().setPropertyValue("span", span);
    }


}
