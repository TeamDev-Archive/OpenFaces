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

import org.openfaces.taglib.internal.panel.AbstractPanelWithCaptionTag;
import org.openfaces.taglib.internal.panel.SidePanelTag;

import javax.el.ValueExpression;

/**
 * @author Alexey Tarasyuk
 */
public class SidePanelJspTag extends AbstractPanelWithCaptionJspTag {

    public SidePanelJspTag() {
        super(new SidePanelTag());
    }

    public SidePanelJspTag(AbstractPanelWithCaptionTag delegate) {
        super(delegate);
    }

    public void setSize(ValueExpression size) {
        getDelegate().setPropertyValue("size", size);
    }

    public void setMinSize(ValueExpression minSize) {
        getDelegate().setPropertyValue("minSize", minSize);
    }

    public void setMaxSize(ValueExpression maxSize) {
        getDelegate().setPropertyValue("maxSize", maxSize);
    }

    public void setAlignment(ValueExpression alignment) {
        getDelegate().setPropertyValue("alignment", alignment);
    }

    public void setSplitterStyle(ValueExpression splitterStyle) {
        getDelegate().setPropertyValue("splitterStyle", splitterStyle);
    }

    public void setSplitterRolloverStyle(ValueExpression splitterRolloverStyle) {
        getDelegate().setPropertyValue("splitterRolloverStyle", splitterRolloverStyle);
    }

    public void setSplitterClass(ValueExpression splitterClass) {
        getDelegate().setPropertyValue("splitterClass", splitterClass);
    }

    public void setSplitterRolloverClass(ValueExpression splitterRolloverClass) {
        getDelegate().setPropertyValue("splitterRolloverClass", splitterRolloverClass);
    }

    public void setResizable(ValueExpression resizable) {
        getDelegate().setPropertyValue("resizable", resizable);
    }

    public void setCollapsible(ValueExpression collapsible) {
        getDelegate().setPropertyValue("collapsible", collapsible);
    }

    public void setCollapsed(ValueExpression collapsed) {
        getDelegate().setPropertyValue("collapsed", collapsed);
    }

    public void setOnsplitterdrag(ValueExpression onsplitterdrag) {
        getDelegate().setPropertyValue("onsplitterdrag", onsplitterdrag);
    }

    public void setOncollapse(ValueExpression oncollapse) {
        getDelegate().setPropertyValue("oncollapse", oncollapse);
    }

    public void setOnrestore(ValueExpression onrestore) {
        getDelegate().setPropertyValue("onrestore", onrestore);
    }
}