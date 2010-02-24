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
import org.openfaces.component.timetable.EventEditorPage;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Rendering;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class EventEditorPageRenderer extends RendererBase {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("span", component);
        writeIdAttribute(context, component);
        writer.writeAttribute("style", "display: none", null);

        EventEditorPage eventEditorPage = (EventEditorPage) component;
        DayTable dayTable = (DayTable) eventEditorPage.getParent();
        Rendering.renderInitScript(context, new ScriptBuilder().initScript(context, dayTable, "O$._initEventEditorPage",
                eventEditorPage,
                eventEditorPage.getActionExpression() != null,
                eventEditorPage.getUrl(),
                eventEditorPage.getModeParamName(),
                eventEditorPage.getEventIdParamName(),
                eventEditorPage.getEventStartParamName(),
                eventEditorPage.getEventEndParamName(),
                eventEditorPage.getResourceIdParamName()).semicolon());
        writer.endElement("span");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String clientId = component.getClientId(context) + Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "action";
        if (!requestParameterMap.containsKey(clientId))
            return;
        EventEditorPage eventEditorPage = (EventEditorPage) component;
        FacesEvent event = new ActionEvent(eventEditorPage);
        event.setPhaseId(PhaseId.INVOKE_APPLICATION);
        eventEditorPage.queueEvent(event);
    }
}
