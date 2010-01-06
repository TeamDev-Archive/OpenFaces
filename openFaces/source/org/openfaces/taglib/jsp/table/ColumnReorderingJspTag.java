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

import org.openfaces.taglib.internal.table.ColumnReorderingTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

public class ColumnReorderingJspTag extends AbstractComponentJspTag {

    public ColumnReorderingJspTag() {
        super(new ColumnReorderingTag());
    }

    public void setDraggedCellStyle(ValueExpression draggedCellStyle) {
        getDelegate().setPropertyValue("draggedCellStyle", draggedCellStyle);
    }

    public void setDraggedCellClass(ValueExpression draggedCellClass) {
        getDelegate().setPropertyValue("draggedCellClass", draggedCellClass);
    }

    public void setDraggedCellTransparency(ValueExpression draggedCellTransparency) {
        getDelegate().setPropertyValue("draggedCellTransparency", draggedCellTransparency);
    }

    public void setAutoScrollAreaStyle(ValueExpression autoScrollAreaStyle) {
        getDelegate().setPropertyValue("autoScrollAreaStyle", autoScrollAreaStyle);
    }

    public void setAutoScrollAreaClass(ValueExpression autoScrollAreaClass) {
        getDelegate().setPropertyValue("autoScrollAreaClass", autoScrollAreaClass);
    }

    public void setAutoScrollAreaTransparency(ValueExpression autoScrollAreaTransparency) {
        getDelegate().setPropertyValue("autoScrollAreaTransparency", autoScrollAreaTransparency);
    }

    public void setAutoScrollLeftImageUrl(ValueExpression autoScrollLeftImageUrl) {
        getDelegate().setPropertyValue("autoScrollLeftImageUrl", autoScrollLeftImageUrl);
    }

    public void setAutoScrollRightImageUrl(ValueExpression autoScrollRightImageUrl) {
        getDelegate().setPropertyValue("autoScrollRightImageUrl", autoScrollRightImageUrl);
    }

    public void setDropTargetStyle(ValueExpression dropTargetStyle) {
        getDelegate().setPropertyValue("dropTargetStyle", dropTargetStyle);
    }

    public void setDropTargetClass(ValueExpression dropTargetClass) {
        getDelegate().setPropertyValue("dropTargetClass", dropTargetClass);
    }

    public void setDropTargetTopImageUrl(ValueExpression dropTargetTopImageUrl) {
        getDelegate().setPropertyValue("dropTargetTopImageUrl", dropTargetTopImageUrl);
    }

    public void setDropTargetBottomImageUrl(ValueExpression dropTargetBottomImageUrl) {
        getDelegate().setPropertyValue("dropTargetBottomImageUrl", dropTargetBottomImageUrl);
    }
}
