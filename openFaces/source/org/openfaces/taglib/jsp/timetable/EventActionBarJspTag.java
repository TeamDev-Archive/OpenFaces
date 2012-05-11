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

import org.openfaces.taglib.internal.timetable.EventActionBarTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

public class EventActionBarJspTag extends AbstractComponentJspTag {
    public EventActionBarJspTag() {
        super(new EventActionBarTag());
        setId("_eventActionBar"); // this component should be only the one at DayTable. See JSFC-3916
    }

    public void setNoteText(ValueExpression noteText) {
        getDelegate().setPropertyValue("noteText", noteText);
    }

    public void setBackgroudnIntensity(ValueExpression backgroudnIntensity) {
        getDelegate().setPropertyValue("backgroundIntensity", backgroudnIntensity);
    }

    public void setActionRolloverBackgroundIntensity(ValueExpression value) {
        getDelegate().setPropertyValue("actionRolloverBackgroundIntensity", value);
    }

    public void setActionPressedBackgroundIntensity(ValueExpression value) {
        getDelegate().setPropertyValue("actionPressedBackgroundIntensity", value);
    }
}