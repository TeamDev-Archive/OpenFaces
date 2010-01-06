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
package org.openfaces.taglib.jsp.validation;

import org.openfaces.taglib.internal.validation.FloatingIconMessageTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Vladimir Korenev
 */
public class FloatingIconMessageJspTag extends AbstractComponentJspTag {

    public FloatingIconMessageJspTag() {
        super(new FloatingIconMessageTag());
    }

    public void setShowDetail(ValueExpression showDetail) {
        getDelegate().setPropertyValue("showDetail", showDetail);
    }

    public void setShowSummary(ValueExpression showSummary) {
        getDelegate().setPropertyValue("showSummary", showSummary);
    }

    public void setNoImage(ValueExpression noImage) {
        getDelegate().setPropertyValue("noImage", noImage);
    }

    public void setNoStyle(ValueExpression noStyle) {
        getDelegate().setPropertyValue("noStyle", noStyle);
    }

    public void setFor(ValueExpression aFor) {
        getDelegate().setPropertyValue("for", aFor);
    }

    public void setImageUrl(ValueExpression imageUrl) {
        getDelegate().setPropertyValue("imageUrl", imageUrl);
    }

    public void setOffsetTop(ValueExpression offsetTop) {
        getDelegate().setPropertyValue("offsetTop", offsetTop);
    }

    public void setOffsetLeft(ValueExpression offsetLeft) {
        getDelegate().setPropertyValue("offsetLeft", offsetLeft);
    }
}
