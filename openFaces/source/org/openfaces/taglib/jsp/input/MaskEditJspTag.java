/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.taglib.jsp.input;

import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

public class MaskEditJspTag extends org.openfaces.taglib.jsp.AbstractComponentJspTag {

    public MaskEditJspTag() {
        super(new org.openfaces.taglib.internal.input.MaskEditTag());
    }


    public void setRendered(ValueExpression rendered) {
        getDelegate().setPropertyValue("rendered", rendered);
    }


    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }

    public void setStyle(ValueExpression style) {
        getDelegate().setPropertyValue("style", style);
    }


    public void setStyleClass(ValueExpression styleClass) {
        getDelegate().setPropertyValue("styleClass", styleClass);
    }

    public void setId(ValueExpression id) {
        getDelegate().setPropertyValue("id", id);
    }

    public void setIncludeliterals(ValueExpression includeliterals) {
        getDelegate().setPropertyValue("includeliterals", includeliterals);
    }
}
