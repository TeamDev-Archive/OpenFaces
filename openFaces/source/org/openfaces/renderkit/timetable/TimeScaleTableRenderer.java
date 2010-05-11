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

import org.openfaces.component.timetable.*;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.TableRenderer;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Components;
import org.openfaces.util.DataUtil;
import org.openfaces.util.Log;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.*;

/**
 * @author Dmitry Pikhulya
 * @author Roman Porotnikov
 */
public abstract class TimeScaleTableRenderer extends TimetableViewRenderer {

    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered())
            return;

        TimeScaleTable timetableView = (TimeScaleTable) component;
        Rendering.registerDateTimeFormatObject(timetableView.getLocale());
        AjaxUtil.prepareComponentForAjax(context, timetableView);

        timetableView.setEvent(null);
        ResponseWriter writer = context.getResponseWriter();
        String clientId = timetableView.getClientId(context);
        writer.startElement("table", timetableView);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("class", Styles.getCSSClass(context,
                timetableView, timetableView.getStyle(), "o_timetableView", timetableView.getStyleClass()), null);
        Rendering.writeStandardEvents(writer, timetableView);
        writer.startElement("tbody", timetableView);

        renderHeader(context, timetableView, clientId);
        renderSpecificHeaders(context, timetableView, clientId);
        List<TimetableResource> resources = renderResourceHeadersRow(context, timetableView, clientId);

        writer.startElement("tr", timetableView);
        writer.writeAttribute("class", "o_timetableView_tableRow", null);
        writer.startElement("td", timetableView);
        writer.writeAttribute("style", "height: 100%", null);

        renderContentTable(writer, timetableView, clientId, resources);
        encodeEventEditor(context, timetableView, resources);
        encodeActionBar(context, timetableView);

        writer.endElement("td");
        writer.endElement("tr");

        renderFooter(context, timetableView, clientId);

        writer.endElement("tbody");
        writer.endElement("table");

        Styles.renderStyleClasses(context, timetableView);
    }

    protected void renderSpecificHeaders(FacesContext context, final TimeScaleTable timetableView, String clientId) throws IOException {
    }

    private List<TimetableResource> renderResourceHeadersRow(FacesContext context, final TimeScaleTable timetableView, String clientId) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        ValueExpression resourcesExpression = timetableView.getResourcesValueExpression();
        final List<TimetableResource> resources = resourcesExpression != null
                ? DataUtil.readDataModelExpressionAsList(context, resourcesExpression)
                : Collections.EMPTY_LIST;
        if (resources.size() > 0) {
            int dayCount = getDayCount();
            final int colCount = 1 + dayCount * (resources.size() > 0 ? resources.size() : 1);
            writer.startElement("tr", timetableView);
            writer.startElement("td", timetableView);
            new TableRenderer(clientId + Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "resourceHeaders", 0, 0, 0, "o_resourceHeadersTable") {
                @Override
                protected void encodeCellContents(FacesContext context, ResponseWriter writer, UIComponent component,
                                                  int rowIndex, int colIndex) throws IOException {
                    if (colIndex == 0 || colIndex == colCount)
                        return;
                    TimetableResource resource = resources.get((colIndex - 1) % resources.size());
                    UIComponent resourceHeader = timetableView.getResourceHeader();
                    if (resourceHeader != null) {
                        Object prevValue = Components.setRequestMapValue("resource", resource);
                        resourceHeader.encodeAll(context);
                        Components.setRequestMapValue("resource", prevValue);
                    } else {
                        writer.write(resource.getName());
                    }
                }
            }.render(timetableView, true, 1, colCount + 1);
            writer.endElement("td");
            writer.endElement("tr");
        }
        return resources;
    }

    private void encodeInitScript(FacesContext context, TimeScaleTable timetableView, List<TimetableResource> resources) throws IOException {
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
        if (!thereIsChangeListener) {
            Log.log(context, "The " + getComponentName() + " with clientID=[" + clientId + "] is set to be editable, but is not configured to accept the changes. " +
                    "You should either make it read-only explicitly (using editable=\"false\" attribute), or define timetableChangeListener attribute to accept the changes (see " + getComponentName() + " reference).");
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

        List<UITimetableEvent> uiEvents = Components.findChildrenWithClass(timetableView, UITimetableEvent.class);
        if (uiEvents.size() > 1)
            throw new FacesException("There should be only one <o:timetableEvent> tag inside of " + getTagName() + " tag. " +
                    getComponentName() + " clientId = " + timetableView.getClientId(context));
        UITimetableEvent uiEvent = uiEvents.size() > 0 ? uiEvents.get(0) : null;

        try {
            Rendering.renderInitScript(context,
                    new ScriptBuilder().initScript(context, timetableView, getJsInitFunctionName(),
                            DataUtil.formatDateTimeForJs(timetableView.getDay(), timeZone),
                            timetableView.getLocale(),
                            "MMMM, dd yyyy",
                            timetableView.getStartTime(),
                            timetableView.getEndTime(),
                            timetableView.getScrollTime(),
                            eventParams,
                            resourcesJsArray,
                            areaSettings,
                            editable,
                            timetableView.getOnchange(),
                            editingOptions,
                            stylingParams,
                            uiEvent != null ? uiEvent.toJSONObject(null) : null,
                            timetableView.getTimePattern(),
                            timetableView.getTimeSuffixPattern(),
                            timetableView.getMajorTimeInterval(),
                            timetableView.getMinorTimeInterval(),
                            timetableView.getShowTimeForMinorIntervals(),
                            calendarOptions)/*.semicolon()*/,
                    Resources.getUtilJsURL(context),
                    Resources.getJsonJsURL(context),
                    TableUtil.getTableUtilJsURL(context),
                    Resources.getInternalURL(context, TimeScaleTableRenderer.class, "rangeMap.js"),
                    Resources.getInternalURL(context, TimeScaleTableRenderer.class, "timeTableView.js"),
                    Resources.getInternalURL(context, TimeScaleTableRenderer.class, "timeScaleTable.js"),
                    Resources.getInternalURL(context, TimeScaleTableRenderer.class, "timetable.js"),
                    Resources.getInternalURL(context, TimeScaleTableRenderer.class, getJsLibraryName())
            );
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    private Date getDayTimeAtTimeZone(Date day, String timeStr, TimeZone timeZone) {
        Calendar c = new GregorianCalendar(timeZone);
        c.setTime(day);
        String[] timeFields = timeStr.split(":");
        int hours = Integer.parseInt(timeFields[0]);
        int minutes = Integer.parseInt(timeFields[1]);
        c.set(Calendar.HOUR_OF_DAY, hours);
        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    private JSONObject composeEventParams(FacesContext context, TimeScaleTable timetableView, List putEventsHere) {
        PreloadedEvents preloadedEvents = timetableView.getPreloadedEvents();
        Map<String, TimeZone> timeZoneParam = getTimeZoneParamForJSONConverter(timetableView);
        TimeZone timeZone = timeZoneParam.get("timeZone");

        Date startTime = null;
        Date endTime = null;
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        Object prevStartTimeValue = null;
        Object prevEndTimeValue = null;
        if (preloadedEvents == PreloadedEvents.NONE) {
            String dayStartTime = timetableView.getStartTime();
            String dayEndTime = timetableView.getEndTime();
            startTime = getDayTimeAtTimeZone(timetableView.getDay(), dayStartTime != null ? dayStartTime : "00:00", timeZone);
            endTime = getDayTimeAtTimeZone(timetableView.getDay(), dayEndTime != null ? dayEndTime : "24:00", timeZone);
            prevStartTimeValue = requestMap.put(START_TIME, startTime);
            prevEndTimeValue = requestMap.put(END_TIME, endTime);
        }

        ValueExpression eventsExpression = timetableView.getEventsValueExpression();
        List events = eventsExpression == null ? Collections.EMPTY_LIST : DataUtil.readDataModelExpressionAsList(context, eventsExpression);
        if (putEventsHere != null) {
            putEventsHere.clear();
            putEventsHere.addAll(events);
        }

        if (preloadedEvents == PreloadedEvents.NONE) {
            requestMap.put(START_TIME, prevStartTimeValue);
            requestMap.put(END_TIME, prevEndTimeValue);
        }

        JSONArray eventsJsArray = DataUtil.listToJSONArray(events, timeZoneParam);

        JSONObject eventParams = new JSONObject();
        try {
            eventParams.put("events", eventsJsArray);
            eventParams.put("from", DataUtil.formatDateTimeForJs(startTime, timeZone));
            eventParams.put("to", DataUtil.formatDateTimeForJs(endTime, timeZone));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return eventParams;
    }

    private JSONObject getStylingParamsObj(FacesContext context, TimeScaleTable timetableView) {
        JSONObject stylingParams = new JSONObject();


        Styles.addStyleJsonParam(context, timetableView, stylingParams, "rolloverClass",
                timetableView.getRolloverStyle(), timetableView.getRolloverClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "resourceHeadersRowClass",
                timetableView.getResourceHeadersRowStyle(), timetableView.getResourceHeadersRowClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "rowClass",
                timetableView.getRowStyle(), timetableView.getRowClass());

        Rendering.addJsonParam(stylingParams, "timeTextPosition",
                timetableView.getTimeTextPosition(), TimeTextPosition.UNDER_MARK);

        Styles.addStyleJsonParam(context, timetableView, stylingParams, "timeColumnClass",
                timetableView.getTimeColumnStyle(), timetableView.getTimeColumnClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "majorTimeClass",
                timetableView.getMajorTimeStyle(), timetableView.getMajorTimeClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "minorTimeClass",
                timetableView.getMinorTimeStyle(), timetableView.getMinorTimeClass());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "timeSuffixClass",
                timetableView.getTimeSuffixStyle(), timetableView.getTimeSuffixClass(), StyleGroup.regularStyleGroup(1));

        Rendering.addJsonParam(stylingParams, "defaultEventColor", timetableView.getDefaultEventColor());
        Rendering.addJsonParam(stylingParams, "reservedTimeEventColor", timetableView.getReservedTimeEventColor());
        Styles.addStyleJsonParam(context, timetableView, stylingParams, "reservedTimeEventClass",
                timetableView.getReservedTimeEventStyle(), timetableView.getReservedTimeEventClass());
        Rendering.addJsonParam(stylingParams, "dragAndDropTransitionPeriod", timetableView.getDragAndDropTransitionPeriod(), 70);
        Rendering.addJsonParam(stylingParams, "dragAndDropCancelingPeriod", timetableView.getDragAndDropCancelingPeriod(), 200);
        Rendering.addJsonParam(stylingParams, "undroppableStateTransitionPeriod", timetableView.getUndroppableStateTransitionPeriod(), 250);
        Rendering.addJsonParam(stylingParams, "undroppableEventTransparency", timetableView.getUndroppableEventTransparency(), 0.5);

        Rendering.addJsonParam(stylingParams, "resourceHeadersRowSeparator", timetableView.getResourceHeadersRowSeparator());
        Rendering.addJsonParam(stylingParams, "resourceColumnSeparator", timetableView.getResourceColumnSeparator());
        Rendering.addJsonParam(stylingParams, "timeColumnSeparator", timetableView.getTimeColumnSeparator());
        Rendering.addJsonParam(stylingParams, "primaryRowSeparator", timetableView.getPrimaryRowSeparator());
        Rendering.addJsonParam(stylingParams, "secondaryRowSeparator", timetableView.getSecondaryRowSeparator());
        Rendering.addJsonParam(stylingParams, "timeColumnPrimaryRowSeparator", timetableView.getTimeColumnPrimaryRowSeparator());
        Rendering.addJsonParam(stylingParams, "timeColumnSecondaryRowSeparator", timetableView.getTimeColumnSecondaryRowSeparator());

        addSpecificStylingParams(context, timetableView, stylingParams);

        return stylingParams;
    }

    protected void addSpecificStylingParams(FacesContext context, TimeScaleTable timetableView, JSONObject stylingParams) {
    }

    protected JSONObject getCalendarOptionsObj(TimeScaleTable timetableView) {
        return null;
    }

    private void renderContentTable(
            ResponseWriter writer,
            final TimeScaleTable timetableView,
            final String clientId,
            final List<TimetableResource> resources) throws IOException {
        writer.startElement("div", timetableView);
        writer.writeAttribute("id", clientId + "::scroller", null);
        writer.writeAttribute("class", "o_timetableView_scroller", null);

        int colCount = 1 + (resources.size() > 0 ? resources.size() : 1) * getDayCount();

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


    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        TimeScaleTable timetableView = (TimeScaleTable) component;

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
        String scrollTimeStr = requestParams.get(clientId + Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "scrollPos");
        if (scrollTimeStr != null) {
            timetableView.setScrollTime(scrollTimeStr);
        }
        decodeTimetableChanges(context, timetableView);

    }

    @Override
    protected JSONArray encodeRequestedEventsArray(
            FacesContext context,
            TimetableView timetableView,
            JSONObject jsonParam,
            Map<String, AbstractTimetableEvent> putLoadedEventsHere) throws JSONException, IOException {
        Map<String, TimeZone> timeZoneParam = getTimeZoneParamForJSONConverter(timetableView);
        TimeZone timeZone = timeZoneParam.get("timeZone");
        String startTimeStr = jsonParam.getString(START_TIME);
        String endTimeStr = jsonParam.getString(END_TIME);
        Date startTime = DataUtil.parseDateTimeFromJs(startTimeStr, timeZone);
        Date endTime = DataUtil.parseDateTimeFromJs(endTimeStr, timeZone);

        PreloadedEvents preloadedEvents = timetableView.getPreloadedEvents();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

        Object prevStartTimeValue = null;
        Object prevEndTimeValue = null;
        if (PreloadedEvents.NONE.equals(preloadedEvents)) {
            prevStartTimeValue = requestMap.put(START_TIME, startTime);
            prevEndTimeValue = requestMap.put(END_TIME, endTime);
        }
        ValueExpression eventsExpression = timetableView.getEventsValueExpression();
        List<AbstractTimetableEvent> events = eventsExpression == null
                ? Collections.emptyList()
                : DataUtil.readDataModelExpressionAsList(context, eventsExpression);
        if (PreloadedEvents.NONE.equals(preloadedEvents)) {
            requestMap.put(START_TIME, prevStartTimeValue);
            requestMap.put(END_TIME, prevEndTimeValue);
        }

        if (putLoadedEventsHere != null)
            for (AbstractTimetableEvent event : events) {
                putLoadedEventsHere.put(event.getId(), event);
            }

        encodeEventAreas(context, timetableView, events);
        return DataUtil.listToJSONArray(events, timeZoneParam);
    }

    protected abstract String getJsLibraryName(); 
    protected abstract String getJsInitFunctionName();
    protected abstract int getDayCount();

}
