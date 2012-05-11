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
import org.openfaces.util.Styles;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MonthSwitcherRenderer extends AbstractSwitcherRenderer {
    @Override
    protected Object[] getAdditionalParams(FacesContext context) {
        return new Object[0];
    }

    @Override
    protected Date getDayInitParam(TimetableView timetableView) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timetableView.getDay());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    @Override
    protected void renderText(FacesContext context, AbstractSwitcher switcher, TimetableView timetableView, SimpleDateFormat dateFormat) throws IOException {
        String pattern = dateFormat.toPattern();
        if (pattern.length() == 0) throw new FacesException("MonthSwitcher's pattern is empty.");

        ResponseWriter writer = context.getResponseWriter();
        String clientId = switcher.getClientId(context);
        writer.startElement("div", switcher);
        writer.writeAttribute("id", clientId + "::text", null);
        String textClass = Styles.getCSSClass(context,
                switcher, switcher.getTextStyle(), "o_monthSwitcher_text", switcher.getTextClass());
        writer.writeAttribute("class", textClass, null);

        Date date = timetableView.getDay();
        writer.write(dateFormat.format(date));
        writer.endElement("div");
    }

    @Override
    protected String getPattern(AbstractSwitcher switcher) {
        String pattern = super.getPattern(switcher);
        if (pattern == null)
            pattern = "MMMM yyyy";
        return pattern;
    }
}
