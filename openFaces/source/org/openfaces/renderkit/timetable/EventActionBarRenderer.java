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

import org.openfaces.component.timetable.DayTable;
import org.openfaces.component.timetable.DeleteEventAction;
import org.openfaces.component.timetable.EventAction;
import org.openfaces.component.timetable.EventActionBar;
import org.openfaces.component.window.Confirmation;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventActionBarRenderer extends RendererBase {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        EventActionBar actionBar = (EventActionBar) component;
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", actionBar);
        String clientId = actionBar.getClientId(context);
        writer.writeAttribute("id", clientId, null);
        Rendering.writeStandardEvents(writer, actionBar);

        writer.writeText(actionBar.getNoteText(), null);
        writer.endElement("div");

        DayTable dayTable = (DayTable) actionBar.getParent();

        String userSpecifiedStyle = Styles.getCSSClass(context, actionBar, actionBar.getStyle(), actionBar.getStyleClass());
        JSONArray eventActions = getEventActions(context, actionBar);
        double actionRolloverBackgroundIntensity = actionBar.getActionRolloverBackgroundIntensity();
        double actionPressedBackgroundIntensity = actionBar.getActionPressedBackgroundIntensity();
        Rendering.renderInitScript(context, new ScriptBuilder().initScript(context, actionBar, "O$._initEventActionBar",
                dayTable,
                actionBar.getBackgroundIntensity(),
                userSpecifiedStyle,
                eventActions,
                actionRolloverBackgroundIntensity,
                actionPressedBackgroundIntensity));
        Styles.renderStyleClasses(context, actionBar);
        Confirmation confirmation = dayTable.getDeletionConfirmation();
        if (confirmation.isRendered())
            confirmation.encodeAll(context);
    }

    private List<EventAction> getEventActionComponents(FacesContext context, EventActionBar actionBar) {
        List<EventAction> result = new ArrayList<EventAction>();
        List<UIComponent> children = actionBar.getChildren();
        DeleteEventAction deleteEventAction = null;
        for (UIComponent component : children) {
            if (!(component instanceof EventAction))
                continue;
            if (component.isRendered())
                result.add((EventAction) component);
            if (component instanceof DeleteEventAction)
                deleteEventAction = (DeleteEventAction) component;
        }
        DayTable dayTable = (DayTable) actionBar.getParent();
        boolean tableEditable = dayTable.isEditable();
        if (tableEditable) {
            if (deleteEventAction == null) {
                deleteEventAction = new DeleteEventAction();
                result.add(deleteEventAction);
                actionBar.getChildren().add(deleteEventAction);
            }
            deleteEventAction.setOnclick("O$._deleteCurrentTimetableEvent(event)");
            Confirmation confirmation = dayTable.getDeletionConfirmation();
            if (deleteEventAction.isRendered() && deleteEventAction.getShowConfirmation()) {
                confirmation.setRendered(true);
                if (deleteEventAction.getId() == null)
                    deleteEventAction.setId(context.getViewRoot().createUniqueId());
                confirmation.setFor(deleteEventAction.getId());
                confirmation.setEvent("click");
            } else
                confirmation.setRendered(false);
        }

        return result;
    }

    private JSONArray getEventActions(FacesContext context, EventActionBar actionBar) {
        List<EventAction> actionComponents = getEventActionComponents(context, actionBar);
        JSONArray result = new JSONArray();
        for (EventAction action : actionComponents) {
            try {
                JSONObject actionParams = getActionParams(context, actionBar, action);
                result.put(result.length(), actionParams);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    private JSONObject getActionParams(FacesContext context, EventActionBar actionBar, EventAction action) throws JSONException {
        JSONObject params = new JSONObject();
        if (action.getId() != null)
            params.put("id", action.getClientId(context));
        params.put("image", new JSONArray(Arrays.asList(
                getActionImageUrl(context, action),
                Resources.getApplicationURL(context, action.getRolloverImageUrl()),
                Resources.getApplicationURL(context, action.getPressedImageUrl()))));
        params.put("style", new JSONArray(Arrays.asList(
                Styles.getCSSClass(context, actionBar, action.getStyle(), "o_eventActionButton", action.getStyleClass()),
                Styles.getCSSClass(context, actionBar, action.getRolloverStyle(), action.getRolloverClass()),
                Styles.getCSSClass(context, actionBar, action.getPressedStyle(), action.getPressedClass()))));
        params.put("onclick", action.getOnclick());
        params.put("hint", action.getHint());
        return params;
    }

    private String getActionImageUrl(FacesContext context, EventAction action) {
        if (action instanceof DeleteEventAction && action.getImageUrl() == null) {
            return Resources.getInternalURL(
                    FacesContext.getCurrentInstance(), EventActionBarRenderer.class, "deleteEvent.gif");
        }
        return Resources.getApplicationURL(context, action.getImageUrl());
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }
}
