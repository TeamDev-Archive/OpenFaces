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

import org.openfaces.taglib.internal.AbstractComponentTag;
import org.openfaces.taglib.internal.util.ForEachTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Alexey Tarasyuk
 */
public class ForEachJspTag extends AbstractComponentJspTag {

    public ForEachJspTag() {
        super(new ForEachTag());
    }

    public ForEachJspTag(AbstractComponentTag delegate) {
        super(delegate);
    }

    public void setItems(ValueExpression items) {
        getDelegate().setPropertyValue("items", items);
    }

    public void setBegin(ValueExpression beginIndex) {
        getDelegate().setPropertyValue("begin", beginIndex);
    }

    public void setEnd(ValueExpression endIndex) {
        getDelegate().setPropertyValue("end", endIndex);
    }

    public void setStep(ValueExpression step) {
        getDelegate().setPropertyValue("step", step);
    }

    public void setVar(ValueExpression var) {
        getDelegate().setPropertyValue("var", var);
    }

    public void setVarStatus(ValueExpression varStatus) {
        getDelegate().setPropertyValue("varStatus", varStatus);
    }

    public void setWrapperTagName(ValueExpression wrapperTagName) {
        getDelegate().setPropertyValue("wrapperTagName", wrapperTagName);
    }

}