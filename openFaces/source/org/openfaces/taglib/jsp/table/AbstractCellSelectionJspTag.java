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

import org.openfaces.taglib.internal.table.AbstractCellSelectionTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

/**
 * @author andrii.loboda
 */
public abstract class AbstractCellSelectionJspTag extends AbstractTableSelectionJspTag {
    protected AbstractCellSelectionJspTag(AbstractCellSelectionTag delegate) {
        super(delegate);
    }

    public void setCellSelectable(MethodExpression cellSelectable) {
        getDelegate().setPropertyValue("cellSelectable", cellSelectable);
    }

    public void setSelectableCells(MethodExpression selectableCells) {
        getDelegate().setPropertyValue("selectableCells", selectableCells);
    }

    public void setCursorStyle(ValueExpression cursorStyle) {
        getDelegate().setPropertyValue("cursorStyle", cursorStyle);
    }

    public void setCursorClass(ValueExpression cursorClass) {
        getDelegate().setPropertyValue("cursorClass", cursorClass);
    }

    public void setFillDirection(ValueExpression fillDirection) {
        getDelegate().setPropertyValue("fillDirection", fillDirection);
    }
}
