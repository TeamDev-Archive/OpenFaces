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

import org.openfaces.taglib.internal.table.AbstractTableSelectionTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;
import org.openfaces.taglib.jsp.OUICommandJspTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AbstractTableSelectionJspTag extends OUICommandJspTag {

    protected AbstractTableSelectionJspTag(AbstractTableSelectionTag delegate) {
        super(delegate);
    }

    public void setRawStyleClass(ValueExpression rawStyleClass) {
        getDelegate().setPropertyValue("rawStyleClass", rawStyleClass);
    }


    public void setAction(MethodExpression action) {
        getDelegate().setPropertyValue("action", action);
    }

    public void setEnabled(ValueExpression enabled) {
        getDelegate().setPropertyValue("enabled", enabled);
    }

    public void setRequired(ValueExpression required) {
        getDelegate().setPropertyValue("required", required);
    }

    public void setMouseSupport(ValueExpression mouseSupport) {
        getDelegate().setPropertyValue("mouseSupport", mouseSupport);
    }

    public void setKeyboardSupport(ValueExpression keyboardSupport) {
        getDelegate().setPropertyValue("keyboardSupport", keyboardSupport);
    }

    public void setOnchange(ValueExpression onchange) {
        getDelegate().setPropertyValue("onchange", onchange);
    }
}
