/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.richfaces.demo.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Dmitry Pikhulya
 */
public class DateRange implements Serializable {
    private String category;
    private Date startDate;
    private Date endDate;

    public DateRange(String category, int diff) {
        this(category, diff, diff);
    }

    public DateRange(String category, int startDiff, int endDiff) {
        this(category, createStartDate(startDiff), createEndDate(endDiff));
    }

    public DateRange(String category, Date startDate, Date endDate) {
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    static Date createStartDate(int difference) {
        Calendar mondayStartDateC = Calendar.getInstance();
        mondayStartDateC.add(Calendar.DAY_OF_MONTH, -difference);
        mondayStartDateC.set(Calendar.HOUR_OF_DAY, 23);
        mondayStartDateC.set(Calendar.MINUTE, 59);
        return mondayStartDateC.getTime();
    }

    static Date createEndDate(int difference) {
        Calendar fridayEndDateC = Calendar.getInstance();
        fridayEndDateC.add(Calendar.DAY_OF_MONTH, -difference);
        fridayEndDateC.set(Calendar.HOUR_OF_DAY, 0);
        fridayEndDateC.set(Calendar.MINUTE, 0);
        return fridayEndDateC.getTime();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DateRange)) return false;

        DateRange range = (DateRange) o;

        if (category != null ? !category.equals(range.category) : range.category != null) return false;
        if (endDate != null ? !endDate.equals(range.endDate) : range.endDate != null) return false;
        if (startDate != null ? !startDate.equals(range.startDate) : range.startDate != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (category != null ? category.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }

    public String getCategory() {
        return category;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
