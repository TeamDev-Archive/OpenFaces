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

import org.openfaces.taglib.internal.table.SummariesTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;


public class SummariesJspTag extends AbstractComponentJspTag {

    public SummariesJspTag() {
        super(new SummariesTag());
    }


    public void setFooterVisible(ValueExpression footerVisible) {
        getDelegate().setPropertyValue("footerVisible", footerVisible);
    }

    public void setInGroupFootersVisible(ValueExpression inGroupFootersVisible) {
        getDelegate().setPropertyValue("inGroupFootersVisible", inGroupFootersVisible);
    }

    public void setPattern(ValueExpression pattern) {
        getDelegate().setPropertyValue("pattern", pattern);
    }

    public void setFunctionEditable(ValueExpression functionEditable) {
        getDelegate().setPropertyValue("functionEditable", functionEditable);
    }
}
