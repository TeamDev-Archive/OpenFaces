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

import org.openfaces.util.ValueBindings;
import org.openfaces.component.HorizontalAlignment;
import org.openfaces.component.VerticalAlignment;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class EventPreview extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.EventPreview";
    public static final String COMPONENT_FAMILY = "org.openfaces.EventPreview";

    private String style;
    private String styleClass;
    private String eventNameStyle;
    private String eventNameClass;
    private String eventDescriptionStyle;
    private String eventDescriptionClass;
    private Boolean escapeEventName;
    private Boolean escapeEventDescription;

    private HorizontalAlignment horizontalAlignment;
    private VerticalAlignment verticalAlignment;
    private String horizontalDistance;
    private String verticalDistance;

    private Integer showingDelay;

    public EventPreview() {
        setRendererType("org.openfaces.EventPreviewRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                style, styleClass,
                eventNameStyle, eventNameClass, eventDescriptionStyle, eventDescriptionClass, escapeEventName,
                escapeEventDescription, horizontalAlignment, verticalAlignment, horizontalDistance, verticalDistance,
                showingDelay};
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        style = (String) state[i++];
        styleClass = (String) state[i++];
        eventNameStyle = (String) state[i++];
        eventNameClass = (String) state[i++];
        eventDescriptionStyle = (String) state[i++];
        eventDescriptionClass = (String) state[i++];
        escapeEventName = (Boolean) state[i++];
        escapeEventDescription = (Boolean) state[i++];

        horizontalAlignment = (HorizontalAlignment) state[i++];
        verticalAlignment = (VerticalAlignment) state[i++];
        horizontalDistance = (String) state[i++];
        verticalDistance = (String) state[i++];
        showingDelay = (Integer) state[i++];
    }

    public String getStyle() {
        return ValueBindings.get(this, "style", style);
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return ValueBindings.get(this, "styleClass", styleClass);
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getEventNameStyle() {
        return ValueBindings.get(this, "eventNameStyle", eventNameStyle);
    }

    public void setEventNameStyle(String eventNameStyle) {
        this.eventNameStyle = eventNameStyle;
    }

    public String getEventNameClass() {
        return ValueBindings.get(this, "eventNameClass", eventNameClass);
    }

    public void setEventNameClass(String eventNameClass) {
        this.eventNameClass = eventNameClass;
    }

    public String getEventDescriptionStyle() {
        return ValueBindings.get(this, "eventDescriptionStyle", eventDescriptionStyle);
    }

    public void setEventDescriptionStyle(String eventDescriptionStyle) {
        this.eventDescriptionStyle = eventDescriptionStyle;
    }

    public String getEventDescriptionClass() {
        return ValueBindings.get(this, "eventDescriptionClass", eventDescriptionClass);
    }

    public void setEventDescriptionClass(String eventDescriptionClass) {
        this.eventDescriptionClass = eventDescriptionClass;
    }

    public boolean getEscapeEventName() {
        return ValueBindings.get(this, "escapeEventName", escapeEventName, true);
    }

    public void setEscapeEventName(boolean escapeEventName) {
        this.escapeEventName = escapeEventName;
    }

    public boolean getEscapeEventDescription() {
        return ValueBindings.get(this, "escapeEventDescription", escapeEventDescription, true);
    }

    public void setEscapeEventDescription(boolean escapeEventDescription) {
        this.escapeEventDescription = escapeEventDescription;
    }

    public HorizontalAlignment getHorizontalAlignment() {
        return ValueBindings.get(
                this, "horizontalAlignment", horizontalAlignment, HorizontalAlignment.LEFT, HorizontalAlignment.class);
    }

    public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public VerticalAlignment getVerticalAlignment() {
        return ValueBindings.get(
                this, "verticalAlignment", verticalAlignment, VerticalAlignment.ABOVE, VerticalAlignment.class);
    }

    public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public String getHorizontalDistance() {
        return ValueBindings.get(this, "horizontalDistance", horizontalDistance, "0");
    }

    public void setHorizontalDistance(String horizontalDistance) {
        this.horizontalDistance = horizontalDistance;
    }

    public String getVerticalDistance() {
        return ValueBindings.get(this, "verticalDistance", verticalDistance, "10px");
    }

    public void setVerticalDistance(String verticalDistance) {
        this.verticalDistance = verticalDistance;
    }

    public int getShowingDelay() {
        return ValueBindings.get(this, "showingDelay", showingDelay, 400);
    }

    public void setShowingDelay(int showingDelay) {
        this.showingDelay = showingDelay;
    }

    @Override
    public void setId(String id) {
        super.setId("_eventPreview");  //this component should be only the one at DayTable. See JSFC-3916
    }
}
