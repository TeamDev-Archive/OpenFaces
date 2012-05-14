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
package org.openfaces.taglib.jsp.ajax;

import org.openfaces.taglib.internal.ajax.DefaultProgressMessageTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Eugene Goncharov
 */
public class DefaultProgressMessageJspTag extends AbstractComponentJspTag {
    public DefaultProgressMessageJspTag() {
        super(new DefaultProgressMessageTag());
    }

    public void setText(ValueExpression text) {
        getDelegate().setPropertyValue("text", text);
    }

    public void setImageUrl(ValueExpression imageUrl) {
        getDelegate().setPropertyValue("imageUrl", imageUrl);
    }

    public void setHorizontalAlignment(ValueExpression horizontalAlignment) {
        getDelegate().setPropertyValue("horizontalAlignment", horizontalAlignment);
    }

    public void setVerticalAlignment(ValueExpression verticalAlignment) {
        getDelegate().setPropertyValue("verticalAlignment", verticalAlignment);
    }

    public void setTransparency(ValueExpression transparency) {
        getDelegate().setPropertyValue("transparency", transparency);
    }

    public void setTransparencyTransitionPeriod(ValueExpression transparencyTransitionPeriod) {
        getDelegate().setPropertyValue("transparencyTransitionPeriod", transparencyTransitionPeriod);
    }

    public void setFillBackground(ValueExpression fillBackground) {
        getDelegate().setPropertyValue("fillBackground", fillBackground);
    }

    public void setBackgroundTransparency(ValueExpression backgroundTransparency) {
        getDelegate().setPropertyValue("backgroundTransparency", backgroundTransparency);
    }

    public void setBackgroundTransparencyTransitionPeriod(ValueExpression backgroundTransparencyTransitionPeriod) {
        getDelegate().setPropertyValue("backgroundTransparencyTransitionPeriod", backgroundTransparencyTransitionPeriod);
    }

    public void setBackgroundStyle(ValueExpression backgroundStyle) {
        getDelegate().setPropertyValue("backgroundStyle", backgroundStyle);
    }

    public void setBackgroundClass(ValueExpression backgroundClass) {
        getDelegate().setPropertyValue("backgroundClass", backgroundClass);
    }

    public void setMode(ValueExpression mode) {
        getDelegate().setPropertyValue("mode", mode);
    }
}
