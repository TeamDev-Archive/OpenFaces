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
package org.openfaces.renderkit.calendar;

import org.openfaces.component.calendar.AbstractDateRange;
import org.openfaces.component.calendar.Calendar;
import org.openfaces.component.calendar.CalendarMonthPopup;
import org.openfaces.component.calendar.CalendarYearPopup;
import org.openfaces.component.calendar.DateRanges;
import org.openfaces.component.calendar.SimpleDateRange;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.*;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Kharchenko
 */
public class CalendarRenderer extends RendererBase {
    private static final String DEFAULT_CLASS = "o_calendar";
    private static final String DEFAULT_DAY_CLASS = "o_calendar_day";
    private static final String DEFAULT_ROLLOVER_DAY_CLASS = "o_calendar_rollover_day";
    private static final String DEFAULT_INACTIVE_MONTH_DAY_CLASS = "o_calendar_inactive_month_day";
    private static final String DEFAULT_ROLLOVER_INACTIVE_MONTH_DAY_CLASS = "o_calendar_rollover_inactive_month_day";
    private static final String DEFAULT_ROLLOVER_SELECTED_DAY_CLASS = "";
    private static final String DEFAULT_TODAY_CLASS = "o_calendar_today";
    private static final String DEFAULT_ROLLOVER_TODAY_CLASS = "o_calendar_rollover_today";
    private static final String DEFAULT_WEEKEND_DAY_CLASS = "o_calendar_weekend_day";
    private static final String DEFAULT_ROLLOVER_WEEKEND_DAY_CLASS = "o_calendar_rollover_weekend_day";

    private static final String DEFAULT_DISABLED_DAY_CLASS = "o_calendar_disabled_day";
    private static final String DEFAULT_ROLLOVER_DISABLED_DAY_CLASS = "";

    private static final String DEFAULT_DAYS_HEADER_CLASS = "o_calendar_days_header";

    private static final String DEFAULT_HEADER_CLASS = "o_calendar_header";
    private static final String DEFAULT_FOOTER_CLASS = "o_calendar_footer";

    private static final String DEFAULT_BODY_CLASS = "";

    private static final String DEFAULT_DATE_RANGES_CLASS = "";
    private static final String DEFAULT_ROLLOVER_DATE_RANGES_CLASS = "";
    private static final String DEFAULT_DATE_RANGE_CLASS = "";
    private static final String DEFAULT_ROLLOVER_DATE_RANGE_CLASS = "";

    public static final String MONTH_DECREASE_SELECTOR_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "month_decrease";
    public static final String MONTH_INCREASE_SELECTOR_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "month_increase";
    public static final String MONTH_SELECTOR_SUFFIX = Rendering.SERVER_ID_SUFFIX_SEPARATOR + "month";
    public static final String DROP_SUFFIX = Rendering.SERVER_ID_SUFFIX_SEPARATOR + "drop";
    public static final String YEAR_DECREASE_SELECTOR_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "year_decrease";
    public static final String YEAR_INCREASE_SELECTOR_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "year_increase";
    public static final String YEAR_SELECTOR_SUFFIX = Rendering.SERVER_ID_SUFFIX_SEPARATOR + "year";
    public static final String WEEK_NUMBER_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "week_num";
    public static final String TODAY_SELECTOR_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "today";
    public static final String NONE_SELECTOR_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "none";
    public static final String VALUE_HOLDER_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "long_date_holder";
    public static final String VALUE_DATE_HOLDER_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "date_holder";
    private static final String RIGHT_ARROW_ENTITY = "&#x25c4;";
    private static final String LEFT_ARROW_ENTITY = "&#x25ba;";
    public static final String BODY_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "body";

    public static final String HIDE_DEFAULT_FOCUS_KEY = "hideDefaultFocusStyle";

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        Calendar calendar = ((Calendar) component);
        String newValue = params.get(component.getClientId(context) + VALUE_DATE_HOLDER_SUFFIX);
        if (null != newValue) {
            calendar.setSubmittedValue(newValue);
        }
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;

