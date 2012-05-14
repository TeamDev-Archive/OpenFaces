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

import org.openfaces.taglib.internal.table.ColumnResizingTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class ColumnResizingJspTag extends AbstractComponentJspTag {

    public ColumnResizingJspTag() {
        super(new ColumnResizingTag());
    }

    public void setEnabled(ValueExpression enabled) {
        getDelegate().setPropertyValue("enabled", enabled);
    }

    public void setRetainTableWidth(ValueExpression retainTableWidth) {
        getDelegate().setPropertyValue("retainTableWidth", retainTableWidth);
    }

    public void setMinColWidth(ValueExpression minColWidth) {
        getDelegate().setPropertyValue("minColWidth", minColWidth);
    }

    public void setResizeHandleWidth(ValueExpression resizeHandleWidth) {
        getDelegate().setPropertyValue("resizeHandleWidth", resizeHandleWidth);
    }

    public void setResizingState(ValueExpression resizingState) {
        getDelegate().setPropertyValue("resizingState", resizingState);
    }

    public void setAutoSaveState(ValueExpression autoSaveState) {
        getDelegate().setPropertyValue("autoSaveState", autoSaveState);
    }
}
