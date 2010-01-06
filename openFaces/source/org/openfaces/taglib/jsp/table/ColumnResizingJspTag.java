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
        getDelegate().setPropertyValue("reszingState", resizingState);
    }


    public void setRolloverSeparatorStyle(ValueExpression rolloverSeparatorStyle) {
        getDelegate().setPropertyValue("rolloverSeparatorStyle", rolloverSeparatorStyle);
    }

    public void setRolloverSeparatorClass(ValueExpression rolloverSeparatorClass) {
        getDelegate().setPropertyValue("rolloverSeparatorClass", rolloverSeparatorClass);
    }

    public void setRolloverSeparatorHeaderStyle(ValueExpression rolloverSeparatorHeaderStyle) {
        getDelegate().setPropertyValue("rolloverSeparatorHeaderStyle", rolloverSeparatorHeaderStyle);
    }

    public void setRolloverSeparatorHeaderClass(ValueExpression rolloverSeparatorHeaderClass) {
        getDelegate().setPropertyValue("rolloverSeparatorHeaderClass", rolloverSeparatorHeaderClass);
    }

    public void setDraggedSeparatorStyle(ValueExpression draggedSeparatorStyle) {
        getDelegate().setPropertyValue("draggedSeparatorStyle", draggedSeparatorStyle);
    }

    public void setDraggedSeparatorClass(ValueExpression draggedSeparatorClass) {
        getDelegate().setPropertyValue("draggedSeparatorClass", draggedSeparatorClass);
    }

    public void setDraggedSeparatorHeaderStyle(ValueExpression draggedSeparatorHeaderStyle) {
        getDelegate().setPropertyValue("draggedSeparatorHeaderStyle", draggedSeparatorHeaderStyle);
    }

    public void setDraggedSeparatorHeaderClass(ValueExpression draggedSeparatorHeaderClass) {
        getDelegate().setPropertyValue("draggedSeparatorHeaderClass", draggedSeparatorHeaderClass);
    }


}
