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

package org.openfaces.component.timetable;

import org.openfaces.component.OUIData;
import org.openfaces.component.OUIObjectIteratorBase;
import org.openfaces.component.window.Confirmation;
import org.openfaces.util.CalendarUtil;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.ValueBindings;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Dmitry Pikhulya
 * @author Roman Porotnikov
 */
@ResourceDependencies({
        @ResourceDependency(name = "jsf.js", library = "javax.faces"),
        @ResourceDependency(name = "default.css", library = "openfaces")
})
public abstract class TimetableView extends OUIObjectIteratorBase {
    private static final String EVENT_EDITOR_FACET_NAME = "eventEditor";
    private static final String DELETE_EVENT_CONFIRMATION_FACET_NAME = "deleteEventConfirmation";

    private MethodExpression timetableChangeListener;
    private String onchange;
    protected Date day;

    private Locale locale;
    private TimeZone timeZone;
    private PreloadedEvents preloadedEvents;

    private Color defaultEventColor;
    private Color reservedTimeEventColor;
    private String reservedTimeEventStyle;
    private String reservedTimeEventClass;
    private String rolloverEventNoteStyle;
    private String rolloverEventNoteClass;

    private String headerStyle;
    private String headerClass;
    private String footerStyle;
    private String footerClass;

    private String rowStyle;
    private String rowClass;

    private Boolean editable;
    private String eventVar = "event";
    private AbstractTimetableEvent event;
    private Object initialDescendantsState;
    private Map<String, Object> descendantsStateForEvents = new HashMap<String, Object>();
    private Map<String, AbstractTimetableEvent> loadedEvents = new HashMap<String, AbstractTimetableEvent>();

    protected boolean childrenValid = true;

    public Timetable getTimetable() {
        UIComponent parent = getParent();
        while (parent != null && !(parent instanceof Timetable))
            parent = parent.getParent();
        if (parent == null) return null;
        if (this instanceof Timetable)
            throw new IllegalStateException("Timetable cannot be nested within another timetable");
        return (Timetable) parent;
    }

    @Override
    protected UIComponent getDelegate() {
        return getTimetable();
    }

    public Date getDay() {
        return ValueBindings.get(this, getTimetable(), "day", day, new Date(), Date.class);
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public MethodExpression getTimetableChangeListener() {
        Timetable timetable = getTimetable();
        if (timetable != null)
            return timetable.getTimetableChangeListener();
        return timetableChangeListener;
    }

    public void setTimetableChangeListener(MethodExpression timetableChangeListener) {
        this.timetableChangeListener = timetableChangeListener;
    }

    public String getOnchange() {
        return ValueBindings.get(this, getTimetable(), "onchange", onchange);
    }

    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }

    public void addTimetableChangeListener(TimetableChangeListener listener) {
        if (getTimetable() != null)
            throw new FacesException("TimetableChangeListener can't be specified for individual views when a view " +
                    "is in Timetable -- it must be specified for the Timetable component itself.");
        addFacesListener(listener);
    }

    public void removeTimetableChangeListener(TimetableChangeListener listener) {
        if (getTimetable() != null)
            throw new FacesException("TimetableChangeListener can't be specified for individual views when a view " +
                    "is in Timetable -- it must be specified for the Timetable component itself.");
        removeFacesListener(listener);
    }

    public TimetableChangeListener[] getTimetableChangeListeners() {
        if (getTimetable() != null)
            return getTimetable().getTimetableChangeListeners();
        else
            return (TimetableChangeListener[]) getFacesListeners(TimetableChangeListener.class);
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);
        MethodExpression timetableChangeListener = getTimetableChangeListener();
        if (timetableChangeListener == null || !(event instanceof TimetableChangeEvent))
            return;

