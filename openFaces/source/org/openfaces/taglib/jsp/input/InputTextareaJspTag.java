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
package org.openfaces.taglib.jsp.input;

import org.openfaces.taglib.internal.input.InputTextareaTag;
import org.openfaces.taglib.jsp.OUIInputTextJspTag;

import javax.el.ValueExpression;

/**
 * @author Alexander Golubev
 */
public class InputTextareaJspTag extends OUIInputTextJspTag {

    public InputTextareaJspTag() {
        super(new InputTextareaTag());
    }

    public void setRows(ValueExpression rows) {
        getDelegate().setPropertyValue("rows", rows);
    }

    public void setCols(ValueExpression cols) {
        getDelegate().setPropertyValue("cols", cols);
    }

    public void setOnselect(ValueExpression onselect) {
        getDelegate().setPropertyValue("onselect", onselect);
    }

    public void setLang(ValueExpression lang) {
        getDelegate().setPropertyValue("lang", lang);
    }

    public void setDir(ValueExpression dir) {
        getDelegate().setPropertyValue("dir", dir);
    }

    public void setReadonly(ValueExpression readonly) {
        getDelegate().setPropertyValue("readonly", readonly);
    }

    public void setAutoGrowing(ValueExpression autoGrowing){
        getDelegate().setPropertyValue("autoGrowing", autoGrowing);
    }
}
