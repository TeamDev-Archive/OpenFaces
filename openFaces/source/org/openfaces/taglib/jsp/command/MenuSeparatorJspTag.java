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
package org.openfaces.taglib.jsp.command;

import org.openfaces.taglib.internal.command.MenuSeparatorTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Vladimir Kurganov
 */
public class MenuSeparatorJspTag extends AbstractComponentJspTag {
    public MenuSeparatorJspTag() {
        super(new MenuSeparatorTag());
    }

    public void setIndentStyle(ValueExpression indentStyle) {
        getDelegate().setPropertyValue("indentStyle", indentStyle);
    }

    public void setIndentClass(ValueExpression indentClass) {
        getDelegate().setPropertyValue("indentClass", indentClass);
    }
}