        Components.generateIdIfNotSpecified(component);

        Calendar calendar = (Calendar) component;
        Rendering.registerDateTimeFormatObject(calendar.getLocale());

        String clientId = component.getClientId(context);
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("table", calendar);
        context.getResponseWriter().writeAttribute("id", clientId, null);
        Rendering.writeStandardEvents(writer, calendar);

        writeAttribute(writer, "cellpadding", "0");
        writeAttribute(writer, "cellspacing", "0");
        writeAttribute(writer, "border", "0");

        String calendarStyleClass = Styles.getCSSClass(context, calendar, calendar.getStyle(), StyleGroup.regularStyleGroup(), calendar.getStyleClass(),
                getDefaultClass());

        if (calendar.isDisabled()) {
            String disabledCalendarStyleClass = Styles.getCSSClass(context, calendar, calendar.getDisabledStyle(),
                    StyleGroup.disabledStyleGroup(), calendar.getDisabledClass(), null);

            if (Rendering.isNullOrEmpty(calendar.getDisabledStyle()) && Rendering.isNullOrEmpty(calendar.getDisabledClass())) {
                calendarStyleClass = Styles.mergeClassNames(disabledCalendarStyleClass, calendarStyleClass);
            } else {
                calendarStyleClass = Styles.mergeClassNames(disabledCalendarStyleClass, Styles.getCSSClass(context, calendar, null
                        , StyleGroup.regularStyleGroup(), null, getDefaultClass()));
            }
        }
        calendarStyleClass = Styles.mergeClassNames(DefaultStyles.CLASS_INITIALLY_INVISIBLE, calendarStyleClass);
        writeAttribute(writer, "class", calendarStyleClass);

        writer.startElement("col", calendar);
        writer.writeAttribute("id", clientId + Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "col", null);
        writer.writeAttribute("span", "7", null);
        writer.writeAttribute("class", "o_calendarCol", null);
        writer.endElement("col");

        renderHeader(context, calendar);
        renderBody(context, calendar);
        if (calendar.isShowFooter())
            renderFooter(context, calendar);

