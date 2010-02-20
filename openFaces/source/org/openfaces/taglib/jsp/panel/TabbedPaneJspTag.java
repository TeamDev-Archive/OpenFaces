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
package org.openfaces.taglib.jsp.panel;

import org.openfaces.taglib.internal.panel.TabbedPaneTag;

import javax.el.ValueExpression;

/**
 * @author Andrew Palval
 */
public class TabbedPaneJspTag extends MultiPageContainerJspTag {

    public TabbedPaneJspTag() {
        super(new TabbedPaneTag());
    }

    public void setTabEmptySpaceStyle(ValueExpression tabEmptySpaceStyle) {
        getDelegate().setPropertyValue("tabEmptySpaceStyle", tabEmptySpaceStyle);
    }

    public void setTabEmptySpaceClass(ValueExpression tabEmptySpaceClass) {
        getDelegate().setPropertyValue("tabEmptySpaceClass", tabEmptySpaceClass);
    }

    public void setTabGapWidth(ValueExpression tabGapWidth) {
        getDelegate().setPropertyValue("tabGapWidth", tabGapWidth);
    }

    public void setRolloverContainerStyle(ValueExpression rolloverContainerStyle) {
        getDelegate().setPropertyValue("rolloverContainerStyle", rolloverContainerStyle);
    }

    public void setRolloverContainerClass(ValueExpression rolloverContainerClass) {
        getDelegate().setPropertyValue("rolloverContainerClass", rolloverContainerClass);
    }

    public void setTabAlignment(ValueExpression tabAlignment) {
        getDelegate().setPropertyValue("tabAlignment", tabAlignment);
    }

    public void setTabPlacement(ValueExpression tabPlacement) {
        getDelegate().setPropertyValue("tabPlacement", tabPlacement);
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

    public void setFrontBorderStyle(ValueExpression frontBorderStyle) {
        getDelegate().setPropertyValue("frontBorderStyle", frontBorderStyle);
    }

    public void setBackBorderStyle(ValueExpression backBorderStyle) {
        getDelegate().setPropertyValue("backBorderStyle", backBorderStyle);
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

    public void setOnselectionchange(ValueExpression onselectionchange) {
        getDelegate().setPropertyValue("onselectionchange", onselectionchange);
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
