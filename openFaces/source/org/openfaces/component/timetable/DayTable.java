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
package org.openfaces.component.timetable;

import org.openfaces.component.OUIData;
import org.openfaces.component.OUIObjectIteratorBase;
import org.openfaces.component.window.Confirmation;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.CalendarUtil;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.ValueBindings;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Dmitry Pikhulya
 */
public class DayTable extends OUIObjectIteratorBase {
    public static final String COMPONENT_TYPE = "org.openfaces.DayTable";
    public static final String COMPONENT_FAMILY = "org.openfaces.DayTable";

    private static final String RESOURCE_HEADER_FACET_NAME = "resourceHeader";
    private static final String EVENT_EDITOR_FACET_NAME = "eventEditor";
    private static final String DELETE_EVENT_CONFIRMATION_FACET_NAME = "deleteEventConfirmation";
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    private String eventVar = "event";
    private AbstractTimetableEvent event;
    private Boolean editable;
    private MethodExpression timetableChangeListener;
    private String onchange;
    private Date day;


    // todo: implement keyboard events inherited from OUIObjectIterator, this should probably be done along with keyboard support
    private Locale locale;
    private TimeZone timeZone;
    private PreloadedEvents preloadedEvents;
    private Map<String, AbstractTimetableEvent> loadedEvents = new HashMap<String, AbstractTimetableEvent>();

    private String startTime;
    private String endTime;
    private String scrollTime;
    private TimeTextPosition timeTextPosition;

    private String headerStyle;
    private String headerClass;
    private String footerStyle;
    private String footerClass;
    private String resourceHeadersRowStyle;
    private String resourceHeadersRowClass;
    private String rowStyle;
    private String rowClass;
    private String timeColumnStyle;
    private String timeColumnClass;
    private String timePattern;
    private String timeSuffixPattern;
    private Integer majorTimeInterval;
    private Integer minorTimeInterval;
    private Boolean showTimeForMinorIntervals;

    private String majorTimeStyle;
    private String majorTimeClass;
    private String minorTimeStyle;
    private String minorTimeClass;
    private String timeSuffixStyle;
    private String timeSuffixClass;

    private Color defaultEventColor;
    private Color reservedTimeEventColor;
    private String reservedTimeEventStyle;
    private String reservedTimeEventClass;
    private String rolloverEventNoteStyle;
    private String rolloverEventNoteClass;

    private Integer dragAndDropTransitionPeriod;
    private Integer dragAndDropCancelingPeriod;
    private Integer undroppableStateTransitionPeriod;
    private Double undroppableEventTransparency;

    private String resourceColumnSeparator;
    private String resourceHeadersRowSeparator;
    private String timeColumnSeparator;
    private String primaryRowSeparator;
    private String secondaryRowSeparator;
    private String timeColumnPrimaryRowSeparator;
    private String timeColumnSecondaryRowSeparator;


    private Object initialDescendantsState;
    private Map<String, Object> descendantsStateForEvents = new HashMap<String, Object>();
    private boolean childrenValid = true;

