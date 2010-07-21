/*
 * OpenFaces - JSF Component Library 3.0
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
import org.openfaces.renderkit.AjaxPortionRenderer;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.util.Components;
import org.openfaces.util.DataUtil;
import org.openfaces.util.Rendering;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Dmitry Pikhulya
 * @author Roman Porotnikov
 */
public abstract class TimetableViewRenderer extends RendererBase implements AjaxPortionRenderer {

    public static final String USE_RESOURCE_SEPARATION_MODE_ATTR = "_of_useResourceSeparationMode";
    public static final String EVENTEDITOR_RESOURCES_ATTR = "_resources";

    protected void encodeActionBar(FacesContext context, TimetableView timetableView) throws IOException {
        EventActionBar bar = timetableView.getEventActionBar();
        if (bar != null) {
            bar.encodeAll(context);
        }
    }

    protected void encodeEventPreview(FacesContext context, TimetableView timetableView) throws IOException {
        EventPreview preview = timetableView.getEventPreview();
        if (preview != null) {
            preview.encodeAll(context);
        }
    }

    protected void encodeEventEditor(FacesContext context, TimetableView timetableView, List<TimetableResource> resources) throws IOException {
        UIComponent eventEditor = timetableView.getEventEditor();
        if (eventEditor == null) {
            eventEditor = getOrCreateEventEditor(context, timetableView);
        }
        if (eventEditor instanceof EventEditorDialog)
            ((EventEditorDialog) eventEditor).setVisible(false);
        eventEditor.getAttributes().put(EVENTEDITOR_RESOURCES_ATTR, resources);
        eventEditor.encodeAll(context);
        eventEditor.getAttributes().remove(EVENTEDITOR_RESOURCES_ATTR);
    }

    private EventEditorDialog getOrCreateEventEditor(FacesContext context, TimetableView timetableView) {
        return Components.getOrCreateFacet(context, timetableView,
                EventEditorDialog.COMPONENT_TYPE, "eventEditor", "_eventEditor", EventEditorDialog.class);
    }

    protected void renderHeader(FacesContext context, TimetableView timetableView, String clientId) throws IOException {
        UIComponent header = timetableView.getFacet("header");
        if (header != null) {
            ResponseWriter writer = context.getResponseWriter();
            writer.startElement("tr", header);
            writer.startElement("td", header);
            writer.startElement("table", header);
            String headerClass = Styles.getCSSClass(context,
                    timetableView, timetableView.getHeaderStyle(), "o_timetableView_header", timetableView.getHeaderClass());
            writer.writeAttribute("class", headerClass, null);

            writer.startElement("tr", header);
            writer.startElement("td", header);
            header.encodeAll(context);
            writer.endElement("td");
            writer.endElement("tr");

            writer.endElement("table");
            writer.endElement("td");
            writer.endElement("tr");
        }
    }

    protected void renderFooter(FacesContext context, TimetableView timetableView, String clientId) throws IOException {
        UIComponent footer = timetableView.getFacet("footer");
        if (footer != null) {
            ResponseWriter writer = context.getResponseWriter();
            writer.startElement("tr", footer);
            writer.startElement("td", footer);
            writer.startElement("table", footer);
            String footerClass = Styles.getCSSClass(context,
                    timetableView, timetableView.getFooterStyle(), "o_timetableView_footer", timetableView.getFooterClass());
            writer.writeAttribute("class", footerClass, null);

            writer.startElement("tr", footer);
            writer.startElement("td", footer);
            footer.encodeAll(context);
            writer.endElement("td");
            writer.endElement("tr");

            writer.endElement("table");
            writer.endElement("td");
            writer.endElement("tr");
        }
    }

