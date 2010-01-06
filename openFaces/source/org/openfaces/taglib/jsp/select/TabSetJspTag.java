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
package org.openfaces.taglib.jsp.select;

import org.openfaces.taglib.internal.select.TabSetTag;
import org.openfaces.taglib.jsp.UIInputJspTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

/**
 * @author Andrew Palval
 */
public class TabSetJspTag extends UIInputJspTag {

    public TabSetJspTag() {
        super(new TabSetTag());
    }

    public void setGapWidth(ValueExpression gapWidth) {
        getDelegate().setPropertyValue("gapWidth", gapWidth);
    }

    public void setFrontBorderStyle(ValueExpression frontBorderStyle) {
        getDelegate().setPropertyValue("frontBorderStyle", frontBorderStyle);
    }

    public void setBackBorderStyle(ValueExpression backBorderStyle) {
        getDelegate().setPropertyValue("backBorderStyle", backBorderStyle);
    }

    public void setSelectionChangeListener(MethodExpression selectionChangeListener) {
        getDelegate().setPropertyValue("selectionChangeListener", selectionChangeListener);
    }

    public void setSelectedIndex(ValueExpression selectedIndex) {
        getDelegate().setPropertyValue("selectedIndex", selectedIndex);
    }

    public void setTabStyle(ValueExpression tabStyle) {
        getDelegate().setPropertyValue("tabStyle", tabStyle);
    }

    public void setRolloverTabStyle(ValueExpression rolloverTabStyle) {
        getDelegate().setPropertyValue("rolloverTabStyle", rolloverTabStyle);
    }

    public void setSelectedTabStyle(ValueExpression selectedTabStyle) {
        getDelegate().setPropertyValue("selectedTabStyle", selectedTabStyle);
    }

    public void setFocusedTabStyle(ValueExpression focusedTabStyle) {
        getDelegate().setPropertyValue("focusedTabStyle", focusedTabStyle);
    }

    public void setRolloverSelectedTabStyle(ValueExpression rolloverSelectedTabStyle) {
        getDelegate().setPropertyValue("rolloverSelectedTabStyle", rolloverSelectedTabStyle);
    }

    public void setTabClass(ValueExpression tabClass) {
        getDelegate().setPropertyValue("tabClass", tabClass);
    }

    public void setRolloverTabClass(ValueExpression rolloverTabClass) {
        getDelegate().setPropertyValue("rolloverTabClass", rolloverTabClass);
    }

    public void setSelectedTabClass(ValueExpression selectedTabClass) {
        getDelegate().setPropertyValue("selectedTabClass", selectedTabClass);
    }

    public void setFocusedTabClass(ValueExpression focusedTabClass) {
        getDelegate().setPropertyValue("focusedTabClass", focusedTabClass);
    }

    public void setRolloverSelectedTabClass(ValueExpression rolloverSelectedTabClass) {
        getDelegate().setPropertyValue("rolloverSelectedTabClass", rolloverSelectedTabClass);
    }

    public void setEmptySpaceStyle(ValueExpression emptySpaceStyle) {
        getDelegate().setPropertyValue("emptySpaceStyle", emptySpaceStyle);
    }

    public void setEmptySpaceClass(ValueExpression emptySpaceClass) {
        getDelegate().setPropertyValue("emptySpaceClass", emptySpaceClass);
    }

    public void setAlignment(ValueExpression alignment) {
        getDelegate().setPropertyValue("alignment", alignment);
    }

    public void setPlacement(ValueExpression placement) {
        getDelegate().setPropertyValue("placement", placement);
    }

    public void setFocusable(ValueExpression focusable) {
        getDelegate().setPropertyValue("focusable", focusable);
    }

    public void setFocusAreaStyle(ValueExpression focusAreaStyle) {
        getDelegate().setPropertyValue("focusAreaStyle", focusAreaStyle);
    }

    public void setFocusAreaClass(ValueExpression focusAreaClass) {
        getDelegate().setPropertyValue("focusAreaClass", focusAreaClass);
    }
}
