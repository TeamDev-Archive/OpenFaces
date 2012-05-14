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
package org.openfaces.taglib.jsp.output;

import org.openfaces.taglib.internal.output.HintLabelTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Andrew Palval
 */
public class HintLabelJspTag extends AbstractComponentJspTag {

    public HintLabelJspTag() {
        super(new HintLabelTag());
    }

    public void setConverter(ValueExpression converter) {
        getDelegate().setPropertyValue("converter", converter);
    }

    public void setHintTimeout(ValueExpression hintTimeout) {
        getDelegate().setPropertyValue("hintTimeout", hintTimeout);
    }

    public void setHintStyle(ValueExpression hintStyle) {
        getDelegate().setPropertyValue("hintStyle", hintStyle);
    }

    public void setHintClass(ValueExpression hintClass) {
        getDelegate().setPropertyValue("hintClass", hintClass);
    }

    public void setHint(ValueExpression hint) {
        getDelegate().setPropertyValue("hint", hint);
    }

    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }

    public void setEscape(ValueExpression escape) {
        getDelegate().setPropertyValue("escape", escape);
    }

}
