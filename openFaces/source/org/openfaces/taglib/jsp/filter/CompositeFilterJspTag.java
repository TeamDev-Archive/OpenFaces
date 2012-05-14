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
package org.openfaces.taglib.jsp.filter;

import org.openfaces.taglib.internal.filter.CompositeFilterTag;

import javax.el.ValueExpression;

/**
 * @author Natalia Zolochevska
 */
public class CompositeFilterJspTag extends FilterJspTag {

    public CompositeFilterJspTag() {
        super(new CompositeFilterTag());
    }

    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }

    public void setNoFilterMessage(ValueExpression noFilterMessage) {
        getDelegate().setPropertyValue("noFilterMessage", noFilterMessage);
    }

     public void setLabels(ValueExpression labels) {
        getDelegate().setPropertyValue("labels", labels);
    }

    public void setAutoDetect(ValueExpression autoDetect) {
        getDelegate().setPropertyValue("autoDetect", autoDetect);
    }

}