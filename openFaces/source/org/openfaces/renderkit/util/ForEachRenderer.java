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

package org.openfaces.renderkit.util;

import org.openfaces.component.util.ForEach;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Alexey Tarasyuk
 */
public class ForEachRenderer extends RendererBase {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) return;
        super.encodeBegin(context, component);
        ForEach forEach = (ForEach) component;
        forEach.setObjectId(null);
        String wrapperTagName = forEach.getWrapperTagName();
        if (wrapperTagName == null || wrapperTagName.length() == 0)
            return;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = forEach.getClientId(context);
        writer.startElement(wrapperTagName, forEach);
        writer.writeAttribute("id", clientId, null);
        String classStr = Styles.getCSSClass(context, forEach, forEach.getStyle(), forEach.getStyleClass());
        if (classStr != null) {
            writer.writeAttribute("class", classStr, null);
        }
        Styles.renderStyleClasses(context, forEach);
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered())
            return;
        ForEach forEach = (ForEach) component;
        forEach.setObjectId(null);
        while (forEach.hasNext()) {
            forEach.next();
            super.encodeChildren(context, component);
        }
        forEach.setObjectId(null);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) return;
        super.encodeEnd(context, component);
        ForEach forEach = (ForEach) component;
        String wrapperTagName = forEach.getWrapperTagName();
        if (wrapperTagName == null || wrapperTagName.length() == 0)
            return;
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement(wrapperTagName);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
