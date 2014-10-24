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
package org.openfaces.renderkit;

import org.openfaces.component.OUICommand;
import org.openfaces.component.ajax.AjaxInitializer;
import org.openfaces.util.FunctionCallScript;
import org.openfaces.util.RawScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */

public class RendererBase extends Renderer {

    protected void writeAttribute(ResponseWriter writer, String name, String value) throws IOException {
        Rendering.writeAttribute(writer, name, value);
    }

    protected String writeIdAttribute(FacesContext context, UIComponent component) throws IOException {
        return Rendering.writeIdAttribute(context, component);
    }

    protected void writeNameAttribute(FacesContext context, UIComponent component) throws IOException {
        Rendering.writeNameAttribute(context, component);
    }

    protected void writeAttribute(ResponseWriter writer, String name, int value, int emptyValue) throws IOException {
        Rendering.writeAttribute(writer, name, value, emptyValue);
    }

    protected boolean writeEventsWithAjaxSupport(FacesContext context, ResponseWriter writer, OUICommand command) throws IOException {
        return Rendering.writeEventsWithAjaxSupport(context, writer, command, null);
    }

    protected boolean writeEventsWithAjaxSupport(
            FacesContext context,
            ResponseWriter writer,
            OUICommand command,
            String submitIfNoAjax
    ) throws IOException {
        return Rendering.writeEventsWithAjaxSupport(context, writer, command, submitIfNoAjax);
    }
}