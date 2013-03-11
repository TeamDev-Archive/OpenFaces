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

import org.openfaces.component.timetable.AbstractTimetableEvent;
import org.openfaces.component.timetable.MonthTable;
import org.openfaces.component.timetable.ScrollButton;
import org.openfaces.component.timetable.ScrollDirection;
import org.openfaces.component.timetable.Timetable;
import org.openfaces.component.timetable.TimetableResource;
import org.openfaces.component.timetable.TimetableView;
import org.openfaces.component.timetable.UITimetableEvent;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.TableRenderer;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.DataUtil;
import org.openfaces.util.Log;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Roman Porotnikov
 */
public class MonthTableRenderer extends TimetableViewRenderer {

    private static String EXPANDED_VIEW_SUFFIX = "::expandedDayView";

    private static final String DEFAULT_EXPANDED_DAY_VIEW_CLASS = "o_expandedDayView";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered())
            return;

        MonthTable timetableView = (MonthTable) component;
        Rendering.registerDateTimeFormatObject(timetableView.getLocale());
        AjaxUtil.prepareComponentForAjax(context, timetableView);
        ResponseWriter writer = context.getResponseWriter();
        String clientId = timetableView.getClientId(context);
        Styles.renderStyleClasses(context, timetableView);
        writer.startElement("table", timetableView);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("border", "0", null);
        Timetable timetable = timetableView.getTimetable();
        writer.writeAttribute("class", Styles.getCSSClass(context,
                timetableView, timetableView.getStyle(),
                timetable == null ? "o_timetableView" : "o_timetableView o_timetableView_embedded",
                timetableView.getStyleClass()), null);
        Rendering.writeStandardEvents(writer, timetableView);
        writer.startElement("tbody", timetableView);

        renderHeader(context, timetableView);

        renderWeekdayHeadersRow(context, timetableView, clientId);

        ValueExpression resourcesExpression = timetableView.getResourcesValueExpression();
        List<TimetableResource> resources = resourcesExpression != null
                ? DataUtil.readDataModelExpressionAsList(context, resourcesExpression)
                : Collections.<TimetableResource>emptyList();

        writer.startElement("tr", timetableView);
        writer.writeAttribute("class", "o_timetableView_tableRow", null);
        writer.startElement("td", timetableView);
        writer.writeAttribute("style", "height: 100%", null);

        renderContentTable(context, timetableView, clientId, resources);

        encodeEventEditor(context, timetableView, resources);
        encodeActionBar(context, timetableView);

        writer.endElement("td");
        writer.endElement("tr");

        renderFooter(context, timetableView);

        writer.endElement("tbody");
        writer.endElement("table");
        timetableView.getExpandedDayViewFooter();



    }

    private void renderExpandedDayView(FacesContext context, MonthTable monthTable) throws IOException{
        UIComponent header = monthTable.getExpandedDayViewHeader();
        if (header == null ){
            header = new ScrollButton(ScrollDirection.UP);
            header.setParent(monthTable);
        }
        UIComponent footer = monthTable.getExpandedDayViewFooter();
        if (footer == null ){
            footer = new ScrollButton(ScrollDirection.DOWN);
            footer.setParent(monthTable);
        }
        String expandDayViewId = monthTable.getClientId(context) + EXPANDED_VIEW_SUFFIX;
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", monthTable);

        writer.writeAttribute("id", expandDayViewId , null);

        writer.writeAttribute("class",Styles.getCSSClass(context, monthTable, monthTable.getExpandedDayViewStyle(), StyleGroup.regularStyleGroup(),
                monthTable.getExpandedDayViewClass(), getDefaultExpandedDayViewClass()),null);
        writer.writeAttribute("style", "position: absolute;", null);

        writer.startElement("div", monthTable);
        //TODO: rework with z-index
        writer.writeAttribute("style", "width: 100%; z-index: 213123; position: relative;", null);
        writer.writeAttribute("id", expandDayViewId + "::header" , null);
        header.encodeAll(context);
        writer.endElement("div");

        writer.startElement("div", monthTable);
        writer.writeAttribute("style", "height: 100%; overflow:hidden; position: relative;", null);
        writer.writeAttribute("id", expandDayViewId + "::eventBlock" , null);
        writer.endElement("div");


        writer.startElement("div", monthTable);
        //TODO: move ID to static
        writer.writeAttribute("id", expandDayViewId + "::footer" , null);
        //TODO: rework with z-index
        writer.writeAttribute("style", "width: 100%; position: relative; ", null);
        footer.encodeAll(context);
        writer.endElement("div");

        writer.endElement("div");
    }


    private void renderWeekdayHeadersRow(FacesContext context, final MonthTable timetableView, String clientId) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(timetableView.getDay());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, 1 - dayOfWeek);

        writer.startElement("tr", timetableView);
        writer.startElement("td", timetableView);

        new TableRenderer(clientId + Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "weekdayHeaders", 0, 0, 0, "o_weekdayHeadersTable")
                .render(timetableView, true, 1, 8);

        writer.endElement("td");
        writer.endElement("tr");
    }

    private void renderContentTable(
            FacesContext context,
            final MonthTable timetableView,
            final String clientId,
            final List<TimetableResource> resources) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", timetableView);
        writer.writeAttribute("id", clientId + "::scroller", null);
        writer.writeAttribute("style",  "overflow : visible; overflow-x : visible; overflow-y = hidden;", null);
        writer.writeAttribute("class", "o_timetableView_scroller", null);
        renderExpandedDayView(context, timetableView);
        int colCount = 7;

        new TableRenderer(clientId + Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "table", 0, 0, 0, "o_timetableView_table") {

            protected void encodeTFoot(ResponseWriter writer, UIComponent component) throws IOException {
                writer.startElement("tfoot", component);
                writer.writeAttribute("id", clientId + "::hiddenArea", null);
                writer.writeAttribute("style", "display: none", null);
                writer.startElement("tr", component);
                writer.startElement("td", component);

                FacesContext context = FacesContext.getCurrentInstance();
                encodeEventPreview(context, timetableView);
                encodeInitScript(context, timetableView, resources);

                writer.endElement("td");
                writer.endElement("tr");
                writer.endElement("tfoot");
            }

        }.render(timetableView, true, 0, colCount);

        writer.endElement("div");
    }

    private void encodeInitScript(FacesContext context, MonthTable timetableView, List<TimetableResource> resources) throws IOException {
        Map<String, TimeZone> timeZoneParam = getTimeZoneParamForJSONConverter(timetableView);
        JSONArray resourcesJsArray = DataUtil.listToJSONArray(resources, timeZoneParam);
        timetableView.getAttributes().put(USE_RESOURCE_SEPARATION_MODE_ATTR, resourcesJsArray.length() > 0);

        String clientId = timetableView.getClientId(context);

        JSONObject editingOptions = getEditingOptionsObj(timetableView);
        JSONObject stylingParams = getStylingParamsObj(context, timetableView);
        JSONObject calendarOptions = getCalendarOptionsObj(timetableView);

        TimeZone timeZone = (timetableView.getTimeZone() != null)
                ? timetableView.getTimeZone()
                : TimeZone.getDefault();

        boolean editable = timetableView.isEditable();
        boolean thereIsChangeListener = timetableView.getTimetableChangeListener() != null ||
                timetableView.getTimetableChangeListeners().length > 0;
        if (editable && !thereIsChangeListener) {
            Log.log(context, "The " + getComponentName() + " with clientID=[" + clientId + "] is set to be editable, but is not configured to accept the changes. " +
                    "You should either make it read-only explicitly (using editable=\"false\" attribute), or define timetableChangeListener attriubte to accept the changes (see " + getComponentName() + " reference).");
            editable = false;
        }
        List<AbstractTimetableEvent> events = new ArrayList<AbstractTimetableEvent>();
        JSONObject eventParams = composeEventParams(context, timetableView, events);
        Map<String, AbstractTimetableEvent> loadedEvents = timetableView.getLoadedEvents();
        loadedEvents.clear();
        for (AbstractTimetableEvent event : events) {
            loadedEvents.put(event.getId(), event);
        }


        JSONArray areaSettings = encodeEventAreas(context, timetableView, events);

        UITimetableEvent uiEvent = timetableView.getUITimetableEvent();

        Timetable timetable = timetableView.getTimetable();

        try {
            Rendering.renderInitScript(context,
                    new ScriptBuilder().initScript(
                            context,
                            timetableView,
                            "O$.MonthTable._init",
                            DataUtil.formatDateTimeForJs(timetableView.getDay(), timeZone),
                            timetableView.getLocale(),
                            "MMMM, dd yyyy",
                            timetableView.getScrollOffset(),
                            eventParams,
                            resourcesJsArray,
                            areaSettings,
                            editable,
                            timetableView.getOnchange(),
                            editingOptions,
                            stylingParams,
                            uiEvent != null ? uiEvent.toJSONObject(null) : null,
                            calendarOptions,
                            timetable
                    ),
                    Resources.utilJsURL(context),
                    Resources.jsonJsURL(context),
                    TableUtil.getTableUtilJsURL(context),
                    Resources.internalURL(context, "timetable/rangeMap.js"),
                    Resources.internalURL(context, "timetable/timetable.js"),
                    Resources.internalURL(context, "timetable/timeTableView.js"),
                    Resources.internalURL(context, "timetable/monthTable.js")
            );
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    private JSONObject getCalendarOptionsObj(MonthTable monthTable) {
        JSONObject calendarOptionsObj = new JSONObject();

        int firstDayOfWeek = monthTable.getFirstDayOfWeek() - 1; // JS weekdays are 0-based while Java weekdays are 1-based
        Rendering.addJsonParam(calendarOptionsObj, "firstDayOfWeek", firstDayOfWeek);
        return calendarOptionsObj;
    }

    private JSONObject composeEventParams(FacesContext context, MonthTable timetableView, List putEventsHere) {
        Map<String, TimeZone> timeZoneParam = getTimeZoneParamForJSONConverter(timetableView);

        ValueExpression eventsExpression = timetableView.getEventsValueExpression();
        List events = eventsExpression == null ? Collections.EMPTY_LIST : DataUtil.readDataModelExpressionAsList(context, eventsExpression);
        if (putEventsHere != null) {
            putEventsHere.clear();
            putEventsHere.addAll(events);
        }

        JSONArray eventsJsArray = DataUtil.listToJSONArray(events, timeZoneParam);

        JSONObject eventParams = new JSONObject();
        try {
            eventParams.put("events", eventsJsArray);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return eventParams;
    }


    private JSONObject getStylingParamsObj(FacesContext context, MonthTable timetableView) {
        JSONObject stylingParams = new JSONObject();

        Styles.addStyleJsonParam(context, timetableView, stylingParams, "rolloverClass",
                timetableView.getRolloverStyle(), timetableView.getRolloverClass());

        Rendering.addJsonParam(stylingParams, "defaultEventColor", timetableView.getDefaultEventColor());
        Rendering.addJsonParam(stylingParams, "reservedTimeEventColor", timetableView.getReservedTimeEventColor());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "reservedTimeEventClass",
                timetableView.getReservedTimeEventStyle(), timetableView.getReservedTimeEventClass());

        Styles.addStyleJsonParam(context, timetableView, stylingParams, "weekdayHeadersRowClass",
                timetableView.getWeekdayHeadersRowStyle(), timetableView.getWeekdayHeadersRowClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "weekdayClass",
                timetableView.getWeekdayStyle(), timetableView.getWeekdayClass());
        Rendering.addJsonParam(stylingParams, "weekdayPattern", timetableView.getWeekdayPattern());
        Rendering.addJsonParam(stylingParams, "defaultEventColor", timetableView.getDefaultEventColor());
        Rendering.addJsonParam(stylingParams, "weekdayHeadersRowSeparator", timetableView.getWeekdayHeadersRowSeparator());
        Rendering.addJsonParam(stylingParams, "weekdayColumnSeparator", timetableView.getWeekdayColumnSeparator());
        Rendering.addJsonParam(stylingParams, "rowSeparator", timetableView.getRowSeparator());
        Rendering.addJsonParam(stylingParams, "columnSeparator", timetableView.getColumnSeparator());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "dayHeaderRowClass",
                timetableView.getDayHeaderRowStyle(), timetableView.getDayHeaderRowClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "rowClass",
                timetableView.getRowStyle(), timetableView.getRowClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "weekdayHeaderCellClass",
                timetableView.getWeekdayHeaderCellStyle(), timetableView.getWeekdayHeaderCellClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "weekendWeekdayHeaderCellClass",
                timetableView.getWeekendWeekdayHeaderCellStyle(), timetableView.getWeekendWeekdayHeaderCellClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "cellHeaderClass",
                timetableView.getCellHeaderStyle(), timetableView.getCellHeaderClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "cellClass",
                timetableView.getCellStyle(), timetableView.getCellClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "todayCellHeaderClass",
                timetableView.getTodayCellHeaderStyle(), timetableView.getTodayCellHeaderClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "todayCellClass",
                timetableView.getTodayCellStyle(), timetableView.getTodayCellClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "weekendCellHeaderClass",
                timetableView.getWeekendCellHeaderStyle(), timetableView.getWeekendCellHeaderClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "weekendCellClass",
                timetableView.getWeekendCellStyle(), timetableView.getWeekendCellClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "inactiveMonthCellHeaderClass",
                timetableView.getInactiveMonthCellHeaderStyle(), timetableView.getInactiveMonthCellHeaderClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "inactiveMonthCellClass",
                timetableView.getInactiveMonthCellStyle(), timetableView.getInactiveMonthCellClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "moreLinkElementClass",
                timetableView.getMoreLinkElementStyle(), timetableView.getMoreLinkElementClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "moreLinkClass",
                timetableView.getMoreLinkStyle(), timetableView.getMoreLinkClass());
        Rendering.addJsonParam(stylingParams, "moreLinkText", timetableView.getMoreLinkText());
        Rendering.addJsonParam(stylingParams, "expandTransitionPeriod", timetableView.getExpandTransitionPeriod().intValue());

        return stylingParams;
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        MonthTable timetableView = (MonthTable) component;

        Map<String, String> requestParams = context.getExternalContext().getRequestParameterMap();
        String clientId = timetableView.getClientId(context);
        String dayStr = requestParams.get(clientId + Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "day");
        if (dayStr != null) {
            TimeZone timeZone = (timetableView.getTimeZone() != null)
                    ? timetableView.getTimeZone()
                    : TimeZone.getDefault();
            Date day = DataUtil.parseDateFromJs(dayStr, timeZone);
            timetableView.setDay(day);
        }
        int scrollOffset = 0;
        String scrollOffsetStr = requestParams.get(clientId + Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "scrollPos");
        if (scrollOffsetStr != null) {
            try {
                scrollOffset = Integer.parseInt(scrollOffsetStr);
            } catch (NumberFormatException ignore) {
            }
        }
        timetableView.setScrollOffset(scrollOffset);
        decodeTimetableChanges(context, timetableView);
    }

    @Override
    protected JSONArray encodeRequestedEventsArray(
            FacesContext context,
            TimetableView timetableView,
            JSONObject jsonParam,
            Map<String, AbstractTimetableEvent> putLoadedEventsHere) throws JSONException, IOException {
        Map<String, TimeZone> timeZoneParam = getTimeZoneParamForJSONConverter(timetableView);
        ValueExpression eventsExpression = timetableView.getEventsValueExpression();
        List<AbstractTimetableEvent> events = eventsExpression == null
                ? Collections.emptyList()
                : DataUtil.readDataModelExpressionAsList(context, eventsExpression);

        if (putLoadedEventsHere != null)
            for (AbstractTimetableEvent event : events) {
                putLoadedEventsHere.put(event.getId(), event);
            }

        encodeEventAreas(context, timetableView, events);
        return DataUtil.listToJSONArray(events, timeZoneParam);
    }

    protected  String getDefaultExpandedDayViewClass(){
        return DEFAULT_EXPANDED_DAY_VIEW_CLASS;
    }

    protected String getComponentName() {
        return "MonthTable";
    }

    protected String getTagName() {
        return "<o:monthTable>";
    }

}
