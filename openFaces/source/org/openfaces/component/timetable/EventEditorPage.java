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

import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class EventEditorPage extends UICommand {
    public static final String COMPONENT_TYPE = "org.openfaces.EventEditorPage";
    public static final String COMPONENT_FAMILY = "org.openfaces.EventEditorPage";

    private String url;
    private String modeParamName;
    private String eventIdParamName;
    private String eventStartParamName;
    private String eventEndParamName;
    private String resourceIdParamName;

    public EventEditorPage() {
        setRendererType("org.openfaces.EventEditorPageRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                url,
                modeParamName,
                eventIdParamName,
                eventStartParamName,
                eventEndParamName,
                resourceIdParamName
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        url = (String) state[i++];
        modeParamName = (String) state[i++];
        eventIdParamName = (String) state[i++];
        eventStartParamName = (String) state[i++];
        eventEndParamName = (String) state[i++];
        resourceIdParamName = (String) state[i++];
    }


    public String getUrl() {
        return ValueBindings.get(this, "url", url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getModeParamName() {
        return ValueBindings.get(this, "modeParamName", modeParamName, "mode");
    }

    public void setModeParamName(String modeParamName) {
        this.modeParamName = modeParamName;
    }

    public String getEventIdParamName() {
        return ValueBindings.get(this, "eventIdParamName", eventIdParamName, "eventId");
    }

    public void setEventIdParamName(String eventIdParamName) {
        this.eventIdParamName = eventIdParamName;
    }

    public String getEventStartParamName() {
        return ValueBindings.get(this, "eventStartParamName", eventStartParamName, "eventStart");
    }

    public void setEventStartParamName(String eventStartParamName) {
        this.eventStartParamName = eventStartParamName;
    }

    public String getEventEndParamName() {
        return ValueBindings.get(this, "eventEndParamName", eventEndParamName, "eventEnd");
    }

    public void setEventEndParamName(String eventEndParamName) {
        this.eventEndParamName = eventEndParamName;
    }

    public String getResourceIdParamName() {
        return ValueBindings.get(this, "resourceIdParamName", resourceIdParamName, "resourceId");
    }

    public void setResourceIdParamName(String resourceIdParamName) {
        this.resourceIdParamName = resourceIdParamName;
    }

}
