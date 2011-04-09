/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component.timetable;

import javax.faces.context.FacesContext;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Roman Gorodischer
 */
public class WeekSwitcher extends AbstractSwitcher<WeekTable> {
    public static final String COMPONENT_TYPE = "org.openfaces.WeekSwitcher";
    public static final String COMPONENT_FAMILY = "org.openfaces.WeekSwitcher";

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    protected String getTimetableNotFoundMsg() {
        return "WeekSwitcher's \"for\" attribute must refer to a WeekTable component.";
    }

    public Date getFirstDayOfTheWeek () {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getTimetableView().getDay());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, 1 - dayOfWeek);
        return calendar.getTime();
    }

    public Date getLastDayOfTheWeek () {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(getTimetableView().getDay());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, 7 - dayOfWeek);
        return calendar.getTime ();
    }    

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
    }

}