        try {
            ELContext elContext = getFacesContext().getELContext();
            timetableChangeListener.invoke(elContext, new Object[]{event});
        } catch (FacesException e) {
            if (e.getCause() != null && e.getCause() instanceof AbortProcessingException)
                throw (AbortProcessingException) e.getCause();
            else
                throw e;
        }
    }

    public Locale getLocale() {
        return CalendarUtil.getBoundPropertyValueAsLocale(this, getTimetable(), "locale", locale);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public TimeZone getTimeZone() {
        return ValueBindings.get(this, getTimetable(), "timeZone", timeZone,
                TimeZone.getDefault(), TimeZone.class);
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public PreloadedEvents getPreloadedEvents() {
        return ValueBindings.get(this, getTimetable(), "preloadedEvents", preloadedEvents, PreloadedEvents.ALL, PreloadedEvents.class);
    }

    public void setPreloadedEvents(PreloadedEvents preloadedEvents) {
        this.preloadedEvents = preloadedEvents;
    }

    public Color getDefaultEventColor() {
        return ValueBindings.get(this, getTimetable(), "defaultEventColor", defaultEventColor, Color.class);
    }

    public void setDefaultEventColor(Color defaultEventColor) {
        this.defaultEventColor = defaultEventColor;
    }

    public Color getReservedTimeEventColor() {
        return ValueBindings.get(this, getTimetable(), "reservedTimeEventColor", reservedTimeEventColor, Color.class);
    }

    public void setReservedTimeEventColor(Color reservedTimeEventColor) {
        this.reservedTimeEventColor = reservedTimeEventColor;
    }

    public String getReservedTimeEventStyle() {
        return ValueBindings.get(this, getTimetable(), "reservedTimeEventStyle", reservedTimeEventStyle);
    }

    public void setReservedTimeEventStyle(String reservedTimeEventStyle) {
        this.reservedTimeEventStyle = reservedTimeEventStyle;
    }

    public String getReservedTimeEventClass() {
        return ValueBindings.get(this, getTimetable(), "reservedTimeEventClass", reservedTimeEventClass);
    }

    public void setReservedTimeEventClass(String reservedTimeEventClass) {
        this.reservedTimeEventClass = reservedTimeEventClass;
    }

    public String getRolloverEventNoteStyle() {
        return ValueBindings.get(this, getTimetable(), "rolloverEventNoteStyle", rolloverEventNoteStyle);
    }

    public void setRolloverEventNoteStyle(String rolloverEventNoteStyle) {
        this.rolloverEventNoteStyle = rolloverEventNoteStyle;
    }

    public String getRolloverEventNoteClass() {
        return ValueBindings.get(this, getTimetable(), "rolloverEventNoteClass", rolloverEventNoteClass);
    }

    public void setRolloverEventNoteClass(String rolloverEventNoteClass) {
        this.rolloverEventNoteClass = rolloverEventNoteClass;
    }

    public String getHeaderStyle() {
        return ValueBindings.get(this, getTimetable(), "headerStyle", headerStyle);
    }

    public void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle;
    }

    public String getHeaderClass() {
        return ValueBindings.get(this, getTimetable(), "headerClass", headerClass);
    }

    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }

    public String getFooterStyle() {
        return ValueBindings.get(this, getTimetable(), "footerStyle", footerStyle);
    }

    public void setFooterStyle(String footerStyle) {
        this.footerStyle = footerStyle;
    }

    public String getFooterClass() {
        return ValueBindings.get(this, getTimetable(), "footerClass", footerClass);
    }

    public void setFooterClass(String footerClass) {
        this.footerClass = footerClass;
    }

    public String getRowStyle() {
        return ValueBindings.get(this, getTimetable(), "rowStyle", rowStyle);
    }

    public void setRowStyle(String rowStyle) {
        this.rowStyle = rowStyle;
    }

    public String getRowClass() {
        return ValueBindings.get(this, getTimetable(), "rowClass", rowClass);
    }

    public void setRowClass(String rowClass) {
        this.rowClass = rowClass;
    }

    public boolean isEditable() {
        return ValueBindings.get(this, getTimetable(), "editable", editable, true);
    }

    public void setEditable(boolean value) {
        editable = value;
    }

    public AbstractTimetableEvent getEvent() {
        return event;
    }

    public void setEvent(AbstractTimetableEvent event) {
        if (this.event == event)
            return;

        Timetable timetable = getTimetable();
        if (timetable != null)
            timetable.setEvent(event);

        List<EventArea> eventAreas = getEventAreas();

        if (this.event == null)
            initialDescendantsState = OUIData.saveDescendantComponentStates(new ArrayList<UIComponent>(eventAreas).iterator(), true);
        else {
            String eventId = this.event.getId();
            Object stateForEvent = OUIData.saveDescendantComponentStates(new ArrayList<UIComponent>(eventAreas).iterator(), true);
            descendantsStateForEvents.put(eventId, stateForEvent);
        }

        FacesContext context = getFacesContext();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        requestMap.put(eventVar, event);
        this.event = event;

        if (this.event == null)
            OUIData.restoreDescendantComponentStates(new ArrayList<UIComponent>(eventAreas).iterator(), initialDescendantsState, true);
        else {
            Object stateForEvent = descendantsStateForEvents.get(this.event.getId());
            if (stateForEvent != null)
                OUIData.restoreDescendantComponentStates(new ArrayList<UIComponent>(eventAreas).iterator(), stateForEvent, true);
        }

        for (EventArea eventArea : eventAreas) {
            Components.clearCachedClientIds(eventArea);
        }

    }

    public void setObjectId(String objectId) {
        super.setObjectId(objectId);
        if (objectId == null) {
            setEvent(null);
            return;
        }
        AbstractTimetableEvent event = getLoadedEventByObjectId(objectId);
        setEvent(event);
    }

    protected AbstractTimetableEvent getLoadedEventByObjectId(String objectId) {
        return getLoadedEvents().get(objectId);
    }

    public String getObjectId() {
        if (event == null)
            return null;
        return event.getId();
    }

    public List<EventArea> getEventAreas() {
        List<EventArea> eventAreas = Components.findChildrenWithClass(this, EventArea.class);
        if (eventAreas.size() == 0) {
            Timetable timetable = getTimetable();
            if (timetable != null)
                eventAreas = timetable.getEventAreas();
        }
        return eventAreas;
    }

    public String getEventVar() {
        Timetable timetable = getTimetable();
        return timetable != null ? timetable.getEventVar() : eventVar;
    }

    public void setEventVar(String eventVar) {
        this.eventVar = eventVar;
    }

    public Map<String, AbstractTimetableEvent> getLoadedEvents() {
        return loadedEvents;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        if (childrenValid && !hasErrorMessages(context))
            descendantsStateForEvents.clear();
        super.encodeBegin(context);
    }

    private boolean hasErrorMessages(FacesContext context) {
        for (Iterator<FacesMessage> iter = context.getMessages(); iter.hasNext();) {
            FacesMessage message = iter.next();
            if (FacesMessage.SEVERITY_ERROR.compareTo(message.getSeverity()) <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                saveAttachedState(context, timetableChangeListener),
                onchange,
                day,
                locale,
                timeZone,
                preloadedEvents,
                defaultEventColor,
                reservedTimeEventColor,
                reservedTimeEventStyle,
                reservedTimeEventClass,
                rolloverEventNoteStyle,
                rolloverEventNoteClass,
                headerStyle,
                headerClass,
                footerStyle,
                footerClass,
                rowStyle,
                rowClass,
                editable,
                eventVar,
                loadedEvents
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        timetableChangeListener = (MethodExpression) restoreAttachedState(context, state[i++]);
        onchange = (String) state[i++];
        day = (Date) state[i++];
        locale = (Locale) state[i++];
        timeZone = (TimeZone) state[i++];
        preloadedEvents = (PreloadedEvents) state[i++];
        defaultEventColor = (Color) state[i++];
        reservedTimeEventColor = (Color) state[i++];
        reservedTimeEventStyle = (String) state[i++];
        reservedTimeEventClass = (String) state[i++];
        rolloverEventNoteStyle = (String) state[i++];
        rolloverEventNoteClass = (String) state[i++];
        headerStyle = (String) state[i++];
        headerClass = (String) state[i++];
        footerStyle = (String) state[i++];
        footerClass = (String) state[i++];
        rowStyle = (String) state[i++];
        rowClass = (String) state[i++];
        editable = (Boolean) state[i++];
        eventVar = (String) state[i++];
        loadedEvents = (Map<String, AbstractTimetableEvent>) state[i++];
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public Confirmation getDeletionConfirmation() {
        FacesContext context = FacesContext.getCurrentInstance();
        Confirmation confirmation = getDeleteEventConfirmation();
        if (confirmation == null) {
            Timetable timetable = getTimetable();
            if (timetable != null)
                confirmation = timetable.getDeleteEventConfirmation();
        }
        if (confirmation == null) {
            confirmation = Components.createComponent(context,
                    Confirmation.COMPONENT_TYPE, Confirmation.class, this, "deleteEventConfirmation");
            confirmation.setMessage("Delete the event?");
            confirmation.setDetails("Click OK to delete the event");
            confirmation.setDraggable(true);
            setDeleteEventConfirmation(confirmation);
        }
        return confirmation;
    }

    public Confirmation getDeleteEventConfirmation() {
        return (Confirmation) getFacet(DELETE_EVENT_CONFIRMATION_FACET_NAME);
    }

    public void setDeleteEventConfirmation(Confirmation confirmation) {
        getFacets().put(DELETE_EVENT_CONFIRMATION_FACET_NAME, confirmation);
    }

    public TimetableEditingOptions getEditingOptions() {
        Timetable timetable = getTimetable();
        return timetable != null
                ? timetable.getEditingOptions()
                : Components.getChildWithClass(this, TimetableEditingOptions.class, "editingOptions");
    }

    public UIComponent getEventEditor() {
        UIComponent result = Components.getFacet(this, EVENT_EDITOR_FACET_NAME);
        if (result == null) {
            Timetable timetable = getTimetable();
            if (timetable != null)
                result = timetable.getEventEditor();
        }
        return result;
    }

    public void setEventEditor(UIComponent dialog) {
        getFacets().put(EVENT_EDITOR_FACET_NAME, dialog);
    }

    public ValueExpression getEventsValueExpression() {
        Timetable timetable = getTimetable();
        return timetable != null ? timetable.getEventsValueExpression() : getValueExpression("events");
    }

    public void setEventsValueExpression(ValueExpression expression) {
        setValueExpression("events", expression);
    }

    public ValueExpression getResourcesValueExpression() {
        Timetable timetable = getTimetable();
        return timetable != null ? timetable.getResourcesValueExpression() : getValueExpression("resources");
    }

    public void setResourcesValueExpression(ValueExpression expression) {
        setValueExpression("resources", expression);
    }

    public EventActionBar getEventActionBar() {
        EventActionBar result = Components.findChildWithClass(this, EventActionBar.class, "eventActionBar");
        if (result == null) {
            Timetable timetable = getTimetable();
            if (timetable != null)
                result = timetable.getEventActionBar();
        }
        if (result == null) {
            result = Components.getChildWithClass(this, EventActionBar.class, "eventActionBar");
        }
        return result;
    }

    public EventPreview getEventPreview() {
        EventPreview result = Components.findChildWithClass(this, EventPreview.class);
        if (result == null) {
            Timetable timetable = getTimetable();
            if (timetable != null)
                result = timetable.getEventPreview();
        }
        return result;
    }

    public UITimetableEvent getUITimetableEvent() {
        List<UITimetableEvent> uiEvents = Components.findChildrenWithClass(this, UITimetableEvent.class);
        if (uiEvents.size() > 1) {
            FacesContext currentInstance = FacesContext.getCurrentInstance();
            throw new FacesException("There should be only one <o:timetableEvent> tag inside of the same timetable " +
                    "(or timetable view) component. Component iclientId: " + getClientId(currentInstance));
        }
        UITimetableEvent result = uiEvents.size() > 0 ? uiEvents.get(0) : null;
        if (result == null) {
            Timetable timetable = getTimetable();
            if (timetable != null)
                result = timetable.getUITimetableEvent();
        }
        return result;
    }


    public UIComponent getHeader() {
        UIComponent result = Components.getFacet(this, "header");
        return result;
    }

    public void setHeader(UIComponent header) {
        getFacets().put("header", header);
    }

    public UIComponent getHeaderRight() {
        UIComponent result = Components.getFacet(this, "headerRight");
        return result;
    }

    public void setHeaderRight(UIComponent header) {
        getFacets().put("headerRight", header);
    }

    public UIComponent getFooter() {
        UIComponent result = Components.getFacet(this, "footer");
        return result;
    }

    public void setFooter(UIComponent footer) {
        getFacets().put("footer", footer);
    }
    public UIComponent getFooterRight() {
        UIComponent result = Components.getFacet(this, "footerRight");
        return result;
    }

    public void setFooterRight(UIComponent footerRight) {
        getFacets().put("footerRight", footerRight);
    }

    public abstract Timetable.ViewType getType();

}
