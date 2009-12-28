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