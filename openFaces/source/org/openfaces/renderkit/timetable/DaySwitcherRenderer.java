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

import org.openfaces.component.timetable.DaySwitcher;
import org.openfaces.component.timetable.DayTable;
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
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Natalia Zolochevska
 */
public class DaySwitcherRenderer extends RendererBase {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered())
            return;

        DaySwitcher daySwitcher = (DaySwitcher) component;

        Locale locale = daySwitcher.getLocale();
        Rendering.registerDateTimeFormatObject(locale);

        DayTable dayTable = daySwitcher.getDayTable();
        TimeZone timeZone = daySwitcher.getTimeZone();
        Date date = dayTable.getDay();

        Boolean enabled = daySwitcher.isEnabled();
        SimpleDateFormat upperDateFormat = CalendarUtil.getSimpleDateFormat(daySwitcher.getUpperDateFormat(), null,
                daySwitcher.getUpperPattern(), DaySwitcher.DEFAULT_SUP_PATTERN, locale, timeZone);
        String upperPattern = upperDateFormat.toPattern();
        boolean renderUpperText = upperPattern.length() != 0;

        SimpleDateFormat dateFormat = CalendarUtil.getSimpleDateFormat(daySwitcher.getDateFormat(),
                DaySwitcher.DEFAULT_DATE_FORMAT, daySwitcher.getPattern(), null, locale, timeZone);
        String pattern = dateFormat.toPattern();
        boolean renderText = pattern.length() != 0;

        if (!renderText && !renderUpperText) {
            throw new FacesException("DaySwitcher's pattern and upperPattern are both empty.");
        }

        String clientId = daySwitcher.getClientId(context);

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("table", daySwitcher);

        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("border", "0", null);
        String styleClass = Styles.getCSSClass(context,
                daySwitcher, daySwitcher.getStyle(), "o_daySwitcher", daySwitcher.getStyleClass());
        writer.writeAttribute("class", styleClass, null);
        writer.startElement("tbody", daySwitcher);
        writer.startElement("tr", daySwitcher);

        //previous button
        if (enabled) {
            writer.startElement("td", daySwitcher);
            writer.writeAttribute("id", clientId + "::previous_button", null);
            writer.writeAttribute("class", Styles.getCSSClass(context, daySwitcher,
                    daySwitcher.getPreviousButtonStyle(), "o_daySwitcher_previous_button",
                    daySwitcher.getPreviousButtonClass()), null);
            String previousButtonImageUrl = Resources.getURL(context, daySwitcher.getPreviousButtonImageUrl(),
                    DaySwitcherRenderer.class, "previousButton.gif");
            writer.startElement("img", daySwitcher);
            writer.writeAttribute("src", previousButtonImageUrl, null);
            writer.endElement("img");
            writer.endElement("td");
        }

        writer.startElement("td", daySwitcher);
        if (renderUpperText) {
            //upper text
            writer.startElement("p", daySwitcher);
            writer.writeAttribute("id", clientId + "::upper_text", null);
            String upperTextClass = Styles.getCSSClass(context,
                    daySwitcher, daySwitcher.getUpperTextStyle(), "o_daySwitcher_upper_text", daySwitcher.getUpperTextClass());
            writer.writeAttribute("class", upperTextClass, null);

            writer.write(upperDateFormat.format(date));
            writer.endElement("p");
        }
        if (renderText) {
            /*if (renderUpperText){
                writer.startElement("br", daySwitcher);
                writer.endElement("br");
            } */
            //text
            writer.startElement("p", daySwitcher);
            writer.writeAttribute("id", clientId + "::text", null);
            String textClass = Styles.getCSSClass(context,
                    daySwitcher, daySwitcher.getTextStyle(), "o_daySwitcher_text", daySwitcher.getTextClass());
            writer.writeAttribute("class", textClass, null);

            writer.write(dateFormat.format(dayTable.getDay()));
            writer.endElement("p");
        }
        writer.endElement("td");

        //next button
        if (enabled) {
            writer.startElement("td", daySwitcher);
            writer.writeAttribute("id", clientId + "::next_button", null);
            writer.writeAttribute("class", Styles.getCSSClass(context,
                    daySwitcher, daySwitcher.getNextButtonStyle(), "o_daySwitcher_next_button", daySwitcher.getNextButtonClass()), null);
            String nextButtonImageUrl = Resources.getURL(context, daySwitcher.getNextButtonImageUrl(), DaySwitcherRenderer.class, "nextButton.gif");
            writer.startElement("img", daySwitcher);
            writer.writeAttribute("src", nextButtonImageUrl, null);
            writer.endElement("img");
            writer.endElement("td");
        }

        writer.endElement("tr");

        writer.endElement("tbody");
        writer.endElement("table");

        JSONObject stylingParams = getStylingParamsObj(context, daySwitcher);
        Styles.renderStyleClasses(context, daySwitcher);

        ScriptBuilder script = new ScriptBuilder().initScript(context, daySwitcher, "O$.DaySwitcher._init",
                dayTable.getClientId(context),
                DataUtil.formatDateTimeForJs(dayTable.getDay(), timeZone),
                pattern,
                upperPattern,
                locale,
                stylingParams,
                enabled);

        Rendering.renderInitScript(context, script,
                Resources.getUtilJsURL(context),
                Resources.getJsonJsURL(context),
                Resources.getInternalURL(context, DaySwitcherRenderer.class, "daySwitcher.js"));

    }

    private JSONObject getStylingParamsObj(FacesContext context, DaySwitcher daySwitcher) {
        JSONObject stylingParams = new JSONObject();
        Styles.addStyleJsonParam(context, daySwitcher, stylingParams, "rolloverClass",
                daySwitcher.getRolloverStyle(), daySwitcher.getRolloverClass());
        Styles.addStyleJsonParam(context, daySwitcher, stylingParams, "previousButtonRolloverClass",
                daySwitcher.getPreviousButtonRolloverStyle(), daySwitcher.getPreviousButtonRolloverClass());
        Styles.addStyleJsonParam(context, daySwitcher, stylingParams, "previousButtonPressedClass",
                daySwitcher.getPreviousButtonPressedStyle(), daySwitcher.getPreviousButtonPressedClass(), StyleGroup.rolloverStyleGroup());
        Styles.addStyleJsonParam(context, daySwitcher, stylingParams, "nextButtonRolloverClass",
                daySwitcher.getNextButtonRolloverStyle(), daySwitcher.getNextButtonRolloverClass(), StyleGroup.rolloverStyleGroup());
        Styles.addStyleJsonParam(context, daySwitcher, stylingParams, "nextButtonPressedClass",
                daySwitcher.getNextButtonPressedStyle(), daySwitcher.getNextButtonPressedClass(), StyleGroup.rolloverStyleGroup());
        Styles.addStyleJsonParam(context, daySwitcher, stylingParams, "labelRolloverClass",
                daySwitcher.getTextRolloverStyle(), daySwitcher.getTextRolloverClass(), StyleGroup.rolloverStyleGroup());
        Styles.addStyleJsonParam(context, daySwitcher, stylingParams, "supLabelRolloverClass",
                daySwitcher.getUpperTextRolloverStyle(), daySwitcher.getUpperTextRolloverClass(), StyleGroup.rolloverStyleGroup());

        return stylingParams;
    }


}