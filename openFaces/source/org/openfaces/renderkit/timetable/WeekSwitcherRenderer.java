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
package org.openfaces.renderkit.timetable;

import org.openfaces.component.timetable.AbstractSwitcher;
import org.openfaces.component.timetable.TimetableView;
import org.openfaces.component.timetable.WeekSwitcher;
import org.openfaces.util.CalendarUtil;
import org.openfaces.util.Styles;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Roman Gorodischer
 */
public class WeekSwitcherRenderer extends AbstractSwitcherRenderer {
    private static final String DATE_RANGE_SEPARATOR = " \u2013 ";

    @Override
    protected Object[] getAdditionalParams(FacesContext context) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        String fromPattern = (String) requestMap.get(getFromPatternKey());
        String toPattern = (String) requestMap.get(getToPatternKey());
        return new Object[]{
                DATE_RANGE_SEPARATOR,
                fromPattern,
                toPattern
        };
    }

    @Override
    protected Date getDayInitParam(TimetableView timetableView) {
        return getFirstDayOfTheWeek(timetableView);
    }

    @Override
    protected void renderText(
            FacesContext context,
            AbstractSwitcher switcher,
            TimetableView timetableView,
            SimpleDateFormat dateFormat) throws IOException {
        WeekSwitcher weekSwitcher = (WeekSwitcher) switcher;
        Locale locale = switcher.getLocale();
        TimeZone timeZone = switcher.getTimeZone();

        SimpleDateFormat fromDateFormat = CalendarUtil.getSimpleDateFormat(null, null,
                weekSwitcher.getFromPattern(), "MMMM d", locale, timeZone);
        SimpleDateFormat toDateFormat = CalendarUtil.getSimpleDateFormat(null, null,
                weekSwitcher.getToPattern(), "MMMM d, yyyy", locale, timeZone);
        Map<String,Object> requestMap = context.getExternalContext().getRequestMap();
        requestMap.put(getFromPatternKey(), fromDateFormat.toPattern());
        requestMap.put(getToPatternKey(), toDateFormat.toPattern());

        ResponseWriter writer = context.getResponseWriter();
        String clientId = switcher.getClientId(context);
        writer.startElement("div", switcher);
        writer.writeAttribute("id", clientId + "::text", null);
        String textClass = Styles.getCSSClass(context,
                switcher, switcher.getTextStyle(), "o_weekSwitcher_text", switcher.getTextClass());
        writer.writeAttribute("class", textClass, null);

        writer.write(fromDateFormat.format(getFirstDayOfTheWeek(timetableView)) + DATE_RANGE_SEPARATOR
                + toDateFormat.format(getLastDayOfTheWeek(timetableView)));
        writer.endElement("div");
    }

    private String getToPatternKey() {
        return WeekSwitcherRenderer.class + ".toPattern";
    }

    private String getFromPatternKey() {
        return WeekSwitcherRenderer.class + ".fromPattern";
    }

    private Date getFirstDayOfTheWeek(TimetableView timetableView) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timetableView.getDay());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, 1 - dayOfWeek);
        return calendar.getTime();
    }

    private Date getLastDayOfTheWeek(TimetableView timetableView) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(timetableView.getDay());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, 7 - dayOfWeek);
        return calendar.getTime();
    }

}
