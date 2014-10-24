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

import org.openfaces.taglib.internal.table.ScrollingTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class ScrollingJspTag extends AbstractComponentJspTag {
    public ScrollingJspTag() {
        super(new ScrollingTag());
    }

    public void setVertical(ValueExpression vertical) {
        getDelegate().setPropertyValue("vertical", vertical);
    }

    public void setHorizontal(ValueExpression horizontal) {
        getDelegate().setPropertyValue("horizontal", horizontal);
    }

    public void setPosition(ValueExpression position) {
        getDelegate().setPropertyValue("position", position);
    }


    public void setAutoScrollbars(ValueExpression autoScrollbars) {
        getDelegate().setPropertyValue("autoScrollbars", autoScrollbars);
    }

    public void setMinimizeHeight(ValueExpression minimizeHeight) {
        getDelegate().setPropertyValue("minimizeHeight", minimizeHeight);
    }

    public void setAutoSaveState(ValueExpression autoSaveState) {
        getDelegate().setPropertyValue("autoSaveState", autoSaveState);
    }

    public void setAutoSaveStateDelay(ValueExpression autoSaveStateDelay) {
        getDelegate().setPropertyValue("autoSaveStateDelay", autoSaveStateDelay);
    }
}
