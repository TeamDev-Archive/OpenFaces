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

import org.openfaces.component.timetable.TimeScaleTable;
import org.openfaces.component.timetable.WeekTable;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.TableRenderer;
import org.openfaces.util.Rendering;
import org.openfaces.util.Styles;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author Roman Porotnikov
 */
public class WeekTableRenderer extends TimeScaleTableRenderer {

    protected String getTagName() {
        return "<o:weekTable>";
    }

    protected String getComponentName() {
        return "WeekTable";
    }

    protected String getJsLibraryName() {
        return "timetable/weekTable.js";
    }

    protected String getJsInitFunctionName() {
        return "O$.WeekTable._init";
    }

    protected int getDayCount() {
        return 7;
    }

    protected Date getFirstDayForDefaultPeriod(TimeScaleTable timetableView, TimeZone timeZone) {
        GregorianCalendar c = new GregorianCalendar(timeZone);
        Date day = timetableView.getDay();
        c.setTime(day);
        c.set(Calendar.DAY_OF_WEEK, ((WeekTable) timetableView).getFirstDayOfWeek());
        return c.getTime();
    }

    protected Date getLastDayForDefaultPeriod(TimeScaleTable timetableView, TimeZone timeZone) {
        GregorianCalendar c = new GregorianCalendar(timeZone);
        Date day = timetableView.getDay();
        c.setTime(day);
        c.set(Calendar.DAY_OF_WEEK, ((WeekTable) timetableView).getFirstDayOfWeek() + 6);
        return c.getTime();
    }

    @Override
    protected void renderSpecificHeaders(FacesContext context, final TimeScaleTable timetableView, String clientId) throws IOException {
        renderWeekdayHeadersRow(context, timetableView, clientId);
    }

    @Override
    protected void addSpecificStylingParams(FacesContext context, TimeScaleTable timetableView, JSONObject stylingParams) {
        WeekTable weekTable = (WeekTable) timetableView;
        Rendering.addJsonParam(stylingParams, "weekdayColumnSeparator", weekTable.getWeekdayColumnSeparator());
        Rendering.addJsonParam(stylingParams, "weekdayHeadersRowSeparator", weekTable.getWeekdayHeadersRowSeparator());
        Rendering.addJsonParam(stylingParams, "weekdayPattern", weekTable.getWeekdayPattern());
        Styles.addStyleJsonParam(context, weekTable, stylingParams, "weekdayHeadersRowClass",
                weekTable.getWeekdayHeadersRowStyle(), weekTable.getWeekdayHeadersRowClass());
        Styles.addStyleJsonParam(context, weekTable, stylingParams, "weekdayClass",
                weekTable.getWeekdayStyle(), weekTable.getWeekdayClass());
    }

    @Override
    protected JSONObject getCalendarOptionsObj(TimeScaleTable timetableView) {
        WeekTable weekTable = (WeekTable) timetableView;

        JSONObject calendarOptionsObj = new JSONObject();

        int firstDayOfWeek = weekTable.getFirstDayOfWeek() - 1; // JS weekdays are 0-based while Java weekdays are 1-based
        Rendering.addJsonParam(calendarOptionsObj, "firstDayOfWeek", firstDayOfWeek);
        return calendarOptionsObj;
    }

    private void renderWeekdayHeadersRow(FacesContext context, final TimeScaleTable timetableView, String clientId) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(timetableView.getDay());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, 1 - dayOfWeek);

        writer.startElement("tr", timetableView);
        writer.startElement("td", timetableView);

        new TableRenderer(clientId + Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "weekdayHeaders", 0, 0, 0, "o_weekdayHeadersTable")
                .render(timetableView, true, 1, 9);

        writer.endElement("td");
        writer.endElement("tr");
    }

}
