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

import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Roman Gorodischer
 */
public class WeekSwitcher extends AbstractSwitcher<WeekTable> {
    public static final String COMPONENT_TYPE = "org.openfaces.WeekSwitcher";
    public static final String COMPONENT_FAMILY = "org.openfaces.WeekSwitcher";
    private String fromPattern;
    private String toPattern;

    public WeekSwitcher() {
        setRendererType("org.openfaces.WeekSwitcherRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                fromPattern,
                toPattern
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        fromPattern = (String) state[i++];
        toPattern = (String) state[i++];
    }

    @Override
    public Timetable.ViewType getApplicableViewType() {
        return Timetable.ViewType.WEEK;
    }

    public String getFromPattern() {
        String fromPattern = ValueBindings.get(this, "fromPattern", this.fromPattern);
        if (fromPattern == null) {
            TimePeriodSwitcher timePeriodSwitcher = getTimePeriodSwitcher();
            if (timePeriodSwitcher != null)
                fromPattern = timePeriodSwitcher.getFromWeekPattern();
        }
        return fromPattern;
    }

    public void setFromPattern(String fromPattern) {
        this.fromPattern = fromPattern;
    }

    public String getToPattern() {
        String toPattern = ValueBindings.get(this, "toPattern", this.toPattern);
        if (toPattern == null) {
            TimePeriodSwitcher timePeriodSwitcher = getTimePeriodSwitcher();
            if (timePeriodSwitcher != null)
                toPattern = timePeriodSwitcher.getToWeekPattern();
        }

        return toPattern;
    }

    public void setToPattern(String toPattern) {
        this.toPattern = toPattern;
    }
}
