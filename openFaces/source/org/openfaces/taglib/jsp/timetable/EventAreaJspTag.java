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
package org.openfaces.taglib.jsp.timetable;

import org.openfaces.taglib.internal.timetable.EventAreaTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class EventAreaJspTag extends AbstractComponentJspTag {
    public EventAreaJspTag() {
        super(new EventAreaTag());
    }

    public void setHorizontalAlignment(ValueExpression horizontalAlignment) {
        getDelegate().setPropertyValue("horizontalAlignment", horizontalAlignment);
    }

    public void setVerticalAlignment(ValueExpression verticalAlignment) {
        getDelegate().setPropertyValue("verticalAlignment", verticalAlignment);
    }

}
