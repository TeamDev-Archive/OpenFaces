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
package org.openfaces.renderkit.timetable;

import org.openfaces.component.timetable.AbstractSwitcher;
import org.openfaces.component.timetable.TimetableView;
import org.openfaces.util.DataUtil;
import org.openfaces.util.Styles;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Roman Gorodischer
 */
public class WeekSwitcherRenderer extends AbstractSwitcherRenderer {
    private static final String DATE_RANGE_SEPARATOR = " \u2013 ";

    @Override
    protected Object[] getAdditionalParams(FacesContext context) {
        return new Object[]{
                DATE_RANGE_SEPARATOR
        };
    }

    @Override
    protected String formatDayInitParam(TimetableView timetableView, TimeZone timeZone) {
        return DataUtil.formatDateTimeForJs(getFirstDayOfTheWeek(timetableView), timeZone);
    }

    @Override
    protected void renderText(FacesContext context, AbstractSwitcher switcher, TimetableView timetableView, SimpleDateFormat dateFormat) throws IOException {
        String pattern = dateFormat.toPattern();
        boolean renderText = pattern.length() != 0;

        if (!renderText) {
            throw new FacesException("WeekSwitcher's pattern is empty.");
        }

        ResponseWriter writer = context.getResponseWriter();
        String clientId = switcher.getClientId(context);
        writer.startElement("p", switcher);
        writer.writeAttribute("id", clientId + "::text", null);
        String textClass = Styles.getCSSClass(context,
                switcher, switcher.getTextStyle(), "o_timeSwitcher_text", switcher.getTextClass());
        writer.writeAttribute("class", textClass, null);

        writer.write(dateFormat.format(getFirstDayOfTheWeek(timetableView)) + DATE_RANGE_SEPARATOR
                + dateFormat.format(getLastDayOfTheWeek(timetableView)));
        writer.endElement("p");
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
