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
}
