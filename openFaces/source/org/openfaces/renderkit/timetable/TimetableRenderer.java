/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.renderkit.timetable;

import org.openfaces.component.panel.LayeredPane;
import org.openfaces.component.panel.SubPanel;
import org.openfaces.component.timetable.Timetable;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.ScriptBuilder;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

public class TimetableRenderer extends RendererBase {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Timetable timetable = (Timetable) component;
        writer.startElement("div", timetable);
        Rendering.writeIdAttribute(context, timetable);
        Rendering.writeStyleAndClassAttributes(writer, timetable);
        Rendering.writeStandardEvents(writer, timetable);
        getLayeredPane(timetable).encodeAll(context);

        Rendering.renderInitScript(context, new ScriptBuilder().initScript(context, timetable, "O$.Timetable._init"),
                "timetable/timetable.js");
        writer.endElement("div");


    }

    private LayeredPane getLayeredPane(Timetable timetable) {
        LayeredPane layeredPane = Components.getChildWithClass(timetable, LayeredPane.class, "layeredPane");
        if (layeredPane.getChildCount() == 0) {
            List<UIComponent> children = layeredPane.getChildren();
            children.add(new SubPanel(null, timetable.getMonthView()));
            children.add(new SubPanel(null, timetable.getWeekView()));
            children.add(new SubPanel(null, timetable.getDayView()));
        }
        return layeredPane;
    }
}