        writer.endElement("table");
    }

    private void encodeAuxiliaryTags(FacesContext context, Calendar calendar) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = calendar.getClientId(context);
        writer.startElement("input", calendar);
        writeAttribute(writer, "id", clientId + VALUE_HOLDER_SUFFIX);
        writeAttribute(writer, "name", clientId + VALUE_HOLDER_SUFFIX);
        writeAttribute(writer, "type", "hidden");
        Object value = calendar.getValue();
        writeAttribute(writer, "value", Rendering.convertToString(context, calendar, value));
        writer.endElement("input");
        writer.startElement("input", calendar);
        writeAttribute(writer, "id", clientId + VALUE_DATE_HOLDER_SUFFIX);
        writeAttribute(writer, "name", clientId + VALUE_DATE_HOLDER_SUFFIX);
        writeAttribute(writer, "type", "hidden");
        writeAttribute(writer, "value", "");
        writer.endElement("input");
        renderScriptsAndStyles(context, calendar);
    }

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        Rendering.encodeClientActions(context, component);
    }

    private DateRanges findDateRanges(Calendar calendar) {
        boolean oneFound = false;
        List<UIComponent> children = calendar.getChildren();
        DateRanges result = null;
        for (UIComponent child : children) {
            if (child instanceof DateRanges) {
                if (oneFound)
                    throw new FacesException("There should be only one 'dateRanges' component declaration inside " +
                            "'calendar'");
                oneFound = true;
                result = (DateRanges) child;
            }
        }
        return result;
    }

    private void renderScriptsAndStyles(FacesContext context, UIComponent component) throws IOException {
        Calendar calendar = ((Calendar) component);

        String clientId = component.getClientId(context);
        ScriptBuilder sb = new ScriptBuilder();

        String onchange = calendar.getOnchange();
        if (onchange != null && onchange.length() > 0) {
            sb.append("O$('").append(clientId).append("')._onDateChange = " +  // todo: refactor passing events into passing them as a single JSON param to the initialization function
                    "function(event) {").append(onchange).append("};\n");
        }

        String onPeriodChange = calendar.getOnperiodchange();
        if (onPeriodChange != null && onPeriodChange.length() > 0) {
            sb.append("O$('").append(clientId).append("')._onPeriodChange = " +
                    "function(event) {").append(onPeriodChange).append("};\n");
        }

        TimeZone timeZone = (calendar.getTimeZone() != null)
                ? calendar.getTimeZone()
                : TimeZone.getDefault();
        String selectedDateParam = null;
        Date selectedDate = (Date) calendar.getValue();
        if (selectedDate != null) {
            selectedDateParam = DataUtil.formatDateForJs(selectedDate, timeZone);
        }

        Date todayDate = java.util.Calendar.getInstance().getTime();
        String todayDateStr = DataUtil.formatDateForJs(todayDate, timeZone);

        String focusedClassStr;
        if (calendar.getAttributes().get(HIDE_DEFAULT_FOCUS_KEY) == null)
            focusedClassStr = Rendering.getFocusedClass(context, calendar);
        else
            focusedClassStr = Styles.getCSSClass(context, calendar, calendar.getFocusedStyle(), StyleGroup.selectedStyleGroup(), calendar.getFocusedClass(), getDefaultClass());

        Script rangesArray = getJSDateRangesArray(context, calendar);

        sb.initScript(context, calendar, "O$.Calendar._init",
                selectedDateParam,
                todayDateStr,
                Rendering.getRolloverClass(context, calendar),
                calendar.isFocusable(),
                focusedClassStr,
                getStyleClassesWithFont(context, calendar.getDayStyle(), calendar.getDayClass(), DEFAULT_DAY_CLASS, calendar),
                new StyleParam(calendar, "rolloverDay", DEFAULT_ROLLOVER_DAY_CLASS, StyleGroup.rolloverStyleGroup()),
                new StyleParam(calendar, "inactiveMonthDay", DEFAULT_INACTIVE_MONTH_DAY_CLASS, StyleGroup.regularStyleGroup(3)),
                new StyleParam(calendar, "rolloverInactiveMonthDay", DEFAULT_ROLLOVER_INACTIVE_MONTH_DAY_CLASS, StyleGroup.rolloverStyleGroup(3)),
                Styles.getCSSClass_dontCascade(context, component, calendar.getSelectedDayStyle(), StyleGroup.selectedStyleGroup(), calendar.getSelectedDayClass(), DefaultStyles.getDefaultSelectionStyle()),
                new StyleParam(calendar, "rolloverSelectedDay", DEFAULT_ROLLOVER_SELECTED_DAY_CLASS, StyleGroup.selectedStyleGroup(1)),
                new StyleParam(calendar, "today", DEFAULT_TODAY_CLASS, StyleGroup.regularStyleGroup(5)),
                new StyleParam(calendar, "rolloverToday", DEFAULT_ROLLOVER_TODAY_CLASS, StyleGroup.rolloverStyleGroup(5)),
                new StyleParam(calendar, "weekendDay", DEFAULT_WEEKEND_DAY_CLASS, StyleGroup.regularStyleGroup(2)),
                new StyleParam(calendar, "rolloverWeekendDay", DEFAULT_ROLLOVER_WEEKEND_DAY_CLASS, StyleGroup.rolloverStyleGroup(2)),
                new StyleParam(calendar, "disabledDay", DEFAULT_DISABLED_DAY_CLASS, StyleGroup.rolloverStyleGroup(2)),
                new StyleParam(calendar, "rolloverDisabledDay", DEFAULT_ROLLOVER_DISABLED_DAY_CLASS, StyleGroup.rolloverStyleGroup()),
                calendar.getFirstDayOfWeek() - 1, // in JS Date object first day is less by one that in corresponding Java object

                getStyleClassesWithFont(context, calendar.getHeaderStyle(), calendar.getHeaderClass(), DEFAULT_HEADER_CLASS, calendar),

                rangesArray,
                calendar.isShowFooter(),
                calendar.getLocale().toString(),
                calendar.isRequired(),
                calendar.isDisabled());

        Styles.renderStyleClasses(context, calendar);
        Rendering.renderInitScript(context, sb,
                Resources.getUtilJsURL(context),
                Resources.getInternalURL(context, CalendarRenderer.class, "calendar.js"),
                Resources.getInternalURL(context, CalendarRenderer.class, "dateRange.js")
        );
    }

    private String getDefaultClass() {
        return DEFAULT_CLASS + ' ' + DefaultStyles.getTextColorClass() + ' ' +
                DefaultStyles.getBackgroundColorClass();
    }

    private String getStyleClassesWithFont(FacesContext context, String style, String cls, String defaultClass, UIComponent component) {
        String resultCls = Styles.getCSSClass(context, component, style, defaultClass, cls);
        resultCls = Styles.mergeClassNames(resultCls, DefaultStyles.CLASS_CALENDAR_FONT_FAMILY);
        return resultCls;
    }

    private void renderHeader(FacesContext context, Calendar calendar) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("tbody", calendar);
        writer.startElement("tr", calendar);
        writer.startElement("td", calendar);
        writer.writeAttribute("colspan", "7", null);

        writer.startElement("table", calendar);
        writeHeaderStyleAndClassAttributes(context, calendar);
        writer.startElement("tr", calendar);
        renderMonthSelectorSection(context, calendar);
        renderYearSelectorSection(context, calendar);
        writer.endElement("tr");
        writer.endElement("table");

        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("tbody");
    }

    private void renderMonthSelectorSection(FacesContext context, Calendar calendar) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = calendar.getClientId(context);

        writer.startElement("td", calendar);
        writeAttribute(writer, "style", "width: 1%;");

        writer.startElement("div", calendar);
        writeAttribute(writer, "id", clientId + MONTH_DECREASE_SELECTOR_SUFFIX);
        if (calendar.isDisabled()) {
            writeAttribute(writer, "style", "cursor: default; color:#808080;  width: 1%; font-size: 68%; font-family: Arial;");
        } else {
            writeAttribute(writer, "style", "cursor: pointer; width: 1%; font-size: 68%; font-family: Arial;");
        }
        writer.write(RIGHT_ARROW_ENTITY); // black left-oriented triangle
        writer.endElement("div");

        writer.endElement("td");

        renderMonthSelector(context, calendar);

        writer.startElement("td", calendar);
        writeAttribute(writer, "style", "width: 1%;");

        writer.startElement("div", calendar);
        writeAttribute(writer, "id", clientId + MONTH_INCREASE_SELECTOR_SUFFIX);
        if (calendar.isDisabled()) {
            writeAttribute(writer, "style", "cursor: default; color:#808080;  width: 1%; font-size: 68%; font-family: Arial;");
        } else {
            writeAttribute(writer, "style", "cursor: pointer; width: 1%; font-size: 68%; font-family: Arial;");
        }
        writer.write(LEFT_ARROW_ENTITY); // black right-oriented triangle
        writer.endElement("div");

        writer.endElement("td");
    }

    private void renderMonthSelector(FacesContext context, Calendar calendar) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = calendar.getClientId(context);
        writer.startElement("td", calendar);
        writeAttribute(writer, "style", "width: 1%;");

        writer.startElement("div", calendar);
        writeAttribute(writer, "id", clientId + MONTH_SELECTOR_SUFFIX);
        if (calendar.isDisabled()) {
            writeAttribute(writer, "style", "cursor: default; color:#808080; white-space: nowrap; text-align: center; width: 52px;"); // todo: replace inplace style declaration with using a class declared in default.css
        } else {
            writeAttribute(writer, "style", "cursor: pointer; white-space: nowrap; text-align: center; width: 52px;");
        }
        writer.endElement("div");

        CalendarMonthPopup popup =
                (CalendarMonthPopup) context.getApplication().createComponent(CalendarMonthPopup.COMPONENT_TYPE);
        List<UIComponent> children = calendar.getChildren();
        for (UIComponent child : children) {
            if (child instanceof CalendarMonthPopup
                    && child.getClientId(context).indexOf(MONTH_SELECTOR_SUFFIX + DROP_SUFFIX) > -1) {
                children.remove(child);
                break;
            }
        }
        children.add(popup);

        popup.setId(calendar.getId() + MONTH_SELECTOR_SUFFIX + DROP_SUFFIX);

        popup.encodeBegin(context);
        popup.encodeChildren(context);
        popup.encodeEnd(context);

        writer.endElement("td");

    }

    private void renderYearSelectorSection(FacesContext context, Calendar calendar) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = calendar.getClientId(context);

        writer.startElement("td", calendar);
        writeAttribute(writer, "style", "text-align: right;");

        writer.startElement("span", calendar);
        writeAttribute(writer, "id", clientId + YEAR_DECREASE_SELECTOR_SUFFIX);
        if (calendar.isDisabled()) {
            writeAttribute(writer, "style", "cursor: default; color:#808080;  width: 1%; text-align: right; font-size: 68%; font-family: Arial;");
        } else {
            writeAttribute(writer, "style", "cursor: pointer; width: 1%; text-align: right; font-size: 68%; font-family: Arial;");
        }
        writer.write(RIGHT_ARROW_ENTITY);
        writer.endElement("span");

        writer.endElement("td");

        writer.startElement("td", calendar);
        writeAttribute(writer, "style", "width: 1%;");

        renderYearSelector(context, calendar);

        writer.endElement("td");

        writer.startElement("td", calendar);
        writeAttribute(writer, "style", "width: 1%;");

        writer.startElement("div", calendar);
        writeAttribute(writer, "id", clientId + YEAR_INCREASE_SELECTOR_SUFFIX);
        if (calendar.isDisabled()) {
            writeAttribute(writer, "style", "cursor: default; color:#808080;  width: 1%; font-size: 68%; font-family: Arial;");
        } else {
            writeAttribute(writer, "style", "cursor: pointer; width: 1%; font-size: 68%; font-family: Arial;");
        }
        writer.write(LEFT_ARROW_ENTITY);
        writer.endElement("div");

        writer.endElement("td");

    }

    private void writeHeaderStyleAndClassAttributes(FacesContext context, Calendar calendar) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writeAttribute(writer, "style", calendar.getHeaderStyle());
        writeHeaderClassAtribute(context, calendar);
    }

    private void writeHeaderClassAtribute(FacesContext context, Calendar calendar) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String cls = getStyleClassesWithFont(context, null, calendar.getHeaderClass(), DEFAULT_HEADER_CLASS, calendar);
        writeAttribute(writer, "class", cls);
    }

    private void renderYearSelector(FacesContext context, Calendar calendar) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = calendar.getClientId(context);
        writer.startElement("div", calendar);
        writeAttribute(writer, "id", clientId + YEAR_SELECTOR_SUFFIX);
        if (calendar.isDisabled()) {
            writeAttribute(writer, "style", "cursor: default; color:#808080;  width: 1%; text-align: center;");
        } else {
            writeAttribute(writer, "style", "cursor: pointer; width: 1%; text-align: center;");
        }
        writer.endElement("div");

        CalendarYearPopup popup
                = (CalendarYearPopup) context.getApplication().createComponent(CalendarYearPopup.COMPONENT_TYPE);
        List<UIComponent> children = calendar.getChildren();
        for (UIComponent child : children) {
            if (child instanceof CalendarYearPopup
                    && child.getClientId(context).indexOf(YEAR_SELECTOR_SUFFIX + DROP_SUFFIX) > -1) {
                children.remove(child);
                break;
            }
        }
        children.add(popup);

        popup.setId(calendar.getId() + YEAR_SELECTOR_SUFFIX + DROP_SUFFIX);

        popup.encodeBegin(context);
        popup.encodeChildren(context);
        popup.encodeEnd(context);

    }

    private void renderBody(FacesContext context, Calendar calendar) throws IOException {
        String clientId = calendar.getClientId(context);
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("tbody", calendar);
        writer.writeAttribute("id", clientId + BODY_SUFFIX, null);
        String bodyStyle = calendar.getBodyStyle();
        writeAttribute(writer, "style", bodyStyle == null ? "" : bodyStyle);

        String cls = getStyleClassesWithFont(context, null, calendar.getBodyClass(), DEFAULT_BODY_CLASS, calendar);
        writeAttribute(writer, "class", cls);

        writer.startElement("tr", calendar);
        writeAttribute(writer, "style", calendar.getDaysHeaderStyle());
        cls = getStyleClassesWithFont(context, null, calendar.getDaysHeaderClass(), DEFAULT_DAYS_HEADER_CLASS, calendar);
        writeAttribute(writer, "class", cls);

        DateFormatSymbols dfs = new DateFormatSymbols(calendar.getLocale());
        String[] dayNames = dfs.getShortWeekdays();
        int fdw = calendar.getFirstDayOfWeek();
        for (int i = 0; i < 7; i++) {
            int idx = i + fdw;
            if (idx > 7) idx -= 7;
            renderDayNameCell(context, calendar, dayNames[idx]);
        }
        writer.endElement("tr");

        for (int row = 0; row < 6; row++) {
            writer.startElement("tr", calendar);
            writeAttribute(writer, "class", "o_cal_week_row");
            for (int col = 0; col < 7; col++) {
                writer.startElement("td", calendar);
                writer.startElement("div", calendar);
                writer.endElement("div");
                if (col == 6 && row == 5 && !isAuxiliaryTagsRenderedInFooter(calendar))
                    encodeAuxiliaryTags(context, calendar);
                writer.endElement("td");
            }
            writer.endElement("tr");
        }

        writer.endElement("tbody");
    }

    private boolean isAuxiliaryTagsRenderedInFooter(Calendar calendar) {
        return calendar.isShowFooter();
    }

    private void renderDayNameCell(FacesContext context, Calendar calendar, String dayName) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("td", calendar);
        writer.write(dayName);
        writer.endElement("td");
    }

    private void renderFooter(FacesContext context, Calendar calendar) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = calendar.getClientId(context);
        writer.startElement("tbody", calendar);
        writer.startElement("tr", calendar);
        writer.startElement("td", calendar);
        writer.writeAttribute("colspan", "7", null);

        writer.startElement("table", calendar);
        writeAttribute(writer, "style", calendar.getFooterStyle());
        String cls = getStyleClassesWithFont(
                context, calendar.getFooterStyle(), calendar.getFooterClass(),
                DEFAULT_FOOTER_CLASS, calendar);
        writeAttribute(writer, "class", cls);

        writer.startElement("tr", calendar);
        writer.startElement("td", calendar);
        writeAttribute(writer, "style", "text-align: left;");

        Object value = calendar.getValue();

        writer.startElement("div", calendar);
        writeAttribute(writer, "id", clientId + TODAY_SELECTOR_SUFFIX);
        if (value != null) {
            Date date = (Date) value;
            java.util.Calendar valueCal = java.util.Calendar.getInstance();
            valueCal.setTime(date);
            java.util.Calendar todayCal = java.util.Calendar.getInstance();
            todayCal.setTime(new Date());
            int yearFlag = java.util.Calendar.YEAR;
            int monthFlag = java.util.Calendar.MONTH;
            int dayFlag = java.util.Calendar.DATE;
            if (valueCal.get(yearFlag) == todayCal.get(yearFlag)
                    && valueCal.get(monthFlag) == todayCal.get(monthFlag)
                    && valueCal.get(dayFlag) == todayCal.get(dayFlag)) {
                writeAttribute(writer, "style", "width: 1%; white-space: nowrap; color: gray;");
            } else {
                writeAttribute(writer, "style", "cursor: pointer; width: 1%; white-space: nowrap;");
            }
        } else {
            writeAttribute(writer, "style", "cursor: pointer; width: 1%; white-space: nowrap;");
        }
        writer.write(calendar.getTodayText());
        writer.endElement("div");

        writer.endElement("td");

        writer.startElement("td", calendar);
        writeAttribute(writer, "style", "text-align: right;");

        writer.startElement("span", calendar);
        writeAttribute(writer, "id", clientId + NONE_SELECTOR_SUFFIX);

        if (calendar.isRequired() || value == null) {
            writeAttribute(writer, "style", "width: 1%; white-space: nowrap; color: gray;");
        } else {
            writeAttribute(writer, "style", "cursor: pointer; width: 1%; white-space: nowrap;");
        }
        writer.write(calendar.getNoneText());
        writer.endElement("span");
        encodeAuxiliaryTags(context, calendar);

        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");

        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("tbody");
    }

    private Script getJSDateRangesArray(FacesContext context, Calendar calendar) {
        List<Object[]> dateRangesList = new ArrayList<Object[]>();
        DateRanges dateRanges = findDateRanges(calendar);
        String dateRangesStyleClassName = null;
        String dateRangesRolloverStyleClassName = null;
        String selectedDayClassName = null;
        String rolloverSelectedClassName = null;
        boolean disableExcluded = false;
        boolean disableIncluded = false;

        if (dateRanges != null) {
            ValueExpression ve = dateRanges.getValueExpression("value");
            Object ranges = null;
            if (ve != null) {
                ranges = ve.getValue(context.getELContext());
            }
/*
      if (dateRanges.getChildCount() == 0 && ranges == null)
        throw new FacesException("At least one date range should be declared inside " +
                "'dateRanges' component or value binding to 'value' property specified.");
*/
            dateRangesStyleClassName = Styles.getCSSClass(context, calendar, dateRanges.getDayStyle(),
                    StyleGroup.regularStyleGroup(4), dateRanges.getDayClass(), DEFAULT_DATE_RANGES_CLASS);
            dateRangesRolloverStyleClassName = Styles.getCSSClass(context, calendar, dateRanges.getRolloverDayStyle(),
                    StyleGroup.rolloverStyleGroup(4), dateRanges.getRolloverDayClass(), DEFAULT_ROLLOVER_DATE_RANGES_CLASS);
            selectedDayClassName = Styles.getCSSClass(context, calendar, dateRanges.getSelectedDayStyle(),
                    StyleGroup.selectedStyleGroup(2), dateRanges.getSelectedDayClass());
            rolloverSelectedClassName = Styles.getCSSClass(context, calendar, dateRanges.getRolloverSelectedDayStyle(),
                    StyleGroup.selectedStyleGroup(3), dateRanges.getRolloverSelectedDayClass());
            disableExcluded = dateRanges.isDisableExcludes();
            disableIncluded = dateRanges.isDisableIncludes();
            List<UIComponent> allRanges = dateRanges.getChildren();
            for (Object dateRangeObj : allRanges) {
                AbstractDateRange dateRange = (AbstractDateRange) dateRangeObj;
                if (dateRange instanceof SimpleDateRange) {
                    String styleClassName = Styles.getCSSClass(context, calendar, dateRange.getDayStyle(),
                            StyleGroup.regularStyleGroup(5), dateRange.getDayClass(), DEFAULT_DATE_RANGE_CLASS);
                    String rolloverStyleClassName = Styles.getCSSClass(context, calendar, dateRange.getRolloverDayStyle(),
                            StyleGroup.rolloverStyleGroup(5), dateRange.getRolloverDayClass(), DEFAULT_ROLLOVER_DATE_RANGE_CLASS);
                    String selectedDayClassNameDR = Styles.getCSSClass(context, calendar, dateRange.getSelectedDayStyle(),
                            StyleGroup.selectedStyleGroup(4), dateRanges.getSelectedDayClass());
                    String rolloverSelectedDayClassNameDR = Styles.getCSSClass(context,
                            calendar, dateRange.getRolloverSelectedDayStyle(), StyleGroup.selectedStyleGroup(5), dateRange.getRolloverSelectedDayClass()
                    );
                    dateRangesList.add(new Object[]{dateRange, styleClassName, rolloverStyleClassName, selectedDayClassNameDR,
                            rolloverSelectedDayClassNameDR});
                } else
                    throw new FacesException("'dateRanges' component does not support components of " + dateRange.getClass() +
                            " class");
            }
            if (ranges != null) {
                Collection rangesCollection = (Collection) ranges;
                for (Object dateRangeObj : rangesCollection) {
                    AbstractDateRange dateRange = (AbstractDateRange) dateRangeObj;
                    if (dateRange instanceof SimpleDateRange) {
                        String styleClassName = Styles.getCSSClass(context, calendar, dateRange.getDayStyle(), StyleGroup.regularStyleGroup(5), null);
                        String rolloverStyleClassName = Styles.getCSSClass(context, calendar, dateRange.getRolloverDayStyle(), StyleGroup.rolloverStyleGroup(5), null);
                        String selectedDayClassNameDR = Styles.getCSSClass(context, calendar, dateRange.getSelectedDayStyle(),
                                StyleGroup.selectedStyleGroup(4), dateRange.getSelectedDayClass());
                        String rolloverSelectedDayClassNameDR = Styles.getCSSClass(context,
                                calendar, dateRange.getRolloverSelectedDayStyle(), StyleGroup.selectedStyleGroup(5), dateRange.getRolloverSelectedDayClass()
                        );
                        dateRangesList.add(new Object[]{dateRange, styleClassName, rolloverStyleClassName,
                                selectedDayClassNameDR, rolloverSelectedDayClassNameDR});
                    } else
                        throw new FacesException("'dateRanges' component does not support component of " + dateRange.getClass() +
                                " class");
                }
            }
        }

        if (dateRangesList.size() == 0 && dateRanges == null)
            return null;

        List<NewInstanceScript> dateRangesParam = new ArrayList<NewInstanceScript>();
        for (Object[] dateRangeObj : dateRangesList) {
            AbstractDateRange dateRange = (AbstractDateRange) dateRangeObj[0];
            String rangeStyle = (String) dateRangeObj[1];
            String rangeRolloverStyle = (String) dateRangeObj[2];
            String rangeSelectedDayStyle = (String) dateRangeObj[3];
            String rangeRolloverSelectedDayStyle = (String) dateRangeObj[4];
            if (dateRange instanceof SimpleDateRange) {
                SimpleDateRange simpleDateRange = ((SimpleDateRange) dateRange);
                Date fromDate = simpleDateRange.getFromDate();
                Date toDate = simpleDateRange.getToDate();
                boolean isDRValid = !(fromDate == null || toDate == null) && !fromDate.after(toDate);
                if (isDRValid) {
                    dateRangesParam.add(new NewInstanceScript("O$.SimpleDateRange",
                            new NewInstanceScript("Date", fromDate.getTime()),
                            new NewInstanceScript("Date", toDate.getTime()),
                            rangeStyle,
                            rangeRolloverStyle,
                            rangeSelectedDayStyle,
                            rangeRolloverSelectedDayStyle));
                }
            }
        }

        return new NewInstanceScript("O$.DateRanges",
                dateRangesParam,
                dateRangesStyleClassName,
                dateRangesRolloverStyleClassName,
                disableExcluded,
                disableIncluded,
                selectedDayClassName,
                rolloverSelectedClassName);
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        Date convertedValue = (Date) Rendering.convertFromString(context, component, (String) submittedValue);
        Calendar calendar = (Calendar) component;
        boolean keepTime = calendar.isKeepTime();
        if (!keepTime) {
            return convertedValue;
        }
        Date currentValue = (Date) calendar.getValue();
        TimeZone timeZone = calendar.getTimeZone();
        DataUtil.copyDateKeepingTime(convertedValue, currentValue, timeZone);
        return currentValue;
    }
}
