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
package org.openfaces.taglib.jsp.panel;

import org.openfaces.taglib.internal.panel.FoldingPanelTag;

import javax.el.ValueExpression;

/**
 * @author Kharchenko
 */
public class FoldingPanelJspTag extends AbstractPanelWithCaptionJspTag {

    public FoldingPanelJspTag() {
        super(new FoldingPanelTag());
    }

    public void setExpanded(ValueExpression expanded) {
        getDelegate().setPropertyValue("expanded", expanded);
    }


    public void setOnstatechange(ValueExpression onstatechange) {
        getDelegate().setPropertyValue("onstatechange", onstatechange);
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

    public void setToggleOnCaptionClick(ValueExpression toggleOnCaptionClick) {
        getDelegate().setPropertyValue("toggleOnCaptionClick", toggleOnCaptionClick);
    }

    public void setRolloverTogglableCaptionStyle(ValueExpression rolloverTogglableCaptionStyle) {
        getDelegate().setPropertyValue("rolloverTogglableCaptionStyle", rolloverTogglableCaptionStyle);
    }

    public void setRolloverTogglableCaptionClass(ValueExpression rolloverTogglableCaptionClass) {
        getDelegate().setPropertyValue("rolloverTogglableCaptionClass", rolloverTogglableCaptionClass);
    }
}
