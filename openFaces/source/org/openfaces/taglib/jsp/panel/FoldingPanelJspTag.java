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
package org.openfaces.taglib.jsp.panel;

import org.openfaces.taglib.internal.panel.FoldingPanelTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Kharchenko
 */
public class FoldingPanelJspTag extends AbstractComponentJspTag {

    public FoldingPanelJspTag() {
        super(new FoldingPanelTag());
    }

    public void setExpanded(ValueExpression expanded) {
        getDelegate().setPropertyValue("expanded", expanded);
    }

    public void setCaptionStyle(ValueExpression captionStyle) {
        getDelegate().setPropertyValue("captionStyle", captionStyle);
    }

    public void setContentStyle(ValueExpression contentStyle) {
        getDelegate().setPropertyValue("contentStyle", contentStyle);
    }

    public void setOnstatechange(ValueExpression onstatechange) {
        getDelegate().setPropertyValue("onstatechange", onstatechange);
    }

    public void setCaptionClass(ValueExpression captionClass) {
        getDelegate().setPropertyValue("captionClass", captionClass);
    }

    public void setContentClass(ValueExpression contentClass) {
        getDelegate().setPropertyValue("contentClass", contentClass);
    }

    public void setFoldingDirection(ValueExpression foldingDirection) {
        getDelegate().setPropertyValue("foldingDirection", foldingDirection);
    }

    public void setLoadingMode(ValueExpression loadingMode) {
        getDelegate().setPropertyValue("loadingMode", loadingMode);
    }

    public void setFocusable(ValueExpression focusable) {
        getDelegate().setPropertyValue("focusable", focusable);
    }

    public void setFocusedCaptionStyle(ValueExpression focusedCaptionStyle) {
        getDelegate().setPropertyValue("focusedCaptionStyle", focusedCaptionStyle);
    }

    public void setFocusedCaptionClass(ValueExpression focusedCaptionClass) {
        getDelegate().setPropertyValue("focusedCaptionClass", focusedCaptionClass);
    }

    public void setFocusedContentStyle(ValueExpression focusedContentStyle) {
        getDelegate().setPropertyValue("focusedContentStyle", focusedContentStyle);
    }

    public void setFocusedContentClass(ValueExpression focusedContentClass) {
        getDelegate().setPropertyValue("focusedContentClass", focusedContentClass);
    }

}
