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
package org.openfaces.taglib.jsp.util;

import org.openfaces.taglib.jsp.AbstractComponentJspTag;
import javax.el.ValueExpression;

public class KeepVisibleJspTag extends AbstractComponentJspTag {

    public KeepVisibleJspTag() {
        super(new org.openfaces.taglib.internal.util.KeepVisibleTag());
    }

    public void setTopMargin(ValueExpression topMargin) {
        getDelegate().setPropertyValue("topMargin", topMargin);
    }

    public void setBottomMargin(ValueExpression bottomMargin) {
        getDelegate().setPropertyValue("bottomMargin", bottomMargin);
    }

    public void setFor(ValueExpression _for) {
        getDelegate().setPropertyValue("for", _for);
    }
}
