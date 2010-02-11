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
package org.openfaces.taglib.jsp;

import org.openfaces.taglib.internal.AbstractComponentTag;
import org.openfaces.taglib.jsp.window.PopupLayerJspTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class AbstractWindowJspTag extends PopupLayerJspTag {
    public AbstractWindowJspTag(AbstractComponentTag delegate) {
        super(delegate);
    }

    public void setDraggableByContent(ValueExpression value) {
        getDelegate().setPropertyValue("draggableByContent", value);
    }

    public void setCaption(ValueExpression caption) {
        getDelegate().setPropertyValue("caption", caption);
    }

    public void setCaptionStyle(ValueExpression captionStyle) {
        getDelegate().setPropertyValue("captionStyle", captionStyle);
    }

    public void setCaptionClass(ValueExpression captionClass) {
        getDelegate().setPropertyValue("captionClass", captionClass);
    }

    public void setContentStyle(ValueExpression contentStyle) {
        getDelegate().setPropertyValue("contentStyle", contentStyle);
    }

    public void setContentClass(ValueExpression contentClass) {
        getDelegate().setPropertyValue("contentClass", contentClass);
    }

    public void setMinWidth(ValueExpression width) {
        getDelegate().setPropertyValue("minWidth", width);
    }

    public void setMinHeight(ValueExpression height) {
        getDelegate().setPropertyValue("minHeight", height);
    }

}
