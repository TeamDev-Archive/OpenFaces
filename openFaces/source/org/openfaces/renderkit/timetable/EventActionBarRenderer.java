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

import org.openfaces.component.timetable.DeleteEventAction;
import org.openfaces.component.timetable.EventAction;
import org.openfaces.component.timetable.EventActionBar;
import org.openfaces.component.timetable.MonthTable;
import org.openfaces.component.timetable.TimetableView;
import org.openfaces.component.window.Confirmation;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

        TimetableView timeTable = (TimetableView) actionBar.getParent();
        if (!(timeTable instanceof MonthTable)) {
            writer.writeText(actionBar.getNoteText(), null);
        }
        writer.endElement("div");

        String userSpecifiedStyle = Styles.getCSSClass(context, actionBar, actionBar.getStyle(), actionBar.getStyleClass());
        JSONArray eventActions = getEventActions(context, actionBar);
        double actionRolloverBackgroundIntensity = actionBar.getActionRolloverBackgroundIntensity();
        double actionPressedBackgroundIntensity = actionBar.getActionPressedBackgroundIntensity();
        Rendering.renderInitScript(context, new ScriptBuilder().initScript(context, actionBar, "O$.Timetable._initEventActionBar",
                actionBar.getBackgroundIntensity(),
                userSpecifiedStyle,
                eventActions,
                actionRolloverBackgroundIntensity,
                actionPressedBackgroundIntensity));
        Styles.renderStyleClasses(context, actionBar);

        Confirmation confirmation = timeTable.getDeletionConfirmation();

        if (confirmation != null && confirmation.isRendered())
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
        TimetableView timeTable = (TimetableView) actionBar.getParent();
        boolean tableEditable = timeTable.isEditable();
        if (tableEditable) {
            if (deleteEventAction == null) {
                deleteEventAction = Components.createComponent(context,
                        DeleteEventAction.COMPONENT_TYPE, DeleteEventAction.class, actionBar, "deleteEventAction");
                result.add(deleteEventAction);
                actionBar.getChildren().add(deleteEventAction);
            }
            deleteEventAction.setOnclick("O$.Timetable._deleteCurrentTimetableEvent(event)");
            Confirmation confirmation = timeTable.getDeletionConfirmation();

            if (confirmation != null) {
                if (deleteEventAction.isRendered() && deleteEventAction.getShowConfirmation()) {
                    confirmation.setRendered(true);
                    if (deleteEventAction.getId() == null)
                        deleteEventAction.setId(context.getViewRoot().createUniqueId());
                    confirmation.setFor(deleteEventAction.getId());
                    confirmation.setEvent("click");
                } else
                    confirmation.setRendered(false);
            }
        }

        return result;
    }

    private JSONArray getEventActions(FacesContext context, EventActionBar actionBar) {
        List<EventAction> actionComponents = getEventActionComponents(context, actionBar);
        JSONArray result = new JSONArray();
        for (EventAction action : actionComponents) {
            try {
                JSONObject actionParams = action.toJSONObject(
                        Collections.singletonMap(EventActionBar.class, actionBar));
                result.put(result.length(), actionParams);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }
}
