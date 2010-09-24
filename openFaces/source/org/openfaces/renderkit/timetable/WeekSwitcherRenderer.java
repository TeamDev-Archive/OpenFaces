/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.renderkit.timetable;

import org.openfaces.component.timetable.TimetableView;
import org.openfaces.component.timetable.WeekSwitcher;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.CalendarUtil;
import org.openfaces.util.DataUtil;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Roman Gorodischer
 */
public class WeekSwitcherRenderer extends RendererBase {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered())
            return;

        WeekSwitcher weekSwitcher = (WeekSwitcher) component;
            
        Locale locale = weekSwitcher.getLocale();
        Rendering.registerDateTimeFormatObject(locale);

        TimetableView timetableView = weekSwitcher.getTimetableView();
        TimeZone timeZone = weekSwitcher.getTimeZone();

        String splitter = weekSwitcher.getSplitter();
        if (splitter == null) {
            splitter = " - ";
        }

        Boolean enabled = weekSwitcher.isEnabled();

        SimpleDateFormat dateFormat = CalendarUtil.getSimpleDateFormat(weekSwitcher.getDateFormat(),
                WeekSwitcher.DEFAULT_DATE_FORMAT, weekSwitcher.getPattern(), null, locale, timeZone);
        String pattern = dateFormat.toPattern();
        boolean renderText = pattern.length() != 0;

        if (!renderText) {
            throw new FacesException("WeekSwitcher's pattern is empty.");
        }

        String clientId = weekSwitcher.getClientId(context);

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("table", weekSwitcher);

        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("border", "0", null);
        String styleClass = Styles.getCSSClass(context,
                weekSwitcher, weekSwitcher.getStyle(), "o_weekSwitcher", weekSwitcher.getStyleClass());
        writer.writeAttribute("class", styleClass, null);
        writer.startElement("tbody", weekSwitcher);
        writer.startElement("tr", weekSwitcher);

        //previous button
        if (enabled) {
            writer.startElement("td", weekSwitcher);
            writer.writeAttribute("id", clientId + "::previous_button", null);
            writer.writeAttribute("class", Styles.getCSSClass(context, weekSwitcher,
                    weekSwitcher.getPreviousButtonStyle(), "o_weekSwitcher_previous_button",
                    weekSwitcher.getPreviousButtonClass()), null);
            String previousButtonImageUrl = Resources.getURL(context, weekSwitcher.getPreviousButtonImageUrl(), null,
                    "timetable/previousButton.gif");
            writer.startElement("img", weekSwitcher);
            writer.writeAttribute("src", previousButtonImageUrl, null);
            writer.endElement("img");
            writer.endElement("td");
        }

        writer.startElement("td", weekSwitcher);

        writer.startElement("p", weekSwitcher);
        writer.writeAttribute("id", clientId + "::text", null);
        String textClass = Styles.getCSSClass(context,
                weekSwitcher, weekSwitcher.getTextStyle(), "o_weekSwitcher_text", weekSwitcher.getTextClass());
        writer.writeAttribute("class", textClass, null);

        writer.write(dateFormat.format(weekSwitcher.getFirstDayOfTheWeek()) + splitter
                  + dateFormat.format(weekSwitcher.getLastDayOfTheWeek()));
        writer.endElement("p");

        writer.endElement("td");

        //next button
        if (enabled) {
            writer.startElement("td", weekSwitcher);
            writer.writeAttribute("id", clientId + "::next_button", null);
            writer.writeAttribute("class", Styles.getCSSClass(context,
                    weekSwitcher, weekSwitcher.getNextButtonStyle(), "o_weekSwitcher_next_button", weekSwitcher.getNextButtonClass()), null);
            String nextButtonImageUrl = Resources.getURL(context, weekSwitcher.getNextButtonImageUrl(), null,
                    "timetable/nextButton.gif");
            writer.startElement("img", weekSwitcher);
            writer.writeAttribute("src", nextButtonImageUrl, null);
            writer.endElement("img");
            writer.endElement("td");
        }

        writer.endElement("tr");

        writer.endElement("tbody");
        writer.endElement("table");

        JSONObject stylingParams = getStylingParamsObj(context, weekSwitcher);
        Styles.renderStyleClasses(context, weekSwitcher);

        ScriptBuilder script = new ScriptBuilder().initScript(context, weekSwitcher, "O$.WeekSwitcher._init",
                timetableView.getClientId(context),
                DataUtil.formatDateTimeForJs(weekSwitcher.getFirstDayOfTheWeek(), timeZone),
                pattern,
                locale,
                stylingParams,
                enabled,
                splitter);

        Rendering.renderInitScript(context, script,
                Resources.getUtilJsURL(context),
                Resources.getJsonJsURL(context),
                Resources.getInternalURL(context, "timetable/weekSwitcher.js"));

    }

    private JSONObject getStylingParamsObj(FacesContext context, WeekSwitcher weekSwitcher) {
        JSONObject stylingParams = new JSONObject();
        Styles.addStyleJsonParam(context, weekSwitcher, stylingParams, "rolloverClass",
                weekSwitcher.getRolloverStyle(), weekSwitcher.getRolloverClass());
        Styles.addStyleJsonParam(context, weekSwitcher, stylingParams, "previousButtonRolloverClass",
                weekSwitcher.getPreviousButtonRolloverStyle(), weekSwitcher.getPreviousButtonRolloverClass());
        Styles.addStyleJsonParam(context, weekSwitcher, stylingParams, "previousButtonPressedClass",
                weekSwitcher.getPreviousButtonPressedStyle(), weekSwitcher.getPreviousButtonPressedClass(), StyleGroup.rolloverStyleGroup());
        Styles.addStyleJsonParam(context, weekSwitcher, stylingParams, "nextButtonRolloverClass",
                weekSwitcher.getNextButtonRolloverStyle(), weekSwitcher.getNextButtonRolloverClass(), StyleGroup.rolloverStyleGroup());
        Styles.addStyleJsonParam(context, weekSwitcher, stylingParams, "nextButtonPressedClass",
                weekSwitcher.getNextButtonPressedStyle(), weekSwitcher.getNextButtonPressedClass(), StyleGroup.rolloverStyleGroup());
        Styles.addStyleJsonParam(context, weekSwitcher, stylingParams, "labelRolloverClass",
                weekSwitcher.getTextRolloverStyle(), weekSwitcher.getTextRolloverClass(), StyleGroup.rolloverStyleGroup());

        return stylingParams;
    }



}
