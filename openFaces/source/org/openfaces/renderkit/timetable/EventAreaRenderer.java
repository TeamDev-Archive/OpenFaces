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

import org.openfaces.component.timetable.AbstractTimetableEvent;
import org.openfaces.component.timetable.EventArea;
import org.openfaces.component.timetable.TimetableEvent;
import org.openfaces.component.timetable.TimetableView;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Rendering;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class EventAreaRenderer extends RendererBase {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        EventArea eventArea = (EventArea) component;
        TimetableView parent = (TimetableView) eventArea.getParent();

        AbstractTimetableEvent event = parent.getEvent();

        if (!(event instanceof TimetableEvent))
            return;
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", eventArea);
        String clientId = eventArea.getClientId(context);
        writer.writeAttribute("id", clientId, null);
        Rendering.writeStandardEvents(writer, eventArea);
        Rendering.writeStyleAndClassAttributes(writer,
                eventArea.getStyle(),
                eventArea.getStyleClass(),
                "o_timetableEventArea");

        Rendering.renderChildren(context, eventArea);

        writer.endElement("div");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }
}
