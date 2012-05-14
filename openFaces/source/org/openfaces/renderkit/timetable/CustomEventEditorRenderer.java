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

import org.openfaces.component.timetable.CustomEventEditor;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.AnonymousFunction;
import org.openfaces.util.Rendering;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class CustomEventEditorRenderer extends RendererBase {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("span", component);
        writeIdAttribute(context, component);
        writer.writeAttribute("style", "display: none", null);

        CustomEventEditor editor = (CustomEventEditor) component;
        UIComponent timetableView = editor.getParent();

        Rendering.renderInitScript(context, new ScriptBuilder().initScript(context, timetableView, "O$.Timetable._initCustomEventEditor",
                editor,
                new AnonymousFunction(editor.getOncreate(), "timetable", "timetableEvent"),
                new AnonymousFunction(editor.getOnedit(), "timetable", "timetableEvent")).semicolon());
        writer.endElement("span");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

}
