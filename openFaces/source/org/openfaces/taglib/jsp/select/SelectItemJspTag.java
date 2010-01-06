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

package org.openfaces.taglib.jsp.select;

import org.openfaces.taglib.jsp.AbstractComponentJspTag;
import org.openfaces.taglib.internal.select.SelectItemTag;

import javax.el.ValueExpression;

/**
 * @author Oleg Marshalenko
 */
public class SelectItemJspTag extends AbstractComponentJspTag {

    public SelectItemJspTag() {
        super(new SelectItemTag());
    }

    public void setItemDescription(ValueExpression itemDescription) {
        getDelegate().setPropertyValue("itemDescription", itemDescription);
    }

    public void setItemLabel(ValueExpression itemLabel) {
        getDelegate().setPropertyValue("itemLabel", itemLabel);
    }

    public void setItemDisabled(ValueExpression itemDisabled) {
        getDelegate().setPropertyValue("itemDisabled", itemDisabled);
    }

    public void setItemValue(ValueExpression value) {
        getDelegate().setPropertyValue("itemValue", value);
    }

    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }

}
