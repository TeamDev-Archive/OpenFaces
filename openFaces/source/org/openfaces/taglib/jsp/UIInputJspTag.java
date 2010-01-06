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

import javax.el.MethodExpression;
import javax.el.ValueExpression;

/**
 * @author Andrew Palval
 */
public abstract class UIInputJspTag extends AbstractComponentJspTag {

    protected UIInputJspTag(AbstractComponentTag delegate) {
        super(delegate);
    }

    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }

    public void setDisabled(ValueExpression disabled) {
        getDelegate().setPropertyValue("disabled", disabled);
    }

    public void setImmediate(ValueExpression immediate) {
        getDelegate().setPropertyValue("immediate", immediate);
    }

    public void setValidator(MethodExpression validator) {
        getDelegate().setPropertyValue("validator", validator);
    }

    public void setConverter(ValueExpression converter) {
        getDelegate().setPropertyValue("converter", converter);
    }

    public void setRequired(ValueExpression required) {
        getDelegate().setPropertyValue("required", required);
    }

    public void setValueChangeListener(MethodExpression valueChangeListener) {
        getDelegate().setPropertyValue("valueChangeListener", valueChangeListener);
    }

    public void setLabel(ValueExpression label) {
        getDelegate().setPropertyValue("label", label);
    }
}
