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

import org.openfaces.taglib.internal.input.InputTextTag;
import org.openfaces.taglib.jsp.OUIInputTextJspTag;

import javax.el.ValueExpression;

/**
 * @author Vladimir Kurganov
 */

public class InputTextJspTag extends OUIInputTextJspTag {

    public InputTextJspTag() {
        super(new InputTextTag());
    }

    public void setSize(ValueExpression size) {
        getDelegate().setPropertyValue("size", size);
    }

    public void setMaxlength(ValueExpression maxlength) {
        getDelegate().setPropertyValue("maxlength", maxlength);
    }

    public void setReadonly(ValueExpression readonly) {
        getDelegate().setPropertyValue("readonly", readonly);
    }

    public void setOnselect(ValueExpression onselect) {
        getDelegate().setPropertyValue("onselect", onselect);
    }

    public void setAlt(ValueExpression alt) {
        getDelegate().setPropertyValue("alt", alt);
    }

    public void setLang(ValueExpression lang) {
        getDelegate().setPropertyValue("lang", lang);
    }

    public void setDir(ValueExpression dir) {
        getDelegate().setPropertyValue("dir", dir);
    }

    public void setAutocomplete(ValueExpression autocomplete) {
        getDelegate().setPropertyValue("autocomplete", autocomplete);
    }
}
