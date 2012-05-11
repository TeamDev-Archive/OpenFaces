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

package org.openfaces.component.chart;

import org.jfree.data.time.*;

/**
 * @author Eugene Goncharov
 */
public enum TimePeriod {
    FIXED_MILLISECOND("fixedMillisecond", FixedMillisecond.class),
    MILLISECOND("millisecond", Millisecond.class),
    SECOND("second", Second.class),
    MINUTE("minute", Minute.class),
    HOUR("hour", Hour.class),
    DAY("day", Day.class),
    WEEK("week", Week.class),
    MONTH("month", Month.class),
    QUARTER("quarter", Quarter.class),
    YEAR("year", Year.class);

    private String name;
    private Class timePeriodClass;

    TimePeriod(String name, Class timePeriodClass) {
        this.name = name;
        this.timePeriodClass = timePeriodClass;
    }

    public Class getTimePeriodClass() {
        return timePeriodClass;
    }

    @Override
    public String toString() {
        return name;
    }
}