    protected void decodeTimetableChanges(FacesContext context, TimetableView timetableView) {
        Map<String, String> requestParams = context.getExternalContext().getRequestParameterMap();

        String changesKey = timetableView.getClientId(context) + Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "timetableChanges";
        String timetableChangesStr = requestParams.get(changesKey);
        if (timetableChangesStr == null)
            return;

        TimetableEvent[] addedEvents;
        TimetableEvent[] editedEvents;
        String[] removedEventIds;
        try {
            JSONObject timetableChanges = new JSONObject(timetableChangesStr);
            Object addedEventsArray = timetableChanges.get("addedEvents");
            Object editedEventsObj = timetableChanges.get("editedEvents");
            Object removedEventIdsObj = timetableChanges.get("removedEventIds");

            TimeZone timeZone = (timetableView.getTimeZone() != null)
                    ? timetableView.getTimeZone()
                    : TimeZone.getDefault();

            addedEvents = eventsFromJSONArray(timeZone, addedEventsArray);
            editedEvents = eventsFromJSONArray(timeZone, editedEventsObj);
            JSONArray removedEventIdsArray = JSONObject.NULL.equals(removedEventIdsObj) ? null : (JSONArray) removedEventIdsObj;
            int removedEventCount = removedEventIdsArray != null ? removedEventIdsArray.length() : 0;
            removedEventIds = new String[removedEventCount];
            for (int i = 0; i < removedEventCount; i++) {
                removedEventIds[i] = removedEventIdsArray.getString(i);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        TimetableChangeEvent event = new TimetableChangeEvent(timetableView, addedEvents, editedEvents, removedEventIds);
        event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
        if (addedEvents.length > 0 || editedEvents.length > 0 || removedEventIds.length > 0)
            timetableView.queueEvent(event);
        context.getExternalContext().getRequestMap().put(getTimetableChangeEventKey(context, timetableView), event);
    }

    protected String getTimetableChangeEventKey(FacesContext context, TimetableView timetableView) {
        return "OF__TimetableChangeEvent for " + timetableView.getClientId(context);
    }

    protected TimetableEvent[] eventsFromJSONArray(TimeZone timeZone, Object arrayObj) throws JSONException {
        JSONArray array = JSONObject.NULL.equals(arrayObj) ? null : (JSONArray) arrayObj;
        int arrayLength = array != null ? array.length() : 0;
        TimetableEvent[] targetArray = new TimetableEvent[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            JSONObject eventObj = (JSONObject) array.get(i);

            TimetableEvent event = new TimetableEvent();
            event.setId((String) eventObj.opt("id"));
            event.setName(eventObj.getString("name"));
            event.setDescription(eventObj.getString("description"));
            if (!eventObj.isNull("resourceId"))
                event.setResourceId(eventObj.getString("resourceId"));
            String startStr = eventObj.getString("startStr");
            event.setStart(DataUtil.parseDateTimeFromJs(startStr, timeZone));
            String endStr = eventObj.getString("endStr");
            event.setEnd(DataUtil.parseDateTimeFromJs(endStr, timeZone));
            Object colorObj = eventObj.opt("color");
            String colorStr = JSONObject.NULL != colorObj ? (String) colorObj : null;
            event.setColor(CSSUtil.parseColor(colorStr));
            JSONObject customPropertiesObj = eventObj.optJSONObject("customProperties");
            if (customPropertiesObj != null) {
                for (Iterator<String> keys = customPropertiesObj.keys(); keys.hasNext();) {
                    String key = keys.next();
                    event.setCustomProperty(key, customPropertiesObj.getString(key));
                }
            }

            targetArray[i] = event;
        }
        return targetArray;
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }

    protected void encodeLoadEventsPortion(
            FacesContext context,
            TimetableView timetableView,
            JSONObject responseData,
            JSONObject jsonParam) throws JSONException, IOException {
        Map<String, AbstractTimetableEvent> loadedEvents = timetableView.getLoadedEvents();
        JSONArray eventsJsArray = encodeRequestedEventsArray(context, timetableView, jsonParam, loadedEvents);
        responseData.put("events", eventsJsArray);
    }

    protected abstract JSONArray encodeRequestedEventsArray(
            FacesContext context,
            TimetableView timetableView,
            JSONObject jsonParam,
            Map<String, AbstractTimetableEvent> putLoadedEventsHere) throws JSONException, IOException;


    protected JSONArray encodeEventAreas(FacesContext context, TimetableView timetableView, List<AbstractTimetableEvent> events) throws IOException {
        List<EventArea> eventAreas = timetableView.getEventAreas();
        if (eventAreas.size() > 0) {
            String eventVarName = timetableView.getEventVar();
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            Object prevEventVarValue = requestMap.get(eventVarName);
            for (AbstractTimetableEvent event : events) {
                timetableView.setEvent(event);
                for (EventArea eventArea : eventAreas) {
                    eventArea.encodeAll(context);
                }

            }
            timetableView.setEvent(null);
            requestMap.put(eventVarName, prevEventVarValue);
        }

        JSONArray areaSettings = new JSONArray();
        try {
            for (int i = 0; i < eventAreas.size(); i++) {
                EventArea eventArea = eventAreas.get(i);
                JSONObject thisAreaSettings = new JSONObject();
                thisAreaSettings.put("id", eventArea.getId());
                thisAreaSettings.put("horizontalAlignment", eventArea.getHorizontalAlignment());
                thisAreaSettings.put("verticalAlignment", eventArea.getVerticalAlignment());
                areaSettings.put(i, thisAreaSettings);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return areaSettings;
    }

    protected Map<String, TimeZone> getTimeZoneParamForJSONConverter(TimetableView timetableView) {
        TimeZone timeZone = (timetableView.getTimeZone() != null)
                ? timetableView.getTimeZone()
                : TimeZone.getDefault();
        Map<String, TimeZone> timeZoneParam = new HashMap<String, TimeZone>();
        timeZoneParam.put("timeZone", timeZone);
        return timeZoneParam;
    }

    public JSONObject encodeAjaxPortion(FacesContext context,
                                        UIComponent component,
                                        String portionName,
                                        JSONObject jsonParam) throws IOException, JSONException {
        TimetableView timetableView = (TimetableView) component;
        JSONObject responseData = new JSONObject();
        if (portionName.equals("saveEventChanges")) {
            encodeSaveEventChangesPortion(context, timetableView, responseData, jsonParam);
        } else if (portionName.equals("loadEvents")) {
            encodeLoadEventsPortion(context, timetableView, responseData, jsonParam);
        } else {
            throw new IllegalArgumentException("Unknown portion name: " + portionName);
        }
        return responseData;
    }

    private void encodeSaveEventChangesPortion(
            FacesContext context,
            TimetableView timetableView,
            JSONObject responseData,
            JSONObject jsonParam) throws JSONException, IOException {
        boolean reloadAllEvents = jsonParam.getBoolean("reloadAllEvents");
        Map<String, TimeZone> timeZoneParam = getTimeZoneParamForJSONConverter(timetableView);

        TimetableChangeEvent changeEvent = (TimetableChangeEvent) context.getExternalContext().getRequestMap().get(
                getTimetableChangeEventKey(context, timetableView));
        reloadAllEvents |= changeEvent.getReloadAllEvents();

        Map<String, AbstractTimetableEvent> loadedEvents = timetableView.getLoadedEvents();
        if (!reloadAllEvents) {
            TimetableEvent[] addedEvents = changeEvent.getAddedEvents();
            TimetableEvent[] changedEvents = changeEvent.getChangedEvents();
            String[] removedEventIds = changeEvent.getRemovedEventIds();
            responseData.put("addedEvents", DataUtil.arrayToJSONArray(addedEvents, timeZoneParam));
            responseData.put("editedEvents", DataUtil.arrayToJSONArray(changedEvents, timeZoneParam));
            List<AbstractTimetableEvent> events = new ArrayList<AbstractTimetableEvent>();
            events.addAll(Arrays.asList(addedEvents));
            events.addAll(Arrays.asList(changedEvents));
            encodeEventAreas(context, timetableView, events);
            for (TimetableEvent event : addedEvents) {
                String eventId = event.getId();
                if (eventId == null)
                    throw new IllegalStateException("You must assign id field for all added events inside of TimetableChangeEvent. See \"timetableChangeListener\" attribute description in " + getComponentName() + " reference.");
                loadedEvents.put(eventId, event);
            }
            for (TimetableEvent event : changedEvents) {
                loadedEvents.put(event.getId(), event);
            }
            for (String eventId : removedEventIds) {
                loadedEvents.remove(eventId);
            }

        } else {
            loadedEvents.clear();
            JSONArray requestedEvents = encodeRequestedEventsArray(context, timetableView, jsonParam, loadedEvents);
            responseData.put("reloadedEvents", requestedEvents);
        }

    }

    protected JSONObject getEditingOptionsObj(TimetableView timetableView) {
        JSONObject editingOptionsObj = new JSONObject();
        TimetableEditingOptions editingOptions = timetableView.getEditingOptions();
        Rendering.addJsonParam(editingOptionsObj, "autoSaveChanges", editingOptions.getAutoSaveChanges(), true);
        Rendering.addJsonParam(editingOptionsObj, "overlappedEventsAllowed", editingOptions.getOverlappedEventsAllowed(), true);
        Rendering.addJsonParam(editingOptionsObj, "eventResourceEditable", editingOptions.isEventResourceEditable(), true);
        Rendering.addJsonParam(editingOptionsObj, "eventDurationEditable", editingOptions.isEventDurationEditable(), true);
        Rendering.addJsonParam(editingOptionsObj, "defaultEventDuration", editingOptions.getDefaultEventDuration(), 30);
        return editingOptionsObj;
    }

    protected abstract String getComponentName();

    protected abstract String getTagName();

}