    public DayTable() {
        setRendererType("org.openfaces.DayTableRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                eventVar,
                editable,
                saveAttachedState(context, timetableChangeListener),
                onchange,
                day,
                locale,
                timeZone,
                preloadedEvents,
                loadedEvents,

                startTime,
                endTime,
                scrollTime,
                timeTextPosition,

                headerStyle,
                headerClass,
                footerStyle,
                footerClass,
                resourceHeadersRowStyle,
                resourceHeadersRowClass,
                rowStyle,
                rowClass,
                timeColumnStyle,
                timeColumnClass,
                timePattern,
                timeSuffixPattern,
                majorTimeInterval,
                minorTimeInterval,
                showTimeForMinorIntervals,
                majorTimeStyle,
                majorTimeClass,
                minorTimeStyle,
                minorTimeClass,
                timeSuffixStyle,
                timeSuffixClass,

                defaultEventColor,
                reservedTimeEventColor,
                reservedTimeEventStyle,
                reservedTimeEventClass,
                rolloverEventNoteStyle,
                rolloverEventNoteClass,

                dragAndDropCancelingPeriod,
                dragAndDropTransitionPeriod,
                undroppableStateTransitionPeriod,
                undroppableEventTransparency,

                resourceColumnSeparator,
                resourceHeadersRowSeparator,
                timeColumnSeparator,
                primaryRowSeparator,
                secondaryRowSeparator,
                timeColumnPrimaryRowSeparator,
                timeColumnSecondaryRowSeparator
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        eventVar = (String) state[i++];
        editable = (Boolean) state[i++];
        timetableChangeListener = (MethodExpression) restoreAttachedState(context, state[i++]);
        onchange = (String) state[i++];
        day = (Date) state[i++];
        locale = (Locale) state[i++];
        timeZone = (TimeZone) state[i++];
        preloadedEvents = (PreloadedEvents) state[i++];
        loadedEvents = (Map<String, AbstractTimetableEvent>) state[i++];

        startTime = (String) state[i++];
        endTime = (String) state[i++];
        scrollTime = (String) state[i++];
        timeTextPosition = (TimeTextPosition) state[i++];

        headerStyle = (String) state[i++];
        headerClass = (String) state[i++];
        footerStyle = (String) state[i++];
        footerClass = (String) state[i++];
        resourceHeadersRowStyle = (String) state[i++];
        resourceHeadersRowClass = (String) state[i++];
        rowStyle = (String) state[i++];
        rowClass = (String) state[i++];
        timeColumnStyle = (String) state[i++];
        timeColumnClass = (String) state[i++];
        timePattern = (String) state[i++];
        timeSuffixPattern = (String) state[i++];
        majorTimeInterval = (Integer) state[i++];
        minorTimeInterval = (Integer) state[i++];
        showTimeForMinorIntervals = (Boolean) state[i++];
        majorTimeStyle = (String) state[i++];
        majorTimeClass = (String) state[i++];
        minorTimeStyle = (String) state[i++];
        minorTimeClass = (String) state[i++];
        timeSuffixStyle = (String) state[i++];
        timeSuffixClass = (String) state[i++];

        defaultEventColor = (Color) state[i++];
        reservedTimeEventColor = (Color) state[i++];
        reservedTimeEventStyle = (String) state[i++];
        reservedTimeEventClass = (String) state[i++];
        rolloverEventNoteStyle = (String) state[i++];
        rolloverEventNoteClass = (String) state[i++];

        dragAndDropTransitionPeriod = (Integer) state[i++];
        dragAndDropCancelingPeriod = (Integer) state[i++];
        undroppableStateTransitionPeriod = (Integer) state[i++];
        undroppableEventTransparency = (Double) state[i++];

        resourceColumnSeparator = (String) state[i++];
        resourceHeadersRowSeparator = (String) state[i++];
        timeColumnSeparator = (String) state[i++];
        primaryRowSeparator = (String) state[i++];
        secondaryRowSeparator = (String) state[i++];
        timeColumnPrimaryRowSeparator = (String) state[i++];
        timeColumnSecondaryRowSeparator = (String) state[i++];
    }

    @Override
    public void processRestoreState(FacesContext context, Object state) {
        Object ajaxState = AjaxUtil.retrieveAjaxStateObject(context, this);
        super.processRestoreState(context, ajaxState != null ? ajaxState : state);
    }

    public String getEventVar() {
        return eventVar;
    }

    public void setEventVar(String eventVar) {
        this.eventVar = eventVar;
    }

    public boolean isEditable() {
        return ValueBindings.get(this, "editable", editable, true);
    }

    public void setEditable(boolean value) {
        editable = value;
    }

    public ValueExpression getEventsValueExpression() {
        return getValueExpression("events");
    }

    public void setEventsValueExpression(ValueExpression expression) {
        setValueExpression("events", expression);
    }

    public Date getDay() {
        return ValueBindings.get(this, "day", day, new Date(), Date.class);
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Locale getLocale() {
        return CalendarUtil.getBoundPropertyValueAsLocale(this, "locale", locale);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public TimeZone getTimeZone() {
        return ValueBindings.get(this, "timeZone", timeZone,
                TimeZone.getDefault(), TimeZone.class);
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public PreloadedEvents getPreloadedEvents() {
        return ValueBindings.get(this, "preloadedEvents", preloadedEvents, PreloadedEvents.ALL, PreloadedEvents.class);
    }

    public void setPreloadedEvents(PreloadedEvents preloadedEvents) {
        this.preloadedEvents = preloadedEvents;
    }

    public ValueExpression getResourcesValueExpression() {
        return getValueExpression("resources");
    }

    public void setResourcesValueExpression(ValueExpression expression) {
        setValueExpression("resources", expression);
    }

    public UIComponent getResourceHeader() {
        return getFacet(RESOURCE_HEADER_FACET_NAME);
    }

    public void setResourceHeader(UIComponent component) {
        getFacets().put(RESOURCE_HEADER_FACET_NAME, component);
    }

    public UIComponent getEventEditor() {
        return getFacet(EVENT_EDITOR_FACET_NAME);
    }

    public void setEventEditor(UIComponent dialog) {
        getFacets().put(EVENT_EDITOR_FACET_NAME, dialog);
    }

    public Confirmation getDeleteEventConfirmation() {
        return (Confirmation) getFacet(DELETE_EVENT_CONFIRMATION_FACET_NAME);
    }

    public void setDeleteEventConfirmation(Confirmation confirmation) {
        getFacets().put(DELETE_EVENT_CONFIRMATION_FACET_NAME, confirmation);
    }

    public String getStartTime() {
        String startTime = ValueBindings.get(this, "startTime", this.startTime);
        checkTimeString(startTime);
        return startTime;
    }

    public void setStartTime(String startTime) {
        checkTimeString(startTime);

        this.startTime = startTime;
    }

    private void checkTimeString(String startTime) {
        if (startTime == null)
            return;
        try {
            TIME_FORMAT.parse(startTime);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Couldn't parse time string. The value should comply to the 'HH:mm' pattern, but the following value was encountered: \"" + startTime + "\"");
        }
    }

    public String getEndTime() {
        String endTime = ValueBindings.get(this, "endTime", this.endTime);
        checkTimeString(endTime);
        return endTime;
    }

    public void setEndTime(String endTime) {
        checkTimeString(endTime);
        this.endTime = endTime;
    }

    public String getScrollTime() {
        String scrollTime = ValueBindings.get(this, "scrollTime", this.scrollTime);
        checkTimeString(scrollTime);
        return scrollTime;
    }

    public void setScrollTime(String scrollTime) {
        checkTimeString(scrollTime);

        this.scrollTime = scrollTime;
    }


    public TimeTextPosition getTimeTextPosition() {
        return ValueBindings.get(this, "timeTextPosition", timeTextPosition, TimeTextPosition.UNDER_MARK, TimeTextPosition.class);
    }

    public void setTimeTextPosition(TimeTextPosition timeTextPosition) {
        this.timeTextPosition = timeTextPosition;
    }

    public String getHeaderStyle() {
        return ValueBindings.get(this, "headerStyle", headerStyle);
    }

    public void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle;
    }

    public String getHeaderClass() {
        return ValueBindings.get(this, "headerClass", headerClass);
    }

    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }

    public String getFooterStyle() {
        return ValueBindings.get(this, "footerStyle", footerStyle);
    }

    public void setFooterStyle(String footerStyle) {
        this.footerStyle = footerStyle;
    }

    public String getFooterClass() {
        return ValueBindings.get(this, "footerClass", footerClass);
    }

    public void setFooterClass(String footerClass) {
        this.footerClass = footerClass;
    }

    public String getResourceHeadersRowStyle() {
        return ValueBindings.get(this, "resourceHeadersRowStyle", resourceHeadersRowStyle);
    }

    public void setResourceHeadersRowStyle(String resourceHeadersRowStyle) {
        this.resourceHeadersRowStyle = resourceHeadersRowStyle;
    }

    public String getResourceHeadersRowClass() {
        return ValueBindings.get(this, "resourceHeadersRowClass", resourceHeadersRowClass);
    }

    public void setResourceHeadersRowClass(String resourceHeadersRowClass) {
        this.resourceHeadersRowClass = resourceHeadersRowClass;
    }

    public String getRowStyle() {
        return ValueBindings.get(this, "rowStyle", rowStyle);
    }

    public void setRowStyle(String rowStyle) {
        this.rowStyle = rowStyle;
    }

    public String getRowClass() {
        return ValueBindings.get(this, "rowClass", rowClass);
    }

    public void setRowClass(String rowClass) {
        this.rowClass = rowClass;
    }

    public String getTimeColumnStyle() {
        return ValueBindings.get(this, "timeColumnStyle", timeColumnStyle);
    }

    public void setTimeColumnStyle(String timeColumnStyle) {
        this.timeColumnStyle = timeColumnStyle;
    }

    public String getTimeColumnClass() {
        return ValueBindings.get(this, "timeColumnClass", timeColumnClass);
    }

    public void setTimeColumnClass(String timeColumnClass) {
        this.timeColumnClass = timeColumnClass;
    }


    public String getTimePattern() {
        return ValueBindings.get(this, "timePattern", timePattern, "H");
    }

    public void setTimePattern(String timePattern) {
        this.timePattern = timePattern;
    }

    public String getTimeSuffixPattern() {
        return ValueBindings.get(this, "timeSuffixPattern", timeSuffixPattern, "mm");
    }

    public void setTimeSuffixPattern(String timeSuffixPattern) {
        this.timeSuffixPattern = timeSuffixPattern;
    }

    public int getMajorTimeInterval() {
        return ValueBindings.get(this, "majorTimeInterval", majorTimeInterval, 60);
    }

    public void setMajorTimeInterval(int majorTimeInterval) {
        this.majorTimeInterval = majorTimeInterval;
    }

    public int getMinorTimeInterval() {
        return ValueBindings.get(this, "minorTimeInterval", minorTimeInterval, 30);
    }

    public void setMinorTimeInterval(int minorTimeInterval) {
        this.minorTimeInterval = minorTimeInterval;
    }

    public boolean getShowTimeForMinorIntervals() {
        return ValueBindings.get(this, "showTimeForMinorIntervals", showTimeForMinorIntervals, false);
    }

    public void setShowTimeForMinorIntervals(boolean showTimeForMinorIntervals) {
        this.showTimeForMinorIntervals = showTimeForMinorIntervals;
    }

    public String getMajorTimeStyle() {
        return ValueBindings.get(this, "majorTimeStyle", majorTimeStyle);
    }

    public void setMajorTimeStyle(String majorTimeStyle) {
        this.majorTimeStyle = majorTimeStyle;
    }

    public String getMajorTimeClass() {
        return ValueBindings.get(this, "majorTimeClass", majorTimeClass);
    }

    public void setMajorTimeClass(String majorTimeClass) {
        this.majorTimeClass = majorTimeClass;
    }

    public String getMinorTimeStyle() {
        return ValueBindings.get(this, "minorTimeStyle", minorTimeStyle);
    }

    public void setMinorTimeStyle(String minorTimeStyle) {
        this.minorTimeStyle = minorTimeStyle;
    }

    public String getMinorTimeClass() {
        return ValueBindings.get(this, "minorTimeClass", minorTimeClass);
    }

    public void setMinorTimeClass(String minorTimeClass) {
        this.minorTimeClass = minorTimeClass;
    }

    public String getTimeSuffixStyle() {
        return ValueBindings.get(this, "timeSuffixStyle", timeSuffixStyle);
    }

    public void setTimeSuffixStyle(String timeSuffixStyle) {
        this.timeSuffixStyle = timeSuffixStyle;
    }

    public String getTimeSuffixClass() {
        return ValueBindings.get(this, "timeSuffixClass", timeSuffixClass);
    }

    public void setTimeSuffixClass(String timeSuffixClass) {
        this.timeSuffixClass = timeSuffixClass;
    }

    public Color getDefaultEventColor() {
        return ValueBindings.get(this, "defaultEventColor", defaultEventColor, Color.class);
    }

    public void setDefaultEventColor(Color defaultEventColor) {
        this.defaultEventColor = defaultEventColor;
    }

    public Color getReservedTimeEventColor() {
        return ValueBindings.get(this, "reservedTimeEventColor", reservedTimeEventColor, Color.class);
    }

    public void setReservedTimeEventColor(Color reservedTimeEventColor) {
        this.reservedTimeEventColor = reservedTimeEventColor;
    }

    public String getReservedTimeEventStyle() {
        return ValueBindings.get(this, "reservedTimeEventStyle", reservedTimeEventStyle);
    }

    public void setReservedTimeEventStyle(String reservedTimeEventStyle) {
        this.reservedTimeEventStyle = reservedTimeEventStyle;
    }

    public String getReservedTimeEventClass() {
        return ValueBindings.get(this, "reservedTimeEventClass", reservedTimeEventClass);
    }

    public void setReservedTimeEventClass(String reservedTimeEventClass) {
        this.reservedTimeEventClass = reservedTimeEventClass;
    }

    public String getRolloverEventNoteStyle() {
        return ValueBindings.get(this, "rolloverEventNoteStyle", rolloverEventNoteStyle);
    }

    public void setRolloverEventNoteStyle(String rolloverEventNoteStyle) {
        this.rolloverEventNoteStyle = rolloverEventNoteStyle;
    }

    public String getRolloverEventNoteClass() {
        return ValueBindings.get(this, "rolloverEventNoteClass", rolloverEventNoteClass);
    }

    public void setRolloverEventNoteClass(String rolloverEventNoteClass) {
        this.rolloverEventNoteClass = rolloverEventNoteClass;
    }

    public int getDragAndDropTransitionPeriod() {
        return ValueBindings.get(this, "dragAndDropTransitionPeriod", dragAndDropTransitionPeriod, 70);
    }

    public void setDragAndDropTransitionPeriod(int dragAndDropTransitionPeriod) {
        this.dragAndDropTransitionPeriod = dragAndDropTransitionPeriod;
    }

    public int getDragAndDropCancelingPeriod() {
        return ValueBindings.get(this, "dragAndDropCancelingPeriod", dragAndDropCancelingPeriod, 200);
    }

    public void setDragAndDropCancelingPeriod(int dragAndDropCancelingPeriod) {
        this.dragAndDropCancelingPeriod = dragAndDropCancelingPeriod;
    }

    public int getUndroppableStateTransitionPeriod() {
        return ValueBindings.get(this, "undroppableStateTransitionPeriod", undroppableStateTransitionPeriod, 250);
    }

    public void setUndroppableStateTransitionPeriod(int undroppableStateTransitionPeriod) {
        this.undroppableStateTransitionPeriod = undroppableStateTransitionPeriod;
    }

    public double getUndroppableEventTransparency() {
        return ValueBindings.get(this, "undroppableEventTransparency", undroppableEventTransparency, 0.5);
    }

    public void setUndroppableEventTransparency(double undroppableEventTransparency) {
        this.undroppableEventTransparency = undroppableEventTransparency;
    }

    public String getResourceColumnSeparator() {
        return ValueBindings.get(this, "resourceColumnSeparator", resourceColumnSeparator);
    }

    public void setResourceColumnSeparator(String resourceColumnSeparator) {
        this.resourceColumnSeparator = resourceColumnSeparator;
    }

    public String getResourceHeadersRowSeparator() {
        return ValueBindings.get(this, "resourceHeadersRowSeparator", resourceHeadersRowSeparator);
    }

    public void setResourceHeadersRowSeparator(String resourceHeadersRowSeparator) {
        this.resourceHeadersRowSeparator = resourceHeadersRowSeparator;
    }

    public String getTimeColumnSeparator() {
        return ValueBindings.get(this, "timeColumnSeparator", timeColumnSeparator);
    }

    public void setTimeColumnSeparator(String timeColumnSeparator) {
        this.timeColumnSeparator = timeColumnSeparator;
    }

    public String getPrimaryRowSeparator() {
        return ValueBindings.get(this, "primaryRowSeparator", primaryRowSeparator);
    }

    public void setPrimaryRowSeparator(String primaryRowSeparator) {
        this.primaryRowSeparator = primaryRowSeparator;
    }

    public String getSecondaryRowSeparator() {
        return ValueBindings.get(this, "secondaryRowSeparator", secondaryRowSeparator);
    }

    public void setSecondaryRowSeparator(String secondaryRowSeparator) {
        this.secondaryRowSeparator = secondaryRowSeparator;
    }

    public String getTimeColumnPrimaryRowSeparator() {
        return ValueBindings.get(this, "timeColumnPrimaryRowSeparator", timeColumnPrimaryRowSeparator);
    }

    public void setTimeColumnPrimaryRowSeparator(String timeColumnPrimaryRowSeparator) {
        this.timeColumnPrimaryRowSeparator = timeColumnPrimaryRowSeparator;
    }

    public String getTimeColumnSecondaryRowSeparator() {
        return ValueBindings.get(this, "timeColumnSecondaryRowSeparator", timeColumnSecondaryRowSeparator);
    }

    public void setTimeColumnSecondaryRowSeparator(String timeColumnSecondaryRowSeparator) {
        this.timeColumnSecondaryRowSeparator = timeColumnSecondaryRowSeparator;
    }

    public MethodExpression getTimetableChangeListener() {
        return timetableChangeListener;
    }

    public void setTimetableChangeListener(MethodExpression timetableChangeListener) {
        this.timetableChangeListener = timetableChangeListener;
    }

    public String getOnchange() {
        return ValueBindings.get(this, "onchange", onchange);
    }

    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }

    public void addTimetableChangeListener(TimetableChangeListener listener) {
        addFacesListener(listener);
    }

    public void removeTimetableChangeListener(TimetableChangeListener listener) {
        removeFacesListener(listener);
    }

    public TimetableChangeListener[] getTimetableChangeListeners() {
        return (TimetableChangeListener[]) getFacesListeners(TimetableChangeListener.class);
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);
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

    public EventActionBar getEventActionBar() {
        return Components.getChildWithClass(this, EventActionBar.class, "eventActionBar");
    }

    public TimetableEditingOptions getEditingOptions() {
        return Components.getChildWithClass(this, TimetableEditingOptions.class, "editingOptions");
    }

    public EventPreview getEventPreview() {
        return Components.findChildWithClass(this, EventPreview.class);
    }


    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public Confirmation getDeletionConfirmation() {
        FacesContext context = FacesContext.getCurrentInstance();
        Confirmation confirmation = getDeleteEventConfirmation();
        if (confirmation == null) {
            confirmation = (Confirmation) Components.createComponent(context,
                    getId() + Rendering.SERVER_ID_SUFFIX_SEPARATOR + "deleteEventConfirmation",
                    Confirmation.COMPONENT_TYPE);
            confirmation.setMessage("Delete the event?");
            confirmation.setDetails("Click OK to delete the event");
            confirmation.setDraggable(true);
            setDeleteEventConfirmation(confirmation);
        }
        return confirmation;
    }

    public List<EventArea> getEventAreas() {
        return Components.findChildrenWithClass(this, EventArea.class);
    }

    public AbstractTimetableEvent getEvent() {
        return event;
    }

    public void setEvent(AbstractTimetableEvent event) {
        if (this.event == event)
            return;

        List<EventArea> eventAreas = getEventAreas();

        if (this.event == null)
            initialDescendantsState = OUIData.saveDescendantComponentStates(eventAreas.iterator(), true);
        else {
            String eventId = this.event.getId();
            Object stateForEvent = OUIData.saveDescendantComponentStates(eventAreas.iterator(), true);
            descendantsStateForEvents.put(eventId, stateForEvent);
        }

        FacesContext context = getFacesContext();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        requestMap.put(eventVar, event);
        this.event = event;

        if (this.event == null)
            OUIData.restoreDescendantComponentStates(eventAreas.iterator(), initialDescendantsState, true);
        else {
            Object stateForEvent = descendantsStateForEvents.get(this.event.getId());
            if (stateForEvent != null)
                OUIData.restoreDescendantComponentStates(eventAreas.iterator(), stateForEvent, true);
        }

        for (int areaIndex = 0, areaCount = eventAreas.size(); areaIndex < areaCount; areaIndex++) {
            EventArea eventArea = eventAreas.get(areaIndex);
            refreshEventSubRender(eventArea);
        }

    }

    private void refreshEventSubRender(UIComponent component) {
        // force client id recalculation
        component.setId(component.getId());
        Iterator<UIComponent> kids = component.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = kids.next();
            refreshEventSubRender(kid);
        }

    }


