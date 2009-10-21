/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.renderkit;

import org.openfaces.component.OUIComponent;
import org.openfaces.component.OUIInput;
import org.openfaces.util.RenderingUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */

public class RendererBase extends Renderer {

    protected String escapeId(String id) {
        return id.replace(':', '_').replace('-', '_');
    }

    protected void writeStandardEvents(ResponseWriter writer, OUIInput component) throws IOException {
        if (component.isDisabled())
            return;

        writeStandardEvents(writer, (OUIComponent) component);
    }

    protected void writeStandardEvents(ResponseWriter writer, OUIComponent component) throws IOException {
        writeAttribute(writer, "onclick", component.getOnclick());
        writeAttribute(writer, "ondblclick", component.getOndblclick());
        writeAttribute(writer, "onmousedown", component.getOnmousedown());
        writeAttribute(writer, "onmouseup", component.getOnmouseup());
        writeAttribute(writer, "onmousemove", component.getOnmousemove());
        writeAttribute(writer, "onmouseout", component.getOnmouseout());
        writeAttribute(writer, "onmouseover", component.getOnmouseover());

        writeAttribute(writer, "onfocus", component.getOnfocus());
        writeAttribute(writer, "onblur", component.getOnblur());
        writeAttribute(writer, "onkeypress", component.getOnkeypress());
        writeAttribute(writer, "onkeydown", component.getOnkeydown());
        writeAttribute(writer, "onkeyup", component.getOnkeyup());
    }

    protected void writeAttribute(ResponseWriter writer, String name, String value) throws IOException {
        RenderingUtil.writeAttribute(writer, name, value);
    }

    protected String writeIdAttribute(FacesContext facesContext, UIComponent component) throws IOException {
        return RenderingUtil.writeIdAttribute(facesContext, component);
    }

    protected void writeNameAttribute(FacesContext facesContext, UIComponent component) throws IOException {
        RenderingUtil.writeNameAttribute(facesContext, component);
    }

    protected void writeAttribute(ResponseWriter writer, String name, int value, int emptyValue) throws IOException {
        RenderingUtil.writeAttribute(writer, name, value, emptyValue);
    }
}