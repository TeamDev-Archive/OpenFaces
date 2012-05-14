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

import org.openfaces.util.Components;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public abstract class TimeScaleTable extends TimetableView {
    private static final String RESOURCE_HEADER_FACET_NAME = "resourceHeader";
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    // todo: implement keyboard events inherited from OUIObjectIterator, this should probably be done along with keyboard support

    private String startTime;
    private String endTime;
    private String scrollTime;
    private TimeTextPosition timeTextPosition;

    private String resourceHeadersRowStyle;
    private String resourceHeadersRowClass;
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


    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),

                startTime,
                endTime,
                scrollTime,
                timeTextPosition,

                resourceHeadersRowStyle,
                resourceHeadersRowClass,
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

        startTime = (String) state[i++];
        endTime = (String) state[i++];
        scrollTime = (String) state[i++];
        timeTextPosition = (TimeTextPosition) state[i++];

        resourceHeadersRowStyle = (String) state[i++];
        resourceHeadersRowClass = (String) state[i++];
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

    public UIComponent getResourceHeader() {
        UIComponent result = Components.getFacet(this, RESOURCE_HEADER_FACET_NAME);
        if (result == null) {
            Timetable timetable = getTimetable();
            if (timetable != null)
                result = timetable.getResourceHeader();
        }
        return result;
    }

    public void setResourceHeader(UIComponent component) {
        getFacets().put(RESOURCE_HEADER_FACET_NAME, component);
    }

    public String getStartTime() {
        String startTime = ValueBindings.get(this, getTimetable(), "startTime", this.startTime);
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
        String endTime = ValueBindings.get(this, getTimetable(), "endTime", this.endTime);
        checkTimeString(endTime);
        return endTime;
    }

    public void setEndTime(String endTime) {
        checkTimeString(endTime);
        this.endTime = endTime;
    }

    public String getScrollTime() {
        String scrollTime = ValueBindings.get(this, getTimetable(), "scrollTime", this.scrollTime);
        checkTimeString(scrollTime);
        return scrollTime;
    }

    public void setScrollTime(String scrollTime) {
        checkTimeString(scrollTime);

        this.scrollTime = scrollTime;
    }


    public TimeTextPosition getTimeTextPosition() {
        return ValueBindings.get(this, getTimetable(), "timeTextPosition", timeTextPosition, TimeTextPosition.UNDER_MARK, TimeTextPosition.class);
    }

    public void setTimeTextPosition(TimeTextPosition timeTextPosition) {
        this.timeTextPosition = timeTextPosition;
    }

    public String getResourceHeadersRowStyle() {
        return ValueBindings.get(this, getTimetable(), "resourceHeadersRowStyle", resourceHeadersRowStyle);
    }

    public void setResourceHeadersRowStyle(String resourceHeadersRowStyle) {
        this.resourceHeadersRowStyle = resourceHeadersRowStyle;
    }

    public String getResourceHeadersRowClass() {
        return ValueBindings.get(this, getTimetable(), "resourceHeadersRowClass", resourceHeadersRowClass);
    }

    public void setResourceHeadersRowClass(String resourceHeadersRowClass) {
        this.resourceHeadersRowClass = resourceHeadersRowClass;
    }

    public String getTimeColumnStyle() {
        return ValueBindings.get(this, getTimetable(), "timeColumnStyle", timeColumnStyle);
    }

    public void setTimeColumnStyle(String timeColumnStyle) {
        this.timeColumnStyle = timeColumnStyle;
    }

    public String getTimeColumnClass() {
        return ValueBindings.get(this, getTimetable(), "timeColumnClass", timeColumnClass);
    }

    public void setTimeColumnClass(String timeColumnClass) {
        this.timeColumnClass = timeColumnClass;
    }


    public String getTimePattern() {
        return ValueBindings.get(this, getTimetable(), "timePattern", timePattern, "H");
    }

    public void setTimePattern(String timePattern) {
        this.timePattern = timePattern;
    }

    public String getTimeSuffixPattern() {
        return ValueBindings.get(this, getTimetable(), "timeSuffixPattern", timeSuffixPattern, "mm");
    }

    public void setTimeSuffixPattern(String timeSuffixPattern) {
        this.timeSuffixPattern = timeSuffixPattern;
    }

    public int getMajorTimeInterval() {
        return ValueBindings.get(this, getTimetable(), "majorTimeInterval", majorTimeInterval, 60);
    }

    public void setMajorTimeInterval(int majorTimeInterval) {
        this.majorTimeInterval = majorTimeInterval;
    }

    public int getMinorTimeInterval() {
        return ValueBindings.get(this, getTimetable(), "minorTimeInterval", minorTimeInterval, 30);
    }

    public void setMinorTimeInterval(int minorTimeInterval) {
        this.minorTimeInterval = minorTimeInterval;
    }

    public boolean getShowTimeForMinorIntervals() {
        return ValueBindings.get(this, getTimetable(), "showTimeForMinorIntervals", showTimeForMinorIntervals, false);
    }

    public void setShowTimeForMinorIntervals(boolean showTimeForMinorIntervals) {
        this.showTimeForMinorIntervals = showTimeForMinorIntervals;
    }

    public String getMajorTimeStyle() {
        return ValueBindings.get(this, getTimetable(), "majorTimeStyle", majorTimeStyle);
    }

    public void setMajorTimeStyle(String majorTimeStyle) {
        this.majorTimeStyle = majorTimeStyle;
    }

    public String getMajorTimeClass() {
        return ValueBindings.get(this, getTimetable(), "majorTimeClass", majorTimeClass);
    }

    public void setMajorTimeClass(String majorTimeClass) {
        this.majorTimeClass = majorTimeClass;
    }

    public String getMinorTimeStyle() {
        return ValueBindings.get(this, getTimetable(), "minorTimeStyle", minorTimeStyle);
    }

    public void setMinorTimeStyle(String minorTimeStyle) {
        this.minorTimeStyle = minorTimeStyle;
    }

    public String getMinorTimeClass() {
        return ValueBindings.get(this, getTimetable(), "minorTimeClass", minorTimeClass);
    }

    public void setMinorTimeClass(String minorTimeClass) {
        this.minorTimeClass = minorTimeClass;
    }

    public String getTimeSuffixStyle() {
        return ValueBindings.get(this, getTimetable(), "timeSuffixStyle", timeSuffixStyle);
    }

    public void setTimeSuffixStyle(String timeSuffixStyle) {
        this.timeSuffixStyle = timeSuffixStyle;
    }

    public String getTimeSuffixClass() {
        return ValueBindings.get(this, getTimetable(), "timeSuffixClass", timeSuffixClass);
    }

    public void setTimeSuffixClass(String timeSuffixClass) {
        this.timeSuffixClass = timeSuffixClass;
    }

    public int getDragAndDropTransitionPeriod() {
        return ValueBindings.get(this, getTimetable(), "dragAndDropTransitionPeriod", dragAndDropTransitionPeriod, 70);
    }

    public void setDragAndDropTransitionPeriod(int dragAndDropTransitionPeriod) {
        this.dragAndDropTransitionPeriod = dragAndDropTransitionPeriod;
    }

    public int getDragAndDropCancelingPeriod() {
        return ValueBindings.get(this, getTimetable(), "dragAndDropCancelingPeriod", dragAndDropCancelingPeriod, 200);
    }

    public void setDragAndDropCancelingPeriod(int dragAndDropCancelingPeriod) {
        this.dragAndDropCancelingPeriod = dragAndDropCancelingPeriod;
    }

    public int getUndroppableStateTransitionPeriod() {
        return ValueBindings.get(this, getTimetable(), "undroppableStateTransitionPeriod", undroppableStateTransitionPeriod, 250);
    }

    public void setUndroppableStateTransitionPeriod(int undroppableStateTransitionPeriod) {
        this.undroppableStateTransitionPeriod = undroppableStateTransitionPeriod;
    }

    public double getUndroppableEventTransparency() {
        return ValueBindings.get(this, getTimetable(), "undroppableEventTransparency", undroppableEventTransparency, 0.5);
    }

    public void setUndroppableEventTransparency(double undroppableEventTransparency) {
        this.undroppableEventTransparency = undroppableEventTransparency;
    }

    public String getResourceColumnSeparator() {
        return ValueBindings.get(this, getTimetable(), "resourceColumnSeparator", resourceColumnSeparator);
    }

    public void setResourceColumnSeparator(String resourceColumnSeparator) {
        this.resourceColumnSeparator = resourceColumnSeparator;
    }

    public String getResourceHeadersRowSeparator() {
        return ValueBindings.get(this, getTimetable(), "resourceHeadersRowSeparator", resourceHeadersRowSeparator);
    }

    public void setResourceHeadersRowSeparator(String resourceHeadersRowSeparator) {
        this.resourceHeadersRowSeparator = resourceHeadersRowSeparator;
    }

    public String getTimeColumnSeparator() {
        return ValueBindings.get(this, getTimetable(), "timeColumnSeparator", timeColumnSeparator);
    }

    public void setTimeColumnSeparator(String timeColumnSeparator) {
        this.timeColumnSeparator = timeColumnSeparator;
    }

    public String getPrimaryRowSeparator() {
        return ValueBindings.get(this, getTimetable(), "primaryRowSeparator", primaryRowSeparator);
    }

    public void setPrimaryRowSeparator(String primaryRowSeparator) {
        this.primaryRowSeparator = primaryRowSeparator;
    }

    public String getSecondaryRowSeparator() {
        return ValueBindings.get(this, getTimetable(), "secondaryRowSeparator", secondaryRowSeparator);
    }

    public void setSecondaryRowSeparator(String secondaryRowSeparator) {
        this.secondaryRowSeparator = secondaryRowSeparator;
    }

    public String getTimeColumnPrimaryRowSeparator() {
        return ValueBindings.get(this, getTimetable(), "timeColumnPrimaryRowSeparator", timeColumnPrimaryRowSeparator);
    }

    public void setTimeColumnPrimaryRowSeparator(String timeColumnPrimaryRowSeparator) {
        this.timeColumnPrimaryRowSeparator = timeColumnPrimaryRowSeparator;
    }

    public String getTimeColumnSecondaryRowSeparator() {
        return ValueBindings.get(this, getTimetable(), "timeColumnSecondaryRowSeparator", timeColumnSecondaryRowSeparator);
    }

    public void setTimeColumnSecondaryRowSeparator(String timeColumnSecondaryRowSeparator) {
        this.timeColumnSecondaryRowSeparator = timeColumnSecondaryRowSeparator;
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
        Object prevEventVarValue = requestMap.get(getEventVar());
        Map<?, AbstractTimetableEvent> eventMap = getLoadedEvents();
        Collection<AbstractTimetableEvent> events = eventMap.values();
        for (AbstractTimetableEvent event : events) {
            setEvent(event);
            for (EventArea eventArea : areas) {
                processor.process(context, eventArea);
            }
        }
        setEvent(null);
        requestMap.put(getEventVar(), prevEventVarValue);
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
