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
package org.openfaces.component.timetable;

import javax.faces.context.FacesContext;

public class MonthSwitcher extends AbstractSwitcher<MonthTable> {
    public static final String COMPONENT_TYPE = "org.openfaces.MonthSwitcher";
    public static final String COMPONENT_FAMILY = "org.openfaces.MonthSwitcher";

    public MonthSwitcher() {
        setRendererType("org.openfaces.MonthSwitcherRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
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

    @Override
    public Timetable.ViewType getApplicableViewType() {
        return Timetable.ViewType.MONTH;
    }

    @Override
    public String getPattern() {
        String pattern = super.getPattern();
        if (pattern == null) {
            TimePeriodSwitcher timePeriodSwitcher = getTimePeriodSwitcher();
            if (timePeriodSwitcher != null)
                pattern = timePeriodSwitcher.getMonthPattern();
        }
        return pattern;
    }

}