    public void setObjectId(String objectId) {
        if (objectId == null) {
            setEvent(null);
            return;
        }
        AbstractTimetableEvent event = getLoadedEvents().get(objectId);
        setEvent(event);
    }

    public String getObjectId() {
        if (event == null)
            return null;
        return event.getId();
    }

    @Override
    public void processDecodes(FacesContext context) {
        if (!isRendered())
            return;

        processInnerComponents(context, new DecodesComponentProcessor());

        try {
            decode(context);
        } catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        }

    }

    @Override
    public void processValidators(FacesContext context) {
        if (!isRendered())
            return;

        processInnerComponents(context, new ValidateComponentProcessor());

        if (context.getRenderResponse())
            childrenValid = false;
    }

    @Override
    public void processUpdates(FacesContext context) {
        if (!isRendered())
            return;

        processInnerComponents(context, new UpdatesComponentProcessor());

        if (context.getRenderResponse())
            childrenValid = false;
        if (day != null && ValueBindings.set(this, "day", day))
            day = null;
        if (scrollTime != null && ValueBindings.set(this, "scrollTime", scrollTime))
            scrollTime = null;
    }


    private void processInnerComponents(FacesContext context, ComponentProcessor processor) {
        Iterator<UIComponent> kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = kids.next();
            if (kid instanceof EventArea)
                continue;

            processor.process(context, kid);
        }

        List<EventArea> areas = getEventAreas();

        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        Object prevEventVarValue = requestMap.get(eventVar);
        Map<?, AbstractTimetableEvent> eventMap = getLoadedEvents();
        Collection<AbstractTimetableEvent> events = eventMap.values();
        for (AbstractTimetableEvent event : events) {
            setEvent(event);
            for (EventArea eventArea : areas) {
                processor.process(context, eventArea);
            }
        }
        setEvent(null);
        requestMap.put(eventVar, prevEventVarValue);
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

    private static class UpdatesComponentProcessor implements ComponentProcessor {
        public void process(FacesContext context, UIComponent component) {
            component.processUpdates(context);
        }
    }

    private static class DecodesComponentProcessor implements ComponentProcessor {
        public void process(FacesContext context, UIComponent component) {
            component.processDecodes(context);
        }
    }

    private static class ValidateComponentProcessor implements ComponentProcessor {
        public void process(FacesContext context, UIComponent component) {
            component.processValidators(context);
        }
    }
}
