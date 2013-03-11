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

import org.openfaces.component.SimplePopup;
import org.openfaces.component.calendar.Calendar;
import org.openfaces.component.timetable.AbstractSwitcher;
import org.openfaces.component.timetable.DaySwitcher;
import org.openfaces.component.timetable.TimetableView;
import org.openfaces.util.CalendarUtil;
import org.openfaces.util.Styles;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Natalia Zolochevska
 */
public class DaySwitcherRenderer extends AbstractSwitcherRenderer {
    private static final String DEFAULT_SUP_PATTERN = "EEEE";

    private String getUpperPatternKey() {
        return DaySwitcherRenderer.class.getName() + ".upperPattern";
    }

    protected Object[] getAdditionalParams(FacesContext context) {
        return new Object[]{
                context.getExternalContext().getRequestMap().get(getUpperPatternKey())
        };
    }

    protected Date getDayInitParam(TimetableView timetableView) {
        return timetableView.getDay();
    }

    protected void renderText(FacesContext context, AbstractSwitcher switcher, TimetableView timetableView, SimpleDateFormat dateFormat) throws IOException {
        Date date = timetableView.getDay();
        Locale locale = switcher.getLocale();
        TimeZone timeZone = switcher.getTimeZone();

        String pattern = dateFormat.toPattern();
        boolean renderText = pattern.length() != 0;

        DaySwitcher daySwitcher = (DaySwitcher) switcher;
        SimpleDateFormat upperDateFormat = CalendarUtil.getSimpleDateFormat(daySwitcher.getUpperDateFormat(), null,
                daySwitcher.getUpperPattern(), DEFAULT_SUP_PATTERN, locale, timeZone);
        String upperPattern = upperDateFormat.toPattern();
        context.getExternalContext().getRequestMap().put(getUpperPatternKey(), upperPattern);
        boolean renderUpperText = upperPattern.length() != 0;

        if (!renderText && !renderUpperText) {
            throw new FacesException("DaySwitcher's pattern and upperPattern are both empty.");
        }

        ResponseWriter writer = context.getResponseWriter();
        String clientId = switcher.getClientId(context);
        if (renderUpperText) {
            writer.startElement("div", switcher);
            writer.writeAttribute("id", clientId + "::upper_text", null);
            String upperTextClass = Styles.getCSSClass(context,
                    switcher, daySwitcher.getUpperTextStyle(),
                    "o_daySwitcher_upper_text", daySwitcher.getUpperTextClass());
            writer.writeAttribute("class", upperTextClass, null);

            writer.write(upperDateFormat.format(date));
            writer.endElement("div");
        }

        if (renderText) {
            writer.startElement("div", switcher);
            writer.writeAttribute("id", clientId + "::text", null);
            String textClass = Styles.getCSSClass(context,
                    switcher, switcher.getTextStyle(), "o_daySwitcher_text", switcher.getTextClass());
            writer.writeAttribute("class", textClass, null);

            writer.write(dateFormat.format(timetableView.getDay()));

            writer.endElement("div");

            if (daySwitcher.isPopupCalendarEnabled()) {
                Calendar calendar = daySwitcher.getPopupCalendar();
                calendar.setRequired(true);
                calendar.setValue(new Date());
                calendar.setLocale(daySwitcher.getLocale());
                calendar.setTimeZone(daySwitcher.getTimeZone());
                calendar.setId(switcher.getId() + TimePeriodSwitcherRenderer.CALENDAR_SUFFIX);
                SimplePopup popup = new SimplePopup("o_daySwitcherPopup", calendar);
                popup.setId(switcher.getId() + TimePeriodSwitcherRenderer.POPUP_SUFFIX);
                try {
                    popup.encodeAll(context);
                } catch (IOException e) {
                    throw new RuntimeException();
                }
            }
        }
    }


}