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

import org.openfaces.taglib.internal.output.GraphicTextTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Darya Shumilina
 */
public class GraphicTextJspTag extends AbstractComponentJspTag {

    public GraphicTextJspTag() {
        super(new GraphicTextTag());
    }

    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }

    public void setDirection(ValueExpression direction) {
        getDelegate().setPropertyValue("direction", direction);
    }

    public void setTextStyle(ValueExpression textStyle) {
        getDelegate().setPropertyValue("textStyle", textStyle);
    }

    public void setTitle(ValueExpression title) {
        getDelegate().setPropertyValue("title", title);
    }

    public void setLang(ValueExpression lang) {
        getDelegate().setPropertyValue("lang", lang);
    }

    public void setConverter(ValueExpression converter) {
        getDelegate().setPropertyValue("converter", converter);
    }

}