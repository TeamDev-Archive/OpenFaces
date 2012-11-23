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
package org.openfaces.taglib.jsp.input;

import org.openfaces.taglib.internal.input.InputSecretTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;
import org.openfaces.taglib.jsp.OUIInputTextJspTag;

import javax.el.ValueExpression;

/**
 * @author Andre Shapovalov
 */
public class InputSecretJspTag  extends OUIInputTextJspTag {

    public InputSecretJspTag() {
        super(new InputSecretTag());
    }

    public void setSize(ValueExpression size) {
        getDelegate().setPropertyValue("size", size);
    }

    public void setMaxlength(ValueExpression maxlength) {
        getDelegate().setPropertyValue("maxlength", maxlength);
    }

    public void setInterval(ValueExpression interval) {
        getDelegate().setPropertyValue("interval", interval);
    }

    public void setDuration(ValueExpression duration) {
        getDelegate().setPropertyValue("duration", duration);
    }

    public void setReplacement(ValueExpression replacement) {
        getDelegate().setPropertyValue("replacement", replacement);
    }

    public void setPromptText(ValueExpression promptText) {
        getDelegate().setPropertyValue("promptText", promptText);
    }
}
